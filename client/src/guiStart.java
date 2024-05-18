import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class guiStart extends JFrame {
    private BufferedImage immagineSfondo;
    private JPanel pannelloSfondo;
    private GridBagConstraints contenitore;
    private JButton start;
    private BufferedImage imgCasino;
    public comunicazione communication;
    public guiGame game;

    public guiStart() throws IOException {
        //sfondo
        immagineSfondo = ImageIO.read(new File("immagini/sfondoStart.jpg"));
        //crea sfondo
        pannelloSfondo = creaPannelloConSfondo();
        //crea contenitore
        contenitore = new GridBagConstraints();
        //set layout a griglia
        pannelloSfondo.setLayout(new GridBagLayout());


        //bottone di inizio
        start = new JButton("Unisciti alla partita");
        //set dimensione
        start.setPreferredSize(new Dimension(200, 50));
        //set font
        start.setFont(new Font("Arial", Font.PLAIN, 20));
        //set colore scritta
        start.setForeground(new Color(184, 134, 11));

        //aggiunta elemento alla finestra
        this.addComponent(350, 20, 0, 0, start);

        //quando premi sul bottone
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // creo un oggetto per comunicare
                    communication = new comunicazione();
                    communication.output("client");

                    //ricezione, salvataggio carte
                    String stringa;
                    String[] carteRicevute;
                    carte listacarte = new carte();
                    carta c;

                    for (int i = 0; i < 2; i++) {
                        // ricevo in input la linea
                        stringa = communication.input();

                        // stampo la carta ricevuta (non utile)
                        System.out.println(stringa);

                        // splitto il vettore
                        carteRicevute = stringa.split(";");

                        // se la carta è scoperta
                        if (carteRicevute[2].equals("true")) {
                            // inizializzo l'oggetto carta con le informazioni utili
                            c = new carta(carteRicevute[0], carteRicevute[1], true);
                        } else {
                            c = new carta(carteRicevute[0], carteRicevute[1], false);
                        }

                        // aggiunta della carta nella mano del giocatore
                        listacarte.addCarta(c);
                    }

                    //ricezione e salvataggio flop banco
                    stringa = communication.input();
                    String[] carteFlop = stringa.split("/");
                    String[] cartaRicevuta;

                    // creazione oggetto mano del giocatore
                    carte flop = new carte();
                    // inserimento carte nella mano del giocatore
                    for (int i = 0; i < 4; i++) {
                        cartaRicevuta = carteFlop[i].split(";");
                        System.out.println(carteFlop[i]);

                        // inizializzo l'oggetto carta con le informazioni utili
                        c = new carta(cartaRicevuta[0], cartaRicevuta[1], true);

                        flop.addCarta(c);
                    }

                    // creo la partita
                    game = new guiGame(communication, listacarte, flop);

                    // Nascondo la finestra di avvio partita
                    setVisible(false);
                    
                    game.play.aspettaInformazioniDalServer();

                    // Inizio il gioco
                    game.isClose = false;

                    // Visualizzo la finestra del gioco con le carte
                    game.setVisible(true);

                    if(game.play.isYourTurn)
                        game.inserisciMex("E' il tuo turno!", "ENTRA");
                    else{
                        game.inserisciErrore("Non è il tuo turno!", "NON PUOI ENTRARE");
                        game.play.aspettaInformazioniDalServer();
                    }

                } catch (IOException e1) {
                    // Gestisci l'eccezione in modo appropriato, ad esempio, mostrando un messaggio di errore.
                    e1.printStackTrace();
                }
            }
        });

        // immagine del casino
        imgCasino = ImageIO.read(new File("immagini/scrittaPoker.png"));
        imgCasino = resizeImage(imgCasino, 600, 300);
        this.addComponent(50, 20, 0, 0, new JLabel(new ImageIcon(imgCasino)));

        // impostazioni di default
        setTitle("Casino.com");
        add(pannelloSfondo);
        setSize(1000, 600);
        // chiudo la finestra e chiudo anche il "programma"
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // metodo che mi permette di posizionare qualsiasi componente e di posizionarlo
    private void addComponent(int daSu, int daSinistra, int daGiu, int daDestra, JComponent component) {
        contenitore.gridx = 0;
        contenitore.gridy = 1;
        // distanze da i margini
        contenitore.insets = new Insets(daSu, daSinistra, daGiu, daDestra);
        pannelloSfondo.add(component, contenitore);
    }

    private JPanel creaPannelloConSfondo() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                disegnaSfondo(g);
            }
        };
    }

 private void disegnaSfondo(Graphics g) 
    {
        if (immagineSfondo != null)
            g.drawImage(immagineSfondo, 0, 0, getWidth(), getHeight(), this);
    }

    //metodo che ridimensiona un'immagine
    private BufferedImage resizeImage(BufferedImage img, int larghezza, int altezza) 
    {
        //creo un'immagine temporanea con le nuove dimensioni
        Image tmp = img.getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);   
        BufferedImage resizedImage = new BufferedImage(larghezza, altezza, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

}