package de.nordakademie.facadepatternteachlet.backend.exception;

/**
 * The exception <b>CustomerAlreadyLoggedInException</b> is thrown, if a customer attempt to log in, but is already
 * authenticated towards the application or his bank account is already registered.
 * This should only occur when a customer manually navigate to the LoginView and try to log in again.
 *
 * @author Niklas Witzel
 */
public class CustomerAlreadyLoggedInException extends Exception {}
