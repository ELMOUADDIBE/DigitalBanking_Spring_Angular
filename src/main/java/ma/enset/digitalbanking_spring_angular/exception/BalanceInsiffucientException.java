package ma.enset.digitalbanking_spring_angular.exception;

public class BalanceInsiffucientException extends Exception {
    public BalanceInsiffucientException(String insufficientBalance) {
        super(insufficientBalance);
    }
}
