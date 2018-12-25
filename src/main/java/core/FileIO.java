package core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileIO {

    private static String[] getModlist() {

        //Mod Ordner laden
        File ModOrdner = new File(CONST.MODFOLDERPATH);

        //Dateien Auflisten
        String[] ModListeDirty = ModOrdner.list();

        //Überprüfen, ob da ein Zug drin ist

        List<String> ModListeClean = new ArrayList<>();

        for (String element : ModListeDirty) {
            switch (checkIfContainsTrains(element)) {
                case "KEINE":
                    break;
                //Wenn der Mod Züge enthält, dann der Liste hinzufügen = Filter nur Züge und Waggons
                default:
                    //Mods von Urbangames rausfiltern
                    if (!element.contains("urbangames")) {
                        ModListeClean.add(element);
                    }

                    break;
            }
        }

        //Liste in Stringarray umwandeln
        String[] ZugUndWaggonArray = ModListeClean.toArray(new String[0]);

        //Rückgabe
        return ZugUndWaggonArray;
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

        List<String> OrdnerInhaltsliste = new ArrayList<>();

        for (String element : VehicleOrdnerList) {
            if (!new File(Ordner.toString() + "/" + element).isDirectory()) {
                System.out.println("File" + element);

                String[] MdlContent = getMDLContent(new File(Ordner.toString() + "/" + element));

                boolean isWendezug = false;

                for (String mdlLine : MdlContent) {
                    if (mdlLine.contains("reversible = true")) {
                        isWendezug = true;
                    }
                }

                if (isWendezug) {
                    OrdnerInhaltsliste.add("[WZ] " + element);
                } else {
                    OrdnerInhaltsliste.add("[NW] " + element);
                }


            } else {
                //Ist ein Ordner
                OrdnerInhaltsliste.add(element);
            }

        }

        String[] VehicleOrdnerListModified = OrdnerInhaltsliste.toArray(new String[0]);

        //Rückgabe
        return VehicleOrdnerListModified;
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
        String[] mdlContentArray = mdlContentList.toArray(new String[0]);

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
        }

        try {
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Rückgabe TRUE, wenn der Ordner existiert, sonst FALSE
    public static boolean checkIfFolderExists(File File) {

        return File.exists();

    }

    //Rückgabe von KEINE, TRAIN, WAGGON oder BEIDE
    public static String checkIfContainsTrains(String ModName) {

        File TrainFolder = new File(CONST.MODFOLDERPATH + "/" + ModName + "/res/models/model/vehicle/train");
        File WaggonFolder = new File(CONST.MODFOLDERPATH + "/" + ModName + "/res/models/model/vehicle/waggon");

        boolean TrainExists = checkIfFolderExists(TrainFolder);
        boolean WaggonsExists = checkIfFolderExists(WaggonFolder);

        if (TrainExists && !WaggonsExists) {
            System.out.println(ModName + " enthält nur Züge");
            return "TRAIN";
        }

        if (TrainExists && WaggonsExists) {
            System.out.println(ModName + " enthält Züge und Waggons");
            return "BEIDE";
        }

        if (!TrainExists && WaggonsExists) {
            System.out.println(ModName + " enthält nur Waggons");
            return "WAGGON";
        }

        System.out.println(ModName + " enthält keine Züge");
        return "KEINE";
    }

}
