import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Beschreiben Sie hier die Klasse Primegame.
 * 
 * @author guet 
 * @version 2023
 */
public class Primegame
{
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    private int[] zahlen;
    private int max;
    private int punkteA;
    private int punkteB;
    int restzuege;

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));;

    /**
     * Konstruktor für Objekte der Klasse Primegame
     */
    public Primegame(int pMax)
    {
        // Instanzvariablen initialisieren
        max = pMax;
        zahlen = new int[max];

        setup();
        // starte gameLoop
        //gameLoop();
    }

    public void gameLoop()
    {
        boolean spielLaeuft = true;

        System.out.println("Bitte Spielername 1 eingeben, oder COM für Computerspieler.");
        String spieler1 = eingabezeileEinlesen();


        System.out.println("Bitte Spielername 2 eingeben, oder COM für Computerspieler.");
        String spieler2 = eingabezeileEinlesen();

        int spiel = 0;
        int wahl = 0;

        while(spielLaeuft)
        {
            // Konsole leeren
            //System.out.print('\u000C');
            // Ausgaben für die neue Runde
            System.out.println("Der aktuelle Spielstand ist:");
            System.out.println(spieler1 +": "+ punkteA);
            System.out.println(spieler2 +": "+ punkteB);
            zeigeAktZahlen();

            // Falls noch Zahlen übrig sind, kann gewählt werden
            if(restzuege>0)
            {
                if(spiel == 0)
                {    
                    System.out.println(spieler1 + " bitte wähle eine Zahl oder 0 zum Beenden.");
                    if(spieler1.equals("COM"))
                    {
                        wahl = computerZug();
                        System.out.println(spieler1 + " wählt " + wahl);
                    
                    }
                    else
                    {
                        wahl = Integer.parseInt(eingabezeileEinlesen());
                    }

                }
                else
                {
                    System.out.println(spieler2 + " bitte wähle eine Zahl oder 0 zum Beenden.");
                    if(spieler2.equals("COM"))
                    {
                    
                        wahl = computerZug();
                        System.out.println(spieler2 + " wählt " + wahl);
                    }
                    else
                    {
                        wahl = Integer.parseInt(eingabezeileEinlesen());
                    }
                }
            }

            // Falls eine Zahl gewählt wurde wird sie ausgewertet.
            if(wahl>0 && restzuege >0)
            {
                try
                {
                    spielerWaehleZahl(spiel, wahl);

                    // Spielerwechsel
                    if(spiel == 0)
                        spiel = 1;
                    else
                        spiel = 0;

                    restzuege=restzuege-1;
                }
                catch(IOException e)
                {
                    //System.out.println("Die Zahl gibt es nicht!");

                }

            }
            else
            {
                // Das Spiel ist beendet, Ausgabe des Endstands.
                spielLaeuft = false;
                System.out.println("Spiel beendet.");
                System.out.println("Der aktuelle Spielstand ist:");
                System.out.println(spieler1 +": "+ punkteA);
                System.out.println(spieler2 +": "+ punkteB);

                // TODO: Weitere Runde spielen? Spiel zurücksetzen...
            }

        }
    }

    // -------------------- private - Methoden für die Auswertung und Anzeige --------- //
    public void setup()
    {
        punkteA = 0;
        punkteB = 0;
        restzuege = max;
        // fülle das Array mit Zahlen
        for(int i=0; i<max; i++)
        {
            zahlen[i]=i+1;
        }
    }

    private int computerZug()
    {
        int wahl=0;

        double score = 0.0;
        double maxScore = 0.0;
        int maxWahl = 0;

        for(int i=0; i<max; i++)
        {
            if(zahlen[i]!=0)
            {
                double teiler=0;
                for(int j=0; j<i-1;j++)
                {
                    if(zahlen[j] != 0 && (i+1) % zahlen[j] == 0)
                    {
                        teiler=teiler + zahlen[j];
                    }
                    
                }

                if(teiler > 0)
                {
                    score = zahlen[i]/teiler;
                }
                else
                {
                    score = i;
                }

                if(score > maxScore)
                {
                    maxScore = score;
                    maxWahl = zahlen[i];
                }
            }
        }
        wahl = maxWahl;

        return wahl;
    }

    /**
     * zeigeAktZahlen zeigt das aktuelle Zahlenfeld an bzw. "-", falls es die Zahl nicht
     * mehr gibt.
     */
    private void zeigeAktZahlen()
    {
        int stellen = (int) Math.floor(Math.log10(max))+1;

        for(int i = 0; i<max; i++)
        {
            if(zahlen[i] < 10)
            {
                for(int j=0; j<stellen-1; j++)
                {
                    System.out.print(" ");
                }
            }
            else if(zahlen[i] < 100)
            {
                for(int j=0; j<stellen-2; j++)
                {
                    System.out.print(" ");
                }
            }
            if(zahlen[i]==0)
                System.out.print("- ");
            else
                System.out.print(zahlen[i] + " ");

            int wurzel = (int) Math.sqrt(max);
            if((max >= 20) && (i != 0) && (i+1)%wurzel==0)
            {
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * spielerWaehleZahl Auswertungsmethode, Punktevergabe
     * @param spieler: Spieler 0 oder 1
     * @param pZahl: die gewählte Zahl
     */
    private void spielerWaehleZahl(int spieler, int pZahl) throws IOException
    {
        // Prüfe, ob Zahl noch vorhanden:
        if(pZahl>max)
        {
            throw new IOException();
        }

        if(spieler==0)
        {
            if(zahlen[pZahl-1] != 0)
            {
                // Zahl ist noch vorhanden
                zahlen[pZahl-1] = 0;
                streicheTeilerBeiSpieler(1,pZahl);

                punkteA += pZahl;
            }
            else
            {
                throw new IOException();
            }
        }
        else
        {
            if(zahlen[pZahl-1] != 0)
            {
                // Zahl ist noch vorhanden
                zahlen[pZahl-1] = 0;
                streicheTeilerBeiSpieler(0,pZahl);

                punkteB += pZahl;
            }
            else
            {
                throw new IOException();
            }

        }

    }

    /**
     * streicheTeilerBeiSpieler streicht alle Teiler der Zahl pZahl und 
     * wertet diese bei Spieler spieler
     * @param spieler: Spieler, bei dem gewertet wird.
     * @param pZahl: Zahl, deren Teiler gestrichen werden sollen.
     */
    private void streicheTeilerBeiSpieler(int spieler, int pZahl)
    {
        for(int i=0; i<pZahl-1;i++)
        {
            if(zahlen[i] != 0 && pZahl % zahlen[i] == 0)
            {

                if(spieler == 1)
                {
                    punkteB += zahlen[i];
                    restzuege--;
                    //System.out.println("Zahl " + zahlen[i] + " gewertet bei Spieler " + spieler);
                }
                else
                {
                    punkteA += zahlen[i];
                    restzuege--;
                    //System.out.println("Zahl " + zahlen[i] + " gewertet bei Spieler " + spieler);

                }
                zahlen[i] = 0;
            }
        }
    }

    /**
     * Lies eine Eingabezeile und liefere sie als String-Ergebnis.
     *
     * @return  Einen String, der die Eingabezeile enthält, oder 
     *           einen leeren String im Falle eines Fehlers.
     */
    private String eingabezeileEinlesen()
    {
        String zeile = "";

        try {
            zeile = reader.readLine();
        }
        catch(java.io.IOException exc) {
            System.out.println ("Beim Einlesen ist ein Fehler aufgetreten: " + exc.getMessage());
        }
        return zeile;
    }
}
