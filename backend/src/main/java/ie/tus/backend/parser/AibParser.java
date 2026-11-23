package ie.tus.backend.parser;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// reads CSV exports from the AIB internet banking website
public class AibParser implements StatementParser {

    @Override
    public String bank() {
        return "AIB";
    }

    @Override
    public boolean canParse(String headerLine) {
        return false; // TODO
    }

    @Override
    public List<Transaction> parse(Reader csv) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> lines = new BufferedReader(csv).lines().toList();

        // start at 1 to skip the header row
        for (int i = 1; i < lines.size(); i++) {
            String[] cols = lines.get(i).split(",", -1);

            // column 3 is Debit Amount, money out so make its negative
            BigDecimal debit = new BigDecimal(cols[3]);

            // date will come later, just the amount for now
            transactions.add(new Transaction(null, cols[2], debit.negate(), "EUR"));
        }
        return transactions;
    }
}