package payment;

import company.InsuranceCompany;
import contracts.AbstractContract;
import contracts.MasterVehicleContract;
import contracts.InvalidContractException;
import contracts.SingleVehicleContract;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class PaymentHandler {
    private final InsuranceCompany insurer;
    private final Map<AbstractContract, Set<PaymentInstance>> paymentHistory; // Declare the field

    public PaymentHandler(InsuranceCompany insurer) {
        if (insurer == null) {
            throw new IllegalArgumentException("Insurer cannot be null");
        }
        this.insurer = insurer;
        this.paymentHistory = new HashMap<>(); // Initialize the map
    }

    public Map<AbstractContract, Set<PaymentInstance>> getPaymentHistory() {
        return paymentHistory;
    }

    public void pay(AbstractContract contract, int amount) {
        if (contract == null || amount <= 0) {
            throw new IllegalArgumentException("Contract cannot be null and amount must be positive");
        }
        if (!contract.isActive() || contract.getInsurer() != insurer) {
            throw new InvalidContractException();
        }

        ContractPaymentData paymentData = contract.getContractPaymentData();
        paymentData.setOutstandingBalance(paymentData.getOutstandingBalance() - amount);

        PaymentInstance payment = new PaymentInstance(insurer.getCurrentTime(), amount);
        paymentHistory.computeIfAbsent(contract, k -> new LinkedHashSet<>()).add(payment);
    }

    public void pay(MasterVehicleContract contract, int amount) {
        if (contract == null || amount <= 0) {
            throw new IllegalArgumentException("Contract cannot be null and amount must be positive");
        }
        if (!contract.isActive() || contract.getInsurer() != insurer || contract.getChildContracts().isEmpty()) {
            throw new InvalidContractException();
        }

        // First pass: pay outstanding balances
        for (SingleVehicleContract child : contract.getChildContracts()) {
            if (child.isActive() && amount > 0) {
                int outstanding = child.getContractPaymentData().getOutstandingBalance();
                if (outstanding > 0) {
                    int payment = Math.min(outstanding, amount);
                    child.getContractPaymentData().setOutstandingBalance(outstanding - payment);
                    amount -= payment;
                }
            }
        }

        // Subsequent passes: create overpayments
        while (amount > 0) {
            boolean anyPaymentMade = false;
            for (SingleVehicleContract child : contract.getChildContracts()) {
                if (child.isActive() && amount > 0) {
                    int premium = child.getContractPaymentData().getPremium();
                    int payment = Math.min(premium, amount);
                    child.getContractPaymentData().setOutstandingBalance(
                            child.getContractPaymentData().getOutstandingBalance() - payment
                    );
                    amount -= payment;
                    anyPaymentMade = true;
                }
            }
            if (!anyPaymentMade) break;
        }

        PaymentInstance payment = new PaymentInstance(insurer.getCurrentTime(), amount);
        paymentHistory.computeIfAbsent(contract, k -> new LinkedHashSet<>()).add(payment);
    }
}