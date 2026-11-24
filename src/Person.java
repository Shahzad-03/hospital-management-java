// Person.java - Abstract base class for Doctor and Patient
public abstract class Person {
    private String firstName;
    private String surname;
    private String address;
    private String phone;
    private String email;

    public Person(String firstName, String surname, String address, String phone, String email) {
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getFullName() {
        return displayValue(firstName) + " " + displayValue(surname);
    }

    public void updateContact(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public abstract void displayDetails();

    protected String displayValue(String value) {
        if (value == null || value.trim().isEmpty() || value.trim().equalsIgnoreCase("null")) {
            return "Unknown";
        }
        return value.trim();
    }

    // Getters
    public String getFirstName() { return displayValue(firstName); }
    public String getSurname() { return displayValue(surname); }
    public String getAddress() { return displayValue(address); }
    public String getPhone() { return displayValue(phone); }
    public String getEmail() { return displayValue(email); }
}
