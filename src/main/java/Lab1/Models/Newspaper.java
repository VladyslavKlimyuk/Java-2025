package Lab1.Models;

import Lab1.Interfaces.ILibraryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Newspaper implements ILibraryItem {
    private String title;
    private String releaseDate;
    private List<String> headlines;

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getGenre() {
        return null;
    }

    @Override
    public int getPages() {
        return 0;
    }

    @Override
    public List<Book> getWorks() {
        return null;
    }

    @Override
    public String getType() {
        return "Газета";
    }
}
