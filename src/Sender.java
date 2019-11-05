import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Sender implements Runnable{
    private DatagramSocket socket;
    private String hostname;
    private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    public static String msg;
    public static boolean reSend;
    public static ArrayList<String> list = new ArrayList<>();
    public static MessageQueue fila;
    public static boolean returnMsg;

    Sender(DatagramSocket socket, String hostname) {
        this.socket = socket;
        this.hostname = hostname;
    }

    private void sendMessage(String message) throws Exception {
        byte buf[] = message.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Client.PORT);
        socket.send(packet);
    }

    public void run() {
        do {
            try {
                while(Client.hasToken){
                    String sentence;
                    if(returnMsg){
                        sentence = Sender.msg;
                    } else {
                        sentence = in.readLine().trim();
                    }
                    if(sentence.length() > 0)
                        sendMessage(sentence);
                    else
                        System.err.println("Mensagem vazia");
                }
//                }
//                while(!Client.hasToken){
//                    fila.addMessage();
//                }
            } catch (Exception e) { }
        } while (!Client.connected);
    }
}
