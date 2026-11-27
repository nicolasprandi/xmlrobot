/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.fun_string;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jdom2.Element;

import at.nicolas.x.pub.IFun;
import at.nicolas.x.pub.IRequestHandler;
import at.nicolas.x.pub.LangException;
import at.nicolas.x.pub.Vcar;

public class Akeys implements IFun {

	
	private static final String pm="akeys: ";
	public Vcar execute(ArrayList<Vcar> vcar_list, IRequestHandler rh ) throws Exception {


		if ( vcar_list.size() != 3) 
			throw new LangException(373,pm+"I want 3 parameters: 1=encode/decode 2=message, 3=hex-key");

		Vcar   type= (Vcar)vcar_list.get(0);		
		Vcar   message= (Vcar)vcar_list.get(1);
		Vcar   hexkey= (Vcar)vcar_list.get(2);

		if ( type.s.equals("encode")) {
			
			PublicKey key = loadPublicKey(hexkey.s);
			byte[] enc = encrypt(message.s, key);

			String ret = byteArrayToMessage(enc);
			
			return (new Vcar(ret));
			
		} else if ( type.s.equals("decode")) {
			
			PrivateKey key = loadPrivateKey(hexkey.s);
			byte[] dec = decrypt(message.s, key);

			String ret = byteArrayToMessage(dec);
		
			return (new Vcar(ret));
		}

		String rx="hallo";
		return (new Vcar(rx));
	}
	  private static byte[] encrypt(String in, PublicKey key) throws Exception {
		    Cipher cipher = Cipher.getInstance("RSA");
		    cipher.init(Cipher.ENCRYPT_MODE, key);
		    return cipher.doFinal(in.getBytes());
		  }
	  
	  private static byte[] decrypt(String in, PrivateKey key) throws Exception{
	    Cipher cipher = Cipher.getInstance("RSA");
	    cipher.init(Cipher.DECRYPT_MODE, key);
	    return cipher.doFinal(in.getBytes());
	  }
	
	private static String bytesArrayToStringHex (byte[] in ) {
		return Hex.encodeHexString( in );
	}
	
	private static byte[] stringToByteArrayHex (String in ) throws DecoderException {
		return Hex.decodeHex(in.toCharArray());
	}
	
	private static byte[] messageToByteArray (String in ) throws DecoderException {
		return  in.getBytes();
	}	
	private static String byteArrayToMessage (byte[] in ) throws DecoderException {
		return  new String(in);
	}		

	

	public String getExtensionId() {
		return "akeys";
	}

	public static PrivateKey loadPrivateKey(String s) throws GeneralSecurityException, DecoderException {
	    byte[] clear = (stringToByteArrayHex(s));
	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
	    KeyFactory fact = KeyFactory.getInstance("RSA");
	    PrivateKey priv = fact.generatePrivate(keySpec);
	    Arrays.fill(clear, (byte) 0);
	    return priv;
	}	
	
	public static PublicKey loadPublicKey(String s) throws GeneralSecurityException, DecoderException {
	    byte[] data = (stringToByteArrayHex(s));
	    X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
	    KeyFactory fact = KeyFactory.getInstance("RSA");
	    return fact.generatePublic(spec);
	}	

	public static String publicKeyToString(PublicKey p) {

	    return Hex.encodeHexString( p.getEncoded() ) ;

	}
	public static String privateKeyToString(PrivateKey p) {

	    return Hex.encodeHexString( p.getEncoded() ) ;
	}
	@Override
	public void staticFundef(Element e, String id, IRequestHandler rh) {
		// TODO Auto-generated method stub
		
	}	

}