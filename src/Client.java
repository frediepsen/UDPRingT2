import java.io.File;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

class Client {
    public static String apelido = "fred";
    public static String host = "192.168.43.96";
    public static int BUFFER_SIZE = 1024;
    public static boolean connected = false;
    public static LinkedList<String> list = new LinkedList<>();
    public static HashMap<String, Message> hash = new HashMap<>();
    public static int PORT;
    public static String nextMachine;
    public static String nickAtual;
    public static int timeOutToken;
    public static boolean tokenSender;
    public static Thread st;
    public static Thread rt;
    public static boolean hasToken;
    public static DatagramSocket socket;

    public static void main(String args[]) throws Exception{
        File file = new File("C:\\Users\\Usuario\\Desktop\\UDPRingT2\\src\\config");
        Scanner sc = new Scanner(file);
        String host = sc.nextLine();
        String[] firstLine = host.split(":");
        nextMachine = firstLine[0];
        PORT = Integer.parseInt(firstLine[1]);
        nickAtual = sc.nextLine();
        timeOutToken = Integer.parseInt(sc.nextLine());
        if(sc.nextLine().equals("true")) {
            tokenSender = true;
        } else{
            tokenSender = false;
        }

        socket = new DatagramSocket(PORT);
        Receiver r = new Receiver(socket);
        Sender s = new Sender(socket, nextMachine);
        rt = new Thread(r);
        st = new Thread(s);
        rt.start();
        st.start();
    }
}
 