package fileTestComunicazione;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Test {
    public static void main(String[] args) throws Exception {
        String serverHost = "127.0.0.1"; // Indirizzo IP o nome host del server
        int serverPort = 666; // Numero di porta del server

        try (Socket socket = new Socket(serverHost, serverPort)) {
            // Ottenere gli stream di ioutput per la comunicazione con il server
            OutputStream outputStream = socket.getOutputStream();

            // Esempio: Invio di dati al server
            String message = "Ciao, didios!";
            outputStream.write(message.getBytes());

            /*  Esempio: Ricezione di dati dal server
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String receivedMessage = new String(buffer, 0, bytesRead);
            System.out.println("Messaggio ricevuto dal server: " + receivedMessage);
            */

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

