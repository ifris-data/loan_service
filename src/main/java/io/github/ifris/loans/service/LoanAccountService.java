package io.github.ifris.loans.service;

import io.github.ifris.loans.service.dto.LoanAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing LoanAccount.
 */
public interface LoanAccountService {

    /**
     * Save a loanAccount.
     *
     * @param loanAccountDTO the entity to save
     * @return the persisted entity
     */
    LoanAccountDTO save(LoanAccountDTO loanAccountDTO);

    /**
     * Get all the loanAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LoanAccountDTO> findAll(Pageable pageable);


    /**
     * Get the "id" loanAccount.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LoanAccountDTO> findOne(Long id);

    /**
     * Delete the "id" loanAccount.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the loanAccount corresponding to the query.
     *
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LoanAccountDTO> search(String query, Pageable pageable);
}
