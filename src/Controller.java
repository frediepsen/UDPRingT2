import javafx.fxml.Initializable;

import java.io.File;
import java.net.DatagramSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    public static String apelido = "fred";
    public static int PORT;
    public static String nextMachine;
    public static int timeOutToken;
    public static boolean tokenSender;
    public static Thread st;
    public static Thread rt;
    public static boolean hasToken;
    public static DatagramSocket socket;
    public static boolean connected;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       try {
           File file = new File("C:\\Users\\Usuario\\Desktop\\UDPRingT2\\src\\config");
           Scanner sc = new Scanner(file);
           String host = sc.nextLine();
           String[] firstLine = host.split(":");
           nextMachine = firstLine[0];
           PORT = Integer.parseInt(firstLine[1]);
           apelido = sc.nextLine();
           timeOutToken = Integer.parseInt(sc.nextLine());
           if (sc.nextLine().equals("true")) {
               tokenSender = true;
               hasToken = true;
           } else {
               tokenSender = false;
               hasToken = false;
           }

           socket = new DatagramSocket(PORT);
           Receiver r = new Receiver(socket);
           Sender s = new Sender(socket, nextMachine);
           rt = new Thread(r);
           st = new Thread(s);
           rt.start();
           st.start();
       } catch (Exception e){
           e.printStackTrace();
       }
    }
}
