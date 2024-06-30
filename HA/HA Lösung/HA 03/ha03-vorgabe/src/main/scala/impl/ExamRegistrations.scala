package impl

import scala.io.Source

object ExamRegistrations {

    /**
     * Aufgabe 3.2.1: Lesen Sie die Anmeldungen aus der gegebenen Datei (registrations.csv) ein.
     *
     * @param fileName Name of the resource file containing the list of registrations.
     * @return List of tuples where each entry represents one line of the given CSV file.
     */
    def readRegistrationsFromFile(fileName: String): List[(String, Int, Int)] = {
        val lines = Source.fromResource(fileName).getLines()
        //TODO: implement
        List()
    }

    /**
     * Aufgabe 3.2.2: Lesen Sie die Ergebnisse aus der gegebenen Datei (results.csv) ein.
     *
     * @param fileName Name of the resource file containing the list of results.
     * @return List of tuples where each entry represents one line of the given CSV file.
     */
    def readResultsFromFile(fileName: String) : List[(Int, Double)] = {
        val lines = Source.fromResource(fileName).getLines()
        //TODO: implement
        List()
    }

    /**
     * Aufgabe 3.2.3: Ermitteln Sie den Namen der Student*in mit der besten Note.
     *
     * @param registrations List of tuples representing registrations
     * @param results List of tuples representing results
     * @return Name of the best student (i.e. the student with the best grade)
     */
    def findBestStudent(registrations: List[(String, Int, Int)], results: List[(Int, Double)]) : String = {
        //TODO: implement
        ""
    }

    /**
     * Aufgabe 3.2.4: Ermitteln Sie alle Student*innen, die im 2. Versuch eine 3.0 oder besser geschrieben haben.
     *
     * @param registrations List of tuples representing registrations
     * @param results List of tuples representing results
     * @return List of matriculation numbers of relevant students
     */
    def findAdequateStudents(registrations: List[(String, Int, Int)], results: List[(Int, Double)]) : List[Int] = {
        //TODO: implement
        List()
    }

    /**
     * Aufgabe 3.2.5: Ermitteln Sie die Matrikelnummern aller Studenten, die sich mehrfach registriert haben,
     *              d.h. die mehrmals in der Liste der Registrierungen auftauchen.
     *
     * @param registrations List of tuples representing registrations
     * @return List of matriculation numbers of relevant students
     */
    def findDuplicateRegistrations(registrations: List[(String, Int, Int)]) : Array[Int] = {
        //TODO: implement
        Array[Int]()
    }

    /**
     * Aufgabe 3.2.6: Berechnen Sie die Durchschnittsnote der Student*innen, die die Prüfung zum zweiten Mal schreiben.
     *
     * @param registrations List of tuples representing registrations
     * @param results List of tuples representing results
     * @return Average grade of relevant students
     */
    def findAvgGradeOfRepeatedExams(registrations: List[(String, Int, Int)], results: List[(Int, Double)]) : Double = {
        //TODO: implement
        -1
    }
}
