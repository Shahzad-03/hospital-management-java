// Insurance.java
public class Insurance {
    private String insuranceID;
    private String company;
    private String address;
    private String phone;

    public Insurance(String insuranceID, String company, String address, String phone) {
        this.insuranceID = insuranceID;
        this.company = company;
        this.address = address;
        this.phone = phone;
    }

    // Getters
    public String getInsuranceID() { return clean(insuranceID); }
    public String getCompany() { return clean(company); }
    public String getAddress() { return clean(address); }
    public String getPhone() { return clean(phone); }

    private String clean(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return "Unknown";
        }
        return value.trim();
    }
}
