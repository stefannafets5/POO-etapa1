package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class CreateOneTimeCard implements Command{
    private Bank bank;
    private CommandInput input;

    public CreateOneTimeCard(Bank bank, CommandInput input) {
        this.bank = bank;
        this.input = input;
    }

    @Override
    public void execute() {
        bank.createCard(input, "oneTime");
    }
}
