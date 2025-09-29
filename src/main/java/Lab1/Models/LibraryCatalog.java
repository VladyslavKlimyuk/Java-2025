package Lab1.Models;

import Lab1.Interfaces.ILibraryItem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryCatalog {
    private List<ILibraryItem> items;

    public LibraryCatalog() {
        items = new ArrayList<>();
    }

    // Завантаження даних з файлу
    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                switch (parts[0]) {
                    case "Book": {
                        String title = parts[1];
                        String author = parts[2];
                        String genre = parts[3];
                        int pages = Integer.parseInt(parts[4]);
                        items.add(new Book(title, author, genre, pages));
                        break;
                    }
                    case "Newspaper": {
                        String title = parts[1];
                        String date = parts[2];
                        List<String> headlines = Arrays.asList(parts[3].split(","));
                        items.add(new Newspaper(title, date, headlines));
                        break;
                    }
                    case "Almanac": {
                        String title = parts[1];
                        String[] worksRaw = parts[2].split("\\|");
                        List<Book> works = new ArrayList<>();
                        for (String w : worksRaw) {
                            String[] b = w.split(":");
                            works.add(new Book(b[0], b[1], b[2], Integer.parseInt(b[3])));
                        }
                        items.add(new Almanac(title, works));
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка при зчитуванні файлу: " + e.getMessage());
        }
    }

    // Тестова ініціалізація
    public void initializeTestData() {
        items.add(new Book("Маленький принц", "Антуан де Сент-Екзюпері", "Фентезі", 96));
        items.add(new Newspaper("Українська правда", "2023-10-27",
                List.of("Новини", "Політика", "Спорт")));
        items.add(new Almanac("Сучасна поезія", List.of(
                new Book("Вірші", "Ліна Костенко", "Поезія", 100),
                new Book("Вірші", "Тарас Шевченко", "Поезія", 120)
        )));
    }

    // Додавання об'єкта конкретного типу
    public void addItem(ILibraryItem item) {
        items.add(item);
    }

    // Додавання об'єкта випадкового типу
    public void addRandomItem() {
        Random random = new Random();
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                items.add(new Book("Випадкова книга", "Випадковий автор",
                        "Випадковий жанр", random.nextInt(500) + 50));
                break;
            case 1:
                items.add(new Newspaper("Випадкова газета", "2023-10-27",
                        List.of("Випадковий заголовок 1", "Випадковий заголовок 2")));
                break;
            case 2:
                items.add(new Almanac("Випадковий альманах", List.of(
                        new Book("Випадкова книга 1", "Випадковий автор 1",
                                "Випадковий жанр 1", random.nextInt(500) + 50),
                        new Book("Випадкова книга 2", "Випадковий автор 2",
                                "Випадковий жанр 2", random.nextInt(500) + 50)
                )));
                break;
        }
    }

    // Видалення об'єкта за назвою
    public void removeItem(String title) {
        items.removeIf(item -> item.getTitle().equals(title));
    }

    // Коригування об'єкта
    public void updateItem(String oldTitle, ILibraryItem newItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getTitle().equals(oldTitle)) {
                items.set(i, newItem);
                System.out.println("Об'єкт '" + oldTitle + "' успішно оновлено!");
                return;
            }
        }
        System.out.println("Об'єкт з назвою '" + oldTitle + "' не знайдено.");
    }

    // Виведення всього каталогу на екран
    public void displayCatalog() {
        for (ILibraryItem item : items) {
            System.out.println(item);
        }
    }

    // Виведення каталогу з групуванням за типом
    public void displayCatalogGroupedByType() {
        Map<String, List<ILibraryItem>> grouped = items.stream()
                .collect(Collectors.groupingBy(ILibraryItem::getType));

        grouped.forEach((type, list) -> {
            System.out.println(type);
            list.forEach(System.out::println);
            System.out.println();
        });
    }

    // Пошук за назвою книги або газети
    public void searchByTitle(String title) {
        items.stream()
                .filter(item -> item.getTitle().equals(title))
                .forEach(System.out::println);
    }

    // Пошук за автором
    public void searchByAuthor(String author) {
        items.stream()
                .filter(item -> {
                    if (item instanceof Book) {
                        return item.getAuthor() != null && item.getAuthor().equals(author);
                    } else if (item instanceof Almanac) {
                        return ((Almanac) item).getWorks().stream()
                                .anyMatch(book -> book.getAuthor() != null && book.getAuthor().equals(author));
                    }
                    return false;
                })
                .forEach(System.out::println);
    }

    // Пошук за роком випуску для газети
    public void searchByYear(String year) {
        items.stream()
                .filter(item -> item instanceof Newspaper &&
                        item.getReleaseDate() != null &&
                        item.getReleaseDate().startsWith(year))
                .forEach(System.out::println);
    }
}
