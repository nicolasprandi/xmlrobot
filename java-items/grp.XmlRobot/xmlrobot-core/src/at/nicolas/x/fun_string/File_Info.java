/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.httpclient.util.URIUtil;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.KeyWordMapper;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class File_Info implements IFun {

	private final static String funn="file_info";
	private final static String pm ="function 'file_info' ";
	
	private static final int act_classic   = 0;
	private static final int act_hash   = 1;
	private static final int act_last_mod   = 2;
	private static final int act_len   = 3;
	private static final int act_xml_content   = 4;
	private static final int act_size   = 5;
	private static final int act_libro   = 6;

	private static KeyWordMapper tasknames;	
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh) throws Exception {
		
		Vcar ret = null;
		if ( vcar_list.size() < 1 ) 
			throw new LangException(332,pm+"wants at least 1 parameter: [1] hash|last_mod|len|size|classic|libro ");
		

		int ac = tasknames.convert(((Vcar)vcar_list.get(0)).s);

		switch ( ac ) {
		case act_classic: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(333,pm+" variation 'classic' wants 2 parameters: [1] 'classic' [2] filename");
			// CommonsVfs.grantsFileRead(((Vcar)vcar_list.get(1)).s, rh.getSitzung());
			ret = new Vcar(Commons.fileClassic(((Vcar)vcar_list.get(1)).s));
			break;
		}
		case act_hash: {	
			if ( vcar_list.size() != 3 ) 
				throw new LangException(333,pm+" variation 'hash' wants 3 parameters: [1] 'hash' [2] hash-algorigthm [3] filename");
			String alg = ((Vcar)vcar_list.get(1)).s;
			String filename = ((Vcar)vcar_list.get(2)).s;
			// FileInputStream fis = new FileInputStream(filename);asdf
			InputStream fis = CommonsVfs.getInputStreamByFilename(filename,rh.getSitzung());
			String code="";
			
			int hc; 
				
			try {
				hc = Hash.tasknames.convert(alg);
			} catch (Exception e) {
				// TODO: handle exception
				fis.close();
				throw e;
			}
			{
				

				switch ( hc ) {
				case Hash.act_md2: {	
					code = org.apache.commons.codec.digest.DigestUtils.md2Hex(fis);
				    break;
				}
				case Hash.act_md5: {	
					code = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
				    break;
				}
				case Hash.act_sha1: {
					code = org.apache.commons.codec.digest.DigestUtils.sha1Hex(fis);
				    break;
				}
				case Hash.act_sha256: {
					code = org.apache.commons.codec.digest.DigestUtils.sha256Hex(fis);
				    break;
				}
				case Hash.act_sha384: {	
					code = org.apache.commons.codec.digest.DigestUtils.sha384Hex(fis);
					break;
				}
				case Hash.act_sha512: {	
					code = org.apache.commons.codec.digest.DigestUtils.sha512Hex(fis);
				    break;
				}

				}  	
			}
			

			fis.close();						
			ret = new Vcar(code);
			ret.dontImport();
			break;
		}
		case act_last_mod: {	
			if ( vcar_list.size() < 2 || vcar_list.size() > 3) 
				throw new LangException(335,pm+" variation 'last_mod' wants 2 or 3 parameters: [2] file [3] date format");			
			
			// File file = new File ( ((Vcar)vcar_list.get(1)).s );
			long last_mod = CommonsVfs.getLastModByFilename(((Vcar)vcar_list.get(1)).s,rh.getSitzung());
			Date date_mod = new Date(last_mod);
			
			SimpleDateFormat formatter;
			if ( vcar_list.size() == 3 )
				formatter = new SimpleDateFormat (((Vcar)vcar_list.get(2)).s);
			else
				formatter = new SimpleDateFormat ("yyyy-MM-dd");
			
			String dateString = formatter.format(date_mod);
			
			ret = new Vcar(dateString);
			break;
		}
		case act_len: {	
			/*[echnaton-crypto]*/
			if ( vcar_list.size() < 2 || vcar_list.size() > 3) 
				throw new LangException(336,pm+" variation 'len' wants 2 or 3 parameters: [2] file [3] encoding");			
			
			String filename = ((Vcar)vcar_list.get(1)).s;

			String fileconent = null;
			if ( vcar_list.size() == 2 )
				fileconent = CommonsVfs.readTextfileFormatLfDefault(filename,"UTF-8",rh.getSitzung());
			else {
				fileconent = CommonsVfs.readTextfileFormatLfURI(filename, ((Vcar)vcar_list.get(2)).s, "\n",rh.getSitzung());
			}
						
			ret = new Vcar(fileconent.length());
			ret.dontImport();
			break;
		}
		case act_xml_content: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(336,pm+" variation 'xml_content' wants 2 parameters: [2] file");			
			
			String filename = ((Vcar)vcar_list.get(1)).s;
			// CommonsVfs.grantsFileRead(filename, rh.getSitzung());
			Element ec = CommonsVfs.getElementByFilename(filename,rh.getSitzung());
			String fileconent = Commons.toTextRaw(ec);

						
			ret = new Vcar(fileconent);
			ret.dontImport();
			break;
		}
		case act_size: {	
			if ( vcar_list.size() < 2 || vcar_list.size() > 3) 
				throw new LangException(337, pm+" variation 'size' wants 2 or 3 parameters: [2] file [3] unit");			
			
			String filename = ((Vcar)vcar_list.get(1)).s;
					
			//File f = new File (filename);			
			//double bytes_double = f.length();
			long bytes_double = CommonsVfs.getSizeByFilename(filename,rh.getSitzung());
						
			String alg = "bytes";
			if ( vcar_list.size() ==3) 
				alg = ((Vcar)vcar_list.get(2)).s;
			
			int ret_int = 0;
			
			int su = Dir_Info.sunames.convert(alg);

			switch ( su ) {
			case Dir_Info.su_bytes: {	
				ret_int = (int)bytes_double;
				break;
			}
			case Dir_Info.su_kb: {	
				double kb_double = (bytes_double / 1024);
				ret_int = (int)kb_double;
				break;
			}
			case Dir_Info.su_mb: {	
				double mb_double = (bytes_double / 1048576);
				ret_int = (int)mb_double;
				break;
			}

			} 
			
						
			ret = new Vcar(ret_int);
			ret.dontImport();
			break;
		}
		case act_libro: {	
			if ( vcar_list.size() != 2 ) 
				throw new LangException(336,pm+" variation 'libro' wants 2 parameters: [2] file");			
			
			String filename = ((Vcar)vcar_list.get(1)).s;
			CommonsVfs.grantsFileRead(filename, rh.getSitzung());
			
			String rel = rh.getSitzung().getRelativeLibroPath(filename);
			String pathEncoded=URIUtil.encodePath(rel);
			
						
			ret = new Vcar(pathEncoded);
			// ret.dontImport();
			break;
		}

		}  		
		

							
		return (ret);
	}

	public String getExtensionId() {
		return "file_info";
	}

	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		tasknames = new KeyWordMapper("Fun-FileInfo",7);
		tasknames.put("classic", act_classic);
		tasknames.put("hash", act_hash);
		tasknames.put("last_mod", act_last_mod);
		tasknames.put("len", act_len);
		tasknames.put("xml_content", act_xml_content);
		tasknames.put("size", act_size);
		tasknames.put("libro", act_libro);
		tasknames.putFinished();		
		
	}

}