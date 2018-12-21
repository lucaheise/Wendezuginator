package core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.File;

public class CONST {
    public static String GAMEPATH = "C:/Program Files (x86)/Steam/steamapps/common/Transport Fever";
    public static String MODFOLDERPATH = GAMEPATH + "/mods";


    public static ListView<String> getListViewTypeSwitcher() {
        //ListView erstellen
        ListView<String> list = new ListView<String>();

        //Einträge hinzufügen
        ObservableList<String> items = FXCollections.observableArrayList("Züge", "Waggons");

        //Einträge ins ListView einsetzen
        list.setItems(items);

        //Rückgabe der ListView
        return list;
    }
}
