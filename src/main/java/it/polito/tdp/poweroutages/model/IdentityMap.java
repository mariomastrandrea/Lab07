package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentityMap
{
	private Map<Integer, Nerc> nercsById;
	private Map<Nerc, List<PowerOutage>> powerOutagesByNerc;
	
	public IdentityMap()
	{
		this.nercsById = new HashMap<>();
		this.powerOutagesByNerc = new HashMap<>();
	}
	
	public void addAllNercs(Iterable<Nerc> nercs)
	{
		for(Nerc n : nercs)
			if(n != null)
				this.nercsById.put(n.getId(), n);
	}
	
	public void addAllPowerOutages(Iterable<PowerOutage> powerOutages)
	{
		for(PowerOutage po : powerOutages)
			if(po != null)
			{
				Nerc nerc = po.getNerc();
				if(!this.powerOutagesByNerc.containsKey(nerc))
					this.powerOutagesByNerc.put(nerc, new ArrayList<PowerOutage>());
				
				this.powerOutagesByNerc.get(nerc).add(po);
			}
	}
	
	public Nerc getNerc(int id)
	{
		return this.nercsById.get(id);
	}
	
	public boolean existsNerc(int id)
	{
		return this.nercsById.containsKey(id);
	}
	
	public List<PowerOutage> getPowerOutages(Nerc nerc)
	{
		return this.powerOutagesByNerc.get(nerc);
	}
	
}
