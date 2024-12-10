package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class SetMinimumBalance implements Command{
    private Bank bank;
    private CommandInput input;

    public SetMinimumBalance(Bank bank, CommandInput input) {
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        bank.setMinimumBalance(input);
    }
}
