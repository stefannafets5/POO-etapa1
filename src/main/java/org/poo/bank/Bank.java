package org.poo.bank;

import java.util.ArrayList;
import java.util.Currency;

import org.poo.converter.CurrencyConverter;
import org.poo.users.Account;
import org.poo.users.User;
import org.poo.users.Card;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.CommandInput;

public class Bank {
    private final ArrayList<User> users = new ArrayList<>();
    private CurrencyConverter moneyConverter;

    public ArrayList<User> getUsers() {
        return users;
    }
    public CurrencyConverter getCurrencyConverter() {
        return moneyConverter;
    }

    public Bank (ObjectInput input) {
        for (int i = 0; i < input.getUsers().length; i++) { // initialize users
            this.users.add(new User(input.getUsers()[i].getFirstName(),
                    input.getUsers()[i].getLastName(),
                    input.getUsers()[i].getEmail()));
        }
        moneyConverter = new CurrencyConverter(input);
    }

    public void addAccount (CommandInput input){
        String email = input.getEmail();

        for (User user : users)
            if (user.getEmail().equals(email)){
                Account aux = new Account(input.getCurrency(),
                        input.getAccountType(), input.getTimestamp());
                user.getAccounts().add(aux);
                break;
            }
    }

    public int deleteAccount (CommandInput input){
        String iban = input.getAccount();
        String email = input.getEmail();

        for (User user : users)
            if (user.getEmail().equals(email))
                for (int i = 0; i < user.getAccounts().size(); i++)
                    if (user.getAccounts().get(i).getIban().equals(iban))
                        if (user.getAccounts().get(i).getBalance() == 0){
                            user.getAccounts().remove(i);
                            return 1;
                        }
        return 0;
    }

    public void addFounds (CommandInput input){
        String iban = input.getAccount();

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                if (currentAccount.getIban().equals(iban)){
                    currentAccount.addMoney(input.getAmount());
                    break;
                }
    }

    public void createCard (CommandInput input, String type){
        String email = input.getEmail();
        String iban = input.getAccount();

        for (User user : users)
            if (user.getEmail().equals(email))
                for (Account currentAccount : user.getAccounts())
                    if (currentAccount.getIban().equals(iban)){
                        Card card = new Card(input.getTimestamp(), type);
                        currentAccount.getCards().add(card);
                        break;
                    }
    }

    public void deleteCard (CommandInput input){
        String cardNr = input.getCardNumber();

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                for (int i = 0; i < currentAccount.getCards().size(); i++)
                    if (currentAccount.getCards().get(i).getCardNumber().equals(cardNr)){
                        currentAccount.getCards().remove(i);
                        break;
                    }
    }

    public int payOnline (CommandInput input){
        String cardNr = input.getCardNumber();
        double amount = input.getAmount();
        String from = input.getCurrency();
        int timestamp = input.getTimestamp();
        String description = input.getDescription();
        String commerciant = input.getCommerciant();
        String email = input.getEmail();
        int found = 0;

        for (User user : users)
            if (user.getEmail().equals(email))
                for (Account currentAccount : user.getAccounts())
                    for (Card currentCard : currentAccount.getCards())
                        if (currentCard.getCardNumber().equals(cardNr)) {
                            found = 1;
                            String to = currentAccount.getCurrency();
                            double amountToBePayed = moneyConverter.convert(amount, from, to);
                            if (currentAccount.getBalance() >= amountToBePayed) {
                                currentAccount.subtractMoney(amountToBePayed);
                                return 1;
                            }
                        }
        if (found == 0)
            return 2;
        return 0;
    }

    public int sendMoney(CommandInput input){
        String iban = input.getAccount();
        double amount = input.getAmount();
        String ibanReceiver = input.getReceiver();
        int timestamp = input.getTimestamp();
        String description = input.getDescription();
        int hasMoney = 1;
        int found = 0;
        String from = "RON"; // initialization but never used like this

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                if (currentAccount.getIban().equals(iban)) {
                    found = 1;
                    from = currentAccount.getCurrency();
                    if (currentAccount.getBalance() < amount) {
                        System.out.println("Not enough money");
                        hasMoney = 0;
                    }
                    if (hasMoney == 1)
                        currentAccount.subtractMoney(amount);
                }
        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                if (currentAccount.getIban().equals(ibanReceiver)) {
                    String to = currentAccount.getCurrency();
                    if (found == 1 && hasMoney == 1){
                        double amountToBePayed = moneyConverter.convert(amount, from, to);
                        currentAccount.addMoney(amountToBePayed);
                        return 1;
                    }
                }


        return 0;
    }
}
