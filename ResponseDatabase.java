package com.caterer.chatbot;

import java.util.*;
import java.util.regex.*;

public class ResponseDatabase {

    public static class PackageInfo {
        public final String event;
        public final String tier;
        public final int vegPrice;
        public final int nonVegPrice; // -1 if not available
        public final int minGuests;

        public PackageInfo(String event, String tier, int vegPrice, int nonVegPrice, int minGuests) {
            this.event = event;
            this.tier = tier;
            this.vegPrice = vegPrice;
            this.nonVegPrice = nonVegPrice;
            this.minGuests = minGuests;
        }

        public String getFullName() {
            return event.substring(0, 1).toUpperCase() + event.substring(1) + " " + tier;
        }
    }

    private final List<PackageInfo> packages = new ArrayList<>();
    private final Map<String, List<String>> responses = new HashMap<>();

    public ResponseDatabase() {
        initPackages();
        initResponses();
    }

    private void initPackages() {
        // Haldi & Muhurt
        packages.add(new PackageInfo("haldi", "Essential", 80, -1, 50));
        packages.add(new PackageInfo("haldi", "Premium", 120, 160, 75));
        packages.add(new PackageInfo("haldi", "Royal", 150, 200, 100));

        // Sangeet & Mehendi
        packages.add(new PackageInfo("sangeet", "Essential", 120, 180, 100));
        packages.add(new PackageInfo("sangeet", "Premium", 160, 220, 150));
        packages.add(new PackageInfo("sangeet", "Royal", 200, 280, 200));

        // Engagement / Roka
        packages.add(new PackageInfo("engagement", "Essential", 100, 160, 50));
        packages.add(new PackageInfo("engagement", "Premium", 140, 200, 75));
        packages.add(new PackageInfo("engagement", "Royal", 180, 250, 100));

        // Wedding / Pheras
        packages.add(new PackageInfo("wedding", "Essential", 100, 200, 150));
        packages.add(new PackageInfo("wedding", "Premium", 150, 250, 200));
        packages.add(new PackageInfo("wedding", "Royal", 200, 350, 300));

        // Reception
        packages.add(new PackageInfo("reception", "Essential", 150, 250, 200));
        packages.add(new PackageInfo("reception", "Premium", 200, 300, 300));
        packages.add(new PackageInfo("reception", "Royal", 280, 400, 500));

        // Vidai
        packages.add(new PackageInfo("vidai", "Essential", 90, 140, 30));
        packages.add(new PackageInfo("vidai", "Premium", 120, 180, 50));
        packages.add(new PackageInfo("vidai", "Royal", 150, 220, 75));

        // Anniversary
        packages.add(new PackageInfo("anniversary", "Essential", 120, 200, 50));
        packages.add(new PackageInfo("anniversary", "Premium", 180, 280, 75));
        packages.add(new PackageInfo("anniversary", "Royal", 250, 380, 100));

        // Destination
        packages.add(new PackageInfo("destination", "Essential", 180, 280, 100));
        packages.add(new PackageInfo("destination", "Premium", 250, 380, 150));
        packages.add(new PackageInfo("destination", "Royal", 350, 500, 200));
    }

