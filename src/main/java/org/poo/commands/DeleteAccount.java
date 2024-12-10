package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.converter.ConverterJson;

public class DeleteAccount implements Command {
    private CommandInput input;
    private Bank bank;
    private ConverterJson out;

    public DeleteAccount(Bank bank, CommandInput input, ConverterJson out) {
        this.input = input;
        this.bank = bank;
        this.out = out;
    }

    @Override
    public void execute() {
        if (bank.deleteAccount(input) == 1) {
            out.deleteAccount(input.getTimestamp());
        } else {
            out.deleteAccountFail(input.getTimestamp());
        }
    }
}
