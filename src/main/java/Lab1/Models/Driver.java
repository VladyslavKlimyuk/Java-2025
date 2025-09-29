package Lab1.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
    private String name;
    private int experience;
    private boolean available = true;
}
