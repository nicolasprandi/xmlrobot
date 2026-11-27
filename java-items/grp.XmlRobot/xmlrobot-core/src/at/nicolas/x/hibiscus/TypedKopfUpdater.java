package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.nicolas.x.int_specs.Commons;

public class TypedKopfUpdater {
	private PreparedStatement psReadKopf = null;
	
	private boolean hasMv = false;
	private int mv_style = 0;
	private PreparedStatement psMvDelete = null;
	private PreparedStatement psMvInsert = null;
	private PreparedStatement pr = null;
	private PreparedStatement pw = null;
	int pc = -1;
	
	
	TypedKopfBase tkb = null;
	int obj_type = -1;
	
	TypedKopfUpdater( 
			TypedKopfBase _tkb,
			int _obj_type,
			MvUpdateDefinition mvud ) throws Exception {
		tkb = _tkb;
		obj_type = _obj_type;
		// einlesen des attribut-types des kopfes
		tkb.psReadAttrTyp.setInt(1, _obj_type);
		ResultSet rsAt = tkb.psReadAttrTyp.executeQuery();
				
		int kopfAttrTypOfObjTyp = -1;
		int countAt = 0;
		while (rsAt.next()) {
		    ++countAt;
		    kopfAttrTypOfObjTyp = rsAt.getInt("kopf_attr_typ");		    
		}
		try {
			rsAt.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ( countAt == 1 && kopfAttrTypOfObjTyp != -1) {
			tkb.iParas.logPrint("found attr_typ '"+kopfAttrTypOfObjTyp+"' "+
					 "as kopf-acting-attribute for obj_typ '"+_obj_type+"'" );
				
				String vorbereiten = "select wert.inhalt as kopf " +
						"from attr as attr, wert as wert " +
						"where " +
						"attr.attr_typ = "+kopfAttrTypOfObjTyp+" and " +
						"attr.attr_id = wert.attr_id and " +
						"wert.ist_top = 1 and " +
						"attr.obj_id = ?;";
				tkb.iParas.logPrint("prepStmt: "+vorbereiten);
				
				psReadKopf = tkb.connection.prepareStatement( vorbereiten );				
		} else
			throw new Exception("There is something wrong with "+
			 "the vocabulary table t_o for obj_type="+_obj_type);
		
		// Mv behandeltn
		if ( mvud != null && _tkb.iParas.mv_style > 0) {
			hasMv = true;
			mv_style = _tkb.iParas.mv_style;
			String deleteText = "delete from "+mvud.getTable()+" where obj_id = ?;";
			
			tkb.iParas.logPrint("prepStmtDel: "+deleteText);
			psMvDelete = tkb.connection.prepareStatement( deleteText);
			
			if (mv_style == 1) {
				String insertText = "insert into "+mvud.getTable()+" (obj_id "+mvud.getCols()+") select obj_id "+mvud.getCols()+" from "+mvud.getView()+" where obj_id = ?;";
				tkb.iParas.logPrint("prepStmtInsert: "+insertText);
				psMvInsert = tkb.connection.prepareStatement( insertText );
				
			} else //if (mv_style == 2) 
				{
				// nuevo: reader
				String tr = "select obj_id "+mvud.getCols()+" from "+mvud.getView()+" where obj_id = ?;";
				tkb.iParas.logPrint("prepStmtRead: "+tr);
				pr = tkb.connection.prepareStatement( tr );
				
				// anzahl
				pc = mvud.getCols().length() - mvud.getCols().replace(",", "").length();
				
				// writer
				String tw = "insert into "+mvud.getTable()+" (obj_id "+
						mvud.getCols()+") values (" +
						Commons.StringTimes("?,", pc) +
						"?);";
				tkb.iParas.logPrint("prepStmtWrite: "+tw);
				pw = tkb.connection.prepareStatement( tw ); 			
				
				pc++;
				tkb.iParas.logPrint("pc: "+pc);				
			} 

			
			tkb.iParas.logPrint("prepStmt finished." );
			//System.out.println("deleteText = "+deleteText);
			//System.out.println("insertText = "+insertText);

		}

		
				
	}
	
	void updateKopf ( int obj_id ) throws SQLException {

		psReadKopf.setInt(1, obj_id);
		ResultSet rsAt= psReadKopf.executeQuery();
				
		String kopf = "_?_";
		while (rsAt.next()) {
		    kopf = rsAt.getString("kopf");		    
		}		
		
		try {
			rsAt.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tkb.psUpdateKopfOfObject.setString(1, kopf);
		tkb.psUpdateKopfOfObject.setInt(2, obj_id);
		tkb.psUpdateKopfOfObject.executeUpdate();
		//System.out.println("Did Kopf, obj_id="+obj_id);
		
		if ( hasMv ) {
			tkb.iParas.cnt_mv_rule_updates++;
			
			psMvDelete.setInt(1, obj_id);
			int delres= psMvDelete.executeUpdate();			
			tkb.iParas.cnt_mv_rule_deletes=tkb.iParas.cnt_mv_rule_deletes
					+ delres;
			
			if (mv_style == 1) {
				psMvInsert.setInt(1, obj_id);
				int insres= psMvInsert.executeUpdate();
				tkb.iParas.cnt_mv_rule_inserts=
						tkb.iParas.cnt_mv_rule_inserts
						+ insres;				
			} else  {
				pr.setInt(1, obj_id);
				ResultSet rs = pr.executeQuery();	
				
				try {
					if (rs.next()) {
						tkb.iParas.cnt_mv_rule_reads++;
						for (int i = 1; i <= pc; i++) {
							pw.setObject(i, rs.getObject(i));
						}
						int insres = pw.executeUpdate();
						tkb.iParas.cnt_mv_rule_inserts = tkb.iParas.cnt_mv_rule_inserts + insres;
					} 
				} finally {
					if (rs!=null) rs.close();
				}
				
			} 
			
						
			/*
			tkb.iParas.logPrintDebug("Did MV-Update, obj_typ="+obj_type+", obj_id="+obj_id
					+", delres="+delres
					+", insres="+insres); 
					*/
		
		}
				
	}
	
	void close () {
		try {
			psReadKopf.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			if ( psMvDelete != null )
				psMvDelete.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		try {
			if ( psMvInsert != null )
				psMvInsert.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		try {
			if ( pr != null )
				pr.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
		
		
		try {
			if ( pw != null )
				pw.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

}
