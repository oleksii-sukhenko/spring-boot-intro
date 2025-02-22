package mate.academy.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:/database/book/book-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/database/book/clear-book-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find books by category ID")
    void findAllByCategoriesId_shouldReturnTrue_whenBookExists() {
        List<Book> booksInCategory1 = bookRepository.findAllByCategoriesId(1L);
        List<Book> booksInCategory2 = bookRepository.findAllByCategoriesId(2L);

        assertEquals(2, booksInCategory1.size(), "Category 1 should contain 2 books");
        assertEquals(1, booksInCategory2.size(), "Category 2 should contain 1 book");

        assertTrue(booksInCategory1.stream()
                .anyMatch(book -> book.getTitle().equals("First book")));
        assertTrue(booksInCategory1.stream()
                .anyMatch(book -> book.getTitle().equals("Second book")));
        assertTrue(booksInCategory2.stream()
                .anyMatch(book -> book.getTitle().equals("Third book")));
    }
}
