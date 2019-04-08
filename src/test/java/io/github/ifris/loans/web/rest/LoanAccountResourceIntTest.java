package io.github.ifris.loans.web.rest;

import io.github.ifris.loans.LoanserviceApp;

import io.github.ifris.loans.config.SecurityBeanOverrideConfiguration;

import io.github.ifris.loans.domain.LoanAccount;
import io.github.ifris.loans.repository.LoanAccountRepository;
import io.github.ifris.loans.repository.search.LoanAccountSearchRepository;
import io.github.ifris.loans.service.LoanAccountService;
import io.github.ifris.loans.service.dto.LoanAccountDTO;
import io.github.ifris.loans.service.mapper.LoanAccountMapper;
import io.github.ifris.loans.web.rest.errors.ExceptionTranslator;
import io.github.ifris.loans.service.dto.LoanAccountCriteria;
import io.github.ifris.loans.service.LoanAccountQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;


import static io.github.ifris.loans.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LoanAccountResource REST controller.
 *
 * @see LoanAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, LoanserviceApp.class})
public class LoanAccountResourceIntTest {

    private static final String DEFAULT_SBU = "AAAAAAAAAA";
    private static final String UPDATED_SBU = "BBBBBBBBBB";

    private static final String DEFAULT_RM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEME_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEME_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_CODE = "KES";
    private static final String UPDATED_CURRENCY_CODE = "USD";

    private static final LocalDate DEFAULT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(0);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_LIMIT_AMOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_LIMIT_AMOUNT = new BigDecimal(1);

    private static final Double DEFAULT_NOMINAL_RATE = 1D;
    private static final Double UPDATED_NOMINAL_RATE = 2D;

    private static final LocalDate DEFAULT_EXPIRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_INTEREST_SUSPENDED = new BigDecimal(0);
    private static final BigDecimal UPDATED_INTEREST_SUSPENDED = new BigDecimal(1);

    private static final BigDecimal DEFAULT_LOAN_PROVISION = new BigDecimal(0);
    private static final BigDecimal UPDATED_LOAN_PROVISION = new BigDecimal(1);

    private static final String DEFAULT_ECONOMIC_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_ECONOMIC_SECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_ECONOMIC_SUB_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_ECONOMIC_SUB_SECTOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_APPRAISAL_MONTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPRAISAL_MONTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SYSTEM_CLASSIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_SYSTEM_CLASSIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_CLASSIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_USER_CLASSIFICATION = "BBBBBBBBBB";

    @Autowired
    private LoanAccountRepository loanAccountRepository;

    @Autowired
    private LoanAccountMapper loanAccountMapper;

    @Autowired
    private LoanAccountService loanAccountService;

    /**
     * This repository is mocked in the io.github.ifris.loans.repository.search test package.
     *
     * @see io.github.ifris.loans.repository.search.LoanAccountSearchRepositoryMockConfiguration
     */
    @Autowired
    private LoanAccountSearchRepository mockLoanAccountSearchRepository;

    @Autowired
    private LoanAccountQueryService loanAccountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLoanAccountMockMvc;

    private LoanAccount loanAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoanAccountResource loanAccountResource = new LoanAccountResource(loanAccountService, loanAccountQueryService);
        this.restLoanAccountMockMvc = MockMvcBuilders.standaloneSetup(loanAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LoanAccount createEntity(EntityManager em) {
        LoanAccount loanAccount = new LoanAccount()
            .sbu(DEFAULT_SBU)
            .rmCode(DEFAULT_RM_CODE)
            .glCode(DEFAULT_GL_CODE)
            .schemeCode(DEFAULT_SCHEME_CODE)
            .customerCode(DEFAULT_CUSTOMER_CODE)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .accountName(DEFAULT_ACCOUNT_NAME)
            .currencyCode(DEFAULT_CURRENCY_CODE)
            .openingDate(DEFAULT_OPENING_DATE)
            .accountBalance(DEFAULT_ACCOUNT_BALANCE)
            .limitAmount(DEFAULT_LIMIT_AMOUNT)
            .nominalRate(DEFAULT_NOMINAL_RATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .interestSuspended(DEFAULT_INTEREST_SUSPENDED)
            .loanProvision(DEFAULT_LOAN_PROVISION)
            .economicSector(DEFAULT_ECONOMIC_SECTOR)
            .economicSubSector(DEFAULT_ECONOMIC_SUB_SECTOR)
            .appraisalMonth(DEFAULT_APPRAISAL_MONTH)
            .systemClassification(DEFAULT_SYSTEM_CLASSIFICATION)
            .userClassification(DEFAULT_USER_CLASSIFICATION);
        return loanAccount;
    }

    @Before
    public void initTest() {
        loanAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoanAccount() throws Exception {
        int databaseSizeBeforeCreate = loanAccountRepository.findAll().size();

        // Create the LoanAccount
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);
        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the LoanAccount in the database
        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeCreate + 1);
        LoanAccount testLoanAccount = loanAccountList.get(loanAccountList.size() - 1);
        assertThat(testLoanAccount.getSbu()).isEqualTo(DEFAULT_SBU);
        assertThat(testLoanAccount.getRmCode()).isEqualTo(DEFAULT_RM_CODE);
        assertThat(testLoanAccount.getGlCode()).isEqualTo(DEFAULT_GL_CODE);
        assertThat(testLoanAccount.getSchemeCode()).isEqualTo(DEFAULT_SCHEME_CODE);
        assertThat(testLoanAccount.getCustomerCode()).isEqualTo(DEFAULT_CUSTOMER_CODE);
        assertThat(testLoanAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testLoanAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testLoanAccount.getCurrencyCode()).isEqualTo(DEFAULT_CURRENCY_CODE);
        assertThat(testLoanAccount.getOpeningDate()).isEqualTo(DEFAULT_OPENING_DATE);
        assertThat(testLoanAccount.getAccountBalance()).isEqualTo(DEFAULT_ACCOUNT_BALANCE);
        assertThat(testLoanAccount.getLimitAmount()).isEqualTo(DEFAULT_LIMIT_AMOUNT);
        assertThat(testLoanAccount.getNominalRate()).isEqualTo(DEFAULT_NOMINAL_RATE);
        assertThat(testLoanAccount.getExpiryDate()).isEqualTo(DEFAULT_EXPIRY_DATE);
        assertThat(testLoanAccount.getInterestSuspended()).isEqualTo(DEFAULT_INTEREST_SUSPENDED);
        assertThat(testLoanAccount.getLoanProvision()).isEqualTo(DEFAULT_LOAN_PROVISION);
        assertThat(testLoanAccount.getEconomicSector()).isEqualTo(DEFAULT_ECONOMIC_SECTOR);
        assertThat(testLoanAccount.getEconomicSubSector()).isEqualTo(DEFAULT_ECONOMIC_SUB_SECTOR);
        assertThat(testLoanAccount.getAppraisalMonth()).isEqualTo(DEFAULT_APPRAISAL_MONTH);
        assertThat(testLoanAccount.getSystemClassification()).isEqualTo(DEFAULT_SYSTEM_CLASSIFICATION);
        assertThat(testLoanAccount.getUserClassification()).isEqualTo(DEFAULT_USER_CLASSIFICATION);

        // Validate the LoanAccount in Elasticsearch
        verify(mockLoanAccountSearchRepository, times(1)).save(testLoanAccount);
    }

    @Test
    @Transactional
    public void createLoanAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loanAccountRepository.findAll().size();

        // Create the LoanAccount with an existing ID
        loanAccount.setId(1L);
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoanAccount in the database
        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeCreate);

