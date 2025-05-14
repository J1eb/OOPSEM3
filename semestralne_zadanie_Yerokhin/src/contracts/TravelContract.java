package contracts;

import company.InsuranceCompany;
import objects.Person;
import payment.ContractPaymentData;

import java.util.Set;

public class TravelContract extends AbstractContract {
    private final Set<Person> insuredPersons;

    public TravelContract(String contractNumber, InsuranceCompany insurer,
                          Person policyHolder, ContractPaymentData contractPaymentData,
                          int coverageAmount, Set<Person> personsToInsure) {
        super(contractNumber, insurer, policyHolder, contractPaymentData, coverageAmount);

        if (personsToInsure == null || personsToInsure.isEmpty()) {
            throw new IllegalArgumentException("Persons to insure cannot be null or empty");
        }
        this.insuredPersons = Set.copyOf(personsToInsure); // Defensive copy
    }

    public Set<Person> getInsuredPersons() {
        return Set.copyOf(insuredPersons); // Return defensive copy
    }
}