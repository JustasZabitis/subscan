package ie.tus.backend.parser;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        // skip the first line because its the header and not needed
        for (int i = 1; i < lines.size(); i++) {
            String[] cols = lines.get(i).split(",", -1);

            // date looks like 06/11/2026
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(cols[1], format);

            String description = cols[2];

            // debit is money going out so make it minus
            BigDecimal amount = new BigDecimal(cols[3]).negate();

            transactions.add(new Transaction(date, description, amount, "EUR"));
        }
        return transactions;
    }
}