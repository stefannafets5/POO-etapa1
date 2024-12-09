package org.poo.users;

import java.util.ArrayList;

import org.poo.fileio.CommandInput;
import org.poo.users.transactions.CreateOrDeleteCard;
import org.poo.users.transactions.Transaction;
import org.poo.utils.Utils;

public class Account {
    private String currency;
    private String type;
    private String iban;
    private String alias;
    private double balance;
    private double minBalance;
    private ArrayList<Card> cards = new ArrayList<>();

    public Account(String currency, String type) {
        this.currency = currency;
        this.type = type;
        this.balance = 0;
        this.iban = Utils.generateIBAN();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public void addMoney (double amount) {
        this.balance += amount;
    }

    public void subtractMoney (double amount) {
        this.balance -= amount;
    }

    public void addCard (CommandInput input, String type,
                         ArrayList<Transaction> transactions, String description){
        Card card = new Card(input.getTimestamp(), type);

        card.addCardCreationTransaction(input.getTimestamp(),
                input.getEmail(), getIban(), transactions, description);
        getCards().add(card);

    }

    public void removeCard (int index, CommandInput input, String email,
                            ArrayList<Transaction> transactions, String description){
        getCards().get(index).addCardCreationTransaction(input.getTimestamp(),
                email, getIban(), transactions, description);
        getCards().remove(index);
    }
}
