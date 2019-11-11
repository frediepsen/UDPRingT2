import javafx.scene.control.Control;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Random;

public class Receiver implements Runnable {
    private static DatagramSocket socket;
    private int countError=0;
    private byte[] buffer;
    private Message m;

    Receiver(DatagramSocket socket) {
        this.socket = socket;
        buffer = new byte[1024];
    }

    public void run() {
                while (true) {
                    try {
                        DatagramPacket packet = new DatagramPacket(buffer,0, buffer.length);
                        socket.receive(packet);
                        String content = new String(packet.getData(), 0, packet.getLength());
                        System.out.println(content);

                        if (content.equals("1234")) {
                            Controller.hasToken = true;
                            Controller.time_token = System.currentTimeMillis();
                            System.out.println("Recebi o token");
                        }
                        else {
                            m = new Message(content);
                            if (Controller.apelido.equals(m.getApelidoOrigem())) {

                                if (m.getControleDeErro().equals("ACK")) {
                                    System.out.println("Mensagem Recebida Pelo Destinatario");
                                    Sender.messageQueue.removeMessage();
                                    Sender.sendingMessage = false;
                                    Controller.hasToken = false;
                                    Sender.sendToken();
                                }
                                else if (m.getControleDeErro().equals("naocopiado")) {

                                    if (m.getApelidoDestino().equals("TODOS")) {
                                        System.out.println("Mensagem de Broadcast enviada a todos");
                                        Sender.messageQueue.removeMessage();
                                        Sender.sendingMessage = false;
                                        Controller.hasToken = false;
                                        Sender.sendToken();
                                    } else {
                                        System.out.println("Destinatario nao Existe na Rede");
                                        Sender.messageQueue.removeMessage();
                                        Sender.sendingMessage = false;
                                        Controller.hasToken = false;
                                        Sender.sendToken();
                                    }

                                } else {
                                    countError++;
                                    if(countError==2){
                                        System.out.println("Mensagem voltou com erro duas vezes, enviandotoken para a proxima maquina");
                                        Sender.messageQueue.removeMessage();
                                        Sender.sendingMessage = false;
                                        Controller.hasToken = false;
                                        Sender.sendToken();
                                    }
                                    else{
                                        System.out.println("Mensagem voltou com erro, tentando reenviar");
                                    }
                                }
                            } else {
                                if (Controller.apelido.equals(m.getApelidoDestino())) {
                                    System.out.println("Mensagem para mim");
                                    System.out.println("Apelido de Origem: " + m.getApelidoOrigem());
                                    System.out.println("Mensagem: " + m.getMensagem());

                                    Random gerador = new Random();
                                    int erroRandom = gerador.nextInt(11);
                                    if (erroRandom < 5) {
                                        m.setMensagem("Erro Randomico");
                                    }

                                    if (CRC16.calculate_crc(m.getMensagem().getBytes()) == Integer.parseInt(m.getCRC())) {
                                        m.setControleDeErro("ACK");
                                    } else {
                                        m.setControleDeErro("erro");
                                        m.setCRC(String.valueOf(CRC16.calculate_crc(m.getMensagem().getBytes())));
                                    }

                                    String msg = "2345;" + m.getControleDeErro()
                                            + ":" + m.getApelidoOrigem()
                                            + ":" + m.getApelidoDestino()
                                            + ":" + m.getCRC()
                                            + ":" + m.getMensagem();
                                    Sender.resend(msg);

                                } else if (m.getApelidoDestino().equals("TODOS")) {
                                    System.out.println("Mensagem Broadcast");
                                    Sender.resend(content);

                                } else {
                                    System.out.println("A mensagem nao eh para mim");
                                    Sender.resend(content);
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
    }
}