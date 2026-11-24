import java.util.Date;

public class Claim {
    private String claimID;
    private Date claimDate;
    private double amount;
    private String status;
    private Patient patient;
    private Doctor doctor;
    private Prescription prescription;

    public Claim(String claimID, Date claimDate, double amount, String status, Patient patient, Doctor doctor, Prescription prescription) {
        this.claimID = claimID;
        this.claimDate = claimDate;
        this.amount = amount;
        this.status = status;
        this.patient = patient;
        this.doctor = doctor;
        this.prescription = prescription;
    }

    public void displayDetails() {
        String prescriptionID = prescription == null ? "No prescription" : prescription.getPrescriptionID();
        System.out.println(claimID + " - " + patient.getFullName() + " - Dr " + doctor.getFullName() + " - " + prescriptionID + " - " + clean(status) + " - GBP " + amount);
    }

    public String getClaimID() { return claimID; }
    public Date getClaimDate() { return claimDate; }
    public double getAmount() { return amount; }
    public String getStatus() { return status; }
    public Patient getPatient() { return patient; }
    public Doctor getDoctor() { return doctor; }
    public Prescription getPrescription() { return prescription; }

    private String clean(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return "Unknown";
        }
        return value.trim();
    }
}
