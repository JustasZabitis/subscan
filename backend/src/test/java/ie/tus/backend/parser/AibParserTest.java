package ie.tus.backend.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

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
}