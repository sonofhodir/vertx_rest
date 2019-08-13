package io.vertx.rest.first;

public class Account {


    private String email;
    private Double balance;

    public Account(String email, double balance){
        this.email = email;
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void transfer(Double amount) {
        synchronized (this) {
            this.balance += amount;
        }

    }
}
