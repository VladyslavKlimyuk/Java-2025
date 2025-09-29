package Lab1.Models;

import Lab1.Interfaces.ILibraryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Almanac implements ILibraryItem {
    private String title;
    private List<Book> works;

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
    public String getReleaseDate() {
        return null;
    }

    @Override
    public List<String> getHeadlines() {
        return null;
    }

    @Override
    public String getType() {
        return "Альманах";
    }
}
