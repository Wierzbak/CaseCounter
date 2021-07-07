import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public enum ModeratorService {

    INSTANCE;

    private final File modListFile = new File(ImportReader.getCurrentPath() + "/mods/Mods.txt");

    private Map<String, Moderator> modsMap = new HashMap<>();
    private Map<String, String> nameDictionary = new HashMap<>();

    public Map<String, String> getNameDictionary() {
        return nameDictionary;
    }

    private String getModListAsString() {
        StringBuilder modList = new StringBuilder();
        try {
            Scanner scanner = new Scanner(modListFile);
            while (scanner.hasNext()) {
                modList.append(scanner.nextLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            PopUps.errorStage(e.getLocalizedMessage(), 200, 150);
        }
        return modList.toString();
    }

    public boolean addMod(String mod) {
        StringBuilder modList = new StringBuilder();
        modList.append(getModListAsString())
                .append(mod);
        try {
            FileWriter writer = new FileWriter(modListFile);
            writer.write(modList.toString());
            writer.close();
        } catch (IOException e) {
            PopUps.errorStage(e.getLocalizedMessage(), 200, 150);
            return false;
        }
        return true;
    }

    private List<String> loadMods() {
        try {
            Scanner scanner = new Scanner(modListFile);
            List<String> modStringList = new ArrayList<>();
            while (scanner.hasNext()) {
                modStringList.add(scanner.nextLine());
            }
            scanner.close();
            return modStringList;
        } catch (FileNotFoundException e) {
            PopUps.errorStage("Can't find moderators list!", 200, 100);
            return null;
        }
    }

    public void removeMod(String mod) {
        StringBuilder modList = new StringBuilder();
        try {
            Scanner scanner = new Scanner(modListFile);
            while (scanner.hasNext()) {
                String[] modSplit = scanner.nextLine().split(";");
                if (!modSplit[0].equals(mod)) {
                    modList.append(modSplit[0])
                            .append(";")
                            .append(modSplit[1])
                            .append("\n");
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            PopUps.errorStage(e.getLocalizedMessage(), 200, 150);
        }
        try {
            FileWriter writer = new FileWriter(modListFile);
            writer.write(modList.toString());
            writer.close();
        } catch (IOException e) {
            PopUps.errorStage(e.getLocalizedMessage(), 300, 100);
        }
        initModList();
        rebuildSelectedMods();
    }

    private void rebuildSelectedMods() {
        List<String> selectedMods = ConfigHandler.INSTANCE.getSelectedMods();
        selectedMods.removeIf(mod -> !modsMap.containsKey(mod));
        ConfigHandler.INSTANCE.setSelectedMods(selectedMods);
    }

    public void initModList() {
        List<String> modStringList = loadMods();
        if (modStringList != null) {
            try {
                modsMap = new HashMap<>();
                nameDictionary = new HashMap<>();
                for (String mod : modStringList
                ) {
                    String[] split = mod.split(";");
                    modsMap.put(split[1].toLowerCase(), new Moderator(split[0], split[1].toLowerCase()));
                    nameDictionary.put(split[0], split[1].toLowerCase());
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                PopUps.errorStage("Moderator list file corrupted!", 200, 100);
            }
        }
    }

    public void updateModPerformance() {
        if (!Stages.isFreshlyLaunched) {
            List<List<String>> data = new ImportReader().getData();
            if (data == null) {
                PopUps.errorStage("Can't load all of the queues' export files." +
                        "\nExport missing queue files and click Reload data button.", 325, 100);
            } else {
                for (Map.Entry<String, Moderator> entry : modsMap.entrySet()
                ) {
                    Moderator moderator = entry.getValue();
                    moderator.resetPerformance();
                    modsMap.put(entry.getKey(), moderator);
                }
                updateL1(data.get(0));
                updateL2(data.get(1));
                updateL3(data.get(2));
                updateL4(data.get(3));
                updateUserRate(data.get(4));
            }
        }
    }

    private void updateUserRate(List<String> userRate) {
        for (String modP : userRate
        ) {
            String[] split = modP.split(",");
            if (modsMap.containsKey(split[1])) {
                Moderator mod = modsMap.get(split[1]);
                mod.setUserRateCount(Integer.parseInt(split[2]));
                modsMap.put(split[1], mod);
            }
        }
    }

    private void updateL4(List<String> l4) {
        for (String modP : l4
        ) {
            String[] split = modP.split(",");
            if (modsMap.containsKey(split[1])) {
                Moderator mod = modsMap.get(split[1]);
                mod.setL4count(Integer.parseInt(split[2]));
                modsMap.put(split[1], mod);
            }
        }
    }

    private void updateL3(List<String> l3) {
        for (String modP : l3
        ) {
            String[] split = modP.split(",");
            if (modsMap.containsKey(split[1])) {
                Moderator mod = modsMap.get(split[1]);
                mod.setL3count(Integer.parseInt(split[2]));
                modsMap.put(split[1], mod);
            }
        }
    }

    private void updateL2(List<String> l2) {
        for (String modP : l2
        ) {
            String[] split = modP.split(",");
            if (modsMap.containsKey(split[1])) {
                Moderator mod = modsMap.get(split[1]);
                mod.setL2count(Integer.parseInt(split[2]));
                modsMap.put(split[1], mod);
            }
        }
    }

    private void updateL1(List<String> l1) {
        for (String modP : l1
        ) {
            String[] split = modP.split(",");
            if (modsMap.containsKey(split[1])) {
                Moderator mod = modsMap.get(split[1]);
                mod.setL1count(Integer.parseInt(split[2]));
                modsMap.put(split[1], mod);
            }
        }
    }

    public ObservableList<Moderator> getMods() {
        updateModPerformance();
        ObservableList<Moderator> list = FXCollections.observableArrayList();
        for (String mod : ConfigHandler.INSTANCE.getSelectedMods()
        ) {
            list.add(modsMap.get(mod));
        }
        return list;
    }
}
