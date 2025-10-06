package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private int clientId;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String address;
    private double discount;

    public Client(String firstName, String lastName, String phoneNumber, String address, double discount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.discount = discount;
    }
}