package contracts;

import company.InsuranceCompany;
import objects.Person;
import objects.Vehicle;
import payment.ContractPaymentData;

import java.util.Objects;

public class SingleVehicleContract extends AbstractVehicleContract {
    private final Vehicle insuredVehicle;

    public SingleVehicleContract(String contractNumber, InsuranceCompany insurer,
                                 Person beneficiary, Person policyHolder, ContractPaymentData contractPaymentData,
                                 int coverageAmount, Vehicle insuredVehicle) {
        super(contractNumber, insurer, beneficiary, policyHolder, contractPaymentData, coverageAmount);
        if (insuredVehicle == null) {
            throw new IllegalArgumentException("Insured vehicle cannot be null");
        }
        this.insuredVehicle = insuredVehicle;
    }

    public Vehicle getInsuredVehicle() {
        return insuredVehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleVehicleContract)) return false;
        SingleVehicleContract that = (SingleVehicleContract) o;
        return Objects.equals(getContractNumber(), that.getContractNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContractNumber());
    }
}
