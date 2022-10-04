package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.inHouse;
import model.outSource;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class created the modifyPartController.
 * @author Christopher Ram
 */
public class ModifyPartForm  implements Initializable {
    public Button cancel;
    public RadioButton inHouse;
    public RadioButton outSource;
    public ToggleGroup tGroup;
    public Label machineIdCompanyName;
    public TextField partId;
    public TextField partName;
    public TextField partStock;
    public TextField partCost;
    public TextField partMax;
    public TextField partMachineIdCompanyName;
    public TextField partMin;
    public Button partSave;

    /**
     * Initializes the class(Modify part)
     */
    @Override

    public void initialize (URL url, ResourceBundle resourceBundle) {
    }

    /**
     * This method takes the data from the part selected on the main screen in sends it to the part fields in the
     * modifyPartController screen.
     * @param data the part data from the main controller screen.
     */
    public void receivePartData(Part data) {

        partId.setText(String.valueOf(data.getId()));
        partName.setText(data.getName());
        partStock.setText(String.valueOf(data.getStock()));
        partMax.setText(String.valueOf(data.getMax()));
        partMin.setText(String.valueOf(data.getMin()));
        partCost.setText(String.valueOf(data.getPrice()));

        if (data instanceof inHouse) {
            inHouse.setSelected(true);
            partMachineIdCompanyName.setText(String.valueOf(((model.inHouse) data).getMachineId()));
        }

        if (data instanceof outSource) {
            outSource.setSelected(true);
            partMachineIdCompanyName.setText(String.valueOf(((model.outSource)data).getCompanyName()));
        }
    }

    /**
     * This method sends the user back to the main screen without saving any of the changes to the part selected.
     * @param actionEvent clicking the cancel button
     * @throws IOException IOException
     */
    public void toMain(ActionEvent actionEvent)  throws IOException {
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
     * This method changes the machineIdCompanyName label to Machine ID if the inhouse radio button is selected.
     * @param actionEvent selecting the inhouse Radio button.
     */
    public void onInHouse(ActionEvent actionEvent) {
        machineIdCompanyName.setText("Machine ID");

    }

    /**
     * This method changes the machineIdCompanyName label to Company name if the outhouse radio button is selected.
     * @param actionEvent selecting the outsource radio button.
     */
    public void onOutSource(ActionEvent actionEvent) {
        machineIdCompanyName.setText("Company Name");

    }

    /**
     * This method modifies the part that was selected on the main screen. The existing parts information will be
     * changed to the information added in the text fields in the modifyPartForm. The partID can not be altered as
     * this is used to reference the part on the mainController screen.
     * @param actionEvent clicking the save button
     * @throws IOException IOException
     */
    public void onPartSave(ActionEvent actionEvent) throws IOException {

        try {
            int id = Integer.parseInt(partId.getText());
            String name = partName.getText();
            int stock = Integer.parseInt(partStock.getText());
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
                    Inventory.updatePart(Integer.parseInt(partId.getText()), new inHouse(id, name, price, stock, min, max, machineId));
                } else {
                    String companyName = partMachineIdCompanyName.getText();
                    Inventory.updatePart(Integer.parseInt(partId.getText()), new outSource(id, name, price, stock, min, max, companyName));
                }

                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.show();
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Part cannot be added , try again.");
            alert.showAndWait();
        }
    }

}
