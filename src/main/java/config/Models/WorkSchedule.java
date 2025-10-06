package config.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkSchedule {
    private int scheduleId;
    private int employeeId;
    private LocalDate workDate;
    private Time startTime;
    private Time endTime;
}