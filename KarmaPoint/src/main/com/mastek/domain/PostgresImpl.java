package com.mastek.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import net.sf.json.JSONArray;

public class PostgresImpl {
	Connection conn = null;
	Statement stmt = null;

	public void connect(){
    try {
       Class.forName("org.postgresql.Driver");
       conn = DriverManager
          .getConnection("jdbc:postgresql://localhost:5432/yammer",
          "postgres", "root");
       conn.setAutoCommit(false);
       System.out.println("Opened database successfully");
    } catch (Exception e) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        System.exit(0);
     }

    }
	

	public void insertUsers(String type, List<?> rows)
	{
    try {
    	if (type.equals("users"))
    	{
    		stmt = conn.createStatement();
    		String sql = "INSERT INTO yamusers (userid,fullname,email, jobtitle,activatedat)" + " VALUES ";
    		String valueStr = "(%s,%s,%s,%s,%s)";			
    		String fullValue="";
    		for (Object user : rows)
    		{
    			YamUser yamusr =  (YamUser) user;
    			String.format(valueStr,
    							yamusr.getUserId(), 
    							yamusr.getFullName(),
    							yamusr.getEmail(),
    							yamusr.getJobTitle(),
    							yamusr.getActivatedAt());

    			fullValue = fullValue+(fullValue.isEmpty()?"":",")+valueStr;
    		}
    		fullValue=(fullValue.isEmpty()?
    					"":
    					fullValue.replaceAll("),", ");")
    		);
    		sql = sql + fullValue + ";";
    		
    	}
    	
		
	    String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
	            + "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
	      stmt.executeUpdate(sql);

	      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
	            + "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
	      stmt.executeUpdate(sql);

	      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
	            + "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
	      stmt.executeUpdate(sql);

	      sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
	            + "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
	      stmt.executeUpdate(sql);

	      stmt.close();
	      conn.commit();
	    System.out.println("Records created successfully");

    } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }

public void close()
{
	if (conn!=null)
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
}
	
}