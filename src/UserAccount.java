import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;

public class UserAccount {

        private static Map<String, UserAccount> userAccounts = new HashMap<>();

        static {

           userAccounts = Database.getAccounts();

        // test if it works
//        for (Map.Entry<String, UserAccount> entry : userAccounts.entrySet()) {
//            String name = entry.getKey();
//            UserAccount userAccount = entry.getValue();
//            System.out.println("Name: " + name);
//            System.out.println("Username: " + userAccount.getUsername());
//            System.out.println("Password: " + userAccount.getPassword());
//            System.out.println("Online status: " + userAccount.isOnline());
//            System.out.println();
//        }
    }


        private  String username;
        private String password;
        private static boolean onlineStatus;



        public UserAccount(String username, String password, boolean onlineStatus) {

            this.username = username;
            this.password = password;
            this.onlineStatus = onlineStatus;

        }

        public UserAccount(String username, String password) {
            this.username = username;
            this.password = password;

        }

        public UserAccount(String username) {
            this.username = username;

        }



        public boolean isOnline() {
            return onlineStatus;
        }

        public static void setOnline(String username, boolean online) {

            if (userAccounts.containsKey(username)) {
                // Get the existing UserAccount object
                UserAccount userAccount = userAccounts.get(username);
                // Create a new UserAccount object with the new password and the other properties of the existing userAccount
                UserAccount newUserAccount = new UserAccount(userAccount.getUsername(), userAccount.getPassword(), online);
                // Update the mapping in the map
                userAccounts.put(username, newUserAccount);
            }
            onlineStatus = online;

            Database.UpdateOnline(username,online);
        }

        public void setUsername (String username) {

             if (userAccounts.containsKey(this.username)) {
                 // Remove the old mapping from the map
                 UserAccount oldUserAccount = userAccounts.remove(this.username);
                 // Create a new UserAccount object with the new username and the other properties of the oldUserAccount
                 UserAccount newUserAccount = new UserAccount(username, oldUserAccount.getPassword(), oldUserAccount.isOnline());
                 // Add the new mapping to the map
                 userAccounts.put(username, newUserAccount);
             }
             this.username = username;

             Database.UpdateUsername(username);

        }

        public void setPassword(String password) {
        if (userAccounts.containsKey(this.username)) {
            // Get the existing UserAccount object
            UserAccount userAccount = userAccounts.get(this.username);
            // Create a new UserAccount object with the new password and the other properties of the existing userAccount
            UserAccount newUserAccount = new UserAccount(userAccount.getUsername(), password, userAccount.isOnline());
            // Update the mapping in the map
            userAccounts.put(this.username, newUserAccount);
        } else {
            // Create a new UserAccount object with the given password and add it to the map
            UserAccount newUserAccount = new UserAccount(this.username, password, false);
            userAccounts.put(this.username, newUserAccount);
        }
        this.password = password;

        Database.UpdatePassword(username,password);
    }

        public String getUsername() {
            return  username;
        }

        public String getPassword() {
            return password;
        }
        public void saveToFile() throws IOException {
            for (Map.Entry<String, UserAccount> entry : userAccounts.entrySet()) {
                String username = entry.getKey();
                UserAccount userAccount = entry.getValue();

                Database.insertAccount(username, userAccount.getUsername(), userAccount.getPassword(),
                        userAccount.isOnline());

            }
        }

        public void updateInformation() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();
        // Check if the entered password matches the current password for this user
        if (!currentPassword.equals(password)) {
            System.out.println("Incorrect password. Update information failed.");
            return;
        }
        System.out.println("What would you like to update?");
        System.out.println("1. Username");
        System.out.println("2. Password");
        String choiceStr = scanner.nextLine();
        int choice = 0;
        try {
            choice = Integer.parseInt(choiceStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Update information aborted.");
            return;
        }
        if (choice == 1) {
            System.out.print("Enter your new username: ");
            String newUsername = scanner.nextLine();
            Database.UpdateUsername(newUsername);
            setUsername(newUsername);
        } else if (choice == 2) {
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
            Database.UpdatePassword(username,newPassword);
            setPassword(newPassword);

        } else {
            System.out.println("Invalid choice. Update information failed.");
            return;
        }

    }



}
