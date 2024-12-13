package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.commands.Command;
import org.poo.converter.CurrencyConverter;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.converter.ConverterJson;
import org.poo.bank.Bank;
import org.poo.utils.Utils;
import org.poo.commands.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        var sortedFiles = Arrays.stream(Objects.requireNonNull(directory.listFiles())).
                sorted(Comparator.comparingInt(Main::fileConsumer))
                .toList();

        for (File file : sortedFiles) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * Action.
     *
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(CheckerConstants.TESTS_PATH + filePath1);
        ObjectInput inputData = objectMapper.readValue(file, ObjectInput.class);

        ArrayNode output = objectMapper.createArrayNode();
        ConverterJson out = new ConverterJson(output);
        Bank bank = Bank.getInstance(inputData);

        for (int i = 0; i < inputData.getCommands().length; i++) {
            CommandInput input = inputData.getCommands()[i];
            int timestamp = input.getTimestamp();

            switch (input.getCommand()) {
                case "printUsers" -> {
                    Command toExecute = new PrintUsers(bank, out, timestamp);
                    toExecute.execute();
                }
                case "addAccount" -> {
                    Command toExecute = new AddAccount(bank, input);
                    toExecute.execute();
                }
                case "deleteAccount" -> {
                    Command toExecute = new DeleteAccount(bank, input, out);
                    toExecute.execute();
                }
                case "createCard" -> {
                    Command toExecute = new CreateCard(bank, input);
                    toExecute.execute();
                }
                case "createOneTimeCard" -> {
                    Command toExecute = new CreateOneTimeCard(bank, input);
                    toExecute.execute();
                }
                case "deleteCard" -> {
                    Command toExecute = new DeleteCard(bank, input);
                    toExecute.execute();
                }
                case "addFunds" -> {
                    Command toExecute = new AddFunds(bank, input);
                    toExecute.execute();
                }
                case "sendMoney" -> {
                    Command toExecute = new SendMoney(bank, input);
                    toExecute.execute();
                }
                case "payOnline" -> {
                    Command toExecute = new PayOnline(bank, input, out);
                    toExecute.execute();
                }
                case "setAlias" -> {
                    Command toExecute = new SetAlias(bank, input);
                    toExecute.execute();
                }
                case "printTransactions" -> {
                    Command toExecute = new PrintTransactions(bank, input, out);
                    toExecute.execute();
                }
                case "setMinimumBalance" -> {
                    Command toExecute = new SetMinimumBalance(bank, input);
                    toExecute.execute();
                }
                case "checkCardStatus" -> {
                    Command toExecute = new CheckCardStatus(bank, input, out);
                    toExecute.execute();
                }
                case "addInterest" -> {
                    Command toExecute = new AddInterest(bank, input, out);
                    toExecute.execute();
                }
                case "changeInterestRate" -> {
                    Command toExecute = new ChangeInterestRate(bank, input, out);
                    toExecute.execute();
                }
                case "splitPayment" -> {
                    Command toExecute = new SplitPayment(bank, input);
                    toExecute.execute();
                }
                case "report" -> {
                    Command toExecute = new Report(bank, input, out);
                    toExecute.execute();
                }
                case "spendingsReport" -> {
                    Command toExecute = new SpendingsReport(bank, input, out);
                    toExecute.execute();
                }
            }
        }
        Utils.resetRandom();
        Bank.resetInstance();
        CurrencyConverter.resetInstance();

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    /**
     * Method used for extracting the test number from the file name.
     *
     * @param file the input file
     * @return the extracted numbers
     */
    public static int fileConsumer(final File file) {
        String fileName = file.getName()
                .replaceAll(CheckerConstants.DIGIT_REGEX, CheckerConstants.EMPTY_STR);
        return Integer.parseInt(fileName.substring(0, 2));
    }
}
