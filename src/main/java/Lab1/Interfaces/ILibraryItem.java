package Lab1.Interfaces;

import Lab1.Models.Book;
import java.util.List;

public interface ILibraryItem {
    String getTitle();
    String getAuthor(); // Для книги та альманаху
    String getGenre(); // Для книги
    int getPages(); // Для книги
    String getReleaseDate(); // Для газети
    List<String> getHeadlines(); // Для газети
    List<Book> getWorks(); // Для альманаху
    String getType();
}