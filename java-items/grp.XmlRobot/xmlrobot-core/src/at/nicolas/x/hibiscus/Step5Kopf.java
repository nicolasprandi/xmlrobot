package at.nicolas.x.hibiscus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Set;

import at.nicolas.x.int_specs.CommonsVfs;

public class Step5Kopf {
	/*
	private IntParas iParas ;
	public Step5Kopf (IntParas iParas) {
		this.iParas = iParas;		
	}
	*/
	
	public static void run ( IntParas iParas, Mp parentMp ) throws Exception {
		TypedKopfBase tkb = new TypedKopfBase(iParas.getConn(), iParas);
		
		MvUpdateDefintions mvuds = null;
		if ( iParas.mv != null ) {
			mvuds = new MvUpdateDefintions(
						CommonsVfs.getXmldocumentByFilenameBlind(
								iParas.mv));
			iParas.logPrint("Did Read Mv-Rules: "+iParas.mv);
		} else
			mvuds = new MvUpdateDefintions();
		try {
			if ( iParas.kopfgrouped ) {
				Mp mpKopf = iParas.mm.beginMeasurePoint(parentMp,"Kopf.Grouped");
				mpKopf.addMeasure("mv_rule_size", mvuds.defList.size());
				runKopfGrouped ( tkb,iParas, mpKopf,mvuds);

				mpKopf.finalizeAndPrint();	
				
			} else {
				Mp mpKopf = iParas.mm.beginMeasurePoint(parentMp,"Kopf.Plain");
				mpKopf.addMeasure("mv_rule_size", mvuds.defList.size());
				runKopfPlain ( tkb,iParas, mpKopf,mvuds);

				mpKopf.finalizeAndPrint();					
			}
		} catch (Exception e) {			
			e.printStackTrace();
		} finally {
			try {tkb.closeStatic();} catch (Exception e1) {}
		}
	}
	static void runKopfPlain ( 
			TypedKopfBase tkb,
			IntParas iParas, 
			Mp parentMp,
			MvUpdateDefintions mvuds) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;		
		
		Hashtable<Integer, TypedKopfUpdater> defList = 
				new Hashtable<>();;
		

		int commmitSize = iParas.getCommitSize();
		int statisticCount = 0;
		Connection conn = iParas.getConn();
		
		try {
			stmt = conn.createStatement();
			rs= iParas.doRunAndExplainQuery(
					"5.Plain", 
					"kopf/plain","Get FRISCH-Objects plainly" , 
					"select obj_id,obj_typ from frisch order by obj_id;",
					stmt,
					iParas.sx_KopfPlain,
					parentMp,
					iParas.ix_sx_KopfPlain,false);
			int commitCount = 0;
			int last_obj_id = -1;
			while (rs.next()) {
				
				int this_obj_id = rs.getInt("obj_id");
				
				if ( this_obj_id != last_obj_id ) {
					
					commitCount++;
					statisticCount++;
					if ( commitCount > commmitSize) { 
						conn.commit();
						commitCount = 0;					
					}	
					
					int rows_obj_typ = rs.getInt("obj_typ");
					TypedKopfUpdater tku = null;
					if ( defList.containsKey(rows_obj_typ)) {
						tku = defList.get(rows_obj_typ);
					} else {
						tku = new TypedKopfUpdater(
								tkb,
								rows_obj_typ,
								mvuds.getMvUpdateDefinition(rows_obj_typ));
						defList.put(rows_obj_typ, tku);
					}
	
					tku.updateKopf(this_obj_id);
					
					last_obj_id = this_obj_id;
				}
				
				
			}
			
			parentMp.addMeasure("updated_kopf", statisticCount);
			parentMp.addMeasure("mv_updates", iParas.cnt_mv_rule_updates);
			parentMp.addMeasure("mv_deletes", iParas.cnt_mv_rule_deletes);
			if (iParas.mv_style == 2) {
				parentMp.addMeasure("mv_reads", iParas.cnt_mv_rule_reads);
			}
			parentMp.addMeasure("mv_inserts", iParas.cnt_mv_rule_inserts);
			
			iParas.mpMain.addMeasure("updated_kopf", statisticCount);
			iParas.mpMain.addMeasure("mv_updates", iParas.cnt_mv_rule_updates);
			
		} catch ( Exception ec ) {		
			System.out.println(ec.getLocalizedMessage());
			ec.printStackTrace();
			iParas.printExceptionBy(ec, "5.Kopf.Plainly");							  
		} finally {		         		    
		    try { if (rs != null) rs.close(); } catch (Exception e) {}	
		    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		    try { 
		    	Set<Integer> keys = defList.keySet();
		    	for(Integer key: keys){
		    		(defList.get(key)).close();
		    	}		    	
		    } catch (Exception e) {}
		}
			
	
	}		
	static void runKopfGrouped ( 
			TypedKopfBase tkb,
			IntParas iParas, 
			Mp parentMp,
			MvUpdateDefintions mvuds) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;		
		TypedKopfUpdater tku = null;
		int currentObjTyp = -1;
		int commmitSize = iParas.getCommitSize();
		int statisticCount = 0;
		Connection conn = iParas.getConn();
		
		try {
			stmt = conn.createStatement();
			rs= iParas.doRunAndExplainQuery(
					"5.Grouped", 
					"kopf/grouped","Get sorted FRISCH-Objects" , 
					"select obj_id,obj_typ from frisch  order by obj_typ;",
					stmt,
					iParas.sx_KopfGrouped,
					parentMp,
					iParas.ix_sx_KopfGrouped,false);
			int commitCount = 0;
			while (rs.next()) {
				
				commitCount++;
				statisticCount++;
				if ( commitCount > commmitSize) { 
					conn.commit();
					commitCount = 0;					
				}	
				
				int rows_obj_typ = rs.getInt("obj_typ");
				if ( currentObjTyp != rows_obj_typ ) {
					// neue gruppe
					if (tku != null ) tku.close();
					tku = new TypedKopfUpdater(
							tkb,
							rows_obj_typ,
							mvuds.getMvUpdateDefinition(rows_obj_typ));
					currentObjTyp = rows_obj_typ;
				}
				tku.updateKopf(rs.getInt("obj_id"));
				
			}
			
			parentMp.addMeasure("updated_kopf", statisticCount);
			parentMp.addMeasure("mv_updates", iParas.cnt_mv_rule_updates);
			parentMp.addMeasure("mv_deletes", iParas.cnt_mv_rule_deletes);
			if (iParas.mv_style == 2) {
				parentMp.addMeasure("mv_reads", iParas.cnt_mv_rule_reads);
			}
			parentMp.addMeasure("mv_inserts", iParas.cnt_mv_rule_inserts);
			
			iParas.mpMain.addMeasure("updated_kopf", statisticCount);
			iParas.mpMain.addMeasure("mv_updates", iParas.cnt_mv_rule_updates);
	

		} catch ( Exception ec ) {					
			iParas.printExceptionBy(ec, "5.Kopf.Grouped");							  
		} finally {		         		    
		    try { if (rs != null) rs.close(); } catch (Exception e) {}	
		    try { if (stmt != null) stmt.close(); } catch (Exception e) {}	
		    try { if (tku != null) tku.close(); } catch (Exception e) {}
		}
			
	
	}	

}