    private void initResponses() {
        responses.put("greeting", Arrays.asList(
            "Namaste! 🙏 Welcome to Omkar Vivah Caterers. I'm your wedding catering assistant. How can I help you plan your special feast today?",
            "Hello! Warm greetings from Omkar Vivah. How can I assist you with your wedding or event menu planning today? 😊",
            "Namaste! Glad to connect with you. I can assist you with event packages, pricing, customized quotes, or scheduling a tasting session!"
        ));

        responses.put("goodbye", Arrays.asList(
            "Thank you for chatting! We hope to make your wedding feast truly royal. Have a wonderful day ahead! 🌸",
            "Goodbye! Please feel free to reach out again or complete the booking form below to secure your date. Namaste! 🙏",
            "Thank you! If you need anything else, just drop a message. Looking forward to serving your wedding guests!"
        ));

        responses.put("help", Arrays.asList(
            "I can help you with:\n\n• **Packages & Pricing**: Ask about Haldi, Sangeet, Engagement, Wedding, or Reception packages.\n• **Instant Catering Quotes**: Try asking *'estimate for 250 guests Sangeet Premium'*.\n• **Dietary Information**: Ask about Veg/Non-Veg options and purity standards.\n• **Booking/Tastings**: Find out how to book a free food tasting session."
        ));

        responses.put("packages", Arrays.asList(
            "We offer customized catering packages for 8 event types:\n\n" +
            "1. **Haldi & Muhurt**: ₹80 to ₹150 per plate (Veg/Non-Veg)\n" +
            "2. **Sangeet & Mehendi**: ₹120 to ₹280 per plate\n" +
            "3. **Engagement / Roka**: ₹100 to ₹250 per plate\n" +
            "4. **Wedding / Pheras**: ₹100 to ₹350 per plate\n" +
            "5. **Reception Banquet**: ₹150 to ₹400 per plate\n" +
            "6. **Vidai**: ₹90 to ₹220 per plate\n" +
            "7. **Anniversary**: ₹120 to ₹380 per plate\n" +
            "8. **Destination Wedding**: ₹180 to ₹500 per plate\n\n" +
            "Which event package are you planning for? You can ask details about any specific package (e.g. *'Tell me about Sangeet Premium'*)."
        ));

        responses.put("booking_info", Arrays.asList(
            "Booking a catering package with Omkar Vivah is simple:\n" +
            "1. **Schedule a Tasting**: Click the 'Book Wedding Tasting' button above to select your menu options.\n" +
            "2. **Quote & Finalization**: We'll review your guest count, customize the dishes, and finalize the date.\n" +
            "3. **Secure Date**: Pay a 25% booking advance to secure our services for your wedding date.\n\n" +
            "Would you like to visit our office in Pune, or shall I guide you to submit an inquiry right here?",
            "To book us for your wedding, scroll down to our 'Book Us' form, fill in your details, and submit! Our wedding coordinator will call you back within 2 hours to set up a free tasting session."
        ));

        responses.put("veg_nonveg", Arrays.asList(
            "🌿 **100% Purity and Separation**: At Omkar Vivah, we maintain absolute hygiene and segregation. We use entirely separate kitchens, utensils, and service staff for Vegetarian and Non-Vegetarian food preparations.\n\n" +
            "We serve classic Maharashtrian vegetarian feasts, royal North Indian menus, Mughlai non-veg specials, mocktail bars, live chaat, and international food counters.",
            "Yes, we provide both Vegetarian and Non-Vegetarian menus! We operate dedicated, separate preparation lines to maintain the highest purity and respect dietary preferences."
        ));

        responses.put("min_guests", Arrays.asList(
            "Our minimum guest count varies by package, ranging from **30 guests** for Vidai packages, **50 guests** for Haldi/Engagement, to **150-300 guests** for Sangeet, Wedding, and Reception events. \n\nWhat is your expected guest list size? I can recommend packages that suit your event!"
        ));

        responses.put("location", Arrays.asList(
            "📍 Omkar Vivah Caterers is proudly based in **Pune, Maharashtra**. We provide complete catering services all across Pune city, PCMC, and destination weddings throughout Maharashtra (like Lonavala, Mahabaleshwar, Alibaug, and Mulshi).",
            "We are located in Pune, but our destination packages cover catering across the state of Maharashtra. We bring our full kitchen team and logistics straight to your wedding venue!"
        ));

        responses.put("about_us", Arrays.asList(
            "✨ **Omkar Vivah Caterers** has been Pune's premier wedding and Shaadi caterer since 2006. We have successfully catered over **1,200+ weddings** and celebrations.\n\nWe are known for our authentic Maharashtrian wedding thalis, royal North Indian buffets, interactive live counters, and high-end 5-star hospitality."
        ));
    }

