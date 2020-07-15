package inventoryapplication.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class InhouseParts extends Parts
{
	
	private IntegerProperty machineID = new SimpleIntegerProperty();
	
	
	public InhouseParts()
        {
		this("New Part", 0, 0);
		setName(getName() + " " + getPartID());
	}
	
	public InhouseParts(String name, double price, int machineID) throws IllegalArgumentException
        {
		this(name, price, 0, 0, 0, machineID);
	}
	
	public InhouseParts(String name, double price, int instock, int min, int max, int machineID) throws IllegalArgumentException
        {

		setPartID();

		setName(name);
		setPrice(price);
		setMax(max);
		setMin(min);
		setInstock(instock);
		
		setMachineID(machineID);
	}
	
	
	public int getMachineID()
        {
		return machineID.get();
	}
	
	public void setMachineID(int machineID)
        {
		this.machineID.set(machineID);
	}
	
	public IntegerProperty machineIDProperty()
        {
		return machineID;
	}
}