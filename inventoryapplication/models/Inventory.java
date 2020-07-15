package inventoryapplication.models;

import inventoryapplication.utilities.PriceFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The main inventory class that holds all of the parts and products
 * @author Aaron Echols
 *
 */
public class Inventory
{

	private ObservableList<Parts> partList;
    private ObservableList<Products> productList;
    
    public Inventory()
    {
	partList = FXCollections.observableArrayList();
	productList = FXCollections.observableArrayList();
    }
    
    public ObservableList<Parts> getPartList()
    {
    	return partList;
    }
    
    public ObservableList<Products> getProductList()
    {
    	return productList;
    }
    
    public void addPart(Parts partToAdd)
    {
        if(partToAdd != null)
        {
            partList.add(partToAdd);
	}
    }
    
    public boolean removePart(Parts partToRemove)
    {
        boolean result = false;
        if(partToRemove != null)
        {
            result = partList.remove(partToRemove);
        }
        return result;
    }
    
    public Parts lookupPart(String name)
    {
	Parts partFound = null;
        for(Parts part : partList)
        {
            if(name.toLowerCase().equals(part.getName().toLowerCase()))
            {
                partFound = part;
                break;
            }
        }
        return partFound;
    }
    
    public void updatePart(Parts oldPart, Parts newPart)
    {
	newPart.copyPartID(oldPart);
	removePart(oldPart);
	addPart(newPart);
    }
    public ObservableList<Parts> searchForPartByString(String str)
    {
        ObservableList<Parts> partsFound = FXCollections.observableArrayList();
        
        String name = null;
	String companyName = null;
	String machineID = null;
	String price = null;
        
        for(Parts part : partList)
        {
            name = part.getName();
            machineID = part instanceof InhouseParts ? String.valueOf(((InhouseParts)part).getMachineID()) : "";
            companyName = part instanceof OutsourcedParts ? ((OutsourcedParts)part).getCompanyName() : "";
            price = PriceFormatter.format(part.getPrice());
            
            if(name.toLowerCase().contains(str.toLowerCase()) || 
                    machineID.toLowerCase().contains(str.toLowerCase()) || 
                    companyName.toLowerCase().contains(str.toLowerCase()) || 
                    price.toLowerCase().contains(str.toLowerCase()))
            {
                partsFound.add(part);
            }
        }
        return partsFound;
    }
    public void addProduct(Products productToAdd)
    {
        if (productToAdd != null)
        {
            productList.add(productToAdd);
        }
    }
    public boolean removeProduct(Products productToRemove) throws Exception
    {
        boolean result = false;
        
        if (productToRemove ==  null) return false;
        
        if(!productToRemove.getParts().isEmpty())
        {
            throw new Exception("Product still contains one or more parts.");
        }
        result = this.productList.remove(productToRemove);
        
        return result;
    }
    public boolean removeNotEmptyProduct(Products productToRemove)
    {
        boolean result = false;
        
        if (productToRemove ==  null) return result;
        
        result = this.productList.remove(productToRemove);
        
        return result;
    }
    public Products lookupProduct(String name)
    {
        Products productFound = null;
        
        for(Products product : productList)
        {
            if(name.toLowerCase().equals(product.getName().toLowerCase()))
            {
                productFound = product;
                break;
            }
        }
        return productFound;
	}
    public void updateProduct(Products oldProduct, Products newProduct)
    {
        newProduct.copyProductID(oldProduct);
        removeNotEmptyProduct(oldProduct);
        addProduct(newProduct);
    }
    public ObservableList<Products> searchForProductByString(String str)
    {
        ObservableList<Products> productsFound = FXCollections.observableArrayList();
        
        String name = null;
        String price = null;
        
        PRODUCTS:
        for(Products product : productList)
        {
            name = product.getName();
            price = PriceFormatter.format(product.getPrice());
            
            for(Parts part : product.getParts())
            {
                if(part.getName().toLowerCase().contains(str.toLowerCase()))
                {
                    productsFound.add(product);
                    continue PRODUCTS;
                }
            }
            if(name.toLowerCase().contains(str.toLowerCase()) || price.toLowerCase().contains(str.toLowerCase()))
            {
                productsFound.add(product);
            }
        }
        return productsFound;
    }
}