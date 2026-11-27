package at.nicolas.webwalk.experiments;

public class TestConvertJsoupXHTML {
 //https://stackoverflow.com/questions/15063870/jtidy-java-api-toconvert-html-to-xhtml
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String dir = "C:\\ProgsDirect\\EclipseNeon\\XmlRobot-Home\\xmlrobot-internals\\workspaces\\ws.WebWalker\\app.wwTest1\\pro.test\\";
		String in = dir+"in.html";
		String out = dir+"out-ergebnis.xml";
		
		/*
		org.jsoup.nodes.Document document = Jsoup.parse(
				Commons.readTextfileFormat(in, "UTF-8")); 
		document.outputSettings().syntax(
				org.jsoup.nodes.Document.OutputSettings.Syntax.xml); 		
		
		String result = document.toString();
		Commons.writeToEncodedFile(result, out, "UTF-8");
		*/
	        		
	}

}
