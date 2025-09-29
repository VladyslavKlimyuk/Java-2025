package Lab1;

import Lab1.Models.Dispatcher;

public class Lab1_2 {
    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher();

        dispatcher.loadDrivers("src/main/java/Lab1/Files/Driver.txt");
        dispatcher.loadCars("src/main/java/Lab1/Files/Car.txt");

        for (int i = 0; i < 5; i++) {
            dispatcher.createAndAssignRequest();
        }
    }
}
