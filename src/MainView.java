import java.io.IOException;
import java.util.Scanner;

public class MainView {
    private static final Scanner scanner = new Scanner(System.in);

    private static String userName;


    public static void show(String username, String password) throws IOException, InterruptedException {
        userName = username;
        UserAccount userAccount = new UserAccount(username, password);

        String choice;
        boolean exit = false;
        System.out.println("\nWelcome "+ username + "!");
        while (!exit) {
            System.out.println("Please choose an option:");
            System.out.println("1. Join a room");
            System.out.println("2. Create a room");
            System.out.println("3. Account");
            System.out.println("4. Exit");
            System.out.print("Choice: ");
            choice = scanner.nextLine();

            if (choice.equals("1")) {

                System.out.print("Enter the name of the room: ");
                String name = scanner.nextLine() ;
                CreateRoom createRoom = new CreateRoom(name);
                if (createRoom.roomExists()) {
                    System.out.println("Joining room...");
                    Database.UpdateRoomStatus(username,name);
                    username = userAccount.getUsername();
                    System.out.println(username);
                    RoomView roomView = new RoomView(name, username);
                    roomView.start();
                } else {
                    System.out.println("Room does not exist.\n");
                }
            }else if (choice.equals("2")) {
                System.out.print("Enter the name of the room: ");
                String name = scanner.nextLine() ;
                CreateRoom createRoom = new CreateRoom(name);
                createRoom.newRoom(name);
                Database.createRoom(name);
            } else if (choice.equals("3")) {
                userAccount.updateInformation();
            } else if (choice.equals("4")) {
                exit = true;
                Database.UpdateOnline(username,false);
            } else {
                System.out.println("Invalid choice. Please try again.\n");
            }
        }
        userName = username;
    }

    public static String getUserName() {
        return userName;
    }
}
