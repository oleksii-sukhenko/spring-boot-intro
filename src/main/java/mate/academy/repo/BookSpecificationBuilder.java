package mate.academy.repo;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.BookSearchParameters;
import mate.academy.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            specification
                    = specification.and(
                            bookSpecificationProviderManager
                                    .getSpecificationProvider(TITLE)
                                    .getSpecification(searchParameters.titles())
            );
        }
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            specification
                    = specification.and(
                            bookSpecificationProviderManager
                                    .getSpecificationProvider(AUTHOR)
                                    .getSpecification(searchParameters.authors())
            );
        }
        return specification;
    }
}
