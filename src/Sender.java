import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Sender implements Runnable{
    private static DatagramSocket socket;
    private static String hostname;
    public static String msg;
    public static boolean reSend;
    public static boolean returnMsg;
    public static MessageQueue messageQueue;
    public static boolean sendingMessage = false;

    Sender(DatagramSocket socket, String hostname) {
        this.socket = socket;
        this.hostname = hostname;
        messageQueue = new MessageQueue();
    }

    private static void sendMessage(String message) throws Exception {
        byte buf[] = message.getBytes();
        InetAddress address = InetAddress.getByAddress(hostname.getBytes());
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Controller.PORT);
        socket.send(packet);
        //messageQueue = new MessageQueue();
    }

    public void run() {
        do {
            try {
                //if has token and has msg -> send first
                //if dont have token -> put list
                while(Controller.hasToken){
                    //if(!sendingMessage){
                        if(messageQueue.isEmpty()){
//                            sendMessage(String.valueOf(Controller.token));
//                            Controller.hasToken = false;
//                            break;
                            
                        } else {
                            sendMessage("2345" + messageQueue.getFirstMessage());
//                            sendingMessage = true;
                        }
                    //}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!Controller.connected);
    }

    public static void resend() throws IOException {
        byte buf[] = Sender.msg.getBytes();
        InetAddress address = InetAddress.getByName(Sender.hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Controller.PORT);
        socket.send(packet);
        return;
    }

    public static void retransmitir() throws Exception {
        if(Controller.hasToken){
            if(!sendingMessage){
                sendMessage("2345" + messageQueue.getFirstMessage());
//                sendingMessage = true;
            }
        }
    }
}
