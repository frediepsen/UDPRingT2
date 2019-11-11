public class Message {
    String token;
    String apelidoOrigem;
    String apelidoDestino;
    String controleDeErro;
    String mensagem;
    String CRC;

    public Message(String content){
        //1234;naocopiado:fred:vini:19385749:Oi Mundo!
        if(!content.contains(";")){
            this.controleDeErro = content;
            return;
        }
        String[] aux = content.split(";");
        this.token = aux[0];
        aux = aux[1].split(":");
        this.controleDeErro = aux[0];
        this.apelidoOrigem = aux[1];
        this.apelidoDestino = aux[2];
        this.CRC = aux[3];
        this.mensagem = aux[4];
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApelidoOrigem() {
        return apelidoOrigem;
    }

    public void setApelidoOrigem(String apelidoOrigem) {
        this.apelidoOrigem = apelidoOrigem;
    }

    public String getApelidoDestino() {
        return apelidoDestino;
    }

    public void setApelidoDestino(String apelidoDestino) {
        this.apelidoDestino = apelidoDestino;
    }

    public String getControleDeErro() {
        return controleDeErro;
    }

    public void setControleDeErro(String controleDeErro) {
        this.controleDeErro = controleDeErro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCRC() {
        return CRC;
    }

    public void setCRC(String CRC) {
        this.CRC = CRC;
    }
}
