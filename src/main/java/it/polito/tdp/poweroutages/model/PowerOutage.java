package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	
	public long getHours()
	{
		return Duration.between(dateEventBegan, dateEventFinished).toHours();
	}
	
	public int getMinutes()
	{
		return Duration.between(dateEventBegan, dateEventFinished).toMinutesPart();
	}
	
	public int getYear()
	{
		return this.dateEventBegan.getYear();
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
	
	@Override
	public String toString()
	{
		return String.format("P%d", this.id);
	}
	
	public String toFormattedString()
	{
		String eventBegan = this.dateEventBegan.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String eventFinished = this.dateEventFinished.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		return String.format("%-5d %-5d %-20s %-20s %-4d %-4d %-8d", this.id, this.getYear(), 
																eventBegan, eventFinished, 
																this.getHours(), this.getMinutes(),
																this.getCustomersAffected());
	}
	
}
