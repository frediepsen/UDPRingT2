import java.net.InetAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageQueue {
    public LinkedList<String> queue = new LinkedList<>();

    // Adiciona a mensagem na fila
    public void addMessage(String message){
        this.queue.add(message);
    }

    // Retorna a primeira mensagem da fila
    public String removeMessage(){
        String msg = this.queue.removeFirst();
        return msg;
    }

    public boolean isEmpty(){
        return queue.size() == 0;
    }

    public void showQueue(){
        if(!isEmpty()){
            for(int i = 0; i<queue.size(); i++){
                System.out.println(queue.get(i));
            }
        }
    }

    public String getFirstMessage(){
        return queue.getFirst();
    }

    public String getMessage(int index){
        return this.queue.get(index);
    }

    public int size(){ return this.queue.size(); }
}
