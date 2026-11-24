package ie.tus.backend.parser;

import java.io.Reader;
import java.util.List;

// reads CSV exports from bank of ireland 365 online
public class BoiParser implements StatementParser {

    @Override
    public String bank() {
        return "BOI";
    }

    @Override
    public boolean canParse(String headerLine) {
        return false; // TODO
    }

    @Override
    public List<Transaction> parse(Reader csv) {
        return List.of(); // TODO
    }
}