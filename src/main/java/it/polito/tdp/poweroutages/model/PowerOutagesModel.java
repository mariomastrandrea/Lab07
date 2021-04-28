package it.polito.tdp.poweroutages.model;

import java.util.Collection;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class PowerOutagesModel 
{
	private PowerOutageDAO powerOutageDao;
	private IdentityMap idMap;
	
	
	public PowerOutagesModel() 
	{
		this.idMap = new IdentityMap();
		this.powerOutageDao = new PowerOutageDAO();
	}
	
	public Collection<Nerc> getAllNercs() 
	{
		Collection<Nerc> allNercs = this.powerOutageDao.getAllNercs();
		this.idMap.addAllNercs(allNercs);
		return allNercs;
	}

	public List<PowerOutage> computeWorstCase(Nerc selectedNerc, int maxYears, int maxHours)
	{
		List<PowerOutage> powerOutagesOfNerc = this.idMap.getPowerOutages(selectedNerc);
		
		if(powerOutagesOfNerc == null)
			powerOutagesOfNerc = this.powerOutageDao.getPowerOutagesOf(selectedNerc, idMap);
		
		
		
		
		return null;
	}
	
	@SuppressWarnings("unused")
	private void recursiveWorstCaseComputation(List<PowerOutage> partialSolution, 
			List<PowerOutage> remainedPowerOutages, List<PowerOutage> worstCase, int maxYears, int maxHours)
	{
		//TODO: implement recursion
	}

}
