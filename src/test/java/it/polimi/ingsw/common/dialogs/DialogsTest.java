package it.polimi.ingsw.common.dialogs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DialogsTest {

    @Test void loadsAndPrintsFine(){
        assertEquals(Dialogs.getDialog(Dialog.USER_CONNECTED, "Cristiano Malgiolio"),
                "Utente Cristiano Malgiolio si è connesso");
    }

    @Test
    void testFormatWithMoreParameters() {
        assertEquals("Scegli la casella in cui muoverti", Dialogs.getDialog(Dialog.MOVE, "1", "1"));
    }

}