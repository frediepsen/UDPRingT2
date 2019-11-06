import javax.naming.ldap.Control;
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


                switch (m.getControleDeErro()){
                    case "erro":

                        break;
                    case "ACK":

                        break;
                    case "naocopiado":

                        break;
                    default:
                        // recebeu o token
                        Controller.token = Integer.parseInt(m.getControleDeErro());
                        Controller.hasToken = true;
                        break;
                }

                if(content.equals("1234")){
                    //recebeu token
                    Controller.hasToken = true;
                    System.out.println("Recebi token");
                } else {
                    if(Controller.apelido.equals(m.getApelidoOrigem())){
                        if(m.getControleDeErro().equals("ACK")){
                            System.out.println("Mensagem Recebida Pelo Destinatario");
                            Sender.messageQueue.removeMessage();
                        } else if(m.getControleDeErro().equals("naocopiado")){
                            System.out.println("Destinatario nao Existe na Rede");
                            Sender.messageQueue.removeMessage();
                        }
                        else{
                            System.out.println("Mensagem Voltou com erro");
                            //TODO ENVIAR TOKEN PARA O PROXIMO
                        }
                    } else {
                        if(Controller.apelido.equals(m.getApelidoDestino())){
                            System.out.println("Mensagem para mim");
                            System.out.println("Apelido de Origem: " + m.getApelidoOrigem());
                            System.out.println("Mensagem: " + m.getMensagem());

                            m.setControleDeErro("ACK");
                            m.setCRC("123321");
                            Sender s = new Sender(Controller.socket, Controller.nextMachine);

                            Sender.msg = "1234;" + m.getControleDeErro()
                                    + ":" + m.getApelidoOrigem()
                                    + ":" + m.getApelidoDestino()
                                    + ":" + m.getCRC()
                                    + ":" + m.getMensagem();
                            Sender.returnMsg = true;
                            Sender.resend();
//                            Controller.st = new Thread(s);
//                            Controller.st.start();

                        } else if(m.getApelidoDestino().equals("TODOS")){
                            System.out.println("Mensagem Broadcast");
                            Sender.msg = content;
                            Sender.resend();
                        }
                        else {
                            System.out.println("A mensagem nao eh para mim");
                            Sender.msg = content;
                            Sender.resend();
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