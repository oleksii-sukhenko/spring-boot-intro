package mate.academy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.model.User;
import mate.academy.service.book.BookService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        tearDown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/book-data.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        tearDown(dataSource);
    }

    @SneakyThrows
    static void tearDown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/book/clear-book-data.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("First book")
                .setAuthor("First author")
                .setIsbn("First isbn")
                .setPrice(BigDecimal.valueOf(1.11))
                .setDescription("First description")
                .setCoverImage("First image");

        BookDto expected = new BookDto()
                .setId(1L) // Додай ID, оскільки реальний сервіс його генерує
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage());

        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent); // Додано логування

        BookDto actual = objectMapper.readValue(responseContent, BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get all books")
    void getAll_GivenBooksInCatalog_ShouldReturnAllBooks() throws Exception {
        User user = new User();
        user.setEmail("user@mail.com");
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        List<BookDto> expectedBooks = List.of(
                new BookDto().setId(1L).setTitle("First book").setAuthor("First Author")
                        .setIsbn("first_isbn").setPrice(BigDecimal.valueOf(1.11))
                        .setDescription("First description").setCoverImage("first_image.img"),
                new BookDto().setId(2L).setTitle("Second book").setAuthor("Second Author")
                        .setIsbn("second_isbn").setPrice(BigDecimal.valueOf(2.22))
                        .setDescription("Second description").setCoverImage("second_image.img"),
                new BookDto().setId(3L).setTitle("Third book").setAuthor("Third Author")
                        .setIsbn("third_isbn").setPrice(BigDecimal.valueOf(3.33))
                        .setDescription("Third description").setCoverImage("third_image.img")
        );

        Pageable pageable = PageRequest.of(0, 10);

        when(bookService.findAll(anyString(), any(Pageable.class))).thenReturn(expectedBooks);

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Assertions.assertFalse(responseJson.isEmpty(), "Response body is empty!");

        BookDto[] actualBooks = objectMapper.readValue(responseJson, BookDto[].class);

        Assertions.assertEquals(expectedBooks.size(), actualBooks.length);
        Assertions.assertEquals(expectedBooks, Arrays.asList(actualBooks));
    }

    @WithMockUser
    @Test
    @DisplayName("Get book by ID")
    void getBookById_ValidId_ShouldReturnBook() throws Exception {
        Long bookId = 1L;
        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("Test Description")
                .setCoverImage("test-image.jpg");

        when(bookService.findById(bookId)).thenReturn(expected);

        MvcResult result = mockMvc.perform(
                get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        BookDto actual = objectMapper.readValue(responseContent, BookDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }
}
