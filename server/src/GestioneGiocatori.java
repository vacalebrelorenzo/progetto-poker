//////////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE GESTIONEGIOCATORI CONTENENTE LA LISTA DEI GIOCATORI                                        //
//UTILITA': GESTIONE DEI GIOCATORI CHE PARTECIPERANNO AL GIOCO, RICONOSCIMENTO CLIENT DATE LE SOCKET//
//////////////////////////////////////////////////////////////////////////////////////////////////////

import java.net.Socket;
import java.util.*;

public class GestioneGiocatori {
    //lista giocatori
    private List<Giocatore> listaGiocatori;

    //costruttore di default
    public GestioneGiocatori()
    {
        this.listaGiocatori = new ArrayList<Giocatore>();
    }

    //metodo per aggiungere un nuovo giocatore alla lista
    public boolean aggiungiGiocatore(Giocatore giocatore, int NUMERO_GIOCATORI)
    {
        //se ci sono meno dei giocatori consentiti
        if(this.listaGiocatori.size() < NUMERO_GIOCATORI) { this.listaGiocatori.add(giocatore); return true; }
        else return false;
    }

    //metodo per ottenere un determinato giocatore
    public int trovaPosizioneClient(Socket tmpSocket)
    {
        return this.posizioneGiocatore(tmpSocket);
    }

    //metodo per ottenere direttamente il giocatore utile
    public Giocatore ottieniGiocatore(Socket socketClient) {
        //scorro lista giocatori
        for (Giocatore giocatore : this.listaGiocatori) {
            //se trovo la stessa socket
            if (giocatore.getSocket().getInetAddress().equals(socketClient.getInetAddress())) {
                return giocatore;
            }
        }
        return null;
    }

    //metodo per ottenere direttamente il giocatore utile
    public Giocatore ottieniGiocatore(int pos) {
        if(pos > -1)
            return this.listaGiocatori.get(pos);
        return null;
    }

    //metodo per controllare se un client già connesso tenta di riconnettersi alla partita
    public boolean controllaDuplicati(Socket sClientTemp)
    {   
        //controllo se il client si è già connesso in precedenza o no
        if(this.posizioneGiocatore(sClientTemp) >= 0)
            return true;
        else 
            return false;
    }

    //metodo per scorrere tutta la lista di giocatori
    private int posizioneGiocatore(Socket sClientTemp)
    {
        //scorrimento lista
        for(int i = 0; i < this.listaGiocatori.size();i++){
            //se client si è già connesso in precedenza
            if(this.listaGiocatori.get(i).getSocket().getInetAddress().equals(sClientTemp.getInetAddress()))
                return i;
        }
        return -1;
    }
    
    //metodo utile a restituire un giocatore dalla lista data la sua posizione in essa
    public Giocatore getGiocatore(int posG)
    {
        return this.listaGiocatori.get(posG);
    }

    //metodo per restituire la grandezza della lista
    public int size() {
        return this.listaGiocatori.size();
    }

    //metodo per eliminare un giocatore dalla partita
    public void pullGiocatore(Socket client)
    {
        //scorro lista
        for(int i = 0; i < this.listaGiocatori.size(); i++)
        {
            //se trovo stessa socket rimuovo il giocatore dalla lista giocatori
            if(client.getInetAddress().equals(this.listaGiocatori.get(i).getSocket().getInetAddress()))
                this.listaGiocatori.remove(i);
        }
    }

    //metodo per eliminare un giocatore dalla partita
    public void pull(int pos)
    {
        //scorro lista
        this.listaGiocatori.remove(pos);
    }
}

