package io.github.ifris.loans.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of LoanAccountSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class LoanAccountSearchRepositoryMockConfiguration {

    @MockBean
    private LoanAccountSearchRepository mockLoanAccountSearchRepository;

}
