import java.util.*;
import java.io.*;
public class Client implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private String address;
  private String phone;
  private String id;
  private static final String CLIENT_STRING = "C";
  private static int idNum = 1;
  private Wishlist wishlist = new Wishlist();
  private List<Order> orders;
  private List<Transaction> transactions;  
  private float balance;
  private float credit;
  public  Client (String name, String address, String phone) {
    this.name = name;
    this.address = address;
    this.phone = phone;
    id = CLIENT_STRING + (ClientIdServer.instance()).getId();
    orders = new LinkedList<Order>();
    transactions = new LinkedList<Transaction>();
    balance = 0;
    credit = 0;
  }

  public String getName() {
    return name;
  }
  public String getPhone() {
    return phone;
  }
  public String getAddress() {
    return address;
  }
  public String getId() {
    return id;
  }
  public float getBalance() {
    return balance;
  }
  public float getCredit() {
    return credit;
  }
  // Update this function with wishlist class
  public Wishlist getWishlist() {
    return wishlist;
  }

  public Iterator<Transaction> getTransactions() {
    return transactions.iterator();
  }

  public Iterator<Order> getOrders() {
    return orders.iterator();
  }
  // Update this function with wishlist class
  public void addToWishlist(WishlistItem item) {
    wishlist.addProduct(item);
  }
  public void setName(String newName) {
    name = newName;
  }
  public void setAddress(String newAddress) {
    address = newAddress;
  }
  public void setPhone(String newPhone) {
    phone = newPhone;
  }
  public void setBalance(float amount) {
    this.balance = amount;
  }
  public void setCredit(float amount) {
    this.credit = amount;
  }
  public void addOrder(Order order) {
    orders.add(order);
  }
  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
  }
  public boolean equals(String id) {
    return this.id.equals(id);
  }
  public String toString() {
    return String.format("%-5s %-15s %-20s %-15s $%-8.2f $%-8.2f", id, name, address, phone, balance, credit);
  }
}
