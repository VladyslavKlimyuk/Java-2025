package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private LocalDateTime orderTimestamp;
    private int employeeId;
    private Integer clientId;
    private double totalAmount;
}