import java.util.*;
import java.text.*;
import java.io.*;
import java.util.Iterator;
public class UserInterface {
  private static UserInterface userInterface;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private static final int EXIT = 0;
  private static final int ADD_CLIENT = 1;
  private static final int ADD_PRODUCTS = 2;
  private static final int ADD_TO_WISHLIST = 3;
  private static final int SHOW_PRODUCTS = 4;
  private static final int SHOW_CLIENTS = 5;
  private static final int SHOW_WISHLIST = 6;
  private static final int PROCESS_ORDER = 7;
  private static final int SAVE = 8;
  private static final int RETRIEVE = 9;
  private static final int HELP = 10;
  private static final int RECIEVE_PAYMENT = 11;
  private static final int SHOW_WAITLIST = 12;
  private static final int SHOW_TRANSACTIONS = 13;
  private static final int SHOW_ORDERS = 14;
  private static final int RECEIVE_SHIPMENT = 15;

 
  private UserInterface() {
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
        warehouse = Warehouse.instance();
    }
  }
  public static UserInterface instance() {
    if (userInterface == null) {
      return userInterface = new UserInterface();
    } else {
      return userInterface;
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
        int value = Integer.parseInt(getToken("Enter command: " + HELP + " for help"));
        if (value >= EXIT && value <= RECEIVE_SHIPMENT) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 11 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_CLIENT + " to add a client");
    System.out.println(ADD_PRODUCTS + " to add products");
    System.out.println(ADD_TO_WISHLIST + " to add product to clients wishlist");
    System.out.println(SHOW_PRODUCTS + " to display the warehouse inventory");
    System.out.println(SHOW_CLIENTS + " to display the warehouse clients");
    System.out.println(SHOW_WISHLIST + " to display the wishlist of a client");
    System.out.println(PROCESS_ORDER + " to process a client's order");
    System.out.println(SAVE + " to save data");
    System.out.println(RETRIEVE + " to retrieve");
    System.out.println(HELP + " for help");
    System.out.println(RECIEVE_PAYMENT + " to receive payment from a client");
    System.out.println(SHOW_WAITLIST + " to show product waitlist");
    System.out.println(SHOW_TRANSACTIONS + " to show client transactions");
    System.out.println(SHOW_ORDERS + " to show client orders");
    System.out.println(RECEIVE_SHIPMENT + " to receive a shipment");
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

  public void addProducts() {
    Product result;
    do {
      String name = getToken("Enter the product name");
      int stock = Integer.parseInt(getToken("Enter the amount of stock"));
      double price = Double.parseDouble(getToken("Enter the per unit price"));
      result = warehouse.addProduct(name, stock, price);
      if (result != null) {
        System.out.println("Product added:\n");
        System.out.printf("%-5s %-20s %-10s %-10s%n",
        "ID", "NAME", "STOCK", "PRICE");
        System.out.println("================================================================");
        System.out.println(result.toString());
        System.out.println("================================================================");
      } else {
        System.out.println("Product could not be added");
      }
      if (!yesOrNo("Add more products?")) {
        break;
      }
    } while (true);
  }
  public void addToWishlist(){
    WishlistItem result;
    do {
      String productId = getToken("Enter the product ID");
      productId = productId.toUpperCase();
      String clientId = getToken("Enter the client ID");
      clientId = clientId.toUpperCase();
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

  public void showWishlist() {
    String clientId = getToken("Enter the client ID");
    clientId = clientId.toUpperCase();
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

  public void showWaitlist() {
    String productId = getToken("Enter the product ID");
    productId = productId.toUpperCase();
    Product p = warehouse.verifyProduct(productId);
    if (p == null) {
      System.out.println("No products matching that ID");
    } else {
      System.out.println("WAITLIST FOR PRODUCT: " +  p.getId() + " " + p.getName() + "\n");
      System.out.printf("%-5s %-5s %-20s %-10s %-10s%n",
        "CID", "PID", "PRODUCT NAME", "PRICE", "QUANTITY");
      System.out.println("================================================================");
      Waitlist wl = p.getWaitlist();
      Iterator<WaitlistItem> wlItems = wl.getWaitlistIterator();
      while (wlItems.hasNext()) {
        WaitlistItem item = wlItems.next();
        System.out.println(item.toString());
      }
      System.out.println("================================================================");
    }
  }


  public void processClientOrder() {
    String clientId = getToken("Enter the client ID");
    clientId = clientId.toUpperCase();
    Client c = warehouse.verifyClient(clientId);
    if (c == null) {
      System.out.println("No clients matching that ID");
      return;
    }
    if (c.getWishlist().isEmpty()) {
      System.out.println("Client's wishlist is empty");
      return;
    }

    Iterator<WishlistItem> wlItems = warehouse.getClientWishlist(clientId);
    
    // Action Loop -- Confirm, Remove, Change Quantity of wishlist items
    System.out.println("\nConfirm, Update, or Remove items from wishlist:\n");

    while (wlItems.hasNext()) {
      WishlistItem item = wlItems.next();
      System.out.println("\nID  PRODUCT NAME  PRICE   QUANTITY");
      System.out.println("================================================================");
      System.out.println(item.toString());
      System.out.println("================================================================");
      System.out.println("\nPlease Select an action:");
      System.out.println("1 to confirm product and quantity");
      System.out.println("2 to remove item from wishlist");
      System.out.println("3 change quantity of product\n");
      int choice = getNumber("Enter choice:");
      switch (choice) {
        case 1:
          System.out.println("Item confirmed\n");
          break;
        case 2:
          wlItems.remove();
          System.out.println("Item removed from wishlist\n");
          break;
        case 3:
          int newQuant = getNumber("Enter new quantity:");
          warehouse.updateWishlistItemQuantity(clientId, item, newQuant);
          System.out.println("Item quantity updated\n");
          break;
        default:
          System.out.println("Invalid choice, item confirmed by default\n");
      }
    }

    Order order = warehouse.processClientOrder(clientId);
    if (order == null) {
      System.out.println("Order was not processed");
    } else {
      System.out.println("Order processed successfully:");
      System.out.println("\n------------------------------------------------------------");
      System.out.println(order.toString());
    }
  }

  public void showTransactions() {
    String clientId = getToken("Enter the client ID");
    clientId = clientId.toUpperCase();
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

  public void showOrders() {
    String clientId = getToken("Enter the client ID");
    clientId = clientId.toUpperCase();
    Client c = warehouse.verifyClient(clientId);
    if (c == null) {
      System.out.println("No clients matching that ID");
    } else {
      System.out.println("ORDERS FOR CLIENT: " + c.getId() + " " + c.getName());
      System.out.println("------------------------------------------------------------\n\n");
      Iterator<Order> orders = warehouse.getClientOrders(c);
      while (orders.hasNext()) {
        Order o = orders.next();
        System.out.println(o.toString());
      }
    }
  } 
  private void save() {
    if (warehouse.save()) {
      System.out.println(" The warehouse has been successfully saved in the file WarehouseData \n" );
    } else {
      System.out.println(" There has been an error in saving \n" );
    }
  }
  private void retrieve() {
    try {
      Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The warehosue has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void receivePayment() {
    String clientID = getToken("Enter the ClientID"); // accept user input
    clientID = clientID.toUpperCase();
    String userInputClientAmount = getToken("Enter the amount");
    double amount = 0;

    try { // parse user input string into a double
      amount = Double.parseDouble(userInputClientAmount);
    } catch (Exception e) {
      System.out.println("There was an issue with the input");
    }

    Calendar date = getDate("Enter the date (mm/dd/yy)"); // accept date input
    String dateString = date.get(Calendar.MONTH)+1 + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR);

    Client targetClient = warehouse.verifyClient(clientID);
    if (targetClient == null) { // verify client exists
      System.out.println("Client not found");
      return;
    }

    warehouse.receivePayment(targetClient, (float) amount, dateString); // update warehouse records
  }

  public void receiveShipment() {
	  warehouse.receiveShipment();
  }
  public void process() {
    int command;
    help();
    while ((command = getCommand()) != EXIT) {
      switch (command) {
        case ADD_CLIENT:        addClient();
                                break;
        case ADD_PRODUCTS:      addProducts();
                                break;
        case ADD_TO_WISHLIST:   addToWishlist();
                                break;
        case SAVE:              save();
                                break;
        case RETRIEVE:          retrieve();
                                break;
        case SHOW_CLIENTS:	    showClients();
                                break; 		
        case SHOW_PRODUCTS:	    showProducts();
                                break;
        case SHOW_WISHLIST:     showWishlist();
                                break; 		
        case PROCESS_ORDER:     processClientOrder(); 
                                break;
        case HELP:              help();
                                break;
        case RECIEVE_PAYMENT:   receivePayment();
                                break;
        case SHOW_WAITLIST:     showWaitlist();
                                break;  
        case SHOW_TRANSACTIONS: showTransactions(); 
                                break;
        case SHOW_ORDERS:       showOrders(); 
                                break;  
	      case RECEIVE_SHIPMENT:  receiveShipment();
				                        break;
      }
    }
  }
  public static void main(String[] s) {
    UserInterface.instance().process();
  }
}
