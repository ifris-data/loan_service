package io.github.ifris.loans.repository;

import io.github.ifris.loans.domain.LoanAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LoanAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoanAccountRepository extends JpaRepository<LoanAccount, Long>, JpaSpecificationExecutor<LoanAccount> {

}
