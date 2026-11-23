package ie.tus.backend.parser;

import java.io.Reader;
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
        return List.of(); // TODO - returns nothing yet so the test fails
    }
}