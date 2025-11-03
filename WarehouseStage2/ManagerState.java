import java.util.*;
import java.text.*;
import java.io.*;


public class ManagerState extends WareState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Warehouse warehouse;
    private WareContext context;
    private static WareState instance;
    private static final int ADD_PRODUCT = 0;
    private static final int DISPLAY_WAITLIST = 1;
    private static final int RECEIVE_SHIPMENT = 2;
    private static final int BECOME_CLERK = 3;
    private static final int LOGOUT = 4;

    private static final int HELP = 5;


    private ManagerState() {
        super();
        warehouse = Warehouse.instance();
        //context = LibContext.instance();
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
                if (value >= ADD_PRODUCT && value <= HELP) {
                    return value;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        } while (true);
    }
    public void help() {
        System.out.println("Enter a number between 0 and 12 as explained below:");
        System.out.println(ADD_PRODUCT + " to add a product\n");
        System.out.println(DISPLAY_WAITLIST + " to display a waitlist\n");
        System.out.println(RECEIVE_SHIPMENT + " to receive a shipment\n");
        System.out.println(BECOME_CLERK + " to become a clerk\n");
        System.out.println(LOGOUT + " to logout of the system\n");

        System.out.println(HELP + " for help");
    }

    public void addProduct() {
        Product result;
        do {
            String name = getToken("Enter name");
            int quantity = Integer.parseInt(getToken("Enter quantity"));
            float price = Float.parseFloat(getToken("Enter price"));
            result = warehouse.addProduct(name, quantity, price);
            if (result != null) {
                System.out.println(result);
            } else {
                System.out.println("Book could not be added");
            }
            if (!yesOrNo("Add more products?")) {
                break;
            }
        } while (true);
    }

    public void displayWaitlist() {
        String productID = getToken("Enter the product ID");
        productID = productID.toUpperCase();
        Product product = warehouse.verifyProduct(productID);
        if (product == null) {
            System.out.println("No products matching that ID");
        } else {
            System.out.println("WAITLIST FOR PRODUCT: " +  product.getId() + " " + product.getName() + "\n");
            System.out.printf("%-5s %-5s %-20s %-10s %-10s%n",
                    "CID", "PID", "PRODUCT NAME", "PRICE", "QUANTITY");
            System.out.println("================================================================");
            Waitlist wl = product.getWaitlist();
            Iterator<WaitlistItem> wlItems = wl.getWaitlistIterator();
            while (wlItems.hasNext()) {
                WaitlistItem item = wlItems.next();
                System.out.println(item.toString());
            }
            System.out.println("================================================================");
        }
    }

    public void receiveShipment() {
        String productID = getToken("Enter Product ID");
        int quantity = getNumber("Enter quantity");

        Product product = warehouse.invenstory.search(productID);

        if (product == null) {
            System.out.println("ERROR: Product not found");
            return;
        }

        warehouse.processShipment(product, quantity);
        warehouse.displayShipmentConfirmation(product, quantity);
    }

    public void becomeClerk() {
        WareContext.changestate(clerkstate);
    }

    public void logout() {
        terminate(exitcode);
    }

    public void terminate(int exitcode)
    {
        (WareContext.instance()).changeState(exitcode); // exit with a code
    }


    public void process() {
        int command, exitcode = -1;
        help();
        boolean done = false;
        while (!done) {
            switch (getCommand()) {
                case ADD_PRODUCT:        addProduct();
                    break;
                case DISPLAY_WAITLIST:         displayWaitlist();
                    break;
                case RECEIVE_SHIPMENT:      receiveShipment();
                    break;
                case BECOME_CLERK:      //removeBooks();
                    break;
                case LOGOUT:      //processHolds();
                    break;

                case HELP:              help();
                    break;

            }
        }
        terminate(exitcode);
    }
    public void run() {
        process();
    }
}
