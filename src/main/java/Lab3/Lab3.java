package Lab3;

import config.DAO.*;
import config.Models.*;
import config.DAO.WorkScheduleDAO.ScheduleView;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Lab3 {

    private static final int BARISTA_POSITION_ID = 1;
    private static final int WAITER_POSITION_ID = 2;

    private static final DrinkDAO drinkDAO = new DrinkDAO();
    private static final DessertDAO dessertDAO = new DessertDAO();
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();
    private static final OrderDAO orderDAO = new OrderDAO();
    private static final ClientDAO clientDAO = new ClientDAO();
    private static final WorkScheduleDAO workScheduleDAO = new WorkScheduleDAO();

    private static final LocalDate TARGET_ORDER_DATE = LocalDate.of(2025, 10, 10);
    private static final LocalDate TARGET_SCHEDULE_DATE = LocalDate.now().plusDays(1);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean running = true;

        while (running) {
            printMenu();

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> showAllDrinks();
                    case 2 -> showTop3Drinks();
                    case 3 -> showAllDesserts();
                    case 4 -> showTop5Desserts();
                    case 5 -> showBaristas();
                    case 6 -> showWaiters();
                    case 7 -> calculateAverageOrder(TARGET_ORDER_DATE);
                    case 8 -> showLargestOrders(TARGET_ORDER_DATE);
                    case 9 -> showFrequentClients();
                    case 10 -> showWorkSchedule(TARGET_SCHEDULE_DATE);
                    case 0 -> {
                        running = false;
                        System.out.println("Вихід з програми. До побачення!");
                    }
                    default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
                }
            } else {
                System.out.println("Невірний ввід. Будь ласка, введіть число.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("1. Показати усі напої");
        System.out.println("2. Показати 3 найулюбленіші напої за попередній місяць");
        System.out.println("3. Показати усі десерти");
        System.out.println("4. Показати 5 найулюбленіших десертів за попередні 10 днів");
        System.out.println("5. Показати інформацію про всіх барист");
        System.out.println("6. Показати інформацію про всіх офіціантів");
        System.out.printf("7. Підрахувати середню суму замовлення на %s\n", TARGET_ORDER_DATE);
        System.out.printf("8. Показати найбільше(-і) замовлення на %s\n", TARGET_ORDER_DATE);
        System.out.println("9. Показати інформацію про постійних клієнтів (3+ замовлення за 7 днів)");
        System.out.printf("10. Показати розклад роботи для усіх працівників на %s\n", TARGET_SCHEDULE_DATE);
        System.out.println("0. Вихід");
        System.out.print("Оберіть пункт: ");
    }

    private static void showAllDrinks() {
        System.out.println("УСІ НАПОЇ");
        List<Drink> drinks = drinkDAO.getAll();
        if (drinks.isEmpty()) {
            System.out.println("Напої не знайдені.");
        } else {
            drinks.forEach(System.out::println);
        }
    }

    private static void showTop3Drinks() {
        System.out.println("ТОП-3 НАПОЇ ЗА ОСТАННІЙ МІСЯЦЬ");
        List<Drink> topDrinks = drinkDAO.getTop3FavoriteLastMonth();
        if (topDrinks.isEmpty()) {
            System.out.println("Немає даних про замовлення напоїв за останній місяць.");
        } else {
            topDrinks.forEach(System.out::println);
        }
    }

    private static void showAllDesserts() {
        System.out.println("УСІ ДЕСЕРТИ");
        List<Dessert> desserts = dessertDAO.getAll();
        if (desserts.isEmpty()) {
            System.out.println("Десерти не знайдені.");
        } else {
            desserts.forEach(System.out::println);
        }
    }

    private static void showTop5Desserts() {
        System.out.println("ТОП-5 ДЕСЕРТІВ ЗА 10 ДНІВ");
        List<Dessert> topDesserts = dessertDAO.getTop5FavoriteLast10Days();
        if (topDesserts.isEmpty()) {
            System.out.println("Немає даних про замовлення десертів за останні 10 днів.");
        } else {
            topDesserts.forEach(System.out::println);
        }
    }

    private static void showBaristas() {
        System.out.println("УСІ БАРИСТИ");
        employeeDAO.getEmployeesByPosition(BARISTA_POSITION_ID).forEach(System.out::println);
    }

    private static void showWaiters() {
        System.out.println("УСІ ОФІЦІАНТИ");
        employeeDAO.getEmployeesByPosition(WAITER_POSITION_ID).forEach(System.out::println);
    }

    private static void calculateAverageOrder(LocalDate date) {
        System.out.printf("СЕРЕДНЯ СУМА ЗАМОВЛЕННЯ ЗА %s \n", date);
        double avg = orderDAO.getAverageOrderAmountByDate(date);
        System.out.printf("Середня сума замовлення: %.2f грн\n", avg);
    }

    private static void showLargestOrders(LocalDate date) {
        System.out.printf("НАЙБІЛЬШЕ(-І) ЗАМОВЛЕННЯ ЗА %s \n", date);
        List<Order> largestOrders = orderDAO.getLargestOrdersByDate(date);
        if (largestOrders.isEmpty()) {
            System.out.println("Замовлень на цю дату не знайдено.");
        } else {
            largestOrders.forEach(order ->
                    System.out.println("ID: " + order.getOrderId() + ", Сума: " + order.getTotalAmount() +
                            ", Час: " + order.getOrderTimestamp())
            );
        }
    }

    private static void showFrequentClients() {
        System.out.println("ПОСТІЙНІ КЛІЄНТИ (3+ замовлення за 7 днів)");
        List<Client> frequentClients = clientDAO.getFrequentClients();
        if (frequentClients.isEmpty()) {
            System.out.println("Постійних клієнтів (за критерієм) не знайдено.");
        } else {
            frequentClients.forEach(System.out::println);
        }
    }

    private static void showWorkSchedule(LocalDate date) {
        System.out.printf("РОЗКЛАД РОБОТИ НА %s \n", date);
        System.out.printf("%-20s | %-15s | %s | %s\n", "ПІБ", "Посада", "Дата", "Час роботи");

        List<ScheduleView> schedule = workScheduleDAO.getScheduleByDate(date);
        if (schedule.isEmpty()) {
            System.out.println("Розклад на цю дату відсутній.");
        } else {
            schedule.forEach(System.out::println);
        }
    }
}