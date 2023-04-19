import java.util.*;
import java.io.*;

public class CreateRoom {
    private String name;

    private UserAccount userAccount ;

    public CreateRoom (String name) throws IOException {
        this.name = name;
    };

    public static void newRoom (String name) throws InterruptedException {
        if (isValidName(name)) {
            if (Database.isRoomNameAvailable(name)) {
                Database.insertRoom(name);
                // go to roomview class
                RoomView roomView = new RoomView(name, MainView.getUserName());
                Database.UpdateRoomStatus(MainView.getUserName(), name);
                roomView.start();
            } else {
                System.out.println("The room name already exists. Please choose a different name.");
            }
        } else {
            System.out.println("The name is not valid, only lower case letters and numbers allowed.\n");
        }

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected static boolean isValidName(String name) {
        // Check that the name only contains lowercase letters and numbers
        return name.matches("^[a-z0-9]+$");
    }

    public boolean roomExists() {
        ArrayList<String> roomNames = new ArrayList<>();
        roomNames = Database.getRoom();

        if (roomNames.contains(name)) {
            return true;
        }
        return false;
    }


}
