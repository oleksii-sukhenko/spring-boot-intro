package mate.academy;

import java.math.BigDecimal;
import mate.academy.model.Book;
import mate.academy.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootIntroHwApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIntroHwApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book theGodFatherBook = new Book();
            theGodFatherBook.setTitle("The GodFather");
            theGodFatherBook.setAuthor("Mario Puzo");
            theGodFatherBook.setIsbn("74359870379087345");
            theGodFatherBook.setPrice(BigDecimal.valueOf(9.99));
            theGodFatherBook.setDescription(
                    "Never Tell Anyone Outside The Family What You Are Thinking Again!"
            );
            theGodFatherBook.setCoverImage("Hand of master of puppets");

            bookService.save(theGodFatherBook);
            System.out.println(bookService.findAll());
        };
    }
}
