package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDessert {
    private int orderDessertId;
    private int orderId;
    private int dessertId;
    private int quantity;
}