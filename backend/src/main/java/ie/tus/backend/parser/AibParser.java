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

        // only aib has a column called "Posted Transactions Date"
        return headerLine.contains("Posted Transactions Date");
    }

    @Override
    public List<Transaction> parse(Reader csv) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> lines = new BufferedReader(csv).lines().toList();

        // skip the first line because its the header and not needed
        for (int i = 1; i < lines.size(); i++) {
            // skip empty lines
            if (lines.get(i).isBlank()) {
                continue;
            }

            String[] cols = lines.get(i).split(",", -1);

            // date looks like 06/11/2026
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(cols[1], format);

            String description = cols[2];

            // if debit is empty then its money coming in (credit)
            BigDecimal amount;
            if (cols[3].isEmpty()) {
                amount = new BigDecimal(cols[4]);
            } else {
                // debit is money going out so make it minus
                amount = new BigDecimal(cols[3]).negate();
            }
            // skip the junk rows aib adds with 0 (reference numbers etc)
            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            transactions.add(new Transaction(date, description, amount, "EUR"));
        }
        return transactions;
    }
}