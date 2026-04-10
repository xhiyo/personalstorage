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
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.shape.SVGPath;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.SnapshotParameters;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.TextArea;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class homeView {
    private Stage stage;
    private ObservableList<Item> items = FXCollections.observableArrayList();
    private FlowPane itemsPane = new FlowPane();
    private ImageView imageView = new ImageView();
    private TextField nameField = new TextField();
    private String photoPath = "";
    private Label itemCountLabel = new Label();

    public homeView(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        // Header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 20, 10, 20));
        header.setStyle("-fx-background-color: transparent;");
        // Icon (simple SVG box)
        SVGPath icon = new SVGPath();
        icon.setContent("M4 4h16v16H4z");
        icon.setStyle("-fx-fill: #5A7863; -fx-scale-x: 1.5; -fx-scale-y: 1.5;");
        Label appName = new Label("Personal Storage");
        appName.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222;");
        itemCountLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #888;");
        updateItemCount();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button addItemBtn = new Button("+ Add Item");
        addItemBtn.setStyle("-fx-background-color: #5A7863; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;");
        addItemBtn.setOnMouseEntered(e -> addItemBtn.setStyle("-fx-background-color: #466052; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
        addItemBtn.setOnMouseExited(e -> addItemBtn.setStyle("-fx-background-color: #5A7863; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
        addItemBtn.setOnAction(e -> showAddItemDialog());
        header.getChildren().addAll(icon, appName, itemCountLabel, spacer, addItemBtn);

        // Card grid
        itemsPane.setHgap(30);
        itemsPane.setVgap(30);
        itemsPane.setPrefWrapLength(900);
        itemsPane.setPadding(new Insets(10, 20, 10, 20));
        itemsPane.setStyle("-fx-background-color: transparent;");
        refreshItemsPane();
        ScrollPane scrollPane = new ScrollPane(itemsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        VBox root = new VBox(header, scrollPane);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #F5F8F6; -fx-background-image: url('file:/C:/Users/Fabian/Downloads/projectBian/Calming%20green%20gradient%20background.png'); -fx-background-size: cover; -fx-background-repeat: no-repeat;");
        Scene scene = new Scene(root, 1100, 700);
        return scene;
    }

    private void updateItemCount() {
        itemCountLabel.setText(items.size() + " items stored");
    }

 
    private void refreshItemsPane() {
        itemsPane.getChildren().clear();
        try {
            items.setAll(getAllItems());
            for (Item item : items) {
                itemsPane.getChildren().add(createCard(item));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        updateItemCount();
    }

    private VBox createCard(Item item) {
        final StackPane imagePane = new StackPane();
        imagePane.setPrefSize(180, 180);
        imagePane.setAlignment(Pos.CENTER);
        imagePane.setStyle("-fx-background-color: #fff; -fx-background-radius: 20;");
        Color normalGray = Color.web("#ffffff");
        Color hoverGray = Color.web("#e0e0e0");

        // Product image (centered in a square)
        Image originalImage = new Image(item.getItemPhoto());
        double size = 180;
        WritableImage squareImage = new WritableImage((int)size, (int)size);
        PixelWriter pw = squareImage.getPixelWriter();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                pw.setColor(x, y, normalGray);
            }
        }
        double imgW = originalImage.getWidth();
        double imgH = originalImage.getHeight();
        double scale = Math.min(size / imgW, size / imgH);
        double newW = imgW * scale;
        double newH = imgH * scale;
        double offsetX = (size - newW) / 2;
        double offsetY = (size - newH) / 2;
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        ImageView tempView = new ImageView(originalImage);
        tempView.setFitWidth(newW);
        tempView.setFitHeight(newH);
        tempView.setPreserveRatio(true);
        WritableImage tempImg = tempView.snapshot(params, null);
        PixelReader pr = tempImg.getPixelReader();
        for (int y = 0; y < (int)newH; y++) {
            for (int x = 0; x < (int)newW; x++) {
                pw.setColor((int)(x + offsetX), (int)(y + offsetY), pr.getColor(x, y));
            }
        }


        VBox card = new VBox();
        card.setPrefWidth(240);
        card.setPrefHeight(320);
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: #fff; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #22222222, 12, 0.2, 0, 2); -fx-padding: 0; -fx-border-radius: 16; -fx-cursor: hand; -fx-transition: background-color 0.1s, box-shadow 0.1s;");

        card.setOnMouseEntered(e -> {
            card.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #22222222, 18, 0.3, 0, 6); -fx-padding: 0; -fx-border-radius: 16; -fx-cursor: hand; -fx-transition: background-color 0.1s, box-shadow 0.1s;");
            imagePane.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 20;");
            
        });
        card.setOnMouseExited(e -> {
            card.setStyle("-fx-background-color: #fff; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #22222222, 12, 0.2, 0, 2); -fx-padding: 0; -fx-border-radius: 16; -fx-cursor: hand; -fx-transition: background-color 0.1s, box-shadow 0.1s;");
            imagePane.setStyle("-fx-background-color: #fff; -fx-background-radius: 20;");
        });

        // ...existing code...
        ImageView img = new ImageView(squareImage);
        img.setFitWidth(180);
        img.setFitHeight(180);
        img.setPreserveRatio(false);
        img.setSmooth(true);
        img.setStyle("-fx-background-color: #eee;");
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(180, 180);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        img.setClip(clip);
        img.setViewport(new javafx.geometry.Rectangle2D(0, 0, 180, 180));
        imagePane.getChildren().clear();
        imagePane.getChildren().add(img);
        VBox.setMargin(imagePane, new Insets(16, 0, 0, 0));

        // Product name
        Label nameLbl = new Label(item.getItemName());
        nameLbl.setStyle("-fx-font-size: 17px; -fx-font-family: 'Segoe UI', 'Arial', sans-serif; -fx-font-weight: 800; -fx-text-fill: #222; -fx-padding: 12 0 0 0;");
        nameLbl.setAlignment(Pos.CENTER);
        nameLbl.setMaxWidth(Double.MAX_VALUE);

        // Edit button styled as product action
        Button editBtn = new Button("Edit");
        editBtn.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 6 18; -fx-font-weight: bold; -fx-cursor: hand;");
        editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: #0d47a1; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 6 18; -fx-font-weight: bold; -fx-cursor: hand;"));
        editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color: #1976d2; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 6 18; -fx-font-weight: bold; -fx-cursor: hand;"));
        editBtn.setOnAction(e -> showEditItemDialog(item));

        // Delete button styled as product action
        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 6 18; -fx-font-weight: bold; -fx-cursor: hand;");
        deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 6 18; -fx-font-weight: bold; -fx-cursor: hand;"));
        deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-padding: 6 18; -fx-font-weight: bold; -fx-cursor: hand;"));
        deleteBtn.setOnAction(e -> {
            try {
                deleteItem(item.getItemId());
            } catch (SQLException ex) {                                                                                                                                                                                                                                                                             
                ex.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR, "Failed to delete item from database.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            refreshItemsPane();
        });

        // Add buttons to card
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(12, 0, 12, 0));
        buttonBox.getChildren().addAll(editBtn, deleteBtn);

        card.getChildren().addAll(imagePane, nameLbl, buttonBox);
        card.setOnMouseClicked(e -> {
            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setTitle(item.getItemName());
            VBox content = new VBox(18);
            content.setPadding(new Insets(28));
            content.setAlignment(Pos.CENTER);
            content.setStyle("-fx-background-color: #fff; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #22222222, 18, 0.3, 0, 6);");

            Label nameLabel = new Label(item.getItemName());
            nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222; -fx-padding: 0 0 8 0;");

            ImageView photoView = new ImageView(new Image(item.getItemPhoto()));
            photoView.setFitWidth(180);
            photoView.setFitHeight(180);
            photoView.setPreserveRatio(true);
            photoView.setStyle("-fx-effect: dropshadow(gaussian, #22222222, 12, 0.2, 0, 2);");
            VBox.setMargin(photoView, new Insets(0, 0, 8, 0));

            Label priceLabel = new Label("Price: " + item.getItemPrice());
            priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1976d2; -fx-padding: 0 0 8 0;");

            Label descLabel = new Label("Description:");
            descLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #222; -fx-padding: 0 0 4 0;");

            TextArea descArea = new TextArea(item.getItemDescription());
            descArea.setEditable(false);
            descArea.setWrapText(true);
            descArea.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8; -fx-text-fill: #444;");
            descArea.setPrefRowCount(3);
            descArea.setMaxHeight(80);
            descArea.setTranslateY(-16);

            Button closeBtn = new Button("Close");
            closeBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;");
            closeBtn.setTranslateY(-20);
            closeBtn.setOnMouseEntered(ev -> closeBtn.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
            closeBtn.setOnMouseExited(ev -> closeBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
            closeBtn.setOnAction(ev -> dialogStage.close());

            content.getChildren().addAll(nameLabel, photoView, priceLabel, descLabel, descArea, closeBtn);
            Scene dialogScene = new Scene(content, 420, 480);
            dialogStage.setScene(dialogScene);
            dialogStage.showAndWait();
        });
        return card;
    }

    private void showEditItemDialog(Item item) {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(stage);
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Edit Item");
        VBox content = new VBox(18);
        content.setPadding(new Insets(24));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #fff; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #22222222, 18, 0.3, 0, 6);");

        // Add hover effect to overlay
       
        Label title = new Label("Edit Item");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222;");
        TextField nameInput = new TextField(item.getItemName());
        nameInput.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8;");
        TextField priceInput = new TextField(item.getItemPrice());
        priceInput.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8;");
        TextArea descInput = new TextArea(item.getItemDescription());
        descInput.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8;");
        descInput.setPrefRowCount(3);
        Button updateBtn = new Button("Update Item");
        updateBtn.setStyle("-fx-background-color: #5A7863; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;");
        updateBtn.setOnMouseEntered(e -> updateBtn.setStyle("-fx-background-color: #466052; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
        updateBtn.setOnMouseExited(e -> updateBtn.setStyle("-fx-background-color: #5A7863; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
        updateBtn.setOnAction(e -> {
            String name = nameInput.getText().trim();
            String price = priceInput.getText().trim();
            String description = descInput.getText().trim();
            if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR, "Please enter all fields.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            Item updatedItem = new Item(item.getItemId(), name, price, item.getItemPhoto(), description);
            try {
                updateItem(item.getItemId(), updatedItem);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR, "Failed to update item in database.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            refreshItemsPane();
            dialogStage.close();
        });
        content.getChildren().addAll(title, nameInput, priceInput, descInput, updateBtn);
        Scene dialogScene = new Scene(content);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private void showAddItemDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initOwner(stage);
        dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Add New Item");
        VBox content = new VBox(18);
        content.setPadding(new Insets(24));
        content.setAlignment(Pos.CENTER);
        Label title = new Label("Add New Item");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222;");
        Label desc = new Label("Add a new item to your personal storage. Upload a photo and provide details.");
        desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter item name");
        nameInput.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8;");
        TextField priceInput = new TextField();
        priceInput.setPromptText("Enter price");
        priceInput.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8;");
        TextArea descInput = new TextArea();
        descInput.setPromptText("Enter description");
        descInput.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8;");
        descInput.setPrefRowCount(3);
        VBox photoBox = new VBox();
        photoBox.setPrefSize(180, 180);
        photoBox.setAlignment(Pos.CENTER);
        photoBox.setStyle("-fx-border-color: #ccc; -fx-border-style: dashed; -fx-border-radius: 20; -fx-background-radius: 20; -fx-background-color: #fafafa;");
        Label uploadLabel = new Label("Click to upload photo");
        uploadLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 15px;");
        photoBox.getChildren().add(uploadLabel);
        final String[] selectedPhotoPath = {null};
        photoBox.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            java.io.File file = fileChooser.showOpenDialog(dialogStage);
            if (file != null) {
                selectedPhotoPath[0] = file.toURI().toString();
                Image img = new Image(selectedPhotoPath[0]);
                ImageView preview = new ImageView(img);
                preview.setFitWidth(180);
                preview.setFitHeight(180);
                preview.setPreserveRatio(true);
                javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(180, 180);
                clip.setArcWidth(20);
                clip.setArcHeight(20);
                preview.setClip(clip);
                photoBox.getChildren().setAll(preview);
            }
        });
        Button addBtn = new Button("Add Item");
        addBtn.setStyle("-fx-background-color: #5A7863; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;");
        addBtn.setOnAction(e -> {
            String name = nameInput.getText().trim();
            String price = priceInput.getText().trim();
            String description = descInput.getText().trim();
            String photo = selectedPhotoPath[0];
            if (name.isEmpty() || price.isEmpty() || photo == null || description.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR, "Please enter all fields and select a photo.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            Item newItem = new Item(0, name, price, photo, description);
            try {
                insertItem(newItem);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR, "Failed to save item to database.", ButtonType.OK);
                alert.showAndWait();
                return;
            }
            refreshItemsPane();
            dialogStage.close();
        });
        content.getChildren().addAll(title, desc, photoBox, nameInput, priceInput, descInput, addBtn);
        Scene dialogScene = new Scene(content);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    // Add selectedItem field
    private Item selectedItem = null;

    // Item class for storage
    public static class Item {
        private int itemId;
        private String itemName;
        private String itemPrice;
        private String itemPhoto;
        private String itemDescription;
        public Item(int itemId, String itemName, String itemPrice, String itemPhoto, String itemDescription) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.itemPhoto = itemPhoto;
            this.itemDescription = itemDescription;
        }
        public int getItemId() { return itemId; }
        public String getItemName() { return itemName; }
        public String getItemPrice() { return itemPrice; }
        public String getItemPhoto() { return itemPhoto; }
        public String getItemDescription() { return itemDescription; }
        @Override
        public String toString() { return itemName; }
    }

    // Custom cell to show image and name
    private class ItemCell extends javafx.scene.control.ListCell<Item> {
        private ImageView cellImage = new ImageView();
        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                cellImage.setImage(new Image(item.getItemPhoto(), 50, 50, true, true));
                setText(item.getItemName());
                setGraphic(cellImage);
            }
        }
    }

    // ItemView helper class
    private static class ItemView {
        public static void showDialog(Stage owner, Item item) {
            Stage dialogStage = new Stage();
            dialogStage.initOwner(owner);
            dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            dialogStage.setTitle(item.getItemName());
            VBox content = new VBox(18);
            
            content.setPadding(new Insets(28));
            content.setAlignment(Pos.CENTER);
            content.setStyle("-fx-background-color: #fff; -fx-background-radius: 16; -fx-effect: dropshadow(gaussian, #22222222, 18, 0.3, 0, 6);");

            Label nameLabel = new Label(item.getItemName());
            nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #222; -fx-padding: 0 0 8 0;");

            ImageView photoView = new ImageView(new Image(item.getItemPhoto()));
            photoView.setFitWidth(180);
            photoView.setFitHeight(180);
            photoView.setPreserveRatio(true);
            photoView.setStyle("-fx-effect: dropshadow(gaussian, #22222222, 12, 0.2, 0, 2);");
            VBox.setMargin(photoView, new Insets(0, 0, 8, 0));

            Label priceLabel = new Label("Price: " + item.getItemPrice());
            priceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1976d2; -fx-padding: 0 0 8 0;");

            Label descLabel = new Label("Description:");
            descLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #222; -fx-padding: 0 0 4 0;");

            TextArea descArea = new TextArea(item.getItemDescription());
            descArea.setEditable(false);
            descArea.setWrapText(true);
            descArea.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #ccc; -fx-border-width: 2; -fx-font-size: 15px; -fx-padding: 8; -fx-text-fill: #444;");
            descArea.setPrefRowCount(3);
            descArea.setMaxHeight(80);
            descArea.setTranslateY(-16);

            Button closeBtn = new Button("Close");
            closeBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;");
            closeBtn.setTranslateY(-20);
            closeBtn.setOnMouseEntered(e -> closeBtn.setStyle("-fx-background-color: #b71c1c; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
            closeBtn.setOnMouseExited(e -> closeBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-size: 15px; -fx-background-radius: 8; -fx-padding: 8 24; -fx-font-weight: bold;"));
            closeBtn.setOnAction(e -> dialogStage.close());
           
            content.getChildren().addAll(nameLabel, photoView, priceLabel, descLabel, descArea, closeBtn);
            Scene dialogScene = new Scene(content, 420, 480);
            dialogStage.setScene(dialogScene);
            dialogStage.showAndWait();
        }
    }

    // Utility method to get MySQL connection for 'storage' database
    public static Connection getStorageDbConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/storage";
        String user = "root"; // change to your MySQL username
        return DriverManager.getConnection(url, user, "");
    }

    // CRUD methods for Item
    public static void insertItem(Item item) throws SQLException {
        String sql = "INSERT INTO item (itemName, itemPrice, itemPhoto, itemDescription) VALUES (?, ?, ?, ?)";
        try (Connection conn = getStorageDbConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getItemPrice());
            stmt.setString(3, item.getItemPhoto());
            stmt.setString(4, item.getItemDescription());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                item.itemId = rs.getInt(1);
            }
        }
    }

    public static void updateItem(int itemId, Item item) throws SQLException {
        String sql = "UPDATE item SET itemName=?, itemPrice=?, itemPhoto=?, itemDescription=? WHERE itemId=?";
        try (Connection conn = getStorageDbConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getItemPrice());
            stmt.setString(3, item.getItemPhoto());
            stmt.setString(4, item.getItemDescription());
            stmt.setInt(5, itemId);
            stmt.executeUpdate();
        }
    }

    public static void deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM item WHERE itemId=?";
        try (Connection conn = getStorageDbConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        }
    }

    public static ObservableList<Item> getAllItems() throws SQLException {
        ObservableList<Item> items = FXCollections.observableArrayList();
        String sql = "SELECT itemId, itemName, itemPrice, itemPhoto, itemDescription FROM item";
        try (Connection conn = getStorageDbConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Item item = new Item(
                    rs.getInt("itemId"),
                    rs.getString("itemName"),
                    rs.getString("itemPrice"),
                    rs.getString("itemPhoto"),
                    rs.getString("itemDescription")
                );
                items.add(item);
            }
        }
        return items;
    }

    private int getItemId(Item item) {
        // Updated to match new column names
           try (Connection conn = getStorageDbConnection();
               PreparedStatement stmt = conn.prepareStatement("SELECT itemId FROM item WHERE itemName=? AND itemPrice=? AND itemPhoto=? AND itemDescription=? LIMIT 1")) {
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getItemPrice());
            stmt.setString(3, item.getItemPhoto());
            stmt.setString(4, item.getItemDescription());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("itemId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Not found
    }
}