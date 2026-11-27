package at.nicolas.x.int_coop;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.ILogConnector;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Parameters;
import at.nicolas.x.tasks.Unique;


public class CoopDbc  implements ITask, ICoop {
	
	private static final int act_open   = 0;
	private static final int act_get_autocommit   = 1;
	private static final int act_ps_add   = 2;
	private static final int act_ps_remove   = 3;
	private static final int act_ps_set_value   = 4;
	private static final int act_select   = 5;
	private static final int act_update   = 6;
	private static final int act_test   = 7;
	private static final int act_set   = 8;
	private static final int act_commit   = 9;
	private static final int act_rollback   = 10;
	private static final int act_close   = 11;

	private static KeyWordMapper tasknames;
	
	private static final int pst_int   = 0;
	private static final int pst_int_null   = 1;
	private static final int pst_null   = 2;
	private static final int pst_timestamp   = 3;
	private static final int pst_timestamp_null   = 4;
	private static final int pst_string   = 5;
	private static final int pst_string_null   = 6;

	private static KeyWordMapper pstypes;	
	
	private static final int how_dbci   = 0;
	private static final int how_url   = 1;	
	private static KeyWordMapper hows;
	//Element e = null;
	
	String tname ="dbc";
	
	Connection conn = null;
	String id = null;
	private HashMap<String, PreparedStatement> psMap = null;
		
	IRequestHandler rh;	
	ILogConnector _lc=null;
	int how_int = -1;
	String dbci_id = null;
	String dbci_app = null;
	
	public PreparedStatement getPs(String psId ) {
		return ( psMap.get(psId) );
	}
	private void setPrepParas (PreparedStatement ps, Element e1 ) throws Exception {
		
		if ( e1.getAttribute("type") != null  &&
				e1.getAttribute("pos") != null  &&
				e1.getAttribute("value") != null ) {
			//String typ = e1.getAttributeValue("type");
			int pc = pstypes.convert(e1.getAttributeValue("type"));
			
			int pos = Commons.string2int(e1.getAttributeValue("pos"));
			
			switch ( pc ) {
			case pst_int: {	
				//System.out.println("int-value="+e1.getAttributeValue("value"));
				ps.setInt(pos, new Integer( Commons.string2int(e1.getAttributeValue("value"))));
				break;
			}
			case pst_int_null: {	
				ps.setInt(pos, java.sql.Types.INTEGER);
				break;
			}
			case pst_null: {	
				ps.setNull(pos,java.sql.Types.NULL);
				break;
			}			
			case pst_timestamp: {	
				ps.setTimestamp(pos,Timestamp.valueOf(e1.getAttributeValue("value")));
				break;
			}
			case pst_timestamp_null: {	
				ps.setNull(pos,java.sql.Types.TIMESTAMP);
				break;
			}
			case pst_string: {	
				ps.setString(pos,e1.getAttributeValue("value"));
				break;
			}
			case pst_string_null: {	
				ps.setNull(pos,java.sql.Types.CHAR);
				break;
			}			
			}
			
		}
	}
	
	@Override
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		
		psMap = new HashMap<String, PreparedStatement>();
		this.rh = rh;
		this._lc = rh.getLogConnector();
		/*
		if ( e.getAttribute("act") == null ) {
			throw new Exception(tname+" needs attribute @act (open,println,print,close)");
		}*/
		
		String exeid = null;
		if ( e.getAttribute("id") == null ) 
			throw new Exception(tname+" needs attribute @id");
		else	
			exeid = e.getAttributeValue("id");
		
		//String action = e.getAttributeValue("act");
		int ac = tasknames.convert(e.getAttributeValue("act"));
		
