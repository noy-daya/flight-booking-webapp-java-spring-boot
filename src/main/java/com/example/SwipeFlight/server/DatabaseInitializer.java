package com.example.SwipeFlight.server;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptStatementFailedException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import com.example.SwipeFlight.auth.user.User;
import com.example.SwipeFlight.auth.user.UserService;

/**
 * The class is responsible for database initialization.
 * Is called from AppStartup.
 */
@Component
public class DatabaseInitializer
{
    private DataSource dataSource; // Providing database connection.
     					  		   // based on the configuration described in "application.properties" file
     				      		   // (Combination of: URL, user name, password)
    @Autowired // dependency injection
    private UserService userService;

    /**
     * Constructor
     * @param dataSource: Providing database connection.
     */
    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * The method connects to dataSource and initializes the database:
     * 		1. executes "schema.sql" file (in path: "src/main/resources")
     * 	  		in order to create the tables.
     * 		2. executes "data.sql" file (in path: "src/main/resources")
     * 	  		in order to insert initial information to the tables.
     * Handles possible exceptions.
     */
    public void initializeDatabase()
    {
    	// connect to dataSource
        try (Connection connection = dataSource.getConnection())
        {
            try {
	        	// execute "schema.sql" file
	        	ClassPathResource schemaResource = new ClassPathResource("schema.sql");
	            ScriptUtils.executeSqlScript(connection, schemaResource);
	            System.out.println("==> \"schema.sql\" script was executed successfully.");
	            
	            if (userService.getUserByUserName("admin") == null)
	            {
		            // execute "data.sql" file
		            // (only if necessary, meaning that 'admin' does not exist in database yet)
		            ClassPathResource dataResource = new ClassPathResource("data.sql");
		            ScriptUtils.executeSqlScript(connection, dataResource);
		            System.out.println("==> SQL \"data.sql\" script was executed successfully.");
		            
		            // create 'admin' user and insert into database
	                User admin = new User("admin", "swipeflight@gmail.com", "12345", true);
	                userService.insertUser(admin);
	            }
	            else
	            System.out.println("==> \"data.sql\" script has already run.");
            } catch (ScriptStatementFailedException e) {
            	System.err.println("*** Error -- Script -- script execution failed: " + e.getMessage());
            }
	        catch (ScriptException e) {
	        	System.err.println("*** Error -- Script -- script error: " + e.getMessage());
	        }
        } catch (SQLException e) {
        	System.err.println("*** Error -- SQL -- database access error: " + e.getMessage());
        }
        catch (NullPointerException e) {
        	System.err.println("*** Error -- DataSource -- datasource is null.");
        }
	    catch (Exception e) {
	    	System.err.println("*** Error -- DataSource -- datasource error: " + e.getMessage());
	    }
        
    }
}
