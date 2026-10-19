import java.io.FileWriter;
import java.io.IOException;
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
    private static final ArrayList<Message> messages =
            new ArrayList<>();

    // Creates a message with a randomly generated 10-digit ID.
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

    // Allows a fixed ID to be supplied for unit testing.
    public Message(String messageID, String recipient,
                   String message, int messageNumber) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    // Checks that the message ID has no more than 10 characters.
    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("\\d{1,10}");
    }

    // Checks the recipient's South African international number.
    public String checkRecipientCell() {
        if (recipient != null
                && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does "
                + "not contain an international code. Please correct "
                + "the number and try again.";
    }

    // Creates the message hash using the first two ID digits,
    // message number, first word and last word.
    public String createMessageHash() {
        String firstTwoDigits = messageID.substring(0, 2);
        String[] words = message.trim().split("\\s+");

        String firstWord = words[0].replaceAll(
                "^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");

        String lastWord = words[words.length - 1].replaceAll(
                "^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", "");

        return (firstTwoDigits + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();
    }

    // Checks whether the message is within the 250-character limit.
    public String checkMessageLength() {
        if (message == null) {
            return "Please enter a message.";
        }

        if (message.length() > 250) {
            return "Message exceeds 250 characters by "
                    + (message.length() - 250)
                    + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    // Sends, disregards or stores a message according to the choice.
    public String SentMessage(int choice) {
        if (!checkMessageID()
                || !checkRecipientCell().equals(
                        "Cell phone number successfully captured.")
                || !checkMessageLength().equals("Message ready to send.")) {
            return "Message could not be processed.";
        }

        switch (choice) {
            case 1:
                status = "Sent";
                totalMessagesSent++;
                messages.add(this);
                return "Message successfully sent.";

            case 2:
                status = "Disregarded";
                return "Press 0 to delete the message.";

            case 3:
                status = "Stored";
                storeMessage();
                return "Message successfully stored.";

            default:
                return "Invalid option.";
        }
    }

    // Returns details of messages sent during this program session.
    public String printMessages() {
        StringBuilder output = new StringBuilder();

        for (Message sentMessage : messages) {
            output.append("Message ID: ")
                    .append(sentMessage.messageID)
                    .append("\nMessage Hash: ")
                    .append(sentMessage.messageHash)
                    .append("\nRecipient: ")
                    .append(sentMessage.recipient)
                    .append("\nMessage: ")
                    .append(sentMessage.message)
                    .append("\n\n");
        }

        if (output.length() == 0) {
            return "No messages have been sent yet.";
        }

        return output.toString();
    }

    // Returns the number of messages successfully sent.
    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    // Stores a message in a JSON file.
    public void storeMessage() {
        try (FileWriter writer = new FileWriter(
                "stored_messages.json", true)) {

            String jsonMessage = "{"
                    + "\"messageID\":\"" + escapeJson(messageID) + "\","
                    + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                    + "\"recipient\":\"" + escapeJson(recipient) + "\","
                    + "\"message\":\"" + escapeJson(message) + "\","
                    + "\"status\":\"" + escapeJson(status) + "\""
                    + "}"
                    + System.lineSeparator();

            writer.write(jsonMessage);

        } catch (IOException exception) {
            System.out.println(
                    "Error storing message: " + exception.getMessage());
        }
    }

    // Escapes special characters so message text can be stored as JSON.
    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Getter methods
    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }
}

