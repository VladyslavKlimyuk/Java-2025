package com.example.webjavaspring.Repositories;

import com.example.webjavaspring.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByPageCount(Integer pageCount);

    @Query("SELECT b FROM Book b WHERE b.author = :author ORDER BY b.publicationYear DESC LIMIT 1")
    Optional<Book> findLatestBookByAuthor(@Param("author") String author);

    List<Book> findByPublisherIgnoreCaseAndPublicationYear(String publisher, Integer currentYear);

    List<Book> findByPublicationYear(Integer publicationYear);

    List<Book> findByGenreIgnoreCaseAndAuthorIgnoreCaseAndPublicationYear(String genre, String author,
                                                                          Integer publicationYear);

    @Query("SELECT b FROM Book b WHERE b.circulation > 100000")
    List<Book> findBestSellers();

    List<Book> findByDescriptionContainingIgnoreCase(String keyword);
}