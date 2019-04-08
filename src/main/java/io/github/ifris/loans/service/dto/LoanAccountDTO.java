package io.github.ifris.loans.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the LoanAccount entity.
 */
public class LoanAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String sbu;

    @NotNull
    private String rmCode;

    @NotNull
    private String glCode;

    @NotNull
    private String schemeCode;

    @NotNull
    private String customerCode;

    @NotNull
    @Size(min = 10)
    private String accountNumber;

    @NotNull
    private String accountName;

    @NotNull
    @Size(min = 3)
    @Pattern(regexp = "[A-Z]{3}")
    private String currencyCode;

    @NotNull
    private LocalDate openingDate;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal accountBalance;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal limitAmount;

    private Double nominalRate;

    private LocalDate expiryDate;

    @DecimalMin(value = "0")
    private BigDecimal interestSuspended;

    @DecimalMin(value = "0")
    private BigDecimal loanProvision;

    @NotNull
    private String economicSector;

    @NotNull
    private String economicSubSector;

    @NotNull
    private LocalDate appraisalMonth;

    @NotNull
    private String systemClassification;

    @NotNull
    private String userClassification;


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

    public String getRmCode() {
        return rmCode;
    }

    public void setRmCode(String rmCode) {
        this.rmCode = rmCode;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public String getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public Double getNominalRate() {
        return nominalRate;
    }

    public void setNominalRate(Double nominalRate) {
        this.nominalRate = nominalRate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getInterestSuspended() {
        return interestSuspended;
    }

    public void setInterestSuspended(BigDecimal interestSuspended) {
        this.interestSuspended = interestSuspended;
    }

    public BigDecimal getLoanProvision() {
        return loanProvision;
    }

    public void setLoanProvision(BigDecimal loanProvision) {
        this.loanProvision = loanProvision;
    }

    public String getEconomicSector() {
        return economicSector;
    }

    public void setEconomicSector(String economicSector) {
        this.economicSector = economicSector;
    }

    public String getEconomicSubSector() {
        return economicSubSector;
    }

    public void setEconomicSubSector(String economicSubSector) {
        this.economicSubSector = economicSubSector;
    }

    public LocalDate getAppraisalMonth() {
        return appraisalMonth;
    }

    public void setAppraisalMonth(LocalDate appraisalMonth) {
        this.appraisalMonth = appraisalMonth;
    }

    public String getSystemClassification() {
        return systemClassification;
    }

    public void setSystemClassification(String systemClassification) {
        this.systemClassification = systemClassification;
    }

    public String getUserClassification() {
        return userClassification;
    }

    public void setUserClassification(String userClassification) {
        this.userClassification = userClassification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LoanAccountDTO loanAccountDTO = (LoanAccountDTO) o;
        if (loanAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), loanAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LoanAccountDTO{" +
            "id=" + getId() +
            ", sbu='" + getSbu() + "'" +
            ", rmCode='" + getRmCode() + "'" +
            ", glCode='" + getGlCode() + "'" +
            ", schemeCode='" + getSchemeCode() + "'" +
            ", customerCode='" + getCustomerCode() + "'" +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", accountName='" + getAccountName() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", openingDate='" + getOpeningDate() + "'" +
            ", accountBalance=" + getAccountBalance() +
            ", limitAmount=" + getLimitAmount() +
            ", nominalRate=" + getNominalRate() +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", interestSuspended=" + getInterestSuspended() +
            ", loanProvision=" + getLoanProvision() +
            ", economicSector='" + getEconomicSector() + "'" +
            ", economicSubSector='" + getEconomicSubSector() + "'" +
            ", appraisalMonth='" + getAppraisalMonth() + "'" +
            ", systemClassification='" + getSystemClassification() + "'" +
            ", userClassification='" + getUserClassification() + "'" +
            "}";
    }
}
