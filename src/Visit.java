// Visit.java
import java.util.Date;

public class Visit {
    private Date dateOfVisit;
    private String symptoms;
    private String diagnosisID;
    private Doctor doctor;
    private Patient patient;

    public Visit(Date dateOfVisit, String symptoms, String diagnosisID, Doctor doctor, Patient patient) {
        this.dateOfVisit = dateOfVisit;
        this.symptoms = symptoms;
        this.diagnosisID = diagnosisID;
        this.doctor = doctor;
        this.patient = patient;
    }

    // Getters
    public Date getDateOfVisit() { return dateOfVisit; }
    public String getSymptoms() { return symptoms; }
    public String getDiagnosisID() { return diagnosisID; }
    public Doctor getDoctor() { return doctor; }
    public Patient getPatient() { return patient; }

    public void displayDetails() {
        System.out.println(patient.getPatientID() + " - " + patient.getFullName() + " - Dr " + doctor.getFullName() + " - " + dateOfVisit + " - " + clean(symptoms));
    }

    private String clean(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return "Unknown";
        }
        return value.trim();
    }
}
