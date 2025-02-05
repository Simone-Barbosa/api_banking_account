package br.com.bank.account.exception;

public class CreateAccountException extends RuntimeException{
    public CreateAccountException(String message) {
        super(message);
    }
}
