package ie.tus.backend.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}