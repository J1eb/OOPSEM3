// src/objects/Vehicle.java
package objects;

public class Vehicle {
    private final String licensePlate;
    private final int originalValue;

    public Vehicle(String licensePlate, int originalValue) {
        if (licensePlate == null || licensePlate.length() != 7) {
            throw new IllegalArgumentException("License plate must be 7 characters long");
        }
        if (originalValue <= 0) {
            throw new IllegalArgumentException("Original value must be positive");
        }

        this.licensePlate = licensePlate;
        this.originalValue = originalValue;
    }

    public String getLicensePlate() { return licensePlate; }
    public int getOriginalValue() { return originalValue; }
}