package at.nicolas.webwalk;

public class ConvJTidy {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String home="C:\\Users\\nicolas\\Desktop\\Requ\\xmlrobot-internals\\workspaces\\ws.WebWalk\\app.Sugarbabe2\\pro.0325_[Lnn10]\\results\\aussenministerium\\";

		/*
		String ret = convert(home+"b.html",home+"b.xhtml");
		System.out.println(ret);
		*/

	}
	

	/*
	public static String convert(String inputFile,
            String outputFile) throws Exception {
		System.out.println("to "+outputFile);

        File file = new File(inputFile);
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            fos = new FileOutputStream(outputFile);
            is = new FileInputStream(file);
            Tidy tidy = new Tidy(); 
            tidy.setXHTML(true); 
            tidy.parse(is, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    fos = null;
                }
                fos = null;
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                }
                is = null;
            }
        }
        return outputFile;


    }	
    */

}
