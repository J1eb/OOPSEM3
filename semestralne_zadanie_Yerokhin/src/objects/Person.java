// src/objects/Person.java
package objects;

import contracts.AbstractContract;
import java.util.LinkedHashSet;
import java.util.Set;

public class Person {
    private final String id;
    private final LegalForm legalForm;
    private int paidOutAmount;
    private final Set<AbstractContract> contracts = new LinkedHashSet<>();

    public Person(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        this.id = id;
        this.legalForm = id.length() == 8 ? LegalForm.LEGAL : LegalForm.NATURAL;
        this.paidOutAmount = 0;
    }

    public String getId() { return id; }
    public LegalForm getLegalForm() { return legalForm; }
    public int getPaidOutAmount() { return paidOutAmount; }
    public Set<AbstractContract> getContracts() { return contracts; }

    public void payout(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payout amount must be positive");
        }
        this.paidOutAmount += amount;
    }

    public void addContract(AbstractContract contract) {
        if (contract == null) {
            throw new IllegalArgumentException("Contract cannot be null");
        }
        contracts.add(contract);
    }
}