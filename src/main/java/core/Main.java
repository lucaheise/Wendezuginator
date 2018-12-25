package core;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    Stage mainWindow;

    Scene modlistScene;
    Scene vehiclefolderScene;
    Scene mdllistScene;

    ListView<String> modlistListView;
    ListView<String> vehiclefolderListView;
    ListView<String> mdllistListView;

    public static void main(String[] args) {
        launch(args);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;
        mainWindow.setTitle("Wendezuginator");

        OpenModlist();

    }

    public void OpenModlist() {
        //StarterScene importieren
        modlistScene = getStarterScene();

        //StackPane für die Modliste erstellen
        StackPane modlistStackPane = new StackPane();

        //ListView mit der Modliste holen
        modlistListView = FileIO.getModListView();

        //ListView in das StackPane laden
        modlistStackPane.getChildren().add(modlistListView);

        //In Scene einfügen
        ((VBox) modlistScene.getRoot()).getChildren().add(modlistStackPane);

        //In das Fenster einfügen anzeigen
        mainWindow.setScene(modlistScene);

        //Fenster anzeigen
        mainWindow.show();

        //Hinzufügen eines Änderungssuchers zum ListView der Modliste
        modlistListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                                        String old_val, String new_val) {
                        System.out.println("Mod Ausgewählt: " + new_val);

                        switch (FileIO.checkIfContainsTrains(new_val)) {
                            case "KEINE":
                                //Sollte hier eigentlich nicht vorkommen
                                break;
                            case "TRAIN":
                                OpenMDLFolder(new File(CONST.MODFOLDERPATH + "/" + new_val + "/res/models/model/vehicle/train"));
                                break;
                            case "WAGGON":
                                OpenMDLFolder(new File(CONST.MODFOLDERPATH + "/" + new_val + "/res/models/model/vehicle/waggon"));
                                break;
                            case "BEIDE":
                                OpenModVehicles(new_val);
                                break;
                        }

                    }
                });

    }

    public void OpenModVehicles(String ModName) {

        File ModVehiclesFolder = new File(CONST.MODFOLDERPATH + "/" + ModName + "/res/models/model/vehicle/");

        //StarterScene importieren
        vehiclefolderScene = getStarterScene();

        //StackPane für die Modliste erstellen
        StackPane vehiclefolderStackPane = new StackPane();

        //ListView mit der Modliste holen
        vehiclefolderListView = FileIO.getOrdnerListView(ModVehiclesFolder);

        //ListView in das StackPane laden
        vehiclefolderStackPane.getChildren().add(vehiclefolderListView);

        //In Scene einfügen
        ((VBox) vehiclefolderScene.getRoot()).getChildren().add(vehiclefolderStackPane);

        //In das Fenster einfügen anzeigen
        mainWindow.setScene(vehiclefolderScene);


        //Hinzufügen eines Änderungssuchers zum ListView der VehicleOrdnerliste
        vehiclefolderListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                                        String old_val, String new_val) {
                        System.out.println("Vehicle-Typ Ausgewählt: " + new_val);
                        OpenMDLFolder(new File(ModVehiclesFolder.toString() + "/" + new_val + "/"));

                    }
                });
    }

    public void OpenMDLFolder(File MDLFolder) {

        //StarterScene importieren
        mdllistScene = getStarterScene();

        //StackPane für die Modliste erstellen
        StackPane mdllistStackPane = new StackPane();

        //ListView mit der Modliste holen
        mdllistListView = FileIO.getOrdnerListView(MDLFolder);

        //ListView in das StackPane laden
        mdllistStackPane.getChildren().add(mdllistListView);

        //In Scene einfügen
        ((VBox) mdllistScene.getRoot()).getChildren().add(mdllistStackPane);

        //In das Fenster einfügen anzeigen
        mainWindow.setScene(mdllistScene);


        //Hinzufügen eines Änderungssuchers zum ListView der VehicleOrdnerliste
        mdllistListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                                        String old_val, String new_val) {
                        if (new_val.contains(".")) {
                            //Ausgewähltes Objekt ist eine Datei
                            SetMDLToReversible(new File(MDLFolder.toString() + "/" + new_val.substring(5)));
                            OpenMDLFolder(MDLFolder);
                        } else {
                            //Ausgewähltes Objekt ist ein Unterordner
                            System.out.println("Unterordner Ausgewählt: " + new_val);
                            OpenMDLFolder(new File(MDLFolder.toString() + "/" + new_val + "/"));
                        }
                    }
                });
    }

    public void SetMDLToReversible(File MDL) {

        //MDL-Content lesen
        String[] MdlContent = FileIO.getMDLContent(MDL);

        //Backup schreiben
        FileIO.writeMDL(new File(MDL.toString() + ".backup"), MdlContent);

        //Suchen der Linie, in der TransportVehicle definiert wird
        Integer LineMitTransportVehicle = 0;
        String[] MdlContentModified = new String[MdlContent.length + 1];

        for (int i = 1; i < MdlContent.length; i++) {
            if (MdlContent[i].contains("transportVehicle")) {
                LineMitTransportVehicle = i;
            }
        }

        //Zusammensetzen des neuen MDL-Content

        //Teil vor dem Einschub + Einschub
        for (int i = 0; i < LineMitTransportVehicle + 1; i++) {
            MdlContentModified[i] = MdlContent[i];
            MdlContentModified[i + 1] = "reversible = true,";
        }

        for (int i = LineMitTransportVehicle + 2; i < MdlContent.length + 1; i++) {

            MdlContentModified[i] = MdlContent[i - 1];
        }

        //Neue MDL-Datei schreiben
        FileIO.writeMDL(new File(MDL.toString()), MdlContentModified);


    }

    public Scene getStarterScene() {
        //Einträge für den Hauptmenübutton erstellen
        MenuItem itemStartseite = new MenuItem("Startseite");
        MenuItem itemClose = new MenuItem("Beenden");

        //Menüeintrag erstellen
        Menu menuEntryDatei = new Menu("Datei");

        //Einträge zum Menü hinzufügen
        menuEntryDatei.getItems().add(itemStartseite);
        menuEntryDatei.getItems().add(itemClose);

        //Listener für die Knöpfe erstellen
        itemStartseite.setOnAction(event -> {
            System.out.println("Wechsele zur Modliste");
            OpenModlist();
        });

        //Menüknopf der Menübar hinzufügen
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menuEntryDatei);

        VBox mainVBox = new VBox(menuBar);

        return new Scene(mainVBox, 300, 250);
    }

}



