import java.util.Arrays;

/** Aufteilung:
 * Borbala: Hat eine grobe Textdatei mit einem Plan erstellt. Sie hat die Klassenstruktur festgelegt und die Klasse „Wetter“
 * vollständig implementiert. Außerdem hat sie an „Test“ gearbeitet.
 * Rada: Hat die Klasse „KlimaBudget“ vollständig implementiert, die Klassen überprüft, Korrekturen vorgenommen, Kommentare
 * hinzugefügt und die Beschreibung in „Test“ verfasst.
 * Alex: Sie hat die Klassen „KlimaBudget“ und „Wetter“ überprüft und bei Bedarf Korrekturen vorgenommen.
 * Die Klasse „Simulation“ hat sie vollständig implementiert.
 * Gemeinsam: Wir haben besprochen, wie wir die Aufgabe angehen und die einzelnen Aufgaben verteilen.
 * Wir haben uns während des Prozesses ausgetauscht, um eine optimale Abstimmung zu gewährleisten.
 *
 * KI nutzung:
 * Borbala: Sie nutzte Google Gemini, um die Aufgabe besser zu verstehen und sie ins Ungarische zu übersetzen.
 * Sie verwendete es für kleine Codeblöcke (bis zu 10 Zeilen).
 * Rada: Sie nutzte Gemini als Hilfe für kleinere Codeblöcke in KlimaBudget() und Google Translate für die Übersetzung
 * und Korrektur beim Hinzufügen von Kommentaren.
 * Alex: Sie nutzte Gemini als Hilfe für kleinere Codeblöcke in der Funktion Simulation()
 * und Google Translate für die Übersetzung und Korrektur beim Hinzufügen von Kommentaren.
 *
 */

/**
 * Diese Methode beginnt der Simulationen (Randfälle und 88 Evaluierungsläufe).
 */
public class Test {
    public static void main(String[] args) {

        //Erste 3 Randfälle = Ausgeben auf Konsole

        Simulation sim = new Simulation();
        sim.Simulate(1.0, 0.0);


        sim.printSimulationOut(1.0, 0.0, sim.getEndA(), sim.getDurchschnittSM(),  sim.getUeberschreitungen());
        sim.Simulate(1.7, 1.0);
        sim.printSimulationOut(1.7, 1.0, sim.getEndA(), sim.getDurchschnittSM(),  sim.getUeberschreitungen());
        sim.Simulate(1.7, 0.0);
        sim.printSimulationOut(1.7, 0.0, sim.getEndA(), sim.getDurchschnittSM(),  sim.getUeberschreitungen());


        //88 Simulationsläufe = Nicht ausgeben, sondern bewerten

        double[][] evaluationArray = new double[11][4];

        int index = 0;

        for (int i = 0; i <= 10; i ++){ //Wir gehen dur die M Werte von 0.0 bis 1.0

            double M = i / 10.0;
            double sum = 0.0;
            double minEndA = 5.0;
            double maxEndA = 0.0;

            for (int j = 0; j < 8; j ++){ //Wir gehen durch die A Werte von 1.0 bis 1.7

                double startA = (j * 0.1) + 1.0; //rechnet aus die A wert basierend auf j

                sim.Simulate(startA, M);

                if (sim.getEndA()  < minEndA){
                    minEndA = sim.getEndA();
                }
                if (sim.getEndA() > maxEndA){
                    maxEndA = sim.getEndA();
                }

                double value = sim.evaluateSimulation(M, startA); //wir abrufen evaluateSimulation für jedes Paare (Mi, Ai)
                sum += value;
            }

            sum /= 8; //Wir ausrechnen ein Durchschnitt vom Werte, um zu bestimmten welches M parameter hat die besten Resultate im Allgemeinen.


            evaluationArray[index][0] = M;
            evaluationArray[index][1] = sum;
            evaluationArray[index][2] = minEndA;
            evaluationArray[index][3] = maxEndA;
            index++;
        }


        /*

        System.out.println("=== Ergebnisse von 88 Simulation ==="); //Diese Teil war KI generiert um zu überprüfen die Tabelle.
        for (int i = 0; i < evaluationArray.length; i++) {
            double m = evaluationArray[i][0];
            double score = evaluationArray[i][1];
            double minA = evaluationArray[i][2];
            double maxA = evaluationArray[i][3];

            System.out.printf("M: %.1f | Wert: %.2f | Min A: %.2f | Max A: %.2f%n", m, score, minA, maxA);
        }

         */

        //wir gehen durch die Tabelle, und wählen aus die M Parameter das hat die beste durchschnittliche Bewertung.
        int bestMindex = 0;
        double bestE = evaluationArray[0][1];
        double bestM = evaluationArray[0][0];
        for (int i = 0; i < 8; i += 10){
            if (evaluationArray[i][1] > bestE){
                bestE = evaluationArray[i][1];
                bestM = evaluationArray[i][0];
                bestMindex = i;
            }
        }
        System.out.println();
        System.out.printf("Beste M war: %.1f , A nach 100 Jahren: von %.2f bis %.2f", //KI zur korrekten Formatierung von printf verwendet
                bestM,
                evaluationArray[bestMindex][2],
                evaluationArray[bestMindex][3]);

    }
}
