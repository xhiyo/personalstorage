    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.TextAlignment;
    import javafx.geometry.Pos;
    import javafx.stage.Stage;
    import javafx.scene.control.Label;
    import javafx.geometry.Insets;
    import javafx.scene.control.TextField;
    import javafx.scene.control.PasswordField;
    import javafx.scene.control.Alert;
    import javafx.scene.control.Alert.AlertType;
    import javafx.scene.layout.StackPane;

public class loginView {

    private Stage stage;

    public loginView(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {

        Label label = new Label("Welcome Back Fabian,");
        label.setStyle("-fx-font-size: 20px;" +"-fx-font-weight: bold; -fx-text-fill: #5A7863;");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #5A7863");
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setMaxWidth(250);
        usernameField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #5A7863 transparent; -fx-border-width: 0 0 2 0; -fx-padding: 6 0 6 0;");
        
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14px;-fx-text-fill: #5A7863");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setMaxWidth(250);
        passwordField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent transparent #5A7863 transparent; -fx-border-width: 0 0 2 0; -fx-padding: 6 0 6 0;");
        
        Button btnLogin = new Button("LOGIN");
        btnLogin.setStyle("-fx-font-size: 14px; -fx-padding: 8 30 8 30; -fx-background-color: #5A7863; -fx-text-fill: white;");
        btnLogin.setTranslateY(9);
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle("-fx-font-size: 14px; -fx-padding: 8 30 8 30; -fx-background-color: #5a7863bb; -fx-text-fill: white;"));
        btnLogin.setOnMouseExited(e -> btnLogin.setStyle("-fx-font-size: 14px; -fx-padding: 8 30 8 30; -fx-background-color: #5A7863; -fx-text-fill: white;"));

        
        usernameField.setOnAction(e -> btnLogin.fire());
        passwordField.setOnAction(e -> btnLogin.fire());

        btnLogin.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username.equals("xhiyo") && password.equals("bian123#")) {
                homeView home = new homeView(stage);
                stage.setScene(home.getScene());
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("Incorrect username or password. Please try again.");
                alert.showAndWait();
                passwordField.clear();
            }
        });
        
        VBox loginBox = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, btnLogin);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setStyle("");
        loginBox.setMaxWidth(600);

        VBox contentBox = new VBox(30, label, loginBox);
        contentBox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(contentBox);
        root.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(root, 650, 500);

        stage.maximizedProperty().addListener((obs, oldVal, isMaximized) -> {
            if (isMaximized) {
                label.setStyle("-fx-font-size: 35px; -fx-font-weight: bold; -fx-text-fill: #5A7863;");
                label.setText("Welcome Back, Fabian");
                StackPane.setAlignment(contentBox, Pos.TOP_CENTER);
                contentBox.setAlignment(Pos.TOP_CENTER);
                contentBox.setPadding(new Insets(300, 0, 0, 0));
                loginBox.setMaxWidth(600);
            } else {
                StackPane.setAlignment(contentBox, Pos.CENTER);
                contentBox.setAlignment(Pos.CENTER);
                contentBox.setPadding(new Insets(0)) ;
                loginBox.setMaxWidth(600);
                label.setText("Welcome Back, Fabian");
                label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #5A7863;");
            }
        });

        return scene;
    }
}