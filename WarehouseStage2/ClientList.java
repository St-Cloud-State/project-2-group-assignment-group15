import java.util.*;
import java.io.*;
import java.sql.ClientInfoStatus;
public class ClientList implements Serializable {
  private static final long serialVersionUID = 1L;
  private List<Client> clients = new LinkedList<>();
  private static ClientList clientList;
  private ClientList() {
  }
  public static ClientList instance() {
    if (clientList == null) {
      return (clientList = new ClientList());
    } else {
      return clientList;
    }
  }

  public boolean addClient(Client client) {
    clients.add(client);
    return true;
  }

  public boolean removeClient(Client client) {
    clients.remove(client);
    return true;
  }

  public Client getClientById(String id) {
    for (int i = 0; i < clients.size(); i++) {
      Client c = clients.get(i);
      if (c.equals(id)) {
        return c;
      }
    }

    return null;
  }

  public Iterator<Client> getClients(){
     return clients.iterator();
  }
  
  private void writeObject(java.io.ObjectOutputStream output) {
    try {
      output.defaultWriteObject();
      output.writeObject(clientList);
    } catch(IOException ioe) {
      ioe.printStackTrace();
    }
  }
  private void readObject(java.io.ObjectInputStream input) {
    try {
      if (clientList != null) {
        return;
      } else {
        input.defaultReadObject();
        if (clientList == null) {
          clientList = (ClientList) input.readObject();
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
  public String toString() {
    return clients.toString();
  }
}