    /**
     * Feeds the NLPClassifier with training examples for each intent.
     */
    public void populateTrainingData(NLPClassifier classifier) {
        // Greeting training phrases
        classifier.addTrainingPhrase("greeting", "hi");
        classifier.addTrainingPhrase("greeting", "hello");
        classifier.addTrainingPhrase("greeting", "namaste");
        classifier.addTrainingPhrase("greeting", "hey there");
        classifier.addTrainingPhrase("greeting", "anyone there");
        classifier.addTrainingPhrase("greeting", "good morning");
        classifier.addTrainingPhrase("greeting", "good afternoon");

        // Goodbye training phrases
        classifier.addTrainingPhrase("goodbye", "bye");
        classifier.addTrainingPhrase("goodbye", "goodbye");
        classifier.addTrainingPhrase("goodbye", "see you later");
        classifier.addTrainingPhrase("goodbye", "thanks bye");
        classifier.addTrainingPhrase("goodbye", "no more questions");

        // Help training phrases
        classifier.addTrainingPhrase("help", "help");
        classifier.addTrainingPhrase("help", "what can you do");
        classifier.addTrainingPhrase("help", "how does this work");
        classifier.addTrainingPhrase("help", "info");
        classifier.addTrainingPhrase("help", "list options");
        classifier.addTrainingPhrase("help", "support");

        // Packages training phrases
        classifier.addTrainingPhrase("packages", "packages");
        classifier.addTrainingPhrase("packages", "what events do you cater");
        classifier.addTrainingPhrase("packages", "what are the menus");
        classifier.addTrainingPhrase("packages", "pricing details");
        classifier.addTrainingPhrase("packages", "show me packages");
        classifier.addTrainingPhrase("packages", "menu plans");
        classifier.addTrainingPhrase("packages", "list of packages");

        // Booking info training phrases
        classifier.addTrainingPhrase("booking_info", "how to book");
        classifier.addTrainingPhrase("booking_info", "make a reservation");
        classifier.addTrainingPhrase("booking_info", "tasting session");
        classifier.addTrainingPhrase("booking_info", "booking advance");
        classifier.addTrainingPhrase("booking_info", "i want to book you");
        classifier.addTrainingPhrase("booking_info", "hire caterer");
        classifier.addTrainingPhrase("booking_info", "schedule a call");

        // Veg/Non-veg training phrases
        classifier.addTrainingPhrase("veg_nonveg", "veg");
        classifier.addTrainingPhrase("veg_nonveg", "non veg");
        classifier.addTrainingPhrase("veg_nonveg", "vegetarian");
        classifier.addTrainingPhrase("veg_nonveg", "do you serve chicken");
        classifier.addTrainingPhrase("veg_nonveg", "separate kitchens");
        classifier.addTrainingPhrase("veg_nonveg", "mutton fish");
        classifier.addTrainingPhrase("veg_nonveg", "pure veg");

        // Min guests training phrases
        classifier.addTrainingPhrase("min_guests", "minimum guests");
        classifier.addTrainingPhrase("min_guests", "how many plates minimum");
        classifier.addTrainingPhrase("min_guests", "small wedding 50 guests");
        classifier.addTrainingPhrase("min_guests", "guest limit");
        classifier.addTrainingPhrase("min_guests", "min capacity");

        // Location training phrases
        classifier.addTrainingPhrase("location", "location");
        classifier.addTrainingPhrase("location", "where are you based");
        classifier.addTrainingPhrase("location", "address");
        classifier.addTrainingPhrase("location", "pune caterers");
        classifier.addTrainingPhrase("location", "do you travel to lonavala");
        classifier.addTrainingPhrase("location", "office location");

        // About us training phrases
        classifier.addTrainingPhrase("about_us", "who are you");
        classifier.addTrainingPhrase("about_us", "tell me about omkar vivah");
        classifier.addTrainingPhrase("about_us", "since when catering");
        classifier.addTrainingPhrase("about_us", "experience");
        classifier.addTrainingPhrase("about_us", "number of weddings");

        // Quote request training phrases
        classifier.addTrainingPhrase("quote_request", "quote");
        classifier.addTrainingPhrase("quote_request", "estimate");
        classifier.addTrainingPhrase("quote_request", "price list");
        classifier.addTrainingPhrase("quote_request", "how much for guests");
        classifier.addTrainingPhrase("quote_request", "cost calculator");
        classifier.addTrainingPhrase("quote_request", "calculate budget");
        classifier.addTrainingPhrase("quote_request", "wedding cost for 200 plates");
    }

