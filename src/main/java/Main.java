iAAmport java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ================= REGISTRATION =================
        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter username (must contain _ and max 5 chars): ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password (min 8 chars, capital, number, special): ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter cellphone (+27 and 9 digits, e.g +27821234567): ");
        String cellphone = scanner.nextLine().trim();

        boolean usernameOk = username.contains("_") && username.length() <= 5;
        boolean passwordOk = password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[!@#$%^&*].*");
        boolean cellOk = cellphone.matches("\\+27\\d{9}");

        if (!usernameOk) {
            System.out.println("Username is not correctly formatted.");
        } else {
            System.out.println("Username successfully captured.");
        }

        if (!passwordOk) {
            System.out.println("Password is not correctly formatted.");
        } else {
            System.out.println("Password successfully captured.");
        }

        if (!cellOk) {
            System.out.println("Cellphone is not correctly formatted.");
        } else {
            System.out.println("Cellphone successfully captured.");
        }

        if (!usernameOk || !passwordOk || !cellOk) {
            System.out.println("Please correct your registration details.");
            scanner.close();
            return;
        }

        // ================= LOGIN =================
        System.out.println();
        System.out.println("=== LOGIN ===");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine().trim();

        boolean loginSuccessful = loginUsername.equals(username) && loginPassword.equals(password);

        if (loginSuccessful) {
            System.out.println("Welcome " + loginUsername + ", it is great to see you again.");
        } else {
            System.out.println("Username or password incorrect, please try again.");
            scanner.close();
            return;
        }

        // ================= HOW MANY MESSAGES =================
        int messageLimit;
        while (true) {
            System.out.print("How many messages would you like to enter? ");
            try {
                messageLimit = Integer.parseInt(scanner.nextLine().trim());
                if (messageLimit >= 0) {
                    break;
                }
                System.out.println("Please enter zero or a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }

        int messagesEntered = 0;
        boolean running = true;
        ArrayList<Message> storedMessages = new ArrayList<>();

        // ================= MAIN MENU =================
        while (running) {
            System.out.println();
            System.out.println("=================================");
            System.out.println("             QUICKCHAT");
            System.out.println("=================================");
            System.out.println("1) Send Messages");
            System.out.println("2) Show Stored Messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (messagesEntered >= messageLimit) {
                        System.out.println("You have entered the number of messages you selected.");
                        break;
                    }

                    while (messagesEntered < messageLimit) {
                        System.out.println();
                        System.out.println("Message " + (messagesEntered + 1));

                        System.out.print("Enter recipient cellphone number: ");
                        String recipient = scanner.nextLine().trim();

                        System.out.print("Enter your message: ");
                        String messageText = scanner.nextLine();

                        Message message = new Message(recipient, messageText, messagesEntered);

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
                            continue;
                        }

                        if (choice < 1 || choice > 3) {
                            System.out.println("Invalid option.");
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

                        if (choice == 3) {
                            storedMessages.add(message);
                            System.out.println("Message stored successfully.");
                        }

                        messagesEntered++;

                        if (messagesEntered < messageLimit) {
                            System.out.println();
                            System.out.println("Messages entered: " + messagesEntered + " of " + messageLimit);
                        }
                    }

                    System.out.println();
                    System.out.println("Total messages sent: " + new Message("", "", 0).returnTotalMessagess());
                    break;

                case "2":
                    System.out.println();
                    System.out.println("=== STORED MESSAGES ===");
                    if (storedMessages.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        for (Message m : storedMessages) {
                            System.out.println("ID: " + m.getMessageID() + " | To: " + m.getRecipient() + " | Msg: " + m.getMessage());
                        }
                    }
                    System.out.println();
                    System.out.println("1) Search by Recipient");
                    System.out.println("2) Delete by ID");
                    System.out.println("3) Show Longest Message");
                    System.out.println("4) Back");
                    System.out.print("Choose: ");
                    String sub = scanner.nextLine().trim();

                    if (sub.equals("1")) {
                        System.out.print("Enter recipient to search: ");
                        String searchRec = scanner.nextLine().trim();
                        for (Message m : storedMessages) {
                            if (m.getRecipient().equals(searchRec)) {
                                System.out.println("Found: " + m.getMessage());
                            }
                        }
                    } else if (sub.equals("2")) {
                        System.out.print("Enter Message ID to delete: ");
                        String delId = scanner.nextLine().trim();
                        storedMessages.removeIf(m -> m.getMessageID().equals(delId));
                        System.out.println("Message deleted if it existed.");
                    } else if (sub.equals("3")) {
                        Message longest = null;
                        for (Message m : storedMessages) {
                            if (longest == null || m.getMessage().length() > longest.getMessage().length()) {
                                longest = m;
                            }
                        }
                        if (longest != null) {
                            System.out.println("Longest message: " + longest.getMessage());
                        }
                    }
                    break;

                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }

        scanner.close();
    }
}