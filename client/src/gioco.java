import java.io.IOException;

public class gioco 
{
    public guiGame game; 
    public boolean isYourTurn;
    public boolean haiVinto;
    public int venti;
    public int cinquanta;
    public int cento;
    
    //costruttore con parametri
    public gioco(guiGame game)
    {
        this.game = game;
        this.isYourTurn = false;
        this.haiVinto = false;
        this.venti = 15;
        this.cinquanta = 10;
        this.cento = 5;
    }

    //metodo che mi permette di scommettere
    public void scommetti(int puntata) throws IOException
    {
        //se ho gia passato non permetto di scommetere in quanto si è gia passato 
        //il turno ed evito anche di mandare continui messaggi al server
        if(!this.game.isPassato && !this.game.isScommesso)
        {
            System.out.println("scommetto");
            this.game.communication.output("scommetti/" + puntata);
            this.game.isScommesso = true; 
        }
        else
            this.game.inserisciErrore("Non puoi scommettere se hai passato!", "Errore");
    }

    //metodo che mi permette di passare 
    public void passa() throws IOException
    {
        //se ho passato non posso scommettere ne ripassare 
        if(!this.game.isScommesso && !this.game.isPassato)
        {
            System.out.println("passo");
            this.game.communication.output("passa/0");
            this.game.isPassato = true;  
        }
        else
            this.game.inserisciErrore("Non puoi passare se hai scommesso!", "Errore");
    }

    //controllo se è il mio turno
    public void aspettaInformazioniDalServer() throws IOException
    {
        String messRicevuto = this.game.communication.input();
        System.out.println(messRicevuto);

        //se il messaggio è true vuol dire che è il tuo turno e puoi giocare
        if(messRicevuto.equals("true"))
        {
            this.isYourTurn = true;
        }
        else if(messRicevuto.equals("false"))
        {
            this.isYourTurn = false;
        }

        String[] tmp = messRicevuto.split("/");

        if(tmp.length > 1){

            if(tmp[2].equals("true"))
            {
                this.game.inserisciMex("HAI VINTO " + tmp[1] + " COIN", "HAI VINTO!!!");
                this.svuotaCarteTurno();
                this.game.nuovoRound();
            }
            else
            {
                this.game.inserisciMex("HAI PERSO, SCARSO", "HAI PERSO!");
                this.svuotaCarteTurno();
                this.game.nuovoRound();
            }
            
        }

    }

    //metodo utile a svuotare le carte per ricevere quelle nuove
    public void svuotaCarteTurno()
    {
        this.game.listaCarteGiocatore.svuotaCarte();
        this.game.flop.svuotaCarte();
    }
}