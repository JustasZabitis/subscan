package ie.tus.backend.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class BoiParserTest {

    // made up row in the same format as a real boi export
    String csv = "Date,Details,Debit,Credit,Balance\n"
            + "22/04/2026,POS22APR Spotify P41B,6.99,,545.94\n";

    @Test
    void readsDebitAsNegative() {
        List<Transaction> result = new BoiParser().parse(new StringReader(csv));

        assertEquals(new BigDecimal("-6.99"), result.get(0).amount());
    }

    @Test
    void readsDate() {
        List<Transaction> result = new BoiParser().parse(new StringReader(csv));

        // boi dates are day/month/year too
        assertEquals(LocalDate.of(2026, 4, 22), result.get(0).date());
    }
}