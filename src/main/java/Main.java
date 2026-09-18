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
                username,
                password,
                cellPhone,
                firstName,
                lastName
        );

        // Display registration result
        System.out.println();
        System.out.println(user.registerUser());

        // Only allow login if registration was successful
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
            System.out.println(
                    user.returnLoginStatus(loginSuccessful)
            );

        } else {

            System.out.println();
            System.out.println(
                    "Please correct your registration details."
            );
        }

        scanner.close();
    }
}
