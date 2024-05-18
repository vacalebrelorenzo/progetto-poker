import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * classe che mi permette di creare i metodi che mi serviranno per la comunicazione 
 * input = riceve dal server
 * output = invio al server
 * terminateConnection = termina la connessione con il server quando finisce la partita
 */

 public class comunicazione 
 {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;

    public comunicazione() throws UnknownHostException, IOException 
    {
        this.clientSocket = new Socket("127.0.0.1", 666);
        this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        this.output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    //metodo utile a ricevere in input un'informazione
    public String input() throws IOException 
    {
        String messaggioLetto = input.readLine();
        return messaggioLetto;
    }

    //metodo per inviare informazioni al server
    public void output(String mess) 
    {
        output.println(mess);
    }

    //metodo per terminare totalmente la connessione
    public void terminateConnection() throws IOException 
    {
        try {
            this.input.close();
            this.output.close();
            this.clientSocket.close();
        } catch (IOException e) {
            System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            throw e;
        }
    }
}

    

