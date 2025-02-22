package mate.academy.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.service.category.CategoryService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

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
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("first category")
                .setDescription("first category");

        CategoryDto expected = new CategoryDto()
                .setId(1L)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        when(categoryService.save(any(CategoryRequestDto.class))).thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        CategoryDto actual = objectMapper.readValue(responseContent, CategoryDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser
    @Test
    @DisplayName("Get all book categories")
    void getAll_GivenCategories_ShouldReturnAllCategories() throws Exception {
        List<CategoryDto> expected = List.of(
                new CategoryDto().setId(1L).setName("first category")
                        .setDescription("first category"),
                new CategoryDto().setId(2L).setName("second category")
                        .setDescription("second category"),
                new CategoryDto().setId(3L).setName("third category")
                        .setDescription("third category")
        );

        Pageable pageable = PageRequest.of(0, 10);
        when(categoryService.findAll(pageable)).thenReturn(expected);

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                CategoryDto[].class
        );

        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.asList(actual));
    }

    @WithMockUser
    @Test
    @DisplayName("Get book category by ID")
    void getCategoryById_ValidId_ShouldReturnCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto expected = new CategoryDto()
                .setId(categoryId)
                .setName("test category")
                .setDescription("test category");

        when(categoryService.getById(categoryId)).thenReturn(expected);

        MvcResult result = mockMvc.perform(get("/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        CategoryDto actual = objectMapper.readValue(responseContent, CategoryDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book gategory by ID")
    void updateCategoryById_GivenValidRequest_ShouldUpdateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("updated category")
                .setDescription("updated category");

        CategoryDto expected = new CategoryDto()
                .setId(categoryId)
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        when(categoryService.update(eq(categoryId), any(CategoryRequestDto.class)))
                .thenReturn(expected);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                CategoryDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete category by ID")
    void deleteCategory_GivenValidId_ShouldDeleteCategory() throws Exception {
        Long categoryId = 1L;

        doNothing().when(categoryService).deleteById(categoryId);

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById(categoryId);
    }
}
