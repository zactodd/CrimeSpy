package crimeSpy.exceptionHandling;

/**
 * Exception: unique crimeID number violated
 * <p>Exception will typically be caused by attempting to add a non unique
 * Crime to a crime collection.</p>
 */
public class NonUniqueCrimeIDException extends Exception{

        // Empty Constructor
        public NonUniqueCrimeIDException() {
            super("A non unique crimeID was identified");
        }

        // Constructor that accepts a message
        public NonUniqueCrimeIDException(String message) {
            super(message);
        }
}
