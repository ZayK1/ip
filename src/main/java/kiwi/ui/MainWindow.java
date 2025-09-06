package kiwi.ui;

import kiwi.Kiwi;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Kiwi kiwi;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/userImage.png"));
    private Image kiwiImage = new Image(this.getClass().getResourceAsStream("/images/kiwiImage.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        // Show welcome message
        String welcomeMessage = "Hello! I'm Kiwi, your personal task manager.\n" +
                "What can I do for you today?";
        dialogContainer.getChildren().add(
                DialogBox.getKiwiDialog(welcomeMessage, kiwiImage)
        );
    }

    /** Injects the Kiwi instance */
    public void setKiwi(Kiwi k) {
        kiwi = k;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Kiwi's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = kiwi.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getKiwiDialog(response, kiwiImage)
        );
        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            System.exit(0);
        }
    }
}