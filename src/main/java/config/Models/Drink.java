package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drink {
    private int drinkId;
    private String nameUa;
    private String nameEn;
    private double price;
}