package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Inventory;
import model.inHouse;
import model.outSource;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class creates the Add Part controller.
 * @author Christopher Ram
 */

public class addPartForm implements Initializable {
    public RadioButton inHouse;
    public RadioButton outSource;
    public ToggleGroup tGroup;
    public Label machineIdCompanyName;
    public Button saveButton;
    public TextField partName;
    public TextField partInventory;
    public TextField partCost;
    public TextField partMax;
    public TextField partMachineIdCompanyName;
    public TextField partMin;
    public TextField partId;

    /**
     * This method will set the radio button to inhouse by default.
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        inHouse.setSelected(true);

    }

    /**
     * This method generates a unique ID that will auto populate in the partId field. This ID will not match any
     * others that are generated.
     * @param partId this is the textfield that will be populated with the unique ID.
     * @return produces a unique ID.
     */
    public int generateUniqueId(TextField partId) {
        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
    /**
     * This method returns the user back to the mainController screen, no data will be saved.
     * @param actionEvent user clicks the Cancel button
     * @throws IOException IOException
     */

    public void toMain(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Changes not Saved");
        alert.setContentText("Did you want to continue to the main screen?");

        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.get() == ButtonType.OK) {

            Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * This method changes the machineIdCompanyName label to machine ID if the inHouse radio button is selected.
     * @param actionEvent clicking the inhouse radiobutton.
     */
    public void onInHouse(ActionEvent actionEvent) {
        machineIdCompanyName.setText("Machine ID");
    }

    /**
     * This method changes the machineIdCompanyName label to Company if the outsource radio button is selected.
     * @param actionEvent clicking the outsource radiobutton
     */
    public void onOutSource(ActionEvent actionEvent) {
        machineIdCompanyName.setText("Company Name");
    }
    /**
    * This method will save a new part once the save button is clicked. The new parts data is added based on the
     * information inputed from the addPartForm screen.
            * @param actionEvent user clicks the Save button
     * @throws IOException IOException
     */
    public void onSaveButton(ActionEvent actionEvent) throws IOException {
        try {
            int id = generateUniqueId(partId);
            String name = partName.getText();
            int stock = Integer.parseInt(partInventory.getText());
            double price = Double.parseDouble(partCost.getText());
            int max = Integer.parseInt(partMax.getText());
            int min = Integer.parseInt(partMin.getText());
            int machineId = 0;

            if ((min > max) || (max < stock) || (stock < min)) {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Check Part. Min cannot be larger than max. Stock cannot be larger than max. Min cannot be larder than stock.");
                alert.showAndWait();

            } else {
                if (inHouse.isSelected()) {
                    machineId = Integer.parseInt(partMachineIdCompanyName.getText());
                    Inventory.addPart(new inHouse(id, name, price, stock, min, max, machineId));
                } else {
                    String companyName = partMachineIdCompanyName.getText();
                    Inventory.addPart(new outSource(id, name, price, stock, min, max, companyName));
                }
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText(" Part cannot be added , try again.");
            alert.showAndWait();
        }
    }
}
