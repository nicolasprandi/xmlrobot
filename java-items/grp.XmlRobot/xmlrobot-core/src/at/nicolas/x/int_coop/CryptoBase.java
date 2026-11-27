package at.nicolas.x.int_coop;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.int_specs.OutputStreamRequest;
import at.nicolas.x.pub.ISitzung;

public class CryptoBase {

	//public static Encryptor staticSymKeyExample = null;

	private static String defaultAlgorithm="AES"; // ECB/PKCS5PADDING";// "AES/ECB/PKCS5";// "AES/CBC/NoPadding"; //"AES/ECB/PKCS5"; //AES
	private static int defaultKeyLen=16;
	private static String defaultEncoding="UTF-8";
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*
		CryptoBase cb = new CryptoBase("hase",cryptoSitzung);
		String start = "Halloiklkjddddddasdfa@<>jsdfpiogjarj";
		String verschl = cb.encrypt(start);
		
		String retur = cb.decrypt(verschl);
		*/

	}	
	
	private Cipher encryptCipher;
	private Cipher decryptCipher;
	public void down() {
		encryptCipher = null;
		decryptCipher = null;
	}

	// ISitzung cryptoSitzung;
	private void makeIt (String secret, int length, String algorithm/*, ISitzung cryptoSitzung*/ )
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		//this.cryptoSitzung = cryptoSitzung;
		byte[] key = new byte[length];
		key = fixSecret(secret, length);
		SecretKeySpec secretKey= new SecretKeySpec(key, algorithm);

		this.encryptCipher = Cipher.getInstance(algorithm);
		this.encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
		this.decryptCipher = Cipher.getInstance(algorithm);
		this.decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);		
	}
	public CryptoBase(String secret, int length, String algorithm/*, ISitzung cryptoSitzung*/)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		makeIt(secret,length,algorithm/*,  cryptoSitzung*/);

	}
	
	public CryptoBase(String secret, String algorithm/*, ISitzung cryptoSitzung*/)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		
		makeIt(secret,defaultKeyLen,defaultAlgorithm/*,  cryptoSitzung*/);
	}	
	
	public CryptoBase(String secret/*, ISitzung cryptoSitzung*/)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		
		makeIt(secret,defaultKeyLen,defaultAlgorithm/*,cryptoSitzung*/);
	}		

	private byte[] fixSecret(String s, int length) throws UnsupportedEncodingException {
		if (s.length() < length) {
			int missingLength = length - s.length();
			for (int i = 0; i < missingLength; i++) {
				s += " ";
			}
		}
		return s.substring(0, length).getBytes(defaultEncoding);
	}
	private String fc ( String filename,ISitzung cryptoSitzung ) throws Exception {
		return CommonsVfs.readTextfileFormatLfURI(filename, "UTF-8", "\n",cryptoSitzung);
	}
	public void encryptFile(String fromFile, String toFile,ISitzung cryptoSitzung )
			throws Exception {
		
		String res = encrypt(fc(fromFile,cryptoSitzung));
		CommonsVfs.writeToEncodedFile(res, toFile, "UTF-8",cryptoSitzung);

	}	
	
	public void decryptFile(String fromFile, String toFile,ISitzung cryptoSitzung )
			throws Exception {
		
		String res = decrypt(fc(fromFile,cryptoSitzung));
		CommonsVfs.writeToEncodedFile(res, toFile, "UTF-8",cryptoSitzung);
	}	
	
	
	
	public String decryptFile(String fIn,ISitzung cryptoSitzung) throws Exception {
		String res = decrypt(fc(fIn,cryptoSitzung));

		
		return res;
		/*
		InputStream inStream = CommonsVfs.getInputStreamByFilename(fIn);
		byte[] input = new byte[(int) fIn.length()];
		inStream.read(input);
		inStream.close();
		byte[] output = decryptCipher.doFinal(input);
		*/
		
		// return new ByteArrayInputStream(res.getBytes());
	}	
	
	
	private void fromFileToFile(String fIn, String fOut, Cipher myCipher,ISitzung cryptoSitzung) throws Exception {
		String filecontent = CommonsVfs.readTextfileFormatLfURI(fIn, "UTF-8", "\n",cryptoSitzung);
		
		
		InputStream inStream = CommonsVfs.getInputStreamByFilename(fIn,cryptoSitzung);
		byte[] input = new byte[(int) fIn.length()];
		inStream.read(input);
		
		// der input muss ein mehrfaches von 16 sein
		OutputStreamRequest osr = CommonsVfs.getOutputStreamByFilenameP1(fOut, false,cryptoSitzung);

		OutputStream outStream = osr.getOutputStream();
		byte[] output = myCipher.doFinal(input);
		
		outStream.write(output);

		outStream.flush();
		outStream.close();
		inStream.close();
		CommonsVfs.finishOutputStreamP2(fOut, cryptoSitzung, osr.getOpnum());
		
	}	
	
	   public  String encrypt(String Data) throws Exception {

           byte[] encVal = encryptCipher.doFinal(Data.getBytes());
           String encryptedValue = DatatypeConverter.printBase64Binary(encVal);
           return encryptedValue;
   }

   public  String decrypt(String encryptedData) throws Exception {
     
       byte[] decordedValue = DatatypeConverter.parseBase64Binary(encryptedData);
       byte[] decValue = decryptCipher.doFinal(decordedValue);
       String decryptedValue = new String(decValue);
       return decryptedValue;
   }	
	
	
	
	/*


	public void decryptFile(File f)
			throws InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

		this.cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
		this.writeToFile(f);
	}
	

	public void writeToFile(File f) throws IOException, IllegalBlockSizeException, BadPaddingException {
		FileInputStream in = new FileInputStream(f);
		byte[] input = new byte[(int) f.length()];
		in.read(input);

		FileOutputStream out = new FileOutputStream(f);
		byte[] output = this.cipher.doFinal(input);
		out.write(output);

		out.flush();
		out.close();
		in.close();
	}
	*/	
	/*
	public byte[] fromFileToBytesDecrypt(File fIn) throws IOException, IllegalBlockSizeException, BadPaddingException {
		FileInputStream in = new FileInputStream(fIn);
		byte[] input = new byte[(int) fIn.length()];
		in.read(input);


		byte[] output = decryptCipher.doFinal(input);

		in.close();
		return output;
		
	}	
	*/
	/*
	private byte[] fromFileToBytes(File fIn,  Cipher myCipher) throws IOException, IllegalBlockSizeException, BadPaddingException {
		FileInputStream in = new FileInputStream(fIn);
		byte[] input = new byte[(int) fIn.length()];
		in.read(input);


		byte[] output = myCipher.doFinal(input);

		in.close();
		return output;
		
	}
	*/		
	/*
	public void doEncrypt(
			String fileIn,
			String fileOut) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
		
		fromFileToFile(new File(fileIn), new File(fileOut),encryptCipher);

	
		
		
	}
	
	public static void doEncryptFolder(
			String aha,
			int len,
			String dirIn,
			String dirOut) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
		
		SymKeyExample ske = new SymKeyExample(aha, len, "AES");
		ske.cipher.init(Cipher.ENCRYPT_MODE, ske.secretKey);
		Commons.mkdirCascade(dirOut, "decript folder");
		File dir = new File(dirIn);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	if ( child.isFile() )
		    	ske.fromFileToFile(child, 
		    			new File(dirOut+child.getName()));
		    }
		}		
	}	
	
	public static void doDecryptFolder(
			String aha,
			int len,
			String dirIn,
			String dirOut) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
		
		SymKeyExample ske = new SymKeyExample(aha, len, "AES");
		ske.cipher.init(Cipher.DECRYPT_MODE, ske.secretKey);
		Commons.mkdirCascade(dirOut, "decript folder");
		File dir = new File(dirIn);
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
		    for (File child : directoryListing) {
		    	if ( child.isFile() )
		    	ske.fromFileToFile(child, 
		    			new File(dirOut+child.getName()));
		    }
		}		
	}	
	
	public static void doDecrypt(
			String aha,
			int len,
			String fileIn,
			String fileOut) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeyException {
		
		SymKeyExample ske = new SymKeyExample(aha, len, "AES");
		ske.cipher.init(Cipher.DECRYPT_MODE, ske.secretKey);
		ske.fromFileToFile(new File(fileIn), new File(fileOut));
		
	}	
	
	
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		doEncryptFolder("hans", 16, 
				"C:\\Users\\nicolas\\Documents\\_testingNewXmlRobot\\in\\a\\", 
				"C:\\Users\\nicolas\\Documents\\_testingNewXmlRobot\\in\\e\\");
		doDecryptFolder("hans", 16, 
				"C:\\Users\\nicolas\\Documents\\_testingNewXmlRobot\\in\\e\\", 
				"C:\\Users\\nicolas\\Documents\\_testingNewXmlRobot\\in\\f\\");		
		
		}
	*/
	/*
	public static void main(String[] args) {
		File dir = new File("src/cryptodir");
		File[] filelist = dir.listFiles();

		SymetricKeyExapmle ske;
		try {
			ske = new SymetricKeyExapmle("!@#$MySecr3tPassw0rd", 16, "AES");

			int choice = -2;
			while (choice != -1) {
				String[] options = { "Encrypt All", "Decrypt All", "Exit" };
				choice = JOptionPane.showOptionDialog(null, "Select an option", "Options", 0,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				switch (choice) {
				case 0:
					Arrays.asList(filelist).forEach(file -> {
						try {
							ske.encryptFile(file);
						} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
								| IOException e) {
							System.err.println("Couldn't encrypt " + file.getName() + ": " + e.getMessage());
						}
					});
					Sp.p("Files encrypted successfully");
					break;
				case 1:
					Arrays.asList(filelist).forEach(file -> {
						try {
							ske.decryptFile(file);
						} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
								| IOException e) {
							System.err.println("Couldn't decrypt " + file.getName() + ": " + e.getMessage());
						}
					});
					Sp.p("Files decrypted successfully");
					break;
				default:
					choice = -1;
					break;
				}
			}
		} catch (UnsupportedEncodingException ex) {
			System.err.println("Couldn't create key: " + ex.getMessage());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.err.println(e.getMessage());
		}
	}
	*/
}