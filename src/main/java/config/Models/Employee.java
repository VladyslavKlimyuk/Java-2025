package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String address;
    private int positionId;
}