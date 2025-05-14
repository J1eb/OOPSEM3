// src/company/InsuranceCompany.java
package company;

import contracts.*;
import objects.Person;
import objects.Vehicle;
import payment.ContractPaymentData;
import payment.PaymentHandler;
import payment.PremiumPaymentFrequency;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class InsuranceCompany {
    private final Set<AbstractContract> contracts = new LinkedHashSet<>();
    private final PaymentHandler handler;
    private LocalDateTime currentTime;

    public InsuranceCompany(LocalDateTime currentTime) {
        if (currentTime == null) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        this.currentTime = currentTime;
        this.handler = new PaymentHandler(this);
    }

    public Set<AbstractContract> getContracts() {
        return contracts;
    }

    public PaymentHandler getHandler() {
        return handler;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        if (currentTime == null) {
            throw new IllegalArgumentException("Current time cannot be null");
        }
        this.currentTime = currentTime;
    }

    public SingleVehicleContract insureVehicle(String contractNumber, Person beneficiary,
                                               Person policyHolder, int premium, PremiumPaymentFrequency frequency, Vehicle vehicle) {
        if (contractNumber == null || contractNumber.isEmpty()) {
            throw new IllegalArgumentException("Contract number cannot be null or empty");
        }
        if (policyHolder == null) {
            throw new IllegalArgumentException("Policy holder cannot be null");
        }
        if (frequency == null) {
            throw new IllegalArgumentException("Frequency cannot be null");
        }
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (premium <= 0) {
            throw new IllegalArgumentException("Premium must be positive");
        }

        // Check if contract number is unique
        if (contracts.stream().anyMatch(c -> c.getContractNumber().equals(contractNumber))) {
            throw new IllegalArgumentException("Contract number must be unique");
        }

        // Check if annual payment is at least 2% of vehicle value
        int annualPayment = premium * (12 / frequency.getValueInMonths());
        if (annualPayment < vehicle.getOriginalValue() * 0.02) {
            throw new IllegalArgumentException("Annual payment must be at least 2% of vehicle value");
        }

        ContractPaymentData paymentData = new ContractPaymentData(
                premium,
                frequency,
                currentTime,
                0
        );

        int coverageAmount = vehicle.getOriginalValue() / 2;
        SingleVehicleContract contract = new SingleVehicleContract(
                contractNumber,
                this,
                beneficiary,
                policyHolder,
                paymentData,
                coverageAmount,
                vehicle
        );

        contracts.add(contract);
        policyHolder.getContracts().add(contract);
        chargePremiumOnContract(contract);

        return contract;
    }

    public void chargePremiumOnContract(AbstractContract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Contract cannot be null");
        }

        ContractPaymentData paymentData = contract.getContractPaymentData();
        while (!currentTime.isBefore(paymentData.getNextPaymentTime())) {
            paymentData.setOutstandingBalance(
                    paymentData.getOutstandingBalance() + paymentData.getPremium()
            );
            paymentData.updateNextPaymentTime();
        }
    }

    public void chargePremiumOnContracts() {
        for (AbstractContract contract : contracts) {
            if (contract.isActive()) {
                chargePremiumOnContract(contract);
            }
        }
    }
}