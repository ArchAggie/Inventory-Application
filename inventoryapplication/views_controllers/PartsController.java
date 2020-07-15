package inventoryapplication.views_controllers;

import java.text.ParseException;
import java.util.Optional;

import inventoryapplication.models.Inventory;
import inventoryapplication.models.Parts;
import inventoryapplication.models.InhouseParts;
import inventoryapplication.models.OutsourcedParts;

import inventoryapplication.utilities.PriceFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class PartsController
{
    private Stage dialogStage;
    private boolean isNewPart;
    private Inventory inventory;
    private Parts part;
    @FXML
    private Label titleLabel;
    @FXML
    private RadioButton inhouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
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
    private Label sourceLabel;
    @FXML
    private TextField sourceField;
    
    public PartsController()
    {
        
    };
    
    @FXML
    private void initialize()
    {
        ToggleGroup radioGroup = new ToggleGroup();
        
        inhouseRadio.setToggleGroup(radioGroup);
        outsourcedRadio.setToggleGroup(radioGroup);
        
        radioGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> sourceChanged(newToggle));
        
        priceField.textProperty().addListener((obs, oldStr, newStr) ->
        {
            if (newStr == null || newStr.isEmpty()) return;
            if (!newStr.contains("$"))
            {
                priceField.setText("$" + newStr);
            }
        });
    }
    
    public void setDialogScreen(Stage stage, Inventory existingInventory, Parts editPart)
    {
        dialogStage = stage;
        inventory = existingInventory;
        part = editPart;
        
        if (editPart == null)
        {
            isNewPart = true;
            titleLabel.setText("Add Part");
        }
        else
        {
            isNewPart = false;
            titleLabel.setText("Modify Part");
            setPartInfo(editPart);
        }
    }
    
    public void setPartInfo(Parts part)
    {
        if (part instanceof InhouseParts)
        {
            inhouseRadio.setSelected(true);
            sourceField.setText(Integer.toString(((InhouseParts)part).getMachineID()));
            sourceLabel.setText("Machine ID");
        }
        else if (part instanceof OutsourcedParts)
        {
            outsourcedRadio.setSelected(true);
            sourceField.setText(((OutsourcedParts)part).getCompanyName());
            sourceLabel.setText("Company Name");
        }
        
        idField.setText(Integer.toString(part.getPartID()));
        nameField.setText(part.getName());
        instockField.setText(Integer.toString(part.getInstock()));
        minField.setText(Integer.toString(part.getMin()));
        maxField.setText(Integer.toString(part.getMax()));
        priceField.setText(PriceFormatter.format(part.getPrice()));
    }
    
    private void sourceChanged(Toggle toggle)
    {
        String labelText = "";
        
        if (toggle.equals(inhouseRadio))
        {
            labelText = "Machine ID";
        }
        else if (toggle.equals(outsourcedRadio))
        {
            labelText = "Company Name";
        }
        sourceLabel.setText(labelText);
        
        if (sourceField.getText().isEmpty())
        {
            sourceField.setPromptText(labelText);
        }
    }
    
    @FXML
    private void handleSave()
    {
        Parts editPart = null;
        boolean isInhouse = inhouseRadio.isSelected();
        
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("Part Not Saved!");
        
        String name = nameField.getText();
        String instockString = instockField.getText();
        String priceString = priceField.getText();
        String maxString = maxField.getText();
        String minString = minField.getText();
        String sourceString = sourceField.getText();
        
        try
        {
            if(name.isEmpty() || instockString.isEmpty() ||
                    priceString.isEmpty() || maxString.isEmpty() ||
                    minString.isEmpty() || sourceString.isEmpty())
            {
                throw new IllegalArgumentException("All text fields must have a value.");
            }
            
            double price = PriceFormatter.parse(priceString);
            int instock = Integer.parseInt(instockString);
            int min = Integer.parseInt(minString);
            int max = Integer.parseInt(maxString);
            
            String companyName = null;
            int machineID = -1;
            
            if (isInhouse)
            {
                machineID = Integer.parseInt(sourceString);
                editPart = new InhouseParts(name, price, instock, min, max, machineID);
            }
            else
            {
                companyName = sourceString;
                editPart = new OutsourcedParts(name, price, instock, min, max, companyName);
            }
            
            if(isNewPart)
            {
                Parts searchPart = inventory.lookupPart(editPart.getName());
                if (searchPart == null || existingPartFound() == ButtonType.YES)
                {
                    inventory.addPart(editPart);
                }
            }
            else
            {
                inventory.updatePart(part, editPart);
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
            alert.setContentText(e.getMessage());;
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
        if (isNewPart)
        {
            alert.setContentText("Exit without saving new part?");
        }
        else
        {
            alert.setContentText("Exit without updating part?");
        }
        alert.setHeaderText("Part Not Saved!");
        alert.initOwner(dialogStage);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK)
        {
            dialogStage.close();
        }
    }
    
    private ButtonType existingPartFound()
    {
        ButtonType result = ButtonType.NO;
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("Duplicate Part Found");
        alert.setHeaderText("Add duplicate part?");
        alert.setContentText("A part with that name already exists in the inventory. \n" + "Do you want to add this part?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        
        Optional<ButtonType> answer = alert.showAndWait();
        result = answer.get();
        
        return result;
    }
}