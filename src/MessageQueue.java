import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageQueue {
    public LinkedList<String> queue;

    // Adiciona a mensagem na fila
    public void addMessage(String message){
        this.queue.add(message);
    }

    public void addFirstMessage(String message){
        this.queue.addFirst(message);
    }

    // Retorna a primeira mensagem da fila
    public String removeMessage(){
        String msg = this.queue.removeFirst();
        return msg;
    }

    public boolean isEmpty(){
        return queue.size() == 0;
    }

    public Integer getTamanho(){
        return this.queue.size();
    }

    public String getFirstMessage(){
        return queue.getFirst();
    }
}
