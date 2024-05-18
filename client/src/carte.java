import java.util.ArrayList;
import java.util.List;

public class carte 
{
    public List<carta> lista;

    public carte()
    {
        this.lista = new ArrayList<carta>();
    }

    public void addCarta(carta c)
    {
        this.lista.add(c);
    }

    public void svuotaCarte()
    {
        this.lista.clear();
    }

    public int size()
    {
        return this.lista.size();
    }
}
