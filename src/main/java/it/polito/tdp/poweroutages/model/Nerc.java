package it.polito.tdp.poweroutages.model;

public class Nerc 
{	
	private final int id;
	private final String value;

	
	public Nerc(int id, String value) 
	{
		this.id = id;
		this.value = value;
	}

	public int getId() 
	{
		return id;
	}

	public String getValue() 
	{
		return value;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nerc other = (Nerc) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() 
	{
		return this.value;
	}
	

}
