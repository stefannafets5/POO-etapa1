package org.poo.commands;

import org.poo.converter.ConverterJson;
import org.poo.bank.Bank;

public class PrintUsers implements Command {
    private Bank bank;
    private ConverterJson out;
    private int timestamp;

    public PrintUsers(Bank bank, ConverterJson out, int timestamp) {
        this.bank = bank;
        this.out = out;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {
        out.printUsers(bank, timestamp);
    }
}
