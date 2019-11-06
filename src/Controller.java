import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


import java.io.File;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    @FXML
    private Button btnColocarFila;

    @FXML
    private TextField etMensagem;

    @FXML
    private Button btnEnviarBroadcast;

    @FXML
    private Button btnEnviarNoOne;

    @FXML
    private Button btnNovoToken;

    @FXML
    private TextField etNome;

    public static String apelido = "fred";
    public static int PORT;
    public static String nextMachine;
    public static int timeOutToken;
    public static boolean tokenSender;
    public static Thread st;
    public static boolean hasToken;
    public static DatagramSocket socket;
    public static boolean connected;
    public static int token;
    public static long time_token;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       try {
           File file = new File("C:\\Users\\Usuario\\Desktop\\UDPRingT2\\src\\config");
           Scanner sc = new Scanner(file);
           String host = sc.nextLine();
           String[] firstLine = host.split(":");
           nextMachine = firstLine[0];
           PORT = Integer.parseInt(firstLine[1]);
           apelido = sc.nextLine();
           timeOutToken = Integer.parseInt(sc.nextLine());
           token = 1;
           time_token = 0;
           Sender.sendingMessage = false;
           if (sc.nextLine().equals("true")) {
               tokenSender = true;
               hasToken = true;
           } else {
               tokenSender = false;
               hasToken = false;
           }
//           DatagramChannel channel = DatagramChannel.open();
//           socket = channel.socket(thiPORT);
           System.out.println(nextMachine);
           System.out.println(PORT);
           socket = new DatagramSocket(PORT);
//           socket = new DatagramSocket(PORT, InetAddress.getByAddress(nextMachine.getBytes()));
       } catch (Exception e){
           e.printStackTrace();
       }

        Sender sender = new Sender(socket, nextMachine);
        Receiver receiver = new Receiver(socket);
        Thread ts = new Thread(sender);
        Thread tr = new Thread(receiver);
        ts.start();
        tr.start();

        btnColocarFila.setOnAction(e ->{
           Sender.messageQueue.addMessage(";naocopiado:" + apelido + ":" + etNome.getText() +":"+ CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
           etMensagem.setText("");
           etNome.setText("");
       });
       btnEnviarBroadcast.setOnAction(e ->{
           Sender.messageQueue.addMessage(";naocopiado:" + apelido + ":broadcast:"+ CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
           etMensagem.setText("");
           etNome.setText("");
       });
       btnEnviarNoOne.setOnAction(e ->{
           Sender.messageQueue.addMessage(";naocopiado:" + apelido + ":ninguem:" + CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
           etMensagem.setText("");
           etNome.setText("");
       });
       btnNovoToken.setOnAction(e ->{
           Controller.token = token * 1234;
       });

    }
}


