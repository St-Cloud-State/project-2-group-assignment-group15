import java.util.*;
import java.text.*;
import java.io.*;
public class ClientState extends WareState {
    private static ClientState clientState;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private static final int EXIT = 0; // logout
    private static final int SHOW_CLIENT_DETAILS = 1;
    private static final int SHOW_PRODUCTS = 2;
    private static final int SHOW_TRANSACTIONS = 3;
    private static final int ADD_TO_WISHLIST = 4;
    private static final int SHOW_WISHLIST = 5;
    private static final int PLACE_ORDER = 6;
    private static final int HELP = 7;

    private ClientState() {
        warehouse = Warehouse.instance();
    }

    public static ClientState instance() {
        if (clientState == null) {
            return clientState = new ClientState();
        } else {
            return clientState;
        }
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
        System.out.println("Enter a number between 0 and 7 as explained below:");
        System.out.println(EXIT + " to Logout\n");
        System.out.println(SHOW_CLIENT_DETAILS + " to see client's details");
        System.out.println(SHOW_PRODUCTS + " to see the warehouse inventory");
        System.out.println(SHOW_TRANSACTIONS + " to see the client's transactions");
        System.out.println(ADD_TO_WISHLIST + " to add products to wishlist");
        System.out.println(SHOW_WISHLIST + " to see the client's wishlist");
        System.out.println(HELP + " for help");
    }


    public void showClientDetails() {
        String clientId = WareContext.instance().getUser();
        Client c = warehouse.verifyClient(clientId);
        System.out.printf("%-5s %-15s %-20s %-15s %-9s %-8s%n", "ID", "NAME", "ADDRESS", "PHONE", "BALANCE", "CREDIT");
        System.out.println("==============================================================================");
        System.out.println(c.toString())
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

    public void showClientTransactions() {
        String clientId = WareContext.instance().getUser();
        Client c = warehouse.verifyClient(clientId);
        if (c == null) {
        System.out.println("No clients matching that ID");
        } else {
            System.out.println("TRANSACTIONS FOR CLIENT: " +  c.getId() + " " + c.getName());
            System.out.printf("\n\n%-5s %-12s %-8s %-10s %-12s %-10s%n", "ID", "DATE", "CLIENT", "AMOUNT", "START BAL", "END BAL");
            System.out.println("================================================================");
            Iterator<Transaction> transactions = warehouse.getClientTransactions(c);
            while (transactions.hasNext()) {
            Transaction t = transactions.next();
            System.out.println(t.toString());
            }
        System.out.println("================================================================");
        }

    }

    public void addToWishlist() {
        WishlistItem result;
        do {
            String productId = getToken("Enter the product ID");
            productId = productId.toUpperCase();
            String clientId = WareContext.instance().getUser();
            int quant = Integer.parseInt(getToken("Enter the quantity"));
            result = warehouse.addProductToWishlist(clientId, productId, quant);
            String clientName = warehouse.verifyClient(clientId).getName();
            if (result != null) {
                System.out.println(String.format("Product added to wishlist of %s, %s:\n", clientId, clientName));
                System.out.printf("%-5s %-20s %-10s %-10s%n",
                    "ID", "PRODUCT NAME", "PRICE", "QUANTITY");
                System.out.println("================================================================");
                System.out.println(result.toString());
                System.out.println("================================================================");
            } else {
                System.out.println("Product could not be added");
            }
            if (!yesOrNo("Add more products to wishlist?")) {
                break;
            }
        } while (true);
    }

    public void displayWishlist() {
        String clientId = WareContext.instance().getUser(); 
        Client c = warehouse.verifyClient(clientId);
        if (c == null) {
            System.out.println("No clients matching that ID");
        } else {
            System.out.println("WISHLIST FOR CLIENT: " +  c.getId() + " " + c.getName() + "\n");
            System.out.printf("%-5s %-20s %-10s %-10s%n",
                "ID", "PRODUCT NAME", "PRICE", "QUANTITY");
            System.out.println("================================================================");
            Wishlist wl = c.getWishlist();
            Iterator<WishlistItem> wlItems = wl.getWishlistIterator();
            while (wlItems.hasNext()) {
                WishlistItem item = wlItems.next();
                System.out.println(item.toString());
            }
            System.out.println("================================================================");
        }
    }

    public void logout() {
        if ((WareContext.instance()).getLogin() == WareContext.IsClerk) { 
            //stem.out.println(" going to clerk \n ");
         (WareContext.instance()).changeState(1); // exit with a code 1
        }
        else if (WareContext.instance().getLogin() == WareContext.IsUser) {  
            //stem.out.println(" going to login \n");
            (WareContext.instance()).changeState(0); // exit with a code 2
        } 
        else {
            (WareContext.instance()).changeState(2); // exit code 2, indicates error
        }
    }

    public void process() {
        int command;
        help();
        while ((command = getCommand()) != EXIT) {
            switch (command) {
                case SHOW_CLIENT_DETAILS:   showClientDetails();
                                            break;
                case SHOW_PRODUCTS:         showProducts();
                                            break;
                case SHOW_TRANSACTIONS:     showClientTransactions();
                                            break;
                case ADD_TO_WISHLIST:       addToWishlist();
                                            break;
                case SHOW_WISHLIST:         displayWishlist();
                                            break;
                case HELP:                  help();
                                            break;
            }
        }
        logout();
    }

    public void run() {
        process();
    }
}