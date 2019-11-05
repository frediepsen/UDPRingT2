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
    public static boolean returnMsg;
    public static MessageQueue messageQueue;

    Sender(DatagramSocket socket, String hostname) {
        this.socket = socket;
        this.hostname = hostname;
    }

    private void sendMessage(String message) throws Exception {
        byte buf[] = message.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Controller.PORT);
        socket.send(packet);
    }

    public void run() {
        do {
            try {
                //if has token and has msg -> send first
                //if dont have token -> put list

                if(returnMsg || reSend){
                    String sentence = Sender.msg;
                    returnMsg = false;
                    reSend = false;
                    if(sentence.length() > 0)
                        sendMessage(sentence);
                }
                while(Controller.hasToken){
                    String sentence;
                    if(returnMsg || reSend){
                        sentence = Sender.msg;
                        returnMsg = false;
                        reSend = false;
                        if(sentence.length() > 0)
                            sendMessage(sentence);
                    } else {
                        sentence = in.readLine().trim();
                    }
                    if(sentence.length() > 0)
                        sendMessage(sentence);
                    else
                        System.err.println("Mensagem vazia");
                }
//                }
//                while(!Controller.hasToken){
//                    fila.addMessage();
//                }
            } catch (Exception e) { }
        } while (!Controller.connected);
    }
}
