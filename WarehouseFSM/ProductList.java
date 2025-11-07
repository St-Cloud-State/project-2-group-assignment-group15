import java.util.*;import java.io.*;

public class ProductList implements Serializable {
	private static final long serialVersionUID = 1L;
	private static ProductList productList;
	private List<Product> products = new LinkedList<>();

	// Singleton Pattern
	private ProductList(){}; 
	// Creation 
	public static ProductList instance() {
		if(productList == null) {
			return (productList = new ProductList());
		} else {
			return productList;
		}
	}
	// opject adapter 
	public boolean addProduct(Product product) {
		products.add(product);
		return true;
	}

	public Product search(String id) {
		for (int i = 0; i < products.size(); i++) {
			Product p = products.get(i);
			if (p.equals(id)) {
				return p;
			}
		}
		return null;
	}
	
	public Iterator<Product> getProducts() {
		return products.iterator();
	}
	

	private void writeObject(java.io.ObjectOutputStream output) {
		try {
			output.defaultWriteObject();
			output.writeObject(productList);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	private void readObject(java.io.ObjectInputStream input) {
		try {
			if (productList != null) {
				return;
			} else {
				input.defaultReadObject();
				if (productList == null) {
					productList = (ProductList) input.readObject();
				} else {
					input.readObject();
				}
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
 @Override
	public String toString() {
		return products.toString();
	}
}




