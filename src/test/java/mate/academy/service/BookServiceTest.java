package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
import mate.academy.service.book.impl.BookServiceImpl;
import mate.academy.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
            Verify save() method works
                        """)
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(TestUtil.getBook());
        when(bookRepository.save(any(Book.class))).thenReturn(TestUtil.getBook());
        when(bookMapper.toDto(any(Book.class))).thenReturn(TestUtil.getBookDto());

        BookDto result = bookService.save(TestUtil.createBookRequestDto());

        assertNotNull(result);
        assertEquals(TestUtil.getBookDto().getId(), result.getId());
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toModel(any(CreateBookRequestDto.class));
        verify(bookMapper).toDto(any(Book.class));
    }

    @Test
    @DisplayName("""
            Verify findById() returns BookDto when book exist
                        """)
    public void findById_ValidId_ReturnsBookDto() {
        Long bookId = 1L;
        Book expectedBook = TestUtil.getBook();
        BookDto expectedBookDto = TestUtil.getBookDto();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));
        when(bookMapper.toDto(expectedBook)).thenReturn(expectedBookDto);

        BookDto result = bookService.findById(bookId);

        assertNotNull(result);
        assertEquals(expectedBookDto, result);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(expectedBook);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findAll() returns list of BookDto")
    public void findAll_ValidEmail_ReturnsListOfBookDto() {
        String email = "test@example.com";
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> expectedBooks = TestUtil.getBooks();
        List<BookDto> expectedBookDtos = expectedBooks.stream()
                .map(book -> {
                    BookDto dto = new BookDto();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthor(book.getAuthor());
                    dto.setIsbn(book.getIsbn());
                    dto.setPrice(book.getPrice());
                    return dto;
                })
                .toList();

        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(expectedBooks));

        // Замокати кожен елемент списку
        for (int i = 0; i < expectedBooks.size(); i++) {
            when(bookMapper.toDto(expectedBooks.get(i))).thenReturn(expectedBookDtos.get(i));
        }

        List<BookDto> result = bookService.findAll(email, pageable);

        assertNotNull(result);
        assertEquals(expectedBookDtos, result);
        assertEquals(expectedBooks.size(), result.size());

        verify(bookRepository).findAll(pageable);
        for (Book book : expectedBooks) {
            verify(bookMapper).toDto(book);
        }
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify updateBookById() updates when book exists
                        """)
    public void updateBookById_ValidId_UpdatesBook() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();

        createUpdatedBook(requestDto);

        Long bookId = 1L;

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle(requestDto.getTitle());
        updatedBook.setAuthor(requestDto.getAuthor());
        updatedBook.setIsbn(requestDto.getIsbn());
        updatedBook.setPrice(requestDto.getPrice());

        Book existingBook = TestUtil.getBook();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookMapper).updateBookFromDto(requestDto, existingBook);
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        bookService.updateBookById(bookId, requestDto);

        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateBookFromDto(requestDto, existingBook);
        verify(bookRepository).save(existingBook);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    public void createUpdatedBook(CreateBookRequestDto requestDto) {
        requestDto.setTitle("Updated Title");
        requestDto.setAuthor("Updated Author");
        requestDto.setIsbn("updatedIsbn");
        requestDto.setPrice(BigDecimal.valueOf(9.99));
    }
}
