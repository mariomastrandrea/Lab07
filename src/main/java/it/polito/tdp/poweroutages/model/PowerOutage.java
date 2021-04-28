package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;

public class PowerOutage
{
	private final int id;
	private final Nerc nerc;
	private final int customersAffected; 
	private final LocalDateTime dateEventBegan;
	private final LocalDateTime dateEventFinished;
	
	
	public PowerOutage(int id, Nerc nerc, int customersAffected, 
			LocalDateTime dateEventBegan, LocalDateTime dateEventFinished)
	{
		this.id = id;
		this.nerc = nerc;
		this.customersAffected = customersAffected;
		this.dateEventBegan = dateEventBegan;
		this.dateEventFinished = dateEventFinished;
	}

	public int getId()
	{
		return this.id;
	}

	public Nerc getNerc()
	{
		return this.nerc;
	}

	public int getCustomersAffected()
	{
		return this.customersAffected;
	}

	public LocalDateTime getDateEventBegan()
	{
		return this.dateEventBegan;
	}

	public LocalDateTime getDateEventFinished()
	{
		return this.dateEventFinished;
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
		PowerOutage other = (PowerOutage) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
