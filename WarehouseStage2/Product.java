import java.io.*;

public class Product implements Serializable {
	private static long serialVersionUID = 1L;
	private String id;
	private String name;
	private Integer stock;
	private double price;
	private Waitlist waitlist;

	public Product(String id, String name, Integer stock, double price) {
		this.id = id;
		this.name = name;
		this.stock = stock;
		this.price = price;
		waitlist = new Waitlist();

	}	
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getStock() {
		return stock;
	}

	public double getPrice() {
		return price;
	}
	public void addStock(int quantity) {
		this.stock += quantity;
	}
	public void setStock(int quantity) {
		this.stock = quantity;	 			
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public Waitlist getWaitlist() {
		return waitlist;
	}

	public void addToWaitlist(WaitlistItem item) {
		waitlist.addToWaitlist(item);
	}
	public boolean isAvailable() {
		if(stock == null) {
			return false;
		}
		return true;	
	}
	public void removeFromWaitlist(WaitlistItem item) {
		waitlist.removeFromWaitlist(item);
	}

	public boolean equals(String id) {
		return this.id.equals(id);
	}
	public String toString() {
		return String.format("%-5s %-20s %-10d $%-10.2f", id, name, stock, price);
	}
}
