///////////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE COMUNICAZIONE CONTENENTE TUTTA LA COMUNICAZIONE CON IL PROTOCOLLO TCP TRA CLIENT E SERVER   //
//UTILITA': RICEVIMENTO/INVIO DATI CON I DIVERSI CLIENT, CREAZIONE GIOCO E GIOCATORI, CUORE DEL GIOCO//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.net.*;

public class Comunicazione {
    //numero massimo di giocatori presenti nella partita
    public static final int NUMERO_GIOCATORI = 1;

    //socket del server
    private ServerSocket serverSocket;

    //porta di collegamento
    private int port;

    //numero client connessi
    private int numeroDiClientConnessi;

    //lista dei giocatori presenti in partita utile a salvare le varie socket
    private GestioneGiocatori listaGiocatori;

    //gioco
    private Gioco gioco;

    //giocatore che deve giocare
    private int turnoGiocatore = 0;

    //contatore temporaneo per invio informazioni client
    private int tmp = 0;

    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////

    //costruttore di default (utile a main)
    public Comunicazione(int port) {
        this.port = port;
        this.numeroDiClientConnessi = 0;
        this.listaGiocatori = new GestioneGiocatori();
    }

    //metodo per avviare la prima volta il server
    public void avviaServer() throws InterruptedException {
        try {
            //inzializzazione socket del server
            this.serverSocket = new ServerSocket(port);

            //mantenimento server in ascolto
            while (true) {
                //accetta connessione da client diversi fino a quando questi diventano 3
                if (this.numeroDiClientConnessi < NUMERO_GIOCATORI) {
                    this.gestisciConnessioneSingoloClient();//metodo gestione singole connessioni iniziali
                } else {
                    if(this.gioco.getStatus() == false) { //se il gioco non è attivo
                        this.gioco.setStatusTrue(); //set stato del gioco a true
                        this.turnoGiocatore = 0; //imposto il turno del giocatore a 0 (tocca al primo giocatore)
                    }
                    else {
                        //leggo le richieste dei client
                        if(this.listaGiocatori.size() >= 1)
                            this.leggiRichiesteDeiClient(); //metodo utile a leggere continuamente tutte le richieste del client
                        else
                        {
                            //se non ci sono giocatori
                            System.out.println("ERRORE: NON CI SONO GIOCATORI!");

                            //termino il programma
                            System.exit(0);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodo gestione singole connessioni iniziali
    private void gestisciConnessioneSingoloClient() throws IOException {

        //inizializzazione socket con il client utile alla gestione del flusso dei dati
        Socket clientSocket = this.serverSocket.accept();

        //RICEVIMENTO INPUT///////////////////////////////////////////////////////////////////////
        //visualizzazione quali giocatori entrano in partita
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        //leggo ciò che mi viene inviato da uno dei client
        String funzioneRichiesta = in.readLine();

        //stampo l'informazione
        System.out.println(funzioneRichiesta);
        //////////////////////////////////////////////////////////////////////////////////////////

        //creazione nuovo giocatore temporaneo
        Giocatore g = new Giocatore(clientSocket);

        //aggiunta giocatore alla lista di giocatori presenti in partita
        this.listaGiocatori.aggiungiGiocatore(g, NUMERO_GIOCATORI);
    
        //incremento numero di client connessi al server
        this.numeroDiClientConnessi++;
        
        //inserimento lista completa dei giocatori all'interno del gioco
        if(numeroDiClientConnessi == NUMERO_GIOCATORI){
            this.gioco = new Gioco(listaGiocatori);//inizio nuovo gioco
        }
    }

    //metodo utile a leggere continuamente tutte le richieste del client
    private void leggiRichiesteDeiClient() throws IOException {

        //definizio socket utile alla trasmissione di dati con i vari giocatori della partita
        Socket s = null;

        //definisco una variabile utile a salvare la posizione del giocatore che ha effettuato una richiesta
        int posClientCheEffettuaRichiesta;

        //se il numero di giocatori è superiore a 1 
        if (NUMERO_GIOCATORI > 1) {
            //impostamento turno corretto
            this.turnoGiocatore = (this.turnoGiocatore % NUMERO_GIOCATORI);
            //mi salvo la socket del giocatore che deve giocare
            s = this.listaGiocatori.getGiocatore(this.turnoGiocatore).getSocket();
        } 
        else 
            //se cè solo un giocatore prendo la sua sola socket
            s = this.listaGiocatori.getGiocatore(0).getSocket();
    
        //salvataggio client che effettua richiesta
        posClientCheEffettuaRichiesta = this.listaGiocatori.trovaPosizioneClient(s);

        //mi salvo la posizione della lista del giocatore che ha effettuato la richiesta
        this.gioco.setPosGiocatoreEffRic(posClientCheEffettuaRichiesta);
        //mi salvo la socket
        this.gioco.setSocketClientTmp(s);
        
        if(this.tmp == 0){
            
            //distribuisco le carte
            this.gioco.distribuisciFlop();
            for(int i = 0; i < this.listaGiocatori.size(); i++)
            {
                //manda carta al giocatore
                this.gioco.trovaGiocatoreEInserisciCartaInMano(i);
                //manda carta al giocatore
                this.gioco.trovaGiocatoreEInserisciCartaInMano(i);
                //manda flop al giocatore
                this.inviaflop(i);
            }
            //incremento tmp
            this.tmp++;
        }

        //invio informazioni al client
        this.invioInformazioniAlClientStato();

        //incremento turno giocatore
        this.turnoGiocatore = (this.turnoGiocatore + 1) % NUMERO_GIOCATORI;

        //se è il turno dell'ultimo giocatore
        if(posClientCheEffettuaRichiesta == this.listaGiocatori.size() - 1)
        {
            //ricevo la sua richiesta e la salvo
            this.riceviRichiestaDalClient(s);

            //eseguo il turno
            this.eseguiTurno(s, posClientCheEffettuaRichiesta);
            
            //assegno il piatto al vincitore
            s = this.gioco.assegnazionePiatto();

            //elimino tutte le carte
            this.gioco.showDown();

            //invio ai vari giocatori se hanno vinto o no la partita
            this.inviaInfoATutti(s);

            //resetto le puntate
            this.gioco.resettaPuntate();

            //"spengo" il gioco
            this.gioco.setStatusFalse();   
            
            //resetto tmp
            this.tmp = 0;
        }
        else //eseguo il turno degli altri
        {
            //ricevo la sua richiesta e la salvo
            this.riceviRichiestaDalClient(s);
            //eseguo il suo turno data la richiesta
            this.eseguiTurno(s, posClientCheEffettuaRichiesta);
        }

        //copio la lista modificata dal gioco nella comunicazione
        this.listaGiocatori = this.gioco.getListaGiocatori();
    }

    //salvataggio funzione richiesta da uno dei client nel gioco
    public void riceviRichiestaDalClient(Socket clientSocket) throws IOException
    {
        //buffer
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //salvo l'input in una variabile
        String funzioneRichiesta = in.readLine();
        //stampo l'input
        System.out.println("INPUT RICEVUTO");
        System.out.println(funzioneRichiesta);
        //salvataggio funzione richiesta dal client in questione
        this.gioco.setFunzioneRichiesta(funzioneRichiesta);
    } 

    //metodo utile ad inviare lo stato del gioco a tutti i client
    private void invioInformazioniAlClientStato() throws IOException
    {
        //scorro tutta la lista dei giocatori
        for(int i = 0; i < this.listaGiocatori.size(); i++)
        {
            //se trovo il giocatore che deve giocare
            if(this.listaGiocatori.getGiocatore(i).getSocket().getInetAddress().equals(this.listaGiocatori.getGiocatore(this.turnoGiocatore).getSocket().getInetAddress()))
                //invio al client in questione che è il suo turno
                this.invioInformazioniAlClient(this.listaGiocatori.getGiocatore(i).getSocket(), "true");
            else
                //se no invio che ha perso
                this.invioInformazioniAlClient(this.listaGiocatori.getGiocatore(i).getSocket(), "false");        
        }
    }

    //metodo utile ad inviare una qualsiasi informazione al client
    public void invioInformazioniAlClient(Socket clientSocket, String messaggio) throws IOException
    {
        //controllo errori
        try { //printwriter per inviare un output
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
            //output effettivo
            output.println(messaggio);

            System.out.println("OUTPUT INVIATO");
            System.out.println(messaggio);
            
        } catch (IOException e) {
            System.out.println("ERRORE INVIO INFORMAZIONI!");
            // Gestione delle eccezioni, ad esempio, log o altre azioni necessarie
            e.printStackTrace();
        }
    }
    //metodo per inviare la flop singolarmente
    public void inviaflop(int posTmp) throws IOException
    {
        //invio informazioni al client
        invioInformazioniAlClient(this.listaGiocatori.getGiocatore(posTmp).getSocket(), this.gioco.flopToString());
    }

    //metodo per inviare le informazioni a ogni client
    public void inviaInfoATutti(Socket vincitore) throws IOException
    {
        //scorro tutta la lista dei giocatori
        for(int i = 0; i < this.listaGiocatori.size(); i++)
        {
            //se trovo il vincitore
            if(this.listaGiocatori.getGiocatore(i).getSocket().getInetAddress().equals(vincitore.getInetAddress()))
                //invio al client in questione che ha vinto la partita
                this.invioInformazioniAlClient(vincitore, "vincita/" + this.gioco.getScommessaTot() + "/true");
            else
                //se no invio che ha perso
                this.invioInformazioniAlClient(this.listaGiocatori.getGiocatore(i).getSocket(), "vincita/" + this.gioco.getScommessaTot() + "/false");
        }
    }

    //metodo per eseguire un turno
    private void eseguiTurno(Socket clientSocket, int posClientCheEffettuaRichiesta) throws IOException
    {
        //se il gioco è attivo
        this.gioco.eseguiMano(); //esegui la mano
    }
}
