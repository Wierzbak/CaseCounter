import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import org.apache.commons.io.FileUtils;

public class ImportReader {

    private final File[] files = {
            new File(getCurrentPath() + "/import/2-Content Quality Label 1-PL-4.0.csv"),
            new File(getCurrentPath() + "/import/2-Content Quality Label 2-PL-4.0.csv"),
            new File(getCurrentPath() + "/import/2-Content Quality Label 1-PL-commerce-4.0.csv"),
            new File(getCurrentPath() + "/import/2-Content Quality Label-PL-Global-4.0.csv"),
            new File(getCurrentPath() + "/import/2-User Rating-PL.csv"),
    };

    private List<Scanner> getScanners() {
        List<Scanner> scanners = new ArrayList<>(5);
        for (File queue : files) {
            try {
                scanners.add(new Scanner(queue));
            } catch (FileNotFoundException e) {
                PopUps.errorStage("Can't find queue export file:\n" + queue.getName(), 300, 100);
            }
        }
        return scanners;
    }

    public static String getCurrentPath() {
        try {
            String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            return jarPath.substring(1, jarPath.lastIndexOf('/'));
        } catch (URISyntaxException e) {
            PopUps.errorStage(e.getMessage(), 600, 200);
        }
        return null;
    }

    public void deleteFiles() {
        try {
            FileUtils.cleanDirectory(new File(getCurrentPath() + "/import"));
        } catch (IOException e) {
            PopUps.errorStage(e.getLocalizedMessage(),300, 100);
        }
    }

    public List<List<String>> getData() {
        List<Scanner> scanners = getScanners();
        List<List<String>> list = new ArrayList<>();
        for (Scanner scanner : scanners
        ) {
            list.add(getQueueAsList(scanner));
            scanner.close();
        }
        if (list.size() < 5) {
            return null;
        }
        return list;
    }

    private List<String> getQueueAsList(Scanner scanner) {
        List<String> queue = new ArrayList<>(10);
        scanner.nextLine();
        while (scanner.hasNext()) {
            queue.add(scanner.nextLine());
        }
        return queue;
    }
}
