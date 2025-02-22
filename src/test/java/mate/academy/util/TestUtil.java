package mate.academy.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.model.Book;
import mate.academy.model.Category;

public class TestUtil {

    private static Category createCategory(Long id, String name, String description) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        return category;
    }

    private static Book createBook(
            Long id,
            String title,
            String author,
            String isbn,
            String description,
            String coverImage,
            BigDecimal price,
            Set<Category> categories
    ) {
        return new Book()
                .setId(id)
                .setTitle(title)
                .setAuthor(author)
                .setIsbn(isbn)
                .setDescription(description)
                .setCoverImage(coverImage)
                .setPrice(price)
                .setCategories(categories);
    }

    public static List<Book> getBooks() {
        Category firstCategory
                = createCategory(1L, "first category", "first category");
        Category secondCategory
                = createCategory(2L, "second category", "second category");

        return List.of(
                createBook(1L, "First book", "First author",
                        "first_isbn", "First description", "first_image.img",
                        BigDecimal.valueOf(1.11), Set.of(firstCategory)),
                createBook(2L, "Second book", "Second author",
                        "second_isbn", "Second description", "second_image.img",
                        BigDecimal.valueOf(1.11), Set.of(firstCategory)),
                createBook(3L, "Third book", "Third author",
                        "third_isbn", "Third description", "third_image.img",
                        BigDecimal.valueOf(1.11), Set.of(secondCategory))
        );
    }

    public static Book getBook() {
        return createBook(1L, "Book", "Author",
                "isbn", "description", "coverImage",
                BigDecimal.valueOf(9.99), Collections.emptySet());
    }

    public static BookDto getBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("isbn")
                .setPrice(BigDecimal.valueOf(9.99));
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Book")
                .setAuthor("Author")
                .setIsbn("isbn")
                .setPrice(BigDecimal.valueOf(9.99));
    }

    public static List<Category> getCategories() {
        return List.of(
                createCategory(1L, "first category", "first category"),
                createCategory(2L, "second category", "second category")
        );
    }

    public static Category getCategory() {
        return createCategory(1L, "test category", "test category");
    }

    public static CategoryDto getCategoryDto() {
        return new CategoryDto()
                .setId(1L)
                .setName("test category")
                .setDescription("test description");
    }

    public static CategoryRequestDto categoryRequestDto() {
        return new CategoryRequestDto()
                .setName("test category")
                .setDescription("test category");
    }

    public static Category getUpdatedCategory() {
        return new Category()
                .setId(1L)
                .setName("Updated Category")
                .setDescription("Updated Description");
    }

    public static CategoryDto getUpdatedCategoryDto() {
        return new CategoryDto()
                .setId(1L)
                .setName("Updated Category")
                .setDescription("Updated Description");
    }
}
