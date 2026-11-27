package at.nicolas.x.web;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDb {

	public TestDb() {
		// TODO Auto-generated constructor stub
	}
	
	public Connection run() {
		// TODO Auto-generated constructor stub
		
		final String url = "jdbc:mysql://localhost:3306/formixat001";
		final String user = "formixat"; // root
		final String password = "xxxx";	//	sunny94?=
	
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			p("schaut gut aus");
		} catch (Exception e) {
			System.out.println("----- problem with class.forname: com.mysql.jdbc.Driver :"+e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}		
		
		try {
			Connection conn = DriverManager.getConnection(url, user, password);
			System.out.println("----- connection ok for "+url);
			return conn;
		} catch (Exception e) {
			System.out.println("----- problem with connection for "+url+" :"+e.getLocalizedMessage());
			//System.out.println("----- problem with connection for "+url+" "+user+" "+password+" :"+e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}		
	}	
	
	private void p(String header ) {
		System.out.println(header);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestDb x = new TestDb();
		x.run();

	}

}
