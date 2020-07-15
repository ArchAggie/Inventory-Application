package inventoryapplication.views_controllers;

import java.util.Optional;

import inventoryapplication.InventoryApplication;
import inventoryapplication.models.Inventory;
import inventoryapplication.models.Parts;
import inventoryapplication.models.Products;

import inventoryapplication.utilities.CurrencyCell;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainScreenController
{
    private InventoryApplication mainApp;
    private Inventory inventory;
    
    @FXML
    private TableView<Parts> partTable;
    @FXML
    private TableColumn<Parts, Integer> partIDColumn;
    @FXML
    private TableColumn<Parts, String> partNameColumn;
    @FXML
    private TableColumn<Parts, Integer> partQTYColumn;
    @FXML
    private TableColumn<Parts, Double> partPriceColumn;
    
    @FXML
    private TableView<Products> productTable;
    @FXML
    private TableColumn<Products, Integer> productIDColumn;
    @FXML
    private TableColumn<Products, String> productNameColumn;
    @FXML
    private TableColumn<Products, Integer> productQTYColumn;
    @FXML
    private TableColumn<Products, Double> productPriceColumn;
    
    @FXML
    private Button modifyPartButton;
    @FXML
    private Button deletePartButton;
    @FXML
    private Button modifyProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private TextField searchPartField;
    @FXML
    private TextField searchProductField;
    
    public MainScreenController()
    {
        
    };
    
    @FXML
    private void initialize()
    {
        partIDColumn.setCellValueFactory(cell -> cell.getValue().partIDProperty().asObject());
        partNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        partQTYColumn.setCellValueFactory(cell -> cell.getValue().instockProperty().asObject());
        partPriceColumn.setCellValueFactory(cell -> cell.getValue().priceProperty().asObject());
        partPriceColumn.setCellFactory(param -> new CurrencyCell<Parts>());
        
        productIDColumn.setCellValueFactory(cell -> cell.getValue().productIDProperty().asObject());
        productNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        productQTYColumn.setCellValueFactory(cell -> cell.getValue().instockProperty().asObject());
        productPriceColumn.setCellValueFactory(cell -> cell.getValue().priceProperty().asObject());
        productPriceColumn.setCellFactory(param -> new CurrencyCell<Products>());
        
        changeButtonState(partTable, true);
        changeButtonState(productTable, true);
        
        partTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
                changeButtonState(partTable, newSel == null));
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
                changeButtonState(productTable, newSel == null));
        
        searchPartField.textProperty().addListener((obs, oldText, newText) ->
        {
            if (newText == null || newText.isEmpty())
            {
                partTable.setItems(inventory.getPartList());
            }
            else
            {
                partTable.setItems(inventory.searchForPartByString(newText));
            }
        });
        searchProductField.textProperty().addListener((obs, oldText, newText) ->
        {
            if (newText == null || newText.isEmpty())
            {
                productTable.setItems(inventory.getProductList());
            }
            else
            {
                productTable.setItems(inventory.searchForProductByString(newText));
            }
        });
    }
    
    public void setMainScreen(InventoryApplication app)
    {
        mainApp = app;
        inventory = app.getInventory();
        
        partTable.setItems(inventory.getPartList());
        productTable.setItems(inventory.getProductList());
    }
    
    private void changeButtonState(TableView<?> table, boolean state)
    {
        if (table.equals(partTable))
        {
            modifyPartButton.setDisable(state);
            deletePartButton.setDisable(state);
        }
        else if (table.equals(productTable))
        {
            modifyProductButton.setDisable(state);
            deleteProductButton.setDisable(state);
        }
    }
    
    @FXML
    private void handleExit()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.initOwner(mainApp.getPrimaryStage());
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK)
        {
            Platform.exit();
        }
    }
    
    @FXML
    private void handleAddPart()
    {
        mainApp.showPartDialog(null);
    }
    
    @FXML
    private void handleModifyPart()
    {
        Parts existingPart = partTable.getSelectionModel().getSelectedItem();
        mainApp.showPartDialog(existingPart);
    }
    
    @FXML
    private void handleSearchPart()
    {
        String searchText = searchPartField.getText();
        if(searchText == null || searchText.isEmpty())
        {
            partTable.setItems(inventory.getPartList());
        }
        ObservableList<Parts> partsFound = inventory.searchForPartByString(searchText);
        partTable.setItems(partsFound);
    }
    
    @FXML
    private void handleDeletePart()
    {
        Parts part = partTable.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText("Are you sure you want to delete " + part.getName() + "?");
        alert.initOwner(mainApp.getPrimaryStage());
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK)
        {
            inventory.removePart(part);
        }
    }
    
    @FXML
    private void handleAddProduct()
    {
        mainApp.showProductDialog(null);
    }
    
    @FXML
    private void handleModifyProduct()
    {
        Products existingProduct = productTable.getSelectionModel().getSelectedItem();
        mainApp.showProductDialog(existingProduct);
    }
    
    @FXML
    private void handleSearchProduct()
    {
        String searchText = searchProductField.getText();
        if(searchText == null || searchText.isEmpty())
        {
            productTable.setItems(inventory.getProductList());
        }
        ObservableList<Products> productsFound = inventory.searchForProductByString(searchText);
        productTable.setItems(productsFound);
    }
    
    @FXML
    private void handleDeleteProduct()
    {
        Products product = productTable.getSelectionModel().getSelectedItem();
        
        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Delete?");
        confirmation.setHeaderText("Are you sure you want to delete " + product.getName() + "?");
        confirmation.initOwner(mainApp.getPrimaryStage());
        Optional<ButtonType> confirmResult = confirmation.showAndWait();
        
        try
        {
            if (confirmResult.get() == ButtonType.OK)
            {
                inventory.removeProduct(product);
            }
        }
        catch (Exception e)
        {
            Alert warning = new Alert(AlertType.WARNING);
            warning.setTitle("Product Not Empty!");
            warning.setContentText("This product still contains one or more parts. \n"
                    + "Are you sure you want to delete this product?");
            warning.getButtonTypes().clear();
            warning.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> deleteResult = warning.showAndWait();
            
            if (deleteResult.get() == ButtonType.YES)
            {
                inventory.removeNotEmptyProduct(product);
            }
        }
    }
}