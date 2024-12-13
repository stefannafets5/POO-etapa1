package org.poo.commands;

import org.poo.bank.Bank;
import org.poo.converter.ConverterJson;
import org.poo.fileio.CommandInput;

public class CommandFactory {
    /**
     *
     * @param bank
     * @param input
     * @param out
     * @return
     */
    public static Command createCommand(final Bank bank, final CommandInput input,
                                        final ConverterJson out) {
        return switch (input.getCommand()) {
            case "printUsers" -> new PrintUsers(bank, out, input.getTimestamp());
            case "addAccount" -> new AddAccount(bank, input);
            case "deleteAccount" -> new DeleteAccount(bank, input, out);
            case "createCard" -> new CreateCard(bank, input);
            case "createOneTimeCard" -> new CreateOneTimeCard(bank, input);
            case "deleteCard" -> new DeleteCard(bank, input);
            case "addFunds" -> new AddFunds(bank, input);
            case "sendMoney" -> new SendMoney(bank, input);
            case "payOnline" -> new PayOnline(bank, input, out);
            case "setAlias" -> new SetAlias(bank, input);
            case "printTransactions" -> new PrintTransactions(bank, input, out);
            case "setMinimumBalance" -> new SetMinimumBalance(bank, input);
            case "checkCardStatus" -> new CheckCardStatus(bank, input, out);
            case "addInterest" -> new AddInterest(bank, input, out);
            case "changeInterestRate" -> new ChangeInterestRate(bank, input, out);
            case "splitPayment" -> new SplitPayment(bank, input);
            case "report" -> new Report(bank, input, out);
            case "spendingsReport" -> new SpendingsReport(bank, input, out);
            default -> throw new IllegalArgumentException("The command is not recognized");
        };
    }
}
