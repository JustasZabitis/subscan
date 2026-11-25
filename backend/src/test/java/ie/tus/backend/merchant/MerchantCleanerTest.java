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
}