package at.nicolas.x.web;

import java.text.DecimalFormat;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import at.nicolas.x.int_interpreter.Sp;
import at.nicolas.x.int_specs.CommonsVfs;
import at.nicolas.x.pub.ISitzung;
import at.nicolas.x.pub.StorageLimitException;

public class StorageControl {
	String homeDir = null;
	FileObject home = null;
	long sizeLimit = 0;
	long currentUsage = 0;
	ISitzung sitzung = null;
	public static final int TYPE_SESSION = 1;
	public static final int TYPE_MEMORY = 2;
	public static final int TYPE_PROJECT = 3;
	public static final int TYPE_DB = 4;
	private int type;
	
	/*
	long    limit_70_percent = 0;
	boolean reached_70_percent = false; */
	
	long    limit_80_percent = 0;
	boolean reached_80_percent = false;
	
	long    limit_90_percent = 0;
	boolean reached_90_percent = false;	
	
	long    limit_95_percent = 0;
	boolean reached_95_percent = false;		
	
	IMessageBox iMessageBox = null;
	private String getTypeText() {
		return typeToText(this.type );
	}
	private  String typeToText(int type ) {
		if ( type == TYPE_SESSION ) return sitzung.lang().getScTpSess();
		else if ( type == TYPE_MEMORY ) return sitzung.lang().getScTpMemo();
		else if ( type == TYPE_PROJECT ) return sitzung.lang().getScTpProj();
		else if ( type == TYPE_DB ) return sitzung.lang().getScTpDb();
		else return (sitzung.lang().getScTpUnknown()+" "+type);
	}

	public StorageControl(String homeDir , long sizeLimit, ISitzung sitzung, int _type,
			IMessageBox _iMessageBox ) throws FileSystemException {
		this.iMessageBox = _iMessageBox;
		this.homeDir = homeDir;
		this.type = _type;
		
		this.sizeLimit = sizeLimit;
		this.sitzung = sitzung;
		//this.home = CommonsVfs.resolveFileRead(homeDir,this.sitzung);
		this.home = CommonsVfs.resolveAnySilent(homeDir);
		// limit_70_percent = (long) (sizeLimit * 70 ) / 100;
		limit_80_percent = (long) (sizeLimit * 80 ) / 100;
		limit_90_percent = (long) (sizeLimit * 90 ) / 100;
		limit_95_percent = (long) (sizeLimit * 95 ) / 100;
	}
	
	public void subDirSize(FileObject theFolder, long opnum, String titel ) throws FileSystemException, StorageLimitException {
		if ( theFolder.exists() && theFolder.isFolder()) {
			//System.out.println("-from- sub-dir-size-storeage");
			long sz = CommonsVfs.getSizeOfDirSilent(theFolder);
			Sp.pSpaceSubDir(theFolder, sz, opnum, titel);
			currentUsage = currentUsage - sz;
			checkSizeAfterSub();
		}
	}
	
	public void addDirSize(FileObject theFolder, long opnum ) throws FileSystemException, StorageLimitException {
		if ( theFolder.exists() && theFolder.isFolder() ) {
			//System.out.println("-from- addDirSize-storeage");
			long sz = CommonsVfs.getSizeOfDirSilent(theFolder);
			currentUsage = currentUsage + sz;
			Sp.pSpaceAddDir(theFolder, sz, opnum);
			checkSizeAfterAdd();
		}
		
	}	
	public void calculateCurrentUsage () throws FileSystemException {
		//System.out.println("-from- calculateCurrentUsage - s"+getTypeText());
		currentUsage = CommonsVfs.getSizeOfDirSilent(this.home);
		
	}
	
	private void setCurrentUsage ( long currentUsage ) {
		this.currentUsage = currentUsage;
	}
	
	public void subFile ( FileObject datei, long opnum, String titel ) throws FileSystemException, StorageLimitException {
		if ( datei.exists() && datei.isFile() ) {
			long sz = CommonsVfs.getSizeOfFileSilent(datei);
			currentUsage = currentUsage - sz;
			Sp.pSpaceSubFile(datei, sz, opnum, titel);
			checkSizeAfterSub();
		}		
	}
	
	public void addFile ( FileObject datei, long opnum ) throws FileSystemException, StorageLimitException {
		if ( datei.exists() && datei.isFile() ) {
			long sz = CommonsVfs.getSizeOfFileSilent(datei);
			currentUsage = currentUsage + sz;
			Sp.pSpaceAddFile(datei, sz, opnum);
			checkSizeAfterAdd();
		}		
	}	
	
	public void addFilesize ( FileObject datei, long newSize, long opnum ) throws StorageLimitException, FileSystemException {
		currentUsage = currentUsage + newSize;
		Sp.pSpaceAddFile(datei, newSize,opnum);
		checkSizeAfterAdd();
	}
	private String getTitel() {
		return typeToText(type);
	}
	

	
	private String getErrorMessage ( int percent, long limit ) {
		return (sitzung.lang().getScWarn1()+
				getTitel()+
				sitzung.lang().getScWarn2()+
				percent+
				sitzung.lang().getScWarn3()+
				limit+
				sitzung.lang().getScWarn4()+
				sizeLimit+
				sitzung.lang().getScWarn5()+
				homeDir);		

	}
	
