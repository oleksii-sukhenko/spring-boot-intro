package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.BookSearchParameters;
import mate.academy.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    BookDto findById(Long id);

    List<BookDto> findAll();

    void deleteById(Long id);

    void updateBookById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
