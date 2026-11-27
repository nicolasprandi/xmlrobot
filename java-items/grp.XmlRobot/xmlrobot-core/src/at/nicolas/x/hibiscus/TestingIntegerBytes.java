package at.nicolas.x.hibiscus;

public class TestingIntegerBytes {
	
	private static byte[] toBytes(int i)
	{
	  byte[] result = new byte[4];

	  result[0] = (byte) (i >> 24);
	  result[1] = (byte) (i >> 16);
	  result[2] = (byte) (i >> 8);
	  result[3] = (byte) (i /*>> 0*/);

	  return result;
	}
	
	private static int fromByteArray(byte[] bytes) {
	     return ((bytes[0] & 0xFF) << 24) | 
	            ((bytes[1] & 0xFF) << 16) | 
	            ((bytes[2] & 0xFF) << 8 ) | 
	            ((bytes[3] & 0xFF) << 0 );
	}
	public static void main(String[] args) throws Exception {
		byte[] eins = toBytes(1);
		byte[] neuner = toBytes(1999999999);
		byte[] max = toBytes(Integer.MAX_VALUE);
		System.out.println("eins "+fromByteArray(eins));
		System.out.println("neuner "+fromByteArray(neuner));
		System.out.println("max "+fromByteArray(max));
	}

}
