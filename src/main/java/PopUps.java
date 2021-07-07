import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUps {

    public static void errorStage(String msg, int stageWidth, int stageHeight) {
        Stage stage = new Stage();
        stage.setTitle("Error");
        popUpSetUp(msg, stageWidth, stageHeight, stage);
    }

    public static void confirmationStage(String msg, int stageWidth, int stageHeight) {
        Stage stage = new Stage();
        stage.setTitle("Success!");
        popUpSetUp(msg, stageWidth, stageHeight, stage);
    }

    public static void warningStage(String msg, int stageWidth, int stageHeight) {
        Stage stage = new Stage();
        stage.setTitle("Warning");
        popUpSetUp(msg, stageWidth, stageHeight, stage);
    }

    private static void popUpSetUp(String msg, int stageWidth, int stageHeight, Stage stage) {
        stage.initModality(Modality.APPLICATION_MODAL);

        Button ok = new Button("OK");
        ok.setOnAction(a -> stage.close());
        HBox hBox = new HBox();
        hBox.getChildren().add(ok);
        hBox.alignmentProperty().setValue(Pos.CENTER);
        Label label = new Label(msg);
        BorderPane layout = new BorderPane(label,null,null,hBox,null);
        layout.setPadding(new Insets(10,10,10,10));
        Scene scene = new Scene(layout, stageWidth, stageHeight);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
