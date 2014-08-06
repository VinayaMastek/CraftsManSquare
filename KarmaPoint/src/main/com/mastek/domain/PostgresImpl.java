package com.mastek.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PostgresImpl {
	private Connection conn = null;
	private Statement stmt = null;

	
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public void connect() {
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/yammer", "postgres",
					"root");
			conn.setAutoCommit(false);
			System.out.println("Opened database successfully");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	
	public YamParam getParameters()
	{
		if (conn == null)
			connect();

		String sql ="";
		ResultSet rs;
		YamParam parameter = new YamParam();
		sql = "SELECT latestid, oldestid, download, pages FROM parameters;";
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				parameter.setLatestid(rs.getInt("latestid"));
				parameter.setOldestid(rs.getInt("oldestid"));
				parameter.setDownload(rs.getString("download"));
				parameter.setPages(rs.getInt("pages"));
			} 
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parameter;
	}


	public void updateParam(YamParam param)
	{
		if (conn == null)
			connect();
		
		String sql ="UPDATE parameters SET "
							+ "latestid = " + param.getLatestid() +","
							+ "oldestid = " + param.getOldestid() + "," 
							+ "download = '"+ param.getDownload() + "';";
		
		System.out.println(sql);
		executeSql(sql);
		commit();
	}

	
	public void syncYammerData (String type, List<?> rows) {
		String sql = "";
		String quote = "'";
		String fullValue = "";
		String valueStrFld = quote + "%s" +quote;
		String valueIntFld = "%s" ;
		String truncate = "";
		String valueStr = "";
		YamParam param = getParameters();
		
		
		if (type.equals("users")) {
			truncate = "truncate yamusers;";
			valueStr = "("+valueIntFld+","+valueStrFld+","+valueStrFld+","+valueStrFld+","+valueStrFld+")";
			for (Object obj : rows) {
				YamUser yamusr = (YamUser) obj;
				if (!yamusr.getFullName().isEmpty())
					yamusr.setFullName(yamusr.getFullName().replaceAll("'", "''"));
				else yamusr.setFullName("Undefined");

				if (!yamusr.getEmail().isEmpty())
					yamusr.setEmail(yamusr.getEmail().replaceAll("'", "''"));
				else yamusr.setEmail("Undefined");

				if (!yamusr.getJobTitle().isEmpty())
					yamusr.setJobTitle(yamusr.getJobTitle().replaceAll("'", "''"));
				else yamusr.setJobTitle("Undefined");
					
				fullValue = fullValue + (fullValue.isEmpty() ? "" : ",")
							+ String.format(valueStr, 
										yamusr.getUserId(),
										yamusr.getFullName(), 
										yamusr.getEmail(),
										yamusr.getJobTitle(), 
										yamusr.getActivatedAt());
			}

			if (!fullValue.isEmpty()) {
				sql = "INSERT INTO yamusers (userid,fullname,email, jobtitle,activatedat)"
						+ " VALUES " + fullValue+";";
			}
		}

		if (type.equals("messages")) {
			valueStr = "("+valueIntFld+","+valueIntFld+","+valueIntFld+","+valueStrFld+","+valueIntFld+","+valueIntFld+")";
			for (Object obj : rows) {
				YamMsg yammsg = (YamMsg) obj;
				fullValue = fullValue + (fullValue.isEmpty() ? "" : ",")
							+ String.format(valueStr, 
										yammsg.getMsgId(),
										yammsg.getSenderId(),
										(yammsg.getRepliedToId()==0)?null:yammsg.getRepliedToId(),
										"Undefined",		
										//(yammsg.getMsgText().isEmpty())?"Undefined":yammsg.getMsgText(),
										yammsg.getLikedBycount(),
										yammsg.getThreadId());
			}

			if (!fullValue.isEmpty()) {
				sql = "INSERT INTO yammessages("
			            + "msgid, senderid, repliedtoid, msgtext, likedbycount, threadid)"
						+ " VALUES " + fullValue+";";
			}
			if (param.getDownload().equals("Full"))
			{
				truncate = "truncate yammessages;";
				executeSql(truncate);
			}
		}
		
		System.out.println("SQL Statement " + sql);
		executeSql(sql);
		commit();
	}

	
	private void commit()
	{
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	private void executeSql(String sql)
	{
		if (!sql.isEmpty()) {
			try {
				stmt = conn.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				System.out.println("Records created successfully");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void close() {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public MsgSummary getMsgSummary(int userId)
	{
		if (conn == null)
			connect();

		String sql ="";
		ResultSet rs;
		MsgSummary msgSummary= new MsgSummary();
		sql = "SELECT userid, postreplied, avgresponses, threads, avglikes, noreponsethreads, "+ 
			       "username, responses "+
			       "FROM msgsummary WHERE userid = " + userId + ";";
		
		System.out.println(sql);

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				msgSummary.setUserid(rs.getInt("userid"));
				msgSummary.setUsername(rs.getString("username"));
				msgSummary.setThreads(rs.getInt("threads"));
				msgSummary.setAvgresponses(rs.getInt("avgresponses"));
				msgSummary.setResponses(rs.getInt("responses"));
				msgSummary.setPostreplied(rs.getInt("postreplied"));
				msgSummary.setAvglikes(rs.getInt("avglikes"));
				msgSummary.setNoresponsethreads(rs.getInt("noreponsethreads"));
			} 
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return msgSummary;
		
	}
}