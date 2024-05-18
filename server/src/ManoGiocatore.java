////////////////////////////////////////////////////////////////////////
//CLASSE MANOGIOCATORE CONTENENTE LA MANO DI UN GIOCATORE             //
//UTILITA': GESTIONE MANO GIOCATORE(IN BASE ALLE RICHIESTE DEL CLIENT)//
////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.List;

public class ManoGiocatore {
    //lista contenente mano del giocatore
    private List<Carta> manoGiocatore;
    private String combinazioneCarte;

    //ritorna la combinazione della mano 
    public String getCombinazioneCarte() { return combinazioneCarte; }

    //setta una valore alla combinazione delle carte
    public void setCombinazioneCarte(String combinazioneCarte) { this.combinazioneCarte = combinazioneCarte; }

    //costruttore di default
    public ManoGiocatore()
    {
        this.manoGiocatore = new ArrayList<Carta>();
    }

    //metodo per restituire la lista
    public List<Carta> mano()
    {
        return this.manoGiocatore;
    }

    //metodo utile per inserire una carta nella mano del giocatore
    public void push(Carta carta)
    {
        //inserimento carta nella mano del giocatore
        this.manoGiocatore.add(carta);
    }

    //metodo utile ad estrarre carte dal mazzo // l'oggetto carta conterrà un nuovo oggetto Carta creato quando
    //il client invierà al server la richiesta di scartare una determinata carta (passaggio di numero e seme)
    public void pull(Carta carta)
    {
        //for per scorrere tutta la mano del giocatore
        for(int i = 0; i < this.manoGiocatore.size(); i++)
        {
            //se la carta da scartare richiesta dal client è uguale a una delle carte presenti nella propria mano, eliminarla dal mazzo
            //if utile a capire la posizione nella quale si trova la carta da eliminare
            if(carta == this.manoGiocatore.get(i))
                this.manoGiocatore.remove(i);
        }
    }

    //metodo per restituire la size
    public int size()
    {
        return this.manoGiocatore.size();
    }

    //metodo utile a restituire un oggetto carta
    public Carta get(int p)
    {
        return this.manoGiocatore.get(p);
    }

    //metodo utile a restituire l'ultima carta inserita
    public Carta getLast()
    {
        return this.manoGiocatore.get(this.manoGiocatore.size() - 1);
    }

    //metodo utile a svuotare la propria mano (cambio round)
    public void svuotaMano(Mazzo carteScartate)
    {
        for(int i = 0; i < this.manoGiocatore.size(); i++)
            carteScartate.push(this.manoGiocatore.get(i));
        this.manoGiocatore.clear();
    }

    //metodo per mettere insieme la mano del giocatore al flop
    public ManoGiocatore mettiInsiemeMano(Mazzo flop)
    {
        //mano temporanea
        ManoGiocatore tmp = new ManoGiocatore();

        //formazione mazzo nuovo dato flop e mano giocatore
        for(int i = 0; i < this.manoGiocatore.size(); i++)
            tmp.push(this.manoGiocatore.get(i));
        for(int i = 0; i < flop.getSize(); i++)
            tmp.push(flop.getCarta(i));

        return tmp;
    }

}
