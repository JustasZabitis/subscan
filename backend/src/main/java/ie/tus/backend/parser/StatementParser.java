package ie.tus.backend.parser;

import java.io.Reader;
import java.util.List;

// every bank parser (AIB, BOI, Revolut, PTSB) follows this
// so the rest of the app doesn't care which bank the file came from
public interface StatementParser {

    // which bank this parser is for, e.g. "AIB"
    String bank();

    // checks the header row to see if this file belongs to this bank
    boolean canParse(String headerLine);

    // reads the whole CSV and gives back the transactions
    List<Transaction> parse(Reader csv);
}