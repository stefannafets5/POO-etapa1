package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;

public class SpendingsReport implements Command {
    private Bank bank;
    private CommandInput input;
    private ConverterJson out;

    public SpendingsReport(Bank bank, CommandInput input, ConverterJson out) {
        this.input = input;
        this.bank = bank;
        this.out = out;
    }

    @Override
    public void execute() {
        if (bank.createReport(input, out, "spendings") == 0)
            out.printError(input.getTimestamp(), "spendingsReport", "Account not found");
    }
}
