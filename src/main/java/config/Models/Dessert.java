package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dessert {
    private int dessertId;
    private String nameUa;
    private String nameEn;
    private double price;
}