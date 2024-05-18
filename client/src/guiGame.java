//UTILI
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.swing.*;
/////////////////////////////////

//classe finestra gioco
public class guiGame extends JFrame {

    //immagine dello sfondo
    private BufferedImage immagineSfondo; 

    //pannello sfondo
    private JPanel pannelloSfondo;

    //contenitore generale
    private GridBagConstraints contenitore;

    //menu a tendina
    private JComboBox<String> menuTendina;

    //IMMAGINI GENERALI
    //giocatori
    private BufferedImage imgGiocatore;

    //dealer
    private BufferedImage imgDealer;

    //immagini listaCarteGiocatore
    private BufferedImage imgcarta;
    //////////////////////////////

    //IMMAGINI FISH
    //20
    private BufferedImage img20;

    //50
    private BufferedImage img50;

    //100
    private BufferedImage img100;
    ////////////////////////////

    //"CONTENITORI FISH"
    private JLabel containerFish20;
    private JLabel containerFish50;
    private JLabel containerFish100;
    //////////////////////////////

    //container puntata visualizzata
    private JLabel visualizzaPuntata;

    //METODI UTILI ALLA LOGICA DEL GIOCO
    public boolean isClose;
    public boolean isScommesso;
    public boolean isPassato;
    public boolean isAbbandonato;
    public boolean isOver;
    //////////////////////////////////////

    //BOTTONI
    private JButton scommetti;
    private JButton passa;
    //////////////////////////
        
    //puntata
    public int puntata;

    //font generale del testo
    private Font fontTesto;

    //attributi utili al funzionamento del gioco
    public comunicazione communication;
    public gioco play;
    public carte listaCarteGiocatore;
    public carte flop;

    //costruttore con parametri
    public guiGame(comunicazione communication, carte listaCarteGiocatore, carte flop) throws IOException {
        this.communication = communication;
        this.play = new gioco(this);
        this.listaCarteGiocatore = listaCarteGiocatore;
        this.flop = flop;
        this.isClose = true;
        this.isScommesso = false;
        this.isAbbandonato = false;
        this.isOver = false;
        this.puntata = 0;
        this.fontTesto = new Font("Arial", Font.PLAIN, 20);
        this.visualizzaPuntata = new JLabel("Puntata = 0");

        //metodo che inizializza tutta l'interfaccia grafica del gioco
        this.initUI();

        //imposta titolo
        this.setTitle("Casino.com");

        //add pannello sfondo
        this.add(this.pannelloSfondo);

        //set grandezza finestra da gioco
        this.setSize(1500, 900);

        //se l'utente chiude la finestra principale, il programma termina
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //metodo per mostrare la mano del giocatore
        this.mostraManoGiocatore();

        //metodo per mostrare la flop sul bancone
        this.mostraFlopSulBanco();
    }

    //metodo utile a iniziare un nuovo round
    public void nuovoRound() throws IOException
    {
        guiGame game = null;
        
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
        game = new guiGame(this.communication, listacarte, flop);

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
    }

    //metodo contenente altri metodi per sistemare e inizializzare per intero la finestra di gioco
    private void initUI() throws IOException
    {
        //creazione sfondo gioco
        this.setupBackground();

        //creazione bottoni
        this.setupBottoni();

        //creazione menu a tendina
        this.setupMenu();

        //creazione tutte le immagini
        this.setupImmagini();

        //aggiungo label puntata
        this.aggiornaFish();
    }

