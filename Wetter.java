import java.util.Random;

/**
 * Modularisierungseinheit: Klasse (Datenkapselung).
 *
 * Die Klasse {@code Wetter} ist für Simulieren das Wetter in der Simulation.
 * Jeden Tag haben wir neue Wetter, und während es immer random ist, bleibt es in einem Bereich.
 * Für Temperatur ist es zwischen 25.0 und 50.0 Grad
 * Für Wasservorrat ist es zwischen -8.0 und 8.0.
 */

public class Wetter {

    //Data_Hiding durch private Variables
    private double nrHochstemperatur;
    private double nrWasservorrat;
    private double anpassungsfaktor;
    private Random random = new Random();

    public Wetter(double temp, double water, double anpassungsfaktor){
        this.nrHochstemperatur = temp;
        this.nrWasservorrat = water;
        this.anpassungsfaktor = anpassungsfaktor;
    }

    /**
     *
     * Nominale Abstraktion, generiert die neuen Werte für den nächsten Tag
     *
     * Diese Methode rechnet aus und setzt die Werte (Temperatur und Wasserstand) dieses Objekt auf die Werte der nächste Tag.
     * Nach Ausführung dieser Methode, die Werte der aktuellen gehen verloren, und können nicht mehr erreicht werden.
    */
    public void generateNextDay(){

        double rTemp =  (random.nextDouble() * 10) - 5; //entwickeln random number zwischen -5 und 5 für Temperatur
        double rWass = (random.nextDouble() * 10) - 5; //entwicklen random number zwischen -5 und 5 für Wasservorrat


        this.nrHochstemperatur = this.nrHochstemperatur + rTemp;
        this.nrWasservorrat = this.nrWasservorrat + rWass;

        //machen wir eine obere und untere Schranke für Temperatur, es kann nicht höher sein als 50 Grad, und weniger als 25 Grad
        if (this.nrHochstemperatur > 50.0){
            this.nrHochstemperatur = 50.0;
        } else if (this.nrHochstemperatur < 25.0){
            this.nrHochstemperatur = 25.0;
        }

        //machen wir eine obere und untere Schranke für Wasservorrat, es muss immer zwischen -8.0 und 8.0 sein.
        if (this.nrWasservorrat > 8.0){
            this.nrWasservorrat = 8.0;
        } else if (this.nrWasservorrat < -8.0){
            this.nrWasservorrat = -8.0;
        }
    }

    /**
     * Diese Methode liefert das modifiedHighTemperatur zurück.
     * modifiedHighTemperatur = Anpassungsfaktor * nrHochstemperatur
     *
     * @return Die ausgerechnete modifiedHighTemperatur als double.
     */
    public double getModifiedHighTemp(){
        return this.nrHochstemperatur * this.anpassungsfaktor;
    }

    /**
     * Diese Methode liefert das modifiedWater zurück.
     * modifiedWater = Anpassungsfaktor * nrWasservorrat
     *
     * @return die ausgerechnete modifiedWater als double.
     */
    public double getModifiedWater(){
        return this.nrWasservorrat * this.anpassungsfaktor;
    }


    /**
     * Diese Methode setzt den aktuellen Anpassungsfaktor auf den als Eingabe gegebene newA.
     *
     * @param newA Der neue Wert für den Anpassungsfaktor.
     */
    public void updateModifier(double newA){
        this.anpassungsfaktor = newA;
    }
}
