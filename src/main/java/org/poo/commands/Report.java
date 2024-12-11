package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;

public class Report implements Command {
    private Bank bank;
    private CommandInput input;
    private ConverterJson out;

    public Report(Bank bank, CommandInput input, ConverterJson out) {
        this.input = input;
        this.bank = bank;
        this.out = out;
    }

    @Override
    public void execute() {
        if (bank.createReport(input, out, "normal") == 0)
            out.printError(input.getTimestamp(), "report", "Account not found");
    }
}
