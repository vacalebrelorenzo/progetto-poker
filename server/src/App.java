public class App {
    public static void main(String[] args) throws Exception {

        //porta di ascolto
        int port = 666;

        //creazione oggetto comunicazione
        Comunicazione communication = new Comunicazione(port);

        //avvio server
        communication.avviaServer();
        
    }
}
