package Controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class ProjectController {
    Stage classStage = new Stage();

    public javafx.scene.control.TextField projectNameTF;
    public javafx.scene.control.TextField projectDescriptionTF;
    public javafx.scene.control.TextField projectCostsTF;
    public GridPane gridPaneLeft;

    public void addProjectBtnClick(ActionEvent actionEvent) {
        String projectName = projectNameTF.getText();
        Label projectNameLb = new Label(projectName);
        Label projectDescriptionLb = new Label(projectDescriptionTF.getText());
        Label projectCostsLb = new Label(projectCostsTF.getText());
        Button projectSelectButton = new Button("Select " + projectNameTF.getText());
        projectSelectButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), projectName, "productView.fxml");
        });

        int rowIndex = gridPaneLeft.getRowCount()+1;

        gridPaneLeft.add(projectNameLb, 0, rowIndex);
        gridPaneLeft.add(projectDescriptionLb, 1, rowIndex);
        gridPaneLeft.add(projectCostsLb,2, rowIndex);
        gridPaneLeft.add(projectSelectButton, 3, rowIndex);

        //System.out.println(gridPaneLeft.getChildren());
    }
}
