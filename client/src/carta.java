public class carta 
{
    private String numero;
    private String seme;
    private boolean isScoperta;

    public carta(String numero, String seme, boolean isScoperta)
    {
        this.numero = numero;
        this.seme = seme;
        this.isScoperta = isScoperta;
    }

    public String getNumero() 
    {
        return numero;
    }

    public void setNumero(String numero) 
    {
        this.numero = numero;
    }
    
    public String getSeme() 
    {
        return seme;
    }
    public void setSeme(String seme) 
    {
        this.seme = seme;
    }

    public boolean getIsScoperta() 
    {
        return isScoperta;
    }

    public void setScoperta(boolean isScoperta) 
    {
        this.isScoperta = isScoperta;
    }

}
