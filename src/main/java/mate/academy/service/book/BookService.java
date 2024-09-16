package mate.academy.service.book;

import java.util.List;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParameters;
import mate.academy.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    BookDto findById(Long id);

    List<BookDto> findAll(Pageable pageable);

    void deleteById(Long id);

    void updateBookById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);
}
