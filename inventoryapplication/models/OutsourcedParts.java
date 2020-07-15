package inventoryapplication.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OutsourcedParts extends Parts
{
    private StringProperty companyName = new SimpleStringProperty();
    
    public OutsourcedParts()
    {
        this("New Part", 0, "No Supplier Specified");
        setName(getName() + " " + getPartID());
    }
    
    public OutsourcedParts(String name, double price, String companyName) throws IllegalArgumentException
    {
        this(name, price, 0, 0, 0, companyName);
    }
    
    public OutsourcedParts(String name, double price, int instock, int min, int max, String companyName) throws IllegalArgumentException
    {
        setPartID();
        
        setName(name);
        setPrice(price);
        setMax(max);
        setMin(min);
        setInstock(instock);
        
        setCompanyName(companyName);
    }
    
    public String getCompanyName()
    {
        return companyName.get();
    }
    
    public void setCompanyName(String companyName)
    {
        if(companyName == null || companyName.isEmpty())
        {
            companyName = "No Supplier Specified";
        }
        this.companyName.set(companyName);
    }
    
    public StringProperty companyNameProperty()
    {
        return companyName;
    }
}