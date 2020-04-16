package Controllers;

import Data.ProductConnector;
import Data.ProductPricingConnector;
import Data.ProjectConnector;
import Misc.HelperMethods;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ProjectController {
    public ComboBox businessModelCB;
    public javafx.scene.control.TextField projectNameTF;
    public javafx.scene.control.TextField projectDescriptionTF;
    public javafx.scene.control.TextField projectCostsTF;
    public GridPane gridPaneLeft;

    public ArrayList<Object> activeProject;
    public TextField productVersionTF;

    public void initialize() {
        //activeProject = SceneController.activeProject;
        //projectName.setText(activeProject);
        ArrayList<ArrayList<Object>> init = ProjectConnector.getAllProjects();
        if (init.size() == 0) {
            System.out.println("No projects");
        } else {
            for (int i = 0; i < init.size(); i++) {
                addProject(init.get(i), gridPaneLeft);
            }
        }
    }

    public void addProjectBtnClick(ActionEvent actionEvent) {

        if ((projectNameTF.getText().equals(""))||(projectDescriptionTF.getText().equals(""))) {
            HelperMethods.throwAlert(gridPaneLeft.getScene(), "Please fill out form");
            return;
        }

        String projectName = projectNameTF.getText();
        Label projectNameLb = new Label(projectName);
        Label projectDescriptionLb = new Label(projectDescriptionTF.getText());
        Label projectCostsLb = new Label("0.0");
        Label businessModelLB;
        try {
            businessModelLB = new Label(businessModelCB.getSelectionModel().getSelectedItem().toString());
            ArrayList<Object> queryResults = ProjectConnector.insertIntoProject(projectName, projectDescriptionLb.getText(),
                    null, businessModelLB.getText());
            addProject(queryResults, gridPaneLeft);
        } catch (Exception e) {
            HelperMethods.throwAlert(gridPaneLeft.getScene(), "Please select a business model");
        }
    }

    public void addProject(ArrayList<Object> project, GridPane gridPaneChosen) {
        Label projectNameLb = new Label((String)project.get(1));
        Label projectDescriptionLb = new Label((String)project.get(2));
        Label projectBusinessModelLb = new Label((String)project.get(4));
        Label projectCostsLb;

        try {
            projectCostsLb = new Label(String.valueOf((Double) project.get(5)));
        } catch (Exception e) {
            projectCostsLb = new Label("Product Pricing Required");
        }

        Button projectSelectButton = new Button("Select");
        projectSelectButton.onActionProperty().setValue(actionEvent1 -> {
            Scene scene = ((Node)actionEvent1.getTarget()).getScene();
            SceneController.openView(scene, getClass(), project, "productView.fxml");
        });

        Button projectDeleteButton = new Button("Delete");
        projectDeleteButton.onActionProperty().setValue(actionEvent1 -> {
            gridPaneLeft.getChildren().removeIf(node -> GridPane.getRowIndex(node) ==
                    GridPane.getRowIndex(((Button)actionEvent1.getTarget())));
            ProjectConnector.deleteProjectByProjectName((Integer) project.get(0));
        });

        HBox hbox = new HBox(projectSelectButton, projectDeleteButton);
        hbox.setSpacing(5);

        int rowIndex = gridPaneChosen.getRowCount()+1;

        gridPaneChosen.add(projectNameLb, 0, rowIndex);
        gridPaneChosen.add(projectDescriptionLb, 1, rowIndex);
        gridPaneChosen.add(projectCostsLb, 2, rowIndex);
        gridPaneChosen.add(projectBusinessModelLb, 3, rowIndex);
        gridPaneChosen.add(hbox, 4, rowIndex);
    }
}
