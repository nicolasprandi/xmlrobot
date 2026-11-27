package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TypedKopfBase {
	Connection connection= null;	
	IntParas iParas = null;	
	PreparedStatement psReadAttrTyp;
	PreparedStatement psUpdateKopfOfObject;
	
	TypedKopfBase ( 
			Connection _connection,
			IntParas _iParas) throws Exception {
		if ( connection != null)
			throw new Exception("TypedKopfUpdater.setup called although already connectin exists in static area ");
		connection = _connection;
		iParas = _iParas;
				
		// prepared statements erzeugen
		psReadAttrTyp = connection.prepareStatement(
			"select kopf_attr_typ as kopf_attr_typ "+
		    " from t_o where obj_typ = ?;");
		
		psUpdateKopfOfObject = connection.prepareStatement(
			"update obj set kopf=? where obj_id=?;" );
	}
	
	 void closeStatic() {
		try {
			if ( psReadAttrTyp != null )
				psReadAttrTyp.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if ( psUpdateKopfOfObject != null )
				psUpdateKopfOfObject.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
			
	}	


}
