package org.poo.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.users.User;
import org.poo.users.Account;
import org.poo.users.Card;
import org.poo.users.transactions.CardPayment;
import org.poo.users.transactions.SplitPaymentFailed;
import org.poo.users.transactions.Transaction;

import java.util.ArrayList;
import java.util.Comparator;

public class ConverterJson {
    private final ArrayNode out;

    public ConverterJson(final ArrayNode output) {
        this.out = output;
    }

    public void printUsers(Bank bank, int timestamp){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode txt = mapper.createObjectNode();

        txt.put("command", "printUsers");

        ArrayNode userList = mapper.createArrayNode();
        for (User user : bank.getUsers()) { // output
            ObjectNode txt2 = mapper.createObjectNode();
            txt2.put("firstName", user.getFirstName());
            txt2.put("lastName", user.getLastName());
            txt2.put("email", user.getEmail());

            ArrayNode accountList = mapper.createArrayNode();
            if (user.getAccounts() != null) {
                for (Account account : user.getAccounts()) {
                    ObjectNode txt3 = mapper.createObjectNode();
                    txt3.put("IBAN", account.getIban());
                    txt3.put("balance", account.getBalance());
                    txt3.put("currency", account.getCurrency());
                    txt3.put("type", account.getType());

                    ArrayNode cardList = mapper.createArrayNode();
                    if (account.getCards() != null) {
                        for (Card card : account.getCards()) {
                            ObjectNode txt4 = mapper.createObjectNode();
                            txt4.put("cardNumber", card.getCardNumber());
                            txt4.put("status", card.getStatus());

                            cardList.add(txt4);
                        }
                    }
                    txt3.set("cards", cardList);
                    accountList.add(txt3);
                }
            }
            txt2.set("accounts", accountList);
            userList.add(txt2);
        }
        txt.set("output", userList);
        txt.put("timestamp",timestamp);
        out.add(txt);
    }

    public void deleteAccount(int timestamp){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode txt = mapper.createObjectNode();

        txt.put("command", "deleteAccount");

        ObjectNode txt2 = mapper.createObjectNode();
        txt2.put("success", "Account deleted");
        txt2.put("timestamp", timestamp);

        txt.set("output", txt2);
        txt.put("timestamp", timestamp);
        out.add(txt);
    }

    public void deleteAccountFail(int timestamp){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode txt = mapper.createObjectNode();

        ObjectNode txt2 = mapper.createObjectNode();
        txt2.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
        txt2.put("timestamp", timestamp);

        txt.set("output", txt2);
        txt.put("timestamp", timestamp);
        txt.put("command", "deleteAccount");
        out.add(txt);
    }

    public void printError(int timestamp, String command, String error){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode txt = mapper.createObjectNode();

        txt.put("command", command);

        ObjectNode txt2 = mapper.createObjectNode();
        txt2.put("timestamp", timestamp);
        txt2.put("description", error);

        txt.set("output", txt2);
        txt.put("timestamp", timestamp);
        out.add(txt);
    }

    public void printTransactions(ArrayList<Transaction> transactions, int timestamp){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode txt = mapper.createObjectNode();

        txt.put("command", "printTransactions");

        ArrayNode transactionList = mapper.createArrayNode();
        for (Transaction transaction : transactions) {
            transactionList.add(transaction.toJson(mapper));
        }

        txt.set("output", transactionList);
        txt.put("timestamp", timestamp);
        out.add(txt);
    }

    public void createReport(ArrayList<Transaction> transactions,
                             CommandInput input, Account account, String type){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode txt = mapper.createObjectNode();

        if (type.equals("normal"))
            txt.put("command", "report");
        else txt.put("command", "spendingsReport");

        ObjectNode txt2 = mapper.createObjectNode();
        txt2.put("IBAN", account.getIban());
        txt2.put("balance", account.getBalance());
        txt2.put("currency", account.getCurrency());
        ArrayNode transactionList = mapper.createArrayNode();
        for (Transaction transaction : transactions)
            if (transaction.getTimestamp() >= input.getStartTimestamp()
                    && transaction.getTimestamp() <= input.getEndTimestamp() ) {
                if (type.equals("normal")) {
                    transactionList.add(transaction.toJson(mapper));
                } else if (transaction.getDescription().equals("Card payment")) {
                    CardPayment pay = (CardPayment) transaction;
                    if (pay.getIban().equals(account.getIban()))
                        transactionList.add(transaction.toJson(mapper));
                }
            }
        txt2.set("transactions", transactionList);

        if (type.equals("spendings")){
            ArrayList<CardPayment> cardPayments = new ArrayList<>();
            for (Transaction transaction : transactions)
                if (transaction.getTimestamp() >= input.getStartTimestamp() &&
                        transaction.getTimestamp() <= input.getEndTimestamp() &&
                        transaction.getDescription().equals("Card payment")) {
                    cardPayments.add((CardPayment) transaction);
                }
            cardPayments.sort(Comparator.comparing(CardPayment::getCommerciant));

            ArrayNode commerciantsList = mapper.createArrayNode();
            for (CardPayment pay : cardPayments) {
                if (pay.getTimestamp() >= input.getStartTimestamp() &&
                        pay.getTimestamp() <= input.getEndTimestamp() &&
                        pay.getIban().equals(account.getIban())) {
                    ObjectNode txt3 = mapper.createObjectNode();
                    txt3.put("commerciant", pay.getCommerciant() != null ? pay.getCommerciant() : "Unknown");
                    txt3.put("total", pay.getAmount());
                    commerciantsList.add(txt3);
                }
            }
            txt2.set("commerciants", commerciantsList);
        }

        txt.set("output", txt2);
        txt.put("timestamp", input.getTimestamp());
        out.add(txt);
    }
}
