package core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileIO {

    private static String[] getModlist() {

        //Mod Ordner laden
        File ModOrdner = new File(CONST.MODFOLDERPATH);

        //Dateien Auflisten
        String[] ModListe = ModOrdner.list();

        //TODO Steam-Mods ausschließen?

        //TODO Überprüfen, ob da ein Zug drin ist

        //Rückgabe
        return ModListe;
    }

    public static ListView<String> getModListView() {

        //ListView erstellen
        ListView<String> list = new ListView<String>();
        //Modliste abrufen
        ObservableList<String> items = FXCollections.observableArrayList(FileIO.getModlist());

        //Modliste ins ListView einsetzen
        list.setItems(items);
        //Rückgabe der ListView
        return list;
    }

    private static String[] getVehicleOrdnerlist(File Ordner) {
        //Dateien Auflisten
        String[] VehicleOrdnerList = Ordner.list();

        //Rückgabe
        return VehicleOrdnerList;
    }

    public static ListView<String> getOrdnerListView(File Ordner) {
        //ListView erstellen
        ListView<String> list = new ListView<String>();

        //VehicleOrdner abrufen
        ObservableList<String> items = FXCollections.observableArrayList(FileIO.getVehicleOrdnerlist(Ordner));

        //VehicleOrdner-inhalte ins ListView einsetzen
        list.setItems(items);

        //Rückgabe der ListView
        return list;
    }

    public static String[] getMDLContent(File MDL) {

        //Mdl-Content lesen und in die Liste schreiben
        List<String> mdlContentList = null;
        try {
            mdlContentList = Files.readAllLines(MDL.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Liste in ein Array umwandeln
        String[] mdlContentArray = mdlContentList.toArray(new String[]{});

        //Auf unerwünschte Teile überprüfen
        for (int i = 0; i < mdlContentArray.length; i++) {
            mdlContentArray[i] = mdlContentArray[i].replaceAll("[\uFEFF-\uFFFF]", "");

        }


        //Rückgabe
        return mdlContentArray;
    }

    public static void writeMDL(File MDL, String[] MDLContent) {
        //
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(MDL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferedWriter = new BufferedWriter(fileWriter);


        for (int i = 0; i < MDLContent.length; i++) {
            try {
                bufferedWriter.write(MDLContent[i] + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i == 0) {
                System.out.println(MDLContent[i] + "\n");
            }
        }

        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*    public static String[] getZugMDLContent(File ZugMDL) throws IOException {

        //File ZugMDL = new File(CONST.MODFOLDERPATH + "/" + ModName + "\\res\\models\\model\\vehicle\\ " + ZugoWaggon + "\\" + ZugName);

        List<String> stringList = Files.readAllLines(ZugMDL.toPath());
        String[] stringArray = stringList.toArray(new String[]{});
        return stringArray;
    }*/

/*    public static void writeZugMDL(File ZugMDL, String[] Content) throws IOException {

        //File ZugMDL = new File(CONST.MODFOLDERPATH + "/" + ModName + "\\res\\models\\model\\vehicle\\ " + ZugoWaggon + "\\" + ZugName);

        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = new FileWriter(ZugMDL);
        bufferedWriter = new BufferedWriter(fileWriter);

        //Content[0] = Content[0].substring(0);

        for (int i = 0; i < Content.length; i++) {
            bufferedWriter.write(Content[i] + "\n");
            if (i == 0) {
                System.out.println(Content[i] + "\n");
            }
        }

        bufferedWriter.flush();
        bufferedWriter.close();

    }*/

}