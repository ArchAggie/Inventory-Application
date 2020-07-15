package inventoryapplication.models;

import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Products
{
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty productID = new SimpleIntegerProperty();
    private DoubleProperty price = new SimpleDoubleProperty();
    private IntegerProperty instock = new SimpleIntegerProperty();
    private IntegerProperty min = new SimpleIntegerProperty();
    private IntegerProperty max = new SimpleIntegerProperty();
    private ObservableList<Parts> parts = FXCollections.observableArrayList();
    private static int nextProductID = 1;
    public Products(String name, double price, List<Parts> parts) throws IllegalArgumentException
    {
        this(name, price, 0, 0, 0, parts);
    }
    public Products(String name, double price, Parts firstPart)  throws IllegalArgumentException
    {
        this(name, price, 0, 0, 0, firstPart);
    }
    public Products(String name, double price, int instock, int min, int max, Parts firstPart)  throws IllegalArgumentException
    {
        this(name, price, instock, min, max, FXCollections.observableArrayList(firstPart));
    }
    public Products(String name, double price, int instock, int min, int max, List<Parts> parts) throws IllegalArgumentException
    {
        setProductID();
        
        this.parts.setAll(parts);
       
        setName(name);
        setPrice(price);
        setMax(max);
        setMin(min);
        setInstock(instock);
    }
    
    public String getName()
    {
        return name.get();
    }
    
    public int getProductID()
    {
        return productID.get();
    }
    
    public double getPrice()
    {
        return price.get();
    }
    
    public int getInstock()
    {
        return instock.get();
    }
    
    public int getMin()
    {
        return min.get();
    }
    
    public int getMax()
    {
        return max.get();
    }
    
    public StringProperty nameProperty()
    {
        return name;
    }
    
    public IntegerProperty productIDProperty()
    {
        return productID;
    }
    
    public DoubleProperty priceProperty()
    {
        return price;
    }
    
    public IntegerProperty instockProperty()
    {
        return instock;
    }
    
    public IntegerProperty minProperty()
    {
        return min;
    }
    
    public IntegerProperty maxProperty()
    {
        return max;
    }
    
    public void setName(String name)
    {
        if(name == null || name.isEmpty())
        {
            name = "New Product";
        }
        this.name.set(name);
    }
    
    public void setProductID()
    {
        this.productID.set(nextProductID++);
    }
    
    public void setPrice(double price) throws IllegalArgumentException
    {
        if(price < 0)
        {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        double total = 0;
        
        for(Parts part : parts)
        {
            total += part.getPrice();
        }
        
        if(price < total)
        {
            throw new IllegalArgumentException("Price cannot be less than the sum of its parts");
        }
        this.price.set(price);
    }
    
    public void setInstock(int instock) throws IllegalArgumentException
    {
        if(instock < getMin())
        {
            throw new IllegalArgumentException("Amount of stock needs to be less than or equal to the minumum.");
        }
        else if(instock > getMax())
        {
            throw new IllegalArgumentException("Amount of stock needs to be greater than or equal to the maximum.");
        }
        this.instock.set(instock);
    }
    
    public void setMin(int min) throws IllegalArgumentException
    {
        if(min > getMax())
        {
            throw new IllegalArgumentException("Minimum amount of stock needs to be less than or equal to the the maximum.");
        }
        this.min.set(min);
    }
    
    public void setMax(int max) throws IllegalArgumentException
    {
        if(max < getMin())
        {
            throw new IllegalArgumentException("Maximum amount of stock needs to be greater than or equal to the the minimum.");
        }
        this.max.set(max);
    }
    
    public void addPart(Parts partToAdd)
    {
        this.parts.add(partToAdd);
    }
    
    public Parts lookupPart(int partID)
    {
        Parts partFound = null;
        
        for(Parts part : parts)
        {
            if(part.getPartID() == partID)
            {
                partFound = part;
                break;
            }
        }
        return partFound;
    }
    
    public boolean removePart(int partID)
    {
        boolean result = false;
        
        if (parts == null || parts.isEmpty())
        {
            return result;
        }
        Parts partToRemove = lookupPart(partID);
        
        if(partToRemove != null)
        {
            result = this.parts.remove(partToRemove);
        }
        return result;
    }
    
    public ObservableList<Parts> getParts()
    {
        return parts;
    }
    
    public void copyProductID(Products oldProduct)
    {
        this.productID.set(oldProduct.getProductID());
    }
}