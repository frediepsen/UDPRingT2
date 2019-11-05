import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver implements Runnable {
    private DatagramSocket socket;
    private byte[] buffer;

    Receiver(DatagramSocket socket) {
        this.socket = socket;
        buffer = new byte[1024];
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
                    Controller.hasToken = true;
                    System.out.println("Recebi token");
                } else {
                    if(Controller.apelido.equals(m.getApelidoOrigem())){
                        System.out.println("Reenviando msg");
                        if(m.getControleDeErro().equals("ACK") || m.getControleDeErro().equals("naocopiado")){
                            System.out.println("Enviando Token...");
                            Controller.hasToken = false;
                            String message = "1234";
                            byte buf[] = message.getBytes();
                            InetAddress address = InetAddress.getByName(Controller.nextMachine);
                            DatagramPacket packet1 = new DatagramPacket(buf, buf.length, address, Controller.PORT);
                            socket.send(packet1);
//                        DatagramSocket socket = new DatagramSocket(Controller.PORT);
//                            Sender s = new Sender(Controller.socket, Controller.nextMachine);
//                            Controller.st = new Thread(s);
//                            Controller.st.start();
                        } else {
                            //erro
                        }
                    } else {
                        if(Controller.apelido.equals(m.getApelidoDestino())){
                            System.out.println("Mensagem pra mim");
                            System.out.println("Apelido de Origem: " + m.getApelidoOrigem());
                            System.out.println("Mensagem: " + m.getMensagem());

                            m.setControleDeErro("ACK");
                            m.setCRC("123321");
//                            m.setApelidoDestino(m.getApelidoOrigem());

                            Sender s = new Sender(Controller.socket, Controller.nextMachine);

                            Sender.msg = "1234;" + m.getControleDeErro()
                                    + ":" + m.getApelidoOrigem()
                                    + ":" + m.getApelidoDestino()
                                    + ":" + m.getCRC()
                                    + ":" + m.getMensagem();
                            Sender.returnMsg = true;

                            Controller.st = new Thread(s);
                            Controller.st.start();

                        } else {
                            System.out.println("nao eh pra mim");
                            Sender s = new Sender(Controller.socket, Controller.nextMachine);
                            Sender.msg = content;
                            Controller.hasToken = false;

                            Sender.reSend = true;
                            Controller.st = new Thread(s);
                            Controller.st.start();
                        }
                    }
                }
                Controller.connected = true;
            } catch(Exception e) {
                System.err.println(e);
            }
        }
    }
}