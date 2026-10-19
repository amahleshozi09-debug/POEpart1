// Part 2 - Main app for sending and storing messages
import java.util.Scanner;

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

        Login user = new Login(
                username, password, cellPhone, firstName, lastName);

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

            boolean loginSuccessful =
                    user.loginUser(loginUsername, loginPassword);

            System.out.println();
            System.out.println(user.returnLoginStatus(loginSuccessful));

            if (loginSuccessful) {

                System.out.println();
                System.out.println("Welcome to QuickChat.");

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
            System.out.println(
                    "Please correct your registration details.");
        }

        scanner.close();
    }
}