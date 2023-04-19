import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RoomView {
    private final String roomName;

    private final String accountName;
    private final UserAccount userAccount;
    private boolean running = true;

    public RoomView(String roomName, String accountName) {
        this.accountName = accountName;
        this.roomName = roomName;
        this.userAccount = new UserAccount(accountName);
        Database.createRoom(roomName);

    }

      public void start() {
        System.out.println("\nYou are now viewing chat room " + roomName + ".");
        System.out.println("Type '/help' for a list of available commands.");

        Scanner scanner = new Scanner(System.in);


          Thread inputThread = new Thread(() -> {
              while (running) {
                  String input = scanner.nextLine();

                  if (input.startsWith("/")) {
                      String[] tokens = input.split(" ", 2);
                      String command = tokens[0];
                      if (command.equals("/list")) {
                          System.out.println("Users in this chat room:");
                          Map<String, String> onlineUsers = Database.getOnlineUsersInRoom(roomName);
                          for (String name : onlineUsers.keySet()) {
                              System.out.println(name);
                          }

                      } else if (command.equals("/leave")) {
                          System.out.println("Leaving chat room " + roomName + "...");
                          Database.UpdateOnline(userAccount.getUsername(), false);
                          running = false;

                      } else if (command.equals("/history")) {
                          System.out.println("Chat history for the Room" + roomName + ":");
                          for (String message : Database.getChatHistory(roomName)) {
                              System.out.println(message);
                          }
                      } else if (command.equals("/help")) {
                          System.out.println("Available commands:");
                          System.out.println("/list - List users in the chat room");
                          System.out.println("/leave - Leave the chat room");
                          System.out.println("/history - Show chat history for the room");
                          System.out.println("/help - Show this list of commands");
                      } else {
                          System.out.println("Invalid command. Type '/help' for a list of available commands.");
                      }
                  } else {

                      Database.insertChatMessage(roomName, accountName, input);
                  }
              }
          });

          Thread printThread = new Thread(() -> {
              int lastMessageId = Database.getLastMessageId(roomName);
              while (running) {
                  Database.printMessages(roomName, lastMessageId);
                  lastMessageId = Database.getLastMessageId(roomName);
                  try {
                      Thread.sleep(100); // Wait for a second before checking for new messages
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          });

          inputThread.start();
          printThread.start();

          try {
              inputThread.join();
              printThread.join();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }

      }

}


//    public void start() throws InterruptedException {
//        System.out.println("\nYou are now viewing chat room " + roomName + ".");
//        System.out.println("Type '/help' for a list of available commands.");
//
//        Scanner scanner = new Scanner(System.in);
//
//        Thread inputThread = new Thread(() -> {
//            boolean running = true;
//            while (running) {
//                String input = scanner.nextLine();
//
//                if (input.startsWith("/")) {
//                    String[] tokens = input.split(" ", 2);
//                    String command = tokens[0];
//                    if (command.equals("/list")) {
//                        System.out.println("Users in this chat room:");
//                        Map<String, String> onlineUsers = Database.getOnlineUsersInRoom(roomName);
//                        for (String name : onlineUsers.keySet()) {
//                            System.out.println(name);
//                        }
//                    } else if (command.equals("/leave")) {
//                        System.out.println("Leaving chat room " + roomName + "...");
//                        Database.UpdateOnline(userAccount.getUsername(), false);
//                        running = false;
//                    } else if (command.equals("/history")) {
//                        System.out.println("Chat history for the Room" + roomName + ":");
//                        for (String message : Database.getChatHistory(roomName)) {
//                            System.out.println(message);
//                        }
//                    } else if (command.equals("/help")) {
//                        System.out.println("Available commands:");
//                        System.out.println("/list - List users in the chat room");
//                        System.out.println("/leave - Leave the chat room");
//                        System.out.println("/history - Show chat history for the room");
//                        System.out.println("/help - Show this list of commands");
//                    } else {
//                        System.out.println("Invalid command. Type '/help' for a list of available commands.");
//                    }
//                } else {
//                    Database.insertChatMessage(roomName, accountName, input);
//                }
//            }
//        });
//
//        inputThread.start();
//
//        int lastMessageId = Database.getLastMessageId(roomName);
//
//
//        while (true) {
//
//            Database.printMessages(roomName, lastMessageId);
//
//
//
//        }
//
//
//    }

//    public void start() {
//        System.out.println("\nYou are now viewing chat room " + roomName + ".");
//        System.out.println("Type '/help' for a list of available commands.");
//
//        Scanner scanner = new Scanner(System.in);
//        boolean running = true;
//
//        int lastMessageId = Database.getLastMessageId(roomName);
//        while (running) {
//
//            String input = scanner.nextLine();
//
//            if (input.startsWith("/")) {
//                String[] tokens = input.split(" ", 2);
//                String command = tokens[0];
//                if (command.equals("/list")) {
//                    System.out.println("Users in this chat room:");
//                    Map<String, String> onlineUsers = Database.getOnlineUsersInRoom(roomName);
//                    for (String name : onlineUsers.keySet()) {
//                        System.out.println(name);
//                    }
//
//                } else if (command.equals("/leave")) {
//                    System.out.println("Leaving chat room " + roomName + "...");
//                    Database.UpdateOnline(userAccount.getUsername(), false);
//                    running = false;
//
//                } else if (command.equals("/history")) {
//                    System.out.println("Chat history for the Room" + roomName + ":");
//                    for (String message : Database.getChatHistory(roomName)) {
//                        System.out.println(message);
//                    }
//                } else if (command.equals("/help")) {
//                    System.out.println("Available commands:");
//                    System.out.println("/list - List users in the chat room");
//                    System.out.println("/leave - Leave the chat room");
//                    System.out.println("/history - Show chat history for the room");
//                    System.out.println("/help - Show this list of commands");
//                } else {
//                    System.out.println("Invalid command. Type '/help' for a list of available commands.");
//                }
//            } else {
//
//                Database.insertChatMessage(roomName, accountName, input);
//            }
//
//            Database.printMessages(roomName,lastMessageId);
//            lastMessageId = Database.getLastMessageId(roomName);
//        }
//    }

    /* public void start() {
        System.out.println("\nYou are now viewing chat room " + roomName + ".");
        System.out.println("Type '/help' for a list of available commands.");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        int messageCount = Database.getMessageCount(roomName);
        int lastMessageId = Database.getLastMessageId(roomName);
        while (running) {

                int newMessageId = Database.getLastMessageId(roomName);
                String input = scanner.nextLine();

                if (input.startsWith("/")) {
                    String[] tokens = input.split(" ", 2);
                    String command = tokens[0];
                    if (command.equals("/list")) {
                        System.out.println("Users in this chat room:");
                        Map<String, String> onlineUsers = Database.getOnlineUsersInRoom(roomName);
                        for (String name : onlineUsers.keySet()) {
                            System.out.println(name);
                        }

                    } else if (command.equals("/leave")) {
                        System.out.println("Leaving chat room " + roomName + "...");
                        Database.UpdateOnline(userAccount.getUsername(), false);
                        running = false;

                    } else if (command.equals("/history")) {
                        System.out.println("Chat history for the Room" + roomName + ":");
                        for (String message : Database.getChatHistory(roomName)) {
                            System.out.println(message);
                        }
                    } else if (command.equals("/help")) {
                        System.out.println("Available commands:");
                        System.out.println("/list - List users in the chat room");
                        System.out.println("/leave - Leave the chat room");
                        System.out.println("/history - Show chat history for the room");
                        System.out.println("/help - Show this list of commands");
                    } else {
                        System.out.println("Invalid command. Type '/help' for a list of available commands.");
                    }
                } else {

                    Database.insertChatMessage(roomName, accountName, input);
                }

                Database.printMessages(roomName,lastMessageId);
        }
    } */



