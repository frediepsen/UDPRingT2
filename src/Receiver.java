import javafx.scene.control.Control;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Receiver implements Runnable {
    private static DatagramSocket socket;
    private byte[] buffer;

    Receiver(DatagramSocket socket) {
//        try{
        this.socket = socket;
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        buffer = new byte[1024];
    }

    public void run() {
            try {
                while (true) {
                    System.out.println("1");
                    if (System.currentTimeMillis() - Controller.time_token > Controller.timeOutToken * 1000) {
                        Controller.hasToken = false;
                        Sender.msg = "1234";
                        Sender.resend();
                    }
                    System.out.println("@");

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    System.out.println("@@");
//                    System.out.println(this.socket.getInetAddress() + ":" + this.socket.getPort());
                    System.out.println(socket.isClosed());
                    socket.receive(packet);

                    System.out.println("@@@");
                    String content = new String(packet.getData(),0,packet.getLength());
                    System.out.println(content);
                    Message m = new Message(content);

                    System.out.println("@");
                    if (content.equals("1234")) {
                        //recebeu token
                        Controller.hasToken = true;
                        Controller.time_token = System.currentTimeMillis();
                        System.out.println("Recebi token");
                    } else {
                        if (Controller.apelido.equals(m.getApelidoOrigem())) {
                            if (m.getControleDeErro().equals("ACK")) {
                                System.out.println("Mensagem Recebida Pelo Destinatario");
                                Sender.messageQueue.removeMessage();
                            } else if (m.getControleDeErro().equals("naocopiado")) {
                                if (m.getApelidoDestino().equals("TODOS")) {
                                    System.out.println("Mensagem de Broadcast enviada a todos");
                                    Sender.messageQueue.removeMessage();
                                } else {
                                    System.out.println("Destinatario nao Existe na Rede");
                                    Sender.messageQueue.removeMessage();
                                }
                            } else {
                                System.out.println("Mensagem Voltou com erro");
                                Controller.hasToken = false;
                                Sender.msg = "1234";
                                Sender.resend();
                            }
                        } else {
                            if (Controller.apelido.equals(m.getApelidoDestino())) {
                                System.out.println("Mensagem para mim");
                                System.out.println("Apelido de Origem: " + m.getApelidoOrigem());
                                System.out.println("Mensagem: " + m.getMensagem());

                                if (CRC16.calculate_crc(m.getMensagem().getBytes()) == Integer.parseInt(m.getCRC())) {
                                    m.setControleDeErro("ACK");
                                } else {
                                    m.setControleDeErro("erro");
                                    m.setCRC(String.valueOf(CRC16.calculate_crc(m.getMensagem().getBytes())));
                                }

//                                Sender s = new Sender(Controller.socket, Controller.nextMachine);

                                Sender.msg = "2345;" + m.getControleDeErro()
                                        + ":" + m.getApelidoOrigem()
                                        + ":" + m.getApelidoDestino()
                                        + ":" + m.getCRC()
                                        + ":" + m.getMensagem();
                                Sender.returnMsg = true;
                                Sender.resend();

                            } else if (m.getApelidoDestino().equals("broadcast")) {
                                System.out.println("Mensagem Broadcast");

                                Random gerador = new Random();
                                int erroRandom = gerador.nextInt(11);
                                if (erroRandom < 6) {
                                    m.setMensagem("Erro Randomico");
                                    Sender.msg = "2345;" + m.getControleDeErro()
                                            + ":" + m.getApelidoOrigem()
                                            + ":" + m.getApelidoDestino()
                                            + ":" + m.getCRC()
                                            + ":" + m.getMensagem();
                                    Sender.resend();
                                } else {
                                    Sender.resend();
                                }

                            } else {
                                System.out.println("A mensagem nao eh para mim");
                                Random gerador = new Random();
                                int erroRandom = gerador.nextInt(11);
                                if (erroRandom < 6) {
                                    m.setMensagem("Erro Randomico");
                                    Sender.msg = "2345;" + m.getControleDeErro()
                                            + ":" + m.getApelidoOrigem()
                                            + ":" + m.getApelidoDestino()
                                            + ":" + m.getCRC()
                                            + ":" + m.getMensagem();
                                    Sender.resend();
                                } else {
                                    Sender.resend();
                                }
                            }
                        }
                    }
//                Controller.connected = true;
                    System.out.println("Estou aqui");
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        System.out.println("FIM");

    }
}