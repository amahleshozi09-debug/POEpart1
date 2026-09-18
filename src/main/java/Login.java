public class Login {

    private String username;
    private String password;
    private String cellPhone;
    private String firstName;
    private String lastName;

    public Login(String username, String password, String cellPhone,
                 String firstName, String lastName) {

        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public boolean checkUserName() {
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {

        if (password.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapitalLetter = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecialCharacter = true;
            }
        }

        return hasCapitalLetter && hasNumber && hasSpecialCharacter;
    }

    public boolean checkCellPhoneNumber() {

        String cellPhonePattern = "^\\+27\\d{9}$";

        return cellPhone.matches(cellPhonePattern);
    }

    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure "
                    + "that your username contains an underscore and is "
                    + "no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure "
                    + "that the password contains at least eight characters, "
                    + "a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted or does not "
                    + "contain an international code.";
        }

        return "User has been registered successfully.";
    }

    public boolean loginUser(String enteredUsername,
                             String enteredPassword) {

        return username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean loginSuccessful) {

        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}