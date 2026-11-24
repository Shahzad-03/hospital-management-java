// Prescription.java
import java.util.Date;

public class Prescription {
    private String prescriptionID;
    private Date datePrescribed;
    private int dosage;
    private int duration;
    private String comment;
    private Drug drug;
    private Doctor doctor;
    private Patient patient;

    public Prescription(String prescriptionID, Date datePrescribed, int dosage, int duration, String comment, Drug drug, Doctor doctor, Patient patient) {
        this.prescriptionID = prescriptionID;
        this.datePrescribed = datePrescribed;
        this.dosage = dosage;
        this.duration = duration;
        this.comment = comment;
        this.drug = drug;
        this.doctor = doctor;
        this.patient = patient;
    }

    // Overloaded constructor for simplicity
    public Prescription(String prescriptionID, Drug drug, Doctor doctor, Patient patient) {
        this(prescriptionID, new Date(), 1, 7, "Standard prescription", drug, doctor, patient);
    }

    // Getters
    public String getPrescriptionID() { return prescriptionID; }
    public Date getDatePrescribed() { return datePrescribed; }
    public int getDosage() { return dosage; }
    public int getDuration() { return duration; }
    public String getComment() { return comment; }
    public Drug getDrug() { return drug; }
    public Doctor getDoctor() { return doctor; }
    public Patient getPatient() { return patient; }

    public void displayDetails() {
        System.out.println(prescriptionID + " - " + patient.getFullName() + " - Dr " + doctor.getFullName() + " - " + drug.getDrugName() + " - dosage " + dosage + " - " + duration + " days");
    }
}
