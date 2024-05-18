//////////////////////////////////////////////////////////////////////////////////////////////////////////
//CLASSE CARTA CONTENENTE LE INFORMAZIONI(ATTRIBUTI) DI UNA CARTA E LA SUA POSIZIONE(COPERTA O SCOPERTA)//
//UTILITA': COSTRUZIONE DEL MAZZO DA GIOCO E IMPORTANZA NELLA GRAFICA CLIENT                            //
//////////////////////////////////////////////////////////////////////////////////////////////////////////

public class Carta {
    //informazioni di una carta da poker
    private String numero;
    private String seme;
    //boolean per sapere se la carta dovrà essere coperta o no
    private boolean isFaceUp;
    
    
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////

    //get numero
    public String getNumero() { return numero; }
    //get seme
    public String getSeme() { return seme;}
    //get se carta è scoperta
    public boolean isFaceUp() { return isFaceUp; }

    //costruttore di default
    public Carta(String numero, String seme)
    {
        this.numero = numero;
        this.seme = seme;
        this.isFaceUp = true;
    }

    //metodo utile a vari controlli di implementazione
    public void visCarta()
    {
        System.out.println(this.numero + " ");
        System.out.println(this.seme + " ");
        System.out.println(this.isFaceUp + " ");
    }

    //metodo utile a ritornare se la carta è scoperta o no
    public boolean getIsFacedUp() {
        return this.isFaceUp;
    }

}
