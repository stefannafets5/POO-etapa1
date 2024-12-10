package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetAlias implements Command{
    private Bank bank;
    private CommandInput input;

    public SetAlias(Bank bank, CommandInput input) {
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        bank.setAlias(input);
    }
}
