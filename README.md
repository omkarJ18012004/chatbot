# Omkar Vivah Chatbot 🤖
A Java-based intelligent catering assistant chatbot designed for **Omkar Vivah Caterers**. This chatbot uses a custom-built Natural Language Processing (NLP) classifier to understand user queries, provide package pricing, compute instant custom catering estimates, and guide users through booking a catering service.
## 🌟 Features
* **Custom NLP Classification:** Built from scratch in pure Java (using a TF-IDF Vector Space Model) to classify intents without requiring external heavy NLP frameworks.
* **Instant Catering Quotes:** Dynamically calculates estimates for queries like *"estimate for 250 guests Sangeet Premium"*.
* **Comprehensive Event Coverage:** Ready-to-use information for 8 distinct event types:
  * Haldi & Muhurt
  * Sangeet & Mehendi
  * Engagement / Roka
  * Wedding / Pheras
  * Reception
  * Vidai
  * Anniversary
  * Destination Wedding
* **Multiple Interfaces:** Includes both a backend socket-based `ChatServer` and a desktop GUI application `ChatWindow` for interactive chatting.
---
## 📂 Project Structure
```
chatbot/
├── run.bat              # Script to compile and run the backend ChatServer
├── run-gui.bat          # Script to compile and run the desktop ChatWindow GUI
├── README.md            # Project documentation (this file)
└── src/main/java/com/caterer/chatbot/
    ├── ChatServer.java      # Socket server handling client connections
    ├── ChatWindow.java      # Swing-based desktop user interface
    ├── NLPClassifier.java  # Vector space model TF-IDF classifier
    └── ResponseDatabase.java # FAQ database & estimating logic
```
---
## 🚀 Getting Started
### Prerequisites
* Java Development Kit (JDK) 8 or higher installed.
* Command line accessibility (`java` and `javac` commands configured in your PATH environment variable).
### Running the Project
#### 1. Start the Chat Server (Backend)
Double-click `run.bat` or execute it from the terminal. This compiles the Java code and starts the socket server:
```bash
run.bat
```
#### 2. Start the Chat Window (GUI Client)
Double-click `run-gui.bat` or run it from the terminal to launch the desktop application:
```bash
run-gui.bat
```
---
## 💡 Example Queries to Try
* *"Hello"* / *"Namaste"*
* *"What wedding packages do you offer?"*
* *"Estimate for 200 guests Reception Royal"*
* *"Can I book a free food tasting session?"*
* *"How can I contact you?"*

Git
