package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;

public class AddInterest implements Command{
    private Bank bank;
    private CommandInput input;
    private ConverterJson out;

    public AddInterest(Bank bank, CommandInput input, ConverterJson out) {
        this.input = input;
        this.bank = bank;
        this.out = out;
    }

    @Override
    public void execute() {
        if (bank.addInterest(input) == 0)
            out.printError(input.getTimestamp(), "addInterest", "This is not a savings account");
    }
}
