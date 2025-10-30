import java.util.*;
import java.text.*;
import java.io.*;
public class ClerkState extends WareState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WareContext context;
    private static ClerkState instance;
    private static final int EXIT = 0;
    private static final int ADD_CLIENT = 1;
    private static final int SHOW_PRODUCTS = 2;
    private static final int SHOW_CLIENTS = 3;
    private static final int SHOW_OUTSTANDING_CLIENTS = 4;
    private static final int RECORD_PAYMENT = 6;
    private static final int BECOME_CLIENT = 7;
    private static final int HELP = 8;
    private ClerkState() {
        super();
        warehouse = Warehouse.instance();
        //context = WareContext.instance(); ***IDK why this is commented out??***
    }

    public static ClerkState instance() {
        if (instance == null) {
            instance = new ClerkState();
        }
        return instance;
    }

    public String getToken(String prompt) {
        do {
            try {
                System.out.println(prompt);
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
                if (tokenizer.hasMoreTokens()) {
                    return tokenizer.nextToken();
                }
            } catch (IOException ioe) {
                System.exit(0);
            }
        } while (true);
    }
    
    private boolean yesOrNo(String prompt) {
        String more = getToken(prompt + " (Y|y)[es] or anything else for no");
        if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
            return false;
        }
        return true;
        
    }

    public int getNumber(String prompt) {
        do {
            try {
                String item = getToken(prompt);
                Integer num = Integer.valueOf(item);
                return num.intValue();
            } catch (NumberFormatException nfe) {
                System.out.println("Please input a number ");
            }
        } while (true);
    }
    
    public Calendar getDate(String prompt) {
        do {
            try {
                Calendar date = new GregorianCalendar();
                String item = getToken(prompt);
                DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                date.setTime(df.parse(item));
                return date;
            } catch (Exception fe) {
                System.out.println("Please input a date as mm/dd/yy");
            }
        } while (true);
    }
    
    public int getCommand() {
        do {
            try {
                int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
                if (value >= EXIT && value <= HELP) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }

    public void help() {
        System.out.println("Enter a number between 0 and 12 as explained below:");
        System.out.println(EXIT + " to Exit\n");
        System.out.println(ADD_CLIENT + " to add a client");
        System.out.println(SHOW_PRODUCTS + " to see warehouse inventory");
        System.out.println(SHOW_CLIENTS + " to see the client list");
        System.out.println(SHOW_OUTSTANDING_CLIENTS + " to see clients with outstanding balances");
        System.out.println(RECORD_PAYMENT + " to record a payment from a client");
        System.out.println(BECOME_CLIENT + " to switch to the client menu");
        System.out.println(HELP + " for help");
    }


    public void addClient() {
        String name = getToken("Enter Client name");
        String address = getToken("Enter address");
        String phone = getToken("Enter phone");
        Client result;
        result = warehouse.addClient(name, address, phone);
        if (result == null) {
            System.out.println("Could not add client");
        }
        System.out.println("Client added:\n");
        System.out.printf("%-5s %-15s %-20s %-15s %-9s %-8s%n", "ID", "NAME", "ADDRESS", "PHONE", "BALANCE", "CREDIT");
        System.out.println("==============================================================================");
        System.out.println(result.toString());
        System.out.println("==============================================================================");
    }

    public void showProducts() {
        Iterator allProducts = warehouse.displayProducts();
        System.out.printf("%-5s %-20s %-10s %-10s%n", "ID", "NAME", "STOCK", "PRICE");
        System.out.println("================================================================");
        while (allProducts.hasNext()){
            Product p = (Product)(allProducts.next());
            System.out.println(p.toString());
        }
        System.out.println("================================================================");
    }

    public void showClients() {
        Iterator allClients = warehouse.displayClients();
        System.out.printf("%-5s %-15s %-20s %-15s %-9s %-8s%n", "ID", "NAME", "ADDRESS", "PHONE", "BALANCE", "CREDIT");
        System.out.println("==============================================================================");
        while (allClients.hasNext()){
            Client c = (Client)(allClients.next());
            System.out.println(c.toString());
        }
        System.out.println("==============================================================================");
    }

    public void showOutstandingClients() {
        Iterator allClients = warehouse.displayClients();
        System.out.printf("%-5s %-15s %-20s %-15s %-9s %-8s%n", "ID", "NAME", "ADDRESS", "PHONE", "BALANCE", "CREDIT");
        System.out.println("==============================================================================");
        while (allClients.hasNext()){
            Client c = (Client)(allClients.next());
            // only print if outstanding balance
            if (c.getBalance() > 0) {
                System.out.println(c.toString());
            }
        }
        System.out.println("==============================================================================");
    }

    public boolean becomeClient() {
        String clientId = getToken("Please input the client id: ");
        if (Warehouse.instance().verifyClient(clientId) != null) {
            (WareContext.instance()).setUser(clientId);      
            return true;
        } else {
            System.out.println("Invalid client id."); return false;
        }
    }

    public void terminate(int exitcode) {
        (WareContext.instance()).changeState(exitcode); // exit with a code 
    }

    public void process() {
        int command, exitcode = -1;
        help();
        boolean done = false;
        while (!done) {
            switch (getCommand()) {
                case ADD_CLIENT:                    addClient();
                                                    break;
                case SHOW_PRODUCTS:                 showProducts();
                                                    break;
                case SHOW_CLIENTS:                  showClients();
                                                    break;
                case SHOW_OUTSTANDING_CLIENTS:      showOutstandingClients();
                                                    break;
                case BECOME_CLIENT:                 becomeClient();
                                                    break;
                case HELP:                          help();
                                                    break;
                case EXIT:                          if (becomeClient()){exitcode = 1; done = true;}
                                                    break;
            }
        }
        terminate(exitcode);
    }

    public void run() {
        process();
    }
    
}