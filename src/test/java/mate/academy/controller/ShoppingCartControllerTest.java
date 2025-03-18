package mate.academy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemRequestDto;
import mate.academy.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:database/book/book-data.sql",
        "classpath:database/shoppingcart/shoppingcart-data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/shoppingcart/clear-shoppingcart-data.sql",
        "classpath:database/book/clear-book-data.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "user1@mail.com", "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")
                )
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities()
                )
        );
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser(username = "user1@mail.com", roles = "USER")
    @DisplayName("Get shopping cart info for authenticated user")
    void getAllInfoAboutShoppingCart_AuthenticatedUser_ReturnsShoppingCartDto() throws Exception {
        ShoppingCartDto expected = TestUtil.getShoppingCartDto();

        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );

        assertEquals(expected.getCartItemDtos(), actual.getCartItemDtos());
    }

    @Test
    @WithMockUser(username = "user1@mail.com", roles = "USER")
    @DisplayName("Add book to shopping cart for authenticated user")
    void addBookToShoppingCart_AuthenticatedUser_ReturnUpdatedShoppingCartDto() throws Exception {
        ShoppingCartDto expected = TestUtil.updateShoppingCartDto();

        CartItemRequestDto requestDto = new CartItemRequestDto()
                .setBookId(1L)
                .setQuantity(2);

        MvcResult result = mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartDto.class
        );

        assertEquals(expected.getCartItemDtos(), actual.getCartItemDtos());
    }
}
