package at.nicolas.x.hibiscus;

public class MvUpdateDefinition {

	public String getTable() {
		return table;
	}

	public String getView() {
		return view;
	}
	
	public String getCols() {
		return cols;
	}	

	private String table = null;
	private String view = null;
	private String cols = null;
	
	MvUpdateDefinition ( String _table, String _view, String _cols) {
		table = _table;
		view = _view;
		cols = _cols;
	}
}
