package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;

public class SplitPayment implements Command{
    private Bank bank;
    private CommandInput input;
    private ConverterJson out;

    public SplitPayment(Bank bank, CommandInput input, ConverterJson out) {
        this.input = input;
        this.bank = bank;
        this.out = out;
    }

    @Override
    public void execute() {
        bank.splitPayment(input);
    }
}
