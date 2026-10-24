
/**
 * Modularisierungseinheit: Klasse (Datenkapselung).
 *
 * Die Klasse {@code Simulation} ist verantwortlich für den Ablauf der Simulation.
 * Wir simulieren immer 100 Jahren, und 1 Jahr dauert 90 Tagen.
 * Nach einer Simulation wir können es auch sehr detailliert auf der Konsole ausgeben lassen, oder die Simulation selbst auswerten.
 */


public class Simulation {

    private Wetter Wetter;
    private KlimaBudget Budget;
    private double[] durchschnittSM = new double[10]; //den 10 über jeweils 10 aufeinander folgende Jahre gemittelten Anteilen der Mittel für Sofortmaßnahmen
    private double[] ueberschreitungen = new double[100]; //wie viel haben wir das Budget jedes Jahr überschreitet
    private double endA; //A nach 100 Jahren

    /**
     * Diese Methode spielt die komplette Simulation für 100 Jahren, 90 Tage pro Jahr,
     * und speichert alle wichtigen Zwischenergebnisse in deinen Arrays ab, damit printSimulationOut sie später ausdrucken kann.
     *
     * @param A Der Anpassungfator am Anfang für die Simulation.
     * @param M Der Anteil des Budgets, der für mittelfristige Maßnahmen fixiert ist.
     */
    public void Simulate(double A, double M) {

        double startA = A;
        this.Budget = new KlimaBudget(M);
        double summe10Jahre = 0.0; //wo wir jedes Jahr die SM Kosten addieren, sodass wir durchschnittSM kalkulieren können


        for (int i = 0; i <100; i++) { //die Schleife, die die Jahre durchläuft

            double startTemp = 25.0 + Math.random() * 25.0; //die Werte für den ersten Sommertag sind zufällig
            double startWater = -8.0 + Math.random() * 16.0;
            this.Wetter = new Wetter(startTemp, startWater, A);

            for (int j = 0; j < 90; j++) { //die Schleife, die die Tage durchläuft.

                //JEDEN TAG
                this.Budget.addDailyNeed(this.Wetter.getModifiedHighTemp(), this.Wetter.getModifiedWater()); //die Tageskosten kalkulieren

                if (j < 89) {
                    this.Wetter.generateNextDay(); //neues Wetter
                }
            }
            summe10Jahre += this.Budget.getAnnualSM();
            ueberschreitungen[i] = this.Budget.calculateYear(); //Überschreitung für dieses Jahr


            A *= Budget.calculateNewA();


            if ((i + 1) % 10 == 0) {
                //NACH 10 JAHREN
                durchschnittSM[i / 10] = summe10Jahre / 10.0;
                summe10Jahre = 0.0; //frisch für die nächste 10 Jahre

            }
        }
        this.endA=A;
    }

    /**
     * Diese Methode gibt den durchschnittSM zurück.
     */
    public double[] getDurchschnittSM() {
        return durchschnittSM;
    }

    /**
     * Diese Methode gibt den ueberschreitungen zurück.
     */
    public double[] getUeberschreitungen() {
        return ueberschreitungen;
    }

    /**
     * Diese Methode gibt den endlichen Anpassungsfaktor zurück.
     */
    public double getEndA() { return endA; }

