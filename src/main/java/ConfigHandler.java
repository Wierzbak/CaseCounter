import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.io.*;
import java.util.List;

public enum ConfigHandler {
    INSTANCE;

    private final File cfg = new File(ImportReader.getCurrentPath() + "/cfg.json");

    private Configuration configuration;
    private Gson gson = new Gson();

    private void loadConfig() {
        try {
            FileReader reader = new FileReader(cfg);
            configuration = gson.fromJson(reader, Configuration.class);
        } catch (FileNotFoundException ignored) {
            configuration = new Configuration();
            saveConfig();
        }
    }

    private void saveConfig() {
        try {
            cfg.delete();
            FileWriter writer = new FileWriter(cfg);
            writer.write(gson.toJson(configuration));
            writer.close();
        } catch (IOException e) {
            PopUps.errorStage(e.getLocalizedMessage(), 200, 150);
        }
    }

    public List<String> getSelectedMods() {
        loadConfig();
        return configuration.getSelectedMods();
    }

    public void setSelectedMods(List<String> selectedMods) {
        configuration.setSelectedMods(selectedMods);
        saveConfig();
    }

    public Shift getShift() {
        loadConfig();
        return configuration.getShift();
    }

    public void setShift(Shift shift) {
        configuration.setShift(shift);
        saveConfig();
    }

    public ComboBox<Shift> getShiftComboBox() {
        loadConfig();
        ObservableList<Shift> options = FXCollections.observableArrayList
                (new Shift("A"), new Shift("B"), new Shift("C"));

        ComboBox<Shift> shift = new ComboBox<>(options);

        try {
            switch (configuration.getShift().toString()) {
                case "A":
                    shift.getSelectionModel().select(0);
                    break;
                case "B":
                    shift.getSelectionModel().select(1);
                    break;
                case "C":
                    shift.getSelectionModel().select(2);
                    break;
            }
        }catch (NullPointerException e){
            shift.getSelectionModel().select(0);
        }

        return shift;
    }

    public void setSimpleView(boolean isSimple) {
        configuration.setSimpleView(isSimple);
        saveConfig();
    }

    public boolean isSimpleView() {
        loadConfig();
        return configuration.isSimpleView();
    }
}
