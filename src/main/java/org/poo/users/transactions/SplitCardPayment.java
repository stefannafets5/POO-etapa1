package org.poo.users.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
 * The type Split card payment.
 */
public final class SplitCardPayment extends Transaction {
    private double amount;
    private double splitAmmount;
    private String currency;
    private ArrayList<String> ibanList;

    /**
     * Instantiates a new Split card payment.
     *
     * @param timestamp    the timestamp
     * @param amount       the amount
     * @param splitAmmount the split ammount
     * @param currency     the currency
     * @param ibanList     the iban list
     */
    public SplitCardPayment(final int timestamp, final double amount, final double splitAmmount,
                            final String currency, final ArrayList<String> ibanList) {
        super(timestamp, "Split payment of " + splitAmmount + "0 " + currency);
        this.amount = amount;
        this.currency = currency;
        this.ibanList = ibanList;
        this.splitAmmount = splitAmmount;
        if (splitAmmount % 1 != 0) {
            this.setDescription("Split payment of " + splitAmmount + " " + currency);
        }
    }

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets amount.
     *
     * @param amount the amount
     */
    public void setAmount(final double amount) {
        this.amount = amount;
    }

    /**
     * Gets currency.
     *
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets currency.
     *
     * @param currency the currency
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     * Gets iban list.
     *
     * @return the iban list
     */
    public ArrayList<String> getIbanList() {
        return ibanList;
    }

    /**
     * Sets iban list.
     *
     * @param ibanList the iban list
     */
    public void setIbanList(final ArrayList<String> ibanList) {
        this.ibanList = ibanList;
    }

    /**
     * Gets split ammount.
     *
     * @return the split ammount
     */
    public double getSplitAmmount() {
        return splitAmmount;
    }

    /**
     * Sets split ammount.
     *
     * @param splitAmmount the split ammount
     */
    public void setSplitAmmount(final double splitAmmount) {
        this.splitAmmount = splitAmmount;
    }

    @Override
    public ObjectNode toJson(final ObjectMapper mapper) {
        ObjectNode txt = mapper.createObjectNode();
        txt.put("timestamp", getTimestamp());
        txt.put("description", getDescription());
        txt.put("amount", getSplitAmmount() / getIbanList().size());
        txt.put("currency", getCurrency());
        txt.putPOJO("involvedAccounts", getIbanList());
        return txt;
    }
}
