package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class ChangeInterestRate implements Command{
    private Bank bank;
    private CommandInput input;

    public ChangeInterestRate(Bank bank, CommandInput input) {
        this.input = input;
        this.bank = bank;
    }

    @Override
    public void execute() {
        bank.changeInterestRate(input);
    }
}
