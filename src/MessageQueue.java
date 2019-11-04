import java.net.InetAddress;
import java.util.ArrayList;

public class MessageQueue {
    public ArrayList<String> queue;

    // Adiciona a mensagem na fila
    public void addMessage(String message){
        this.queue.add(message);
    }

    // Retorna a primeira mensagem da fila
    public String removeMessage(){
        String msg = this.queue.remove(0);
        return msg;
    }

    public boolean isEmpty(){
        return queue.size() == 0;
    }

    public Integer getTamanho(){
        return this.queue.size();
    }
}
