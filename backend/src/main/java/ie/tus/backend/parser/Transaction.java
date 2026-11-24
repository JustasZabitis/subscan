package ie.tus.backend.parser;

import java.math.BigDecimal;
import java.time.LocalDate;

// one row from a bank statement, after it's been read in
// every bank's parser turns its rows into this same format
public record Transaction(
        LocalDate date,
        String description,   // raw text from the bank, e.g. "PAYPAL *NETFLIX.COM"
        BigDecimal amount,    // negative = money out, positive = money in accounts
        String currency       //  EUR because the banks are irish
) {
}