package io.github.ifris.loans.service;

import io.github.ifris.loans.domain.LoanAccount;
import io.github.ifris.loans.repository.LoanAccountRepository;
import io.github.ifris.loans.repository.search.LoanAccountSearchRepository;
import io.github.ifris.loans.service.dto.LoanAccountCriteria;
import io.github.ifris.loans.service.dto.LoanAccountDTO;
import io.github.ifris.loans.service.mapper.LoanAccountMapper;
import io.github.jhipster.service.QueryService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for LoanAccount entities in the database.
 * The main input is a {@link LoanAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanAccountDTO} or a {@link Page} of {@link LoanAccountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanAccountQueryService extends QueryService<LoanAccount> {

    private final Logger log = LoggerFactory.getLogger(LoanAccountQueryService.class);

    private final LoanAccountRepository loanAccountRepository;

    private final LoanAccountMapper loanAccountMapper;

    private final LoanAccountSearchRepository loanAccountSearchRepository;

    public LoanAccountQueryService(LoanAccountRepository loanAccountRepository, LoanAccountMapper loanAccountMapper, LoanAccountSearchRepository loanAccountSearchRepository) {
        this.loanAccountRepository = loanAccountRepository;
        this.loanAccountMapper = loanAccountMapper;
        this.loanAccountSearchRepository = loanAccountSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanAccountDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanAccountDTO> findByCriteria(LoanAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanAccount> specification = createSpecification(criteria);
        return loanAccountMapper.toDto(loanAccountRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanAccountDTO} which matches the criteria from the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanAccountDTO> findByCriteria(LoanAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanAccount> specification = createSpecification(criteria);
        return loanAccountRepository.findAll(specification, page).map(loanAccountMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanAccount> specification = createSpecification(criteria);
        return loanAccountRepository.count(specification);
    }

    /**
     * Function to convert LoanAccountCriteria to a {@link Specification}
     */
    private Specification<LoanAccount> createSpecification(LoanAccountCriteria criteria) {
        Specification<LoanAccount> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), LoanAccount_.id));
            }
            if (criteria.getSbu() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSbu(), LoanAccount_.sbu));
            }
            if (criteria.getRmCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRmCode(), LoanAccount_.rmCode));
            }
            if (criteria.getGlCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGlCode(), LoanAccount_.glCode));
            }
            if (criteria.getSchemeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSchemeCode(), LoanAccount_.schemeCode));
            }
            if (criteria.getCustomerCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerCode(), LoanAccount_.customerCode));
            }
            if (criteria.getAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountNumber(), LoanAccount_.accountNumber));
            }
            if (criteria.getAccountName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountName(), LoanAccount_.accountName));
            }
            if (criteria.getCurrencyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyCode(), LoanAccount_.currencyCode));
            }
            if (criteria.getOpeningDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOpeningDate(), LoanAccount_.openingDate));
            }
            if (criteria.getAccountBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccountBalance(), LoanAccount_.accountBalance));
            }
            if (criteria.getLimitAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLimitAmount(), LoanAccount_.limitAmount));
            }
            if (criteria.getSystemClassification() != null) {
                specification = specification.and(buildSpecification(criteria.getSystemClassification(), LoanAccount_.systemClassification));
            }
            if (criteria.getUserClassification() != null) {
                specification = specification.and(buildSpecification(criteria.getUserClassification(), LoanAccount_.userClassification));
            }
            if (criteria.getNominalRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNominalRate(), LoanAccount_.nominalRate));
            }
            if (criteria.getExpiryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiryDate(), LoanAccount_.expiryDate));
            }
            if (criteria.getInterestSuspended() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInterestSuspended(), LoanAccount_.interestSuspended));
            }
            if (criteria.getLoanProvision() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLoanProvision(), LoanAccount_.loanProvision));
            }
            if (criteria.getEconomicSector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEconomicSector(), LoanAccount_.economicSector));
            }
            if (criteria.getEconomicSubSector() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEconomicSubSector(), LoanAccount_.economicSubSector));
            }
            if (criteria.getAppraisalMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAppraisalMonth(), LoanAccount_.appraisalMonth));
            }
        }
        return specification;
    }
}
