// Drug.java
public class Drug {
    private String drugID;
    private String drugName;
    private String sideEffects;
    private String benefits;

    public Drug(String drugID, String drugName, String sideEffects, String benefits) {
        this.drugID = drugID;
        this.drugName = drugName;
        this.sideEffects = sideEffects;
        this.benefits = benefits;
    }

    public boolean isSafeForPatient(Patient patient) {
        // Simple check, in real app would check interactions
        return sideEffects == null || !sideEffects.toLowerCase().contains("severe");
    }

    // Getters
    public String getDrugID() { return clean(drugID); }
    public String getDrugName() { return clean(drugName); }
    public String getSideEffects() { return clean(sideEffects); }
    public String getBenefits() { return clean(benefits); }

    private String clean(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return "Unknown";
        }
        return value.trim();
    }
}
