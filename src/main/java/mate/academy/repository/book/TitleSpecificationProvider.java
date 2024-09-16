package mate.academy.repository.book;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import mate.academy.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                Arrays.stream(params)
                        .map(param -> "%" + param.toLowerCase() + "%")
                        .map(pattern -> criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(TITLE)), pattern)
                        )
                        .toArray(Predicate[]::new)
        );
    }
}
