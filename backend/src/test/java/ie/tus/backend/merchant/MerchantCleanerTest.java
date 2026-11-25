package ie.tus.backend.merchant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MerchantCleanerTest {

    MerchantCleaner cleaner = new MerchantCleaner();

    @Test
    void removesBoiCardPrefix() {
        // boi puts POSC + the date at the start of card payments
        assertEquals("EVER DISH", cleaner.clean("POSC27NOV EVER DISH"));
    }

    @Test
    void removesBoiPosPrefix() {
        // online payments use POS without the C
        assertEquals("Spotify P41B", cleaner.clean("POS22APR Spotify P41B"));
    }

    @Test
    void removesAibCardPrefix() {
        // aib card payments start with VDP- or VDC-
        assertEquals("SNAPFITNES* SN", cleaner.clean("VDP-SNAPFITNES* SN"));
    }

    @Test
    void sameMerchantGetsSameName() {
        // spotify puts a different reference on the end every month
        assertEquals("Spotify", cleaner.clean("POS22APR Spotify P41B"));
        assertEquals("Spotify", cleaner.clean("POS22MAY Spotify P42B"));
    }
}