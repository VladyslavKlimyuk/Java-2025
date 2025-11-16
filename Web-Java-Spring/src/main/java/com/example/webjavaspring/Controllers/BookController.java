package com.example.webjavaspring.Controllers;

import com.example.webjavaspring.DTOs.*;
import com.example.webjavaspring.Service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.save(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookDTO.setId(id);
        BookDTO updatedBook = bookService.save(bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        if (bookService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/title")
    public List<BookDTO> searchByTitle(@RequestParam String value) {
        return bookService.searchByTitle(value);
    }

    @GetMapping("/search/author")
    public List<BookDTO> searchByAuthor(@RequestParam String value) {
        return bookService.searchByAuthor(value);
    }

    @GetMapping("/search/pages")
    public List<BookDTO> searchByPageCount(@RequestParam Integer value) {
        return bookService.searchByPageCount(value);
    }

    @GetMapping("/search/latest-by-author")
    public ResponseEntity<BookSearchDTO> findLatestBookTitleByAuthor(@RequestParam String author) {
        return bookService.findLatestBookTitleByAuthor(author)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/publisher-current-year")
    public List<BookSearchDTO> findBooksByPublisherInCurrentYear(@RequestParam String publisher) {
        return bookService.findBooksByPublisherInCurrentYear(publisher);
    }

    @GetMapping("/search/year")
    public List<BookDTO> searchByPublicationYear(@RequestParam Integer value) {
        return bookService.searchByPublicationYear(value);
    }

    @GetMapping("/search/complex")
    public List<BookDTO> searchByGenreAuthorYear(
            @RequestParam String genre,
            @RequestParam String author,
            @RequestParam Integer year) {
        return bookService.searchByGenreAuthorYear(genre, author, year);
    }

    @GetMapping("/search/bestsellers")
    public List<BookSearchDTO> findBestSellers() {
        return bookService.findBestSellers();
    }

    @GetMapping("/search/description")
    public List<BookDTO> searchByDescriptionKeyword(@RequestParam String keyword) {
        return bookService.searchByDescriptionKeyword(keyword);
    }
}