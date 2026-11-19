// Part 3 - Message class - FIXED JSON array + Part 3 arrays
// Fixes Part 2 feedback: JSON now a proper array, disregarded kept in array
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String status;

    private static int totalMessagesSent = 0;

    // PART 3 - Arrays populated from user input + JSON
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages = new ArrayList<>();

    // Keep old list for printMessages() compatibility
    private static final ArrayList<Message> messages = sentMessages;

    public Message(String recipient, String message, int messageNumber) {
        this.messageID = String.valueOf(
                ThreadLocalRandom.current().nextLong(
                        1_000_000_000L, 10_000_000_000L));
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    public Message(String messageID, String recipient,
                   String message, int messageNumber) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    // For loading from JSON - keeps hash as stored
    public Message(String messageID, String messageHash, String recipient, String message, String status, int messageNumber) {
        this.messageID = messageID;
        this.messageHash = messageHash;
        this.recipient = recipient;
        this.message = message;
        this.status = status;
        this.messageNumber = messageNumber;
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{1,10}");
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        String firstTwoDigits = messageID.substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0].replaceAll("^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");
        String lastWord = words[words.length - 1].replaceAll("^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");
        return (firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String checkMessageLength() {
        if (message == null) return "Please enter a message.";
        if (message.length() > 250) {
            return "Message exceeds 250 characters by " + (message.length() - 250) + "; please reduce the size.";
        }
        return "Message ready to send.";
    }

    public String SentMessage(int choice) {
        if (!checkMessageID()
                || !checkRecipientCell().equals("Cell phone number successfully captured.")
                || !checkMessageLength().equals("Message ready to send.")) {
            return "Message could not be processed.";
        }
        switch (choice) {
            case 1:
                status = "Sent";
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2:
                status = "Disregarded";
                disregardedMessages.add(this); // FIX: was lost before
                return "Press 0 to delete the message.";
            case 3:
                status = "Stored";
                storedMessages.add(this);
                storeMessage(); // writes full JSON array
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public String printMessages() {
        StringBuilder output = new StringBuilder();
        for (Message m : sentMessages) {
            output.append("Message ID: ").append(m.messageID)
                    .append("\nMessage Hash: ").append(m.messageHash)
                    .append("\nRecipient: ").append(m.recipient)
                    .append("\nMessage: ").append(m.message).append("\n\n");
        }
        if (output.length() == 0) return "No messages have been sent yet.";
        return output.toString();
    }

    public int returnTotalMessagess() { return totalMessagesSent; }

    // ===== PART 3 - GETTERS FOR ARRAYS =====
    public static ArrayList<Message> getSentMessages() { return sentMessages; }
    public static ArrayList<Message> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Message> getStoredMessages() { return storedMessages; }

    public static String[] getSentMessagesArray() {
        String[] arr = new String[sentMessages.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = sentMessages.get(i).getMessage();
        return arr;
    }
    public static String[] getDisregardedMessagesArray() {
        String[] arr = new String[disregardedMessages.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = disregardedMessages.get(i).getMessage();
        return arr;
    }
    public static String[] getStoredMessagesArray() {
        String[] arr = new String[storedMessages.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = storedMessages.get(i).getMessage();
        return arr;
    }
    public static String[] getAllHashesArray() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages); all.addAll(disregardedMessages); all.addAll(storedMessages);
        String[] arr = new String[all.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = all.get(i).getMessageHash();
        return arr;
    }
    public static String[] getAllIDsArray() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages); all.addAll(disregardedMessages); all.addAll(storedMessages);
        String[] arr = new String[all.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = all.get(i).getMessageID();
        return arr;
    }

    // (a) Sender + recipient of stored - used by Main
    public static String displayStoredSendersRecipients() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Recipient: ").append(m.recipient).append(" | Message: ").append(m.message).append("\n");
        }
        return sb.toString();
    }

    // (b) Longest stored message
    public static String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.getMessage().length() > longest.getMessage().length()) longest = m;
        }
        return longest.getMessage();
    }

    // (c) Search by Message ID
    public static Message searchByMessageID(String id) {
        for (Message m : sentMessages) if (m.getMessageID().equals(id)) return m;
        for (Message m : storedMessages) if (m.getMessageID().equals(id)) return m;
        for (Message m : disregardedMessages) if (m.getMessageID().equals(id)) return m;
        return null;
    }

    // (d) Search by recipient
    public static ArrayList<Message> searchByRecipient(String recipient) {
        ArrayList<Message> result = new ArrayList<>();
        for (Message m : sentMessages) if (m.getRecipient().equals(recipient)) result.add(m);
        for (Message m : storedMessages) if (m.getRecipient().equals(recipient)) result.add(m);
        for (Message m : disregardedMessages) if (m.getRecipient().equals(recipient)) result.add(m);
        return result;
    }

    // (e) Delete by Hash
    public static String deleteMessageByHash(String hash) {
        for (Message m : new ArrayList<>(storedMessages)) {
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                storedMessages.remove(m);
                saveAllStoredToJson();
                return "Message: " + m.getMessage() + " successfully deleted.";
            }
        }
        for (Message m : new ArrayList<>(sentMessages)) {
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                sentMessages.remove(m);
                return "Message: " + m.getMessage() + " successfully deleted.";
            }
        }
        return "Message with hash " + hash + " not found.";
    }

    // (f) Full report
    public static String displayFullReport() {
        ArrayList<Message> all = new ArrayList<>();
        all.addAll(sentMessages); all.addAll(storedMessages); all.addAll(disregardedMessages);
        if (all.isEmpty()) return "No messages to display.";
        StringBuilder sb = new StringBuilder();
        for (Message m : all) {
            sb.append("Hash: ").append(m.getMessageHash())
              .append(" | Recipient: ").append(m.recipient)
              .append(" | Message: ").append(m.message).append("\n");
        }
        return sb.toString();
    }

    // ===== JSON - FIXED: proper array =====
    public void storeMessage() {
        saveAllStoredToJson();
    }

    private static void saveAllStoredToJson() {
        try (FileWriter writer = new FileWriter("stored_messages.json", false)) {
            writer.write("[\n");
            for (int i = 0; i < storedMessages.size(); i++) {
                Message m = storedMessages.get(i);
                writer.write("  {\"messageID\":\"" + escapeJsonStatic(m.messageID) + "\","
                        + "\"messageHash\":\"" + escapeJsonStatic(m.messageHash) + "\","
                        + "\"recipient\":\"" + escapeJsonStatic(m.recipient) + "\","
                        + "\"message\":\"" + escapeJsonStatic(m.message) + "\","
                        + "\"status\":\"" + escapeJsonStatic(m.status) + "\"}");
                if (i < storedMessages.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    // Load stored messages from JSON file into array
    public static void loadStoredMessagesFromJson() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("stored_messages.json"))).trim();
            if (content.isEmpty() || content.equals("[]")) return;
            storedMessages.clear();
            // Split objects
            String[] parts = content.split("\\{");
            for (String p : parts) {
                if (!p.contains("messageID")) continue;
                String id = extractJson(p, "messageID");
                String hash = extractJson(p, "messageHash");
                String rec = extractJson(p, "recipient");
                String msg = extractJson(p, "message");
                String st = extractJson(p, "status");
                if (id != null) storedMessages.add(new Message(id, hash, rec, msg, st, 0));
            }
        } catch (IOException e) {
            // No file yet - ignore
        }
    }

    private static String extractJson(String text, String key) {
        String search = "\"" + key + "\":\"";
        int start = text.indexOf(search);
        if (start < 0) return null;
        start += search.length();
        int end = text.indexOf("\"", start);
        if (end < 0) return null;
        return text.substring(start, end).replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private String escapeJson(String value) { return escapeJsonStatic(value); }
    private static String escapeJsonStatic(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    // For tests - reset
    public static void clearAllForTesting() {
        sentMessages.clear(); disregardedMessages.clear(); storedMessages.clear();
        totalMessagesSent = 0;
    }
    public static void addStoredForTesting(Message m) { storedMessages.add(m); }

    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
    public int getMessageNumber() { return messageNumber; }
}