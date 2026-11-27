package at.nicolas.x.hibiscus;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_coop.CoopDbci;
import at.nicolas.x.int_interpreter.StoppableThread;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;


public class ItegrationMain  {
	
	
	
	private boolean isPostgres;
	
	boolean doRunstats = false;

	IntParas iParas = null;
	TaskLogger taskLogger = null;

	Timestamp beginning = null;
	boolean checkOnly = false;
	
	public synchronized void shouldStopThread() {
		System.out.println("IntegrationMain received request to threadStop");
		iParas.shouldStopThread();
	}	
	
	private void doIntegrationCore() throws Exception {
		
	    
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:sss");
		
		MeasureSystem mm = iParas.mm;
		
		Mp mpInt = mm.beginMeasurePoint("integration",0);
		iParas.mpMain = mpInt;
		
		
		// Index-Szenarios
		boolean isIsValid = true;
		if ( iParas.index != null && iParas.getDropIndex() && iParas.hasPronto1() ) {
			// Ein indes-szenario ist da, es soll grundsaetzlich gedroppt werden, pronto au da
			Mp mpIndexSzenario = mm.beginMeasurePoint(mpInt,"index-szenario-initial");
			taskLogger.print("Starting to analyze Index-Scenario ... ");
			isIsValid = iParas.setupIs();
			int isValid2 ;
			if ( isIsValid ) isValid2 = 1;
			else isValid2 = 0;
			mpIndexSzenario.addMeasure("index_valid", isValid2);
			taskLogger.print("Finished to analyze Index-Scenario.");
			mpIndexSzenario.finalizeAndPrint();
		}
		
		if ( iParas.kick != null  ) {
			// Ein indes-szenario ist da, es soll grundsaetzlich gedroppt werden, pronto au da
			Mp mpSelectSzenario = mm.beginMeasurePoint(mpInt,"select-szeario-initial");
			
			try {
				int count = iParas.setupSs();
				mpSelectSzenario.addMeasure("nbr_of_selects", count);
				taskLogger.print("Select Setup successfull, number of replacements: "+count);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				taskLogger.logExceptionBy(e, "Selection-Point-Setup");
				mpSelectSzenario.addMeasure("problem", 1);
				iParas.smps = new String[iParas.selectnames.length];
			}
			
			mpSelectSzenario.finalizeAndPrint();
		}		
		
		
		if ( checkOnly ) {
			taskLogger.print("'checkonly' was requested, intgration therefore finished.");
		} else if (!isIsValid) {
			taskLogger.print("UNVALID Index-Szenario, intgration therefore finished.");

		} 
		else {
			Mp mpBooking = mm.beginMeasurePoint("booking-integration",1,mpInt);
			
		    //boolean fromException = false;
		    //Exception fromExc = null;
		    Exception exceptionAtEnd = null;
			try {
				
				// schauen ob integration besetzt
				// Checking if integration is running
			    Statement stmtCheckRunningIntegration = null;
			    ResultSet rsRunning = null;				
				try {				
					
				    stmtCheckRunningIntegration = iParas.getConn().createStatement();
				    rsRunning = stmtCheckRunningIntegration.executeQuery(
							"select beginningWas from integration_running;"										
							);
				    boolean kaputt = false;    
				    String startzeit = null;
				    while ( rsRunning.next() ) {
				    	kaputt = true;
				    	startzeit = rsRunning.getString("beginningWas");
				    }

				    rsRunning.close();     
				    stmtCheckRunningIntegration.close();
				    
				    if ( kaputt )
				    	throw new Exception("Integration is alread running, Start of running was: "+startzeit);				
					
				} catch (Exception e2) {
					iParas.logPrint("*** Exception e2 *** integration could not be booked *** "+e2.getMessage());
					// taskLogger.printException(e2);
					throw e2;

				} finally {
					if ( rsRunning != null ) rsRunning.close();
					if ( stmtCheckRunningIntegration != null ) rsRunning.close();						
				}
				
				// bei maria und mysql padding
				if ( !isPostgres ) {
					Statement stmtPadding = iParas.getConn().createStatement();
					try {
						int sqlmoderesult = stmtPadding.executeUpdate(
								"SET sql_mode = 'PAD_CHAR_TO_FULL_LENGTH';"					
									);
							// System.out.println("did: SET sql_mode = 'PAD_CHAR_TO_FULL_LENGTH'");
							taskLogger.print("did: SET sql_mode = 'PAD_CHAR_TO_FULL_LENGTH' ("+sqlmoderesult+")");
							iParas.getConn().commit();
							stmtPadding.close();						
					} catch ( Exception e3) {
						iParas.logPrint("*** Exception e3 *** padding mysql/maria *** "+e3.getMessage());
						// taskLogger.printException(e3);						
						throw e3;
					} finally {
						if ( stmtPadding != null ) stmtPadding.close();
					}

				}						
				
				// integration buchen
				Statement integrationRunningBelegen = iParas.getConn().createStatement();
				try {
				    int zurueck1 = integrationRunningBelegen.executeUpdate(
				    		" insert into integration_running (beginningWas) values ('"+formatter.format(new Date())+"');");
				    iParas.getConn().commit();		
				    integrationRunningBelegen.close();
				    taskLogger.print("integration_running was booked ("+zurueck1+")");
				} catch ( Exception e4 ) {
					iParas.logPrint("*** Exception e4 *** booking integration *** "+e4.getMessage());
					// taskLogger.printException(e4);					
					throw ( e4);
				} finally {
					if ( integrationRunningBelegen != null ) integrationRunningBelegen.close();
				}

				mpBooking.finalizeAndPrint();
				
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_Start,mpInt)) 
					// DropIndexStart1()
					;
				
				Mp mpCopy = mm.beginMeasurePoint(mpInt,"COPY-DATA");
				//taskLogger.print(" ");
				taskLogger.print("Started to copy from import to central ...");
				//taskLogger.print(" ");
				//taskLogger.printStepIntro("Copy Data");  	 			
				Step1Pub.run(iParas,isPostgres,mpCopy);
				//taskLogger.printStepExtro("Copy Data");
				taskLogger.print("Finished to copy from import to central.");
				mpCopy.finalizeAndPrint();				
				// taskLogger.print(" ");
				
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_SearchMigrations,mpInt)) 
					//iParas.BuildIndexFindMigrations2()
					;
				
				boolean stillWorkToDo = true;
				int washers = 0;
				
				int intensiveIntegrationRounds = iParas.getIntensiveIntegrationRounds();
				Mp mpMigration = mm.beginMeasurePoint(mpInt,"MIG-CYCLES");
				while ( stillWorkToDo ) {
					washers++;
					
					Mp mpRound = mm.beginMeasurePoint(mpMigration, washers+".CYCLE");
					
					boolean doPronto2Really = false;
					if ( iParas.hasPronto2() && washers <= intensiveIntegrationRounds) 
						doPronto2Really = true;
					mpRound.addMeasure("p2_really", booleanToInt(doPronto2Really));
					
					boolean doPronto2FinalClose = false;
					if ( iParas.hasPronto2() && washers == intensiveIntegrationRounds) 
						doPronto2FinalClose = true;	
					mpRound.addMeasure("p2_final", booleanToInt(doPronto2FinalClose));
					
					/*boolean isFirst = false;
					if ( washers == 1 )
						isFirst = true; */
					
					
					//taskLogger.printTime();
					iParas.logPrint(" ");
					iParas.logPrint("============= ");
					iParas.logPrint("Round #"+washers);
					iParas.logPrint("============= ");
					// xxx roundStart
					// xxx roundStartFirst
					// xxx roundStartIntensive - wenn doPronto2Really
					// xxx roundStartRetiring    wenn doPronto2FinalClose
					
					Mp mpMig1 = mm.beginMeasurePoint("Search-Migrations",mpRound);
					//taskLogger.printStepIntro("Search Migration-Requests #"+washers);
					
					int migCounter = Step2Mig1Search.run(iParas,mpMig1,washers);
					mpMig1.addMeasure("found mig_requests", migCounter);
					//taskLogger.printStepExtro("Search Migration-Requests #"+washers);
					mpMig1.finalizeAndPrint();
					
					// xxx roundDidSearch
					// xxx roundDidSearchFirst
					// xxx roundDidSearchIntensive
					// xxx roundDidSearchRetiring
					
					if ( washers == 1 ) {
						// After first migration
						if (iParas.shouldRunClassicIndexpoint(IntParas.ix_AfterFirstMigrationSearch,mpRound)) 
							//iParas.BuildIndexAfterFirstMigrationSearch3()
							;
						// xxx roundAfterFirstMigrationSearchFirst
						// xxx roundAfterFirstMigrationSearchIntensive
						// xxx roundAfterFirstMigrationSearchRetiring
						
					} else {
						// xxx roundAfterNotFirstMigrationSearch
						// xxx roundAfterNotFirstMigrationSearchFirst
						// xxx roundAfterNotFirstMigrationSearchIntensive
						// xxx roundAfterNotFirstMigrationSearchRetiring
					}
					
					// xxx roundDidSearch2
					// xxx roundDidSearch2First
					// xxx roundDidSearch2Intensive
					// xxx roundDidSearch2Retiring
					
					if ( migCounter > 0) {
											
						if (iParas.shouldRunClassicIndexpoint(IntParas.ix_roundBusyStart,mpRound));
						// xxx roundBusyStartFirst
						// xxx roundBusyStartIntensive
						// xxx roundBusyStartRetiring
						// zweiten teil der migration ausführen boolean isPostgres = iParas.isPostgres;
						
						Mp mpMig2 = mm.beginMeasurePoint("Optimize",mpRound);
						//taskLogger.printStepIntro("Optimize Migration-Requests #"+washers);
						
						//if ( iParas.newopt)
							Step2Mig2OptimizeNew.run(iParas,mpMig2,washers);
						//else
						//	Step2Mig2Optimize.run(iParas,mpMig2);
						
						//taskLogger.printStepExtro("Optimize Migration-Requests #"+washers);	
						
						mpMig2.finalizeAndPrint();
						
						Mp mpFrischMigratration = mm.beginMeasurePoint("Frisch-Handling",mpRound);
						
						Step2Mig3Frisch.run(iParas, mpFrischMigratration);
						mpFrischMigratration.finalizeAndPrint();
						
						// xxx roundBusyDidOptimize
						// xxx roundBusyDidOptimizeFirst
						// xxx roundBusyDidOptimizeIntensive
						// xxx roundBusyDidOptimizeRetiring
						Mp mpMig3 = mm.beginMeasurePoint(mpRound,"Perform Migration");
						//taskLogger.printStepIntro("Execute Migration-Requests #"+washers);
						Step2Mig4Migrate.run(iParas,doPronto2Really&&iParas.upp_mig,doPronto2FinalClose,mpMig3,washers);
						//taskLogger.printStepExtro("Execute Migration-Requests #"+washers);
						mpMig3.finalizeAndPrint();
						// xxx roundBusyDidMigrate
						// xxx roundBusyDidMigrateIntensive
						// xxx roundBusyDidMigrateRetiring
						Mp mpMig4 = mm.beginMeasurePoint(mpRound,"Note Migrations");
						//taskLogger.printStepIntro("Note Migration-Requests #"+washers);
						Step2Mig5Archify.run(iParas,mpMig4);
						//taskLogger.printStepExtro("Note Migration-Requests #"+washers);
						mpMig4.finalizeAndPrint();

						if (iParas.shouldRunClassicIndexpoint(IntParas.ix_roundBusyEnd,mpRound));
						// xxx roundBusyEndFirst
						// xxx roundBusyEndIntensive
						// xxx roundBusyEndRetiring

					} else {
						// raus aus der schleife
						stillWorkToDo = false;
						// xxx roundUnbusy
						// xxx roundUnbusyIntensive
						// xxx roundUnbusyRetiring
					}
					// xxx roundEnd
					// xxx roundEndIntensive
					// xxx roundEndRetiring
					mpRound.finalizeAndPrint();
				}
				
				
				iParas.closeAllPreparedStatements();
				mpMigration.addMeasure("done_rounds", washers);
				mpMigration.finalizeAndPrint();
				//iParas.logPrint(" ");
				iParas.logPrint("==================================================================================== ");
				iParas.logPrint("=         FINAL ACTIONS                                                           == ");
				iParas.logPrint("==================================================================================== ");
				iParas.logPrint(" ");
				
				StepALimit.run(iParas, mpInt);
				
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_Heal,mpInt)) 
					//iParas.BuildIndexHeal4()
					;
				
				Mp mpHeal = mm.beginMeasurePoint(mpInt,"HEAL");
				//taskLogger.printStepIntro("Healing");
				Step3Heal.run(iParas,mpHeal);
				//taskLogger.printStepExtro("Healing");
				mpHeal.finalizeAndPrint();
				
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_Top,mpInt)) 
					//iParas.BuildIndexTop5()
					;
				
				Mp mpTop = mm.beginMeasurePoint(mpInt,"TOP");
				//taskLogger.printStepIntro("Top-Calculations");
				Step4Top.run(iParas,mpTop);
				//taskLogger.printStepExtro("Top-Calulations");
				mpTop.finalizeAndPrint();
				
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_Kopf,mpInt));
				
				try {
					Step5Kopf.run(iParas,mpInt);
				} catch (Exception e) {
					iParas.printExceptionBy(e, "RunningKopf");
					// TODO Auto-generated catch block
					//e.printStackTrace();
					iParas.newMessageWillCome();
					iParas.addFinalMessage("Exception Running 'Kopf' :"
							+ e.getMessage());
				}
				/*
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_Mv,mpInt));
				
				if ( iParas.mvrefresh) {
					Mp mpMv = mm.beginMeasurePoint(mpInt,"MvRefresh");
					iParas.rh.runRhJob("mv-refresh-all-ok", null);
					mpMv.finalizeAndPrint();					
				}
				*/
				
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_Statistics,mpInt));
				
				Mp mpStat = mm.beginMeasurePoint(mpInt,"ObjectTypeStat");
				try {
					Step7Statistics.run(iParas,mpStat);
				} catch (Exception e) {
					iParas.printExceptionBy(e, "Running ObjectTypeStat");
					iParas.newMessageWillCome();
					iParas.addFinalMessage("Exception Running 'ObjectTypeStat' :"
							+ e.getMessage());
				}
				mpStat.finalizeAndPrint();
				
				//if (iParas.shouldRunClassicIndexpoint(IntParas.ix_CustMv,mpInt));
				
				//if (iParas.shouldRunClassicIndexpoint(IntParas.ix_CustProcess,mpInt));
				

				
				if (iParas.shouldRunClassicIndexpoint(IntParas.ix_End,mpInt)) 
					//iParas.BuildIndexEnd8()
					;
				
				// iParas.logPrint(" ");
				iParas.logPrint("Finished with success. Loadcounter = "+iParas.getLoadCount());
				iParas.logPrint(" ");
				
			    taskLogger.print("[ "+beginning.toString()+" ] - Begin");
			    Timestamp ending =  new Timestamp( System.currentTimeMillis() );
			    taskLogger.print("[ "+ending.toString()+" ] - End");
			    taskLogger.print(" ");
			    
			    


				
			} catch (Exception e1) {
				exceptionAtEnd = e1;
				iParas.logPrint("*** Exception by Hibiscus-Integration: "+e1.getMessage());
				//taskLogger.printException(e1);
				//fromException = true;
				//fromExc = e1;
				// if ( iParas.getConn()!=null ) iParas.getConn().close();
				throw ( e1);
			} finally {
				/*if ( fromException ) {
					iParas.logPrint("integration_running was NOT released due to exception.");
					if ( fromExc != null )
						iParas.logPrint("  Exception:"+fromExc.getMessage());
				} else */
				
			 	
				try {
					mpInt.finalizeAndPrint();
					taskLogger.print(" ");
					mm.printMeasurePoints(true);
					taskLogger.printBlank(" ");
					taskLogger.printBlank("[ "+beginning.toString()+" ] - Begin");
					Timestamp ending =  new Timestamp( System.currentTimeMillis() );
					taskLogger.printBlank("[ "+ending.toString()+" ] - End");
					taskLogger.printBlank(" ");
					mpInt.print(true);
					taskLogger.printBlank(" ");
					taskLogger.printBlank(" STARTING RACETIME REPORT ....");
					taskLogger.printBlank(" ");
					mm.printMeasurePoints(false);
					
					taskLogger.printBlank(" ");
					mpInt.print(true);			
					
					if ( exceptionAtEnd != null ){
						taskLogger.printBlank("");
						taskLogger.printBlank("| ****************");
						taskLogger.printBlank("| ** Exception  ** > "+exceptionAtEnd.getLocalizedMessage());
						taskLogger.printBlank("| **************** ");	
						taskLogger.printBlank("");
					}
					iParas.printFinalMessage(taskLogger);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					iParas.printExceptionBy(e, "finalizing the measure-printer");
				}


					
				try {
				    Statement integrationStopper = iParas.getConn().createStatement();
				    
				    int zurueck = integrationStopper.executeUpdate(
				    		" delete from integration_running;");
				    iParas.logPrint("integration_running was released ("+zurueck+")");
				    iParas.getConn().commit();	
				} catch ( Exception e5 ) {
					iParas.logPrint("Exception e5 *** could not release integration_running *** "+e5.getMessage());
					taskLogger.printException(e5);
					throw ( e5);
				} finally {
					
					if ( iParas.getConn() != null ) {
						if ( iParas.getDbci() != null ) {
							try {
								iParas.getDbci().closeConnection(iParas.getConn());	
								iParas.logPrint("Did close Db-Connection/Dbci");
							} catch (Exception e) {
								iParas.logPrint("Problem when closing Db-Connection/Dbci: "+e.getMessage());
								System.out.println("Problem when closing Db-Connection/Dbci: "+e.getMessage());
								e.printStackTrace();
							}
						} else {
							try {
								iParas.getConn().close();
								iParas.logPrint("Did close Db-Connection/Url");
							} catch (Exception e) {
								iParas.logPrint("Problem when closing Db-Connection/Url: "+e.getMessage());
								System.out.println("Problem when closing Db-Connection/Url: "+e.getMessage());
								e.printStackTrace();
							}
						}
						
						
					}
					
					iParas.logPrint(" ");
					iParas.logPrint("Done.");
					try {
						iParas.closeExplainer();
					} catch (Exception e) {
						iParas.logPrint("Problem when closing Explainer: "+e.getMessage());						
					}
					taskLogger.close();							
				}
			}	 			
		}	   	
	}	
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		// TODO Auto-generated method stub
		/*[requo-timer]*/
		/*[requo-customer]*/	
		
		String tname = "integrate";
		
		// beginning
		beginning =  new Timestamp( System.currentTimeMillis() );
		
		if ( e.getAttribute("how") == null )
			throw new Exception("needing @how for integration");
		
		String how = e.getAttributeValue("how");
		String db = null;
		CoopDbci dbci = null;
		Connection conn = null;
		String dbConnectMessage=null;
		if ( how.equals("dbci")) {
			
			String dbci_id = e.getAttributeValue("dbci");
			String dbci_app = null;
			if ( dbci_id == null ) throw new LangException("integration with conn==dbci needs attribute @dbci");
			if ( e.getAttribute("dbci_app")!=null &&
					e.getAttributeValue("dbci_app").trim().length() > 0 ) {
				dbci_app = e.getAttributeValue("dbci_app");
			}
			
			if ( dbci_app == null)				
				dbci= (CoopDbci)rh.getNo(dbci_id, Wellknown.coop_type_dbci);
			else
				dbci= (CoopDbci)rh.getNoApp(dbci_id, Wellknown.coop_type_dbci,dbci_app);
			
			if ( dbci == null )
				throw new LangException("integration: A dbci with the id '"+dbci_id+"' does not exist. dbci_app="+dbci_app);
			db = dbci.getDb();
			conn = dbci.getConnection(rh);
			
			dbConnectMessage ="Connect by Dbci";
			
		} else if ( how.equals("url")) {
			dbConnectMessage ="Connect by Url";			
			String url = e.getAttributeValue("url");
			if ( url == null ) throw new LangException("task ["+tname+"] needs attribute @url");
			
			// datenbank-typ festlegen
			db = e.getAttributeValue("db");

			if ( db == null )
				throw new LangException("task ["+tname+"] needs attribute @db");
			
			Commons.registerJdbcDriverByDbtypeUrl(db, url); // dbms		
			String user = e.getAttributeValue("user");
			if ( user == null ) throw new LangException("task ["+tname+"] needs attribute @user");
			
			String password = e.getAttributeValue("password");
			if ( password == null ) throw new LangException("task ["+tname+"] needs attribute @password");
			
			// Connecting to db
			
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);			
		} else {
			throw new Exception("needing @how to be dbci or url but it is: "+how);
		}
		
		if ( db.equals("maria")) {
			isPostgres = false;
			//isMaria = true;
		} else if ( db.equals("postgres")) {
			isPostgres = true;
		} else if ( db.equals("mysql")) {
			isPostgres = false;
		} else
			throw new LangException("task ["+tname+"] does not know db ["+db+"]");
		

		
		try {

			// TaskLoggger aufsetzen
			String logFile = null;
			String logFlags = null;
			
			if ( e.getAttribute("log_file") != null )
				logFile = e.getAttributeValue("log_file");
			if ( e.getAttribute("log_flag") != null )
				logFlags = e.getAttributeValue("log_flag");
			else
				logFlags = "";			
			
			taskLogger = new TaskLogger(logFile,
					logFlags,rh.getSitzung());	
			
			// iParas aufsetzen			

			int xmypronto = 0;
			if ( e.getAttribute("pronto") !=null ) {
				xmypronto = Commons.string2int(e.getAttributeValue("pronto"));
			}
			boolean mydropIndex = false;
			if ( xmypronto > 0 && e.getAttribute("drop_index")!=null) {
				mydropIndex = Commons.stringToBool(e.getAttributeValue("drop_index"));
			}
			int mycommitsize = 1000;
			if ( e.getAttribute("commit")!=null && e.getAttributeValue("commit").trim().length() > 0 )
				mycommitsize = Commons.string2int(e.getAttributeValue("commit"));
			
			int myreportsize = 20000;
			if ( e.getAttribute("report")!=null && e.getAttributeValue("report").trim().length() > 0 )
				myreportsize = Commons.string2int(e.getAttributeValue("report"));
			
			doRunstats = Commons.stringxr2bool(e.getAttributeValue("runstats"), false);
			// System.out.println("davor");
			boolean background = false;
			if ( e.getAttribute("background") !=null ) {
				background = Commons.stringToBool(e.getAttributeValue("background"));
			}		   
			boolean breakerIgnored = false;
			String breakValue = e.getAttributeValue("break");
			if ( background == false && breakValue != null && breakValue.length() > 0) {
				breakValue = null;
				breakerIgnored = false;
			}
						
			iParas = new IntParas(
					taskLogger,
					xmypronto,
					mydropIndex,
					isPostgres,
					db,
					e.getAttributeValue("explain"),
					rh,
					doRunstats,
					conn,
					mycommitsize,
					myreportsize,
					e.getAttributeValue("index"),
					breakValue,
					e.getAttributeValue("cust_flag"),
					e,
					e.getAttributeValue("kick"),
					e.getAttributeValue("mv"),
					dbci);
			
			taskLogger.setDoPrintIs(iParas.isdetails);


			this.checkOnly = Commons.hasFlag(e, "checkonly");

			
			taskLogger.print("Started Integration at "+beginning.toString());
			
			
			iParas.printWelcomeInfos();
			if ( breakerIgnored && mydropIndex)
				taskLogger.print("breakpoints will be ignored due to foreground-process");
			
			taskLogger.print("reportSize="+iParas.getReportSize());
			taskLogger.print("dss="+iParas.dss);
			taskLogger.print("solo="+iParas.solo);
			taskLogger.print("numform="+iParas.numform);
			taskLogger.print("simple="+iParas.simple);
			taskLogger.print("rapid="+iParas.smart);			
			taskLogger.print("doubleoccupations="+iParas.doubleoccupation);
			taskLogger.print("adapt="+iParas.getAdapt());
			
			taskLogger.print("isdetails="+iParas.isdetails);	
			//taskLogger.print("algo.newopt="+iParas.newopt);	
			// taskLogger.print("algo.newtop="+iParas.newtop);	
			
			
			if (iParas.index!= null && iParas.index.length() > 0 ) {
				taskLogger.print("checkonly="+checkOnly);
				taskLogger.print("Index-Szenario-File="+iParas.index);
				if (iParas.breaker != null && iParas.breaker.length() > 0 )
					taskLogger.print("  BreakPoint-File="+iParas.breaker);
				else
					taskLogger.print("  Kein Breakpointfile definiert");
				if (iParas.cust_flag!=null && iParas.cust_flag.length() > 0 )
					taskLogger.print("  Customer-Flags="+iParas.cust_flag);
				else
					taskLogger.print("  Kein Customer-Flags definiert");				
			} else {
				taskLogger.print("Kein Index-Szenario in Verwendung.");
			}
			
			if (iParas.kick != null) {
				taskLogger.print("Selection-Updates="+iParas.kick);
			}else {
				taskLogger.print("Keine Selection-Updates in Verwendung");
			}
 	    
			if (iParas.mv != null && 
				iParas.mv_style > 0 && 
				iParas.mv_style<3) {
				if ( iParas.mv_remember_print != null)
					taskLogger.print(iParas.mv_remember_print);
				taskLogger.print("mv="+iParas.mv);
				taskLogger.print("mv_style="+iParas.mv_style);
			}else {
				if (iParas.mv != null )
					taskLogger.print("parameter mv_style ist falsch "+iParas.mv_style);
				else
					taskLogger.print("Keine mv-defintionen gefunden");
				iParas.mv_style = 0;
			}		
			taskLogger.print(dbConnectMessage);
			taskLogger.print("autocommit="+conn.getAutoCommit());
			
			
			if ( background ) {
				taskLogger.print("Ausfuehrung im Background");
				IntegrationThread it = new IntegrationThread();
			
				
				it.setA(this,
						iParas.rh.getSitzung().registerProcess(
								it, 
								"Integration",
								"dropIndex="+iParas.getDropIndex() ));
				it.start();
			} else {
				taskLogger.print("Ausfuehrung im Foreground");
				doIntegrationCore();
				
			}

		} catch (Exception e1) {
			
			if ( taskLogger != null ) {
				taskLogger.printException(e1);				
			} else
				System.out.println("Excpetionion: "+e1.getMessage());
			try {
				if ( conn != null ) {
					if ( how.equals("dbci"))
						dbci.closeConnection(conn);
					else
						conn.close();
					// taskLogger.print("** DB-CLOSE ** DONE ** 2");
					
				}
			} catch (Exception e2) {
				System.out.println("Problem during closing db-connection while having an exception");
				e2.printStackTrace();
			}
			
			try {
				if ( taskLogger != null ) taskLogger.close();
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				System.out.println("Problem during closing tasklogger while having an exception");			
				e3.printStackTrace();
			}			
			throw ( e1);
		}

		
		return null;
	}
	class IntegrationThread extends StoppableThread {
		ItegrationMain integrationMain;
		int pid;
		void setA( ItegrationMain _integrationMain, int _pid) {
			integrationMain = _integrationMain;
			pid = _pid;
		}
		public void run () {
			
			
			try {
				integrationMain.innerTheadProgrammCatched();
			} finally {
				try {
					integrationMain = null;
					iParas.rh.getSitzung().finishedProcess(pid);
				} catch (Exception e) {
					System.out.println("Could not kill process after successfull exection, "+e.getMessage());
					e.printStackTrace();
				}
			}			
		}
		@Override
		public void stopExecution() {
			// TODO Auto-generated method stub
			integrationMain.shouldStopThread();
			
		}
		@Override
		public synchronized String getStateStoppable() {
			// TODO Auto-generated method stub
			return integrationMain.iParas.getState();
		}
	}
	private void innerTheadProgrammCatched()  { 
		try {
			doIntegrationCore();
		} catch (Exception e) {
			taskLogger.print("Integration terminated in problemtic behaviour: "+e.getMessage());
		}
	}
	
	private int booleanToInt(boolean in ) {
		if (in) return 1;
		else return 0;
	}
	






}
