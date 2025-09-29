package Lab1;

import Lab1.Interfaces.ILibraryItem;
import Lab1.Models.*;
import java.util.List;
import java.util.Scanner;

public class Lab1_1 {
    public static void main(String[] args) {
        LibraryCatalog catalog = new LibraryCatalog();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nКАТАЛОГ БІБЛІОТЕКИ");
            System.out.println("1. Вивести каталог із файлу");
            System.out.println("2. Тестова ініціалізація");
            System.out.println("3. Додати об'єкт конкретного типу");
            System.out.println("4. Додати об'єкт випадкового типу");
            System.out.println("5. Видалити об'єкт за назвою");
            System.out.println("6. Коригувати об'єкт");
            System.out.println("7. Вивести каталог (усі об'єкти)");
            System.out.println("8. Вивести каталог (з групуванням за типом)");
            System.out.println("9. Пошук");
            System.out.println("10. Вихід");
            System.out.print("Оберіть дію: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    catalog.loadFromFile("src/main/java/Lab1/Files/Catalog.txt");
                    System.out.println("Каталог зчитано з файлу:");
                    catalog.displayCatalog();
                }
                case 2 -> {
                    catalog.initializeTestData();
                    System.out.println("Тестові дані додані.");
                }
                case 3 -> {
                    System.out.println("Оберіть тип: 1 - Книга, 2 - Газета, 3 - Альманах");
                    int type = scanner.nextInt();
                    scanner.nextLine();

                    if (type == 1) {
                        System.out.print("Введіть назву: ");
                        String title = scanner.nextLine();
                        System.out.print("Введіть автора: ");
                        String author = scanner.nextLine();
                        System.out.print("Введіть жанр: ");
                        String genre = scanner.nextLine();
                        System.out.print("Введіть кількість сторінок: ");
                        int pages = scanner.nextInt();
                        catalog.addItem(new Book(title, author, genre, pages));
                    } else if (type == 2) {
                        System.out.print("Введіть назву газети: ");
                        String title = scanner.nextLine();
                        System.out.print("Введіть дату виходу: ");
                        String date = scanner.nextLine();
                        System.out.print("Введіть заголовки через кому: ");
                        List<String> headlines = List.of(scanner.nextLine().split(","));
                        catalog.addItem(new Newspaper(title, date, headlines));
                    } else if (type == 3) {
                        System.out.print("Введіть назву альманаху: ");
                        String title = scanner.nextLine();
                        catalog.addItem(new Almanac(title, List.of(
                                new Book("Випадкова книга 1", "Автор 1", "Жанр 1", 120),
                                new Book("Випадкова книга 2", "Автор 2", "Жанр 2", 150)
                        )));
                    }
                    System.out.println("Об'єкт додано.");
                }
                case 4 -> {
                    catalog.addRandomItem();
                    System.out.println("Додано випадковий об'єкт.");
                }
                case 5 -> {
                    System.out.print("Введіть назву для видалення: ");
                    String title = scanner.nextLine();
                    catalog.removeItem(title);
                }
                case 6 -> {
                    System.out.print("Введіть назву об'єкта для редагування: ");
                    String oldTitle = scanner.nextLine();
                    System.out.print("Введіть нову назву: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("Введіть автора (або '-' якщо не потрібно): ");
                    String author = scanner.nextLine();
                    System.out.print("Введіть жанр (або '-' якщо не потрібно): ");
                    String genre = scanner.nextLine();
                    System.out.print("Введіть кількість сторінок (0 якщо не потрібно): ");
                    int pages = scanner.nextInt();
                    scanner.nextLine();

                    ILibraryItem newItem = new Book(
                            newTitle,
                            author.equals("-") ? null : author,
                            genre.equals("-") ? null : genre,
                            pages
                    );
                    catalog.updateItem(oldTitle, newItem);
                }
                case 7 -> catalog.displayCatalog();
                case 8 -> catalog.displayCatalogGroupedByType();
                case 9 -> {
                    System.out.println("Оберіть пошук: 1 - за назвою, 2 - за автором," +
                            "3 - за видавництвом (газети), 4 - за роком випуску");
                    int searchType = scanner.nextInt();
                    scanner.nextLine();

                    switch (searchType) {
                        case 1 -> {
                            System.out.print("Введіть назву: ");
                            String title = scanner.nextLine();
                            catalog.searchByTitle(title);
                        }
                        case 2 -> {
                            System.out.print("Введіть автора: ");
                            String author = scanner.nextLine();
                            catalog.searchByAuthor(author);
                        }
                        case 3 -> {
                            System.out.print("Введіть видавництво (назву газети): ");
                            String publisher = scanner.nextLine();
                            catalog.searchByTitle(publisher);
                        }
                        case 4 -> {
                            System.out.print("Введіть рік випуску: ");
                            String year = scanner.nextLine();
                            catalog.searchByYear(year);
                        }
                    }
                }
                case 10 -> {
                    running = false;
                    System.out.println("Програма завершена.");
                }
            }
        }
    }
}
