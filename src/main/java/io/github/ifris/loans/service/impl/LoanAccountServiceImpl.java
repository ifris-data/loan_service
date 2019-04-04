package io.github.ifris.loans.service.impl;

import io.github.ifris.loans.service.LoanAccountService;
import io.github.ifris.loans.domain.LoanAccount;
import io.github.ifris.loans.repository.LoanAccountRepository;
import io.github.ifris.loans.repository.search.LoanAccountSearchRepository;
import io.github.ifris.loans.service.dto.LoanAccountDTO;
import io.github.ifris.loans.service.mapper.LoanAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LoanAccount.
 */
@Service
@Transactional
public class LoanAccountServiceImpl implements LoanAccountService {

    private final Logger log = LoggerFactory.getLogger(LoanAccountServiceImpl.class);

    private final LoanAccountRepository loanAccountRepository;

    private final LoanAccountMapper loanAccountMapper;

    private final LoanAccountSearchRepository loanAccountSearchRepository;

    public LoanAccountServiceImpl(LoanAccountRepository loanAccountRepository, LoanAccountMapper loanAccountMapper, LoanAccountSearchRepository loanAccountSearchRepository) {
        this.loanAccountRepository = loanAccountRepository;
        this.loanAccountMapper = loanAccountMapper;
        this.loanAccountSearchRepository = loanAccountSearchRepository;
    }

    /**
     * Save a loanAccount.
     *
     * @param loanAccountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LoanAccountDTO save(LoanAccountDTO loanAccountDTO) {
        log.debug("Request to save LoanAccount : {}", loanAccountDTO);
        LoanAccount loanAccount = loanAccountMapper.toEntity(loanAccountDTO);
        loanAccount = loanAccountRepository.save(loanAccount);
        LoanAccountDTO result = loanAccountMapper.toDto(loanAccount);
        loanAccountSearchRepository.save(loanAccount);
        return result;
    }

    /**
     * Get all the loanAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LoanAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LoanAccounts");
        return loanAccountRepository.findAll(pageable)
            .map(loanAccountMapper::toDto);
    }


    /**
     * Get one loanAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LoanAccountDTO> findOne(Long id) {
        log.debug("Request to get LoanAccount : {}", id);
        return loanAccountRepository.findById(id)
            .map(loanAccountMapper::toDto);
    }

    /**
     * Delete the loanAccount by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LoanAccount : {}", id);
        loanAccountRepository.deleteById(id);
        loanAccountSearchRepository.deleteById(id);
    }

    /**
     * Search for the loanAccount corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LoanAccountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LoanAccounts for query {}", query);
        return loanAccountSearchRepository.search(queryStringQuery(query), pageable)
            .map(loanAccountMapper::toDto);
    }
}
