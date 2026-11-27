package at.nicolas.x.pub;

public interface IGuiController {
	
	
	public boolean mayOperateFiles ( );
	public boolean mayOperateDirs ( );

	public boolean chooseFileOn ( );
	public boolean chooseDirOn ( );
	public boolean seeFileFunctions();
	public boolean seeDirFunctions();
	public boolean isSocalledWeb();
	
	public boolean chooseSaveFile();
	public String getChooseLabel();
	public String getChooseTip();
	public boolean getHasFileExtension();
	public String getFileExtension();
	public String getFileProposedFilename();
	public String getFileCurVal();
	public boolean hasFileCurVal();
	public UserInfo getUserInfo();
	
}
