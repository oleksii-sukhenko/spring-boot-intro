package mate.academy.repo.book;

import java.util.Arrays;
import mate.academy.model.Book;
import mate.academy.repo.SpecificationProvider;
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
        return (root, query, criteriaBuilder)
                -> root.get(TITLE).in(Arrays.stream(params).toArray());
    }
}
