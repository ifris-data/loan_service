package io.github.ifris.loans.repository.search;

import io.github.ifris.loans.domain.LoanAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LoanAccount entity.
 */
public interface LoanAccountSearchRepository extends ElasticsearchRepository<LoanAccount, Long> {
}
