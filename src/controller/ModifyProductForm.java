package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class creates the modifyProductForm.
 * @author Christopher Ram
 */
public class ModifyProductForm implements Initializable {

    public TableView<Part> partList;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInventoryLevelCol;
    public TableColumn partPriceCol;
    public Button toMainButton;
    public TableView<Part> productPartList;
    public TableColumn productPartIdCol;
    public TableColumn productPartNameCol;
    public TableColumn productPartStockCol;
    public TableColumn productPartCostCol;
    public TextField searchParts;
    public Button addPart;
    public Button deletePart;
    public Button saveProduct;
    public TextField productName;
    public TextField productId;
    public TextField productInv;
    public TextField productCost;
    public TextField productMax;
    public TextField productMin;

    Inventory inv = new Inventory();
    private Product SP;
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * This method sets the top part list and the lower associated parts table views. The upper list will contain any
     * parts that are available in the parts inventory. The lower list will contain any parts that are currently
     * associated with the product.
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {

        partList.setItems(Inventory.getAllParts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory((new PropertyValueFactory<>("name")));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameCol.setCellValueFactory((new PropertyValueFactory<>("name")));
        productPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * The method will send the user back to the mainController screen without altering any of the data with the
     * selected product.
     * @param actionEvent clicking the cancel button.
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
     * This method takes the data from the mainController screen and sends it the modifyProductForm data fields. The data
     * populates the fields including any associated parts with that product.
     * @param data the product data from the main controller screen.
     */
    public void receiveProductData(Product data) {

        productPartList.setItems(associatedParts);

        SP = data;
        productId.setText(String.valueOf(SP.getId()));
        productName.setText(SP.getName());
        productCost.setText(String.valueOf(SP.getPrice()));
        productInv.setText(String.valueOf(SP.getStock()));
        productMax.setText(String.valueOf(SP.getMax()));
        productMin.setText(String.valueOf(SP.getMin()));

        this.associatedParts = data.getAssociatedParts();
        productPartList.setItems(associatedParts);
    }

    /**
     * This method searches the Parts inventory. If a partial match is found for either the part name or the partID
     * then it will populate the list. If no match is found an error message will appear. If you hit enter on an
     * empty search then the entire list of parts will repopulate.
     * @param actionEvent adding information to the searchParts text field and hitting enter.
     */
    public void onSearchParts(ActionEvent actionEvent) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> PF = FXCollections.observableArrayList();
        String searchString = searchParts.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                PF.add(part);
            }
        }

        partList.setItems(PF);

        if (PF.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Part not Found. Clear Search and hit Enter to search again.");
            alert.showAndWait();
        }
    }

    /**
     * This method will add a new associated part to the Product. The part is added to the associated parts table
     * which shows any and all parts associated with the product.
     * @param actionEvent clicking the add button.
     */
    public void onAddPart(ActionEvent actionEvent) {
        Part p = partList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Did you want to add this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SP.addAssociatedPart(p);
            productPartList.setItems(SP.getAssociatedParts());
        }
    }

    /**
     * This method will delete an associated part from the lower table which is the parts that are associated with the
     * currently selected product. The part is removed from the product but remains a part of the part inventory.
     * @param actionEvent clicking the remove associated part button.
     */
    public void onDeletePart(ActionEvent actionEvent) {
        Part p = productPartList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Did you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SP.deleteAssociatedPart(p);
        }
    }

    /**
     * This method will modify the existing products information once the save button is clicked. The information is
     * updated to the new information inputed in the text fields of the modifyProductForm screen. The productId text field
     * can not be changed and is used to replace the existing product infomation in the mainController Screen.
     * @param actionEvent clicking the save button
     * @throws IOException IOException
     */
    public void onSaveProduct(ActionEvent actionEvent) throws IOException {
        try {

            int id = Integer.parseInt(productId.getText());
            String name = productName.getText();
            int stock = Integer.parseInt(productInv.getText());
            double price = Double.parseDouble(productCost.getText());
            int max = Integer.parseInt(productMax.getText());
            int min = Integer.parseInt(productMin.getText());

            if ((Integer.parseInt(productMin.getText()) > Integer.parseInt(productMax.getText()))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Min must be less than or equal to Max.");
                alert.showAndWait();
            } else if (Integer.parseInt(productInv.getText()) > Integer.parseInt(productMax.getText()) || Integer.parseInt(productInv.getText()) < Integer.parseInt(productMin.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Stock must be within the Max and Min values");
                alert.showAndWait();
            } else if (productName.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Add Name");
                alert.showAndWait();
            } else {

                Inventory.updateProduct(Integer.parseInt(productId.getText()), new Product(id, name, price, stock, min, max));
                for (Product prod : inv.getAllProducts()) {
                    if (prod.getProductId() == Integer.parseInt(productId.getText())) {
                        for (Part p : associatedParts)
                            prod.addAssociatedPart(p);
                    }

                }
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 600);
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Input valid values for all text fields");
            alert.showAndWait();
        }
    }
}
