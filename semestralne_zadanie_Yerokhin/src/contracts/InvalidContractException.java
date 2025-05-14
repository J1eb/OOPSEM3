// src/contracts/InvalidContractException.java
package contracts;

public class InvalidContractException extends RuntimeException {
    public InvalidContractException() {
        super();
    }

    public InvalidContractException(String message) {
        super(message);
    }
}