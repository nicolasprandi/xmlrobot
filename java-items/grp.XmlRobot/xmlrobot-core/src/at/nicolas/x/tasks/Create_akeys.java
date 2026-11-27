/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.tasks;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.jdom2.Element;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.ITask;
import at.nicolas.x.pub.Parameters;


public class Create_akeys implements ITask{

	String 	export_priv;
	String	export_pub;
	String 	algorithm;
	String	provider;
	String tname;
	


	
	
	public Parameters execute(Element e, IRequestHandler rh) throws Exception {
		this.tname = "akeys";

		
		export_priv = 		Commons.para2string(e.getAttributeValue("export_priv"),null,"export_priv");
		export_pub = 		Commons.para2string(e.getAttributeValue("export_pub"),null,"export_pub");
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);
		
		KeyPair keyPair = keyGen.generateKeyPair();
		PrivateKey priv = keyPair.getPrivate();
		PublicKey pub = keyPair.getPublic();
		
		Parameters p = new Parameters();

		p.setParameter(export_pub, Base64.encodeBase64String(pub.getEncoded())) ;
		p.setParameter(export_priv, Base64.encodeBase64String(priv.getEncoded())) ;
		
		//_lc.warn("Format-pub  :"+pub.getFormat(), this);
		//_lc.warn("Format-priv :"+priv.getFormat(), this);
		
		return p;
	}

	public String getExtensionId() {
		return ( tname);
	}

	@Override
	public void staticTaskdef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}
	
}