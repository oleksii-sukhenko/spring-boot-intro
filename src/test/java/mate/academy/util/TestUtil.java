package mate.academy.util;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemDto;
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
        Category firstCategory = createCategory(1L, "first category", "first category");
        Category secondCategory = createCategory(2L, "second category", "second category");

        return List.of(
                createBook(1L, "First book", "First Author",
                        "first_isbn", "First description", "first_image.img",
                        BigDecimal.valueOf(1.11), Set.of(firstCategory)),
                createBook(2L, "Second book", "Second Author",
                        "second_isbn", "Second description", "second_image.img",
                        BigDecimal.valueOf(2.22), Set.of(firstCategory)),
                createBook(3L, "Third book", "Third Author",
                        "third_isbn", "Third description", "third_image.img",
                        BigDecimal.valueOf(3.33), Set.of(secondCategory))
        );
    }

    public static List<BookDto> getBookDtos() {
        return getBooks().stream()
                .map(book -> new BookDto()
                        .setId(book.getId())
                        .setTitle(book.getTitle())
                        .setAuthor(book.getAuthor())
                        .setIsbn(book.getIsbn())
                        .setPrice(book.getPrice())
                        .setDescription(book.getDescription())
                        .setCoverImage(book.getCoverImage())
                        .setCategoryIds(book.getCategories().stream()
                                .map(Category::getId)
                                .collect(Collectors.toSet())))
                .toList();
    }

    public static Book getBook() {
        return createBook(1L, "Book", "Author",
                "isbn", "description", "coverImage",
                BigDecimal.valueOf(9.99), Collections.emptySet());
    }

    public static BookDto getBookDto() {
        return new BookDto()
                .setId(4L)
                .setTitle("New Book")
                .setAuthor("New Author")
                .setIsbn("new_isbn")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("New Description")
                .setCoverImage("new_image.img")
                .setCategoryIds(Collections.emptySet());
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("New Book")
                .setAuthor("New Author")
                .setIsbn("new_isbn")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("New Description")
                .setCoverImage("new_image.img");
    }

    public static Book getUpdatedBook() {
        return new Book()
                .setId(1L)
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("updated_isbn")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Updated Description")
                .setCoverImage("updated_image.img")
                .setCategories(Collections.emptySet());
    }

    public static BookDto getUpdatedBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("updated_isbn")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("Updated Description")
                .setCoverImage("updated_image.img")
                .setCategoryIds(Collections.emptySet());
    }

    public static List<Category> getCategories() {
        return List.of(
                createCategory(1L, "first category", "first category"),
                createCategory(2L, "second category", "second category")
        );
    }

    public static List<CategoryDto> getCategoryDtos() {
        return getCategories().stream()
                .map(category -> new CategoryDto()
                        .setId(category.getId())
                        .setName(category.getName())
                        .setDescription(category.getDescription())
                ).toList();
    }

    public static Category getCategory() {
        return createCategory(1L, "first category", "first category");
    }

    public static CategoryDto getCategoryDto() {
        return new CategoryDto()
                .setId(1L)
                .setName("first category")
                .setDescription("first category");
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

    public static ShoppingCartDto getShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);

        CartItemDto cartItem1 = new CartItemDto();
        cartItem1.setId(1L);
        cartItem1.setBookId(1L);
        cartItem1.setBookTitle("First book");
        cartItem1.setQuantity(2);

        CartItemDto cartItem2 = new CartItemDto();
        cartItem2.setId(2L);
        cartItem2.setBookId(2L);
        cartItem2.setBookTitle("Second book");
        cartItem2.setQuantity(1);

        shoppingCartDto.setCartItemDtos(Set.of(cartItem1, cartItem2));

        return shoppingCartDto;
    }

    public static ShoppingCartDto updateShoppingCartDto() {
        CartItemDto cartItem1 = new CartItemDto();
        cartItem1.setId(1L);
        cartItem1.setBookId(1L);
        cartItem1.setBookTitle("First book");
        cartItem1.setQuantity(4);

        CartItemDto cartItem2 = new CartItemDto();
        cartItem2.setId(2L);
        cartItem2.setBookId(2L);
        cartItem2.setBookTitle("Second book");
        cartItem2.setQuantity(1);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L);

        shoppingCartDto.setCartItemDtos(Set.of(cartItem1, cartItem2));

        return shoppingCartDto;
    }

    public static ShoppingCartDto getShoppingCartDtoAfterDeletion() {
        CartItemDto cartItem2 = new CartItemDto()
                .setId(2L)
                .setBookId(2L)
                .setBookTitle("Second book")
                .setQuantity(1);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L);

        shoppingCartDto.setCartItemDtos(Set.of(cartItem2));

        return shoppingCartDto;
    }

}
