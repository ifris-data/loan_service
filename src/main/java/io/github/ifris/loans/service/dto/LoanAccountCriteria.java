package io.github.ifris.loans.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.ifris.loans.domain.enumeration.RiskClass;
import io.github.ifris.loans.domain.enumeration.RiskClass;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the LoanAccount entity. This class is used in LoanAccountResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /loan-accounts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LoanAccountCriteria implements Serializable {
    /**
     * Class for filtering RiskClass
     */
    public static class RiskClassFilter extends Filter<RiskClass> {
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sbu;

    private StringFilter rmCode;

    private StringFilter glCode;

    private StringFilter schemeCode;

    private StringFilter customerCode;

    private StringFilter accountNumber;

    private StringFilter accountName;

    private StringFilter currencyCode;

    private LocalDateFilter openingDate;

    private BigDecimalFilter accountBalance;

    private BigDecimalFilter limitAmount;

    private RiskClassFilter systemClassification;

    private RiskClassFilter userClassification;

    private DoubleFilter nominalRate;

    private LocalDateFilter expiryDate;

    private BigDecimalFilter interestSuspended;

    private BigDecimalFilter loanProvision;

    private StringFilter economicSector;

    private StringFilter economicSubSector;

    private LocalDateFilter appraisalMonth;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSbu() {
        return sbu;
    }

    public void setSbu(StringFilter sbu) {
        this.sbu = sbu;
    }

    public StringFilter getRmCode() {
        return rmCode;
    }

    public void setRmCode(StringFilter rmCode) {
        this.rmCode = rmCode;
    }

    public StringFilter getGlCode() {
        return glCode;
    }

    public void setGlCode(StringFilter glCode) {
        this.glCode = glCode;
    }

    public StringFilter getSchemeCode() {
        return schemeCode;
    }

    public void setSchemeCode(StringFilter schemeCode) {
        this.schemeCode = schemeCode;
    }

    public StringFilter getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(StringFilter customerCode) {
        this.customerCode = customerCode;
    }

    public StringFilter getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(StringFilter accountNumber) {
        this.accountNumber = accountNumber;
    }

    public StringFilter getAccountName() {
        return accountName;
    }

    public void setAccountName(StringFilter accountName) {
        this.accountName = accountName;
    }

    public StringFilter getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(StringFilter currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDateFilter getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDateFilter openingDate) {
        this.openingDate = openingDate;
    }

    public BigDecimalFilter getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimalFilter accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimalFilter getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimalFilter limitAmount) {
        this.limitAmount = limitAmount;
    }

    public RiskClassFilter getSystemClassification() {
        return systemClassification;
    }

    public void setSystemClassification(RiskClassFilter systemClassification) {
        this.systemClassification = systemClassification;
    }

    public RiskClassFilter getUserClassification() {
        return userClassification;
    }

    public void setUserClassification(RiskClassFilter userClassification) {
        this.userClassification = userClassification;
    }

    public DoubleFilter getNominalRate() {
        return nominalRate;
    }

    public void setNominalRate(DoubleFilter nominalRate) {
        this.nominalRate = nominalRate;
    }

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimalFilter getInterestSuspended() {
        return interestSuspended;
    }

    public void setInterestSuspended(BigDecimalFilter interestSuspended) {
        this.interestSuspended = interestSuspended;
    }

    public BigDecimalFilter getLoanProvision() {
        return loanProvision;
    }

    public void setLoanProvision(BigDecimalFilter loanProvision) {
        this.loanProvision = loanProvision;
    }

    public StringFilter getEconomicSector() {
        return economicSector;
    }

    public void setEconomicSector(StringFilter economicSector) {
        this.economicSector = economicSector;
    }

    public StringFilter getEconomicSubSector() {
        return economicSubSector;
    }

    public void setEconomicSubSector(StringFilter economicSubSector) {
        this.economicSubSector = economicSubSector;
    }

    public LocalDateFilter getAppraisalMonth() {
        return appraisalMonth;
    }

    public void setAppraisalMonth(LocalDateFilter appraisalMonth) {
        this.appraisalMonth = appraisalMonth;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LoanAccountCriteria that = (LoanAccountCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sbu, that.sbu) &&
            Objects.equals(rmCode, that.rmCode) &&
            Objects.equals(glCode, that.glCode) &&
            Objects.equals(schemeCode, that.schemeCode) &&
            Objects.equals(customerCode, that.customerCode) &&
            Objects.equals(accountNumber, that.accountNumber) &&
            Objects.equals(accountName, that.accountName) &&
            Objects.equals(currencyCode, that.currencyCode) &&
            Objects.equals(openingDate, that.openingDate) &&
            Objects.equals(accountBalance, that.accountBalance) &&
            Objects.equals(limitAmount, that.limitAmount) &&
            Objects.equals(systemClassification, that.systemClassification) &&
            Objects.equals(userClassification, that.userClassification) &&
            Objects.equals(nominalRate, that.nominalRate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(interestSuspended, that.interestSuspended) &&
            Objects.equals(loanProvision, that.loanProvision) &&
            Objects.equals(economicSector, that.economicSector) &&
            Objects.equals(economicSubSector, that.economicSubSector) &&
            Objects.equals(appraisalMonth, that.appraisalMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sbu,
        rmCode,
        glCode,
        schemeCode,
        customerCode,
        accountNumber,
        accountName,
        currencyCode,
        openingDate,
        accountBalance,
        limitAmount,
        systemClassification,
        userClassification,
        nominalRate,
        expiryDate,
        interestSuspended,
        loanProvision,
        economicSector,
        economicSubSector,
        appraisalMonth
        );
    }

    @Override
    public String toString() {
        return "LoanAccountCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sbu != null ? "sbu=" + sbu + ", " : "") +
                (rmCode != null ? "rmCode=" + rmCode + ", " : "") +
                (glCode != null ? "glCode=" + glCode + ", " : "") +
                (schemeCode != null ? "schemeCode=" + schemeCode + ", " : "") +
                (customerCode != null ? "customerCode=" + customerCode + ", " : "") +
                (accountNumber != null ? "accountNumber=" + accountNumber + ", " : "") +
                (accountName != null ? "accountName=" + accountName + ", " : "") +
                (currencyCode != null ? "currencyCode=" + currencyCode + ", " : "") +
                (openingDate != null ? "openingDate=" + openingDate + ", " : "") +
                (accountBalance != null ? "accountBalance=" + accountBalance + ", " : "") +
                (limitAmount != null ? "limitAmount=" + limitAmount + ", " : "") +
                (systemClassification != null ? "systemClassification=" + systemClassification + ", " : "") +
                (userClassification != null ? "userClassification=" + userClassification + ", " : "") +
                (nominalRate != null ? "nominalRate=" + nominalRate + ", " : "") +
                (expiryDate != null ? "expiryDate=" + expiryDate + ", " : "") +
                (interestSuspended != null ? "interestSuspended=" + interestSuspended + ", " : "") +
                (loanProvision != null ? "loanProvision=" + loanProvision + ", " : "") +
                (economicSector != null ? "economicSector=" + economicSector + ", " : "") +
                (economicSubSector != null ? "economicSubSector=" + economicSubSector + ", " : "") +
                (appraisalMonth != null ? "appraisalMonth=" + appraisalMonth + ", " : "") +
            "}";
    }

}
