package org.poo.bank;

import java.util.ArrayList;
import java.util.List;

import org.poo.converter.ConverterJson;
import org.poo.converter.CurrencyConverter;
import org.poo.users.Account;
import org.poo.users.SavingsAccount;
import org.poo.users.User;
import org.poo.users.Card;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.CommandInput;

public class Bank {
    private static Bank instance;
    private final ArrayList<User> users = new ArrayList<>();
    private CurrencyConverter moneyConverter;

    public ArrayList<User> getUsers() {
        return users;
    }

    public CurrencyConverter getMoneyConverter() {
        return moneyConverter;
    }

    private Bank (ObjectInput input) {
        for (int i = 0; i < input.getUsers().length; i++) { // initialize users
            this.users.add(new User(input.getUsers()[i].getFirstName(),
                    input.getUsers()[i].getLastName(),
                    input.getUsers()[i].getEmail()));
        }
        moneyConverter = CurrencyConverter.getInstance(input);
    }

    public static Bank getInstance(ObjectInput input) {
        if (instance == null)
            instance = new Bank(input);
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    public void setAlias (CommandInput input) {
        String iban = input.getAccount();
        String email = input.getEmail();
        String alias = input.getAlias();

        for (User user : users)
            if (user.getEmail().equals(email))
                for (Account currentAccount : user.getAccounts())
                    if (currentAccount.getIban().equals(iban))
                        currentAccount.setAlias(alias);
    }

    public void addAccount (CommandInput input){
        String email = input.getEmail();

        for (User user : users)
            if (user.getEmail().equals(email)){
                user.addAccount(input);
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
                            user.deleteAccount(i);
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

    public void setMinimumBalance (CommandInput input) {
        String iban = input.getAccount();
        double amount = input.getAmount();

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                if (currentAccount.getIban().equals(iban))
                    currentAccount.setMinBalance(amount);
    }

    public int checkCardStatus (CommandInput input) {
        String cardNr = input.getCardNumber();
        int found = 0;

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                for (Card currentCard : currentAccount.getCards())
                    if (currentCard.getCardNumber().equals(cardNr)) {
                        found = 1;
                        if (currentCard.getStatus().equals("active") &&
                                currentAccount.getBalance() <= currentAccount.getMinBalance()) {

                            user.addErrorTransaction(input.getTimestamp(),
                                    "You have reached the minimum amount" +
                                            " of funds, the card will be frozen");
                            currentCard.setStatus("frozen");
                        }
                    }
        return found;
    }

    public void createCard (CommandInput input, String type){
        String email = input.getEmail();
        String iban = input.getAccount();

        for (User user : users)
            if (user.getEmail().equals(email))
                for (Account currentAccount : user.getAccounts())
                    if (currentAccount.getIban().equals(iban)){
                        currentAccount.addCard(input, type,
                                user.getTransactions(), "New card created");
                        break;
                    }
    }

    public void deleteCard (CommandInput input){
        String cardNr = input.getCardNumber();

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                for (int i = 0; i < currentAccount.getCards().size(); i++)
                    if (currentAccount.getCards().get(i).getCardNumber().equals(cardNr)){
                        currentAccount.removeCard(i, input, user.getEmail(),
                                user.getTransactions(), "The card has been destroyed");
                        break;
                    }
    }

    public int payOnline (CommandInput input){
        String cardNr = input.getCardNumber();
        double amount = input.getAmount();
        String from = input.getCurrency();
        int timestamp = input.getTimestamp();
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
                            double amountToBePayed = getMoneyConverter().convert(amount, from, to);
                            if (currentCard.getStatus().equals("frozen")) {
                                user.addErrorTransaction(timestamp, "The card is frozen");
                                return 0;
                            } else if (currentAccount.getBalance() >= amountToBePayed) {
                                currentAccount.subtractMoney(amountToBePayed);
                                user.addCardPaymentTransaction(timestamp, amountToBePayed, commerciant);
                                return 1;
                            }
                        }
        for (User user : users) /// paying failed
            if (user.getEmail().equals(email) && found == 1)
                user.addPaymentFailedTransaction(timestamp);

        if (found == 0)
            return 2;
        return 0;
    }

    public int splitPayment (CommandInput input){
        ArrayList<String> ibanList = new ArrayList<>(input.getAccounts());
        double amount = input.getAmount();
        double eachAccountAmount = amount/ibanList.size();
        String from = input.getCurrency();
        int timestamp = input.getTimestamp();
        int everyoneHasMoney = 0;

        for (User user : users) { // check if everyone can pay
            for (Account currentAccount : user.getAccounts()) {
                for (String iban : ibanList) {
                    if (currentAccount.getIban().equals(iban)) {
                        String to = currentAccount.getCurrency();
                        double amountToBePayed =
                                getMoneyConverter().convert(eachAccountAmount, from, to);
                        if (currentAccount.getBalance() >= amountToBePayed) {
                            everyoneHasMoney++;
                        }
                    }
                }
            }
        }
        if (everyoneHasMoney == ibanList.size()){
            for (User user : users) {
                for (Account currentAccount : user.getAccounts()) {
                    for (String iban : ibanList) {
                        if (currentAccount.getIban().equals(iban)) {
                            String to = currentAccount.getCurrency();
                            double amountToBePayed =
                                    getMoneyConverter().convert(eachAccountAmount, from, to);
                            currentAccount.subtractMoney(amountToBePayed);
                            user.addSplitCardPaymentTransaction(timestamp,
                                    amountToBePayed, amount, from, ibanList);
                        }
                    }
                }
            }
            return 1;
        }
        return 0; // maybe need to create a fail transaction
    }

    public int sendMoney (CommandInput input){
        int timestamp = input.getTimestamp();
        String iban = input.getAccount();
        double amount = input.getAmount();
        String ibanReceiver = input.getReceiver();
        int receiverExists = 0;
        int hasMoney = 1;
        int found = 0;
        String from = "RON"; // initialization but never used like this

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                if (currentAccount.getIban().equals(ibanReceiver))
                    receiverExists = 1;
        if (receiverExists == 1) {

            for (User user : users)
                for (Account currentAccount : user.getAccounts())
                    if (currentAccount.getIban().equals(iban)) {
                        found = 1;
                        from = currentAccount.getCurrency();
                        if (currentAccount.getBalance() < amount) {
                            user.addPaymentFailedTransaction(timestamp);
                            hasMoney = 0;
                        }
                        if (hasMoney == 1) {
                            currentAccount.subtractMoney(amount);
                            user.addMoneyTransferTransaction(input, "sent", from);
                        }
                    }
            for (User user : users)
                for (Account currentAccount : user.getAccounts())
                    if (currentAccount.getIban().equals(ibanReceiver)) {
                        String to = currentAccount.getCurrency();
                        if (found == 1 && hasMoney == 1) {
                            double amountToBePayed = getMoneyConverter().convert(amount, from, to);
                            currentAccount.addMoney(amountToBePayed);
                            //user.addMoneyTransferTransaction(input, "received", to);
                            return 1;
                        }
                    }
        }
        return 0;
    }

    public void printTransactions (CommandInput input, ConverterJson out){
        String email = input.getEmail();
        int timestamp = input.getTimestamp();

        for (User user : users)
            if (user.getEmail().equals(email)) {
                out.printTransactions(user.getTransactions(), timestamp);
                break;
            }
    }

    public void changeInterestRate (CommandInput input){
        String iban = input.getAccount();
        double interestRate = input.getInterestRate();

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                if (currentAccount.getIban().equals(iban))
                    if (currentAccount.getType().equals("savings")){
                        SavingsAccount savingsAccount = (SavingsAccount) currentAccount;
                        savingsAccount.setInterestRate(interestRate);
                    }

    }

    public void createReport (CommandInput input, ConverterJson out){
        String iban = input.getAccount();

        for (User user : users)
            for (Account currentAccount : user.getAccounts())
                if (currentAccount.getIban().equals(iban))
                    out.createReport(user.getTransactions(), input, currentAccount);
    }
}
