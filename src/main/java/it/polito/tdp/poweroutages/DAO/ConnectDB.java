package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Utility class for connecting to the database
 * 
 * Uses the HikariCP library for managing a connection pool
 * @see <a href="https://brettwooldridge.github.io/HikariCP/">HikariCP</a>
 */
public class ConnectDB 
{
	private static final String jdbcURL = "jdbc:mariadb://127.0.0.1/poweroutages";
	private static final String username = "root";
	private static final String password = "root";
	private static HikariDataSource dataSource;
	
	static 
	{
		HikariConfig configuration = new HikariConfig();
		configuration.setJdbcUrl(jdbcURL);
		configuration.setUsername(username);
		configuration.setPassword(password);

		configuration.addDataSourceProperty("cachePrepStmts", true);
		configuration.addDataSourceProperty("prepStmtChacheSize", 250);
		configuration.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
	
		dataSource = new HikariDataSource(configuration);
	}
	
	public static Connection getConnection() 
	{		
		try 
		{
			return dataSource.getConnection();
		} 
		catch (SQLException sqle)
		{
			System.err.println("Errore di connessione ad db, con URL: " + jdbcURL);
			throw new RuntimeException(sqle);
		}
	}
}
