import java.io.*;
import java.net.*;

class ClientUDP {
    public static void main(String args[]) throws Exception {

        FileInputStream fileIn = new FileInputStream("arquivo1.txt");

        byte[] sendData = new byte[1024];
        
        DatagramSocket clientSocket = new DatagramSocket();

        String servidor = "localhost";
        int porta = 7000;

        InetAddress IPAddress = InetAddress.getByName(servidor);

        System.out.println("Tentando enviar arquivo ");
        
        fileIn.read(sendData, 0, sendData.length);

        DatagramPacket dataPacket = new DatagramPacket(sendData,sendData.length, IPAddress, porta);

        clientSocket.send(dataPacket);
        clientSocket.close();
        fileIn.close();

    }
}
