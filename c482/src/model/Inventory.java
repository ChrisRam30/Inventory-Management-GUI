package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<inHouse> allInHouse = FXCollections.observableArrayList();
    private static ObservableList<outSource> allOutSource = FXCollections.observableArrayList();




    public static void deletePart(Part sp) {
        allParts.remove(sp);
    }

    public static Part lookUpPart(int sp) {
        for (Part p : allParts) {
            if (p.getId() == sp) {
                return p;
            }
        }
        return null;
    }

    public static Product lookUpProduct(int sp) {
        for (Product p : allProducts) {
            if (p.getId() == sp) {
                return p;
            }
        }
        return null;
    }

    public static void updatePart(int index, Part sp) {
        Part p = Inventory.lookUpPart(index);
        Inventory.deletePart(p);
        Inventory.addPart(sp);
    }

    public static void updateProduct(int index, Product sp) {
        Product p = Inventory.lookUpProduct(index);
        Inventory.deleteProduct(p);
        Inventory.addProduct(sp);
    }

    public static void deleteProduct(Product sp) {
        allProducts.remove(sp);
    }

    public static boolean isPartUsed(Part SP) {
        boolean usedPart = false;
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getAssociatedParts().contains(SP)) {
                usedPart = true;
            }
        }
        return usedPart;
    }

    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public void setAllParts(ObservableList<Part> allParts) {
        this.allParts = allParts;
    }

    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(ObservableList<Product> allProducts) {
        this.allProducts = allProducts;
    }

    public static ObservableList<inHouse> getAllInHouse () {
        return allInHouse;
    }

    public static ObservableList<outSource> getAllOutSource() {
        return allOutSource;
    }

    public static void addPart (Part SP) {
        allParts.add(SP);
    }

    //used in add product form
    public void addProducts(Product product) {
        this.allProducts.add(product);

    }

    public static void addProduct (Product SP) {
        allProducts.add(SP);

    }


}
