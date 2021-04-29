package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class PowerOutagesModel 
{
	private PowerOutageDAO powerOutageDao;
	private Map<Nerc, List<PowerOutage>> powerOutagesByNerc;
	
	public PowerOutagesModel() 
	{
		this.powerOutageDao = new PowerOutageDAO();
		this.powerOutagesByNerc = new HashMap<>();
	}
	
	public Collection<Nerc> getAllNercs() 
	{
		Collection<Nerc> allNercs = this.powerOutageDao.getAllNercs();
		return allNercs;
	}

	public List<PowerOutage> computeWorstCase(Nerc selectedNerc, int maxYears, int maxHours)
	{
		if(!this.powerOutagesByNerc.containsKey(selectedNerc))
		{
			List<PowerOutage> newPowerOutages = this.powerOutageDao.getPowerOutagesOf(selectedNerc);
			this.powerOutagesByNerc.put(selectedNerc, newPowerOutages);
		}
		
		List<PowerOutage> powerOutagesOfNerc = this.powerOutagesByNerc.get(selectedNerc);
		
		if(powerOutagesOfNerc.isEmpty())
			return powerOutagesOfNerc;
		
		List<PowerOutage> worstCase = new ArrayList<>();
		List<PowerOutage> partialSolution = new ArrayList<>();
		Period maxPeriod = Period.ofYears(maxYears);
		Duration maxDuration = Duration.ofHours(maxHours);
		
		this.recursiveWorstCaseComputation(partialSolution, powerOutagesOfNerc, 0, worstCase, maxPeriod, maxDuration);
		
		return worstCase;
	}
		
	private void recursiveWorstCaseComputation(List<PowerOutage> partialSolution, List<PowerOutage> allPowerOutages,
			int offset, List<PowerOutage> worstCase, Period maxPeriod, Duration maxHours)
	{	
		int size = allPowerOutages.size();
		
		if(offset == size) 
		{
			//examining last element: either partialSolution is a solution 
			//						or partialSolution without this last element is a solution
			
			if(this.respectCostrains(partialSolution, maxPeriod, maxHours))
			{
				if(totalCustomersAffected(partialSolution) > totalCustomersAffected(worstCase))
				{
					worstCase.clear();
					worstCase.addAll(partialSolution);
				}
			}
			else
			{
				List<PowerOutage> temp = partialSolution.subList(0, partialSolution.size() - 1);
				
				if(totalCustomersAffected(temp) > totalCustomersAffected(worstCase))
				{
					worstCase.clear();
					worstCase.addAll(temp);
				}
			}
			
			return; 
		}
		
		if(!this.respectCostrains(partialSolution, maxPeriod, maxHours))
			return;
		
		for(int i=offset; i<size; i++)
		{
			PowerOutage po = allPowerOutages.get(i);
			
			partialSolution.add(po);
			
			recursiveWorstCaseComputation(partialSolution, allPowerOutages, i+1, worstCase, maxPeriod, maxHours);
			
			//backtracking
			int lastIndex = partialSolution.size() - 1;
			partialSolution.remove(lastIndex);
		}
	}
	
	/*
	//meno efficiente del primo
	private void recursiveWorstCaseComputation2(List<PowerOutage> partialSolution, List<PowerOutage> allPowerOutages,
			int offset, List<PowerOutage> worstCase, Period maxPeriod, Duration maxHours)
	{
		int size = allPowerOutages.size();
		
		for(int i=offset; i<size; i++)
		{
			PowerOutage po = allPowerOutages.get(i);
			
			partialSolution.add(po);
			
			if(respectCostrains(partialSolution, maxPeriod, maxHours) && 
					totalCustomersAffected(partialSolution) > totalCustomersAffected(worstCase));
			{
				recursiveWorstCaseComputation2(partialSolution, allPowerOutages, i+1, worstCase, maxPeriod, maxHours);
			}
			//backtracking
			int lastIndex = partialSolution.size() - 1;
			partialSolution.remove(lastIndex);
		}
		
		//after last loop
		if(!partialSolution.isEmpty() && respectCostrains(partialSolution, maxPeriod, maxHours) &&
				totalCustomersAffected(partialSolution) > totalCustomersAffected(worstCase))
		{
			worstCase.clear();
			worstCase.addAll(partialSolution);
		}
	}
	*/
	
	private boolean respectCostrains(List<PowerOutage> powerOutages, Period maxPeriod, Duration maxDuration)
	{
		return !exceed(powerOutages, maxPeriod) && !exceed(powerOutages, maxDuration);
	}
	
	/**
	 * it verifies if a list of ordered power outage events exceeds a determined PERIOD of time
	 * @param powerOutages chronologically ordered list, from oldest to most recent
	 * @param maxPeriod period to not exceed by power outage events
	 * @return true, if the duration from the first event to the last event exceeds max period; false otherwise
	 */
	private boolean exceed(List<PowerOutage> powerOutages, Period maxPeriod)
	{
		if(powerOutages.isEmpty())
			return false;
		
		PowerOutage oldest = powerOutages.get(0);
		PowerOutage mostRecent = powerOutages.get(powerOutages.size() - 1);
		
		LocalDateTime oldestDate = oldest.getDateEventBegan();
		LocalDateTime mostRecentDate = mostRecent.getDateEventFinished();
		
		LocalDateTime maxOffset = oldestDate.plus(maxPeriod);
		
		return mostRecentDate.isAfter(maxOffset);
	}
	
	/**
	 * it verifies if a list of ordered power outage events exceeds a determined AMOUNT of time
	 * @param powerOutages chronologically ordered list, from oldest to most recent
	 * @param maxDuration MAX AMOUNT of time that the power outages can last, globally
	 * @return true, if the amount of time of all events exceeds max duration; false otherwise
	 */
	private boolean exceed(List<PowerOutage> powerOutages, Duration maxDuration)
	{
		Duration duration = Duration.ofHours(0);
		
		for(PowerOutage po : powerOutages)
		{
			LocalDateTime begin = po.getDateEventBegan();
			LocalDateTime end = po.getDateEventFinished();
			
			Duration eventDuration = Duration.between(begin, end);
			duration = duration.plus(eventDuration);
		}
		
		return duration.compareTo(maxDuration) > 0;
	}
	
	public long totHoursAffected(List<PowerOutage> powerOutages)
	{
		Duration duration = Duration.ofHours(0);
		
		for(PowerOutage po : powerOutages)
		{
			LocalDateTime begin = po.getDateEventBegan();
			LocalDateTime end = po.getDateEventFinished();
			
			Duration eventDuration = Duration.between(begin, end);
			duration = duration.plus(eventDuration);
		}
		
		return duration.toHours();
	}
	
	
	public int totalCustomersAffected(List<PowerOutage> powerOutages)
	{
		int tot = 0;
		
		for(PowerOutage po : powerOutages)
			tot += po.getCustomersAffected();
		
		return tot;
	}

}
