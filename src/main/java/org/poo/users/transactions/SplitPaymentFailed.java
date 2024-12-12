package org.poo.users.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

public class SplitPaymentFailed extends Transaction {
    private double amount;
    private String currency;
    private String poor;
    private ArrayList<String> ibanList;


    public SplitPaymentFailed(CommandInput input, String poor, String fromIban) {
        super(input.getTimestamp(), "Split payment of " +
                input.getAmount() + "0 " + input.getCurrency());
        this.amount = input.getAmount()/input.getAccounts().size();
        this.currency = input.getCurrency();
        this.poor= poor;
        this.ibanList = (ArrayList<String>) input.getAccounts();
        setFromIban(fromIban);
    }

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

    public String getPoor() {
        return poor;
    }

    public void setPoor(String poor) {
        this.poor = poor;
    }

    public ArrayList<String> getIbanList() {
        return ibanList;
    }

    public void setIbanList(ArrayList<String> ibanList) {
        this.ibanList = ibanList;
    }

    @Override
    public ObjectNode toJson(ObjectMapper mapper) {
        ObjectNode txt = mapper.createObjectNode();
        txt.put("timestamp", getTimestamp());
        txt.put("description", getDescription());
        txt.put("amount", getAmount());
        txt.put("currency", getCurrency());
        txt.put("error", "Account " + poor +
                " has insufficient funds for a split payment.");
        ArrayNode involvedAccounts = mapper.createArrayNode();
        for (String iban : ibanList)
            involvedAccounts.add(iban);
        txt.set("involvedAccounts", involvedAccounts);
        return txt;
    }
}
