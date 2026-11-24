// Patient.java - Extends Person
public class Patient extends Person {
    private String patientID;
    private String postcode;
    private Insurance insurance;
    private Doctor primaryDoctor;

    public Patient(String patientID, String firstName, String surname, String postcode, String address, String phone, String email, Insurance insurance) {
        this(patientID, firstName, surname, postcode, address, phone, email, insurance, null);
    }

    public Patient(String patientID, String firstName, String surname, String postcode, String address, String phone, String email, Insurance insurance, Doctor primaryDoctor) {
        super(firstName, surname, address, phone, email);
        this.patientID = patientID;
        this.postcode = postcode;
        this.insurance = insurance;
        this.primaryDoctor = primaryDoctor;
    }

    public void visitDoctor(Doctor doctor, String symptoms) {
        // Create visit
        Visit visit = new Visit(new java.util.Date(), symptoms, null, doctor, this);
        System.out.println("Visit recorded for " + getFullName() + " with " + doctor.getFullName());
    }

    @Override
    public void displayDetails() {
        String doctorName = primaryDoctor == null ? "No primary doctor" : "Dr " + primaryDoctor.getFullName();
        String insuranceName = insurance == null ? "No insurance" : insurance.getCompany();
        System.out.println(patientID + " - " + getFullName() + " - " + getPhone() + " - " + getEmail() + " - " + doctorName + " - " + insuranceName);
    }

    // Getters
    public String getPatientID() { return patientID; }
    public String getPostcode() { return postcode; }
    public Insurance getInsurance() { return insurance; }
    public Doctor getPrimaryDoctor() { return primaryDoctor; }
}
