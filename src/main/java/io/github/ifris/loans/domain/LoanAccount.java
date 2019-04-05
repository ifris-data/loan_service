package io.github.ifris.loans.domain;


import io.github.ifris.loans.domain.enumeration.RiskClass;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * A LoanAccount.
 */
@Entity
@Table(name = "loan_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "loanaccount")
public class LoanAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sbu", nullable = false)
    private String sbu;

    @NotNull
    @Column(name = "rm_code", nullable = false)
    private String rmCode;

    @NotNull
    @Column(name = "gl_code", nullable = false)
    private String glCode;

    @NotNull
    @Column(name = "scheme_code", nullable = false)
    private String schemeCode;

    @NotNull
    @Column(name = "customer_code", nullable = false)
    private String customerCode;

    @NotNull
    @Size(min = 10)
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @NotNull
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @NotNull
    @Size(min = 3)
    @Pattern(regexp = "[A-Z]{3}")
    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @NotNull
    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "account_balance", precision = 10, scale = 2, nullable = false)
    private BigDecimal accountBalance;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "limit_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal limitAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "system_classification", nullable = false)
    private RiskClass systemClassification;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_classification", nullable = false)
    private RiskClass userClassification;

    @Column(name = "nominal_rate")
    private Double nominalRate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @DecimalMin(value = "0")
    @Column(name = "interest_suspended", precision = 10, scale = 2)
    private BigDecimal interestSuspended;

    @DecimalMin(value = "0")
    @Column(name = "loan_provision", precision = 10, scale = 2)
    private BigDecimal loanProvision;

    @NotNull
    @Column(name = "economic_sector", nullable = false)
    private String economicSector;

    @NotNull
    @Column(name = "economic_sub_sector", nullable = false)
    private String economicSubSector;

    @NotNull
    @Column(name = "appraisal_month", nullable = false)
    private LocalDate appraisalMonth;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSbu() {
        return sbu;
    }

    public void setSbu(String sbu) {
        this.sbu = sbu;
    }

    public LoanAccount sbu(String sbu) {
        this.sbu = sbu;
        return this;
    }

    public String getRmCode() {
        return rmCode;
    }

    public void setRmCode(String rmCode) {
        this.rmCode = rmCode;
    }

    public LoanAccount rmCode(String rmCode) {
        this.rmCode = rmCode;
        return this;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public LoanAccount glCode(String glCode) {
        this.glCode = glCode;
        return this;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public LoanAccount schemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
        return this;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public LoanAccount customerCode(String customerCode) {
        this.customerCode = customerCode;
        return this;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LoanAccount accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LoanAccount accountName(String accountName) {
        this.accountName = accountName;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LoanAccount currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public LoanAccount openingDate(LocalDate openingDate) {
        this.openingDate = openingDate;
        return this;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public LoanAccount accountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
        return this;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public LoanAccount limitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
        return this;
    }

    public RiskClass getSystemClassification() {
        return systemClassification;
    }

    public void setSystemClassification(RiskClass systemClassification) {
        this.systemClassification = systemClassification;
    }

    public LoanAccount systemClassification(RiskClass systemClassification) {
        this.systemClassification = systemClassification;
        return this;
    }

    public RiskClass getUserClassification() {
        return userClassification;
    }

    public void setUserClassification(RiskClass userClassification) {
        this.userClassification = userClassification;
    }

    public LoanAccount userClassification(RiskClass userClassification) {
        this.userClassification = userClassification;
        return this;
    }

    public Double getNominalRate() {
        return nominalRate;
    }

    public void setNominalRate(Double nominalRate) {
        this.nominalRate = nominalRate;
    }

    public LoanAccount nominalRate(Double nominalRate) {
        this.nominalRate = nominalRate;
        return this;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LoanAccount expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public BigDecimal getInterestSuspended() {
        return interestSuspended;
    }

    public void setInterestSuspended(BigDecimal interestSuspended) {
        this.interestSuspended = interestSuspended;
    }

    public LoanAccount interestSuspended(BigDecimal interestSuspended) {
        this.interestSuspended = interestSuspended;
        return this;
    }

    public BigDecimal getLoanProvision() {
        return loanProvision;
    }

    public void setLoanProvision(BigDecimal loanProvision) {
        this.loanProvision = loanProvision;
    }

    public LoanAccount loanProvision(BigDecimal loanProvision) {
        this.loanProvision = loanProvision;
        return this;
    }

    public String getEconomicSector() {
        return economicSector;
    }

    public void setEconomicSector(String economicSector) {
        this.economicSector = economicSector;
    }

    public LoanAccount economicSector(String economicSector) {
        this.economicSector = economicSector;
        return this;
    }

    public String getEconomicSubSector() {
        return economicSubSector;
    }

    public void setEconomicSubSector(String economicSubSector) {
        this.economicSubSector = economicSubSector;
    }

    public LoanAccount economicSubSector(String economicSubSector) {
        this.economicSubSector = economicSubSector;
        return this;
    }

    public LocalDate getAppraisalMonth() {
        return appraisalMonth;
    }

    public void setAppraisalMonth(LocalDate appraisalMonth) {
        this.appraisalMonth = appraisalMonth;
    }

    public LoanAccount appraisalMonth(LocalDate appraisalMonth) {
        this.appraisalMonth = appraisalMonth;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoanAccount loanAccount = (LoanAccount) o;
        if (loanAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), loanAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LoanAccount{" + "id=" + getId() + ", sbu='" + getSbu() + "'" + ", rmCode='" + getRmCode() + "'" + ", glCode='" + getGlCode() + "'" + ", schemeCode='" + getSchemeCode() + "'" +
            ", customerCode='" + getCustomerCode() + "'" + ", accountNumber='" + getAccountNumber() + "'" + ", accountName='" + getAccountName() + "'" + ", currencyCode='" + getCurrencyCode() + "'" +
            ", openingDate='" + getOpeningDate() + "'" + ", accountBalance=" + getAccountBalance() + ", limitAmount=" + getLimitAmount() + ", systemClassification='" + getSystemClassification() +
            "'" + ", userClassification='" + getUserClassification() + "'" + ", nominalRate=" + getNominalRate() + ", expiryDate='" + getExpiryDate() + "'" + ", interestSuspended=" +
            getInterestSuspended() + ", loanProvision=" + getLoanProvision() + ", economicSector='" + getEconomicSector() + "'" + ", economicSubSector='" + getEconomicSubSector() + "'" +
            ", appraisalMonth='" + getAppraisalMonth() + "'" + "}";
    }
}
