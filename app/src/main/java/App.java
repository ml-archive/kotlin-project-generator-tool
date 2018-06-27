import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL mainUrl = getClass().getClassLoader().getResource("main.fxml");

        if (mainUrl == null) {
            showErrorDialog();
            return;
        }

        Scene scene = new Scene(FXMLLoader.load(mainUrl));

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error locating resource");
        alert.setHeaderText(null);
        alert.setContentText("Unable to locate main.fxml resource");

        alert.showAndWait();

        exitApp();
    }

    private void exitApp() {
        Platform.exit();
        System.exit(0);
    }
}
