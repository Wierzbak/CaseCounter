import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Stages {

    public static boolean isFreshlyLaunched = true;

    Stage primaryStage;
    Button openModSelectorButton;
    Button reloadButton;
    Button clearImportButton;
    Button showImportPathButton;
    Button addModeratorButton;
    Button removeModeratorButton;
    ComboBox<Shift> shift = ConfigHandler.INSTANCE.getShiftComboBox();
    Button simpleViewButton = new Button();

    public void primaryStage(Stage primaryStage) {

        ModeratorService.INSTANCE.initModList();

        primaryStage.setTitle("Case Counter");
        this.primaryStage = primaryStage;

        openModSelectorButton = new Button("Select Moderators");
        openModSelectorButton.setOnAction(a -> modSelectorStage());

        reloadButton = new Button("Reload data");
        reloadButton.setOnAction(a -> {
            isFreshlyLaunched = false;
            ModeratorService.INSTANCE.initModList();
            primaryStageShow();
            if (!ConfigHandler.INSTANCE.getShift().isShiftCorrect()) {
                PopUps.warningStage("Incorrect or past shift selected.\nShowing average over 8 hours.", 300, 100);
            }
        });

        clearImportButton = new Button("Clear import");
        clearImportButton.setOnAction(a -> new ImportReader().deleteFiles());

        showImportPathButton = new Button("Show import path");
        showImportPathButton.setOnAction(a -> importPathStage());

        addModeratorButton = new Button("Add Moderator");
        addModeratorButton.setOnAction(a -> addModeratorStage());

        removeModeratorButton = new Button("Remove Moderator(s)");
        removeModeratorButton.setOnAction(a -> removeModeratorsStage());

        primaryStageShow();
    }

    private void primaryStageShow() {

        setUpSimpleViewButton();
        ConfigHandler.INSTANCE.setShift(shift.getValue());

        HBox buttonsBottom = new HBox(20);
        buttonsBottom.alignmentProperty().setValue(Pos.CENTER);
        buttonsBottom.setPadding(new Insets(10, 10, 10, 10));
        buttonsBottom.getChildren().addAll(openModSelectorButton, reloadButton, clearImportButton);

        HBox shiftSelect = new HBox(20);
        shiftSelect.alignmentProperty().setValue(Pos.CENTER_LEFT);
        shiftSelect.setPadding(new Insets(10, 0, 10, 10));
        Label shiftSelectLabel = new Label("Shift:");
        shift = ConfigHandler.INSTANCE.getShiftComboBox();
        shiftSelect.getChildren().addAll(shiftSelectLabel, shift);

        BorderPane bottom = new BorderPane();
        bottom.setCenter(buttonsBottom);
        bottom.setRight(shiftSelect);

        HBox simpViewCheck = new HBox(5);
        simpViewCheck.alignmentProperty().setValue(Pos.CENTER);
        simpViewCheck.setPadding(new Insets(10, 0, 10, 10));
        simpViewCheck.getChildren().addAll(simpleViewButton);

        HBox buttonsTop = new HBox(10);
        buttonsTop.alignmentProperty().setValue(Pos.CENTER_LEFT);
        buttonsTop.setPadding(new Insets(10, 10, 10, 0));
        buttonsTop.getChildren().addAll(showImportPathButton, removeModeratorButton, addModeratorButton);

        BorderPane top = new BorderPane();
        top.setLeft(buttonsTop);
        top.setRight(simpViewCheck);

        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setBottom(bottom);
        root.setCenter(getTableView());
        root.setPadding(new Insets(0, 10, 0, 10));

        int sceneWidth = 937;
        int sceneHeight = 500;

        if (ConfigHandler.INSTANCE.isSimpleView()) {
            sceneWidth = 492;
        }

        Scene scene = new Scene(root, sceneWidth, sceneHeight);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void importPathStage() {

        Stage stage = new Stage();
        stage.setTitle("Import path");
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField textField = new TextField(ImportReader.getCurrentPath() + "/import");
        textField.editableProperty().setValue(false);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(a -> stage.close());
        HBox hBox = new HBox();
        hBox.getChildren().add(closeButton);
        hBox.alignmentProperty().setValue(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setCenter(textField);
        root.setBottom(hBox);

        Scene scene = new Scene(root, 330, 100);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void addModeratorStage() {
        Stage stage = new Stage();
        stage.setTitle("Add moderator");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(8);
        gridPane.setHgap(10);
        stage.initModality(Modality.APPLICATION_MODAL);

        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);

        TextField nameField = new TextField();
        nameField.setPromptText("name");
        GridPane.setConstraints(nameField, 1, 0);

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 1);

        TextField emailField = new TextField();
        emailField.setPromptText("email");
        GridPane.setConstraints(emailField, 1, 1);

        Button addModButton = new Button("Add");
        addModButton.setOnAction(a -> {
            Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
            Matcher name = p.matcher(nameField.getText());
            if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
                PopUps.errorStage("Fields cannot be left blank!", 200, 100);
            } else if (name.find()) {
                PopUps.errorStage("Only latin alphabet accepted in name field.", 300, 100);
            } else if (nameField.getText().contains(";") || emailField.getText().contains(";")) {
                PopUps.errorStage("Neither Name or Email can contain character ' ; '   !", 300, 100);
            } else {
                String mod = nameField.getCharacters() + ";" + emailField.getCharacters();
                if (ModeratorService.INSTANCE.addMod(mod)) {
                    ModeratorService.INSTANCE.initModList();
                    stage.close();
                    PopUps.confirmationStage("Moderator added!", 230, 100);
                    primaryStageShow();
                }
            }
        });
        GridPane.setConstraints(addModButton, 1, 2);

        gridPane.getChildren().addAll(nameLabel, nameField, emailLabel, emailField, addModButton);

        Scene scene = new Scene(gridPane, 260, 120);
        stage.setScene(scene);
        stage.show();
    }

    private void modSelectorStage() {

        Stage stage = new Stage();
        stage.setTitle("Select moderators");

        stage.initModality(Modality.APPLICATION_MODAL);

        List<CheckBox> checkBoxes = getCheckBoxes(true);

        Button ok = new Button("OK");
        ok.setOnAction(a -> {
            List<String> selectedMods = new ArrayList<>();
            for (CheckBox mod : checkBoxes
            ) {
                if (mod.isSelected()) {
                    selectedMods.add(ModeratorService.INSTANCE.getNameDictionary().get(mod.getText()));
                }
            }

            ConfigHandler.INSTANCE.setSelectedMods(selectedMods);

            primaryStageShow();

            stage.close();
        });

        HBox bottom = new HBox();
        bottom.getChildren().add(ok);
        bottom.setPadding(new Insets(10, 10, 0, 0));

        VBox layout = new VBox(10);

        for (CheckBox checkBox : checkBoxes
        ) {
            layout.getChildren().add(checkBox);
        }

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToHeight(true);

        BorderPane root = new BorderPane(scrollPane);
        root.setPadding(new Insets(15));
        root.setBottom(bottom);

        Scene scene = new Scene(root, 300, 700);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void removeModeratorsStage() {

        Stage stage = new Stage();
        stage.setTitle("Select moderators");

        stage.initModality(Modality.APPLICATION_MODAL);

        List<CheckBox> checkBoxes = getCheckBoxes(false);

        Button removeSelectedButton = new Button("Remove selected");
        removeSelectedButton.setOnAction(a -> {
            for (CheckBox mod : checkBoxes
            ) {
                if (mod.isSelected()) {
                    ModeratorService.INSTANCE.removeMod(mod.getText());
                }
            }
            primaryStageShow();
            stage.close();
        });

        HBox bottom = new HBox();
        bottom.getChildren().add(removeSelectedButton);
        bottom.setPadding(new Insets(10, 10, 0, 0));

        VBox layout = new VBox(10);

        for (CheckBox checkBox : checkBoxes
        ) {
            layout.getChildren().add(checkBox);
        }

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToHeight(true);

        BorderPane root = new BorderPane(scrollPane);
        root.setPadding(new Insets(15));
        root.setBottom(bottom);

        Scene scene = new Scene(root, 300, 700);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private TableView<Moderator> getTableView() {

        TableView<Moderator> tableView = new TableView<>();


        TableColumn<Moderator, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Moderator, Integer> L1column = new TableColumn<>("L1");
        L1column.setMinWidth(50);
        L1column.setCellValueFactory(new PropertyValueFactory<>("L1count"));

        TableColumn<Moderator, Integer> L2column = new TableColumn<>("L2");
        L2column.setMinWidth(50);
        L2column.setCellValueFactory(new PropertyValueFactory<>("L2count"));

        TableColumn<Moderator, Integer> L3column = new TableColumn<>("L3");
        L3column.setMinWidth(50);
        L3column.setCellValueFactory(new PropertyValueFactory<>("L3count"));

        TableColumn<Moderator, Integer> L4column = new TableColumn<>("L4");
        L4column.setMinWidth(50);
        L4column.setCellValueFactory(new PropertyValueFactory<>("L4count"));

        TableColumn<Moderator, Integer> UserRateColumn = new TableColumn<>("UR");
        UserRateColumn.setMinWidth(50);
        UserRateColumn.setCellValueFactory(new PropertyValueFactory<>("userRateCount"));

        TableColumn<Moderator, Integer> summaryScoreColumn = new TableColumn<>("Summary\nscore");
        summaryScoreColumn.setMinWidth(70);
        summaryScoreColumn.setMaxWidth(70);
        summaryScoreColumn.setCellValueFactory(new PropertyValueFactory<>("summaryScore"));


        TableColumn<Moderator, String> L1percentageColumn = new TableColumn<>("L1 %");
        L1percentageColumn.setMinWidth(55);
        L1percentageColumn.setMaxWidth(55);
        L1percentageColumn.setCellValueFactory(new PropertyValueFactory<>("L1percentage"));

        TableColumn<Moderator, String> L2percentageColumn = new TableColumn<>("L2 %");
        L2percentageColumn.setMinWidth(55);
        L2percentageColumn.setMaxWidth(55);
        L2percentageColumn.setCellValueFactory(new PropertyValueFactory<>("L2percentage"));

        TableColumn<Moderator, String> L3percentageColumn = new TableColumn<>("L3 %");
        L3percentageColumn.setMinWidth(55);
        L3percentageColumn.setMaxWidth(55);
        L3percentageColumn.setCellValueFactory(new PropertyValueFactory<>("L3percentage"));

        TableColumn<Moderator, String> L4percentageColumn = new TableColumn<>("L4 %");
        L4percentageColumn.setMinWidth(55);
        L4percentageColumn.setMaxWidth(55);
        L4percentageColumn.setCellValueFactory(new PropertyValueFactory<>("L4percentage"));

        TableColumn<Moderator, String> UserRatePercentageColumn = new TableColumn<>("UR %");
        UserRatePercentageColumn.setMinWidth(55);
        UserRatePercentageColumn.setMaxWidth(55);
        UserRatePercentageColumn.setCellValueFactory(new PropertyValueFactory<>("userRatePercentage"));

        TableColumn<Moderator, String> reasonToValidateColumn = new TableColumn<>("Reason to\nvalidate hour\nby hour");
        reasonToValidateColumn.setMinWidth(90);
        reasonToValidateColumn.setMaxWidth(90);
        reasonToValidateColumn.setCellValueFactory(new PropertyValueFactory<>("reasonToValidate"));

        TableColumn<Moderator, Integer> avgHourlyColumn = new TableColumn<>("Avg hourly");
        avgHourlyColumn.setMinWidth(80);
        avgHourlyColumn.setMaxWidth(80);
        avgHourlyColumn.setCellValueFactory(new PropertyValueFactory<>("avgHourly"));

        if (ConfigHandler.INSTANCE.isSimpleView()) {
            tableView.getColumns().addAll(nameColumn, L1column, L2column, L3column, L4column, UserRateColumn,
                    summaryScoreColumn);
        } else {
            tableView.getColumns().addAll(nameColumn, L1column, L2column, L3column, L4column, UserRateColumn,
                    summaryScoreColumn, L1percentageColumn, L2percentageColumn, L3percentageColumn,
                    L4percentageColumn, UserRatePercentageColumn, reasonToValidateColumn, avgHourlyColumn);
        }

        tableView.setItems(ModeratorService.INSTANCE.getMods());

        return tableView;
    }

    private List<CheckBox> getCheckBoxes(boolean checkForSelected) {
        List<CheckBox> checkBoxes = new ArrayList<>();

        for (Map.Entry<String, String> mod : ModeratorService.INSTANCE.getNameDictionary().entrySet()
        ) {
            CheckBox checkBox = new CheckBox(mod.getKey());
            if (checkForSelected && ConfigHandler.INSTANCE.getSelectedMods().contains(mod.getValue())) {
                checkBox.setSelected(true);
            }
            checkBoxes.add(checkBox);
        }
        checkBoxes.sort((c1, c2) -> c1.getText().compareToIgnoreCase(c2.getText()));
        return checkBoxes;
    }

    private void setUpSimpleViewButton() {
        if(ConfigHandler.INSTANCE.isSimpleView()){
            simpleViewButton.setText("Full view");
        } else {
            simpleViewButton.setText("Simple view");
        }
        simpleViewButton.setOnAction(a -> {
            if(ConfigHandler.INSTANCE.isSimpleView()) {
                simpleViewButton.setText("Simple view");
                ConfigHandler.INSTANCE.setSimpleView(false);
            } else {
                simpleViewButton.setText("Full view");
                ConfigHandler.INSTANCE.setSimpleView(true);
            }
            primaryStageShow();
        });
    }
}

