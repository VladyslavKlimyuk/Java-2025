package com.example.webjavaspring.Service;

import com.example.webjavaspring.DTOs.*;
import com.example.webjavaspring.Entities.Book;
import com.example.webjavaspring.Interfaces.IBookService;
import com.example.webjavaspring.Repositories.IBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final IBookRepository IBookRepository;

    @Override
    public BookDTO save(BookDTO bookDTO) {
        Book book = toEntity(bookDTO);
        Book savedBook = IBookRepository.save(book);
        return toDTO(savedBook);
    }

    @Override
    public List<BookDTO> findAll() {
        return IBookRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDTO> findById(Long id) {
        return IBookRepository.findById(id).map(this::toDTO);
    }

    @Override
    public void deleteById(Long id) {
        IBookRepository.deleteById(id);
    }

    @Override
    public List<BookDTO> searchByTitle(String title) {
        return IBookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> searchByAuthor(String author) {
        return IBookRepository.findByAuthorContainingIgnoreCase(author).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> searchByPageCount(Integer pageCount) {
        return IBookRepository.findByPageCount(pageCount).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookSearchDTO> findLatestBookTitleByAuthor(String author) {
        return IBookRepository.findLatestBookByAuthor(author)
                .map(book -> new BookSearchDTO(book.getTitle(), book.getAuthor()));
    }

    @Override
    public List<BookSearchDTO> findBooksByPublisherInCurrentYear(String publisher) {
        Integer currentYear = Year.now().getValue();
        return IBookRepository.findByPublisherIgnoreCaseAndPublicationYear(publisher, currentYear).stream()
                .map(book -> new BookSearchDTO(book.getTitle(), book.getAuthor()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> searchByPublicationYear(Integer publicationYear) {
        return IBookRepository.findByPublicationYear(publicationYear).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> searchByGenreAuthorYear(String genre, String author, Integer year) {
        return IBookRepository.findByGenreIgnoreCaseAndAuthorIgnoreCaseAndPublicationYear(genre, author, year)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookSearchDTO> findBestSellers() {
        return IBookRepository.findBestSellers().stream()
                .map(book -> new BookSearchDTO(book.getTitle(), book.getAuthor()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookDTO> searchByDescriptionKeyword(String keyword) {
        return IBookRepository.findByDescriptionContainingIgnoreCase(keyword).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BookDTO toDTO(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publicationYear(book.getPublicationYear())
                .publisher(book.getPublisher())
                .genre(book.getGenre())
                .pageCount(book.getPageCount())
                .description(book.getDescription())
                .circulation(book.getCirculation())
                .build();
    }

    private Book toEntity(BookDTO dto) {
        return Book.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publicationYear(dto.getPublicationYear())
                .publisher(dto.getPublisher())
                .genre(dto.getGenre())
                .pageCount(dto.getPageCount())
                .description(dto.getDescription())
                .circulation(dto.getCirculation())
                .build();
    }
}