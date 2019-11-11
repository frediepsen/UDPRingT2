import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


import java.io.File;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable{

    @FXML
    private Button btnColocarFila;

    @FXML
    private TextField etMensagem;

    @FXML
    private Button btnEnviarBroadcast;

    @FXML
    private Button btnEnviarNoOne;

    @FXML
    private Button btnShowFila;

    @FXML
    private Button btnNovoToken;

    @FXML
    private TextField etNome;

    public static String apelido;
    public static int PORT;
    public static String nextMachine;
    public static int timeOutToken;
    public static boolean tokenSender;
    public static Thread st;
    public static boolean hasToken;
    public static DatagramSocket socket;
    public static int token;
    public static long time_token;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
       try {
           File file = new File("C:\\Users\\Vianna\\Downloads\\UDPRingT2\\src\\config");
           Scanner sc = new Scanner(file);
           String host = sc.nextLine();
           String[] firstLine = host.split(":");
           nextMachine = firstLine[0];
           PORT = Integer.parseInt(firstLine[1]);
           apelido = sc.nextLine();
           timeOutToken = Integer.parseInt(sc.nextLine());
           token = 1;
           Sender.sendingMessage = false;
           if (sc.nextLine().equals("true")) {
               tokenSender = true;
               hasToken = true;
           } else {
               tokenSender = false;
               hasToken = false;
           }
           System.out.println(nextMachine);
           System.out.println(PORT);

           socket = new DatagramSocket(PORT);
           Receiver receiver = new Receiver(socket);
           Sender sender = new Sender(socket, nextMachine);
           Thread ts = new Thread(sender);
           Thread tr = new Thread(receiver);
           time_token = System.currentTimeMillis();
           ts.start();
           tr.start();

       } catch (Exception e){
           e.printStackTrace();
       }

        btnColocarFila.setOnAction(e ->{
            try {
                System.out.println("Mensagem adicionada na fila");
                Sender.messageQueue.addMessage("2345;naocopiado:" + apelido + ":" + etNome.getText() +":"+ CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
//                Sender.sendMessage("2345;naocopiado:" + apelido + ":" + etNome.getText() +":"+ CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            etMensagem.setText("");
            etNome.setText("");
       });
       btnEnviarBroadcast.setOnAction(e ->{
           try {
               System.out.println("Mensagem adicionada na fila");
               Sender.messageQueue.addMessage("2345;naocopiado:" + apelido + ":TODOS:"+ CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
               //Sender.sendMessage("2345;naocopiado:" + apelido + ":TODOS:"+ CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
           } catch (Exception ex) {
               ex.printStackTrace();
           }
           etMensagem.setText("");
           etNome.setText("");
       });
       btnEnviarNoOne.setOnAction(e ->{
           try {
               System.out.println("Mensagem adicionada na fila");
               Sender.messageQueue.addMessage("2345;naocopiado:" + apelido + ":ninguem:" + CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
               //Sender.sendMessage("2345;naocopiado:" + apelido + ":ninguem:" + CRC16.calculate_crc(etMensagem.getText().getBytes()) + ":" + etMensagem.getText());
           } catch (Exception ex) {
               ex.printStackTrace();
           }
           etMensagem.setText("");
           etNome.setText("");
       });
       btnShowFila.setOnAction(e ->{
           if(Sender.messageQueue.isEmpty()){
               System.out.println("A fila esta vazia");
           }
           else{
               for(int i = 0; i < Sender.messageQueue.size(); i++){
                   Message m = new Message(Sender.messageQueue.getMessage(i));
                   System.out.println((i+1) + ". De: " + m.getApelidoOrigem() + " - Para: " + m.getApelidoDestino() + "\n"
                           + "Mensagem: " + m.getMensagem());
               }
               System.out.println("----------------------------- \n");
           }

       });

       btnNovoToken.setOnAction(e ->{
           Controller.token = token * 1234;
           try{
               Sender.sendToken();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       });

    }
}


