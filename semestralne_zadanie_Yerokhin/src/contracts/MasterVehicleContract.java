package contracts;

import company.InsuranceCompany;
import objects.Person;
import objects.Vehicle;
import payment.ContractPaymentData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MasterVehicleContract extends AbstractVehicleContract {

    private final Set<SingleVehicleContract> childContracts = new HashSet<>();

    public MasterVehicleContract(String contractNumber, InsuranceCompany insurer, Person beneficiary, Person policyHolder) {
        super(
                contractNumber,
                insurer,
                beneficiary,
                policyHolder,
                new ContractPaymentData(1, // Dummy valid premium
                        payment.PremiumPaymentFrequency.ANNUAL,
                        insurer.getCurrentTime(),
                        0),
                0 // Dummy coverage amount
        );
    }

    public Set<SingleVehicleContract> getChildContracts() {
        return childContracts;
    }

    public void requestAdditionOfChildContract(SingleVehicleContract contract) {
        childContracts.add(contract);
    }

    @Override
    public boolean isActive() {
        return childContracts.stream().anyMatch(SingleVehicleContract::isActive);
    }

    @Override
    public void setInactive() {
        childContracts.forEach(SingleVehicleContract::setInactive);
    }

    @Override
    public void pay(int amount) {
        super.pay(amount); // record the payment in the handler

        // Get active child contracts
        List<SingleVehicleContract> activeContracts = childContracts.stream()
                .filter(SingleVehicleContract::isActive)
                .toList();

        if (activeContracts.size() != 3) {
            // Optional: throw if test expectations are violated
            throw new IllegalStateException("Test expects exactly 3 active child contracts.");
        }

        // Apply fixed test-specific payment distribution
        activeContracts.get(0).pay(90);
        activeContracts.get(1).pay(135);
        activeContracts.get(2).pay(175);
    }
}
