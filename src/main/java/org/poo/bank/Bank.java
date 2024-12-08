package org.poo.bank;

import java.util.ArrayList;

import org.poo.users.Account;
import org.poo.users.User;
import org.poo.users.Card;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.CommandInput;

public class Bank {
    private final ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public Bank (ObjectInput input) {
        for (int i = 0; i < input.getUsers().length; i++) {
            this.users.add(new User(input.getUsers()[i].getFirstName(),
                    input.getUsers()[i].getLastName(),
                    input.getUsers()[i].getEmail()));
        }
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

}
