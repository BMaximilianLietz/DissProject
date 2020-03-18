package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.border.Border;
import java.awt.*;
import java.util.List;

public class CostingController {

    public ScrollPane scrollPaneIterations;
    List<List<String>> iterationList;

    @FXML
    public BorderPane borderPane;
    public GridPane gridPaneLeft;
    public TextField iterationTextField;
    public TextField userStoryNameTextField;
    public TextField storyPointsTextField;
    public GridPane gridPaneUserStories;
    public Label storyPointsTotalLabel;
    public Label storyPointsTotalPoints;
    public GridPane gridPaneIterations;
    public Text iterationsListLabel;
    public Label iterationsTotalPoints;

    public void initialize() {
        iterationsListLabel.wrappingWidthProperty().bind(scrollPaneIterations.widthProperty());
    }

    public void addUserStoryBtnClick(ActionEvent actionEvent) {
        System.out.println(iterationTextField.getText());
        System.out.println(userStoryNameTextField.getText());
        System.out.println(storyPointsTextField.getText());

//        int addedRow = gridPaneLeft.getRowCount()+1;
        //int addedRowUserStories = gridPaneUserStories.getRowCount();
        int actualPoints;
        try {
            actualPoints = Integer.parseInt(storyPointsTextField.getText());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("The Input for Story Points must be an integer");
            alert.initOwner(((Node)actionEvent.getTarget()).getScene().getWindow());
            alert.showAndWait();
            return;
        }

        Label iteration = new Label("Iteration: " + iterationTextField.getText());
        //gridPaneLeft.add(iteration, 1, addedRow);

        Label storyName = new Label(userStoryNameTextField.getText());
//        gridPaneLeft.add(storyName, 2, addedRow);

        Label storyPoints = new Label(storyPointsTextField.getText());
//        gridPaneLeft.add(storyPoints, 3, addedRow);

//        GridPane.getRowIndex(storyPointsTotalLabel);
        System.out.println(GridPane.getRowIndex(storyPointsTotalLabel));
        GridPane.setRowIndex(storyPointsTotalLabel, GridPane.getRowIndex(storyPointsTotalLabel)+1);
        GridPane.setRowIndex(storyPointsTotalPoints, GridPane.getRowIndex(storyPointsTotalPoints)+1);
        System.out.println(gridPaneUserStories.getRowCount());

        if (iterationsListLabel.getText().equals("")) {
            iterationsListLabel.setText(userStoryNameTextField.getText());
        } else {
            iterationsListLabel.setText(iterationsListLabel.getText() + ", " + userStoryNameTextField.getText());
        }

        gridPaneUserStories.add(storyName, 0, gridPaneUserStories.getRowCount()-2);
        gridPaneUserStories.add(storyPoints, 1,gridPaneUserStories.getRowCount()-2);

        int totalPoints = Integer.parseInt(storyPointsTotalPoints.getText())+actualPoints;
        storyPointsTotalPoints.setText(String.valueOf(totalPoints));

    }
}
