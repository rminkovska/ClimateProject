import java.util.List;
import java.util.ArrayList;

/**
 * Modularisierungseinheit: Klasse (Datenkapselung).
 *
 * Die Klasse {@code KlimaBudget} ist verantwortlich für das Budget der Simulation.
 * Sie berechnet die täglichen Kosten für Sofotmaßnahmen.
 * Sie berechnet am Ende jedes Jahres das verbleibende Budget für Ursachenbekämpfung.
 * Falls die Kosten der Sofortmaßnahmen höher als das verfügbare Budget ist, dann speicher wir die Höhe der Überschreitung.
 * Nach 10 Jahren, aktualisieren wir das Parameter A, basiert auf die Höhe der Ausgaben für die Ursachenbekämpfung.
 */

public class KlimaBudget {

    private double M_parameter;
    private double AnnualSM; // Wie viel haben wir für Sofortmaßnahmen ausgeben in TOTAL
    private List<Double> ursachenBekaempfung; //Wie viel Geld haben wir in jedem Jahr gespart für Ursachenbekämpfung.

    public KlimaBudget(double M) {
        this.M_parameter = M;
        this.AnnualSM = 0.0;
        this.ursachenBekaempfung = new ArrayList<>();
    }

    /**
     * Bei dieser Methode werden die Tageskosten anhand fester Parameter berechnet
     * und zu den gesamten Jahreskosten für Sofortmaßnahmen addiert.
     */
    public void addDailyNeed(double modifiedTemp, double modifiedWater) {//Vie viel haben wir heute auf Sofortmaßnahmen ausgegeben?

        //In normalfall unsere Temperatur ist zwischen 25 und 50 Grad,
        //wenn wir keinen Schutz haben (M = 0.0), brauchen wir schon bei 40 Grad Sofortmaßnahmen
        //wenn wir guten Schutz haben (M = 1.0), beginnen wir am 50 Grad Sofortmaßnahmen zu brauchen
        double maxTemp = 40.0 + (this.M_parameter * 10.0);


        //In normalfall liegt unser Wassertstand zwischen -8.0 und 8.0
        //kein Schutz -> unter -5.0 Wasserstand ist kritisch
        //gut Schutz -> unter -10.0 Wasserstand ist kritisch
        double minWasser = -5.0 - (this.M_parameter * 5.0);

        double tagesKosten = 0.0;
        double kostenFaktor = 0.0005; //ein fixiertes kleines Faktor

        if (modifiedTemp > maxTemp) {
            tagesKosten += (modifiedTemp - maxTemp) * kostenFaktor;
        }

        if (modifiedWater < minWasser) {
            tagesKosten += (minWasser - modifiedWater) * kostenFaktor;
        }

        this.AnnualSM += tagesKosten;
    }

    /**
     * Diese Methode kalkuliert das restliche Budget fur den Jahr (der muss grosser als 0 sein)
     * und returns die Überschreitung des Budgets ob solche existiert
     */
    public double calculateYear() {
        double restBudget = 1.0 - M_parameter - this.AnnualSM;
        double ueberschreitung = 0.0;

        if (restBudget < 0.0) {
            ueberschreitung = Math.abs(restBudget);
            restBudget = 0.0;
        }

        this.ursachenBekaempfung.add(restBudget);
        this.AnnualSM = 0.0;

        return ueberschreitung;
    }

    /**
     * Bei dieser Methode wird ein neues A berechnet, wobei der Durchschnittswert der letzten 10 Jahre (Ursachenbekämpfung)
     * und die in der Aufgabenstellung angegebenen Randfälle berücksichtigt werden.
     */
    public double calculateNewA() {
        double newA = 0.0;
        if (ursachenBekaempfung.isEmpty()) {
            return 1.0;
        }
        if(ursachenBekaempfung.size() < 10){
            for(int i = 0; i < this.ursachenBekaempfung.size(); i++){
                newA += this.ursachenBekaempfung.get(i);
            }
            newA = newA / this.ursachenBekaempfung.size();
        }
        else{
            for(int i = this.ursachenBekaempfung.size()-10; i < this.ursachenBekaempfung.size(); i++){
                newA += this.ursachenBekaempfung.get(i);
            }
            newA = newA / 10;
        }

        // Eine mathematische Formel, die wir gemäß der Randfälle fixiert haben
        newA = 1.005 - (newA * 0.005);
        return newA;
    }


    /**
     * Diese Methode gebt zurück das AnnualSM
     */
    public double getAnnualSM() {
        return this.AnnualSM;
    }

}
