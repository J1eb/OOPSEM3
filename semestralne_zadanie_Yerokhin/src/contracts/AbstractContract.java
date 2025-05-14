// src/contracts/AbstractContract.java
package contracts;

import company.InsuranceCompany;
import objects.Person;
import payment.ContractPaymentData;

public abstract class AbstractContract {
    private final String contractNumber;
    protected final InsuranceCompany insurer;
    protected final Person policyHolder;
    protected final ContractPaymentData contractPaymentData;
    protected int coverageAmount;
    protected boolean isActive;

    public AbstractContract(String contractNumber, InsuranceCompany insurer,
                            Person policyHolder, ContractPaymentData contractPaymentData, int coverageAmount) {
        if (contractNumber == null || contractNumber.isEmpty()) {
            throw new IllegalArgumentException("Contract number cannot be null or empty");
        }
        if (insurer == null) {
            throw new IllegalArgumentException("Insurer cannot be null");
        }
        if (policyHolder == null) {
            throw new IllegalArgumentException("Policy holder cannot be null");
        }
        if (contractPaymentData == null) {
            throw new IllegalArgumentException("Contract payment data cannot be null");
        }
        if (coverageAmount < 0) {
            throw new IllegalArgumentException("Coverage amount cannot be negative");
        }

        this.contractNumber = contractNumber;
        this.insurer = insurer;
        this.policyHolder = policyHolder;
        this.contractPaymentData = contractPaymentData;
        this.coverageAmount = coverageAmount;
        this.isActive = true;
    }

    // Getters and setters
    public String getContractNumber() { return contractNumber; }
    public InsuranceCompany getInsurer() { return insurer; }
    public Person getPolicyHolder() { return policyHolder; }
    public ContractPaymentData getContractPaymentData() { return contractPaymentData; }
    public int getCoverageAmount() { return coverageAmount; }
    public boolean isActive() { return isActive; }

    public void setCoverageAmount(int coverageAmount) {
        if (coverageAmount < 0) {
            throw new IllegalArgumentException("Coverage amount cannot be negative");
        }
        this.coverageAmount = coverageAmount;
    }

    public void setInactive() {
        this.isActive = false;
    }

    public void pay(int amount) {
        insurer.getHandler().pay(this, amount);
    }

    public void updateBalance() {
        insurer.chargePremiumOnContract(this);
    }
}