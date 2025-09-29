package Lab1.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Trip {
    private Request request;
    private Driver driver;
    private Car car;
    private boolean completed = false;

    public void startTrip() {
        if (Math.random() < 0.2) { // 20% шанс поломки
            car.setBroken(true);
            car.setAvailable(false);
            System.out.println("Автомобіль " + car.getModel() + " зламався під час рейсу!");
        } else {
            completeTrip();
        }
    }

    public void completeTrip() {
        this.completed = true;
        driver.setAvailable(true);
        car.setAvailable(true);
        System.out.println("Рейс виконано. Водій " + driver.getName() +
                " отримав виплату за доставку у " + request.getDestination());
    }
}
