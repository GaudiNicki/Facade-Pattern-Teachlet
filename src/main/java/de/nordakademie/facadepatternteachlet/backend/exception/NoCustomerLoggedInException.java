package de.nordakademie.facadepatternteachlet.backend.exception;

/**
 * The exception <b>NoCustomerAuthenticatedException</b> is thrown, if no customer is logged in but a customer
 * specific action is to be done.
 *
 * @author Til Zöller
 */
public class NoCustomerLoggedInException extends Exception {}
