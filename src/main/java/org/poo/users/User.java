package org.poo.users;

import java.util.ArrayList;

import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;
import org.poo.users.transactions.*;
import org.poo.users.transactions.Error;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> account) {
        this.accounts = account;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addAccount (CommandInput input) {
        if (input.getAccountType().equals("savings")){
            SavingsAccount account = new SavingsAccount(input.getCurrency(),
                    input.getAccountType(), input.getInterestRate());
            getAccounts().add(account);
        } else {
            Account account = new Account(input.getCurrency(), input.getAccountType());
            getAccounts().add(account);
        }
        addAccountCreationTransaction(input.getTimestamp());
    }

    public void deleteAccount (int index){
        getAccounts().remove(index);
    }

    public void addErrorTransaction (int timestamp, String description) {
        getTransactions().add(new Error(timestamp, description));
    }

    public void addAccountCreationTransaction (int timestamp) {
        getTransactions().add(new CreateAccount(timestamp));
    }

    public void addCardPaymentTransaction (int timestamp, double amount, String commerciant) {
        getTransactions().add(new CardPayment(timestamp, amount, commerciant));
    }

    public void addPaymentFailedTransaction (int timestamp) {
        getTransactions().add(new PaymentFailed(timestamp));
    }

    public void addMoneyTransferTransaction (CommandInput input, String type, String currency) {
        getTransactions().add(new MoneyTransfer(input.getTimestamp(),input.getDescription(),
                input.getAccount(), input.getReceiver(), input.getAmount(), type, currency));
    }
}
