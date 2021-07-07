import java.text.DecimalFormat;

public class Moderator {
    private final String name;
    private final String mail;
    private int L1count;
    private int L2count;
    private int L3count;
    private int L4count;
    private int userRateCount;

    private final double userRateMultiplier = 4.0;

    public void setL1count(int l1count) {
        L1count = l1count;
    }

    public void setL2count(int l2count) {
        L2count = l2count;
    }

    public void setL3count(int l3count) {
        L3count = l3count;
    }

    public void setL4count(int l4count) {
        L4count = l4count;
    }

    public void setUserRateCount(int userRateCount) {
        this.userRateCount = userRateCount;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public int getL1count() {
        return L1count;
    }

    public int getL2count() {
        return L2count;
    }

    public int getL3count() {
        return L3count;
    }

    public int getL4count() {
        return L4count;
    }

    public int getUserRateCount() {
        return userRateCount;
    }

    public int getSummaryScore() {
        return (int) (L1count + L2count + L3count + L4count + (userRateMultiplier * userRateCount));
    }

    public String getL1percentage() {
        if (L1count != 0) {
            return new DecimalFormat("##.##").format((double)L1count*100/getSummaryScore()) + "%";
        } else return "0%";
    }

    public String getL2percentage() {
        if (L2count != 0) {
            return new DecimalFormat("##.##").format((double)L2count*100/getSummaryScore()) + "%";
        } else return "0%";
    }

    public String getL3percentage() {
        if (L3count != 0) {
            return new DecimalFormat("##.##").format((double)L3count*100/getSummaryScore()) + "%";
        } else return "0%";
    }

    public String getL4percentage() {
        if (L4count != 0) {
            return new DecimalFormat("##.##").format((double)L4count*100/getSummaryScore()) + "%";
        } else return "0%";
    }

    public String getUserRatePercentage() {
        if (userRateCount != 0) {
            return new DecimalFormat("##.##").format((double)userRateCount*100*userRateMultiplier/getSummaryScore()) + "%";
        } else return "0%";
    }

    public String getReasonToValidate() {
        if (getSummaryScore() < 950) {
            return "yes";
        } else {
            return  "no";
        }
    }

    public int getAvgHourly() {
        try {
            return (int) Math.round((double) getSummaryScore() / ConfigHandler.INSTANCE.getShift().getWorkTime());
        } catch (NullPointerException e){
            return (int) Math.round((double) getSummaryScore() / 8);
        }
    }

    public Moderator(String name, String mail) {
        this.name = name;
        this.mail = mail;
        this.L1count = 0;
        this.L2count = 0;
        this.L3count = 0;
        this.L4count = 0;
        this.userRateCount = 0;
    }

    public void resetPerformance() {
        L1count = 0;
        L2count = 0;
        L3count = 0;
        L4count = 0;
        userRateCount = 0;
    }

    @Override
    public String toString() {
        return name + ", " + mail + ", " + L1count + ", " + L2count + ", " + L3count + ", " + L4count + ", " + userRateCount;
    }
}
