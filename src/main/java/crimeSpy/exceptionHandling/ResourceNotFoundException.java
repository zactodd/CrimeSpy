

package crimeSpy.exceptionHandling;

/**
 * Exception: Could not find the resource
 * <p>Exception will typically be caused by attempting to access or open a file that does
 * not exist or otherwise is not in the path specified</p>
 */
public class ResourceNotFoundException extends Exception {

        // Empty Constructor
        public ResourceNotFoundException() {
            super("The resource url could not be located");
        }

        // Constructor that accepts a message
        public ResourceNotFoundException(String message) {
            super(message);
        }
}
