package io.github.ifris.loans.web.rest;
import io.github.ifris.loans.service.LoanAccountService;
import io.github.ifris.loans.web.rest.errors.BadRequestAlertException;
import io.github.ifris.loans.web.rest.util.HeaderUtil;
import io.github.ifris.loans.web.rest.util.PaginationUtil;
import io.github.ifris.loans.service.dto.LoanAccountDTO;
import io.github.ifris.loans.service.dto.LoanAccountCriteria;
import io.github.ifris.loans.service.LoanAccountQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing LoanAccount.
 */
@RestController
@RequestMapping("/api")
public class LoanAccountResource {

    private final Logger log = LoggerFactory.getLogger(LoanAccountResource.class);

    private static final String ENTITY_NAME = "loanserviceLoanAccount";

    private final LoanAccountService loanAccountService;

    private final LoanAccountQueryService loanAccountQueryService;

    public LoanAccountResource(LoanAccountService loanAccountService, LoanAccountQueryService loanAccountQueryService) {
        this.loanAccountService = loanAccountService;
        this.loanAccountQueryService = loanAccountQueryService;
    }

    /**
     * POST  /loan-accounts : Create a new loanAccount.
     *
     * @param loanAccountDTO the loanAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new loanAccountDTO, or with status 400 (Bad Request) if the loanAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/loan-accounts")
    public ResponseEntity<LoanAccountDTO> createLoanAccount(@Valid @RequestBody LoanAccountDTO loanAccountDTO) throws URISyntaxException {
        log.debug("REST request to save LoanAccount : {}", loanAccountDTO);
        if (loanAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanAccountDTO result = loanAccountService.save(loanAccountDTO);
        return ResponseEntity.created(new URI("/api/loan-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /loan-accounts : Updates an existing loanAccount.
     *
     * @param loanAccountDTO the loanAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated loanAccountDTO,
     * or with status 400 (Bad Request) if the loanAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the loanAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/loan-accounts")
    public ResponseEntity<LoanAccountDTO> updateLoanAccount(@Valid @RequestBody LoanAccountDTO loanAccountDTO) throws URISyntaxException {
        log.debug("REST request to update LoanAccount : {}", loanAccountDTO);
        if (loanAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LoanAccountDTO result = loanAccountService.save(loanAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, loanAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /loan-accounts : get all the loanAccounts.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of loanAccounts in body
     */
    @GetMapping("/loan-accounts")
    public ResponseEntity<List<LoanAccountDTO>> getAllLoanAccounts(LoanAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LoanAccounts by criteria: {}", criteria);
        Page<LoanAccountDTO> page = loanAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/loan-accounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /loan-accounts/count : count all the loanAccounts.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/loan-accounts/count")
    public ResponseEntity<Long> countLoanAccounts(LoanAccountCriteria criteria) {
        log.debug("REST request to count LoanAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanAccountQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /loan-accounts/:id : get the "id" loanAccount.
     *
     * @param id the id of the loanAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the loanAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/loan-accounts/{id}")
    public ResponseEntity<LoanAccountDTO> getLoanAccount(@PathVariable Long id) {
        log.debug("REST request to get LoanAccount : {}", id);
        Optional<LoanAccountDTO> loanAccountDTO = loanAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanAccountDTO);
    }

    /**
     * DELETE  /loan-accounts/:id : delete the "id" loanAccount.
     *
     * @param id the id of the loanAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/loan-accounts/{id}")
    public ResponseEntity<Void> deleteLoanAccount(@PathVariable Long id) {
        log.debug("REST request to delete LoanAccount : {}", id);
        loanAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/loan-accounts?query=:query : search for the loanAccount corresponding
     * to the query.
     *
     * @param query the query of the loanAccount search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/loan-accounts")
    public ResponseEntity<List<LoanAccountDTO>> searchLoanAccounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanAccounts for query {}", query);
        Page<LoanAccountDTO> page = loanAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/loan-accounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
