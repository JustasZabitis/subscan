package ie.tus.backend.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void readsCreditAsPositive() {
        // money coming in, debit is empty and credit has the amount
        String csvIn = "Date,Details,Debit,Credit,Balance\n"
                + "05/12/2025,365  money           ,,70.00,292.13\n";

        List<Transaction> result = new BoiParser().parse(new StringReader(csvIn));

        assertEquals(new BigDecimal("70.00"), result.get(0).amount());
    }

    @Test
    void trimsSpacesFromDescription() {
        // boi adds spaces at the end to make every description 20 characters
        String csvSpaces = "Date,Details,Debit,Credit,Balance\n"
                + "28/11/2025,POS27NOV EVER DISH   ,6.30,,\n";

        List<Transaction> result = new BoiParser().parse(new StringReader(csvSpaces));

        assertEquals("POS27NOV EVER DISH", result.get(0).description());
    }

    @Test
    void skipsEmptyRowAtEnd() {
        // the real boi export ends with a line that is just a comma
        String csvEnd = "Date,Details,Debit,Credit,Balance\n"
                + "22/04/2026,POS22APR Spotify P41B,6.99,,545.94\n"
                + ",\n";

        List<Transaction> result = new BoiParser().parse(new StringReader(csvEnd));

        assertEquals(1, result.size());
    }

    @Test
    void recognisesBoiHeader() {
        String header = "Date,Details,Debit,Credit,Balance";

        assertTrue(new BoiParser().canParse(header));
    }

    @Test
    void doesNotRecogniseAibHeader() {
        // this is what an aib export starts with
        String header = "Posted Account, Posted Transactions Date, Description, Debit Amount, Credit Amount,Balance,Transaction Type";

        assertFalse(new BoiParser().canParse(header));
    }

    @Test
    void readsSampleFile() {
        // fake boi file in src/test/resources
        InputStream file = getClass().getResourceAsStream("/boi_sample.csv");

        List<Transaction> result = new BoiParser().parse(new InputStreamReader(file));

        // 6 real rows, the "," line at the end gets skipped
        assertEquals(6, result.size());
    }
}