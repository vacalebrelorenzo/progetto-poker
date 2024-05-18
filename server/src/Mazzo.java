///////////////////////////////////////////////////////////////////////////////
//CLASSE MAZZO CONTENENTE LA LISTA DI UN MAZZO(MAZZO DA GIOCO E MAZZO SCARTI)//
//UTILITA': GESTIONE GENERALE MAZZO                                          //
///////////////////////////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazzo {
    //lista di carte (mazzo)
    private List<Carta> mazzo;

    //costruttore di default
    public Mazzo()
    {
        this.mazzo = new ArrayList<Carta>();
    }

    //metodo per svuotare il mazzo
    public void svuota(Mazzo mazzoScarti)
    {
        //svuota tutto il flop
        while(!this.mazzo.isEmpty()) 
            mazzoScarti.push(this.mazzo.remove(0));
    }

    public void riempiMazzo()
    {
        //svutamento mazzo per maggiore sicurezza e meno problemi
        this.mazzo.clear();

        //vettori contenenti le informazioni utili alla creazione delle carte
        String[] semi = {"cuori", "quadri", "fiori", "picche"};
        String[] numeri = {"1","2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"};

        //riempimento mazzo con tutte le carte utili
        //foreach per scorrere tutti i semi e poi tutti i numeri
        for (String seme : semi) {
            for (String numero : numeri) {
                mazzo.add(new Carta(numero, seme));
            }
        }
    }

    //metodo utile a mischiare il mazzo
    public void mischiaMazzo()
    {
        //mischio il mazzo
        Collections.shuffle(mazzo);
    }

    //metodo utile per inserire una carta al mazzo
    public void push(Carta carta)
    {
        //inserimento carta nel mazzo
        this.mazzo.add(carta);
    }

    //metodo utile per inserire una carta al mazzo
    public void pushMano(ManoGiocatore mano)
    {
        for(int i = 0; i < mano.size(); i++)
            this.mazzo.add(mano.get(i));
    }

    //metodo utile a riempire il flop
    public void riempiFlop(Mazzo mazzoGioco)
    {
        this.mazzo.add(mazzoGioco.pull());
        this.mazzo.add(mazzoGioco.pull());
        this.mazzo.add(mazzoGioco.pull());
        this.mazzo.add(mazzoGioco.pull());
    }

    //metodo utile ad estrarre carte dal mazzo
    public Carta pull()
    {
        if (!this.mazzo.isEmpty()) 
            return this.mazzo.remove(this.mazzo.size() - 1);
        return null;  
    }

    //metodo per restituire la size
    public int getSize()
    {
        return this.mazzo.size();
    }

    //metodo per restituire una carta
    public Carta getCarta(int pos)
    {
        return this.mazzo.get(pos);
    }

    //metodo per trasformare il mazzo in una string
    public String flopToString()
    {
        String tmp = "";
        for(int i = 0; i < this.mazzo.size(); i++)
        {
            if(i == this.mazzo.size() - 1)
                tmp += this.mazzo.get(i).getNumero() + ";" + this.mazzo.get(i).getSeme() + ";" + this.mazzo.get(i).getIsFacedUp();
            else
                tmp += this.mazzo.get(i).getNumero() + ";" + this.mazzo.get(i).getSeme() + ";" + this.mazzo.get(i).getIsFacedUp() + "/";
        }
        return tmp;
    }
}
