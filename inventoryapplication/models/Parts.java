package inventoryapplication.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Parts
{
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty partID = new SimpleIntegerProperty();
    private DoubleProperty price = new SimpleDoubleProperty();
    private IntegerProperty instock = new SimpleIntegerProperty();
    private IntegerProperty min = new SimpleIntegerProperty();
    private IntegerProperty max = new SimpleIntegerProperty();
    
    private static int nextPartID = 1;
    
    public String getName()
    {
        return name.get();
    }
    
    public int getPartID()
    {
        return partID.get();
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
    
    public IntegerProperty partIDProperty()
    {
        return partID;
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
            name = "New Part";
        }
        this.name.set(name);
    }
    
    public void setPartID()
    {
        this.partID.set(Parts.nextPartID++);
    }
    
    public void setPrice(double price) throws IllegalArgumentException
    {
        if(price < 0)
        {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price.set(price);
    }
    
    public void setInstock(int instock) throws IllegalArgumentException
    {
        if(instock < getMin())
        {
            throw new IllegalArgumentException("Amount of stock needs to be greater than or equal to the minumum.");
        }
        else if(instock > getMax())
        {
            throw new IllegalArgumentException("Amount of stock needs to be less than or equal to the maximum.");
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
    
    public void copyPartID(Parts oldPart)
    {
        this.partID.set(oldPart.getPartID());
    }
}