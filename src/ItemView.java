import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;

public class ItemView {
    public static void showDialog(Stage owner, Item item) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Item Details");

        VBox content = new VBox(10); // Reduce spacing between elements
        content.setPadding(new Insets(32, 32, 16, 32)); // Reduce bottom padding
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #fff; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #22222222, 16, 0.3, 0, 4);");

        Label nameLabel = new Label(item.getName());
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        nameLabel.setTextFill(Color.web("#222"));

        Label priceLabel = new Label("Price: " + item.getPrice());
        priceLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 18));
        priceLabel.setTextFill(Color.web("#5A7863"));

        Label descLabel = new Label(item.getDescription());
        descLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        descLabel.setTextFill(Color.web("#444"));
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(320);

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #5A7863; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 32; -fx-font-weight: bold; -fx-cursor: hand;");
        VBox.setMargin(closeBtn, new Insets(12, 0, 0, 0));
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: #466052; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 32; -fx-font-weight: bold; -fx-cursor: hand;"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: #5A7863; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 32; -fx-font-weight: bold; -fx-cursor: hand;"));
        closeBtn.setOnAction(e -> dialogStage.close());

        content.getChildren().addAll(nameLabel, priceLabel, descLabel, closeBtn);
        Scene scene = new Scene(content, 400, 320);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}
