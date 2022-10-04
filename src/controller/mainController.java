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

import static model.Inventory.*;

/**
 * Main Screen controller is created with this class.
 * @author Christopher Ram
 */

public class mainController implements Initializable {
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInventoryLevelCol;
    public TableColumn partPriceCol;
    public TableColumn productIDCol;
    public TableColumn productNameCol;
    public TableColumn productInventoryLevelCol;
    public TableColumn productCostCol;
    public Button deletePart;
    public Button deleteProduct;
    public Button AddProduct;
    public Button ModPart;
    public Button ModProduct;
    public Button addPart;
    public Button exit;
    public TableView<Part> partList;
    public TableView<Product> productList;
    public TextField partSearch;
    public Label psLabel;
    public Label prodLabel;
    public TextField productSearch;
    public Button exitButton;

    /**Initialize Method.
     * This populates both the of the tables views and holds the getter for allParts and allProducts.
     * */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partList.setItems(Inventory.getAllParts());
        productList.setItems(Inventory.getAllProducts());

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory((new PropertyValueFactory<>("name")));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory((new PropertyValueFactory<>("name")));
        productInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }


    /**
     * This method loads the Add Part screen when the user clicks the Add button in the Parts section.
     * @param actionEvent user clicks the Add button in the Parts section
     * @throws IOException IOException
     */

    public void toAddPartForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addPartForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Add Part Form");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method loads the modify Part Screen and throws a warning box if no part is selected
     * @param actionEvent user clicks the Modify button in the Parts section
     * @throws IOException IOException
     */

    public void onModifyPartForm(ActionEvent actionEvent) throws IOException {
        if (partList.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Select a Part.");
            alert.showAndWait();

        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/modifyPartForm.fxml"));
            loader.load();

            ModifyPartForm MPFController = loader.getController();
            MPFController.receivePartData(partList.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Modify Part Form");
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    /**
     * This method deletes the selected Part and asks for confirmation before deleting.
     * @param actionEvent user clicks the Delete button and asks for confirmation of action before completing it.
     */
    public void onDeletePart(ActionEvent actionEvent) {

        Part SP = partList.getSelectionModel().getSelectedItem();
        if(SP == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Select part to delete.");
            alert.showAndWait();
        }
        else if (isPartUsed(SP)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Can not delete this part");
            alert.setContentText("Remove any parts from Product before deletion.");
            alert.showAndWait();
        }

        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Part delete Confirmation");
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {
                Inventory.deletePart(SP);
            }
        }
    }
    /**
     * This method loads the addPartForm.
     * @param actionEvent user clicks the addPart button.
     * @throws IOException IOException
     */
    public void onAddProductForm(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addProductForm.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Add Product Form");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This method loads the modifyProductForm. If a product is not selected it will ask you to select a product.
     * @param actionEvent user clicks the modifyProduct button.
     * @throws IOException IOException
     */
    public void onModifyProductForm(ActionEvent actionEvent) throws IOException {
        if (productList.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Select a Product.");
            alert.showAndWait();

        } else {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(("/view/modifyProductForm.fxml")));
            loader.load();

            ModifyProductForm MPFController = loader.getController();
            MPFController.receiveProductData(productList.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Modify Product Form");
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    /**
     * This method deletes the selected Product and asks for confirmation before it deletes product.
     * @param actionEvent user clicks the deleteProduct button.
     */
    public void onDeleteProduct(ActionEvent actionEvent) {
        Product SP = productList.getSelectionModel().getSelectedItem();
        if (SP == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Select a product to delete.");
            alert.showAndWait();
        }
        else if (!SP.getAssociatedParts().isEmpty()) {
            Alert warn = new Alert(Alert.AlertType.WARNING, "Cannot delete a product that has associated parts. Delete assocaited parts to delete product.", ButtonType.OK);
            warn.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm deletion");
            alert.setContentText("Confirm you want to delete this Product.");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {
                Inventory.deleteProduct(SP);
                getAllProducts();
            }
        }
    }

    /**
     * RUNTIME ERROR: When creating the onSearchParts method there were issues with having the partial matches with
     * searches. After meeting with the professor and rewatching the webinar on how to create searches I discovered
     * that my search was looking for exact matches using partList.getSelectionModel().select(p). After discussions with
     * the professor I refactored my code to a "(String.valueOf(part.getId()).contains(searchString) ||
     * part.getName().contains(searchString))" which allowed for partial matches.
     *
     * This method searches the Parts inventory. If a partial match is found for either the part name or the partID
     * then it will populate the list. If no match is found an error message will appear. If you hit enter on an
     * empty search then the entire list of parts will repopulate.
     *
     * @param actionEvent user clicks the search textfield in the parts section.
     */
    public void onSearchParts(ActionEvent actionEvent) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> PF = FXCollections.observableArrayList();
        String searchString = partSearch.getText();

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
     * This method searches the Products inventory. If a partial match is found for either the product name or the
     * productId then it will populate the list. If no match is found an error message will appear. If you hit enter on an
     * empty search then the entire list of Products will repopulate.
     *
     * @param actionEvent user clicks the Search Text field in the products section.
     */
    public void onSearchProducts(ActionEvent actionEvent) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> ProdFound = FXCollections.observableArrayList();
        String searchString = productSearch.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().contains(searchString)) {
                ProdFound.add(product);
            }
        }

        productList.setItems(ProdFound);

        if (ProdFound.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Part not Found. Clear Search and hit Enter to search again.");
            alert.showAndWait();
        }
    }

    /**
     * This method closes the program and asks for confirmation before action.
     * @param actionEvent user clicks the exit button.
     */

    public void onExitButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you would like to exit the Program?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }
}
