package at.nicolas.x.pub;

public class UserInfo {
	
	private String name = null;
	private String collection = null;
	private String dbext = null;
	private long db_size = -1;
	
	private static final long default_db_size = 1048576;
	
	public  UserInfo(String name, String collection, String dbext, long db_size) {
		this.name = name;
		this.collection = collection;
		this.dbext = dbext;
		if (this.dbext != null && this.dbext.length() > 0 )
			this.dbext = this.dbext.toLowerCase();
		if ( db_size >= 0 )
			this.db_size = db_size;
		else
			this.db_size = default_db_size;
			
		
	}

	public final long getDbSize() {
		return db_size;
	}
	public final String getName() {
		return name;
	}

	public final String getCollection() {
		return collection;
	}

	public final String getDbext() {
		return dbext;
	}



}
