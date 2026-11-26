package ie.tus.backend.merchant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ie.tus.backend.parser.BankDetector;
import ie.tus.backend.parser.StatementParser;
import ie.tus.backend.parser.Transaction;

// one off check for the sprint 1 go / no-go
// runs my real statement through the parser and cleaner
public class GoNoGoCheck {

    public static void main(String[] args) throws Exception {
        // my real statement, this stays on my pc and never goes in the repo
        String path = "C:/Users/justa/Downloads/boi.csv";

        // read the header to find out which bank it is
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String header = reader.readLine();
        reader.close();

        StatementParser parser = new BankDetector().detect(header);
        List<Transaction> transactions = parser.parse(new FileReader(path));

        // count how many times each clean name comes up
        MerchantCleaner cleaner = new MerchantCleaner();
        Map<String, Integer> counts = new TreeMap<>();

        for (Transaction t : transactions) {
            // only look at money going out
            if (t.amount().signum() < 0) {
                String name = cleaner.clean(t.description());
                counts.put(name, counts.getOrDefault(name, 0) + 1);
            }
        }

        // print anything paid 3 or more times
        for (String name : counts.keySet()) {
            if (counts.get(name) >= 3) {
                System.out.println(name + " - " + counts.get(name) + " times");
            }
        }
    }
}