	private void checkSizeAfterAdd ( ) throws StorageLimitException, FileSystemException {
		if ( type == TYPE_MEMORY ) {
			if ( currentUsage > sizeLimit ) {
				//System.out.println("wants calculate: memo");
				calculateCurrentUsage();
				if ( currentUsage > sizeLimit )
					throw new StorageLimitException ( 
							sitzung.lang().getScErr1()+
							sizeLimit+
							sitzung.lang().getScErr2()+
							homeDir+
							sitzung.lang().getScErr3()+
							getTitel());
			}

		} else {
			/* if ( currentUsage > limit_70_percent && !reached_70_percent) {
				reached_70_percent = true;
				throw new StorageLimitException ( getErrorMessage(70,limit_70_percent));
			} else */
			
			if ( currentUsage > limit_80_percent && !reached_80_percent) {
				//System.out.println("wants calculate: 80percent");
				calculateCurrentUsage();
				if ( currentUsage > limit_80_percent && !reached_80_percent) {
					reached_80_percent = true;
					iMessageBox.sendNewMessage(new StorageMessage(getErrorMessage(80,limit_80_percent)));					
				}
				
			} else if ( currentUsage > limit_90_percent && !reached_90_percent) {
				//System.out.println("wants calculate: 90percent");
				calculateCurrentUsage();
				if ( currentUsage > limit_90_percent && !reached_90_percent) {
					reached_90_percent = true;
					iMessageBox.sendNewMessage(new StorageMessage(getErrorMessage(90,limit_90_percent)));
				}
			} else if ( currentUsage > limit_95_percent && !reached_95_percent) {
				//System.out.println("wants calculate: 95percent");
				calculateCurrentUsage();
				if ( currentUsage > limit_95_percent && !reached_95_percent) {
					reached_95_percent = true;
					iMessageBox.sendNewMessage(new StorageMessage(getErrorMessage(95,limit_95_percent)));
				}
			}  else if ( currentUsage > sizeLimit ) {
				//System.out.println("wants calculate: 100percent");
				calculateCurrentUsage();
				if ( currentUsage > sizeLimit ) {
					throw new StorageLimitException ( 
							sitzung.lang().getScErr1()+
							sizeLimit+
							sitzung.lang().getScErr2()+
							homeDir+
							sitzung.lang().getScErr3()+
							getTitel());
				}
			}
		}

	}
	
	private void checkSizeAfterSub ( ) throws StorageLimitException, FileSystemException {
		if ( currentUsage < limit_80_percent ) {
			//calculateCurrentUsage();
			if ( currentUsage < limit_80_percent ) {
				reached_80_percent = false;
				reached_90_percent = false;
				reached_95_percent = false;			
			}		
		} else if ( currentUsage>= limit_80_percent && currentUsage < limit_90_percent ) {
			//calculateCurrentUsage();
			if ( currentUsage>= limit_80_percent && currentUsage < limit_90_percent ) {
				reached_80_percent = true;			
				reached_90_percent = false;	
				reached_95_percent = false;
			}
		} else if ( currentUsage>= limit_90_percent && currentUsage < limit_95_percent ) {
			//calculateCurrentUsage();
			if ( currentUsage>= limit_90_percent && currentUsage < limit_95_percent ) {
				reached_80_percent = true;			
				reached_90_percent = true;	
				reached_95_percent = false;
			}
		} else if ( currentUsage >= limit_95_percent ) {
			//calculateCurrentUsage();
			if ( currentUsage >= limit_95_percent ) {
				reached_80_percent = true;			
				reached_90_percent = true;
				reached_95_percent = true;			
			}			
		}

		checkSizeAfterAdd ( );

	}	
	public void requestStatus() {
		DecimalFormat df = new DecimalFormat  ( ",##0" );
		
			
		long kb = currentUsage/1024;
		long sizeLimit_kb = sizeLimit/1024;
		long free = sizeLimit - currentUsage;
		long free_kb = free / 1024;

		long free_percent = (free*100)/sizeLimit;
		long usage_percent = (currentUsage*100)/sizeLimit;
		
		
		
		iMessageBox.sendNewMessage(new StorageMessage(
				// sitzung.lang().getScStorageLimit()+
						"-- "+getTitel()+" --"+"\n   "+
						
						sitzung.lang().getScFree()+" "+
						df.format(free_percent) +"% = "+
						df.format(free) +" Bytes = "+
						df.format(free_kb) +" Kb \n   "+	
						
						sitzung.lang().getScCurrentUsage()+" "+
						df.format(usage_percent) +"% = "+
						df.format(currentUsage) +" Bytes = "+
						df.format(kb) +" Kb\n   "+
						
						sitzung.lang().getScLimit()+" "+
						df.format(sizeLimit) +" Bytes = "+
						df.format(sizeLimit_kb) + " Kb \n   "+
						sitzung.lang().getScLocation()+" "+
						homeDir));

	}

}
