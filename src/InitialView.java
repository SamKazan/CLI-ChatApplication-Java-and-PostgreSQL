import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InitialView {
    private static final Scanner scanner = new Scanner(System.in);

    private static String username;

    private static String password;
    public static void show() throws IOException, InterruptedException {
        System.out.println("Welcome to the My Chat Chat Room App ");

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Your Response : ");

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();

                if (verifyAccount(username)) {
                    // Find the user account with the given username
                    Database.insertAccount(username,username,password,true);
                    MainView.show(username,password);
                } else {
                    System.out.println("Error: User already exists\n");
                }


            } else if (choice.equals("2")) {
                System.out.print("Enter username: ");
                username = scanner.nextLine();
                System.out.print("Enter password: ");
                password = scanner.nextLine();

                if (verifyLogin(username,password)) {
                    // Find the user account with the given username
                    Database.UpdateOnline(username,true);
                    MainView.show(username,password);
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }

            } else if (choice.equals("3")) {
                UserAccount.setOnline(username,false);
                break;

            } else {
                System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    public static boolean verifyLogin(String name, String password) {
        Map<String, UserAccount> accountList = Database.getAccounts();

        for (Map.Entry<String, UserAccount> entry : accountList.entrySet()) {
            String accountName = entry.getKey();
            UserAccount account = entry.getValue();
            if (account.getUsername().equals(name) && account.getPassword().equals(password)) {
                return true; // Name and password match, return true
            }
        }

        return false; // Name or password is invalid, return false
    }

    public static boolean verifyAccount(String name) {
        Map<String, UserAccount> accountList = Database.getAccounts();

        for (Map.Entry<String, UserAccount> entry : accountList.entrySet()) {
            String accountName = entry.getKey();
            UserAccount account = entry.getValue();

            if (account.getUsername().equals(name)) {

                return false; // username already exists, return false
            }
        }
        return true;
    }


}