    ////////////////////////////////////////////////////////////////////////////
    //////////////////////METODO UTILE ALLO SFONDO//////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //metodo utile a sistemare lo sfondo
    private void setupBackground() throws IOException {
        try {
            //lettura percorso file e salvataggio immagine
            this.immagineSfondo = ImageIO.read(new File("immagini/tavolo.jpg"));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        //creazione pannello sfondo
        this.pannelloSfondo = this.creaPannelloConSfondo();

        //creazione contenitore
        this.contenitore = new GridBagConstraints();

        //crea un layout a griglia
        this.pannelloSfondo.setLayout(new GridBagLayout());
    }
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////
    //////////////////////METODI UTILI AI BOTTONI///////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //metodo utile a sistemare i bottoni
    private void setupBottoni() {
        //creazione bottone scommetti
        this.scommetti = createButton("Scommetti", e -> {
            try {
                //logica bottone scommetti
                this.handleScommettiButton();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        //aggiunta bottone scommetti alla finestra
        this.addComponent(600, 1172, 0, 0, this.scommetti);

        //creazione bottone passa
        this.passa = this.createButton("Passa", e -> {
            try {
                //logica bottone passa
                this.handlePassaButton();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        //aggiunta bottone passa alla finestra
        this.addComponent(700, 1172, 0, 0, this.passa);
    }

    //metodo per creare i bottoni
    private JButton createButton(String text, ActionListener actionListener) {
        //creo bottone
        JButton button = new JButton(text);

        //sistemazione grafica
        this.sistemaBottoni(button);

        //aggiunta evento necessario
        button.addActionListener(actionListener);

        //restituisco il bottone creato
        return button;
    }

    //metodo utile alla logica del bottone per scommettere
    private void handleScommettiButton() throws IOException {

        //se è il tuo turno giochi
        if (this.play.isYourTurn) {

            //imposta la scommessa
            this.play.scommetti(this.puntata);

            //attesa per ricevere delle informazioni dal server
            //this.play.aspettaInformazioniDalServer();

            //attesa per ricevere delle informazioni dal server
            this.play.aspettaInformazioniDalServer();

            //set puntata iniziale a 0
            this.puntata = 0;
        } 
        else //se no ti attacchi
        {
            //errore
            this.inserisciErrore("NON E' IL TUO TURNO", "NON ENTRA");

            //inzio ad aspettare una nuova informazione per poter giocare
            this.play.aspettaInformazioniDalServer();
        }
    }
    
    //metodo utile alla logica del bottone per passare
    private void handlePassaButton() throws IOException {

        //se è il tuo turno gioca
        if (this.play.isYourTurn) {

            //passa il turno
            this.play.passa();

            //attesa per ricevere delle informazioni dal server
            //this.play.aspettaInformazioniDalServer();

            //attesa per ricevere delle informazioni dal server
            this.play.aspettaInformazioniDalServer();
        } 
        else //se no ti attacchi
        {
            //errore
            this.inserisciErrore("NON E' IL TUO TURNO", "NON ENTRA");

            //inzio ad aspettare una nuova informazione per poter giocare
            this.play.aspettaInformazioniDalServer();
        }
    }
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////
    //////////////////////METODO UTILE AL MENU//////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //metodo utile alla costruzione del menù a tendina
    private void setupMenu() {

        //vettore contenente le varie funzionalità
        String[] opzioniMenu = {"Menù Funzionalità", "Regolamento", "Abbandona partita"};

        //inzializzazione menù
        this.menuTendina = new JComboBox<>(opzioniMenu);

        //set dimensioni desiderate
        this.menuTendina.setPreferredSize(new Dimension(200, 40));

        //set font desiderato
        this.menuTendina.setFont(this.fontTesto);

        //aggiunta menù alla finestra
        this.addComponent(0, 1200, 700, 0, this.menuTendina);

        //aggiunta evento
        this.menuTendina.addActionListener(e -> {
            try {

                //regole
                this.actionRules();

                //abbandona partita
                this.leftGame();
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
    }
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////
    //////////////////////METODI UTILI PER LE IMMAGINI//////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    //metodo utile a sistemare le varie immagini
    private void setupImmagini() throws IOException {
        try {

            //costruire immagini dei giocatori
            this.setupGiocatoreImage();

            //costruire le immagini delle fish
            this.setupFishImages();

            //costruire immagine dealer
            this.setupDealerImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //metodo utile alla costruzione delle immagini dei giocatori
    private void setupGiocatoreImage() throws IOException {

        //salvataggio immagine sistemata graficalmente 
        this.imgGiocatore = loadImageAndResize("immagini/imgGiocatore.png", 70, 70);

        //aggiunta tutte immagini dei giocatori
        this.addComponent(60, 1000, 0, 0, new JLabel(new ImageIcon(this.imgGiocatore)));
        this.addComponent(620, 28, 0, 0, new JLabel(new ImageIcon(this.imgGiocatore)));
        this.addComponent(60, 0, 0, 970, new JLabel(new ImageIcon(this.imgGiocatore)));
    }
    
    //metodo utile alla sistemazione delle fish nella finestra
    private void setupFishImages() throws IOException {

        //creazione e sistemazione immagine fish 20
        this.img20 = this.loadImageAndResize("immagini/20.png", 102, 77);

        //creazione e sistemazione immagine fish 50
        this.img50 = this.loadImageAndResize("immagini/50.png", 102, 77);

        //creazione e sistemazione immagine fish 100
        this.img100 = this.loadImageAndResize("immagini/100.png", 102, 77);
    
        //inizializzazione container lbl per le fish
        this.containerFish20 = this.createFishLabel(this.img20, 20);
        this.containerFish50 = this.createFishLabel(this.img50, 50);
        this.containerFish100 = this.createFishLabel(this.img100, 100);

        //aggiunta fish alla finestra
        this.addComponent(400, 970, 0, 0, this.containerFish20);
        this.addComponent(400, 1170, 0, 0, this.containerFish50);
        this.addComponent(400, 1370, 0, 0, this.containerFish100);
    }
    
    //metodo utile all'inserimento dell'immagine del dealer nella finestra
    private void setupDealerImage() throws IOException {

        //salvataggio immagine dealer e sistemazione
        this.imgDealer = this.loadImageAndResize("immagini/luigi.png", 289, 200);

        //aggiunta immagine dealer alla finestra
        this.addComponent(0, 20, 630, 0, new JLabel(new ImageIcon(this.imgDealer)));
    }
    
    //metodo utile a caricare e sistemare un'immagine
    private BufferedImage loadImageAndResize(String filePath, int width, int height) throws IOException {

        //salvataggio immagine
        BufferedImage image = ImageIO.read(new File(filePath));

        //ritorno immagine sistemata
        return this.resizeImage(image, width, height);
    }

    //metodo utile a scalare nel modo corretto un'immagine
    private BufferedImage resizeImage(BufferedImage img, int larghezza, int altezza) {

        //instanza per immagine
        Image tmp = img.getScaledInstance(larghezza, altezza, Image.SCALE_SMOOTH);

        //salvataggio immagine 
        BufferedImage resizedImage = new BufferedImage(larghezza, altezza, BufferedImage.TYPE_INT_ARGB);

        //creazione immagine geometricamente migliore
        Graphics2D g2d = resizedImage.createGraphics();

        //disegno immagine
        g2d.drawImage(tmp, 0, 0, null);

        //libero risorse grafiche
        g2d.dispose();

        //ritorno immagine sistemata
        return resizedImage;
    }

    //metodo utile a sistemare l'aspetto grafico dei bottoni
    private void sistemaBottoni(JButton b) {

        //se grandezza
        b.setPreferredSize(new Dimension(200, 50));

        //set font
        b.setFont(this.fontTesto);

        //set colore background
        b.setBackground(Color.WHITE);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    //////////////////////METODI UTILI ALLLE FISH//////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //metodo utile alla creazione della label che conterrà le fish
    private JLabel createFishLabel(BufferedImage image, int fishValue) {

        //creazione effettiva della label
        JLabel fishLabel = new JLabel(new ImageIcon(image));
    
        //aggiunta di un listener per il click del mouse
        fishLabel.addMouseListener(new MouseAdapter() {
            //quando il mouse viene cliccato
            @Override
            public void mouseClicked(MouseEvent e) {

                //aggiorno label
                aggiornaFish();

                //logica al click del mouse
                handleFishClick(fishValue);
            }
    
            //evidenzio l'immagine quando ci passo sopra con il mouse
            @Override
            public void mouseEntered(MouseEvent e) {
                //aggiunta bordo colorato
                fishLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
    
            //tolgo evidenziamento immagine quando non sono più sopra con il mouse
            @Override
            public void mouseExited(MouseEvent e) {
                //rimuovi l'effetto di evidenziazione quando il mouse esce dall'area della freccetta
                fishLabel.setBorder(null);
            }
    
            //evidenzio l'immagine quando premo il tasto sinistro del mouse
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    //aggiunta bordo colorato
                    fishLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                }
            }
    
            //rimuovo l'effetto di evidenziazione di quando la fish è premuta
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    //aggiunta bordo colorato
                    fishLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                }
            }
        });
    
        //ritorno la fish
        return fishLabel;
    }    
    
    //metodo utile a far funzionare nel modo corretto le fish
    private void handleFishClick(int fishValue) {

        //se è il tuo turno
        if (this.play.isYourTurn) {

            //se viene premuta la fish 20 e ho giocate disponibili
            if (fishValue == 20 && this.play.venti > 0) {

                //aumento la puntata 
                this.puntata += 20;

                //visualizzo la puntata nella label
                this.visualizzaPuntata.setText("Puntata = " + this.puntata);

                //aggiorno label
                //this.aggiornaFish();

                //diminuisco le giocate disponibili
                this.play.venti--;
            } 
            //se viene premuta la fish 50 e ho giocate disponibili
            else if (fishValue == 50 && this.play.cinquanta > 0) {

                //aumento la puntata
                this.puntata += 50;

                //visualizzo la puntata nella label
                this.visualizzaPuntata.setText("Puntata = " + this.puntata);

                //aggiorno label
                //this.aggiornaFish();

                //diminuisco le giocate disponibili
                this.play.cinquanta--;
            } 
            //se viene premuta la fish 100 e ho giocate disponibili
            else if (fishValue == 100 && this.play.cento > 0) {

                //aumento la puntata
                this.puntata += 100;

                //visualizzo la puntata nella label
                this.visualizzaPuntata.setText("Puntata = " + this.puntata);

                //aggiorno label
                //this.aggiornaFish();

                //diminuisco le giocate disponibili
                this.play.cento--;
            } 
            else {
                //inserimento errore
                this.inserisciErrore("Hai finito queste fish", "Errore");
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////MESSAGGI//////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    //metodo utile a mostrare un messaggio di errore
    public void inserisciErrore(String message, String title) {
        //dimostrazione effettiva
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    //metodo utile a mostrare un'informazione
    public void inserisciMex(String message, String title) {
        //dimostrazione effettiva
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////LOGICA////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////// 
    //metodo utile a farti abbandonare la partita   
    private void leftGame() throws IOException, URISyntaxException {

        //se viene premuta l'abbandona partita
        if (this.menuTendina.getSelectedItem().equals("Abbandona partita")) {

            //nascondo finestra
            this.setVisible(false);

            //dico al server che ho abbandonato la partita
            this.communication.output("abbandonaPartita/0");

        }
    }

    //metodo utile a mostrare la mano del giocatore
    public void mostraManoGiocatore() throws IOException {
        
        //percorso file carta
        String percorsoCarta = "";

        //scorro il vettore di cartee
        for (int i = 0; i < this.listaCarteGiocatore.size(); i++) {
            //trovo il percorso file utile
            percorsoCarta = this.listaCarteGiocatore.lista.get(i).getNumero() + this.listaCarteGiocatore.lista.get(i).getSeme() + ".png";

            //salvataggio immagine 
            this.imgcarta = ImageIO.read(new File("immagini/carte/" + percorsoCarta));

            //sistemazione carta
            this.imgcarta = resizeImage(this.imgcarta, 80, 100);

            //aggiunta carte alla finestra
            if (i == 1)
                this.addComponent(167, 0, 0, 80, new JLabel(new ImageIcon(this.imgcarta)));
            else
                this.addComponent(167, 120, 0, 0, new JLabel(new ImageIcon(this.imgcarta)));
        }
    }

    //metodo utile a mostrare la flop sul banco da gioco
    public void mostraFlopSulBanco() throws IOException {

        //percorso scart
        String percorsoCarta = "immagini/carte/";

        //scorro la lista della flop
        for (int i = 0; i < this.flop.size(); i++) {

            //prendo carta temporanea da visualizzare
            carta currentCarta = this.flop.lista.get(i);

            //salvo il percorso file generale
            String percorsoCompleto = percorsoCarta + currentCarta.getNumero() + currentCarta.getSeme() + ".png";

            try {
                //salvo immagini delle carte
                BufferedImage imgCarta = ImageIO.read(new File(percorsoCompleto));

                //le sistemo
                imgCarta = this.resizeImage(imgCarta, 80, 100);

                //inserimento in una label
                JLabel labelCarta = new JLabel(new ImageIcon(imgCarta));

                //aggiunta carte flop sul banco da gioco
                if (i == 1)
                    this.addComponent(0, 0, 83, 80, labelCarta);
                else if (i == 2)
                    this.addComponent(0, 120, 83, 0, labelCarta);
                else if (i == 3)
                    this.addComponent(0, 320, 83, 0, labelCarta);
                else
                    this.addComponent(0, 0, 83, 280, labelCarta);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //metodo utile ad aggiornare le fish
    public void aggiornaFish() {

        //imposta colore scritte bianche
        this.visualizzaPuntata.setForeground(Color.WHITE);

        //set font 
        Font fontPuntata = new Font("Arial Black", Font.PLAIN, 41);
        this.visualizzaPuntata.setFont(fontPuntata);

        //aggiunta scritta 
        this.addComponent(670, 0, 0, 1000, this.visualizzaPuntata);
    }
    //////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////METODI GENERALI UTILI///////////////////////////////
    /////////////////////////////////////////////////////////////////////////////// 
    //metodo utile ad aggiungere elementi alla finestra
    private void addComponent(int daSu, int daSinistra, int daGiu, int daDestra, JComponent component) {
        this.contenitore.gridx = 0;
        this.contenitore.gridy = 1;
        this.contenitore.insets = new Insets(daSu, daSinistra, daGiu, daDestra);
        this.pannelloSfondo.add(component, this.contenitore);
    }

    //metodo per creare la finestra con lo sfondo
    private JPanel creaPannelloConSfondo() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                disegnaSfondo(g);
            }
        };
    }

    //metodo utile a disegnare lo sfondo
    private void disegnaSfondo(Graphics g) {
        if (this.immagineSfondo != null)
            g.drawImage(immagineSfondo, 0, 0, getWidth(), getHeight(), this);
    }

    //metodo utile al menu a tendina
    private void actionRules() throws IOException, URISyntaxException {
        if ("Regolamento".equals(this.menuTendina.getSelectedItem()))
            this.exploreUrl("https://poker.md/it/how-to-play-poker/");
    }

    //metodo utile ad aprire il link del regolamento
    private void exploreUrl(String url) throws IOException, URISyntaxException {
        Desktop.getDesktop().browse(new URI(url));
    }
    //////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
}
