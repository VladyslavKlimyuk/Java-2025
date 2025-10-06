package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDrink {
    private int orderDrinkId;
    private int orderId;
    private int drinkId;
    private int quantity;
}