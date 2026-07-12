package com.caterer.chatbot;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

public class ChatServer {
    private static final int PORT = 8080;
    private static NLPClassifier classifier;
    private static ResponseDatabase db;

    public static void main(String[] args) {
        try {
            System.out.println("🤖 Initializing Omkar Vivah Chatbot Backend...");
            db = new ResponseDatabase();
            classifier = new NLPClassifier();
            
            System.out.println("📖 Loading training database...");
            db.populateTrainingData(classifier);
            
            System.out.println("🧠 Training NLP Cosine-Similarity classifier...");
            classifier.train();
            System.out.printf("   - Trained on %d phrases\n", classifier.getTrainingSize());
            System.out.printf("   - Vocabulary size: %d words\n", classifier.getVocabulary().size());

            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Endpoint paths
            server.createContext("/api/chat", new ChatHandler());
            server.createContext("/api/status", new StatusHandler());
            server.createContext("/api/train", new TrainHandler());

            server.setExecutor(null); // default executor
            server.start();
            
            System.out.println("🚀 ChatServer is running!");
            System.out.println("🔌 API Endpoint: http://localhost:" + PORT + "/api/chat");
            System.out.println("🔌 Status Endpoint: http://localhost:" + PORT + "/api/status");
            System.out.println("💡 Press Ctrl+C to terminate the server.");
        } catch (Exception e) {
            System.err.println("❌ Failed to start ChatServer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper to set standard headers (CORS & JSON Content-Type).
     */
    private static void setStandardHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    /**
     * Handles CORS Pre-flight requests. Returns true if request is OPTIONS.
     */
    private static boolean handlePreflight(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            setStandardHeaders(exchange);
            exchange.sendResponseHeaders(204, -1); // No Content
            return true;
        }
        return false;
    }

    /**
     * Handler for /api/chat POST requests.
     */
    private static class ChatHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;

            setStandardHeaders(exchange);
            
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, 405, "Method Not Allowed. Use POST.");
                return;
            }

            try {
                // Read request body
                InputStream is = exchange.getRequestBody();
                String body = readStream(is);
                
                // Parse message from json manually to avoid external dependencies
                String userMessage = parseMessageFromJson(body);
                if (userMessage.isEmpty()) {
                    sendError(exchange, 400, "Bad Request: JSON body must contain a non-empty 'message' field.");
                    return;
                }

                System.out.println("📥 Received query: \"" + userMessage + "\"");

                // Classify intent
                NLPClassifier.ClassificationResult result = classifier.classify(userMessage);
                
                // Generate response
                String botResponse = db.getResponse(result.intent, userMessage);
                System.out.println("📤 Matched intent: [" + result.intent + "] (Conf: " + String.format("%.2f", result.confidence) + ")");

                // Build response JSON string
                String responseJson = String.format(
                    "{\"response\":\"%s\",\"intent\":\"%s\",\"confidence\":%.4f}",
                    escapeJsonString(botResponse),
                    result.intent,
                    result.confidence
                );

                byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            } catch (Exception e) {
                System.err.println("❌ Error processing chat request: " + e.getMessage());
                sendError(exchange, 500, "Internal Server Error: " + e.getMessage());
            }
        }
    }

    /**
     * Handler for /api/status GET requests.
     */
    private static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;

            setStandardHeaders(exchange);
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, 405, "Method Not Allowed. Use GET.");
                return;
            }

            String responseJson = String.format(
                "{\"status\":\"online\",\"trainedInstances\":%d,\"vocabularySize\":%d}",
                classifier.getTrainingSize(),
                classifier.getVocabulary().size()
            );

            byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, responseBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(responseBytes);
            os.close();
        }
    }

    /**
     * Handler for /api/train GET requests (forces classifier re-training).
     */
    private static class TrainHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handlePreflight(exchange)) return;

            setStandardHeaders(exchange);
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendError(exchange, 405, "Method Not Allowed. Use GET.");
                return;
            }

            try {
                classifier.train();
                String responseJson = String.format(
                    "{\"status\":\"success\",\"message\":\"Classifier successfully re-trained.\",\"trainedInstances\":%d,\"vocabularySize\":%d}",
                    classifier.getTrainingSize(),
                    classifier.getVocabulary().size()
                );
                byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            } catch (Exception e) {
                sendError(exchange, 500, "Failed to re-train: " + e.getMessage());
            }
        }
    }

    // Helper functions

    private static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private static String parseMessageFromJson(String json) {
        if (json == null || json.isEmpty()) return "";
        // Match "message" : "value"
        Pattern pattern = Pattern.compile("\"message\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return decodeJsonString(matcher.group(1));
        }
        return "";
    }

    private static String decodeJsonString(String str) {
        return str.replace("\\\"", "\"")
                  .replace("\\n", "\n")
                  .replace("\\t", "\t")
                  .replace("\\\\", "\\");
    }

    private static String escapeJsonString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    private static void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        String errorJson = String.format("{\"error\":\"%s\"}", escapeJsonString(message));
        byte[] responseBytes = errorJson.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }
}
