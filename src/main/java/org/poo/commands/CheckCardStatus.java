package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;

public class CheckCardStatus implements Command{
    private Bank bank;
    private CommandInput input;
    private ConverterJson out;

    public CheckCardStatus(Bank bank, CommandInput input, ConverterJson out) {
        this.input = input;
        this.bank = bank;
        this.out = out;
    }

    @Override
    public void execute() {
        if (bank.checkCardStatus(input) == 0)
            out.printError(input.getTimestamp(), "checkCardStatus", "Card not found");
    }
}
