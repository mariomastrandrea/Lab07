package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.poweroutages.model.IdentityMap;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.PowerOutage;

public class PowerOutageDAO 
{
	
	public Collection<Nerc> getAllNercs() 
	{
		String sqlQuery = "SELECT id, value FROM Nerc";
		Set<Nerc> nercs = new HashSet<>();

		try 
		{
			Connection connection = ConnectDB.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			ResultSet result = statement.executeQuery();

			while (result.next()) 
			{
				Nerc n = new Nerc(result.getInt("id"), result.getString("value"));
				nercs.add(n);
			}

			connection.close();
		} 
		catch (SQLException sqle) 
		{
			throw new RuntimeException("Error DAO in getNercList()",sqle);
		}
		return nercs;
	}

	public List<PowerOutage> getPowerOutagesOf(Nerc selectedNerc, IdentityMap idMap)
	{
		String sqlQuery = String.format("%s %s %s %s",
								"SELECT id, nerc_id, customers_affected, date_event_began, date_event_finished",
								"FROM PowerOutages",
								"WHERE nerc_id = ?",
								"ORDER BY date_event_began ASC");
		
		List<PowerOutage> powerOutages = new ArrayList<>();
		
		try
		{
			Connection connection = ConnectDB.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, selectedNerc.getId());
			ResultSet result = statement.executeQuery();
			
			while(result.next())
			{
				int powerOutageId = result.getInt("id");
				Nerc nerc = idMap.getNerc(result.getInt("nerc_id"));
				int custAffected = result.getInt("customers_affected");
				LocalDateTime dateTimeBegin = result.getTimestamp("date_event_began").toLocalDateTime();
				LocalDateTime dateTimeFinished = result.getTimestamp("date_event_finished").toLocalDateTime();
				
				PowerOutage newPowerOutage = new PowerOutage(powerOutageId, nerc, custAffected, dateTimeBegin, dateTimeFinished);
				powerOutages.add(newPowerOutage);
			}
			
			result.close();
			statement.close();
			connection.close();
		}
		catch(SQLException sqle)
		{
			throw new RuntimeException("SQL Error in 'getPowerOutagesOf()'", sqle);
		}
		
		return powerOutages;
	}	
	
	
}
