// Part 3 - Main app - FIXED: for loop + Option 4 Stored Messages
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("       REGISTRATION SYSTEM");
        System.out.println("=================================");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        System.out.print("Enter your South African cellphone number: ");
        String cellPhone = scanner.nextLine();

        Login user = new Login(username, password, cellPhone, firstName, lastName);

        System.out.println();
        System.out.println(user.registerUser());

        if (user.checkUserName()
                && user.checkPasswordComplexity()
                && user.checkCellPhoneNumber()) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("              LOGIN");
            System.out.println("=================================");

            System.out.print("Enter your username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();

            boolean loginSuccessful = user.loginUser(loginUsername, loginPassword);

            System.out.println();
            System.out.println(user.returnLoginStatus(loginSuccessful));

            if (loginSuccessful) {

                System.out.println();
                System.out.println("Welcome to QuickChat.");
                // Load stored messages from JSON at start - Part 3
                Message.loadStoredMessagesFromJson();

                int messageLimit;

                while (true) {
                    System.out.print("How many messages would you like to enter? ");
                    try {
                        messageLimit = Integer.parseInt(scanner.nextLine().trim());
                        if (messageLimit >= 0) break;
                        System.out.println("Please enter zero or a positive number.");
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid whole number.");
                    }
                }

                boolean running = true;

                while (running) {

                    System.out.println();
                    System.out.println("=================================");
                    System.out.println("             QUICKCHAT");
                    System.out.println("=================================");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.println("4) Stored Messages");
                    System.out.print("Choose an option: ");

                    String option = scanner.nextLine().trim();

                    switch (option) {

                        case "1":
                            // FIX Part 2 feedback: FOR loop, not while loop
                            // Also check total entered via sent+disregarded+stored
                            int totalEntered = Message.getSentMessages().size()
                                    + Message.getDisregardedMessages().size()
                                    + Message.getStoredMessages().size();

                            if (totalEntered >= messageLimit) {
                                System.out.println("You have entered the number of messages you selected.");
                                break;
                            }

                            for (int i = totalEntered; i < messageLimit; i++) {

                                System.out.println();
                                System.out.println("Message " + (i + 1));

                                System.out.print("Enter recipient cellphone number: ");
                                String recipient = scanner.nextLine().trim();

                                System.out.print("Enter your message: ");
                                String messageText = scanner.nextLine();

                                Message message = new Message(recipient, messageText, i);

                                System.out.println(message.checkMessageLength());
                                System.out.println(message.checkRecipientCell());

                                System.out.println();
                                System.out.println("1) Send");
                                System.out.println("2) Disregard");
                                System.out.println("3) Store");
                                System.out.print("Choose an option: ");

                                String choiceText = scanner.nextLine().trim();
                                int choice;
                                try {
                                    choice = Integer.parseInt(choiceText);
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid option.");
                                    i--; // retry same message number
                                    continue;
                                }

                                if (choice < 1 || choice > 3) {
                                    System.out.println("Invalid option.");
                                    i--;
                                    continue;
                                }

                                System.out.println(message.SentMessage(choice));

                                if (choice == 1 && message.getStatus().equals("Sent")) {
                                    System.out.println();
                                    System.out.println("Message ID: " + message.getMessageID());
                                    System.out.println("Message Hash: " + message.getMessageHash());
                                    System.out.println("Recipient: " + message.getRecipient());
                                    System.out.println("Message: " + message.getMessage());
                                }

                                System.out.println();
                                System.out.println("Messages entered: " + (i + 1) + " of " + messageLimit);
                            }

                            System.out.println();
                            System.out.println("Total messages sent: " + new Message("", "", 0).returnTotalMessagess());
                            break;

                        case "2":
                            System.out.println("Coming Soon.");
                            break;

                        case "3":
                            running = false;
                            System.out.println("Thank you for using QuickChat. Goodbye!");
                            break;

                        case "4":
                            // PART 3 - Stored Messages menu
                            boolean inStoredMenu = true;
                            while (inStoredMenu) {
                                System.out.println();
                                System.out.println("--- STORED MESSAGES ---");
                                System.out.println("a) Display sender and recipient of all stored messages");
                                System.out.println("b) Display longest stored message");
                                System.out.println("c) Search for message by Message ID");
                                System.out.println("d) Search messages by recipient");
                                System.out.println("e) Delete a message using Message Hash");
                                System.out.println("f) Display full report");
                                System.out.println("g) Back to main menu");
                                System.out.print("Choose an option: ");
                                String sub = scanner.nextLine().trim().toLowerCase();

                                switch (sub) {
                                    case "a":
                                        System.out.println(Message.displayStoredSendersRecipients());
                                        break;
                                    case "b":
                                        System.out.println("Longest stored message: " + Message.getLongestStoredMessage());
                                        break;
                                    case "c":
                                        System.out.print("Enter Message ID: ");
                                        String searchID = scanner.nextLine().trim();
                                        Message found = Message.searchByMessageID(searchID);
                                        if (found != null) {
                                            System.out.println("Recipient: " + found.getRecipient());
                                            System.out.println("Message: " + found.getMessage());
                                        } else {
                                            System.out.println("Message with ID " + searchID + " not found.");
                                        }
                                        break;
                                    case "d":
                                        System.out.print("Enter recipient number: ");
                                        String searchRec = scanner.nextLine().trim();
                                        ArrayList<Message> results = Message.searchByRecipient(searchRec);
                                        if (results.isEmpty()) {
                                            System.out.println("No messages found for " + searchRec);
                                        } else {
                                            for (Message m : results) {
                                                System.out.println("Recipient: " + m.getRecipient() + " | Message: " + m.getMessage());
                                            }
                                        }
                                        break;
                                    case "e":
                                        System.out.print("Enter Message Hash: ");
                                        String hash = scanner.nextLine().trim();
                                        System.out.println(Message.deleteMessageByHash(hash));
                                        break;
                                    case "f":
                                        System.out.println(Message.displayFullReport());
                                        break;
                                    case "g":
                                        inStoredMenu = false;
                                        break;
                                    default:
                                        System.out.println("Invalid option.");
                                }
                            }
                            break;

                        default:
                            System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
                    }
                }

            } else {
                System.out.println("Please restart the application and try logging in again.");
                int messageLimit;

                while (true) {
                    System.out.print(
                            "How many messages would you like to enter? ");

                    try {
                        messageLimit = Integer.parseInt(scanner.nextLine().trim());

                        if (messageLimit >= 0) {
                            break;
                        }

                        System.out.println(
                                "Please enter zero or a positive number.");

                    } catch (NumberFormatException exception) {
                        System.out.println(
                                "Please enter a valid whole number.");
                    }
                }

                int messagesEntered = 0;
                boolean running = true;

                while (running) {

                    System.out.println();
                    System.out.println("=================================");
                    System.out.println("             QUICKCHAT");
                    System.out.println("=================================");
                    System.out.println("1) Send Messages");
                    System.out.println("2) Show recently sent messages");
                    System.out.println("3) Quit");
                    System.out.print("Choose an option: ");

                    String option = scanner.nextLine().trim(); // FIX 1: trim

                    switch (option) {

                        case "1":

                            if (messagesEntered >= messageLimit) {
                                System.out.println(
                                        "You have entered the number of messages "
                                        + "you selected.");
                                break;
                            }

                            while (messagesEntered < messageLimit) {

                                System.out.println();
                                System.out.println(
                                        "Message " + (messagesEntered + 1));

                                System.out.print(
                                        "Enter recipient cellphone number: ");
                                String recipient = scanner.nextLine().trim();

                                System.out.print("Enter your message: ");
                                String messageText = scanner.nextLine();

                                Message message = new Message(
                                        recipient,
                                        messageText,
                                        messagesEntered);

                                System.out.println(
                                        message.checkMessageLength());

                                System.out.println(
                                        message.checkRecipientCell());

                                System.out.println();
                                System.out.println("1) Send");
                                System.out.println("2) Disregard");
                                System.out.println("3) Store");
                                System.out.print("Choose an option: ");

                                String choiceText = scanner.nextLine().trim(); // FIX 2: trim
                                int choice;

                                try {
                                    choice = Integer.parseInt(choiceText);
                                } catch (NumberFormatException exception) {
                                    System.out.println("Invalid option.");
                                    continue;
                                }

                                if (choice < 1 || choice > 3) {
                                    System.out.println("Invalid option.");
                                    continue;
                                }

                                System.out.println(
                                        message.SentMessage(choice));

                                if (choice == 1
                                        && message.getStatus().equals("Sent")) {

                                    System.out.println();
                                    System.out.println("Message ID: "
                                            + message.getMessageID());

                                    System.out.println("Message Hash: "
                                            + message.getMessageHash());

                                    System.out.println("Recipient: "
                                            + message.getRecipient());

                                    System.out.println("Message: "
                                            + message.getMessage());
                                }

                                messagesEntered++;

                                if (messagesEntered < messageLimit) {
                                    System.out.println();
                                    System.out.println("Messages entered: "
                                            + messagesEntered + " of "
                                            + messageLimit);
                                }
                            }

          System.out.println();
System.out.println("Total messages sent: "
        + new Message("", "", 0)
                .returnTotalMessagess());
                            break;

                        case "2":
                            System.out.println("Coming Soon.");
                            break;

                        case "3":
                            running = false;
                            System.out.println(
                                    "Thank you for using QuickChat. Goodbye!");
                            break;

                        default:
                            System.out.println(
                                    "Invalid option. Please choose 1, 2, or 3.");
                    }
                }

            } else {
                System.out.println(
                        "Please restart the application and try logging in again.");
            }

        } else {
            System.out.println("Please correct your registration details.");
            System.out.println(
                    "Please correct your registration details.");
        }

        scanner.close();
    }
}