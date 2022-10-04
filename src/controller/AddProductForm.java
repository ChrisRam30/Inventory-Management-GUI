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
import java.util.UUID;

/**
 * Created the add product controller
 * @author Christopher Ram
 */
public class AddProductForm  implements Initializable {
    public TableView<Part> partList;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInventoryLevelCol;
    public TableColumn partPriceCol;
    public Button toMainButton;
    public TextField productName;
    public TextField productId;
    public TextField productStock;
    public TextField productCost;
    public TextField productMax;
    public TextField productMin;
    public TextField searchPart;
    public Button saveProduct;
    public Button addPartTemp;
    public TableView<Part> productPartList;
    public TableColumn productPartIdC;
    public TableColumn productPartNameC;
    public TableColumn productPartStockC;
    public TableColumn productPartCostC;
    public Button deleteProductPart;

    public Product p = new Product(0, "", 0, 0, 0, 0);

    Inventory inv = new Inventory();

    ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * This method will set the top parts table and the lower associated parts table views.
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        productId.setText(String.valueOf(generateUniqueId(productId)));

        partList.setItems(Inventory.getAllParts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory((new PropertyValueFactory<>("name")));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productPartIdC.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameC.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartStockC.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartCostC.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This method generates a uniqueID for the product
     * @param productId productId
     * @return returns a unique productId
     */
    public int generateUniqueId(TextField productId) {
        UUID idOne = UUID.randomUUID();
        String str = "" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }

    /**
     * This method will return the user back to the mainController screen without modifying the selected product
     * @param actionEvent clicking the cancel button
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
     * This method searches the Parts inventory. If a partial match is found for either the part name or the partID
     * then it will populate the list. If no match is found an error message will appear. If you hit enter on an
     * empty search then the entire list of parts will repopulate.
     * @param actionEvent entering data in the searchPart text field and hitting enter.
     */
    public void onSearchPart(ActionEvent actionEvent) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> PF = FXCollections.observableArrayList();
        String searchString = searchPart.getText();

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
     * This method will save a new product to the productList. Information inputed in the addProductForm text fields will
     * be used as the data for the new product.
     * @param actionEvent clicking the save button
     * @throws IOException IOException
     */
    public void onSaveProduct(ActionEvent actionEvent) throws IOException {
        try {
            p.setId(Integer.parseInt(productId.getText()));
            p.setName(productName.getText());
            p.setStock(Integer.parseInt(productStock.getText()));
            p.setPrice(Double.parseDouble(productCost.getText()));
            p.setMax(Integer.parseInt(productMax.getText()));
            p.setMin(Integer.parseInt(productMin.getText()));

            if ((Integer.parseInt(productMin.getText()) > Integer.parseInt(productMax.getText()))) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("Min must be less than or equal to Max.");
                alert.showAndWait();
            } else if (Integer.parseInt(productStock.getText()) > Integer.parseInt(productMax.getText()) || Integer.parseInt(productStock.getText()) < Integer.parseInt(productMin.getText())) {
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

                Inventory.addProduct(p);
                Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 600);
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please input valid values for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * This method will add a new associated part to the Product. The part is added to the associated parts table
     * which shows any and all parts associated with the product.
     * @param actionEvent selecting a part and hitting the add button.
     */
    public void onAddPartTemp(ActionEvent actionEvent) {
        Part SP = partList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Did you want to add this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            p.addAssociatedPart(SP);
            productPartList.setItems(p.getAssociatedParts());
        }
    }

    /**
     * This method will delete an associated part from the lower table which is the parts that are associated with the
     * currently selected product. The part is removed from the product but remains a part of the part inventory.
     * @param actionEvent clicking the remove associated part button.
     */
    public void onDeleteProductPart(ActionEvent actionEvent) {
        Part SP = productPartList.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Did you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            p.deleteAssociatedPart(SP);
        }
    }
}
