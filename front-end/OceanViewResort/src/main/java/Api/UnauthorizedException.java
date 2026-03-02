/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Api;

/**
 * UnauthorizedException is a custom runtime exception used to indicate
 * that a user is not authorized to perform a specific action.
 * 
 * This exception can be thrown when:
 * - A user tries to access a resource without proper authentication
 * - A user lacks the required role/permissions
 * 
 * Extending RuntimeException means it is unchecked, so it does not need
 * to be explicitly declared in method signatures.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructor that accepts an error message describing why the exception occurred.
     *
     * @param message Description of the unauthorized access
     */
    public UnauthorizedException(String message) {
        super(message); // Passes the message to the RuntimeException constructor
    }
}