package it.polimi.ingsw.view.dialogs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDialogTest {

    @Test void loadsAndPrintsFine(){
        List<String> params = new ArrayList<>();
        params.add("Cristiano Malgiolio");
        assertEquals(UserDialog.getDialog(DialogType.USER_CONNECTED, params),
                "Utente Cristiano Malgiolio si è connesso");
    }

}