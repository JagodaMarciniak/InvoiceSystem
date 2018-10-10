package pl.coderstrust;

public class ContactDataPayer {

    private String email;
    private String phoneNumber;

    public ContactDataPayer(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "ContactDataPayer{" +
                "email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
