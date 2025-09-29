package Lab1.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private String model;
    private int capacity;
    private boolean available = true;
    private boolean broken = false;
}
