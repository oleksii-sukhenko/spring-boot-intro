package mate.academy.repository.book;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import mate.academy.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR = "author";

    @Override
    public String getKey() {
        return AUTHOR;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                Arrays.stream(params)
                        .map(param -> "%" + param.toLowerCase() + "%")
                        .map(pattern -> criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(AUTHOR)), pattern)
                        )
                        .toArray(Predicate[]::new)
        );
    }
}
