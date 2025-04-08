package mate.academy.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import mate.academy.model.ShoppingCart;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
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
@Sql(scripts = "classpath:/database/shoppingcart/shoppingcart-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/database/shoppingcart/clear-shoppingcart-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "classpath:/database/book/clear-book-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by user ID")
    void findByUserId_ShouldReturnCart_WhenCartExists() {
        Long userId = 1L;

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);

        assertNotNull(shoppingCart, "Shopping cart should not be null");
        assertEquals(
                userId, shoppingCart.getUser().getId(),
                "Shopping cart should belong to the correct user"
        );
        assertFalse(
                shoppingCart.isDeleted(),
                "Shopping cart should not be marked as deleted"
        );
    }
}
