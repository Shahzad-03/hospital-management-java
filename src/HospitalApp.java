// HospitalApp.java - Main application
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class HospitalApp {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static ArrayList<Patient> patients = new ArrayList<>();
    private static ArrayList<Doctor> doctors = new ArrayList<>();
    private static ArrayList<Drug> drugs = new ArrayList<>();
    private static ArrayList<Prescription> prescriptions = new ArrayList<>();
    private static ArrayList<Visit> visits = new ArrayList<>();
    private static ArrayList<Claim> claims = new ArrayList<>();

    private static HashMap<String, Patient> patientMap = new HashMap<>();
    private static HashMap<String, Doctor> doctorMap = new HashMap<>();
    private static HashMap<String, Drug> drugMap = new HashMap<>();
    private static HashMap<String, Prescription> prescriptionMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        loadDataFromDatabase();
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Hospital Management System");
            System.out.println("1. Add Patient");
            System.out.println("2. Add Doctor");
            System.out.println("3. Add Drug");
            System.out.println("4. Create Prescription");
            System.out.println("5. Record Visit");
            System.out.println("6. Create Claim");
            System.out.println("7. View Patients");
            System.out.println("8. View Doctors");
            System.out.println("9. View Drugs");
            System.out.println("10. View Prescriptions");
            System.out.println("11. View Visits");
            System.out.println("12. View Claims");
            System.out.println("13. Delete Patient");
            System.out.println("14. Delete Prescription");
            System.out.println("15. Delete Claim");
            System.out.println("16. Exit");
            System.out.print("Choose option: ");
            int choice = readInt(scanner);

            switch (choice) {
                case 1:
                    addPatient(scanner);
                    break;
                case 2:
                    addDoctor(scanner);
                    break;
                case 3:
                    addDrug(scanner);
                    break;
                case 4:
                    createPrescription(scanner);
                    break;
                case 5:
                    recordVisit(scanner);
                    break;
                case 6:
                    createClaim(scanner);
                    break;
                case 7:
                    viewPatients();
                    break;
                case 8:
                    viewDoctors();
                    break;
                case 9:
                    viewDrugs();
                    break;
                case 10:
                    viewPrescriptions();
                    break;
                case 11:
                    viewVisits();
                    break;
                case 12:
                    viewClaims();
                    break;
                case 13:
                    deletePatient(scanner);
                    break;
                case 14:
                    deletePrescription(scanner);
                    break;
                case 15:
                    deleteClaim(scanner);
                    break;
                case 16:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    private static void addPatient(Scanner scanner) {
        System.out.print("Patient ID: ");
        String id = scanner.nextLine();
        System.out.print("First Name: ");
        String fname = scanner.nextLine();
        System.out.print("Surname: ");
        String sname = scanner.nextLine();
        System.out.print("Postcode: ");
        String postcode = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Primary Doctor ID: ");
        String doctorID = scanner.nextLine();
        System.out.print("Insurance ID: ");
        String insID = scanner.nextLine();
        System.out.print("Insurance company: ");
        String company = scanner.nextLine();
        String insuranceAddress = "Unknown";
        String insurancePhone = "Unknown";

        Doctor primaryDoctor = findDoctor(doctorID);
        if (id.isEmpty() || fname.isEmpty() || sname.isEmpty()) {
            System.out.println("Patient ID, first name and surname are required");
            return;
        }
        if (!doctorID.isEmpty() && primaryDoctor == null) {
            System.out.println("Primary doctor ID not found");
            return;
        }

        Insurance ins = new Insurance(insID, company, insuranceAddress, insurancePhone);
        Patient patient = new Patient(id, fname, sname, postcode, address, phone, email, ins, primaryDoctor);
        saveInsuranceToDB(ins);
        if (savePatientToDB(patient)) {
            patients.add(patient);
            patientMap.put(id, patient);
            System.out.println("Patient added");
        }
    }

    private static void addDoctor(Scanner scanner) {
        System.out.print("Doctor ID: ");
        String id = scanner.nextLine();
        System.out.print("First Name: ");
        String fname = scanner.nextLine();
        System.out.print("Surname: ");
        String sname = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Specialization: ");
        String spec = scanner.nextLine();
        System.out.print("Hospital: ");
        String hosp = scanner.nextLine();

        if (id.isEmpty() || fname.isEmpty() || sname.isEmpty()) {
            System.out.println("Doctor ID, first name and surname are required");
            return;
        }

        Doctor doctor = new Doctor(id, fname, sname, address, phone, email, spec, hosp);
        if (saveDoctorToDB(doctor)) {
            doctors.add(doctor);
            doctorMap.put(id, doctor);
            System.out.println("Doctor added");
        }
    }

    private static void addDrug(Scanner scanner) {
        System.out.print("Drug ID: ");
        String id = scanner.nextLine();
        System.out.print("Drug Name: ");
        String name = scanner.nextLine();
        System.out.print("Purpose/Use: ");
        String purpose = scanner.nextLine();
        System.out.print("Side Effects: ");
        String side = scanner.nextLine();

        if (id.isEmpty() || name.isEmpty()) {
            System.out.println("Drug ID and name are required");
            return;
        }

        Drug drug = new Drug(id, name, side, purpose);
        if (saveDrugToDB(drug)) {
            drugs.add(drug);
            drugMap.put(id, drug);
            System.out.println("Drug added");
        }
    }

    private static void createPrescription(Scanner scanner) {
        System.out.print("Prescription ID: ");
        String id = scanner.nextLine();
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine();
        System.out.print("Doctor ID: ");
        String did = scanner.nextLine();
        System.out.print("Drug ID: ");
        String drugid = scanner.nextLine();
        System.out.print("Dosage: ");
        int dosage = readInt(scanner);
        System.out.print("Duration: ");
        int duration = readInt(scanner);
        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        Patient patient = findPatient(pid);
        Doctor doctor = findDoctor(did);
        Drug drug = findDrug(drugid);

        if (id.isEmpty() || patient == null || doctor == null || drug == null) {
            System.out.println("Invalid prescription, patient, doctor, or drug ID");
            return;
        }
        if (dosage <= 0 || duration <= 0) {
            System.out.println("Dosage and duration must be more than zero");
            return;
        }
        if (hasDrugConflict(patient, drug)) {
            System.out.println("Warning: possible conflict with existing patient prescription or severe side effects");
        }

        Prescription prescription = new Prescription(id, new Date(), dosage, duration, comment, drug, doctor, patient);
        if (savePrescriptionToDB(prescription)) {
            prescriptions.add(prescription);
            prescriptionMap.put(id, prescription);
            System.out.println("Prescription created");
        }
    }

    private static void recordVisit(Scanner scanner) {
        System.out.print("Patient ID: ");
        String pid = scanner.nextLine();
        System.out.print("Doctor ID: ");
        String did = scanner.nextLine();
        System.out.print("Symptoms: ");
        String sym = scanner.nextLine();
        System.out.print("Diagnosis ID: ");
        String diagnosisID = scanner.nextLine();

        Patient patient = findPatient(pid);
        Doctor doctor = findDoctor(did);

        if (patient == null || doctor == null) {
            System.out.println("Invalid patient or doctor ID");
            return;
        }

        Visit visit = new Visit(new Date(), sym, diagnosisID, doctor, patient);
        if (saveVisitToDB(visit)) {
            visits.add(visit);
            System.out.println("Visit recorded");
        }
    }

    private static void createClaim(Scanner scanner) {
        System.out.print("Claim ID: ");
        String claimID = scanner.nextLine();
        System.out.print("Patient ID: ");
        String patientID = scanner.nextLine();
        System.out.print("Doctor ID: ");
        String doctorID = scanner.nextLine();
        System.out.print("Prescription ID: ");
        String prescriptionID = scanner.nextLine();
        System.out.print("Claim amount: ");
        double amount = readDouble(scanner);

        Patient patient = findPatient(patientID);
        Doctor doctor = findDoctor(doctorID);
        Prescription prescription = findPrescription(prescriptionID);

        if (claimID.isEmpty() || patient == null || doctor == null || prescription == null) {
            System.out.println("Invalid claim, patient, doctor, or prescription ID");
            return;
        }

        String status = isClaimEligible(patient, prescription) ? "Eligible" : "Not eligible";
        Claim claim = new Claim(claimID, new Date(), amount, status, patient, doctor, prescription);
        if (saveClaimToDB(claim)) {
            claims.add(claim);
            System.out.println("Claim created: " + status);
        }
    }

    private static void deletePatient(Scanner scanner) {
        System.out.print("Patient ID to delete: ");
        String patientID = scanner.nextLine();
        Patient patient = findPatient(patientID);

        if (patient == null) {
            System.out.println("Patient ID not found");
            return;
        }

        // Delete from the database first, then update the lists in the program.
        if (deletePatientFromDB(patient.getPatientID())) {
            removePatientFromList(patient.getPatientID());
            patientMap.remove(patient.getPatientID());
            removeVisitsForPatient(patient.getPatientID());
            removeClaimsForPatient(patient.getPatientID());
            removePrescriptionsForPatient(patient.getPatientID());
            System.out.println("Patient deleted");
        }
    }

    private static void deletePrescription(Scanner scanner) {
        System.out.print("Prescription ID to delete: ");
        String prescriptionID = scanner.nextLine();
        Prescription prescription = findPrescription(prescriptionID);

        if (prescription == null) {
            System.out.println("Prescription ID not found");
            return;
        }

        // A prescription can have a claim, so I remove the linked claim as well.
        if (deletePrescriptionFromDB(prescription.getPrescriptionID())) {
            removeClaimsForPrescription(prescription.getPrescriptionID());
            removePrescriptionFromList(prescription.getPrescriptionID());
            prescriptionMap.remove(prescription.getPrescriptionID());
            System.out.println("Prescription deleted");
        }
    }

    private static void deleteClaim(Scanner scanner) {
        System.out.print("Claim ID to delete: ");
        String claimID = scanner.nextLine();
        Claim claim = findClaim(claimID);

        if (claim == null) {
            System.out.println("Claim ID not found");
            return;
        }

        if (deleteClaimFromDB(claim.getClaimID())) {
            removeClaimFromList(claim.getClaimID());
            System.out.println("Claim deleted");
        }
    }

    private static void removePatientFromList(String patientID) {
        for (int i = patients.size() - 1; i >= 0; i--) {
            if (patients.get(i).getPatientID().equals(patientID)) {
                patients.remove(i);
            }
        }
    }

    private static void removeVisitsForPatient(String patientID) {
        for (int i = visits.size() - 1; i >= 0; i--) {
            if (visits.get(i).getPatient().getPatientID().equals(patientID)) {
                visits.remove(i);
            }
        }
    }

    private static void removePrescriptionsForPatient(String patientID) {
        for (int i = prescriptions.size() - 1; i >= 0; i--) {
            Prescription prescription = prescriptions.get(i);
            if (prescription.getPatient().getPatientID().equals(patientID)) {
                prescriptionMap.remove(prescription.getPrescriptionID());
                prescriptions.remove(i);
            }
        }
    }

    private static void removePrescriptionFromList(String prescriptionID) {
        for (int i = prescriptions.size() - 1; i >= 0; i--) {
            if (prescriptions.get(i).getPrescriptionID().equals(prescriptionID)) {
                prescriptions.remove(i);
            }
        }
    }

    private static void removeClaimsForPatient(String patientID) {
        for (int i = claims.size() - 1; i >= 0; i--) {
            if (claims.get(i).getPatient().getPatientID().equals(patientID)) {
                claims.remove(i);
            }
        }
    }

    private static void removeClaimsForPrescription(String prescriptionID) {
        for (int i = claims.size() - 1; i >= 0; i--) {
            if (claims.get(i).getPrescription().getPrescriptionID().equals(prescriptionID)) {
                claims.remove(i);
            }
        }
    }

    private static void removeClaimFromList(String claimID) {
        for (int i = claims.size() - 1; i >= 0; i--) {
            if (claims.get(i).getClaimID().equals(claimID)) {
                claims.remove(i);
            }
        }
    }

    private static boolean hasDrugConflict(Patient patient, Drug newDrug) {
        if (!newDrug.isSafeForPatient(patient)) {
            return true;
        }
        for (Prescription prescription : prescriptions) {
            if (prescription.getPatient().getPatientID().equals(patient.getPatientID())) {
                Drug existingDrug = prescription.getDrug();
                if (existingDrug.getDrugID().equals(newDrug.getDrugID())) {
                    return true;
                }
                if (!existingDrug.isSafeForPatient(patient)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isClaimEligible(Patient patient, Prescription prescription) {
        Insurance insurance = patient.getInsurance();
        return insurance != null
                && insurance.getInsuranceID() != null
                && !insurance.getInsuranceID().trim().isEmpty()
                && !hasOtherDrugConflict(patient, prescription);
    }

    private static boolean hasOtherDrugConflict(Patient patient, Prescription claimPrescription) {
        if (!claimPrescription.getDrug().isSafeForPatient(patient)) {
            return true;
        }
        for (Prescription prescription : prescriptions) {
            if (prescription.getPrescriptionID().equals(claimPrescription.getPrescriptionID())) {
                continue;
            }
            if (prescription.getPatient().getPatientID().equals(patient.getPatientID())) {
                Drug existingDrug = prescription.getDrug();
                if (existingDrug.getDrugID().equals(claimPrescription.getDrug().getDrugID())) {
                    return true;
                }
                if (!existingDrug.isSafeForPatient(patient)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void viewPatients() {
        int[] widths = {10, 18, 13, 22, 18, 18};
        printHeading("Patients");
        printRow(widths, "Patient ID", "Name", "Phone", "Email", "Primary Doctor", "Insurance");
        printSeparator(widths);
        for (Patient p : patients) {
            String doctorName = p.getPrimaryDoctor() == null ? "No primary doctor" : "Dr " + p.getPrimaryDoctor().getFullName();
            String insuranceName = p.getInsurance() == null ? "No insurance" : p.getInsurance().getCompany();
            printRow(widths, p.getPatientID(), p.getFullName(), p.getPhone(), p.getEmail(), doctorName, insuranceName);
        }
        System.out.println("Total patients: " + patients.size());
    }

    private static void viewDoctors() {
        int[] widths = {9, 20, 17, 18, 18};
        printHeading("Doctors");
        printRow(widths, "Doctor ID", "Name", "Phone", "Specialisation", "Hospital");
        printSeparator(widths);
        for (Doctor d : doctors) {
            String phone = d.getPhone().equals("Unknown") ? "No phone recorded" : d.getPhone();
            printRow(widths, d.getDoctorID(), "Dr " + d.getFullName(), phone, d.getSpecialization(), d.getHospital());
        }
        System.out.println("Total doctors: " + doctors.size());
    }

    private static void viewDrugs() {
        int[] widths = {8, 20, 30, 30};
        printHeading("Drugs");
        printRow(widths, "Drug ID", "Name", "Purpose / Benefits", "Side Effects");
        printSeparator(widths);
        for (Drug d : drugs) {
            printRow(widths, d.getDrugID(), d.getDrugName(), d.getBenefits(), d.getSideEffects());
        }
        System.out.println("Total drugs: " + drugs.size());
    }

    private static void viewPrescriptions() {
        int[] widths = {12, 16, 16, 18, 6, 8, 18};
        printHeading("Prescriptions");
        printRow(widths, "Prescription", "Patient", "Doctor", "Drug", "Dosage", "Duration", "Comment");
        printSeparator(widths);
        for (Prescription prescription : prescriptions) {
            printRow(widths,
                    prescription.getPrescriptionID(),
                    prescription.getPatient().getFullName(),
                    "Dr " + prescription.getDoctor().getFullName(),
                    prescription.getDrug().getDrugName(),
                    String.valueOf(prescription.getDosage()),
                    prescription.getDuration() + " days",
                    prescription.getComment());
        }
        System.out.println("Total prescriptions: " + prescriptions.size());
    }

    private static void viewVisits() {
        int[] widths = {10, 16, 16, 10, 16, 24};
        printHeading("Visits");
        printRow(widths, "Patient ID", "Patient", "Doctor", "Date", "Diagnosis", "Symptoms");
        printSeparator(widths);
        for (Visit visit : visits) {
            printRow(widths,
                    visit.getPatient().getPatientID(),
                    visit.getPatient().getFullName(),
                    "Dr " + visit.getDoctor().getFullName(),
                    formatDate(visit.getDateOfVisit()),
                    visit.getDiagnosisID(),
                    visit.getSymptoms());
        }
        System.out.println("Total visits: " + visits.size());
    }

    private static void viewClaims() {
        int[] widths = {10, 18, 18, 12, 10, 10};
        printHeading("Claims");
        printRow(widths, "Claim ID", "Patient", "Doctor", "Prescription", "Status", "Amount");
        printSeparator(widths);
        for (Claim claim : claims) {
            String prescriptionID = claim.getPrescription() == null ? "No prescription" : claim.getPrescription().getPrescriptionID();
            printRow(widths,
                    claim.getClaimID(),
                    claim.getPatient().getFullName(),
                    "Dr " + claim.getDoctor().getFullName(),
                    prescriptionID,
                    claim.getStatus(),
                    "GBP " + claim.getAmount());
        }
        System.out.println("Total claims: " + claims.size());
    }

    // Prints a heading above each table.
    private static void printHeading(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }

    // Prints the line between the table headings and the data.
    private static void printSeparator(int[] widths) {
        StringBuilder line = new StringBuilder();
        for (int width : widths) {
            if (line.length() > 0) {
                line.append("-+-");
            }
            line.append(repeat("-", width));
        }
        System.out.println(line);
    }

    // Prints one table row and keeps each column the same width.
    private static void printRow(int[] widths, String... values) {
        for (int i = 0; i < widths.length; i++) {
            if (i > 0) {
                System.out.print(" | ");
            }
            String value = i < values.length ? displayText(values[i]) : "";
            System.out.printf("%-" + widths[i] + "s", shorten(value, widths[i]));
        }
        System.out.println();
    }

    private static String repeat(String text, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(text);
        }
        return result.toString();
    }

    // Shortens long text so the table does not become too wide.
    private static String shorten(String value, int width) {
        if (value.length() <= width) {
            return value;
        }
        if (width <= 3) {
            return value.substring(0, width);
        }
        return value.substring(0, width - 3) + "...";
    }

    // This stops blank database values from showing as null on screen.
    private static String displayText(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return "Unknown";
        }
        return value.trim();
    }

    // Dates are easier to read in this format.
    private static String formatDate(Date date) {
        if (date == null) {
            return "Unknown";
        }
        return DATE_FORMAT.format(date);
    }

    private static Patient findPatient(String id) {
        return patientMap.get(clean(id));
    }

    private static Doctor findDoctor(String id) {
        return doctorMap.get(clean(id));
    }

    private static Drug findDrug(String id) {
        return drugMap.get(clean(id));
    }

    private static Prescription findPrescription(String id) {
        return prescriptionMap.get(clean(id));
    }

    private static Claim findClaim(String id) {
        String cleanID = clean(id);
        for (Claim claim : claims) {
            if (claim.getClaimID().equals(cleanID)) {
                return claim;
            }
        }
        return null;
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.nextLine();
        }
        int number = scanner.nextInt();
        scanner.nextLine();
        return number;
    }

    private static double readDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print("Please enter an amount: ");
            scanner.nextLine();
        }
        double number = scanner.nextDouble();
        scanner.nextLine();
        return number;
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private static boolean savePatientToDB(Patient patient) {
        String sql = "INSERT INTO patient (patientID, firstname, surname, postcode, address, phone, email, insuranceID, primaryDoctorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getPatientID());
            stmt.setString(2, patient.getFirstName());
            stmt.setString(3, patient.getSurname());
            stmt.setString(4, patient.getPostcode());
            stmt.setString(5, patient.getAddress());
            stmt.setString(6, patient.getPhone());
            stmt.setString(7, patient.getEmail());
            stmt.setString(8, patient.getInsurance().getInsuranceID());
            stmt.setString(9, patient.getPrimaryDoctor() == null ? null : patient.getPrimaryDoctor().getDoctorID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving patient: " + e.getMessage());
            return false;
        }
    }

    private static void saveInsuranceToDB(Insurance insurance) {
        String sql = "INSERT IGNORE INTO insurance (insuranceID, company, address, phone) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insurance.getInsuranceID());
            stmt.setString(2, insurance.getCompany());
            stmt.setString(3, insurance.getAddress());
            stmt.setString(4, insurance.getPhone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving insurance: " + e.getMessage());
        }
    }

    private static boolean saveDoctorToDB(Doctor doctor) {
        String sql = "INSERT INTO doctor (doctorID, firstname, surname, address, phone, email, specialization, hospital) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getDoctorID());
            stmt.setString(2, doctor.getFirstName());
            stmt.setString(3, doctor.getSurname());
            stmt.setString(4, doctor.getAddress());
            stmt.setString(5, doctor.getPhone());
            stmt.setString(6, doctor.getEmail());
            stmt.setString(7, doctor.getSpecialization());
            stmt.setString(8, doctor.getHospital());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving doctor: " + e.getMessage());
            return false;
        }
    }

    private static boolean saveDrugToDB(Drug drug) {
        String sql = "INSERT INTO drug (drugID, drugname, sideeffects, benefits) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, drug.getDrugID());
            stmt.setString(2, drug.getDrugName());
            stmt.setString(3, drug.getSideEffects());
            stmt.setString(4, drug.getBenefits());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving drug: " + e.getMessage());
            return false;
        }
    }

    private static boolean savePrescriptionToDB(Prescription prescription) {
        String sql = "INSERT INTO prescription (prescriptionID, dateprescribed, dosage, duration, comment, drugID, doctorID, patientID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prescription.getPrescriptionID());
            stmt.setDate(2, new java.sql.Date(prescription.getDatePrescribed().getTime()));
            stmt.setInt(3, prescription.getDosage());
            stmt.setInt(4, prescription.getDuration());
            stmt.setString(5, prescription.getComment());
            stmt.setString(6, prescription.getDrug().getDrugID());
            stmt.setString(7, prescription.getDoctor().getDoctorID());
            stmt.setString(8, prescription.getPatient().getPatientID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving prescription: " + e.getMessage());
            return false;
        }
    }

    private static boolean saveVisitToDB(Visit visit) {
        String sql = "INSERT INTO visit (patientID, doctorID, dateofvisit, symptoms, diagnosisID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, visit.getPatient().getPatientID());
            stmt.setString(2, visit.getDoctor().getDoctorID());
            stmt.setDate(3, new java.sql.Date(visit.getDateOfVisit().getTime()));
            stmt.setString(4, visit.getSymptoms());
            stmt.setString(5, visit.getDiagnosisID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving visit: " + e.getMessage());
            return false;
        }
    }

    private static boolean saveClaimToDB(Claim claim) {
        String sql = "INSERT INTO claim (claimID, patientID, doctorID, prescriptionID, claimDate, amount, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, claim.getClaimID());
            stmt.setString(2, claim.getPatient().getPatientID());
            stmt.setString(3, claim.getDoctor().getDoctorID());
            stmt.setString(4, claim.getPrescription().getPrescriptionID());
            stmt.setDate(5, new java.sql.Date(claim.getClaimDate().getTime()));
            stmt.setDouble(6, claim.getAmount());
            stmt.setString(7, claim.getStatus());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving claim: " + e.getMessage());
            return false;
        }
    }

    private static boolean deletePatientFromDB(String patientID) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // I use a transaction so the delete does not get half finished.
            conn.setAutoCommit(false);
            try {
                // Related records must be deleted before the patient record.
                deletePatientClaims(conn, patientID);
                runPreparedUpdate(conn, "DELETE FROM visit WHERE patientID = ?", patientID);
                runPreparedUpdate(conn, "DELETE FROM prescription WHERE patientID = ?", patientID);
                int deletedPatients = runPreparedUpdate(conn, "DELETE FROM patient WHERE patientID = ?", patientID);
                conn.commit();
                return deletedPatients > 0;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error deleting patient: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }

    private static boolean deletePrescriptionFromDB(String prescriptionID) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Delete the claim first because it depends on the prescription.
            conn.setAutoCommit(false);
            try {
                runPreparedUpdate(conn, "DELETE FROM claim WHERE prescriptionID = ?", prescriptionID);
                int deletedPrescriptions = runPreparedUpdate(conn, "DELETE FROM prescription WHERE prescriptionID = ?", prescriptionID);
                conn.commit();
                return deletedPrescriptions > 0;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error deleting prescription: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting prescription: " + e.getMessage());
            return false;
        }
    }

    private static boolean deleteClaimFromDB(String claimID) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Claims do not have anything depending on them, so this is one delete.
            int deletedClaims = runPreparedUpdate(conn, "DELETE FROM claim WHERE claimID = ?", claimID);
            return deletedClaims > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting claim: " + e.getMessage());
            return false;
        }
    }

    private static void deletePatientClaims(Connection conn, String patientID) throws SQLException {
        runPreparedUpdate(conn, "DELETE FROM claim WHERE patientID = ?", patientID);
        // This also catches claims connected through the patient's prescriptions.
        String sql = "DELETE FROM claim WHERE prescriptionID IN (SELECT prescriptionID FROM prescription WHERE patientID = ?)";
        runPreparedUpdate(conn, sql, patientID);
    }

    private static int runPreparedUpdate(Connection conn, String sql, String value) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, value);
            return stmt.executeUpdate();
        }
    }

    private static void loadDataFromDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            setupDatabase(conn);
            loadDoctors(conn);
            loadDrugs(conn);
            loadPatients(conn);
            loadPrescriptions(conn);
            loadVisits(conn);
            loadClaims(conn);
            System.out.println("Data loaded from database");
        } catch (SQLException e) {
            System.out.println("Database not loaded: " + e.getMessage());
            System.out.println("You can still use the program, but new records may not save.");
        }
    }

    private static void setupDatabase(Connection conn) throws SQLException {
        if (!columnExists(conn, "doctor", "phone")) {
            runUpdate(conn, "ALTER TABLE doctor ADD phone varchar(30) DEFAULT NULL");
        }
        if (!columnExists(conn, "patient", "primaryDoctorID")) {
            runUpdate(conn, "ALTER TABLE patient ADD primaryDoctorID varchar(10) DEFAULT NULL");
        }
        runUpdate(conn, "CREATE TABLE IF NOT EXISTS claim (claimID varchar(10) NOT NULL, patientID varchar(10) DEFAULT NULL, doctorID varchar(10) DEFAULT NULL, prescriptionID varchar(10) DEFAULT NULL, claimDate date DEFAULT NULL, amount decimal(10,2) DEFAULT NULL, status varchar(30) DEFAULT NULL, PRIMARY KEY (claimID))");
    }

    private static boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = ? AND column_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tableName);
            stmt.setString(2, columnName);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private static void runUpdate(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    private static void loadDoctors(Connection conn) throws SQLException {
        String sql = "SELECT * FROM doctor";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Doctor doctor = new Doctor(clean(rs.getString("doctorID")), rs.getString("firstname"), rs.getString("surname"), rs.getString("address"), rs.getString("phone"), rs.getString("email"), rs.getString("specialization"), rs.getString("hospital"));
                doctors.add(doctor);
                doctorMap.put(doctor.getDoctorID(), doctor);
            }
        }
    }

    private static void loadDrugs(Connection conn) throws SQLException {
        String sql = "SELECT * FROM drug";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Drug drug = new Drug(clean(rs.getString("drugID")), rs.getString("drugname"), rs.getString("sideeffects"), rs.getString("benefits"));
                drugs.add(drug);
                drugMap.put(drug.getDrugID(), drug);
            }
        }
    }

    private static void loadPatients(Connection conn) throws SQLException {
        String sql = "SELECT p.*, i.company, i.address AS insuranceAddress, i.phone AS insurancePhone FROM patient p LEFT JOIN insurance i ON p.insuranceID = i.insuranceID";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Insurance insurance = new Insurance(clean(rs.getString("insuranceID")), rs.getString("company"), rs.getString("insuranceAddress"), rs.getString("insurancePhone"));
                Doctor primaryDoctor = findDoctor(rs.getString("primaryDoctorID"));
                Patient patient = new Patient(clean(rs.getString("patientID")), rs.getString("firstname"), rs.getString("surname"), rs.getString("postcode"), rs.getString("address"), rs.getString("phone"), rs.getString("email"), insurance, primaryDoctor);
                patients.add(patient);
                patientMap.put(patient.getPatientID(), patient);
            }
        }
    }

    private static void loadPrescriptions(Connection conn) throws SQLException {
        String sql = "SELECT * FROM prescription";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = findPatient(rs.getString("patientID"));
                Doctor doctor = findDoctor(rs.getString("doctorID"));
                Drug drug = findDrug(rs.getString("drugID"));
                if (patient != null && doctor != null && drug != null) {
                    Prescription prescription = new Prescription(clean(rs.getString("prescriptionID")), rs.getDate("dateprescribed"), rs.getInt("dosage"), rs.getInt("duration"), rs.getString("comment"), drug, doctor, patient);
                    prescriptions.add(prescription);
                    prescriptionMap.put(prescription.getPrescriptionID(), prescription);
                }
            }
        }
    }

    private static void loadVisits(Connection conn) throws SQLException {
        String sql = "SELECT * FROM visit";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = findPatient(rs.getString("patientID"));
                Doctor doctor = findDoctor(rs.getString("doctorID"));
                if (patient != null && doctor != null) {
                    visits.add(new Visit(rs.getDate("dateofvisit"), rs.getString("symptoms"), rs.getString("diagnosisID"), doctor, patient));
                }
            }
        }
    }

    private static void loadClaims(Connection conn) throws SQLException {
        String sql = "SELECT * FROM claim";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = findPatient(rs.getString("patientID"));
                Doctor doctor = findDoctor(rs.getString("doctorID"));
                Prescription prescription = findPrescription(rs.getString("prescriptionID"));
                if (patient != null && doctor != null && prescription != null) {
                    claims.add(new Claim(clean(rs.getString("claimID")), rs.getDate("claimDate"), rs.getDouble("amount"), rs.getString("status"), patient, doctor, prescription));
                }
            }
        }
    }
}
