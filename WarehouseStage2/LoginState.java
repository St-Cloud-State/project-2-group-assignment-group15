import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class LoginState extends WareState implements ActionListener{
  private static final int CLERK_LOGIN = 0;
  private static final int USER_LOGIN = 1;
  private static final int MANAGER_LOGIN = 2;
  private static final int EXIT = 3;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private WareContext context;
  private JFrame frame;
  private static LoginState instance;
  private AbstractButton userButton, logoutButton, clerkButton, managerButton;
  //private ClerkButton clerkButton;
  private LoginState() {
      super();
      /*userButton = new JButton("user");
      clerkButton =  new JButton("clerk");
      logoutButton = new JButton("logout");
      userButton.addActionListener(this);
      logoutButton.addActionListener(this);
      clerkButton.addActionListener(this); */
 //     ((ClerkButton)clerkButton).setListener();
  }

  public static LoginState instance() {
    if (instance == null) {
      instance = new LoginState();
    }
    return instance;
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.userButton)) 
       {//System.out.println("user \n"); 
         this.user();}
    else if (event.getSource().equals(this.logoutButton)) 
       (WareContext.instance()).changeState(3);
    else if (event.getSource().equals(this.clerkButton)) 
       this.clerk();
    else if (event.getSource().equals(this.managerButton))
        this.manager();
  } 

 

  public void clear() { //clean up stuff
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());   
  }  

  private void clerk() {
     //System.out.println("In clerk \n");
    (WareContext.instance()).setLogin(WareContext.IsClerk);
     clear();
    (WareContext.instance()).changeState(0);

  } 
  
  private void user(){
    String userID = JOptionPane.showInputDialog(
                     frame,"Please input the user id: ");
    if (Warehouse.instance().verifyClient(userID) != null){
      (WareContext.instance()).setLogin(WareContext.IsUser);
      (WareContext.instance()).setUser(userID);  
       clear();
      (WareContext.instance()).changeState(1);
    }
    else 
      JOptionPane.showMessageDialog(frame,"Invalid user id.");
  }

  private void manager() {
    String managerID = JOptionPane.showInputDialog(
                       frame, "Please input the manager id: ");
      if (managerID != null && managerID.equals("manager")) {  
      (WareContext.instance()).setLogin(WareContext.IsManager);  
      (WareContext.instance()).setUser(managerID);
      clear();
      (WareContext.instance()).changeState(2);  // Go to ManagerState
    }
    else {
      JOptionPane.showMessageDialog(frame, "Invalid manager id.");
    }
  }


  public void run() {
   
   frame = WareContext.instance().getFrame();
   frame.getContentPane().removeAll();
   frame.getContentPane().setLayout(new FlowLayout());
      userButton = new JButton("user");
      clerkButton =  new ClerkButton();
      managerButton = new JButton("manager");
      logoutButton = new JButton("logout");  
      userButton.addActionListener(this);
      logoutButton.addActionListener(this);
      clerkButton.addActionListener(this);
      managerButton.addActionListener(this);
   frame.getContentPane().add(this.userButton);
   frame.getContentPane().add(this.clerkButton);
   frame.getContentPane().add(this.managerButton);
   frame.getContentPane().add(this.logoutButton);
   frame.setVisible(true);
   frame.paint(frame.getGraphics()); 
   //frame.repaint();
   frame.toFront();
   frame.requestFocus();
   
  }
}
