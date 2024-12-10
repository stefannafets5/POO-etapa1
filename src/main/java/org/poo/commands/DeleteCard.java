package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;

public class DeleteCard implements Command{
    private Bank bank;
    private CommandInput input;

    public DeleteCard(Bank bank, CommandInput input) {
        this.bank = bank;
        this.input = input;
    }

    @Override
    public void execute() {
        bank.deleteCard(input);
    }
}
