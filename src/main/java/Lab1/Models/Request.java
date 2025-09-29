package Lab1.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {
    private String destination;
    private String cargoType;
    private int cargoWeight;
    private int distance;
    private int requiredExperience;
}
