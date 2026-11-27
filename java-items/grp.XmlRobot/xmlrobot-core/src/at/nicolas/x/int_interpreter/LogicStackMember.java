package at.nicolas.x.int_interpreter;

import org.jdom2.Element;

import at.nicolas.x.int_coop.IInterLoop;
import at.nicolas.x.int_specs.LogConnectorLogic;

public class LogicStackMember {
	
	public static final int TYPE_TRY = 0;
	public static final int TYPE_LOOP = 1;
	public static final int TYPE_CALL_LOCAL = 2;
	public static final int TYPE_WHEN = 3;
	public static final int TYPE_CHOOSE = 4;
	public static final int TYPE_INCLUDE = 5;	
	private int type;
	private Element baseElement;
	private LogConnectorLogic lcl;
	
	private String loop_type = null;
	private IInterLoop interLoop = null;
	
	private String jobid = null;
	private int hashCode = -1;

	public String getTypeName() {
		switch ( type ) {
			case TYPE_TRY: return "try_catch";
			case TYPE_LOOP: return "loop";
			case TYPE_CALL_LOCAL: return "call";
			case TYPE_WHEN: return "when";
			case TYPE_CHOOSE: return "choose";
			case TYPE_INCLUDE: return "include";
		}
		return "Unclassified tastk-type";
	}
	LogicStackMember(int type, Element baseElement, LogConnectorLogic lcl) {
		this.baseElement = baseElement;
		this.lcl = lcl;
		this.type = type;
	}
	
	public void setLoopOptions(String loop_type, IInterLoop interLoop) {
		this.loop_type = loop_type;
		this.interLoop = interLoop;

	}
	
	public void setCallInnerOptions(String jobid) {
		this.jobid = jobid;

	}	
	
	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
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

	public int getType() {
		return type;
	}

	public String getLoop_type() {
		return loop_type;
	}

	public IInterLoop getInterLoop() {
		return interLoop;
	}

	public String getJobid() {
		return jobid;
	}

	public int getHashCode() {
		return hashCode;
	}


}
