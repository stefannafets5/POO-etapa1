package org.poo.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.users.User;
import org.poo.users.Account;
import org.poo.users.Card;

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

    public void cardNotFound(int timestamp){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode txt = mapper.createObjectNode();

        txt.put("command", "payOnline");

        ObjectNode txt2 = mapper.createObjectNode();
        txt2.put("timestamp", timestamp);
        txt2.put("description", "Card not found");

        txt.set("output", txt2);
        txt.put("timestamp", timestamp);
        out.add(txt);
    }
}
