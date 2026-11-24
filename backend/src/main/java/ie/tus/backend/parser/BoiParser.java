package ie.tus.backend.parser;

import java.io.BufferedReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        // boi header is exactly this, aib has different column names
        return headerLine.trim().equals("Date,Details,Debit,Credit,Balance");
    }

    @Override
    public List<Transaction> parse(Reader csv) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> lines = new BufferedReader(csv).lines().toList();

        // skip the first line because its the header
        for (int i = 1; i < lines.size(); i++) {
            String[] cols = lines.get(i).split(",", -1);

            // skip rows with no date, like the "," line at the end of the file
            if (cols[0].isBlank()) {
                continue;
            }

            // date looks like 22/04/2026
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(cols[0], format);

            // remove the extra spaces boi adds at the end
            String description = cols[1].trim();

            // if debit is empty then its money coming in (credit)
            BigDecimal amount;
            if (cols[2].isEmpty()) {
                amount = new BigDecimal(cols[3]);
            } else {
                // debit is money going out so make it minus
                amount = new BigDecimal(cols[2]).negate();
            }

            // date comes next, just the amount for now
            transactions.add(new Transaction(date, description, amount, "EUR"));
        }
        return transactions;
    }


}