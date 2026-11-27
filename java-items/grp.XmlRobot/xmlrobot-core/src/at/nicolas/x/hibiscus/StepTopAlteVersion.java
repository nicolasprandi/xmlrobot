package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

public class StepTopAlteVersion {
	public static void run( IntParas iParas ) throws Exception {
		/*[requo-timer]*/
		/*[requo-customer]*/
		Connection conn = iParas.getConn();
		int commmitSize = iParas.getCommitSize();

		{
			// 
			// top perform
			//
			Timestamp now = null; 
			{
				Date date = new Date();
				long time = date.getTime();
				now = new Timestamp(time);
			}			
			int commitCount = 0;
			int statisticCount = 0;
			int statisticCountTop = 0;
			Statement stmt = iParas.getConn().createStatement();
			ResultSet rs = stmt.executeQuery(
							"select "+ 
							"attr.attr_id as attr_id, "+
							"wert.wert_id as wert_id, "+
							"wert.wann as wann , "+
							"wert.von as von , "+
							"wert.bis as bis "+
							"from "+
							"obj as obj, "+
							"attr as attr, "+
							"wert as wert "+
							"where "+
							"obj.pub_ist_aktuell = 0 and "+
							"attr.obj_id = obj.obj_id and "+
							"wert.attr_id = attr.attr_id "+
							"order by "+
							"attr.attr_id, wert.wann DESC; "
					);
		
				
			PreparedStatement updateOld = conn.prepareStatement(
							" update wert set ist_top=0 where wert_id = ?;");
			PreparedStatement updateTop = conn.prepareStatement(
					" update wert set ist_top=1 where wert_id = ?;");			
					
				

			int grp_attr_id = -1;
			while (rs.next()) {
				
				boolean hs1 = false;
				if ( rs.getTimestamp("von") == null )
					hs1 = true;
				else {
					if ( rs.getTimestamp("von").compareTo(now) <= 0)
						hs1 = true; // von <= now
					else {
						updateOld.setInt(1, rs.getInt("wert_id"));			
						updateOld.executeUpdate();						
					}
				}
				
				if ( hs1 ) {
					boolean hs2 = false;;
					if ( rs.getTimestamp("bis") == null )
						hs2 = true;
					else {
						if ( rs.getTimestamp("bis").compareTo(now) > 0)
							hs2 = true; // bis > now
						else {
							updateOld.setInt(1, rs.getInt("wert_id"));			
							updateOld.executeUpdate();						
						}
					}	
					// wenn
					// rs.getTimestamp("bis") == null 
					if ( hs2 ) {
						if ( grp_attr_id != rs.getInt("attr_id")) {
							// neue gruppe
							grp_attr_id = rs.getInt("attr_id");
							// erster gruppenwert bleibt
							updateTop.setInt(1, rs.getInt("wert_id"));			
							updateTop.executeUpdate();	
							statisticCountTop++;
						} else {
							// weitere gruppenmitglieder brauchma nicht
							updateOld.setInt(1, rs.getInt("wert_id"));			
							updateOld.executeUpdate();							
						}
					}
				}
			
				commitCount++;
				statisticCount++;
				if ( commitCount > commmitSize) { 
					conn.commit();
					commitCount = 0;					
				}						
				
			}
			conn.commit();
			iParas.logPrint("Did "+ statisticCount +" - werte looked into top ");	
			iParas.logPrint("   with "+ statisticCountTop +" - werte as top ");	
			
		}			
		
		//
		// setze alle obj.pub_ist_akuell >> 0
		//
		{
			Statement stmt = iParas.getConn().createStatement();
			int res = stmt.executeUpdate(
				"update obj set pub_ist_aktuell = 1 where pub_ist_aktuell = 0;"					
					);
			conn.commit();
				
		}			
		

		
	}
}