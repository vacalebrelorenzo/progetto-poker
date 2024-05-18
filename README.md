# Progetto Poker

Benvenuto nel progetto Poker! Questo progetto è un'applicazione che simula un gioco di poker utilizzando il protocollo di comunicazione TCP/IP. Il gioco si presenta in modo differente dai diversi poker originali.

## Descrizione

L'applicazione è scritta in java e utilizza la java swing (lato client). Consente agli utenti di controllare semplicemente le proprie carte insieme a quelle del banco e dopo aver controllato la propria combinazione si potrà scegliere se puntare un determinato numero di fish o passare il turno

## Caratteristiche

- **Puntate personalizzate:** Il gioco permette agli utenti di inserire delle puntate personalizzate premendo le differenti fish.

- **Menù personalizzato:** Presenza di un menù che permette di abbandonare la partita e di aprire un sito con scritte tutte le regole del gioco.

- **Interfaccia Utente Intuitiva:** Un'interfaccia utente intuitiva che facilita la partecipazione al gioco.

## Utilizzo
1. Per avviare il server basterà aprire la cartella del server su visual studio code e premere F5.
2. Per avviare il client invece bisognerà aprire unicamente la cartella client da visual studio code e premere F5, il collegamento con il server avverrà solo quando dall'interfaccia utente si preme il pulsante "Unisciti alla partita".

## Funzionamento gioco
Il gioco funziona in modo molto semplice, dopo che tutti i giocatori si sono connessi al gioco, appariranno diversi pop up che indicheranno i turni dei giocatori, potrà giocare solo colui che possiede il testimone del turno. Una volta che tutti i giocatori hanno deciso se scommettere o passare, il server decideròà chi ha vinto e lo condividerà con i vari giocatori: il giocatore che ha vinto riceverà la somma vinta e verrà visualizzata in un pop up, mentre tutti gli altri perdenti non riceveranno niente. Dopo aver controllato il vincitore, il server farà partire un nuovo round che permetterà nuovamente ai giocatori di scommettere o passare.

