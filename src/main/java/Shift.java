import java.time.LocalTime;

public class Shift {

    private LocalTime shiftStart;
    private String shiftName;

    public Shift(String shift) {
        switch (shift) {
            case "A":
                shiftStart = LocalTime.of(8, 0);
                shiftName = "A";
                break;
            case "B":
                shiftStart = LocalTime.of(16, 0);
                shiftName = "B";

                break;
            case "C":
                shiftStart = LocalTime.of(0, 0);
                shiftName = "C";

                break;
        }
    }

    public double getWorkTime() {
        double workTime = calculateWorkTime();
        return workTime > 8 || workTime < 0 ? 8 : workTime;
    }

    public boolean isShiftCorrect() {
        double workTime = calculateWorkTime();
        return (workTime <= 9) && (workTime >= 0);
    }

    private double calculateWorkTime(){
        return (double) (LocalTime.now().toSecondOfDay() - shiftStart.toSecondOfDay()) / 3600;
    }

    @Override
    public String toString() {
        return shiftName;
    }
}
