package at.nicolas.x.int_specs;

import org.jdom2.Element;

import at.nicolas.x.pub.ISitzung;

public class DummyTiHandler implements ITiHandler {

	public DummyTiHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Surrounding getSurrounding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISitzung getSitzung() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printTrace(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public Element translate(Element e) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String translateString(String x) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVar(String varType, String varIdE, String valueE) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVarFollow(String varType, String varIdE, String valueE, String followJobN) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public String getVar(String varType, String varIdE) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITerminator getTerminator() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGlobalAndRemove(String global_id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAppId() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProId() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAppDir() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProDir() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUriPresentation(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean triggerButtonsByButton() {
		// TODO Auto-generated method stub
		return true;
	}

}
