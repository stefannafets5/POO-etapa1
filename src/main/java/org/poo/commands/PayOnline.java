package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;

public class PayOnline implements Command{
    private Bank bank;
    private CommandInput input;
    private ConverterJson out;

    public PayOnline(Bank bank, CommandInput input, ConverterJson out) {
        this.input = input;
        this.bank = bank;
        this.out = out;
    }

    @Override
    public void execute() {
        if (bank.payOnline(input) == 2)
            out.cardNotFound(input.getTimestamp(), "payOnline");
    }
}
