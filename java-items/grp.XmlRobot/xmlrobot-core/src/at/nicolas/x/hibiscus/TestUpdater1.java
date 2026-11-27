package at.nicolas.x.hibiscus;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TestUpdater1 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		Updater updater = new Updater("C:/Users/nicolas/Documents/testupdater1.xml",
			"testCat",
			"testName",
			4,
			10*4+IntParas.bytesPerInt,
			null,null);
		
		updater.setInt(1);
		updater.setInt(244);
		updater.setInt(3234234);
		updater.setInt(4111440);
		updater.switchFromWritingToReading(1);
		
		System.out.println("Read1 ="+updater.getInt());
		System.out.println("Read2 ="+updater.getInt());
		System.out.println("Read3 ="+updater.getInt());
		System.out.println("Read4 ="+updater.getInt());
		System.out.println("Read4 ="+updater.getInt());
		
		updater.close(1);

	}

}
