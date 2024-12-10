package org.poo.users.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class SplitCardPayment extends Transaction {
    private double amount;
    private double splitAmmount;
    private String currency;
    private ArrayList<String> ibanList;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ArrayList<String> getIbanList() {
        return ibanList;
    }

    public void setIbanList(ArrayList<String> ibanList) {
        this.ibanList = ibanList;
    }

    public double getSplitAmmount() {
        return splitAmmount;
    }

    public void setSplitAmmount(double splitAmmount) {
        this.splitAmmount = splitAmmount;
    }

    public SplitCardPayment(int timestamp, double amount, double splitAmmount,
                            String currency, ArrayList<String> ibanList) {
        // in ref there is one more 0 if the splitAmount is integer.
        super(timestamp, "Split payment of " + splitAmmount + "0 " + currency);
        this.amount = amount;
        this.currency = currency;
        this.ibanList = ibanList;
        this.splitAmmount = splitAmmount;
        if (splitAmmount % 1 != 0)
            this.setDescription("Split payment of " + splitAmmount + " " + currency);
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode txt = mapper.createObjectNode();
        txt.put("timestamp", getTimestamp());
        txt.put("description", getDescription());
        txt.put("amount", getSplitAmmount()/getIbanList().size());
        txt.put("currency", getCurrency());
        txt.putPOJO("involvedAccounts", getIbanList());
        return txt;
    }
}
