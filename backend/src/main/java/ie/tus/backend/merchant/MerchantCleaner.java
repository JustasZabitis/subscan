package ie.tus.backend.merchant;

import java.util.Map;

// turns messy bank descriptions into a clean merchant name
// e.g. "POS22APR Spotify P41B" should become "Spotify"
public class MerchantCleaner {

    // keyword to look for -> clean name
    // keywords are in capitals so case doesnt matter when checking
    private final Map<String, String> knownMerchants = Map.of(
            "SPOTIFY", "Spotify",
            "NETFLIX", "Netflix",
            "SNAPFITNES", "Snap Fitness",
            "SPECSAVERS", "Specsavers",
            "MICROSOFT", "Microsoft",
            "ZENIMAX", "Zenimax",
            "AMAZON", "Amazon"
    );

    public String clean(String description) {
        String name = description;

        // boi card payments start with POSC or POS and then the date, e.g. POSC27NOV
        if (name.startsWith("POSC")) {
            name = name.substring(10); // "POSC27NOV " is 10 characters long
        } else if (name.startsWith("POS")) {
            name = name.substring(9);  // "POS22APR " is 9 characters long
        }
        else if (name.startsWith("VDP-") || name.startsWith("VDC-")) {
            name = name.substring(4); // aib card payments, "VDP-" is 4 characters
        } else if (name.startsWith("D/D ")) {
            name = name.substring(4); // aib direct debits, "D/D " is 4 characters
        }
        // if its a merchant we know, use its proper name
        for (String keyword : knownMerchants.keySet()) {
            if (name.toUpperCase().contains(keyword)) {
                return knownMerchants.get(keyword);
            }
        }

        return name.trim();
    }
}