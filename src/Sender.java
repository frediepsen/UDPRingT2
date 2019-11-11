import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Sender implements Runnable{
    private static DatagramSocket socket;
    private static String hostname;
    public static String msg;
    public static boolean aux;
    public static MessageQueue messageQueue;
    public static boolean sendingMessage = false;

    Sender(DatagramSocket socket, String hostname) {
        this.socket = socket;
        this.hostname = hostname;
        messageQueue = new MessageQueue();
    }

    public static void sendMessage(String message) throws Exception {
        messageQueue.addMessage(message);
        processMessage();
    }

    public static void processMessage() throws Exception {
        if(Controller.hasToken && !messageQueue.isEmpty() && !sendingMessage){
            System.out.println(messageQueue.getFirstMessage());
            Sender.resend(messageQueue.getFirstMessage());
            sendingMessage = true;
        }
    }

    public void run() {
        while(true){
            if (System.currentTimeMillis() - Controller.time_token > Controller.timeOutToken * 6000 && Controller.hasToken) {
                Controller.hasToken = false;
                System.out.println("Acabou o tempo do token, enviando para o proximo");
                try {
                    Sender.sendToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //System.out.println(messageQueue.isEmpty());
            if(Controller.hasToken && !messageQueue.isEmpty()){
                try {
                    //System.out.println("enviando msg agora");
                    //aux = false;
                    //processMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void resend(String msg) throws IOException {
        byte buf[] = msg.getBytes();
        InetAddress address = InetAddress.getByName(Sender.hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Controller.PORT);
        socket.send(packet);
    }

    public static void sendToken() throws IOException {
        String token = "1234";
        byte buf[] = token.getBytes();
        InetAddress address = InetAddress.getByName(hostname);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Controller.PORT);
        socket.send(packet);
    }

}
