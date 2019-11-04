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
                long time = System.currentTimeMillis();
                while(Client.hasToken && (((time - System.currentTimeMillis())*-1) < Client.timeOutToken * 1000)) {
                    String sentence;
                    if(fila.isEmpty()){
                        sentence = msg;
                    } else {
                        sentence = fila.removeMessage();
                    }
                    if(sentence.length() > 0)
                        sendMessage(sentence);
                    else
                        System.err.println("Mensagem vazia");
                }
                while(!Client.hasToken){
                    fila.addMessage(in.readLine().trim());
                }
            } catch (Exception e) { }
        } while (!Client.connected);
    }
}
