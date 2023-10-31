import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class NumberGuessingGame extends Application {
    private int targetNumber;
    private int attempts;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Number Guessing Game");

        Label label = new Label("Guess the number between 1 and 100:");
        label.getStyleClass().add("label");

        TextField guessField = new TextField();
        guessField.getStyleClass().add("text-field");

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("button");

        Label feedbackLabel = new Label();
        feedbackLabel.getStyleClass().add("label");

        submitButton.setOnAction(event -> {
            int userGuess = Integer.parseInt(guessField.getText());
            attempts++;
            if (userGuess < targetNumber) {
                feedbackLabel.setText("Too low!");
            } else if (userGuess > targetNumber) {
                feedbackLabel.setText("Too high!");
            } else {
                feedbackLabel.setText("Congratulations! You guessed it in " + attempts + " attempts.");
            }
        });
        VBox root = new VBox(label, guessField, submitButton, feedbackLabel);
        root.setSpacing(15);
        root.setAlignment(Pos.CENTER);
        targetNumber = (int) (Math.random() * 100) + 1; 
        attempts = 0;
        Scene scene = new Scene(root, 600, 300);
        scene.getStylesheets().add("styles.css"); 
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
