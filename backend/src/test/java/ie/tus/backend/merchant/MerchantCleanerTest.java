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
        assertEquals("G2G", cleaner.clean("POS19JUN G2G"));
    }

    @Test
    void removesAibCardPrefix() {
        // aib card payments start with VDP- or VDC-
        assertEquals("TESCO STORE 35", cleaner.clean("VDC-TESCO STORE 35"));
    }

    @Test
    void sameMerchantGetsSameName() {
        // spotify puts a different reference on the end every month
        assertEquals("Spotify", cleaner.clean("POS22APR Spotify P41B"));
        assertEquals("Spotify", cleaner.clean("POS22MAY Spotify P42B"));
    }

    @Test
    void showsRealMerchantBehindSumUp() {
        // sumup is a card reader company, the real shop is after the *
        assertEquals("WOL", cleaner.clean("POSC12MAR SUMUP  *WOL"));
    }

    @Test
    void showsRealMerchantBehindPaypal() {
        assertEquals("STEAM", cleaner.clean("VDP-PAYPAL *STEAM"));
    }

    @Test
    void showsRealMerchantBehindSquare() {
        assertEquals("SOS COO", cleaner.clean("POSC13APR SQ *SOS COO"));
    }

    @Test
    void removesCardNumber() {
        // revolut top ups show part of the card number after **
        assertEquals("Revolut", cleaner.clean("VDP-Revolut**5376*"));
    }

    @Test
    void removesCardNumberBoi() {
        assertEquals("Revolut", cleaner.clean("POS04JUL Revolut**509"));
    }

    @Test
    void cleansRealBoiDescriptions() {
        // real descriptions from my own boi export
        assertEquals("NYA*Vending", cleaner.clean("POSC22NOV NYA*Vending"));
        assertEquals("LEAP CARD AP", cleaner.clean("POS27NOV LEAP CARD AP"));
        assertEquals("TESCO STORE", cleaner.clean("POSC12JAN TESCO STORE"));
        assertEquals("OMNIPLEX CA", cleaner.clean("POSC24SEP OMNIPLEX CA"));
        assertEquals("The GYM Car", cleaner.clean("POSC19SEP The GYM Car"));
        assertEquals("Microsoft", cleaner.clean("POS03DEC Microsoft*PC"));
        assertEquals("Microsoft", cleaner.clean("POS19MAY MICROSOFT*ST"));
        assertEquals("Zenimax", cleaner.clean("POS11DEC ZENIMAX EURO"));
        assertEquals("Spotify", cleaner.clean("POS22JUN Spotify P43C"));
        assertEquals("Amazon", cleaner.clean("POS05JAN Amazon.ie"));
    }

    @Test
    void cleansMoreRealDescriptions() {
        // more real descriptions from boi and a volunteer aib export
        assertEquals("Tax", cleaner.clean("POSC30JAN SumUp  *Tax"));
        assertEquals("Revolut", cleaner.clean("POS04JUL Revolut**509"));
        assertEquals("MGP*Vinted", cleaner.clean("POS30JUN MGP*Vinted"));
        assertEquals("VESTA  *EIR", cleaner.clean("POS27AUG VESTA  *EIR"));
        assertEquals("Snap Fitness", cleaner.clean("VDP-SNAPFITNES* SN"));
        assertEquals("Specsavers", cleaner.clean("D/D SPECSAVERS"));
        assertEquals("STEAM", cleaner.clean("VDP-PAYPAL *STEAM"));
        assertEquals("Revolut", cleaner.clean("VDP-Revolut**5376*"));
        assertEquals("Tommy Hilfiger", cleaner.clean("VDC-Tommy Hilfiger"));
        assertEquals("ACCOUNT FEE OCT 26", cleaner.clean("ACCOUNT FEE OCT 26"));
    }
}