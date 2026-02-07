package dev.ajay.ticket_blaster.ticketblaster_backend.exception;

public class SimulatedPaymentError extends RuntimeException{
    public SimulatedPaymentError(String message) {
        super(message);
    }

    public SimulatedPaymentError(String message, Throwable cause) {
        super(message, cause);
    }
}