    /**
     * Resolves the response for a matched intent, incorporating dynamic entity calculations if needed.
     */
    public String getResponse(String intent, String userMessage) {
        // If the intent is quote_request, or we spot guest/package entities in general, calculate it!
        if (intent.equals("quote_request") || containsQuoteEntities(userMessage)) {
            return calculateCustomQuote(userMessage);
        }

        List<String> list = responses.get(intent);
        if (list == null || list.isEmpty()) {
            return "I'm sorry, I didn't quite catch that. Could you please rephrase or try asking about our event packages, veg/non-veg policy, or type 'help' for options?";
        }

        // Return a random response from the available list to make the conversation feel dynamic
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    /**
     * Returns true if user input contains guest counts or package keywords.
     */
    private boolean containsQuoteEntities(String msg) {
        String lower = msg.toLowerCase();
        boolean hasGuests = lower.matches(".*\\b\\d{2,4}\\b.*");
        boolean hasPackageKeywords = lower.contains("estimate") || lower.contains("quote") || lower.contains("cost") || 
                                     lower.contains("price") || lower.contains("how much") || lower.contains("plate");
        return hasGuests && hasPackageKeywords;
    }

    /**
     * Parses guest count and event package from query to calculate custom quote dynamically.
     */
    private String calculateCustomQuote(String msg) {
        String lower = msg.toLowerCase();

        // 1. Extract guest count (find 2 to 4 digit integers)
        int guestCount = -1;
        Pattern guestPattern = Pattern.compile("\\b(\\d{2,4})\\b");
        Matcher matcher = guestPattern.matcher(msg);
        if (matcher.find()) {
            guestCount = Integer.parseInt(matcher.group(1));
        }

        if (guestCount == -1) {
            return "I see you are asking for a quote. Please let me know **how many guests** you are expecting, and which **package** you prefer (e.g. *'Estimate for 200 guests Sangeet Premium'*).";
        }

        // 2. Identify the target event package
        PackageInfo selectedPkg = null;
        for (PackageInfo pkg : packages) {
            String eventName = pkg.event;
            String tierName = pkg.tier.toLowerCase();
            
            // Check if user text contains both the event name and the tier name
            if (lower.contains(eventName) && lower.contains(tierName)) {
                selectedPkg = pkg;
                break;
            }
        }

        // Fallback package search: check just event name, pick "Premium" as default
        if (selectedPkg == null) {
            for (PackageInfo pkg : packages) {
                if (lower.contains(pkg.event) && pkg.tier.equalsIgnoreCase("Premium")) {
                    selectedPkg = pkg;
                    break;
                }
            }
        }

        // If still no event found, check for wedding keyword
        if (selectedPkg == null && (lower.contains("wedding") || lower.contains("marriage") || lower.contains("shaadi"))) {
            for (PackageInfo pkg : packages) {
                if (pkg.event.equals("wedding") && pkg.tier.equalsIgnoreCase("Premium")) {
                    selectedPkg = pkg;
                    break;
                }
            }
        }

        if (selectedPkg == null) {
            return String.format(
                "I detected a guest count of **%d guests**, but I'm not sure which package you want to estimate. " +
                "Could you specify the event? (e.g., *'Wedding Premium'*, *'Sangeet Royal'*, or *'Haldi Essential'*).\n\n" +
                "Here are standard packages you can choose from:\n" +
                "• **Wedding Premium**: ₹150 (Veg) / ₹250 (Non-Veg)\n" +
                "• **Sangeet Royal**: ₹200 (Veg) / ₹280 (Non-Veg)\n" +
                "• **Engagement Premium**: ₹140 (Veg) / ₹200 (Non-Veg)", 
                guestCount
            );
        }

        // 3. Compute Quotation
        StringBuilder reply = new StringBuilder();
        reply.append("📊 **Catering Budget Estimate (Omkar Vivah)**\n\n");
        reply.append(String.format("• **Selected Package**: %s\n", selectedPkg.getFullName()));
        reply.append(String.format("• **Guest Count**: %d guests\n", guestCount));
        
        // Check minimum guest constraint
        if (guestCount < selectedPkg.minGuests) {
            reply.append(String.format("⚠️ *Note: The minimum guests required for %s is %d. Your request (%d) is below this. We will apply rates for the minimum limit of %d guests if booked.*\n\n", 
                selectedPkg.getFullName(), selectedPkg.minGuests, guestCount, selectedPkg.minGuests));
        }

        // Calculate pricing
        int calculationGuests = Math.max(guestCount, selectedPkg.minGuests);
        
        // Veg calculations
        int vegSubtotal = calculationGuests * selectedPkg.vegPrice;
        int vegGst = (int)Math.round(vegSubtotal * 0.18);
        int vegTotal = vegSubtotal + vegGst;
        
        reply.append(String.format("🌿 **Vegetarian Menu Quote**:\n"));
        reply.append(String.format("  - Base Rate: ₹%d / plate\n", selectedPkg.vegPrice));
        reply.append(String.format("  - Subtotal: ₹%,d\n", vegSubtotal));
        reply.append(String.format("  - GST (18%%): ₹%,d\n", vegGst));
        reply.append(String.format("  - **Total Cost**: **₹%,d**\n\n", vegTotal));

        // Non-veg calculations (if available)
        if (selectedPkg.nonVegPrice != -1) {
            int nvSubtotal = calculationGuests * selectedPkg.nonVegPrice;
            int nvGst = (int)Math.round(nvSubtotal * 0.18);
            int nvTotal = nvSubtotal + nvGst;

            reply.append(String.format("🍖 **Non-Vegetarian Menu Quote**:\n"));
            reply.append(String.format("  - Base Rate: ₹%d / plate\n", selectedPkg.nonVegPrice));
            reply.append(String.format("  - Subtotal: ₹%,d\n", nvSubtotal));
            reply.append(String.format("  - GST (18%%): ₹%,d\n", nvGst));
            reply.append(String.format("  - **Total Cost**: **₹%,d**\n\n", nvTotal));
        } else {
            reply.append("🍖 *Non-Vegetarian menu is not available for this specific breakfast package (Haldi Essential is Veg only).*\n\n");
        }

        reply.append("✨ **Includes**:\n");
        if (selectedPkg.event.equals("wedding")) {
            reply.append("  ✔ Buffet setup, service staff, chaffing dishes, premium cutlery, and welcome drinks.\n");
        } else if (selectedPkg.event.equals("sangeet")) {
            reply.append("  ✔ Live counters (chaat/tandoor), mocktail bar setup, and serving staff.\n");
        } else {
            reply.append("  ✔ Standard layout setup, service staff, and cleanup.\n");
        }

        reply.append("\nTo book a **free tasting session** or customize this menu, scroll down to fill out the form or tell me: *'How do I book?'*");

        return reply.toString();
    }
}
