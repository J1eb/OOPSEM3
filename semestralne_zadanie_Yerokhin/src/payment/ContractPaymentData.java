// src/payment/ContractPaymentData.java
package payment;

import java.time.LocalDateTime;

public class ContractPaymentData {
    private int premium;
    private PremiumPaymentFrequency premiumPaymentFrequency;
    private LocalDateTime nextPaymentTime;
    private int outstandingBalance;

    public ContractPaymentData(int premium, PremiumPaymentFrequency premiumPaymentFrequency,
                               LocalDateTime nextPaymentTime, int outstandingBalance) {
        setPremium(premium);
        setPremiumPaymentFrequency(premiumPaymentFrequency);
        if (nextPaymentTime == null) {
            throw new IllegalArgumentException("Next payment time cannot be null");
        }
        this.nextPaymentTime = nextPaymentTime;
        this.outstandingBalance = outstandingBalance;
    }

    // Getters and setters
    public int getPremium() { return premium; }
    public PremiumPaymentFrequency getPremiumPaymentFrequency() { return premiumPaymentFrequency; }
    public LocalDateTime getNextPaymentTime() { return nextPaymentTime; }
    public int getOutstandingBalance() { return outstandingBalance; }

    public void setPremium(int premium) {
        if (premium <= 0) {
            throw new IllegalArgumentException("Premium must be positive");
        }
        this.premium = premium;
    }

    public void setPremiumPaymentFrequency(PremiumPaymentFrequency premiumPaymentFrequency) {
        if (premiumPaymentFrequency == null) {
            throw new IllegalArgumentException("Premium payment frequency cannot be null");
        }
        this.premiumPaymentFrequency = premiumPaymentFrequency;
    }

    public void setOutstandingBalance(int outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public void updateNextPaymentTime() {
        this.nextPaymentTime = nextPaymentTime.plusMonths(premiumPaymentFrequency.getValueInMonths());
    }
}