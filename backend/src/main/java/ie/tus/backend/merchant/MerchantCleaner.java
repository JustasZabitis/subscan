package ie.tus.backend.merchant;

// turns messy bank descriptions into a clean merchant name
// e.g. "POS22APR Spotify P41B" should become "Spotify"
public class MerchantCleaner {

    public String clean(String description) {
        String name = description;

        // boi card payments start with POSC or POS and then the date, e.g. POSC27NOV
        if (name.startsWith("POSC")) {
            name = name.substring(10); // "POSC27NOV " is 10 characters long
        } else if (name.startsWith("POS")) {
            name = name.substring(9);  // "POS22APR " is 9 characters long
        }

        return name.trim();
    }
}