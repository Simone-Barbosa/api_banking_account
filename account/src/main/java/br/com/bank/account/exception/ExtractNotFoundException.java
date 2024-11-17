package br.com.bank.account.exception;

public class ExtractNotFoundException extends RuntimeException {
    public ExtractNotFoundException(String message) {
        super(message);
    }
}
