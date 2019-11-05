import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver implements Runnable {
    private DatagramSocket socket;
    private byte[] buffer;

    Receiver(DatagramSocket socket) {
        this.socket = socket;
        buffer = new byte[Client.BUFFER_SIZE];
    }

    public void run() {
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String content = new String(packet.getData()).trim();
                System.out.println(content);
                Message m = new Message(content);

                if(content.equals("1234")){
                    //recebeu token
                    Client.hasToken = true;
                    System.out.println("Recebi token");
                } else {
                    if(Client.apelido.equals(m.getApelidoOrigem())){
                        System.out.println("Reenviando msg");
                        if(m.getControleDeErro().equals("ACK") || m.getControleDeErro().equals("naocopiado")){
                            System.out.println("Enviando Token...");
                            Client.hasToken = false;
                            String message = "1234";
                            byte buf[] = message.getBytes();
                            InetAddress address = InetAddress.getByName(Client.nextMachine);
                            DatagramPacket packet1 = new DatagramPacket(buf, buf.length, address, Client.PORT);
                            socket.send(packet1);
//                        DatagramSocket socket = new DatagramSocket(Client.PORT);
//                            Sender s = new Sender(Client.socket, Client.nextMachine);
//                            Client.st = new Thread(s);
//                            Client.st.start();
                        } else {
                            //erro
                        }
                    } else {
                        if(Client.apelido.equals(m.getApelidoDestino())){
                            System.out.println("Mensagem pra mim");
                            System.out.println("Apelido de Origem: " + m.getApelidoOrigem());
                            System.out.println("Mensagem: " + m.getMensagem());

                            m.setControleDeErro("ACK");
                            m.setCRC("123321");
                            m.setApelidoDestino(m.getApelidoOrigem());

                            Sender s = new Sender(Client.socket, Client.nextMachine);

                            Sender.msg = "1234;" + m.getControleDeErro()
                                    + ":" + m.getApelidoOrigem()
                                    + ":" + m.getApelidoDestino()
                                    + ":" + m.getCRC()
                                    + ":" + m.getMensagem();
                            Sender.returnMsg = true;

                            Client.st = new Thread(s);
                            Client.st.start();

                        } else {
                            System.out.println("nao eh pra mim");
                            Sender.msg = content;
                            Sender s = new Sender(Client.socket, Client.nextMachine);
                            Client.st = new Thread(s);
                            Client.st.start();
                        }
                    }
                }
                Client.connected = true;
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}