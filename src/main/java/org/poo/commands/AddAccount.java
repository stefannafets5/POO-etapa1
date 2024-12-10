package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class AddAccount implements Command{
    private Bank bank;
    private CommandInput input;

    public AddAccount(Bank bank, CommandInput input) {
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        bank.addAccount(input);
    }
}
