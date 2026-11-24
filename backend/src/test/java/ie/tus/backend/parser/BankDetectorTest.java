package ie.tus.backend.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BankDetectorTest {

    BankDetector detector = new BankDetector();

    @Test
    void picksAibParser() {
        String header = "Posted Account, Posted Transactions Date, Description, Debit Amount, Credit Amount,Balance,Transaction Type";

        assertEquals("AIB", detector.detect(header).bank());
    }

    @Test
    void picksBoiParser() {
        String header = "Date,Details,Debit,Credit,Balance";

        assertEquals("BOI", detector.detect(header).bank());
    }

    @Test
    void unknownBankThrowsError() {
        // some random csv that isnt from aib or boi
        String header = "Name,Age,Town";

        assertThrows(IllegalArgumentException.class, () -> detector.detect(header));
    }
}