    /**
     * Diese Methode nimmt die Daten, die Simulate() über 100 Jahre simuliert hat, und gibt sie aus auf der Konsole.
     *
     * @param startA Der Anpassungsfaktor am Anfang der Simulation
     * @param M Der Anteil des Budgets, der für mittelfristige Maßnahmen fixiert ist
     * @param endA Der Anpassungfaktor am Ende der Simulation
     * @param durschnittSM  Array der speichert den 10 über jeweils 10 aufeinander folgende Jahre gemittelten Anteilen der Mittel für Sofortmaßnahmen
     * @param ueberschreitungen Array der speichert, wie viel haben wir das Budget jedes Jahr überschreitet
     */
    public void printSimulationOut(double startA, double M, double endA, double[] durschnittSM, double[] ueberschreitungen) {

        System.out.println("=========== Simulation ===========");
        System.out.printf(" Initial parameter (A = %.2f , M = %.2f) %n", startA, M); //KI zur korrekten Formatierung von printf verwendet
        System.out.printf(" Final parameter (A = %.2f , M = %.2f)%n%n", endA, M); //KI zur korrekten Formatierung von printf verwendet

        System.out.println("    ###10 Jahres Mittelwerte der Mittel für Sofortmaßnahmen: (%)###");

        for (int i = 0; i < durchschnittSM.length; i++) {
            int startyear = (i * 10) +1;
            int endyear = (i * 10) + 10;
            System.out.print("  Für Jahre: " + startyear + "-" + endyear + ": " ); //KI zur korrekten Formatierung von printf verwendet
            System.out.printf("%.2f %%%n", durchschnittSM[i]*10);
        }

        System.out.println();
        System.out.println("    ###Budgetüberschreitungen:###");
        System.out.println();
        int counter = 0;
        for (int i = 0; i < ueberschreitungen.length; i++) {

            if (ueberschreitungen[i] > 0.0001) {
                int year = (i + 1);
                System.out.print("  Jahr "+ year + ": Überschreitung des Bedarfs um ");
                System.out.printf("%.2f %%%n", ueberschreitungen[i]*10); //KI zur korrekten Formatierung von printf verwendet
                counter++;
            }
        }

        if (counter == 0) {
            System.out.println();
            System.out.println("Keine Überschreitungen während der Simulation aufgetreten.");
        }
        System.out.println();
        System.out.println();

    }

    /**
     * Diese Methode bewertet, wie gut das gewählte M in Verbindung mit dem Startwert startA funktioniert hat.
     *
     * Die Methode rechnet aus ein Wert zwischen 0 und 1 für die Simulation.
     * Dieser Wert sollte symbolisieren wie gut ist diese Simulation. 1 ist der beste, und 0 ist der schlechte erreichbare Wert.
     *
     * Der Wert ist in dem folgenden Weg ausgerechnet.
     * - A = Wie viel hat der Anpassungfaktor sich verschlechtert.
     * - B = Wie viel Geld haben wir für Sofortmaßnahmen ausgegeben im Durchschnitt durch die ganze Simulation?
     * - C = Wie ist die gleichmäßige Verteilung der Mittel?
     * - D = Mit wie viel haben wir das Budget überschritten?
     *
     * Wir rechnen aus ein Wert für alle A, B, C, D zwischen 0 und 1 und dann ihre durchschnitt.
     *
     * @param M Der Anteil des Budgets, der für mittelfristige Maßnahmen fixiert ist.
     * @param startA Der Anpassungsfaktor am Anfang der Simulation
     *
     * @return Ein ausgerechnete double Wert, der muss zwischen 0 und 1 sein.
     */
    public double evaluateSimulation(double M, double startA) {

        Simulation sim = new Simulation();
        sim.Simulate(startA, M);

        //ValueA = Wie groß ist der Unterschied zwischen startA und endA (die näher ist diese zwei, ist es besser)
        double valueA = startA / sim.getEndA();

        //ValueB = Wie viel haben wir für Sofortmaßnahmen ausgegeben im Durchschnitt im ganzen Jahr?
        double[] averageSM = sim.getDurchschnittSM();
        double yearlySM = 0.0;

        double max_averageSM = averageSM[0];//Wir brauchen diese für valueC.
        double min_averageSM = averageSM[0];

        for (int k = 0; k < averageSM.length; k++){
            yearlySM += averageSM[k];//rechnet aus die totaleSumme
            if  (averageSM[k] > max_averageSM){
                max_averageSM = averageSM[k];//hier suchen wir nach maximum
            }
            if (averageSM[k] < min_averageSM){
                min_averageSM = averageSM[k];//hier suchen wir nach das minimum
            }
        }

        yearlySM /= 10;
        if (yearlySM > 1.0) {
            yearlySM = 0.8;
        }

        double valueB = 1.0 - yearlySM;

        //Value C = Wie ist die gleichmäßige Verteilung der Mittel?

        double valueC = max_averageSM - min_averageSM;
        if (valueC < 0.0){
            valueC = 0.0;
        }
        if (valueC > 1.0){
            valueC = 0.8;
        }

        //Value D = Mit wie viel haben wir das Budget überschritten?

        double[] averageUS = sim.getUeberschreitungen();
        yearlySM = 0.0;

        for (int k = 0; k < averageUS.length; k++){
            yearlySM += averageUS[k];
        }
        yearlySM /=100;

        double valueD = 1.0 / (1.0 + (yearlySM * 5.0));

        return (valueA+valueB+valueC+valueD)/4;
    }

}
