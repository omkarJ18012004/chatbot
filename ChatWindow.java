package com.caterer.chatbot;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatWindow extends JFrame {
    private JTextPane chatLog;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    
    private NLPClassifier classifier;
    private ResponseDatabase db;
    private StringBuilder chatHistoryHtml;

    public ChatWindow() {
        // Initialize NLP Engine
        System.out.println("🤖 Starting Desktop NLP Engine...");
        db = new ResponseDatabase();
        classifier = new NLPClassifier();
        db.populateTrainingData(classifier);
        classifier.train();
        
        chatHistoryHtml = new StringBuilder();
        
        // Setup Window Layout
        setTitle("Omkar Vivah - AI Catering Assistant");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center window
        
        // Create components
        initUI();
        
        // Send initial greeting
        appendBotMessage("Namaste! 🙏 I am your Omkar Vivah Catering Assistant. How can I help you customize your wedding menu or estimate a catering budget today?");
    }

    private void initUI() {
        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(253, 251, 247)); // Soft Cream

        // 1. Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(223, 88, 45)); // Saffron
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        JLabel titleLabel = new JLabel("Omkar Vivah Assistant");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subLabel = new JLabel("Online • Native Desktop AI");
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subLabel.setForeground(new Color(253, 220, 208)); // Pale saffron
        
        JPanel titleTextPanel = new JPanel(new GridLayout(2, 1));
        titleTextPanel.setOpaque(false);
        titleTextPanel.add(titleLabel);
        titleTextPanel.add(subLabel);
        
        headerPanel.add(titleTextPanel, BorderLayout.CENTER);
        
        // Avatar icon label
        JLabel avatarLabel = new JLabel("👩‍💼 ");
        avatarLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
        headerPanel.add(avatarLabel, BorderLayout.WEST);

        // 2. Chat Log Area (HTML Supported Pane)
        chatLog = new JTextPane();
        chatLog.setEditable(false);
        chatLog.setContentType("text/html");
        
        // Setup HTMLEditorKit for styling
        HTMLEditorKit kit = new HTMLEditorKit();
        chatLog.setEditorKit(kit);
        HTMLDocument doc = (HTMLDocument) chatLog.getDocument();
        
        // Add styling rules for bubbles
        String styleSheet = "body { font-family: 'Segoe UI', Arial, sans-serif; font-size: 12px; background-color: #fdfbf7; margin: 10px; }" +
                            ".msg-row-user { text-align: right; margin-bottom: 12px; }" +
                            ".msg-row-bot { text-align: left; margin-bottom: 12px; }" +
                            ".msg-bubble-user { display: inline-block; text-align: left; background: #df582d; color: white; padding: 8px 12px; border-radius: 12px; border-top-right-radius: 2px; max-width: 75%; }" +
                            ".msg-bubble-bot { display: inline-block; text-align: left; background: white; color: #2a2e35; border: 1px solid #dfc28d; padding: 8px 12px; border-radius: 12px; border-top-left-radius: 2px; max-width: 75%; }" +
                            "strong { color: #b08b43; font-weight: bold; }" +
                            "ul { margin-top: 5px; margin-bottom: 5px; padding-left: 18px; }" +
                            "li { margin-bottom: 3px; }";
        kit.getStyleSheet().addRule(styleSheet);

        JScrollPane scrollPane = new JScrollPane(chatLog);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(197, 160, 89, 80), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 3. Input Form Panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setOpaque(false);
        
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(197, 160, 89, 120), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Send and Clear buttons
        sendButton = new JButton("Send");
        sendButton.setBackground(new Color(223, 88, 45));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        sendButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(246, 243, 235));
        clearButton.setForeground(new Color(176, 139, 67));
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        clearButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(clearButton);
        buttonPanel.add(sendButton);
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Assemble Main Layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        add(mainPanel);

        // Attach Listeners
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitMessage();
            }
        });

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitMessage();
                }
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatHistoryHtml.setLength(0);
                chatLog.setText("");
                appendBotMessage("Chat history cleared. How can I help you plan your catering event today?");
            }
        });
    }

    private void submitMessage() {
        String userText = inputField.getText().trim();
        if (userText.isEmpty()) return;
        
        // Render user message in GUI
        appendUserMessage(userText);
        inputField.setText("");
        
        // Perform local NLP classification
        NLPClassifier.ClassificationResult result = classifier.classify(userText);
        String botResponse = db.getResponse(result.intent, userText);
        
        // Render response
        appendBotMessage(botResponse);
    }

    private void appendUserMessage(String message) {
        String escaped = escapeHtml(message);
        chatHistoryHtml.append("<div class='msg-row-user'><div class='msg-bubble-user'>")
                      .append(escaped)
                      .append("</div></div>");
        refreshChatLog();
    }

    private void appendBotMessage(String message) {
        String formatted = formatMarkdownToHtml(message);
        chatHistoryHtml.append("<div class='msg-row-bot'><div class='msg-bubble-bot'>")
                      .append(formatted)
                      .append("</div></div>");
        refreshChatLog();
    }

    private void refreshChatLog() {
        chatLog.setText("<html><body>" + chatHistoryHtml.toString() + "</body></html>");
        
        // Auto-scroll scrollpane to bottom
        SwingUtilities.invokeLater(() -> {
            chatLog.setCaretPosition(chatLog.getDocument().getLength());
        });
    }

    private String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private String formatMarkdownToHtml(String text) {
        if (text == null) return "";
        // Convert Markdown formatting (bolding, lists, breaks) to HTML tags for Swing rendering
        String html = text
            .replace("\n", "<br>")
            .replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>") // **bold**
            .replaceAll("•\\s+(.*?)(<br>|$)", "<li>$1</li>") // bullet lists
            .replaceAll("✔\\s+(.*?)(<br>|$)", "<li>✅ $1</li>"); // inclusion checks
        
        if (html.contains("<li>")) {
            // Group bullet points inside simple list tags
            html = html.replace("<li>", "<ul><li>").replace("</li><br>", "</li>").replace("</li>", "</li></ul>");
            // Fix double list nests
            html = html.replace("</ul><ul>", "");
        }
        return html;
    }

    public static void main(String[] args) {
        // Adjust Swing Theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new ChatWindow().setVisible(true);
        });
    }
}
