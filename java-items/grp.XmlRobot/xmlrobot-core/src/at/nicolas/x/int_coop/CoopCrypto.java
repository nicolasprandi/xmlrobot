package at.nicolas.x.int_coop;

import org.jdom2.Element;

import at.nicolas.x.int_const.Wellknown;
import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.ICoop;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;

public class CoopCrypto implements ITask, ICoop {

	private CryptoBase symKeyExample;
	public static final String myCoopType = "_cry";
	
	IRequestHandler rh;
	/*
	
	int missing_strategy = 2;
	final int MISSING_STRATEGY_EMPTY = 0;
	final int MISSING_STRATEGY_EXCEPTION = 1;
	final int MISSING_STRATEGY_JOVIAL = 2;
	
	int logic_id = -1;
		
	
	
	
	String filename; 	
	Document doc;
	Element root;
	public boolean flag_dont_escape_on_read;
	public boolean old = false;	
	
	*/
	

	String id;
	private String id_app = null;
	
	private boolean allowEncrypt = false;
	private boolean allowDecrypt = false;
	private boolean allowValidate = false;
	

	public boolean isValid ( String xClear, String xEncrypted ) throws Exception {
		if ( !allowValidate ) throw new Exception("crypto-privacy-problem#5");
		final String pwdUser = symKeyExample.encrypt(xClear);

		if ( xEncrypted == null || pwdUser == null || !xEncrypted.equals(pwdUser))
			return false;
		else
			return true;	
	}
	
	public String decrypt ( String in ) throws Exception {
		if ( !allowDecrypt ) throw new Exception("crypto-privacy-problem#6");
		return symKeyExample.decrypt(in);
	}
	public String encrypt ( String in ) throws Exception {
		if ( !allowEncrypt ) throw new Exception("crypto-privacy-problem#7");
		return symKeyExample.encrypt(in);
	}	
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		
		if ( e.getAttribute("act") == null ) {
			throw new Exception("print needs attribute @act (open,xml_update,save,close)");
		}
		
		if ( e.getAttribute("id") == null ) {
			throw new Exception("print needs attribute @id");
		}		
		
		String action = e.getAttributeValue("act");
		id = e.getAttributeValue("id"); // ??nic321

		if (e.getAttribute("id_app") != null ) {
			if (e.getAttributeValue("id_app").trim().length() > 0 )
				id_app = e.getAttributeValue("id_app");
		}	
		
		this.rh = rh;
				
		Parameters retParas = null;
		

		
		if ( action.equals("open")) {
			symKeyExample = new CryptoBase(e.getAttributeValue("type"));				

			if (e.getAttribute("only") != null && 
				e.getAttributeValue("only").trim().length() > 0 ) {
				
				if ( Commons.hasFlag(e.getAttributeValue("only"), "encrypt"))
					allowEncrypt = true;
				if ( Commons.hasFlag(e.getAttributeValue("only"), "decrypt"))
					allowDecrypt = true;
				if ( Commons.hasFlag(e.getAttributeValue("only"), "validate"))
					allowValidate = true;				
				
			} else {
				allowEncrypt = true;
				allowDecrypt = true;
				allowValidate = true;
			}
			
			/*
			 * 	private boolean allowEncrypt = false;
	private boolean allowDecrypt = false;
	private boolean allowValidate = false;
			 */
			if ( id_app == null )
				rh.putNo(id, myCoopType,  this);
			else
				rh.putNoApp(id, myCoopType,  this,id_app);			
					
		} else {
			CoopCrypto scrypt;
			System.out.println("CoopCrypto is requesting object with app="+id_app+" id="+id);
			
			if ( id_app == null )
				scrypt = (CoopCrypto)rh.getNo(id, myCoopType);
			else
				scrypt = (CoopCrypto)rh.getNoApp(id, myCoopType,id_app);
			
			if ( scrypt == null ) 
				throw new Exception("The scrypt with id '"+id+"' was not found");
			CryptoBase ske = scrypt.symKeyExample;

			if ( action.equals("encrypt") && e.getAttribute("in")!=null && e.getAttribute("out")!=null){
				if ( !scrypt.allowEncrypt ) throw new Exception("crypto-privacy-problem#1");
				String in = e.getAttributeValue("in");
				String out = ske.encrypt(in);
				this.rh.setVar("prop", e.getAttributeValue("out"), out);
				// retParas = new Parameters();
				// retParas.setParameter("prop_"+eN.getAttributeValue("out"), out);

			} else if ( action.equals("decrypt") && e.getAttribute("in")!=null&& e.getAttribute("out")!=null ){
				if ( !scrypt.allowDecrypt ) throw new Exception("crypto-privacy-problem#2");
				String in = e.getAttributeValue("in");
				String out = ske.decrypt(in);
				this.rh.setVar("prop", e.getAttributeValue("out"), out);
				/*
				retParas = new Parameters();
				retParas.setParameter(eN.getAttributeValue("out"), out);*/

			} else if ( action.equals("encrypt")){
				if ( !scrypt.allowEncrypt ) throw new Exception("crypto-privacy-problem#3");				
				String from = e.getAttributeValue("from");
				String to = e.getAttributeValue("to");
				ske.encryptFile(from, to,rh.getSitzung());

			} else if ( action.equals("decrypt")){
				if ( !scrypt.allowDecrypt ) throw new Exception("crypto-privacy-problem#4");
				String from = e.getAttributeValue("from");
				String to = e.getAttributeValue("to");
				ske.decryptFile(from, to,rh.getSitzung());				
				
			} else if ( action.equals("close")){

				if ( id_app == null )
					rh.removeNo(id, myCoopType);
				else
					rh.removeNoApp(id, myCoopType, id_app);				
				ske.down();

				//_lc.log_ext("Closed "+id, this);
			} else
				throw new Exception("print attribute @act must be open or close");
			
		}
		 
		return retParas;
	}
	



	@Override
	public void autoClose() {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}



}