package inventoryapplication.views_controllers;

import java.text.ParseException;
import java.util.Optional;

import inventoryapplication.models.Inventory;
import inventoryapplication.models.Parts;
import inventoryapplication.models.Products;

import inventoryapplication.utilities.CurrencyCell;
import inventoryapplication.utilities.PriceFormatter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductsController
{
    private Stage dialogStage;
    private boolean isNewProduct;
    private Inventory inventory;
    private Products product;
    private ObservableList<Parts> parts;
    
    @FXML
    private Label titleLabel;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField instockField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Parts> addPartTable;
    @FXML
    private TableColumn<Parts, Integer> addPartIDColumn;
    @FXML
    private TableColumn<Parts, String> addPartNameColumn;
    @FXML
    private TableColumn<Parts, Integer> addPartQTYColumn;
    @FXML
    private TableColumn<Parts, Double> addPartPriceColumn;
    @FXML
    private TableView<Parts> delPartTable;
    @FXML
    private TableColumn<Parts, Integer> delPartIDColumn;
    @FXML
    private TableColumn<Parts, String> delPartNameColumn;
    @FXML
    private TableColumn<Parts, Integer> delPartQTYColumn;
    @FXML
    private TableColumn<Parts, Double> delPartPriceColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    
    public ProductsController()
    {
        
    };
    
    @FXML
    private void initialize()
    {
        addPartIDColumn.setCellValueFactory(cell -> cell.getValue().partIDProperty().asObject());
        addPartNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        addPartQTYColumn.setCellValueFactory(cell -> cell.getValue().instockProperty().asObject());
        addPartPriceColumn.setCellValueFactory(cell -> cell.getValue().priceProperty().asObject());
        addPartPriceColumn.setCellFactory(param -> new CurrencyCell<Parts>());
        
        delPartIDColumn.setCellValueFactory(cell -> cell.getValue().partIDProperty().asObject());
        delPartNameColumn.setCellValueFactory(cell -> cell.getValue().nameProperty());
        delPartQTYColumn.setCellValueFactory(cell -> cell.getValue().instockProperty().asObject());
        delPartPriceColumn.setCellValueFactory(cell -> cell.getValue().priceProperty().asObject());
        delPartPriceColumn.setCellFactory(param -> new CurrencyCell<Parts>());
        
        searchField.textProperty().addListener((obs, oldText, newText) ->
        {
            if (newText == null || newText.isEmpty())
            {
                addPartTable.setItems(inventory.getPartList());
            }
            else
            {
                addPartTable.setItems(inventory.searchForPartByString(newText));
            }
        });
        priceField.textProperty().addListener((obs, oldStr, newStr) ->
        {
            if (newStr == null || newStr.isEmpty()) return;
            
            if (!newStr.contains("$"))
            {
                priceField.setText("$" + newStr);
            }
        });
        addButton.setDisable(true);
        deleteButton.setDisable(true);
        
        addPartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
                addButton.setDisable(newSel == null));
        delPartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
                deleteButton.setDisable(newSel == null));
    }
    
    public void setDialogScreen(Stage stage, Inventory existingInventory, Products editProduct)
    {
        dialogStage = stage;
        inventory = existingInventory;
        product = editProduct;
        
        addPartTable.setItems(inventory.getPartList());
        
        if (editProduct == null)
        {
            isNewProduct = true;
            titleLabel.setText("Add Product");
        }
        else
        {
            isNewProduct = false;
            titleLabel.setText("Modify Product");
            setProductInfo(editProduct);
        }
    }
    
    public void setProductInfo(Products product)
    {
        idField.setText(Integer.toString(product.getProductID()));
        nameField.setText(product.getName());
        instockField.setText(Integer.toString(product.getInstock()));
        minField.setText(Integer.toString(product.getMin()));
        maxField.setText(Integer.toString(product.getMax()));
        priceField.setText(PriceFormatter.format(product.getPrice()));
        delPartTable.setItems(product.getParts());
    }
    
    @FXML
    private void handleSearch()
    {
        String searchText = searchField.getText();
        if(searchText == null || searchText.isEmpty())
        {
            addPartTable.setItems(inventory.getPartList());
        }
        ObservableList<Parts> partsFound = inventory.searchForPartByString(searchText);
        addPartTable.setItems(partsFound);
    }
    
    @FXML
    private void handleAdd()
    {
        Parts partToAdd = addPartTable.getSelectionModel().getSelectedItem();
        try
        {
            double price = 0;
            String priceString = priceField.getText();
            if(priceString != null && !priceString.isEmpty())
            {
                price = PriceFormatter.parse(priceString);
            }
            delPartTable.getItems().add(partToAdd);
            
            double partCost = getCostOfParts();
            
            if (partCost > price)
            {
                price += partToAdd.getPrice();
                priceString = PriceFormatter.format(price);
                priceField.setText(priceString);
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleDelete()
    {
        Parts partToDelete = delPartTable.getSelectionModel().getSelectedItem();
        
        delPartTable.getItems().remove(partToDelete);
        try
        {
            double price = 0;
            String priceString = priceField.getText();
            if(priceString != null && !priceString.isEmpty())
            {
                price = PriceFormatter.parse(priceString);
            }
            if (price - partToDelete.getPrice() >= 0)
            {
                price -= partToDelete.getPrice();
                priceString = PriceFormatter.format(price);
                priceField.setText(priceString);
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSave()
    {
        Products editProduct = null;
        
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("Product Not Saved!");
        
        String name = nameField.getText();
        String instockString = instockField.getText();
        String priceString = priceField.getText();
        String maxString = maxField.getText();
        String minString = minField.getText();
        parts = delPartTable.getItems();
        try
        {
            if(name.isEmpty() || instockString.isEmpty() ||
                    priceString.isEmpty() || maxString.isEmpty() ||
                    minString.isEmpty() )
            {
                throw new IllegalArgumentException("All text fields must have a value.");
            }
            if (parts.isEmpty())
            {
                throw new IllegalArgumentException("Products must contain at least one part.");
            }
            
            double price = PriceFormatter.parse(priceString);
            int instock = Integer.parseInt(instockString);
            int min = Integer.parseInt(minString);
            int max = Integer.parseInt(maxString);
            
            double partCost = getCostOfParts();
            
            if (partCost > price)
            {
                throw new IllegalArgumentException("Price cannot be less than the total\n" + "price of all the parts it contains.");
            }
            editProduct = new Products(name, price, instock, min, max, parts);
            
            if(isNewProduct)
            {
                Products searchProduct = inventory.lookupProduct(editProduct.getName());
                if (searchProduct == null || existingProductFound() == ButtonType.YES)
                {
                    inventory.addProduct(editProduct);
                }
            }
            else
            {
                inventory.updateProduct(product, editProduct);
            }
        }
        catch (NumberFormatException e)
        {
            alert.setContentText("There was an invalid number in one of the fields. \n" + "Please try again.");
            alert.showAndWait();
            return;
        }
        catch (IllegalArgumentException e)
        {
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        catch (ParseException e)
        {
            alert.setContentText("That is not a valid price. \n" + "Please try again.");
            alert.showAndWait();
            return;
        }
        dialogStage.close();
    }
    
    @FXML
    private void handleCancel()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        if (isNewProduct)
        {
            alert.setContentText("Exit without saving new product?");
        }
        else
        {
            alert.setContentText("Exit without updating product?");
        }
        alert.setHeaderText("Product Not Saved!");
        alert.initOwner(dialogStage);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            dialogStage.close();
        }
    }
    
    private double getCostOfParts()
    {
        double partCost = 0;
        ObservableList<Parts> partsContained = delPartTable.getItems();
        
        if (partsContained == null || partsContained.isEmpty())
        {
            return partCost;
        }
        for(Parts part : partsContained)
        {
            partCost += part.getPrice();
        }
        return partCost;
    }
    
    private ButtonType existingProductFound()
    {
        ButtonType result = ButtonType.NO;
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("Duplicate Product Found");
        alert.setHeaderText("Add duplicate product?");
        alert.setContentText("A product with that name already exists in the inventory. \n" + "Do you want to add this product?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        
        Optional<ButtonType> answer = alert.showAndWait();
        result = answer.get();
        return result;
    }
}