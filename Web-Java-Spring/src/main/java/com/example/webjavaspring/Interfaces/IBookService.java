package com.example.webjavaspring.Interfaces;

import com.example.webjavaspring.DTOs.BookDTO;
import com.example.webjavaspring.DTOs.BookSearchDTO;
import java.util.List;
import java.util.Optional;

public interface IBookService {

    BookDTO save(BookDTO bookDTO);
    List<BookDTO> findAll();
    Optional<BookDTO> findById(Long id);
    void deleteById(Long id);

    List<BookDTO> searchByTitle(String title);
    List<BookDTO> searchByAuthor(String author);
    List<BookDTO> searchByPageCount(Integer pageCount);
    Optional<BookSearchDTO> findLatestBookTitleByAuthor(String author);
    List<BookSearchDTO> findBooksByPublisherInCurrentYear(String publisher);
    List<BookDTO> searchByPublicationYear(Integer publicationYear);
    List<BookDTO> searchByGenreAuthorYear(String genre, String author, Integer year);
    List<BookSearchDTO> findBestSellers();
    List<BookDTO> searchByDescriptionKeyword(String keyword);
}