package at.nicolas.x.int_interpreter;

import org.jdom2.Element;

import at.nicolas.x.int_coop.IInterLoop;
import at.nicolas.x.int_specs.LogConnectorLogic;

public class StackStep {

	private Element stepToRun = null; // to run
	private Element baseElement = null; 
	private LogConnectorLogic lcl = null;
	private IInterLoop interLoop = null;
	private LogicStackMember lsm = null;
	
	StackStep (  Element stepToRun,
				 Element baseElement,
				 LogConnectorLogic lcl,
				 IInterLoop interLoop,
				 LogicStackMember lsm) {
		this.stepToRun = stepToRun;
		this.baseElement = baseElement;
		this.interLoop = interLoop;
		this.lcl = lcl;
		this.lsm = lsm;

		
	}
	

	public Element getStepToRun() {
		return stepToRun;
	}

	public void setStepToRun(Element stepToRun) {
		this.stepToRun = stepToRun;
	}

	

	public IInterLoop getInterLoop() {
		return interLoop;
	}

	public void setInterLoop(IInterLoop interLoop) {
		this.interLoop = interLoop;
	}


	public Element getBaseElement() {
		return baseElement;
	}


	public void setBaseElement(Element baseElement) {
		this.baseElement = baseElement;
	}


	public LogConnectorLogic getLcl() {
		return lcl;
	}


	public void setLcl(LogConnectorLogic lcl) {
		this.lcl = lcl;
	}


	public LogicStackMember getLsm() {
		return lsm;
	}

}
