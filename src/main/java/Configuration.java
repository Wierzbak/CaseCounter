import java.util.ArrayList;
import java.util.List;

public class Configuration {

    private List<String> selectedMods = new ArrayList<>();
    private Shift shift;
    private boolean simpleView;

    public List<String> getSelectedMods() {
        return selectedMods;
    }

    public void setSelectedMods(List<String> selectedMods) {
        this.selectedMods = selectedMods;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public boolean isSimpleView() {
        return simpleView;
    }

    public void setSimpleView(boolean simpleView) {
        this.simpleView = simpleView;
    }
}
