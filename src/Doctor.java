// Doctor.java - Extends Person
public class Doctor extends Person {
    private String doctorID;
    private String specialization;
    private String hospital;

    public Doctor(String doctorID, String firstName, String surname, String address, String phone, String email, String specialization, String hospital) {
        super(firstName, surname, address, phone, email);
        this.doctorID = doctorID;
        this.specialization = specialization;
        this.hospital = hospital;
    }

    public void prescribeDrug(Patient patient, Drug drug, int dosage, int duration) {
        // Simple method to create prescription
        Prescription prescription = new Prescription("P" + System.currentTimeMillis(), new java.util.Date(), dosage, duration, "Prescribed by " + getFullName(), drug, this, patient);
        // Save to DB would be here, but for now just print
        System.out.println("Prescription created for " + patient.getFullName());
    }

    @Override
    public void displayDetails() {
        String phone = getPhone().equals("Unknown") ? "No phone recorded" : getPhone();
        System.out.println(doctorID + " - Dr " + getFullName() + " - " + phone + " - " + displayValue(specialization) + " - " + displayValue(hospital));
    }

    // Getters
    public String getDoctorID() { return doctorID; }
    public String getSpecialization() { return displayValue(specialization); }
    public String getHospital() { return displayValue(hospital); }
}
