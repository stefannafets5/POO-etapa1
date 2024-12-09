package org.poo.users;

import org.poo.users.transactions.CreateOrDeleteCard;
import org.poo.users.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class Card {
    private String status;
    private String cardNumber;
    private String type;
    private int timeOfCreation;

    public Card(int timeOfCreation, String type) {
        this.status = "active";
        this.timeOfCreation = timeOfCreation;
        this.cardNumber = Utils.generateCardNumber();
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(int timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addCardCreationTransaction (int timestamp, String email, String iban,
                                            ArrayList<Transaction> transactions, String description) {
        transactions.add(new CreateOrDeleteCard(timestamp, email, getCardNumber(), iban, description));
    }
}
