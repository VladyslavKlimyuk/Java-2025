package Lab1.Models;

import Lab1.Interfaces.ILibraryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Book implements ILibraryItem {
    private String title;
    private String author;
    private String genre;
    private int pages;

    @Override
    public String getReleaseDate() {
        return null;
    }

    @Override
    public List<String> getHeadlines() {
        return null;
    }

    @Override
    public List<Book> getWorks() {
        return null;
    }

    @Override
    public String getType() {
        return "Книга";
    }
}
