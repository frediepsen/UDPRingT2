import java.io.*;
import java.net.*;
import java.io.FileInputStream;

class ServerUDP {
    public static void main(String args[]) throws Exception {

        int porta = 7000;

        FileOutputStream fileOut = new FileOutputStream("arquivo2.txt");
        
        DatagramSocket serverSocket = new DatagramSocket(porta);

        byte[] received = new byte[1024];

        System.out.println("Servidor ativo");

        while (true) {

            DatagramPacket receivePacket = new DatagramPacket(received, received.length);

            serverSocket.receive(receivePacket);
            
            fileOut.write(received, 0, received.length);
            fileOut.close();

        }
    }
}