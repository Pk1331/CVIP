import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ATM extends Application 
{
    private TextField cardNumberField;
    private TextField display;
    public Stage primaryStage;
    public static void main(String[] args) 
    {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) 
    {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("ATM Interface");
        Region root = createATMLayout();
        Scene scene = new Scene(root, 400, 500);
        String cssFile = getClass().getResource("atm-style.css").toExternalForm();
        scene.getStylesheets().add(cssFile); 
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private Region createATMLayout() 
    {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Label cardLabel = new Label("Card Number");
        cardLabel.getStyleClass().add("label");
        gridPane.add(cardLabel, 0, 0, 3, 1);
        cardNumberField = new TextField();
        cardNumberField.setPromptText("XXXX-XXXX-XXXX-1234");
        cardNumberField.getStyleClass().add("card-field");
        gridPane.add(cardNumberField, 0, 1, 3, 1);
        Label pinLabel = new Label("Enter the PIN number");
        pinLabel.getStyleClass().add("label");
        gridPane.add(pinLabel, 0, 2, 3, 1);
        display = new TextField();
        display.setPromptText("Enter PIN");
        display.getStyleClass().add("display");
        gridPane.add(display, 0, 3, 3, 1);
        String[] buttonLabels = 
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "Clear", "0", "Enter"
        };
        int row = 4;
        int col = 0;
        for (String label : buttonLabels) {
            Button button = new Button(label);
            button.getStyleClass().add("button");
            button.setOnAction(e -> handleLogin());

            int colSpan = 1;
            if ("Clear".equals(label)) {
                colSpan = 2;
            } else if ("Enter".equals(label)) {
                colSpan = 3;
            }

            gridPane.add(button, col, row, colSpan, 1);
            col += colSpan;
            if (col > 2) {
                col = 0;
                row++;
            }
        }
        return gridPane;
    }
    private void handleLogin() 
    {
        int cardnumber = Integer.parseInt(cardNumberField.getText());
        int pin = Integer.parseInt(display.getText());
        if (validateCardNumberAndPIN(cardnumber, pin)) 
        {
            openDashboard();
        } 
        else 
        {
            showAlert("Invalid Card Number or PIN");
        }
    }
    String dbUrl = "jdbc:mysql://localhost/atm";
    String dbUser = "root";
    String dbPassword = "";
    private boolean validateCardNumberAndPIN(int cardNumber, int pin) 
    {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) 
        {
            String query = "SELECT * FROM ATM_Pins WHERE Card_Num = ? AND Pin = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cardNumber);
            statement.setInt(2, pin);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }
    }
    private void openDashboard() 
    {
       int cardNumber = Integer.parseInt(cardNumberField.getText());
       String name = fetchNameFromDatabase(cardNumber);
       Label welcomeLabel = new Label("Welcome to " + name);
       welcomeLabel.setStyle("-fx-text-fill: black; -fx-font-size: 16;");
       GridPane dashboardLayout = new GridPane();
       dashboardLayout.getStyleClass().add("dashboard-layout");
       dashboardLayout.setAlignment(Pos.CENTER);
       dashboardLayout.setHgap(10);
       dashboardLayout.setVgap(10);
       VBox buttonContainer = new VBox(10);
       buttonContainer.setAlignment(Pos.CENTER);
       Button CurrentButton = new Button("Current Account");
       Button savingAccountButton = new Button("Saving Account");
       CurrentButton.getStyleClass().add("dashboard-button");
       savingAccountButton.getStyleClass().add("dashboard-button");
       CurrentButton.setOnAction(e -> handleCurrent());
       savingAccountButton.setOnAction(e -> handleSavingAccount());
       buttonContainer.getChildren().addAll(welcomeLabel,CurrentButton,savingAccountButton);
       dashboardLayout.add(buttonContainer, 0, 0);
       Scene dashboardScene = new Scene(dashboardLayout, 400, 500);
       String cssFile1 = getClass().getResource("dashboard-style.css").toExternalForm();
       dashboardScene.getStylesheets().add(cssFile1);
       primaryStage.setScene(dashboardScene);
    }
    private String fetchNameFromDatabase(int cardNumber) 
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try 
        {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            String sqlQuery = "SELECT name FROM accholder WHERE acc_no = ?";
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, cardNumber);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return resultSet.getString("name");
            } 
            else
            {  
                return null;
            }
        } 
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }     
    }
    private void handleCurrent() 
    {
        GridPane currentLayout = new GridPane();
        currentLayout.getStyleClass().add("dashboard-layout");
        currentLayout.setAlignment(Pos.CENTER);
        currentLayout.setHgap(10);
        currentLayout.setVgap(10);
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        Button balanceButton = new Button("Balance");
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        balanceButton.getStyleClass().add("dashboard-button");
        depositButton.getStyleClass().add("dashboard-button");
        withdrawButton.getStyleClass().add("dashboard-button");
        balanceButton.setOnAction(e -> open_Current_Acc_Balance());
        depositButton.setOnAction(e -> open_Current_Acc_Deposit());
        withdrawButton.setOnAction(e -> open_Current_Acc_Withdraw());
        buttonContainer.getChildren().addAll(balanceButton, depositButton, withdrawButton);
        currentLayout.add(buttonContainer, 0, 0);
        Scene currentScene = new Scene(currentLayout, 400, 500);
        String cssFile1 = getClass().getResource("dashboard-style.css").toExternalForm();
        currentScene.getStylesheets().add(cssFile1);
        primaryStage.setScene(currentScene);
    }
    private void open_Current_Acc_Balance()
    {
        GridPane balanceDashboardLayout = new GridPane();
        double balance = fetchCurrentAccountBalance();
        Label balanceLabel = new Label("Available Balance:");
        Label amountLabel = new Label(String.format(" %.2f", balance));
        balanceLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        amountLabel.setStyle("-fx-text-fill: red;");
        balanceDashboardLayout.add(balanceLabel, 0, 0);
        balanceDashboardLayout.add(amountLabel, 1, 0);
        GridPane.setHalignment(balanceLabel, HPos.CENTER);
        GridPane.setHalignment(amountLabel, HPos.CENTER);
        Scene balanceDashboardScene = new Scene(balanceDashboardLayout, 400, 200);
        primaryStage.setScene(balanceDashboardScene);
    }
    private double fetchCurrentAccountBalance() 
    {
        int cardNumber = Integer.parseInt(cardNumberField.getText());
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT balance FROM current_account WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } 
            else 
            {
                return 0.0; 
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return 0.0; 
        }
    }
    private void open_Current_Acc_Deposit() 
    {
        GridPane depositDashboardLayout = new GridPane();
        depositDashboardLayout.getStyleClass().add("dashboard-layout");
        depositDashboardLayout.setAlignment(Pos.CENTER);
        depositDashboardLayout.setHgap(10);
        depositDashboardLayout.setVgap(10);
        Label enterAmountLabel = new Label("Enter deposit amount:");
        enterAmountLabel.getStyleClass().add("label");
        depositDashboardLayout.add(enterAmountLabel, 0, 0, 2, 1);
        TextField depositAmountField = new TextField();
        depositAmountField.getStyleClass().add("textfield");
        depositDashboardLayout.add(depositAmountField, 0, 1, 2, 1);
        Button depositButton = new Button("Deposit");
        depositButton.getStyleClass().add("green-button");
        depositButton.setOnAction(e -> handleDeposit(depositAmountField.getText()));
        depositDashboardLayout.add(depositButton, 0, 2, 2, 1);
        Scene depositDashboardScene = new Scene(depositDashboardLayout, 400, 200);
        String cssFile1 = getClass().getResource("dashboard-style.css").toExternalForm();
        depositDashboardScene.getStylesheets().add(cssFile1);
        primaryStage.setScene(depositDashboardScene);
    }
    private void handleDeposit(String deposit) 
    {
        double depositAmount = Double.parseDouble(deposit);
        int cardNumber = Integer.parseInt(cardNumberField.getText());
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String updateQuery = "UPDATE current_account SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setDouble(1, depositAmount);
            statement.setInt(2, cardNumber);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Amount deposited: " + depositAmount);
            } else {
                showAlert("Deposit failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("An error occurred while processing your deposit.");
        }
    }
    private void open_Current_Acc_Withdraw() {
        GridPane withdrawDashboardLayout = new GridPane();
        withdrawDashboardLayout.getStyleClass().add("dashboard-layout");
        withdrawDashboardLayout.setAlignment(Pos.CENTER);
        withdrawDashboardLayout.setHgap(10);
        withdrawDashboardLayout.setVgap(10);
        Label enterAmountLabel = new Label("Enter withdrawal amount:");
        enterAmountLabel.getStyleClass().add("label");
        withdrawDashboardLayout.add(enterAmountLabel, 0, 0, 2, 1);
        TextField withdrawAmountField = new TextField();
        withdrawAmountField.getStyleClass().add("textfield");
        withdrawDashboardLayout.add(withdrawAmountField, 0, 1, 2, 1);
        Button withdrawButton = new Button("Withdraw");
        withdrawButton.getStyleClass().add("green-button");
        withdrawButton.setOnAction(e -> handleWithdraw(withdrawAmountField.getText())); // Handle withdrawal
        withdrawDashboardLayout.add(withdrawButton, 0, 2, 2, 1);
        Scene withdrawDashboardScene = new Scene(withdrawDashboardLayout, 400, 200);
        String cssFile1 = getClass().getResource("dashboard-style.css").toExternalForm();
        withdrawDashboardScene.getStylesheets().add(cssFile1);
        primaryStage.setScene(withdrawDashboardScene);
    }
    private void handleWithdraw(String withdrawalAmount) {
        double withdrawal = Double.parseDouble(withdrawalAmount);
        int cardNumber = Integer.parseInt(cardNumberField.getText());
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String updateQuery = "UPDATE current_account SET balance = balance - ? WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setDouble(1, withdrawal);
            statement.setInt(2, cardNumber);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Amount withdrawn: " + withdrawal);
            } else {
                showAlert("Withdrawal failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("An error occurred while processing your withdrawal.");
        }
    }
    private void handleSavingAccount() {
        GridPane SavingLayout = new GridPane();
        SavingLayout.getStyleClass().add("dashboard-layout");
        SavingLayout.setAlignment(Pos.CENTER);
        SavingLayout.setHgap(10);
        SavingLayout.setVgap(10);
        VBox buttonContainer = new VBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        Button balanceButton1 = new Button("Balance");
        Button depositButton1 = new Button("Deposit");
        Button withdrawButton1 = new Button("Withdraw");
        balanceButton1.getStyleClass().add("dashboard-button");
        depositButton1.getStyleClass().add("dashboard-button");
        withdrawButton1.getStyleClass().add("dashboard-button");
        balanceButton1.setOnAction(e -> open_Savings_Acc_Balance());
        depositButton1.setOnAction(e -> open_Savings_Acc_Deposit());
        withdrawButton1.setOnAction(e -> open_Savings_Acc_Withdraw());
        buttonContainer.getChildren().addAll(balanceButton1, depositButton1, withdrawButton1);
        SavingLayout.add(buttonContainer, 0, 0);
        Scene currentScene = new Scene(SavingLayout, 400, 500);
        String cssFile1 = getClass().getResource("dashboard-style.css").toExternalForm();
        currentScene.getStylesheets().add(cssFile1);
    
        primaryStage.setScene(currentScene);
    }
    private void open_Savings_Acc_Balance() {
        GridPane balanceDashboardLayout = new GridPane();
       balanceDashboardLayout .getStyleClass().add("dashboard-layout");
        double balance = fetchSavingsAccountBalance();
        Label balanceLabel = new Label("Available Balance:");
        Label amountLabel = new Label(String.format(" %.2f", balance));
        balanceLabel.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");
        amountLabel.setStyle("-fx-text-fill: red;");
        balanceDashboardLayout.add(balanceLabel, 0, 0);
        balanceDashboardLayout.add(amountLabel, 1, 0);
        GridPane.setHalignment(balanceLabel, HPos.CENTER);
        GridPane.setHalignment(amountLabel, HPos.CENTER);
        Scene balanceDashboardScene = new Scene(balanceDashboardLayout, 400, 200);
        primaryStage.setScene(balanceDashboardScene);
    }
    private double fetchSavingsAccountBalance() {
        int cardNumber = Integer.parseInt(cardNumberField.getText());
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT balance FROM savings_account WHERE acc_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } 
            else 
            {
                return 0.0; 
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            return 0.0; 
        }
    }
    private void open_Savings_Acc_Deposit()
    {
        GridPane depositDashboardLayout = new GridPane(); 
        depositDashboardLayout.getStyleClass().add("dashboard-layout");
        depositDashboardLayout.setAlignment(Pos.CENTER);
        depositDashboardLayout.setHgap(10);
        depositDashboardLayout.setVgap(10);
        Label enterAmountLabel = new Label("Enter deposit amount:");
        enterAmountLabel.getStyleClass().add("label");
        depositDashboardLayout.add(enterAmountLabel, 0, 0, 2, 1);
        TextField depositAmountField = new TextField();
        depositAmountField.getStyleClass().add("textfield");
        depositDashboardLayout.add(depositAmountField, 0, 1, 2, 1);
        Button depositButton = new Button("Deposit");
        depositButton.getStyleClass().add("green-button");
        depositButton.setOnAction(e -> handleSavingsDeposit(depositAmountField.getText()));
        depositDashboardLayout.add(depositButton, 0, 2, 2, 1);
        Scene depositDashboardScene = new Scene(depositDashboardLayout, 400, 200);
        String cssFile1 = getClass().getResource("dashboard-style.css").toExternalForm();
        depositDashboardScene.getStylesheets().add(cssFile1);
        primaryStage.setScene(depositDashboardScene);
    }
    private void handleSavingsDeposit(String deposited) 
    {
        double depositAmount = Double.parseDouble(deposited);
        int cardNumber = Integer.parseInt(cardNumberField.getText());
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String updateQuery = "UPDATE savings_account SET balance = balance + ? WHERE acc_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setDouble(1, depositAmount);
            statement.setInt(2, cardNumber);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Amount deposited: " + depositAmount);
            } else {
                showAlert("Deposit failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("An error occurred while processing your deposit.");
        }
    } 
    private void open_Savings_Acc_Withdraw(){
        GridPane withdrawDashboardLayout = new GridPane();
        withdrawDashboardLayout.getStyleClass().add("dashboard-layout");
        withdrawDashboardLayout.setAlignment(Pos.CENTER);
        withdrawDashboardLayout.setHgap(10);
        withdrawDashboardLayout.setVgap(10);
        Label enterAmountLabel = new Label("Enter withdrawal amount:");
        enterAmountLabel.getStyleClass().add("label");
        withdrawDashboardLayout.add(enterAmountLabel, 0, 0, 2, 1);
        TextField withdrawAmountField = new TextField();
        withdrawAmountField.getStyleClass().add("textfield");
        withdrawDashboardLayout.add(withdrawAmountField, 0, 1, 2, 1);
        Button withdrawButton = new Button("Withdraw");
        withdrawButton.getStyleClass().add("green-button");
        withdrawButton.setOnAction(e -> handleSavingsWithdraw(withdrawAmountField.getText())); // Handle withdrawal
        withdrawDashboardLayout.add(withdrawButton, 0, 2, 2, 1);
        Scene withdrawDashboardScene = new Scene(withdrawDashboardLayout, 400, 200);
        String cssFile1 = getClass().getResource("dashboard-style.css").toExternalForm();
        withdrawDashboardScene.getStylesheets().add(cssFile1);
        primaryStage.setScene(withdrawDashboardScene);
    }
    private void handleSavingsWithdraw(String withdrawalAmount) {
        double withdrawal = Double.parseDouble(withdrawalAmount);
        int cardNumber = Integer.parseInt(cardNumberField.getText());
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String updateQuery = "UPDATE current_account SET balance = balance - ? WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setDouble(1, withdrawal);
            statement.setInt(2, cardNumber);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Amount withdrawn: " + withdrawal);
            } else {
                showAlert("Withdrawal failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("An error occurred while processing your withdrawal.");
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
