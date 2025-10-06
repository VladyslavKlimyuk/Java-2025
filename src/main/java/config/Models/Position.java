package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private int positionId;
    private String titleUa;
    private String titleEn;
}