package inventoryapplication;

import java.io.IOException;

import inventoryapplication.models.InhouseParts;
import inventoryapplication.models.Inventory;
import inventoryapplication.models.OutsourcedParts;
import inventoryapplication.models.Parts;
import inventoryapplication.models.Products;
import inventoryapplication.views_controllers.MainScreenController;
import inventoryapplication.views_controllers.PartsController;
import inventoryapplication.views_controllers.ProductsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InventoryApplication extends Application
{
    private Stage primaryStage;
    private BorderPane root;
    private Inventory inventory;
    
    public InventoryApplication()
    {
    	inventory = new Inventory();
    	
    	try
        {
            inventory.addPart(new InhouseParts("First Part", 1.50, 75, 10, 100, 546));
            inventory.addPart(new InhouseParts("Second Part", 3.75, 17, 5, 20, 112));
            inventory.addPart(new OutsourcedParts("Third Part", 10, 2, 0, 5, "ACME"));
            inventory.addPart(new OutsourcedParts("Fourth Part", 12.10, 4, 1, 10, "Brand X"));
            inventory.addPart(new InhouseParts("Fifth Part", 10, 46, 10, 100, 10101));
            inventory.addPart(new InhouseParts("Sixth Part", 50, 12, 5, 20, 112));
            inventory.addPart(new OutsourcedParts("Seventh Part", 25.64, 0, 0, 5, "ACME"));
            inventory.addPart(new OutsourcedParts("Eighth Part", .02, 7, 1, 10, "Brand X"));
            inventory.addPart(new OutsourcedParts("Ninth Part", 1, 264, 100, 1000, "Generic Industries"));
            inventory.addPart(new OutsourcedParts("Tenth Part", 2, 745, 100, 1000, "Generic Industries"));
            
            Products product1 = new Products("First Product", 500, 88, 10, 100, inventory.lookupPart("First Part"));
            product1.addPart(inventory.lookupPart("Second Part"));
            product1.addPart(inventory.lookupPart("Ninth Part"));
            
            Products product2 = new Products("Second Product", 250, 49, 10, 100, inventory.lookupPart("Sixth Part"));
            product2.addPart(inventory.lookupPart("Sixth Part"));
            product2.addPart(inventory.lookupPart("Sixth Part"));
            product2.addPart(inventory.lookupPart("Sixth Part"));
            product2.addPart(inventory.lookupPart("Sixth Part"));
            
            Products product3 = new Products("Third Product", 70, 12, 10, 100, inventory.lookupPart("Tenth Part"));
            product3.addPart(inventory.lookupPart("Third Part"));
            product3.addPart(inventory.lookupPart("Sixth Part"));
            product3.addPart(inventory.lookupPart("Tenth Part"));
            product3.addPart(inventory.lookupPart("First Part"));
            
            inventory.addProduct(product1);
            inventory.addProduct(product2);
            inventory.addProduct(product3);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        
        initRootLayout();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
    
    public Inventory getInventory()
    {
    	return inventory;
    }
    
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }
    
    public void initRootLayout()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApplication.class.getResource("views_controllers/MainScreen.fxml"));
            root = (BorderPane)loader.load();
            
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            
            MainScreenController controller = loader.getController();
            controller.setMainScreen(this);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void showPartDialog(Parts part)
    {
        String title;
        if (part == null)
        {
            title = "Add a New Part";
        }
        else
        {
            title = "Modify an Existing Part";
        }
    	try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApplication.class.getResource("views_controllers/Parts.fxml"));
            AnchorPane partDialogRoot = (AnchorPane) loader.load();
            
            Stage partDialogStage = new Stage();
            partDialogStage.setTitle(title);
            partDialogStage.initModality(Modality.WINDOW_MODAL);
            partDialogStage.initOwner(primaryStage);
            //partDialogStage.getIcons().add(new Image("file:resources/part_icon.png"));
            
            Scene scene = new Scene(partDialogRoot);
            partDialogStage.setScene(scene);
            
            PartsController controller = loader.getController();
            controller.setDialogScreen(partDialogStage, inventory, part);
            
            partDialogStage.showAndWait();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void showProductDialog(Products product)
    {
        String title;
        if (product == null)
        {
            title = "Add a New Product";
        }
        else
        {
            title = "Modify an Existing Product";
        }
    	try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApplication.class.getResource("views_controllers/Products.fxml"));
            AnchorPane productDialogRoot = (AnchorPane) loader.load();
            
            Stage productDialogStage = new Stage();
            productDialogStage.setTitle(title);
            productDialogStage.initModality(Modality.WINDOW_MODAL);
            productDialogStage.initOwner(primaryStage);
            Scene scene = new Scene(productDialogRoot);
            productDialogStage.setScene(scene);
            
            ProductsController controller = loader.getController();
            controller.setDialogScreen(productDialogStage, inventory, product);
            productDialogStage.showAndWait();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}