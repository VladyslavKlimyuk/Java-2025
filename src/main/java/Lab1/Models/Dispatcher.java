package Lab1.Models;

import java.util.*;
import java.io.*;

public class Dispatcher {
    private List<Driver> drivers = new ArrayList<>();
    private List<Car> cars = new ArrayList<>();
    private List<Trip> trips = new ArrayList<>();

    private static final List<String> CITIES = Arrays.asList(
            "Херсон", "Кременчуг", "Київ", "Харків", "Одеса"
    );

    public void loadDrivers(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.lines().forEach(line -> {
                String[] parts = line.split(";");
                drivers.add(new Driver(parts[0], Integer.parseInt(parts[1]), true));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCars(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.lines().forEach(line -> {
                String[] parts = line.split(";");
                cars.add(new Car(parts[0], Integer.parseInt(parts[1]), true, false));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAndAssignRequest() {
        Random random = new Random();

        String destination = CITIES.get(random.nextInt(CITIES.size()));

        Request request = new Request(
                destination,
                "Вантаж -" + (random.nextInt(3) + 1),
                random.nextInt(500) + 100,
                random.nextInt(1000) + 100,
                random.nextInt(10) + 1
        );

        Optional<Driver> driverOpt = drivers.stream()
                .filter(d -> d.isAvailable() && d.getExperience() >= request.getRequiredExperience())
                .findFirst();

        Optional<Car> carOpt = cars.stream()
                .filter(c -> c.isAvailable() && !c.isBroken() && c.getCapacity() >= request.getCargoWeight())
                .findFirst();

        if (driverOpt.isPresent() && carOpt.isPresent()) {
            Driver driver = driverOpt.get();
            Car car = carOpt.get();

            driver.setAvailable(false);
            car.setAvailable(false);

            Trip trip = new Trip(request, driver, car, false);
            trips.add(trip);

            System.out.println("Заявка призначена: " + driver.getName() +
                    " + " + car.getModel() + " -> " + request.getDestination());

            trip.startTrip();
        } else {
            System.out.println("Немає доступних водіїв або автомобілів для заявки: " + request.getDestination());
        }
    }
}
