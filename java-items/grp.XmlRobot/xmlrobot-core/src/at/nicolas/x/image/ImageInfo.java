package at.nicolas.x.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import at.nicolas.x.int_specs.Commons;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ISitzung;

public class ImageInfo {

	FileObject imageObject;
	int height;
	int width;
	int heightScaled;
	int widthScaled;	
	public ImageInfo(String uriOfImg, ISitzung sitzung ) throws IOException {
		imageObject = CommonsVfs.grantsFileRead(uriOfImg, sitzung);
		URL imageURL = imageObject.getURL();
		final BufferedImage bi = ImageIO.read(imageURL);
		height = bi.getHeight();
		width = bi.getWidth();
		heightScaled = height;
		widthScaled = width;
		
	}
	
	public void scale ( String max_x, String max_y ) {
		// System.out.println("Scale max_x="+max_x+" max_y="+max_y);
		// System.out.println("  Orig width="+width+" height="+height);
		int max_x_int = -1;
		if ( max_x != null && max_x.length() > 0 ) {
			max_x_int = Commons.string2int(max_x);
		}
		
		int max_y_int = -1;
		if ( max_y != null && max_y.length() > 0 ) {
			max_y_int = Commons.string2int(max_y);
		}		
		
		heightScaled = height;
		widthScaled = width;
		
		if ( max_x_int > 0 && max_x_int < widthScaled ) {
			int ratio_x = (max_x_int*1000) / widthScaled;
			// System.out.println("max_x_int="+max_x_int+" widthScaled="+widthScaled+" ratio_x="+ratio_x);
			widthScaled = (widthScaled * ratio_x)/1000;
			heightScaled = (heightScaled * ratio_x)/1000;
			// System.out.println("new: widthScaled="+widthScaled+" heightScaled="+heightScaled);
		}
		
		if ( max_y_int > 0 && max_y_int < heightScaled ) {
			int ratio_y = (max_y_int*1000) / heightScaled;
			// System.out.println("max_y_int="+max_y_int+" heightScaled="+heightScaled+" ratio_y="+ratio_y);
			widthScaled = (widthScaled * ratio_y)/1000;
			heightScaled = (heightScaled * ratio_y)/1000;
			// System.out.println("new: widthScaled="+widthScaled+" heightScaled="+heightScaled);
		}		
		// System.out.println("final: widthScaled="+widthScaled+" heightScaled="+heightScaled);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public final int getHeight() {
		return height;
	}

	public final int getWidth() {
		return width;
	}

	public final int getHeightScaled() {
		return heightScaled;
	}

	public final int getWidthScaled() {
		return widthScaled;
	}

}
