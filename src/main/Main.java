package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import model.inHouse;

/**
  C482 - Software I - Inventory Management System
  Class creates an application that manages an inventory of Parts and Products.

  FUTURE ENHANCEMENT: A feature that I would add to increase the functionality of the application would be to include
  an Order part form.This form could be used to order new parts when inventory is low. Additionally a new method could
  be created in the Part class to alert when parts reach a low inventory level. This would allow for the business to run
  seamlessly and to decrease any downtime that may occur in product creation.
  RUNTIME ERRORS: Can be found at onSearchParts method in the mainController.java
  @author Christopher Ram
 */

public class Main extends Application {
    /** Loads mainForm.fxml to initiate the GUI and displays mainController.
     * @param primaryStage Primary Stage
     *
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/mainForm.fxml"));
        primaryStage.setTitle("Main Form");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    private static void addPartTestData () {
        inHouse a = new inHouse(1, "Brakes", 15.00,10, 0, 10000,1 );
        Inventory.addPart(a);
        inHouse b = new inHouse(2, "Wheel", 11.00,16, 0, 10000,2 );
        Inventory.addPart(b);
        inHouse c = new inHouse(3, "Seat", 15.00,10, 0, 10000,3 );
        Inventory.addPart(c);

        Product d = new Product(1000, "Giant Bike", 299.99,5, 0, 10000 );
        Inventory.addProduct(d);
        Product e = new Product(1001, "Tricycle", 99.99,3, 0, 10000);
        Inventory.addProduct(e);

    }
    /** This is the main method that lauches the Inventory management database program.
     * @param args args for main method.
     */
    public static void main(String[] args) {
        //test data goes here

        addPartTestData();

        launch(args);
    }
}
