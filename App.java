package decisiontree;

import support.decisiontree.PaneOrganizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main App class. Do not edit.
 *
 */
public class App extends Application {

	@Override
	public void start(Stage stage) {
		PaneOrganizer po = new PaneOrganizer(new MyID3());
		Scene scene = new Scene(po.getRoot());
		stage.setScene(scene);
		stage.setTitle("Decision Tree");
		stage.sizeToScene();
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
