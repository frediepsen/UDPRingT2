import javafx.scene.control.Control;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Random;

public class Receiver implements Runnable {
    private static DatagramSocket socket;
    private byte[] buffer;
    private Message m;

    Receiver(DatagramSocket socket) {
        this.socket = socket;
        buffer = new byte[1024];
    }

    public void run() {
                while (true) {
                    try {
                        System.out.println("@");
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        System.out.println(socket.isClosed());
                        socket.receive(packet);
                        System.out.println("@@@");
                        String content = new String(packet.getData(), 0, packet.getLength());
                        System.out.println(content);
//                        System.out.println("@");
//                        if (content.equals("1234")) {
//                            Controller.hasToken = true;
//                            //Controller.time_token = System.currentTimeMillis();
//                            System.out.println("Recebi token");
//                        } else {
//                            m = new Message(content);
//                            if (Controller.apelido.equals(m.getApelidoOrigem())) {
//                                if (m.getControleDeErro().equals("ACK")) {
//                                    System.out.println("Mensagem Recebida Pelo Destinatario");
//                                    Sender.messageQueue.removeMessage();
//                                } else if (m.getControleDeErro().equals("naocopiado")) {
//                                    if (m.getApelidoDestino().equals("TODOS")) {
//                                        System.out.println("Mensagem de Broadcast enviada a todos");
//                                        Sender.messageQueue.removeMessage();
//                                    } else {
//                                        System.out.println("Destinatario nao Existe na Rede");
//                                        Sender.messageQueue.removeMessage();
//                                    }
//                                } else {
//                                    System.out.println("Mensagem Voltou com erro");
//                                    Controller.hasToken = false;
//                                    Sender.msg = "1234";
//                                    Sender.sendMessage("1234");
//                                }
//                            } else {
//                                if (Controller.apelido.equals(m.getApelidoDestino())) {
//                                    System.out.println("Mensagem para mim");
//                                    System.out.println("Apelido de Origem: " + m.getApelidoOrigem());
//                                    System.out.println("Mensagem: " + m.getMensagem());
//
//                                    Random gerador = new Random();
//                                    int erroRandom = gerador.nextInt(11);
//                                    if (erroRandom < 6) {
//                                        m.setMensagem("Erro Randomico");
//                                    }
//
//                                    if (CRC16.calculate_crc(m.getMensagem().getBytes()) == Integer.parseInt(m.getCRC())) {
//                                        m.setControleDeErro("ACK");
//                                    } else {
//                                        m.setControleDeErro("erro");
//                                        m.setCRC(String.valueOf(CRC16.calculate_crc(m.getMensagem().getBytes())));
//                                    }
//
//                                    Sender.msg = "2345;" + m.getControleDeErro()
//                                            + ":" + m.getApelidoOrigem()
//                                            + ":" + m.getApelidoDestino()
//                                            + ":" + m.getCRC()
//                                            + ":" + m.getMensagem();
//                                    Sender.resend();
//
//                                } else if (m.getApelidoDestino().equals("broadcast")) {
//                                    System.out.println("Mensagem Broadcast");
//                                    Sender.resend();
//
//                                } else {
//                                    System.out.println("A mensagem nao eh para mim");
//                                    Sender.resend();
//                                }
//                            }
//                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
    }
}