package at.nicolas.x.int_specs;

import at.nicolas.x.pub.IGuiController;
import at.nicolas.x.pub.UserInfo;

public class GuiControllerDesktop implements IGuiController {
	


	@Override
	public boolean chooseFileOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean chooseDirOn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean seeFileFunctions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean seeDirFunctions() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSocalledWeb() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean chooseSaveFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getChooseLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChooseTip() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getHasFileExtension() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getFileExtension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileProposedFilename() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileCurVal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasFileCurVal() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mayOperateFiles() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean mayOperateDirs() {
		// TODO Auto-generated method stub
		return true;
	}



}
