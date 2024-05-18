/////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE GIOCATORE CONTENENTE LE INFORMAZIONI UTILI COME LA SOCKET DEI CLIENT E LA PROPRIA MANO//
//UTILITA': PONTE TRA GIOCO E MANO DEL GIOCATORE PER LA GESTIONE DEL GIOCO IN SE               //
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.Socket;

public class Giocatore {
    //salvataggio socket determinato giocatore
    private Socket socketDelGiocatore;

    //oggetto ManoGiocatore per tenere memorizzata la mano di quest'ultimo continuando ad aggiornarla
    private ManoGiocatore manoGiocatore;

    //attributo utile a capire se è il turno del giocatore o no
    private boolean urTurn;

    //attributo utile a capire se il giocatore è ancora in gioco o no
    private boolean statusPresenza = true;

    //attributo puntata del giocatore
    private float puntata = 0;

    //get se giocatore è ancora in partita o no
    public boolean getStatusPresenza() { return statusPresenza; }

    //set presenza giocatore in partita
    public void setStatusPresenza(boolean statusPresenza) { this.statusPresenza = statusPresenza;}

    //costruttore di default
    public Giocatore(Socket socketClient)
    {
        this.socketDelGiocatore = socketClient;
        this.manoGiocatore = new ManoGiocatore();
        this.urTurn = false;
    }

    //get urTurn
    public boolean getUrTurn()
    {
        return this.urTurn;
    }

    //get socket
    public Socket getSocket()
    {
        return this.socketDelGiocatore;
    }

    //get mano giocatore
    public ManoGiocatore getManoGiocatore()
    {
        return this.manoGiocatore;
    }

    //set turno giocatore a true o false
    public void setUrTurn(boolean stato)
    {
        this.urTurn = stato;
    }

    //metodo per aggiungere la puntata
    public void addPuntata(float p)
    {
        this.puntata += p;
    }

    //metodo per sommare puntate
    public void addPuntata2(String p)
    {
        this.puntata += Float.parseFloat(p);
    }

    //metodo get puntata
    public float getPuntata()
    {
        return this.puntata;
    }

    //metodo per resettare la puntata
    public void resettaPuntata()
    {
        this.puntata = 0;
    }

}