        // Validate the LoanAccount in Elasticsearch
        verify(mockLoanAccountSearchRepository, times(0)).save(loanAccount);
    }

    @Test
    @Transactional
    public void checkSbuIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setSbu(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRmCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setRmCode(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGlCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setGlCode(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSchemeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setSchemeCode(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setCustomerCode(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setAccountNumber(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setAccountName(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrencyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setCurrencyCode(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOpeningDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setOpeningDate(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setAccountBalance(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLimitAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setLimitAmount(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEconomicSectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setEconomicSector(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEconomicSubSectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setEconomicSubSector(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAppraisalMonthIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setAppraisalMonth(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSystemClassificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setSystemClassification(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserClassificationIsRequired() throws Exception {
        int databaseSizeBeforeTest = loanAccountRepository.findAll().size();
        // set the field null
        loanAccount.setUserClassification(null);

        // Create the LoanAccount, which fails.
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        restLoanAccountMockMvc.perform(post("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLoanAccounts() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList
        restLoanAccountMockMvc.perform(get("/api/loan-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].sbu").value(hasItem(DEFAULT_SBU.toString())))
            .andExpect(jsonPath("$.[*].rmCode").value(hasItem(DEFAULT_RM_CODE.toString())))
            .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE.toString())))
            .andExpect(jsonPath("$.[*].schemeCode").value(hasItem(DEFAULT_SCHEME_CODE.toString())))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE.toString())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME.toString())))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE.toString())))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].limitAmount").value(hasItem(DEFAULT_LIMIT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].nominalRate").value(hasItem(DEFAULT_NOMINAL_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].interestSuspended").value(hasItem(DEFAULT_INTEREST_SUSPENDED.intValue())))
            .andExpect(jsonPath("$.[*].loanProvision").value(hasItem(DEFAULT_LOAN_PROVISION.intValue())))
            .andExpect(jsonPath("$.[*].economicSector").value(hasItem(DEFAULT_ECONOMIC_SECTOR.toString())))
            .andExpect(jsonPath("$.[*].economicSubSector").value(hasItem(DEFAULT_ECONOMIC_SUB_SECTOR.toString())))
            .andExpect(jsonPath("$.[*].appraisalMonth").value(hasItem(DEFAULT_APPRAISAL_MONTH.toString())))
            .andExpect(jsonPath("$.[*].systemClassification").value(hasItem(DEFAULT_SYSTEM_CLASSIFICATION.toString())))
            .andExpect(jsonPath("$.[*].userClassification").value(hasItem(DEFAULT_USER_CLASSIFICATION.toString())));
    }
    
    @Test
    @Transactional
    public void getLoanAccount() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get the loanAccount
        restLoanAccountMockMvc.perform(get("/api/loan-accounts/{id}", loanAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loanAccount.getId().intValue()))
            .andExpect(jsonPath("$.sbu").value(DEFAULT_SBU.toString()))
            .andExpect(jsonPath("$.rmCode").value(DEFAULT_RM_CODE.toString()))
            .andExpect(jsonPath("$.glCode").value(DEFAULT_GL_CODE.toString()))
            .andExpect(jsonPath("$.schemeCode").value(DEFAULT_SCHEME_CODE.toString()))
            .andExpect(jsonPath("$.customerCode").value(DEFAULT_CUSTOMER_CODE.toString()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME.toString()))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE.toString()))
            .andExpect(jsonPath("$.openingDate").value(DEFAULT_OPENING_DATE.toString()))
            .andExpect(jsonPath("$.accountBalance").value(DEFAULT_ACCOUNT_BALANCE.intValue()))
            .andExpect(jsonPath("$.limitAmount").value(DEFAULT_LIMIT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.nominalRate").value(DEFAULT_NOMINAL_RATE.doubleValue()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.interestSuspended").value(DEFAULT_INTEREST_SUSPENDED.intValue()))
            .andExpect(jsonPath("$.loanProvision").value(DEFAULT_LOAN_PROVISION.intValue()))
            .andExpect(jsonPath("$.economicSector").value(DEFAULT_ECONOMIC_SECTOR.toString()))
            .andExpect(jsonPath("$.economicSubSector").value(DEFAULT_ECONOMIC_SUB_SECTOR.toString()))
            .andExpect(jsonPath("$.appraisalMonth").value(DEFAULT_APPRAISAL_MONTH.toString()))
            .andExpect(jsonPath("$.systemClassification").value(DEFAULT_SYSTEM_CLASSIFICATION.toString()))
            .andExpect(jsonPath("$.userClassification").value(DEFAULT_USER_CLASSIFICATION.toString()));
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySbuIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where sbu equals to DEFAULT_SBU
        defaultLoanAccountShouldBeFound("sbu.equals=" + DEFAULT_SBU);

        // Get all the loanAccountList where sbu equals to UPDATED_SBU
        defaultLoanAccountShouldNotBeFound("sbu.equals=" + UPDATED_SBU);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySbuIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where sbu in DEFAULT_SBU or UPDATED_SBU
        defaultLoanAccountShouldBeFound("sbu.in=" + DEFAULT_SBU + "," + UPDATED_SBU);

        // Get all the loanAccountList where sbu equals to UPDATED_SBU
        defaultLoanAccountShouldNotBeFound("sbu.in=" + UPDATED_SBU);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySbuIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where sbu is not null
        defaultLoanAccountShouldBeFound("sbu.specified=true");

        // Get all the loanAccountList where sbu is null
        defaultLoanAccountShouldNotBeFound("sbu.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByRmCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where rmCode equals to DEFAULT_RM_CODE
        defaultLoanAccountShouldBeFound("rmCode.equals=" + DEFAULT_RM_CODE);

        // Get all the loanAccountList where rmCode equals to UPDATED_RM_CODE
        defaultLoanAccountShouldNotBeFound("rmCode.equals=" + UPDATED_RM_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByRmCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where rmCode in DEFAULT_RM_CODE or UPDATED_RM_CODE
        defaultLoanAccountShouldBeFound("rmCode.in=" + DEFAULT_RM_CODE + "," + UPDATED_RM_CODE);

        // Get all the loanAccountList where rmCode equals to UPDATED_RM_CODE
        defaultLoanAccountShouldNotBeFound("rmCode.in=" + UPDATED_RM_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByRmCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where rmCode is not null
        defaultLoanAccountShouldBeFound("rmCode.specified=true");

        // Get all the loanAccountList where rmCode is null
        defaultLoanAccountShouldNotBeFound("rmCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByGlCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where glCode equals to DEFAULT_GL_CODE
        defaultLoanAccountShouldBeFound("glCode.equals=" + DEFAULT_GL_CODE);

        // Get all the loanAccountList where glCode equals to UPDATED_GL_CODE
        defaultLoanAccountShouldNotBeFound("glCode.equals=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByGlCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where glCode in DEFAULT_GL_CODE or UPDATED_GL_CODE
        defaultLoanAccountShouldBeFound("glCode.in=" + DEFAULT_GL_CODE + "," + UPDATED_GL_CODE);

        // Get all the loanAccountList where glCode equals to UPDATED_GL_CODE
        defaultLoanAccountShouldNotBeFound("glCode.in=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByGlCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where glCode is not null
        defaultLoanAccountShouldBeFound("glCode.specified=true");

        // Get all the loanAccountList where glCode is null
        defaultLoanAccountShouldNotBeFound("glCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySchemeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where schemeCode equals to DEFAULT_SCHEME_CODE
        defaultLoanAccountShouldBeFound("schemeCode.equals=" + DEFAULT_SCHEME_CODE);

        // Get all the loanAccountList where schemeCode equals to UPDATED_SCHEME_CODE
        defaultLoanAccountShouldNotBeFound("schemeCode.equals=" + UPDATED_SCHEME_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySchemeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where schemeCode in DEFAULT_SCHEME_CODE or UPDATED_SCHEME_CODE
        defaultLoanAccountShouldBeFound("schemeCode.in=" + DEFAULT_SCHEME_CODE + "," + UPDATED_SCHEME_CODE);

        // Get all the loanAccountList where schemeCode equals to UPDATED_SCHEME_CODE
        defaultLoanAccountShouldNotBeFound("schemeCode.in=" + UPDATED_SCHEME_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySchemeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where schemeCode is not null
        defaultLoanAccountShouldBeFound("schemeCode.specified=true");

        // Get all the loanAccountList where schemeCode is null
        defaultLoanAccountShouldNotBeFound("schemeCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByCustomerCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where customerCode equals to DEFAULT_CUSTOMER_CODE
        defaultLoanAccountShouldBeFound("customerCode.equals=" + DEFAULT_CUSTOMER_CODE);

        // Get all the loanAccountList where customerCode equals to UPDATED_CUSTOMER_CODE
        defaultLoanAccountShouldNotBeFound("customerCode.equals=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByCustomerCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where customerCode in DEFAULT_CUSTOMER_CODE or UPDATED_CUSTOMER_CODE
        defaultLoanAccountShouldBeFound("customerCode.in=" + DEFAULT_CUSTOMER_CODE + "," + UPDATED_CUSTOMER_CODE);

        // Get all the loanAccountList where customerCode equals to UPDATED_CUSTOMER_CODE
        defaultLoanAccountShouldNotBeFound("customerCode.in=" + UPDATED_CUSTOMER_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByCustomerCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where customerCode is not null
        defaultLoanAccountShouldBeFound("customerCode.specified=true");

        // Get all the loanAccountList where customerCode is null
        defaultLoanAccountShouldNotBeFound("customerCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultLoanAccountShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the loanAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultLoanAccountShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultLoanAccountShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the loanAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultLoanAccountShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountNumber is not null
        defaultLoanAccountShouldBeFound("accountNumber.specified=true");

        // Get all the loanAccountList where accountNumber is null
        defaultLoanAccountShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultLoanAccountShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the loanAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultLoanAccountShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultLoanAccountShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the loanAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultLoanAccountShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountName is not null
        defaultLoanAccountShouldBeFound("accountName.specified=true");

        // Get all the loanAccountList where accountName is null
        defaultLoanAccountShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByCurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where currencyCode equals to DEFAULT_CURRENCY_CODE
        defaultLoanAccountShouldBeFound("currencyCode.equals=" + DEFAULT_CURRENCY_CODE);

        // Get all the loanAccountList where currencyCode equals to UPDATED_CURRENCY_CODE
        defaultLoanAccountShouldNotBeFound("currencyCode.equals=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByCurrencyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where currencyCode in DEFAULT_CURRENCY_CODE or UPDATED_CURRENCY_CODE
        defaultLoanAccountShouldBeFound("currencyCode.in=" + DEFAULT_CURRENCY_CODE + "," + UPDATED_CURRENCY_CODE);

        // Get all the loanAccountList where currencyCode equals to UPDATED_CURRENCY_CODE
        defaultLoanAccountShouldNotBeFound("currencyCode.in=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByCurrencyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where currencyCode is not null
        defaultLoanAccountShouldBeFound("currencyCode.specified=true");

        // Get all the loanAccountList where currencyCode is null
        defaultLoanAccountShouldNotBeFound("currencyCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where openingDate equals to DEFAULT_OPENING_DATE
        defaultLoanAccountShouldBeFound("openingDate.equals=" + DEFAULT_OPENING_DATE);

        // Get all the loanAccountList where openingDate equals to UPDATED_OPENING_DATE
        defaultLoanAccountShouldNotBeFound("openingDate.equals=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where openingDate in DEFAULT_OPENING_DATE or UPDATED_OPENING_DATE
        defaultLoanAccountShouldBeFound("openingDate.in=" + DEFAULT_OPENING_DATE + "," + UPDATED_OPENING_DATE);

        // Get all the loanAccountList where openingDate equals to UPDATED_OPENING_DATE
        defaultLoanAccountShouldNotBeFound("openingDate.in=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where openingDate is not null
        defaultLoanAccountShouldBeFound("openingDate.specified=true");

        // Get all the loanAccountList where openingDate is null
        defaultLoanAccountShouldNotBeFound("openingDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where openingDate greater than or equals to DEFAULT_OPENING_DATE
        defaultLoanAccountShouldBeFound("openingDate.greaterOrEqualThan=" + DEFAULT_OPENING_DATE);

        // Get all the loanAccountList where openingDate greater than or equals to UPDATED_OPENING_DATE
        defaultLoanAccountShouldNotBeFound("openingDate.greaterOrEqualThan=" + UPDATED_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where openingDate less than or equals to DEFAULT_OPENING_DATE
        defaultLoanAccountShouldNotBeFound("openingDate.lessThan=" + DEFAULT_OPENING_DATE);

        // Get all the loanAccountList where openingDate less than or equals to UPDATED_OPENING_DATE
        defaultLoanAccountShouldBeFound("openingDate.lessThan=" + UPDATED_OPENING_DATE);
    }


    @Test
    @Transactional
    public void getAllLoanAccountsByAccountBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountBalance equals to DEFAULT_ACCOUNT_BALANCE
        defaultLoanAccountShouldBeFound("accountBalance.equals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the loanAccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultLoanAccountShouldNotBeFound("accountBalance.equals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountBalance in DEFAULT_ACCOUNT_BALANCE or UPDATED_ACCOUNT_BALANCE
        defaultLoanAccountShouldBeFound("accountBalance.in=" + DEFAULT_ACCOUNT_BALANCE + "," + UPDATED_ACCOUNT_BALANCE);

        // Get all the loanAccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultLoanAccountShouldNotBeFound("accountBalance.in=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAccountBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where accountBalance is not null
        defaultLoanAccountShouldBeFound("accountBalance.specified=true");

        // Get all the loanAccountList where accountBalance is null
        defaultLoanAccountShouldNotBeFound("accountBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByLimitAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where limitAmount equals to DEFAULT_LIMIT_AMOUNT
        defaultLoanAccountShouldBeFound("limitAmount.equals=" + DEFAULT_LIMIT_AMOUNT);

        // Get all the loanAccountList where limitAmount equals to UPDATED_LIMIT_AMOUNT
        defaultLoanAccountShouldNotBeFound("limitAmount.equals=" + UPDATED_LIMIT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByLimitAmountIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where limitAmount in DEFAULT_LIMIT_AMOUNT or UPDATED_LIMIT_AMOUNT
        defaultLoanAccountShouldBeFound("limitAmount.in=" + DEFAULT_LIMIT_AMOUNT + "," + UPDATED_LIMIT_AMOUNT);

        // Get all the loanAccountList where limitAmount equals to UPDATED_LIMIT_AMOUNT
        defaultLoanAccountShouldNotBeFound("limitAmount.in=" + UPDATED_LIMIT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByLimitAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where limitAmount is not null
        defaultLoanAccountShouldBeFound("limitAmount.specified=true");

        // Get all the loanAccountList where limitAmount is null
        defaultLoanAccountShouldNotBeFound("limitAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByNominalRateIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where nominalRate equals to DEFAULT_NOMINAL_RATE
        defaultLoanAccountShouldBeFound("nominalRate.equals=" + DEFAULT_NOMINAL_RATE);

        // Get all the loanAccountList where nominalRate equals to UPDATED_NOMINAL_RATE
        defaultLoanAccountShouldNotBeFound("nominalRate.equals=" + UPDATED_NOMINAL_RATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByNominalRateIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where nominalRate in DEFAULT_NOMINAL_RATE or UPDATED_NOMINAL_RATE
        defaultLoanAccountShouldBeFound("nominalRate.in=" + DEFAULT_NOMINAL_RATE + "," + UPDATED_NOMINAL_RATE);

        // Get all the loanAccountList where nominalRate equals to UPDATED_NOMINAL_RATE
        defaultLoanAccountShouldNotBeFound("nominalRate.in=" + UPDATED_NOMINAL_RATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByNominalRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where nominalRate is not null
        defaultLoanAccountShouldBeFound("nominalRate.specified=true");

        // Get all the loanAccountList where nominalRate is null
        defaultLoanAccountShouldNotBeFound("nominalRate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByExpiryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where expiryDate equals to DEFAULT_EXPIRY_DATE
        defaultLoanAccountShouldBeFound("expiryDate.equals=" + DEFAULT_EXPIRY_DATE);

        // Get all the loanAccountList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultLoanAccountShouldNotBeFound("expiryDate.equals=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByExpiryDateIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where expiryDate in DEFAULT_EXPIRY_DATE or UPDATED_EXPIRY_DATE
        defaultLoanAccountShouldBeFound("expiryDate.in=" + DEFAULT_EXPIRY_DATE + "," + UPDATED_EXPIRY_DATE);

        // Get all the loanAccountList where expiryDate equals to UPDATED_EXPIRY_DATE
        defaultLoanAccountShouldNotBeFound("expiryDate.in=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByExpiryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where expiryDate is not null
        defaultLoanAccountShouldBeFound("expiryDate.specified=true");

        // Get all the loanAccountList where expiryDate is null
        defaultLoanAccountShouldNotBeFound("expiryDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByExpiryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where expiryDate greater than or equals to DEFAULT_EXPIRY_DATE
        defaultLoanAccountShouldBeFound("expiryDate.greaterOrEqualThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the loanAccountList where expiryDate greater than or equals to UPDATED_EXPIRY_DATE
        defaultLoanAccountShouldNotBeFound("expiryDate.greaterOrEqualThan=" + UPDATED_EXPIRY_DATE);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByExpiryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where expiryDate less than or equals to DEFAULT_EXPIRY_DATE
        defaultLoanAccountShouldNotBeFound("expiryDate.lessThan=" + DEFAULT_EXPIRY_DATE);

        // Get all the loanAccountList where expiryDate less than or equals to UPDATED_EXPIRY_DATE
        defaultLoanAccountShouldBeFound("expiryDate.lessThan=" + UPDATED_EXPIRY_DATE);
    }


    @Test
    @Transactional
    public void getAllLoanAccountsByInterestSuspendedIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where interestSuspended equals to DEFAULT_INTEREST_SUSPENDED
        defaultLoanAccountShouldBeFound("interestSuspended.equals=" + DEFAULT_INTEREST_SUSPENDED);

        // Get all the loanAccountList where interestSuspended equals to UPDATED_INTEREST_SUSPENDED
        defaultLoanAccountShouldNotBeFound("interestSuspended.equals=" + UPDATED_INTEREST_SUSPENDED);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByInterestSuspendedIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where interestSuspended in DEFAULT_INTEREST_SUSPENDED or UPDATED_INTEREST_SUSPENDED
        defaultLoanAccountShouldBeFound("interestSuspended.in=" + DEFAULT_INTEREST_SUSPENDED + "," + UPDATED_INTEREST_SUSPENDED);

        // Get all the loanAccountList where interestSuspended equals to UPDATED_INTEREST_SUSPENDED
        defaultLoanAccountShouldNotBeFound("interestSuspended.in=" + UPDATED_INTEREST_SUSPENDED);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByInterestSuspendedIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where interestSuspended is not null
        defaultLoanAccountShouldBeFound("interestSuspended.specified=true");

        // Get all the loanAccountList where interestSuspended is null
        defaultLoanAccountShouldNotBeFound("interestSuspended.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByLoanProvisionIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where loanProvision equals to DEFAULT_LOAN_PROVISION
        defaultLoanAccountShouldBeFound("loanProvision.equals=" + DEFAULT_LOAN_PROVISION);

        // Get all the loanAccountList where loanProvision equals to UPDATED_LOAN_PROVISION
        defaultLoanAccountShouldNotBeFound("loanProvision.equals=" + UPDATED_LOAN_PROVISION);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByLoanProvisionIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where loanProvision in DEFAULT_LOAN_PROVISION or UPDATED_LOAN_PROVISION
        defaultLoanAccountShouldBeFound("loanProvision.in=" + DEFAULT_LOAN_PROVISION + "," + UPDATED_LOAN_PROVISION);

        // Get all the loanAccountList where loanProvision equals to UPDATED_LOAN_PROVISION
        defaultLoanAccountShouldNotBeFound("loanProvision.in=" + UPDATED_LOAN_PROVISION);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByLoanProvisionIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where loanProvision is not null
        defaultLoanAccountShouldBeFound("loanProvision.specified=true");

        // Get all the loanAccountList where loanProvision is null
        defaultLoanAccountShouldNotBeFound("loanProvision.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByEconomicSectorIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where economicSector equals to DEFAULT_ECONOMIC_SECTOR
        defaultLoanAccountShouldBeFound("economicSector.equals=" + DEFAULT_ECONOMIC_SECTOR);

        // Get all the loanAccountList where economicSector equals to UPDATED_ECONOMIC_SECTOR
        defaultLoanAccountShouldNotBeFound("economicSector.equals=" + UPDATED_ECONOMIC_SECTOR);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByEconomicSectorIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where economicSector in DEFAULT_ECONOMIC_SECTOR or UPDATED_ECONOMIC_SECTOR
        defaultLoanAccountShouldBeFound("economicSector.in=" + DEFAULT_ECONOMIC_SECTOR + "," + UPDATED_ECONOMIC_SECTOR);

        // Get all the loanAccountList where economicSector equals to UPDATED_ECONOMIC_SECTOR
        defaultLoanAccountShouldNotBeFound("economicSector.in=" + UPDATED_ECONOMIC_SECTOR);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByEconomicSectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where economicSector is not null
        defaultLoanAccountShouldBeFound("economicSector.specified=true");

        // Get all the loanAccountList where economicSector is null
        defaultLoanAccountShouldNotBeFound("economicSector.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByEconomicSubSectorIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where economicSubSector equals to DEFAULT_ECONOMIC_SUB_SECTOR
        defaultLoanAccountShouldBeFound("economicSubSector.equals=" + DEFAULT_ECONOMIC_SUB_SECTOR);

        // Get all the loanAccountList where economicSubSector equals to UPDATED_ECONOMIC_SUB_SECTOR
        defaultLoanAccountShouldNotBeFound("economicSubSector.equals=" + UPDATED_ECONOMIC_SUB_SECTOR);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByEconomicSubSectorIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where economicSubSector in DEFAULT_ECONOMIC_SUB_SECTOR or UPDATED_ECONOMIC_SUB_SECTOR
        defaultLoanAccountShouldBeFound("economicSubSector.in=" + DEFAULT_ECONOMIC_SUB_SECTOR + "," + UPDATED_ECONOMIC_SUB_SECTOR);

        // Get all the loanAccountList where economicSubSector equals to UPDATED_ECONOMIC_SUB_SECTOR
        defaultLoanAccountShouldNotBeFound("economicSubSector.in=" + UPDATED_ECONOMIC_SUB_SECTOR);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByEconomicSubSectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where economicSubSector is not null
        defaultLoanAccountShouldBeFound("economicSubSector.specified=true");

        // Get all the loanAccountList where economicSubSector is null
        defaultLoanAccountShouldNotBeFound("economicSubSector.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAppraisalMonthIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where appraisalMonth equals to DEFAULT_APPRAISAL_MONTH
        defaultLoanAccountShouldBeFound("appraisalMonth.equals=" + DEFAULT_APPRAISAL_MONTH);

        // Get all the loanAccountList where appraisalMonth equals to UPDATED_APPRAISAL_MONTH
        defaultLoanAccountShouldNotBeFound("appraisalMonth.equals=" + UPDATED_APPRAISAL_MONTH);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAppraisalMonthIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where appraisalMonth in DEFAULT_APPRAISAL_MONTH or UPDATED_APPRAISAL_MONTH
        defaultLoanAccountShouldBeFound("appraisalMonth.in=" + DEFAULT_APPRAISAL_MONTH + "," + UPDATED_APPRAISAL_MONTH);

        // Get all the loanAccountList where appraisalMonth equals to UPDATED_APPRAISAL_MONTH
        defaultLoanAccountShouldNotBeFound("appraisalMonth.in=" + UPDATED_APPRAISAL_MONTH);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAppraisalMonthIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where appraisalMonth is not null
        defaultLoanAccountShouldBeFound("appraisalMonth.specified=true");

        // Get all the loanAccountList where appraisalMonth is null
        defaultLoanAccountShouldNotBeFound("appraisalMonth.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAppraisalMonthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where appraisalMonth greater than or equals to DEFAULT_APPRAISAL_MONTH
        defaultLoanAccountShouldBeFound("appraisalMonth.greaterOrEqualThan=" + DEFAULT_APPRAISAL_MONTH);

        // Get all the loanAccountList where appraisalMonth greater than or equals to UPDATED_APPRAISAL_MONTH
        defaultLoanAccountShouldNotBeFound("appraisalMonth.greaterOrEqualThan=" + UPDATED_APPRAISAL_MONTH);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByAppraisalMonthIsLessThanSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where appraisalMonth less than or equals to DEFAULT_APPRAISAL_MONTH
        defaultLoanAccountShouldNotBeFound("appraisalMonth.lessThan=" + DEFAULT_APPRAISAL_MONTH);

        // Get all the loanAccountList where appraisalMonth less than or equals to UPDATED_APPRAISAL_MONTH
        defaultLoanAccountShouldBeFound("appraisalMonth.lessThan=" + UPDATED_APPRAISAL_MONTH);
    }


    @Test
    @Transactional
    public void getAllLoanAccountsBySystemClassificationIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where systemClassification equals to DEFAULT_SYSTEM_CLASSIFICATION
        defaultLoanAccountShouldBeFound("systemClassification.equals=" + DEFAULT_SYSTEM_CLASSIFICATION);

        // Get all the loanAccountList where systemClassification equals to UPDATED_SYSTEM_CLASSIFICATION
        defaultLoanAccountShouldNotBeFound("systemClassification.equals=" + UPDATED_SYSTEM_CLASSIFICATION);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySystemClassificationIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where systemClassification in DEFAULT_SYSTEM_CLASSIFICATION or UPDATED_SYSTEM_CLASSIFICATION
        defaultLoanAccountShouldBeFound("systemClassification.in=" + DEFAULT_SYSTEM_CLASSIFICATION + "," + UPDATED_SYSTEM_CLASSIFICATION);

        // Get all the loanAccountList where systemClassification equals to UPDATED_SYSTEM_CLASSIFICATION
        defaultLoanAccountShouldNotBeFound("systemClassification.in=" + UPDATED_SYSTEM_CLASSIFICATION);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsBySystemClassificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where systemClassification is not null
        defaultLoanAccountShouldBeFound("systemClassification.specified=true");

        // Get all the loanAccountList where systemClassification is null
        defaultLoanAccountShouldNotBeFound("systemClassification.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByUserClassificationIsEqualToSomething() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where userClassification equals to DEFAULT_USER_CLASSIFICATION
        defaultLoanAccountShouldBeFound("userClassification.equals=" + DEFAULT_USER_CLASSIFICATION);

        // Get all the loanAccountList where userClassification equals to UPDATED_USER_CLASSIFICATION
        defaultLoanAccountShouldNotBeFound("userClassification.equals=" + UPDATED_USER_CLASSIFICATION);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByUserClassificationIsInShouldWork() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where userClassification in DEFAULT_USER_CLASSIFICATION or UPDATED_USER_CLASSIFICATION
        defaultLoanAccountShouldBeFound("userClassification.in=" + DEFAULT_USER_CLASSIFICATION + "," + UPDATED_USER_CLASSIFICATION);

        // Get all the loanAccountList where userClassification equals to UPDATED_USER_CLASSIFICATION
        defaultLoanAccountShouldNotBeFound("userClassification.in=" + UPDATED_USER_CLASSIFICATION);
    }

    @Test
    @Transactional
    public void getAllLoanAccountsByUserClassificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        // Get all the loanAccountList where userClassification is not null
        defaultLoanAccountShouldBeFound("userClassification.specified=true");

        // Get all the loanAccountList where userClassification is null
        defaultLoanAccountShouldNotBeFound("userClassification.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLoanAccountShouldBeFound(String filter) throws Exception {
        restLoanAccountMockMvc.perform(get("/api/loan-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].sbu").value(hasItem(DEFAULT_SBU)))
            .andExpect(jsonPath("$.[*].rmCode").value(hasItem(DEFAULT_RM_CODE)))
            .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
            .andExpect(jsonPath("$.[*].schemeCode").value(hasItem(DEFAULT_SCHEME_CODE)))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].limitAmount").value(hasItem(DEFAULT_LIMIT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].nominalRate").value(hasItem(DEFAULT_NOMINAL_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].interestSuspended").value(hasItem(DEFAULT_INTEREST_SUSPENDED.intValue())))
            .andExpect(jsonPath("$.[*].loanProvision").value(hasItem(DEFAULT_LOAN_PROVISION.intValue())))
            .andExpect(jsonPath("$.[*].economicSector").value(hasItem(DEFAULT_ECONOMIC_SECTOR)))
            .andExpect(jsonPath("$.[*].economicSubSector").value(hasItem(DEFAULT_ECONOMIC_SUB_SECTOR)))
            .andExpect(jsonPath("$.[*].appraisalMonth").value(hasItem(DEFAULT_APPRAISAL_MONTH.toString())))
            .andExpect(jsonPath("$.[*].systemClassification").value(hasItem(DEFAULT_SYSTEM_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].userClassification").value(hasItem(DEFAULT_USER_CLASSIFICATION)));

        // Check, that the count call also returns 1
        restLoanAccountMockMvc.perform(get("/api/loan-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLoanAccountShouldNotBeFound(String filter) throws Exception {
        restLoanAccountMockMvc.perform(get("/api/loan-accounts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoanAccountMockMvc.perform(get("/api/loan-accounts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLoanAccount() throws Exception {
        // Get the loanAccount
        restLoanAccountMockMvc.perform(get("/api/loan-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoanAccount() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        int databaseSizeBeforeUpdate = loanAccountRepository.findAll().size();

        // Update the loanAccount
        LoanAccount updatedLoanAccount = loanAccountRepository.findById(loanAccount.getId()).get();
        // Disconnect from session so that the updates on updatedLoanAccount are not directly saved in db
        em.detach(updatedLoanAccount);
        updatedLoanAccount
            .sbu(UPDATED_SBU)
            .rmCode(UPDATED_RM_CODE)
            .glCode(UPDATED_GL_CODE)
            .schemeCode(UPDATED_SCHEME_CODE)
            .customerCode(UPDATED_CUSTOMER_CODE)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .accountName(UPDATED_ACCOUNT_NAME)
            .currencyCode(UPDATED_CURRENCY_CODE)
            .openingDate(UPDATED_OPENING_DATE)
            .accountBalance(UPDATED_ACCOUNT_BALANCE)
            .limitAmount(UPDATED_LIMIT_AMOUNT)
            .nominalRate(UPDATED_NOMINAL_RATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .interestSuspended(UPDATED_INTEREST_SUSPENDED)
            .loanProvision(UPDATED_LOAN_PROVISION)
            .economicSector(UPDATED_ECONOMIC_SECTOR)
            .economicSubSector(UPDATED_ECONOMIC_SUB_SECTOR)
            .appraisalMonth(UPDATED_APPRAISAL_MONTH)
            .systemClassification(UPDATED_SYSTEM_CLASSIFICATION)
            .userClassification(UPDATED_USER_CLASSIFICATION);
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(updatedLoanAccount);

        restLoanAccountMockMvc.perform(put("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isOk());

        // Validate the LoanAccount in the database
        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeUpdate);
        LoanAccount testLoanAccount = loanAccountList.get(loanAccountList.size() - 1);
        assertThat(testLoanAccount.getSbu()).isEqualTo(UPDATED_SBU);
        assertThat(testLoanAccount.getRmCode()).isEqualTo(UPDATED_RM_CODE);
        assertThat(testLoanAccount.getGlCode()).isEqualTo(UPDATED_GL_CODE);
        assertThat(testLoanAccount.getSchemeCode()).isEqualTo(UPDATED_SCHEME_CODE);
        assertThat(testLoanAccount.getCustomerCode()).isEqualTo(UPDATED_CUSTOMER_CODE);
        assertThat(testLoanAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testLoanAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testLoanAccount.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testLoanAccount.getOpeningDate()).isEqualTo(UPDATED_OPENING_DATE);
        assertThat(testLoanAccount.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
        assertThat(testLoanAccount.getLimitAmount()).isEqualTo(UPDATED_LIMIT_AMOUNT);
        assertThat(testLoanAccount.getNominalRate()).isEqualTo(UPDATED_NOMINAL_RATE);
        assertThat(testLoanAccount.getExpiryDate()).isEqualTo(UPDATED_EXPIRY_DATE);
        assertThat(testLoanAccount.getInterestSuspended()).isEqualTo(UPDATED_INTEREST_SUSPENDED);
        assertThat(testLoanAccount.getLoanProvision()).isEqualTo(UPDATED_LOAN_PROVISION);
        assertThat(testLoanAccount.getEconomicSector()).isEqualTo(UPDATED_ECONOMIC_SECTOR);
        assertThat(testLoanAccount.getEconomicSubSector()).isEqualTo(UPDATED_ECONOMIC_SUB_SECTOR);
        assertThat(testLoanAccount.getAppraisalMonth()).isEqualTo(UPDATED_APPRAISAL_MONTH);
        assertThat(testLoanAccount.getSystemClassification()).isEqualTo(UPDATED_SYSTEM_CLASSIFICATION);
        assertThat(testLoanAccount.getUserClassification()).isEqualTo(UPDATED_USER_CLASSIFICATION);

        // Validate the LoanAccount in Elasticsearch
        verify(mockLoanAccountSearchRepository, times(1)).save(testLoanAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingLoanAccount() throws Exception {
        int databaseSizeBeforeUpdate = loanAccountRepository.findAll().size();

        // Create the LoanAccount
        LoanAccountDTO loanAccountDTO = loanAccountMapper.toDto(loanAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoanAccountMockMvc.perform(put("/api/loan-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loanAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LoanAccount in the database
        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LoanAccount in Elasticsearch
        verify(mockLoanAccountSearchRepository, times(0)).save(loanAccount);
    }

    @Test
    @Transactional
    public void deleteLoanAccount() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);

        int databaseSizeBeforeDelete = loanAccountRepository.findAll().size();

        // Delete the loanAccount
        restLoanAccountMockMvc.perform(delete("/api/loan-accounts/{id}", loanAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LoanAccount> loanAccountList = loanAccountRepository.findAll();
        assertThat(loanAccountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LoanAccount in Elasticsearch
        verify(mockLoanAccountSearchRepository, times(1)).deleteById(loanAccount.getId());
    }

    @Test
    @Transactional
    public void searchLoanAccount() throws Exception {
        // Initialize the database
        loanAccountRepository.saveAndFlush(loanAccount);
        when(mockLoanAccountSearchRepository.search(queryStringQuery("id:" + loanAccount.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(loanAccount), PageRequest.of(0, 1), 1));
        // Search the loanAccount
        restLoanAccountMockMvc.perform(get("/api/_search/loan-accounts?query=id:" + loanAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loanAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].sbu").value(hasItem(DEFAULT_SBU)))
            .andExpect(jsonPath("$.[*].rmCode").value(hasItem(DEFAULT_RM_CODE)))
            .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
            .andExpect(jsonPath("$.[*].schemeCode").value(hasItem(DEFAULT_SCHEME_CODE)))
            .andExpect(jsonPath("$.[*].customerCode").value(hasItem(DEFAULT_CUSTOMER_CODE)))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].openingDate").value(hasItem(DEFAULT_OPENING_DATE.toString())))
            .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].limitAmount").value(hasItem(DEFAULT_LIMIT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].nominalRate").value(hasItem(DEFAULT_NOMINAL_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].interestSuspended").value(hasItem(DEFAULT_INTEREST_SUSPENDED.intValue())))
            .andExpect(jsonPath("$.[*].loanProvision").value(hasItem(DEFAULT_LOAN_PROVISION.intValue())))
            .andExpect(jsonPath("$.[*].economicSector").value(hasItem(DEFAULT_ECONOMIC_SECTOR)))
            .andExpect(jsonPath("$.[*].economicSubSector").value(hasItem(DEFAULT_ECONOMIC_SUB_SECTOR)))
            .andExpect(jsonPath("$.[*].appraisalMonth").value(hasItem(DEFAULT_APPRAISAL_MONTH.toString())))
            .andExpect(jsonPath("$.[*].systemClassification").value(hasItem(DEFAULT_SYSTEM_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].userClassification").value(hasItem(DEFAULT_USER_CLASSIFICATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanAccount.class);
        LoanAccount loanAccount1 = new LoanAccount();
        loanAccount1.setId(1L);
        LoanAccount loanAccount2 = new LoanAccount();
        loanAccount2.setId(loanAccount1.getId());
        assertThat(loanAccount1).isEqualTo(loanAccount2);
        loanAccount2.setId(2L);
        assertThat(loanAccount1).isNotEqualTo(loanAccount2);
        loanAccount1.setId(null);
        assertThat(loanAccount1).isNotEqualTo(loanAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanAccountDTO.class);
        LoanAccountDTO loanAccountDTO1 = new LoanAccountDTO();
        loanAccountDTO1.setId(1L);
        LoanAccountDTO loanAccountDTO2 = new LoanAccountDTO();
        assertThat(loanAccountDTO1).isNotEqualTo(loanAccountDTO2);
        loanAccountDTO2.setId(loanAccountDTO1.getId());
        assertThat(loanAccountDTO1).isEqualTo(loanAccountDTO2);
        loanAccountDTO2.setId(2L);
        assertThat(loanAccountDTO1).isNotEqualTo(loanAccountDTO2);
        loanAccountDTO1.setId(null);
        assertThat(loanAccountDTO1).isNotEqualTo(loanAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(loanAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(loanAccountMapper.fromId(null)).isNull();
    }
}
