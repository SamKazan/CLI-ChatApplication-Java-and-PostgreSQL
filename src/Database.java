import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;

public class Database {



    private static Statement stmt = null;

    private static BasicDataSource dataSource;


    protected static void connectToDatabase() {
        if (dataSource == null) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost:5432/chatroom");
            dataSource.setUsername("postgres");
            dataSource.setPassword("11");
            dataSource.setInitialSize(5); // set initial pool size
        }
    }
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            connectToDatabase();
        }
        return dataSource.getConnection();
    }

    // create table for specific room
    public static void createRoom(String roomName) {
        if (CreateRoom.isValidName(roomName)) {
            connectToDatabase();
            try (Connection c = dataSource.getConnection()){

                stmt = c.createStatement();
                DatabaseMetaData dbm = c.getMetaData();
                ResultSet tables = dbm.getTables(null, null, roomName, null);
                if (tables.next()) {

                } else {
                    String sql = "CREATE TABLE " + roomName + " " +
                            "(ID SERIAL PRIMARY KEY, " +
                            "TIMESTAMP TEXT NOT NULL, " +
                            "USERNAME TEXT NOT NULL, " +
                            "MESSAGE TEXT)";
                    stmt.executeUpdate(sql);

                }
                stmt.close();
                c.close();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }

    // create new account
    public static void insertAccount(String username ,String name, String password ,boolean onlinestatus) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO accounts (ID, NAME, PASSWORD, ONLINESTATUS) " +
                    "VALUES ('" + username + "', '" + name + "', '" + password + "', " + onlinestatus + ")";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }

    // enter roomnames in roomnames table

    public static void insertRoom (String roomname) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()){
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "INSERT INTO roomname (RoomName) " +
                    "VALUES ('" + roomname +  "');";

            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();

        } catch (Exception e) {

            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

    }


    public static Map<String, UserAccount> getAccounts(){
        UserAccount userAccount;
        Map<String, UserAccount> userAccounts = new HashMap<>();
        connectToDatabase();
        try(Connection c = dataSource.getConnection()){

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from accounts");

            while(rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                boolean onlinestatus = rs.getBoolean("onlinestatus");

                userAccount = new UserAccount(name, password, onlinestatus);
                userAccounts.put(name, userAccount);
            }
            rs.close();
            stmt.close();
            c.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return userAccounts;
    }

    public static Map<String, String> getOnlineUsersInRoom(String roomName) {

        Map<String, String> onlineUsers = new HashMap<>();
        connectToDatabase();
        try (Connection c = dataSource.getConnection()) {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name, roomname FROM accounts WHERE onlinestatus = 'true' AND roomname = '" + roomName + "';");

            while (rs.next()) {
                String name = rs.getString("name");
                String room = rs.getString("roomname");
                onlineUsers.put(name, room);
            }
            stmt.close();
            rs.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return onlineUsers;
    }

    public static ArrayList<String> getRoom(){
        connectToDatabase();
        ArrayList<String> roomNames = new ArrayList<>();
        try(Connection c = dataSource.getConnection()){
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from roomname");

            while(rs.next()) {
                String roomname = rs.getString("RoomName");
                roomNames.add(roomname);
            }
            rs.close();
            stmt.close();
            c.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return roomNames;
    }

    public static boolean isRoomNameAvailable(String name) {
        try {
            ArrayList<String> roomNames = getRoom();
            return !roomNames.contains(name);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            return false;
        }
    }


    // Update Information
    public static void UpdateUsername(String username) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE accounts set NAME = " + "'" + username + "'" +
                    " where ID = "+ "'" + MainView.getUserName() + "'" +";";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void UpdatePassword (String username, String password) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE accounts set PASSWORD = " + "'" +password + "'" +
                    " where name = "+ "'" + username + "'" +";";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void UpdateOnline (String username, boolean Online ) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()){
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE accounts set ONLINESTATUS = " + Online + " where ID = "+ "'" + username + "'" +";";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);

        }

    }

    public static void UpdateRoomStatus (String username, String roomname ) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "UPDATE accounts set ROOMNAME = " + "'" + roomname + "'"
                    + " where ID = " +"'" + username + "'"+ ";";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }



    // Room Chat methods

    public static void insertChatMessage(String roomName, String username, String message) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            String sql = "INSERT INTO " + roomName + " (TIMESTAMP, USERNAME, MESSAGE) " +
                    "VALUES ('" + getCurrentTimes() + "', " + "'" + username + "', '" + message + "')";
            stmt.executeUpdate(sql);
            c.commit();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static ArrayList<String> getChatHistory(String roomName) {
        connectToDatabase();
        ArrayList<String> history = new ArrayList<String>();
        try (Connection c = dataSource.getConnection()){
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + roomName);
            while (rs.next()) {
                String timestamp = rs.getString("TIMESTAMP");
                String username = rs.getString("USERNAME");
                String message = rs.getString("MESSAGE");
                String entry = timestamp + " " + username + ": " + message;
                history.add(entry);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return history;
    }


    public static void printMessages(String roomName, int lastMessageId) {
        connectToDatabase();
        try (Connection c = dataSource.getConnection()){
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + roomName + " WHERE id > " +
                    lastMessageId + " ORDER BY id ASC");
            while (rs.next()) {

                // A new message has appeared in the result set
                int messageId = rs.getInt("id");
                String username = rs.getString("USERNAME");
                String message = rs.getString("MESSAGE");
                String timestamp = rs.getString("TIMESTAMP");
                System.out.println(timestamp + " " + username + ": " + message);

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static int getLastMessageId(String roomName) {
        connectToDatabase();
        int lastMessageId = 0;
        try (Connection c = dataSource.getConnection()){
            Statement statement = c.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM " + roomName);
            if (resultSet.next()) {
                lastMessageId = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return lastMessageId;
    }


    // current time

    public static String getCurrentTimes() {
        LocalDateTime currentTimestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return currentTimestamp.format(formatter);
    }


}
