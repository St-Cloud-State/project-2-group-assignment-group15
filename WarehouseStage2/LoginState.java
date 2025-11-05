import java.util.*;
import java.io.*;
public class LoginState extends WareState {
    private static final int CLERK_LOGIN = 0;
    private static final int USER_LOGIN = 1;
    private static final int MANAGER_LOGIN = 2;
    private static final int EXIT = 3;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WareContext context;

    private static LoginState instance;

    private LoginState() {
        super();
    }

    public static LoginState instance() {
        if (instance == null) {
            instance = new LoginState();
        }
        return instance;
    }



    private int getCommand() {
        try {
            String input = reader.readLine();
            return Integer.parseInt(input.trim());
        } catch (Exception e) {
            return -1;
        }
    }
    private String getToken(String prompt) {
        try {
            System.out.print(prompt);
            String line = reader.readLine();
            return line;
        } catch (IOException e) {
            System.out.println("Error reading input.");
            return null;
        }
    }

    private void clerk() {
        (WareContext.instance()).setLogin(WareContext.IsClerk);
        (WareContext.instance()).changeState(0);
    }


    private boolean user(){
        String userID = getToken("Please enter the user id: ");
        if (Warehouse.instance().verifyClient(userID) != null){
            (WareContext.instance()).setLogin(WareContext.IsUser);
            (WareContext.instance()).setUser(userID);
            (WareContext.instance()).changeState(1);
            return true;
        }
        else {
            System.out.println("Invalid user id.");
            return false;
        }
    }

    private void manager() {
            (WareContext.instance()).setLogin(WareContext.IsManager); 
            (WareContext.instance()).changeState(2);                
  }



    public void run() {
        int command;
        boolean done = false;

        while (!done) {
            System.out.println("\n=== Login Menu ===");
            System.out.println(CLERK_LOGIN + ". Clerk Login");
            System.out.println(USER_LOGIN + ". User Login");
            System.out.println(MANAGER_LOGIN + ". Manager Login");
            System.out.println(EXIT + ". Exit");
            System.out.print("Enter choice: ");

            command = getCommand();

            switch (command) {
                case CLERK_LOGIN:
                    clerk();
                    done = true;
                    break;
                case USER_LOGIN:
                    if (user()) {
                        done = true;
                    }
                    break;
                case MANAGER_LOGIN:
                    manager();
                    done = true;
                    break;
                case EXIT:
                    (WareContext.instance()).changeState(3);
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }
}
