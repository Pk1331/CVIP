import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BMI Calculator");
        Label weightLabel = new Label("Weight (kg):");
        TextField weightTextField = new TextField();
        Label heightLabel = new Label("Height (m):");
        TextField heightTextField = new TextField();
        Label genderLabel = new Label("Gender:");
        ToggleGroup genderToggleGroup = new ToggleGroup();
        RadioButton maleRadioButton = new RadioButton("Male");
        RadioButton femaleRadioButton = new RadioButton("Female");
        RadioButton otherRadioButton = new RadioButton("Other");
        Button clearButton = new Button("Clear");
        Button calculateButton = new Button("Calculate BMI");
        Label resultLabel = new Label("BMI Result:");
        TextArea resultTextArea = new TextArea();
        resultTextArea.setEditable(false);
        Label errorLabel = new Label(""); 
        maleRadioButton.setToggleGroup(genderToggleGroup);
        femaleRadioButton.setToggleGroup(genderToggleGroup);
        otherRadioButton.setToggleGroup(genderToggleGroup);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.add(weightLabel, 0, 0);
        gridPane.add(weightTextField, 1, 0);
        gridPane.add(heightLabel, 0, 1);
        gridPane.add(heightTextField, 1, 1);
        gridPane.add(genderLabel, 0, 2);
        gridPane.add(maleRadioButton, 1, 2);
        gridPane.add(femaleRadioButton, 1, 3);
        gridPane.add(otherRadioButton, 1, 4);
        gridPane.add(clearButton, 1,5);
        gridPane.add(calculateButton, 2, 5);
        gridPane.add(errorLabel, 0, 6, 2, 1);
        calculateButton.setStyle("-fx-background-color: green;");
        clearButton.setStyle("-fx-background-color: red;");
        VBox resultBox = new VBox(10);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getChildren().addAll(resultLabel, resultTextArea);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setBottom(resultBox);
        calculateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) 
            {
                errorLabel.setText("");
                if (genderToggleGroup.getSelectedToggle() == null) {
                    errorLabel.setText("Please select a gender.");
                    return;
                }
                try
                {
                    double weight = Double.parseDouble(weightTextField.getText());
                    double height = Double.parseDouble(heightTextField.getText());
                    if(weight>=0&& height>=0)
                    {
                       RadioButton selectedGender = (RadioButton) genderToggleGroup.getSelectedToggle();
                       String gender = selectedGender.getText();
                       double bmi = BMICalculation(weight, height);
                       String interpretation = Classification(bmi);
                       resultTextArea.setText("Your BMI is: " + String.format("%.2f", bmi) + "\n" + interpretation);
                    }
                    else
                    {
                        errorLabel.setText("Enter non-negative weights and heights");
                    }
                }
                catch(NumberFormatException x)
                {
                   errorLabel.setText("Please enter valid weight and height");
                }
                
            }
        });
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                weightTextField.clear();
                heightTextField.clear();
                genderToggleGroup.selectToggle(null);
                resultTextArea.clear();
                errorLabel.setText("");
            }
        });
        Scene scene = new Scene(borderPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public double BMICalculation(double weight, double height) {
        return weight / (height * height);
    }

    public String Classification(double bmi) {
        if (bmi < 18.5) {
            return "You are underweight.";
        } else if (bmi >= 18.5 && bmi < 24.9) {
            return "You have a normal weight.";
        } else if (bmi >= 24.9 && bmi < 29.9) {
            return "You are overweight.";
        } else {
            return "You are obese.";
        }
    }
}
