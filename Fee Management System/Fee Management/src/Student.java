import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Student extends Application 
{
    private Stage primaryStage;
    private TableView<StudentDetail> studentDetailTable = new TableView<>();
    ObservableList<Student> studentList = FXCollections.observableArrayList();
    private int loginid;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeLoginScreen();
    }
    //Login Page.............................................................................
    public void initializeLoginScreen()
    {
        primaryStage.close();
        primaryStage = new Stage();
        primaryStage.setTitle("Fee Management System");
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        Text welcomeText = new Text("Welcome to Fee Management System");
        welcomeText.setFont(Font.font(20));
        root.setTop(welcomeText);
        BorderPane.setAlignment(welcomeText, javafx.geometry.Pos.CENTER);
        GridPane grid = new GridPane();
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Label studentLabel = new Label("Student Login");
        studentLabel.setFont(Font.font(18));
        grid.add(studentLabel, 0, 1);
        Label studentUsernameLabel = new Label("StudentId:");
        grid.add(studentUsernameLabel, 0, 2);
        TextField studentUsernameField = new TextField();
        grid.add(studentUsernameField, 1, 2);
        Label studentPasswordLabel = new Label("Password:");
        grid.add(studentPasswordLabel, 0, 3);
        PasswordField studentPasswordField = new PasswordField();
        grid.add(studentPasswordField, 1, 3);
        Label teacherLabel = new Label("Teacher Login");
        teacherLabel.setFont(Font.font(18));
        grid.add(teacherLabel, 0, 5);
        Label teacherUsernameLabel = new Label("TeacherId:");
        grid.add(teacherUsernameLabel, 0, 6);
        TextField teacherUsernameField = new TextField();
        grid.add(teacherUsernameField, 1, 6);
        Label teacherPasswordLabel = new Label("Password:");
        grid.add(teacherPasswordLabel, 0, 7);
        PasswordField teacherPasswordField = new PasswordField();
        grid.add(teacherPasswordField, 1, 7);
        Button studentLoginButton = new Button("Login as Student");
        studentLoginButton.setStyle("-fx-background-color: green;");
        studentLoginButton.setTextFill(Color.WHITE);
        grid.add(studentLoginButton, 1, 4);
        Button teacherLoginButton = new Button("Login as Teacher");
        teacherLoginButton.setStyle("-fx-background-color: green;");
        teacherLoginButton.setTextFill(Color.WHITE);
        grid.add(teacherLoginButton, 1, 8);
        root.setCenter(grid);
        studentLoginButton.setOnAction(e -> 
        {
            int studentid = Integer.parseInt(studentUsernameField.getText());
            String password = studentPasswordField.getText();
            if (authenticateStudent(studentid, password))
            {
                loginid=studentid;
                createStudentDashboard();
        
            } 
            else 
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Login");
                alert.setContentText("Invalid username or password.");
                alert.showAndWait();
            }
        });
        teacherLoginButton.setOnAction(e -> 
        {
           int teacherid  = Integer.parseInt(teacherUsernameField.getText());
            String password = teacherPasswordField.getText();
            if (authenticateTeacher(teacherid,password)) 
            {
                createTeacherDashboard();
            } 
            else 
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Login");
                alert.setContentText("Invalid username or password.");
                alert.showAndWait();
            }
        });
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    // Authenticate Login Details in the database..................................................
    String dbUrl = "jdbc:mysql://localhost/student";
    String dbUser = "root";
    String dbPassword = "";
    private boolean authenticateStudent(int studentid, String password)
    {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) 
        {
            String query = "SELECT * FROM student_credentials WHERE student_id = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, studentid);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean authenticateTeacher(int teacherid, String password) 
    {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) 
        {
            String query = "SELECT * FROM teacher_credentials WHERE teacher_id = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, teacherid);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Student Dashboard..................................................................
    private void createStudentDashboard() 
    {
        primaryStage.setTitle("Student Dashboard");
        VBox studentDashboard = new VBox(10);
        studentDashboard.setAlignment(Pos.CENTER); 
        Button profileButton = new Button("Profile");
        profileButton.setMinWidth(200); 
        profileButton.setMinHeight(40); 
        profileButton.setStyle("-fx-font-size: 16px;");
        profileButton.setOnAction(e-> openStudentProfile());
        Button feeButton = new Button("Fees");
        feeButton.setMinWidth(200);
        feeButton.setMinHeight(40);
        feeButton.setStyle("-fx-font-size: 16px;");
        feeButton.setOnAction(e ->openStudentFee());
        Button backButton = new Button("Back");
        backButton.setMinWidth(200);
        backButton.setMinHeight(40);
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e ->initializeLoginScreen());
        studentDashboard.getChildren().addAll(profileButton, feeButton,backButton);
        Scene studentScene = new Scene(studentDashboard, 400, 200);
        primaryStage.setScene(studentScene);
    }
    // Teacher Dashboard.....................................................................
    private void createTeacherDashboard() 
    {
        primaryStage.setTitle("Teacher Dashboard");
        VBox teacherDashboard = new VBox(10);
        teacherDashboard.setAlignment(Pos.CENTER);
        Button addStudentButton = new Button("Add New Student");
        addStudentButton.setMinWidth(200);
        addStudentButton.setMinHeight(40);
        addStudentButton.setStyle("-fx-font-size: 16px;");
        addStudentButton.setOnAction(e -> openStudentEntryPage());
        Button editStudentButton = new Button("Edit Student Details");
        editStudentButton.setMinWidth(200);
        editStudentButton.setMinHeight(40);
        editStudentButton.setStyle("-fx-font-size: 16px;");
        Button backButton = new Button("Back");
        backButton.setMinWidth(200);
        backButton.setMinHeight(40);
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> initializeLoginScreen());
        teacherDashboard.getChildren().addAll(addStudentButton, editStudentButton,backButton);
        Scene teacherScene = new Scene(teacherDashboard, 400, 200);
        primaryStage.setScene(teacherScene);
    }
    // Entering Student Details........................................................
    private void openStudentEntryPage() 
    {
        primaryStage.setTitle("Add New Student");
        VBox studentEntryPage = new VBox(10);
        studentEntryPage.setAlignment(Pos.CENTER);
        TextField id = new TextField();
        id.setPromptText("StudentId:");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("Date of Birth");
        TextField contactNumberField = new TextField();
        contactNumberField.setPromptText("Contact Number");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        DatePicker enrollmentDatePicker = new DatePicker();
        enrollmentDatePicker.setPromptText("Enrollment Date");
        Button saveButton = new Button("Save");
        saveButton.setMinWidth(200);
        saveButton.setMinHeight(40);
        saveButton.setStyle("-fx-font-size: 16px;");
        saveButton.setOnAction(e -> saveStudentDetails(id,firstNameField, lastNameField, dobPicker, contactNumberField, emailField, enrollmentDatePicker));
        studentEntryPage.getChildren().addAll(
            id,firstNameField, lastNameField, dobPicker, contactNumberField, emailField, enrollmentDatePicker, saveButton);
        Scene studentEntryScene = new Scene(studentEntryPage, 400, 400);
        primaryStage.setScene(studentEntryScene);
    }
    // Saving Student Details.......................................................
    private void saveStudentDetails(TextField id,TextField firstNameField, TextField lastNameField, DatePicker dobPicker, TextField contactNumberField, TextField emailField, DatePicker enrollmentDatePicker) {
        int stdid =Integer.parseInt(id.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String dob = dobPicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String contactNumber = contactNumberField.getText();
        String email = emailField.getText();
        String enrollmentDate =  enrollmentDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || contactNumber.isEmpty() || email.isEmpty() || enrollmentDate.isEmpty()) {
            showAlert("All fields must be filled.");
            return;
        }
        if (saveStudentToDatabase(stdid,firstName, lastName, dob, contactNumber, email, enrollmentDate)) {
            showSuccess("Student details saved successfully.");
        } else {
            showAlert("Failed to save student details.");
        }
    }
    // Adding New Student details into Database...............................................................
    private boolean saveStudentToDatabase(int stdid,String firstName, String lastName, String dob, String contactNumber, String email, String enrollmentDate) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String sql = "INSERT INTO students (StudentID,FirstName, LastName, DateOfBirth,ContactNumber, Email, EnrollmentDate) VALUES (?,?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, stdid);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, dob);
            statement.setString(5, contactNumber);
            statement.setString(6, email);
            statement.setString(7, enrollmentDate);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Student Profie.........................................................................
    private void openStudentProfile() {
        TableColumn<StudentDetail, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(data -> data.getValue().firstNameProperty());
        TableColumn<StudentDetail, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(data -> data.getValue().lastNameProperty());
        TableColumn<StudentDetail, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(data -> data.getValue().dobProperty());
        TableColumn<StudentDetail, String> contactNumCol = new TableColumn<>("Contact Number");
        contactNumCol.setCellValueFactory(data -> data.getValue().contactNumProperty());
        TableColumn<StudentDetail, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        TableColumn<StudentDetail, String> enrollmentDateCol = new TableColumn<>("Enrollment Date");
        enrollmentDateCol.setCellValueFactory(data -> data.getValue().enrollmentDateProperty());
        studentDetailTable.getColumns().addAll(firstNameCol, lastNameCol, dobCol, contactNumCol, emailCol, enrollmentDateCol);
        List<StudentDetail> studentDetails = fetchStudentDetailsFromDatabase(loginid);
        studentDetailTable.setItems(FXCollections.observableArrayList(studentDetails));
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> createStudentDashboard());
        VBox root = new VBox(10);
        root.getChildren().addAll(studentDetailTable,backButton);
        Scene profilescene = new Scene(root, 600, 400);
        primaryStage.setScene(profilescene);
        primaryStage.show();
    }
    public class StudentDetail {
        private final StringProperty firstName = new SimpleStringProperty();
        private final StringProperty lastName = new SimpleStringProperty();
        private final StringProperty dob = new SimpleStringProperty();
        private final StringProperty contactNum = new SimpleStringProperty();
        private final StringProperty email = new SimpleStringProperty();
        private final StringProperty enrollmentDate = new SimpleStringProperty();
        public StudentDetail(String firstName, String lastName, String dob, String contactNum, String email, String enrollmentDate) {
            this.firstName.set(firstName);
            this.lastName.set(lastName);
            this.dob.set(dob);
            this.contactNum.set(contactNum);
            this.email.set(email);
            this.enrollmentDate.set(enrollmentDate);
        }
        public StringProperty firstNameProperty() {
            return firstName;
        }
        public StringProperty lastNameProperty() {
            return lastName;
        }
        public StringProperty dobProperty() {
            return dob;
        }
         public StringProperty contactNumProperty() {
            return contactNum;
        }
        public StringProperty emailProperty() {
            return email;
        }
        public StringProperty enrollmentDateProperty()
        {
            return enrollmentDate;
        }
    } 
    // Retrive Login Student data from Database......................................
    private List<StudentDetail> fetchStudentDetailsFromDatabase(int loginId) {
        List<StudentDetail> studentDetails = new ArrayList<>();
        String query = "SELECT * FROM students WHERE StudentID = ?"; 
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, loginId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String dob = resultSet.getString("DateOfBirth");
                String contactNum = resultSet.getString("ContactNumber");
                String email = resultSet.getString("Email");
                String enrollmentDate = resultSet.getString("EnrollmentDate");
                StudentDetail student = new StudentDetail(firstName, lastName, dob, contactNum, email, enrollmentDate);
                studentDetails.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while fetching student details.");
        }
        return studentDetails;
    }   
    // Student Fee................................................................................ 
    private void openStudentFee() {
        primaryStage.setTitle("Fee Management");
        VBox studentDashboard = new VBox(10);
        studentDashboard.setAlignment(Pos.CENTER);
        Button feeStructuresButton = new Button("Fee Structures");
        feeStructuresButton.setMinWidth(200);
        feeStructuresButton.setMinHeight(40);
        feeStructuresButton.setStyle("-fx-font-size: 16px;");
        feeStructuresButton.setOnAction(e -> openFeeStructures());
        Button feeBalanceButton = new Button("Fee Balance");
        feeBalanceButton.setMinWidth(200);
        feeBalanceButton.setMinHeight(40);
        feeBalanceButton.setStyle("-fx-font-size: 16px;");
        feeBalanceButton.setOnAction(e -> openFeeBalance());
        Button paymentsButton = new Button("Payments");
        paymentsButton.setMinWidth(200);
        paymentsButton.setMinHeight(40);
        paymentsButton.setStyle("-fx-font-size: 16px;");
        paymentsButton.setOnAction(e -> openPayments());
        Button backButton = new Button("Back");
        backButton.setMinWidth(200);
        backButton.setMinHeight(40);
        backButton.setStyle("-fx-font-size: 16px;");
        backButton.setOnAction(e -> createStudentDashboard());
        studentDashboard.getChildren().addAll(feeStructuresButton, feeBalanceButton, paymentsButton, backButton);
        Scene studentScene = new Scene(studentDashboard, 400, 200);
        primaryStage.setScene(studentScene);
    }
    //Fee Structures................................................
    private void openFeeStructures() {
        primaryStage.setTitle("Fee Structures");    
        Hyperlink bTechLink = new Hyperlink("B.Tech");
        Hyperlink mTechLink = new Hyperlink("M.Tech");
        Hyperlink backLink = new Hyperlink("Back");
        VBox linksBox = new VBox(10);
        linksBox.setAlignment(Pos.CENTER);
        linksBox.getChildren().addAll(bTechLink, mTechLink, backLink);
        bTechLink.setOnAction(e -> displayBTechFeeStructure());
        mTechLink.setOnAction(e -> displayMTechFeeStructure());
        backLink.setOnAction(e -> createStudentDashboard());
        StackPane linksPane = new StackPane(linksBox);
        Scene feeScene = new Scene(linksPane, 400, 400);
        primaryStage.setScene(feeScene);
    }
    // B.Tech Fee Structures.......................................................
    private void displayBTechFeeStructure() {
        List<FeeDetail> bTechFeeDetails = new ArrayList<>();
        bTechFeeDetails.add(new FeeDetail("1st Year", "65,000"));
        bTechFeeDetails.add(new FeeDetail("2nd Year", "65,000"));
        bTechFeeDetails.add(new FeeDetail("3rd Year", "65,000"));
        bTechFeeDetails.add(new FeeDetail("4th Year", "65,000"));
        FeeStructure bTechFeeStructure = new FeeStructure("B.Tech", bTechFeeDetails);
        TableView<FeeDetail> table = new TableView<>();
        TableColumn<FeeDetail, String> yearCol = new TableColumn<>("Year of Study");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("yearOfStudy"));
        TableColumn<FeeDetail, String> feeCol = new TableColumn<>("Fee Amount");
        feeCol.setCellValueFactory(new PropertyValueFactory<>("feeAmount"));
        table.getColumns().addAll(yearCol, feeCol);
        table.setItems(FXCollections.observableArrayList(bTechFeeStructure.getFeeDetails()));
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> openFeeStructures());
        StackPane feeStructurePane = new StackPane(table,backButton);
        Scene bTechFeeScene = new Scene(feeStructurePane, 400, 400);
        primaryStage.setScene(bTechFeeScene);
    }
    public class FeeStructure {
        private String courseName;
        private List<FeeDetail> feeDetails;
        public FeeStructure(String courseName, List<FeeDetail> feeDetails) {
            this.courseName = courseName;
            this.feeDetails = feeDetails;
        }
        public String getCourseName() {
            return courseName;
        }
        public List<FeeDetail> getFeeDetails() {
            return feeDetails;
        }
    }
    // M.Tech Fee Structures...........................................................
    private void displayMTechFeeStructure() {
        List<FeeDetail> mTechFeeDetails = new ArrayList<>();
        mTechFeeDetails.add(new FeeDetail("1st Year", "45,000"));
        mTechFeeDetails.add(new FeeDetail("2nd Year", "45,000"));
        FeeStructure mTechFeeStructure = new FeeStructure("M.Tech", mTechFeeDetails);
        TableView<FeeDetail> table = new TableView<>();
        TableColumn<FeeDetail, String> yearCol = new TableColumn<>("Year of Study");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("yearOfStudy"));
        TableColumn<FeeDetail, String> feeCol = new TableColumn<>("Fee Amount");
        feeCol.setCellValueFactory(new PropertyValueFactory<>("feeAmount"));
        table.getColumns().addAll(yearCol, feeCol);
        table.setItems(FXCollections.observableArrayList(mTechFeeStructure.getFeeDetails()));
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> openFeeStructures());
        VBox feeStructurePane = new VBox(10);
        feeStructurePane.getChildren().addAll(table, backButton);
        Scene mTechFeeScene = new Scene(feeStructurePane, 400, 400);
        primaryStage.setScene(mTechFeeScene);
    }    
    public class FeeDetail {
        private String yearOfStudy;
        private String feeAmount;

        public FeeDetail(String yearOfStudy, String feeAmount) {
            this.yearOfStudy = yearOfStudy;
            this.feeAmount = feeAmount;
        }

        public String getYearOfStudy() {
            return yearOfStudy;
        }

        public String getFeeAmount() {
            return feeAmount;
        }
    }
    private void openFeeBalance()
    {
        primaryStage.setTitle("Fee Balances");    
        Hyperlink bTechLink = new Hyperlink("B.Tech");
        Hyperlink mTechLink = new Hyperlink("M.Tech");
        Hyperlink backLink = new Hyperlink("Back");
        VBox linksBox = new VBox(10);
        linksBox.setAlignment(Pos.CENTER);
        linksBox.getChildren().addAll(bTechLink, mTechLink, backLink);
        bTechLink.setOnAction(e -> displayBTechFeeBalances());
        //mTechLink.setOnAction(e -> displayMTechBalances());
        backLink.setOnAction(e -> createStudentDashboard());
        StackPane linksPane = new StackPane(linksBox);
        Scene feeBalScene = new Scene(linksPane, 400, 400);
        primaryStage.setScene(feeBalScene);
    }
    private void displayBTechFeeBalances() {
        primaryStage.setTitle("Fee Balances for B.Tech");
        List<FeeRecord> bTechFeeRecords = fetchBTechFeeRecords(loginid);
        TableView<FeeRecord> feeBalanceTable = new TableView<>();
        TableColumn<FeeRecord, Integer> firstYearBalanceColumn = new TableColumn<>("1st Year Balance");
        firstYearBalanceColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getFirstYearBalance()).asObject());
        TableColumn<FeeRecord, Integer> secondYearBalanceColumn = new TableColumn<>("2nd Year Balance");
        secondYearBalanceColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getSecondYearBalance()).asObject());
        TableColumn<FeeRecord, Integer> thirdYearBalanceColumn = new TableColumn<>("3rd Year Balance");
        thirdYearBalanceColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getThirdYearBalance()).asObject());
        TableColumn<FeeRecord, Integer> fourthYearBalanceColumn = new TableColumn<>("4th Year Balance");
        fourthYearBalanceColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getFourthYearBalance()).asObject());
        TableColumn<FeeRecord, Integer> totalBalanceColumn = new TableColumn<>("Total Balance");
        totalBalanceColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTotalBalance()).asObject());
        feeBalanceTable.getColumns().addAll(firstYearBalanceColumn, secondYearBalanceColumn, thirdYearBalanceColumn, fourthYearBalanceColumn, totalBalanceColumn);
        feeBalanceTable.setItems(FXCollections.observableArrayList(bTechFeeRecords));
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> openFeeBalance());
        VBox layout = new VBox();
        layout.getChildren().addAll(feeBalanceTable, backButton);
        Scene feeBalanceScene = new Scene(layout, 800, 600);
        primaryStage.setScene(feeBalanceScene);
    }
    
   public List<FeeRecord> fetchBTechFeeRecords(int loginid) {
    List<FeeRecord> bTechFeeRecords = new ArrayList<>();
    String query = "SELECT First_year_fee, Second_year_fee, Third_year_fee, Fourth_year_fee, total_fee FROM btech_fee WHERE std_id = ?";
    try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, loginid);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int firstYearFee = resultSet.getInt("First_year_fee");
            int secondYearFee = resultSet.getInt("Second_year_fee");
            int thirdYearFee = resultSet.getInt("Third_year_fee");
            int fourthYearFee = resultSet.getInt("Fourth_year_fee");
            int totalFee = resultSet.getInt("total_fee");
            FeeRecord bTechFeeRecord = new FeeRecord(
                firstYearFee, 
                secondYearFee, 
                thirdYearFee, 
                fourthYearFee, 
                totalFee
            );
            bTechFeeRecords.add(bTechFeeRecord);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Database Error", "An error occurred while fetching B.Tech fee records.");
    }
    return bTechFeeRecords;
   }
   public class FeeRecord {
    private final SimpleIntegerProperty firstYearFee = new SimpleIntegerProperty();
    private final SimpleIntegerProperty secondYearFee = new SimpleIntegerProperty();
    private final SimpleIntegerProperty thirdYearFee = new SimpleIntegerProperty();
    private final SimpleIntegerProperty fourthYearFee = new SimpleIntegerProperty();
    private final SimpleIntegerProperty totalFee = new SimpleIntegerProperty();

    public FeeRecord(int firstYearFee, int secondYearFee, int thirdYearFee, int fourthYearFee, int totalFee) {
        this.firstYearFee.set(firstYearFee);
        this.secondYearFee.set(secondYearFee);
        this.thirdYearFee.set(thirdYearFee);
        this.fourthYearFee.set(fourthYearFee);
        this.totalFee.set(totalFee);
    }

    public SimpleIntegerProperty firstYearFeeProperty() {
        return firstYearFee;
    }

    public SimpleIntegerProperty secondYearFeeProperty() {
        return secondYearFee;
    }

    public SimpleIntegerProperty thirdYearFeeProperty() {
        return thirdYearFee;
    }

    public SimpleIntegerProperty fourthYearFeeProperty() {
        return fourthYearFee;
    }

    public SimpleIntegerProperty totalFeeProperty() {
        return totalFee;
    }

    public int getFirstYearFee() {
        return firstYearFee.get();
    }

    public int getSecondYearFee() {
        return secondYearFee.get();
    }

    public int getThirdYearFee() {
        return thirdYearFee.get();
    }

    public int getFourthYearFee() {
        return fourthYearFee.get();
    }

    public int getTotalFee() {
        return totalFee.get();
    }

    public int getFirstYearBalance() {
        return 65000 - firstYearFee.get();
    }

    public int getSecondYearBalance() {
        return 65000 - secondYearFee.get();
    }

    public int getThirdYearBalance() {
        return 65000 - thirdYearFee.get();
    }

    public int getFourthYearBalance() {
        return 65000 - fourthYearFee.get();
    }

    public int getTotalBalance() {
        return 260000 - (getFirstYearBalance() + getSecondYearBalance() + getThirdYearBalance() + getFourthYearBalance());
    }
   }
    private void openPayments()
    {

    }
    // Alerts.........................................................................
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showSuccess(String message) 
    {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Success");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
   }
} 


