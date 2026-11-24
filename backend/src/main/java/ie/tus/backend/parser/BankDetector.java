package ie.tus.backend.parser;

import java.util.List;

// looks at the header row of a file and picks the right bank parser
public class BankDetector {

    // all the banks we can read, add new ones here later (revolut, ptsb)
    private final List<StatementParser> parsers = List.of(new AibParser(), new BoiParser());

    public StatementParser detect(String headerLine) {
        return null; // TODO
    }
}