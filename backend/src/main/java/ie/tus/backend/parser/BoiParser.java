package ie.tus.backend.parser;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// reads CSV exports from bank of ireland 365 online
public class BoiParser implements StatementParser {

    @Override
    public String bank() {
        return "BOI";
    }

    @Override
    public boolean canParse(String headerLine) {
        return false; // TODO
    }

    @Override
    public List<Transaction> parse(Reader csv) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> lines = new BufferedReader(csv).lines().toList();

        // skip the first line because its the header
        for (int i = 1; i < lines.size(); i++) {
            String[] cols = lines.get(i).split(",", -1);

            String description = cols[1];

            // boi has debit in column 2, money going out so make it minus
            BigDecimal amount = new BigDecimal(cols[2]).negate();

            // date comes next, just the amount for now
            transactions.add(new Transaction(null, description, amount, "EUR"));
        }
        return transactions;
    }
}