		Parameters retParas = null;
		switch ( ac ) {
		case act_open: {	
			System.out.println("open-dbc: "+Commons.toText(e));
			id = exeid;
			String how = e.getAttributeValue("how");
			
			if ( how == null ) 
				how_int = how_url;
			else {
				how_int = hows.convert(how);
			}
			
			if ( how_int == how_url ) {
				// im alten url-style
				String url = e.getAttributeValue("url");
				if ( url == null ) throw new LangException("task ["+tname+"] needs attribute @url");
				
				String db = e.getAttributeValue("db");
				if ( db == null )
					throw new LangException("task ["+tname+"] needs attribute @db");		
							
				try {
					Commons.registerJdbcDriverByDbtypeUrl(db, url);

				} catch (Exception e2) {
					// TODO Auto-generated catch block
					System.out.println("dbc-open failed with "+db);
					rh.printTrace(e2.getLocalizedMessage());
					e2.printStackTrace();
					throw new Exception("dbc-open failed with db ["+db+"] with requested url ["+url+"], Exception: "+e2.getMessage());
				}
				
				String user = e.getAttributeValue("user");
				if ( user == null ) throw new LangException("task ["+tname+"] needs attribute @user");
				
				String password = e.getAttributeValue("password");
				if ( password == null ) throw new LangException("task ["+tname+"] needs attribute @password");
				
				try {
					conn = DriverManager.getConnection(url, user, password);
				} catch (Exception e1) {
					System.out.println("Problem with database-connection");
					_lc.error("Problem with database-connection");
					throw ( e1);
				}
				if ( e.getAttribute("autocommit") != null 
					&& e.getAttributeValue("autocommit").trim().length() > 0 ) {
					conn.setAutoCommit(Commons.stringToBool(e.getAttributeValue("autocommit")));
				}			
				if ( db.equals("mysql")) {
					Statement stmt = conn.createStatement();
					stmt.executeUpdate("SET sql_mode = 'PAD_CHAR_TO_FULL_LENGTH';");
					System.out.println("Did set sql_mode");
				}
			} else {
				// im dbci_style
				dbci_id = e.getAttributeValue("dbci");
				if ( dbci_id == null ) throw new LangException("task ["+tname+"] needs attribute @dbci");
				if ( e.getAttribute("dbci_app")!=null &&
						e.getAttributeValue("dbci_app").trim().length() > 0 ) {
					dbci_app = e.getAttributeValue("dbci_app");
				}
				
				CoopDbci dbci = null;
				
				if ( dbci_app == null)				
					dbci= (CoopDbci)rh.getNo(dbci_id, Wellknown.coop_type_dbci);
				else
					dbci= (CoopDbci)rh.getNoApp(dbci_id, Wellknown.coop_type_dbci,dbci_app);
				
				if ( dbci == null )
					throw new LangException("task ["+tname+"]: A dbci with the id '"+dbci_id+"' does not exist");
				
				conn = dbci.getConnection(rh);
				/*
				if ( e.getAttribute("autocommit") != null 
						&& e.getAttributeValue("autocommit").trim().length() > 0 ) {
						conn.setAutoCommit(Commons.stringToBool(e.getAttributeValue("autocommit")));
					}		*/			
				
			}

			 rh.putNo(exeid, Wellknown.coop_type_dbc, this);
			_lc.log_ext("Opened "+exeid, this);
			break;
		}
		default: { 	
			CoopDbc dbc = (CoopDbc)rh.getNo(exeid, Wellknown.coop_type_dbc);
			if ( dbc == null ) 
				throw new Exception("The "+tname+" with id '"+exeid+"' was not found");
			
			Connection myconn = dbc.conn;
			
			if ( myconn == null ) 
				throw new Exception("The "+tname+" with id '"+exeid+"' does not have a connection.");			
			
			switch ( ac ) {
		    case act_get_autocommit: {	
		    	rh.setVar("prop", e.getAttributeValue("prop"), Commons.boolToString(myconn.getAutoCommit()));
		    	break;
		    }
		    case act_ps_add: {	
				PreparedStatement prep = myconn.prepareStatement(
						 e.getAttributeValue("sql"));
				dbc.psMap.put(e.getAttributeValue("ps"), prep);
		    	break;
		    }
		    case act_ps_remove: {	
		    	dbc.psMap.remove(e.getAttributeValue("ps"));
		    	break;
		    }
		    case act_ps_set_value: {	
				PreparedStatement prep = dbc.psMap.get(e.getAttributeValue("ps"));
				setPrepParas(prep,e);
				List<Element> childs = e.getChildren();
				Iterator<Element> childsI = childs.iterator();

				while ( childsI.hasNext() ) {
					Element cur = childsI.next();
					setPrepParas(prep,cur);
				}
		    	break;
		    }
		    case act_select:
		    case act_update: {	
		    	boolean isSelect = true;
		    	
				if ( ac == act_update ) isSelect = false;
				String query = null;
				PreparedStatement theps = null;
				
				if ( e.getAttribute("ps") != null ) {
					theps = dbc.psMap.get(e.getAttributeValue("ps"));
				} else if ( e.getAttribute("file") != null ) {
					String file_encoding = Commons.para2string(e.getAttributeValue("file_encoding"),"UTF-8","file_encoding",_lc,this);
					if ( file_encoding.equals("") ) file_encoding = "UTF-8";	
					//System.out.println("file="+e.getAttributeValue("file"));
					//System.out.println("enco="+file_encoding);
					query = CommonsVfs.readTextfileFormatLfDefault(e.getAttributeValue("file"), file_encoding,rh.getSitzung());
					//System.out.println("query="+query);
					
				} else {
					query = Commons.getXValue(e);
				}
				
				
				// root_name
				String root_name = Commons.para2string(e.getAttributeValue("root_name"),"root","root_name",_lc,this);
				if ( root_name.equals("") ) root_name = "root";
				
				// row_name
				String row_name = Commons.para2string(e.getAttributeValue("row_name"),"row","row_name",_lc,this);
				if ( row_name.equals("") ) row_name = "row";
				
				// row_index_name
				String row_index_name = Commons.para2string(e.getAttributeValue("row_index_name"),null,"row_index_name",_lc,this);
				if ( row_index_name!= null && row_index_name.equals("") ) row_index_name = null;
				
				// style
				int style =style2int(e.getAttributeValue("style"),_lc,this);
				
				// out
				String out = Commons.para2string(e.getAttributeValue("out"),null,"out",_lc,this);
				if ( out == null && isSelect )
					throw new LangException(503,tname+": attribute 'out' is required."
									+" It has the filename of the xml-file to be created. Found: "+Commons.toText(e));		
				
				// encoding
				String encoding = Commons.para2string(e.getAttributeValue("encoding"),"UTF-8","encoding",_lc,this);
				if ( encoding.equals("") ) encoding = "UTF-8";
				
				// trim
				boolean trim_flag = Commons.hasFlag(e, "trim");
				


				// limit
				int limit = -1;
				if ( e.getAttribute("limit") != null && (e.getAttributeValue("limit")).length()> 0 )
					limit = Commons.string2int(e.getAttributeValue("limit"));
				
				// fields
				String fields = Commons.para2string(e.getAttributeValue("fields"),null,"fields",_lc,this);
						
				Statement stmt = null;
		        ResultSet rs = null;
		        
				try { 
					stmt = myconn.createStatement();
					if ( !isSelect ) {
						// update
						if ( query != null ) {
							int updateValue = stmt.executeUpdate(query);
							if ( e.getAttribute("update_return_prop") != null ) {
								rh.setVar("prop", e.getAttributeValue("update_return_prop"), Commons.int2String(updateValue));
							}							
						} else if ( theps != null ) {
							int updateValue = theps.executeUpdate();
							if ( e.getAttribute("update_return_prop") != null ) {
								rh.setVar("prop", e.getAttributeValue("update_return_prop"), Commons.int2String(updateValue));
							}								
						}

						
					} else {
						// select
						if ( query != null ) {
							rs = stmt.executeQuery(query);
						} else if ( theps !=null ) {
							rs = theps.executeQuery();
						}
						
						ResultSetMetaData rsmd = rs.getMetaData();
						int columnCount = rsmd.getColumnCount();

						Unique unique = new Unique("xml_attribute");
						String[] name_array = new String[columnCount];
						for (int j = 1; j <= columnCount; j++ ) {
							name_array[j-1] = unique.getValue(rsmd.getColumnName(j));							
						}
						
						if ( fields != null && fields.trim().length()>0)  {
							Element eFields = new Element("fields");
							eFields.setAttribute("count", Commons.int2String(columnCount));
							
							
							for (int i = 1; i <= columnCount; i++ ) {
								Element eField = new Element("field");
								eField.setAttribute("catalog_name", rsmd.getCatalogName(i));
								eField.setAttribute("display_size", Commons.int2String(rsmd.getColumnDisplaySize(i)));
								eField.setAttribute("label", rsmd.getColumnLabel(i));
								eField.setAttribute("name", rsmd.getColumnName(i));
								eField.setAttribute("type_name", rsmd.getColumnTypeName(i));
								eField.setAttribute("type", ""+rsmd.getColumnType(i));
								eField.setAttribute("precision", Commons.int2String(rsmd.getPrecision(i)));
								eField.setAttribute("scale", Commons.int2String(rsmd.getScale(i)));
								eField.setAttribute("schema", rsmd.getSchemaName(i));
								eField.setAttribute("table", rsmd.getTableName(i));
								eField.setAttribute("nullable", Commons.int2String(rsmd.isNullable(i)));
								eFields.addContent(eField);
							}
							Document documentFields = new Document(eFields);
							
							
							CommonsVfs.writeToFile(documentFields, fields, Format.getRawFormat(), encoding,rh.getSitzung());
						}
						
						// printer machen
						
						PrintStream psPrinter= null ;
						
					
						
						if ( out != null ) {
							OutputStreamRequest osr = CommonsVfs.getOutputStreamByFilenameP1(out, false,rh.getSitzung());
							OutputStream psOutputStream = osr.getOutputStream();
							
							psPrinter = new PrintStream(psOutputStream,true,encoding);
							// begin of file und header
							switch ( style ) {
								case style_c: {
									if ( row_index_name != null )
										psPrinter.print(row_index_name);
									for (int m = 1; m <= columnCount; m++ ) {
										if ( m > 1 || row_index_name != null)
											psPrinter.print(",");
											
										psPrinter.print(name_array[m-1]);
										
									}
									psPrinter.println("");								
									break;
								} case style_a: {
									psPrinter.println("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>");
									psPrinter.println("<"+root_name+">");
									break;
								} case style_e: {
									psPrinter.println("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>");
									psPrinter.println("<"+root_name+">");
									break;
								} case style_g: {
									psPrinter.println("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>");
									psPrinter.println("<rows>");
									psPrinter.print("<row>");
									for (int m = 1; m <= columnCount; m++ ) {
										psPrinter.println("");
										psPrinter.print("<c>");	
										psPrinter.print(Commons.escapeXml(name_array[m-1]));
										psPrinter.print("</c>");
										
									}	
									psPrinter.println("");
									psPrinter.print("</row>");
									break;
								}
							}
							
							String zero = new String("0");
							//
							int recordCounter = 0;
							// xml file-header ausgeben
							while (rs.next() && ( limit == -1 || recordCounter < limit )) {
								recordCounter++;
								// xml row-header ausgeben
								// gegebenfalls den index ausgeben
								
								switch ( style ) {
									case style_c: {
										if ( recordCounter > 1 )
											psPrinter.println("");
										if ( row_index_name != null )
											psPrinter.print(Commons.int2String(recordCounter));							
										break;
									} case style_a: {
										psPrinter.print("<"+row_name);
										if ( row_index_name != null )
											psPrinter.print(" "+row_index_name+"=\""+recordCounter+"\" ");	
										else 
											psPrinter.print(" ");	
										break;
									} case style_e: {
										psPrinter.println("");
										psPrinter.print("<"+row_name);
										if ( row_index_name != null )
											psPrinter.print(" "+row_index_name+"=\""+recordCounter+"\" ");

										psPrinter.print(">");
										break;
									} case style_g: {
										 	psPrinter.println("");
											psPrinter.print("<row>");
											break;
									}									
								}								
								//System.out.println("Record="+recordCounter );
								
								for (int k = 1; k <= columnCount; k++ ) {
									//rsmd.
									//rs.get
									int the_column_type = rsmd.getColumnType(k);
									String colValueString = null;
									Object columnObject = rs.getObject(k);
									
									if ( the_column_type == 2 ) {
										// numeric
								        if (columnObject != null) {
								        	String work = columnObject.toString();
								        	if ( work.indexOf(".") != -1 ) {
								        		while ( work.endsWith(zero)) {
								        			//System.out.println(work);
								        			work = work.substring(0, work.length()-1);
								        			//System.out.println("  "+work);
								        		}
								        	}
								        	if ( work.endsWith("."))
								        		work = work.substring(0, work.length()-1);
								            colValueString = work;

								        }											
									} else if ( the_column_type == 8 ) {
										// float
										// BigDecimal bd = rs.getBigDecimal(k);
										float fl = rs.getFloat(k);
										String f = String.format("%.5f", fl);
										int i =(int) (fl * 10000f);
										//System.out.println("i="+i);
										int b = i / 10000;
										//System.out.println("fl="+fl+" i="+i+" b="+b+" f="+f);
										colValueString =f; // = Integer.toString(b);
										//float b = (float)
										//Float myFloat = Float.valueOf(fl);
										//BigDecimal bd = new BigDecimal(b);
										//colValueString = bd.toPlainString();
									} else {
										
							            
    									
								        if (columnObject != null) {
								            colValueString = columnObject.toString();

								        }										
									}
									
						            if ( trim_flag && colValueString!= null )
						            	colValueString = colValueString.trim();
						            
									switch ( style ) {
										case style_c: {
											if ( k > 1 || row_index_name != null )
												psPrinter.print(",");
											if (colValueString!=null )
											 psPrinter.print(StringEscapeUtils.escapeCsv(colValueString));
											/*
											if ( colValueString.indexOf("\"") > -1 || colValueString.indexOf(",") > -1 )
												psPrinter.print("\""+colValueString.replace("\"", "\"\"")+"\"");							
											else
												psPrinter.print(colValueString);
												*/
											break;
										} case style_a: {
											if (columnObject != null && colValueString!=null) {
												psPrinter.print(name_array[k-1]+"=\""+Commons.escapeXml(colValueString)+"\" ");
											}										
											break;
										} case style_e: {
											if (columnObject != null && colValueString!=null) {
												psPrinter.println("");
												psPrinter.print("<"+name_array[k-1]+">"+Commons.escapeXml(colValueString)+"</"+name_array[k-1]+">");
											}										
											break;
										} case style_g: {
												psPrinter.print("<c>"+Commons.escapeXml(colValueString)+"</c>");
												break;
										}										
									}
							        
									
								} // ende der columns
								

							    // xml row-footer ausgeben
								switch ( style ) {
									case style_c: {
										
																	
										break;
									} case style_a: {
										psPrinter.println("/>");
										break;
									} case style_e: {
										psPrinter.println("");
										psPrinter.println("</"+row_name+">");
										break;
									} case style_g: {
										psPrinter.print("</row>");
										break;
									}
								}
							}
							// xml file-footer ausgeben und fertig			
							switch ( style ) {
							case style_c: {
								
															
								break;
							} case style_a: {
								psPrinter.println("</"+root_name+">");
								break;
							} case style_e: {
								psPrinter.println("</"+root_name+">");
								break;
							} case style_g: {
								psPrinter.println("</rows>");
								break;
							}
						}							
							psPrinter.close();

						}
						
					}
						



				} finally {
					if (rs != null) { rs.close();  }
			        if (stmt != null) { stmt.close(); }
			    }
		    	break;
		    }
		    case act_test: {	
		        break;
		    }
		    case act_set: {	
				if ( e.getAttribute("autocommit") != null ) {

					myconn.setAutoCommit(Commons.stringToBool(e.getAttributeValue("autocommit")));
				}
		    	break;
		    }
		    case act_commit: {	
		    	myconn.commit();
		    	break;
		    }	
		    case act_rollback: {	
		    	myconn.rollback();
		    	break;
		    }
		    case act_close: {	
		    	rh.removeNo(dbc.id, Wellknown.coop_type_dbc);
		    	if ( dbc.how_int == how_url)
		    		myconn.close();
		    	else {
		    		// per dbci
		    		CoopDbci dbci = (CoopDbci)rh.getNo(dbc.dbci_id, Wellknown.coop_type_dbci);
					if ( dbci == null ) {
						myconn.close();
						
						// throw new LangException("task ["+tname+"]: A dbci with the id '"+dbci_id+"' does not exist");
						
					} else
						dbci.closeConnection(myconn);
		    	}
				_lc.log_ext("Closed "+dbc.id, this);
		    	break;
		    }		    
		    } 
		  	break;

		}
		}  
				
		 
		return retParas;
	}
	
	private static String correctName(String in_orig ) {
		String in = in_orig;
		if ( in_orig == null) in = "";
	
		in = in.trim();
		in = in.replaceAll("[^A-Za-z0-9]", "_");
		if (!in.matches("^[A-Za-z_].*$")) { 
			in = '_' +in;
		}
		
		return in;
		
		
	}
	
	private static final int style_a = 1; // xml attribute
	private static final int style_g = 2; // xml generic
	private static final int style_e = 3; // xml element	
	private static final int style_c = 4; // csv
	private static final int style_gt = 5; // xml generic table (auch mit nullern)
	private static int style2int ( String value, ILogConnector lc, ITask iBug) throws Exception {
		
		String style_orig =Commons.para2string(value,"a","style",lc,iBug);
		int style = -1;
		if ( style_orig.equals("a"))
			style = style_a;
		else if ( style_orig.equals("g"))
			style = style_g;
		else if ( style_orig.equals("e"))
			style = style_e;
		else if ( style_orig.equals("c"))
			style = style_c;
		else if ( style_orig.equals("gt"))
			style = style_gt;
		else
			throw new Exception ("dbc says: The attribute 'style' should be 'c', 'gt', 'e', 'a' or 'g', but it is '"+style_orig+"'");

		return style;

	}		
	public String getExtensionId() {
		return ( tname);
	}



	@Override
	public void autoClose() {
		// TODO Auto-generated method stub
		try {
			conn.close();
			_lc.debug("Autoclose: "+id, this);
			//rh.terminalProgress("Autoclose: "+id, this);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void staticTaskdef(Element eTaskdef, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("WebDriverActions",12);
		tasknames.put("open", act_open);
		tasknames.put("get_autocommit", act_get_autocommit);
		tasknames.put("ps_add", act_ps_add);
		tasknames.put("ps_remove", act_ps_remove);
		tasknames.put("ps_set_value", act_ps_set_value);
		tasknames.put("select", act_select);
		tasknames.put("update", act_update);
		tasknames.put("test", act_test);
		tasknames.put("set", act_set);
		tasknames.put("commit", act_commit);
		tasknames.put("rollback", act_rollback);
		tasknames.put("close", act_close);
		tasknames.putFinished();		
		
		pstypes = new KeyWordMapper("PsTypes",7);
		pstypes.put("int", pst_int);
		pstypes.put("int_null", pst_int_null);
		pstypes.put("null", pst_null);
		pstypes.put("timestamp", pst_timestamp);
		pstypes.put("timestamp_null", pst_timestamp_null);
		pstypes.put("string", pst_string);
		pstypes.put("string_null", pst_string_null);
		pstypes.putFinished();		
		
		hows = new KeyWordMapper("Dbc-How",2);
		hows.put("dbci", how_dbci);
		hows.put("url", how_url);		
	}





}
