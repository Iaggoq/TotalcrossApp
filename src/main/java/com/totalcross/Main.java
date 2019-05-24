package com.totalcross;

import com.totalcross.crud.ui.PersonForm;
import totalcross.sys.Settings;
import totalcross.ui.MainWindow;
import totalcross.ui.gfx.Color;

import java.sql.SQLException;

public class Main extends MainWindow {

    public Main() throws SQLException {
        super("Registry", VERTICAL_GRADIENT);
        gradientTitleStartColor = 0x2979ff;
        gradientTitleEndColor = 0x2979ff;
        setUIStyle(Settings.Material);
        // UI Style
        Settings.uiAdjustmentsBasedOnFontHeight = true;
        setBackColor(Color.brighter(Color.BRIGHT, Color.LESS_STEP));
        // Program Background color
        swap(new PersonForm());
    }
}
