package ie.tus.backend.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AibParserTest {

    // made up row in the same format as a real AIB export for now
    String csv = "Posted Account, Posted Transactions Date, Description, Debit Amount, Credit Amount,Balance,Transaction Type\n"
            + "123456-12345678,06/11/2026,VDP-NETFLIX.COM,12.99,,40.06,Debit\n";

    @Test
    void readsDebitAsNegative() {
        List<Transaction> result = new AibParser().parse(new StringReader(csv));

        assertEquals(new BigDecimal("-12.99"), result.get(0).amount());
    }

    @Test
    void readsDate() {
        List<Transaction> result = new AibParser().parse(new StringReader(csv));

        // AIB dates are day/month/year
        assertEquals(LocalDate.of(2026, 11, 6), result.get(0).date());
    }

    @Test
    void readsCreditAsPositive() {
        // money coming in, debit is empty and credit has the amount
        String csvIn = "Posted Account, Posted Transactions Date, Description, Debit Amount, Credit Amount,Balance,Transaction Type\n"
                + "123456-12345678,07/11/2026,SALARY,,500.00,540.06,Credit\n";

        List<Transaction> result = new AibParser().parse(new StringReader(csvIn));

        assertEquals(new BigDecimal("500.00"), result.get(0).amount());
    }

    @Test
    void skipsZeroRows() {
        // AIB puts reference numbers on their own row with 0, these arent real transactions
        String csvJunk = "Posted Account, Posted Transactions Date, Description, Debit Amount, Credit Amount,Balance,Transaction Type\n"
                + "123456-12345678,12/11/2026,D/D SPECSAVERS,26,,,Debit\n"
                + "123456-12345678,12/11/2026,IE26111125048932,,0,538.73,Credit\n";

        List<Transaction> result = new AibParser().parse(new StringReader(csvJunk));

        // only the specsavers row should be left
        assertEquals(1, result.size());
    }

    @Test
    void skipsBlankLines() {
        // the  aib exports have an empty line at the end
        String csvBlank = "Posted Account, Posted Transactions Date, Description, Debit Amount, Credit Amount,Balance,Transaction Type\n"
                + "123456-12345678,06/11/2026,VDP-NETFLIX.COM,12.99,,40.06,Debit\n"
                + "\n";

        List<Transaction> result = new AibParser().parse(new StringReader(csvBlank));

        assertEquals(1, result.size());
    }

    @Test
    void recognisesAibHeader() {
        String header = "Posted Account, Posted Transactions Date, Description, Debit Amount, Credit Amount,Balance,Transaction Type";

        assertTrue(new AibParser().canParse(header));
    }

    @Test
    void doesNotRecogniseBoiHeader() {
        // this is what a bank of ireland export starts with
        String header = "Date,Details,Debit,Credit,Balance";

        assertFalse(new AibParser().canParse(header));
    }

    @Test
    void readsSampleFile() {
        // fake aib file in src/test/resources
        InputStream file = getClass().getResourceAsStream("/aib_sample.csv");

        List<Transaction> result = new AibParser().parse(new InputStreamReader(file));

        // 7 rows in the file but 2 are nothing rows with 0
        assertEquals(5, result.size());
    }
}