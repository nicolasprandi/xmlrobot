/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.term_tk;

import at.nicolas.x.WebVfs.TkTicket;
import at.nicolas.x.int_specs.Commons;

public class FitsRule {
	
	public static FitsRuleResult check (
			String value,
			String rule_attribute,
			TkTicket ticket,
			String varLabel) {
		FitsRuleResult result = new FitsRuleResult();
		if (value != null && rule_attribute != null ) {
			String val_after_rule = null;
			String applied_rules = null;	
			if ( Commons.hasFlag(rule_attribute , "filename")) {
				if ( ticket.tktype.equals("dir") || 
					 ticket.tktype.equals("file")  ||
					 ticket.isMsg ) {					
				}else {
					val_after_rule = applyRule(value, false); 					
					applied_rules = ticket.lang.getRule_filename();
				}
			} else if ( Commons.hasFlag(rule_attribute , "filename_with_blanks")) {
				if ( ticket.tktype.equals("dir") || 
					 ticket.tktype.equals("file")  ||
					 ticket.isMsg  ) {					
				}else {
					// apply rule filename_with_blank
					val_after_rule = applyRule(value, true); 
					applied_rules = ticket.lang.getRule_filename_blanks();
				}
			}
			
			if ( val_after_rule != null && !value.equals(val_after_rule)) {
				// rule would change
				result.changed = true;
				result.newValue = val_after_rule; 
				result.askMessage =ticket.lang.getRule_reformating()+" '"+varLabel+"'"+
						 "\n\n"+ticket.lang.getRule_old() +" "+value+
						   "\n"+ticket.lang.getRule_new() +"  "+val_after_rule
						+"\n\n"+ticket.lang.getRule_accept() ;
				result.askMessageHeader = ticket.lang.getRule_rule() +""+applied_rules+"/"+varLabel;
			}			
		}
		return result;
	}	
	
	public static FitsRuleResult check (
			String value,
			String rule_attribute,
			Tk_Tk tk,
			String varLabel) {
		FitsRuleResult result = new FitsRuleResult();
		if (value != null && rule_attribute != null ) {
			String val_after_rule = null;
			String applied_rules = null;	
			if ( Commons.hasFlag(rule_attribute , "filename")) {
				if ( tk instanceof Tk_TkDir || 
						tk instanceof Tk_TkFile ||
						tk instanceof Tk_TkMsg) {					
				}else {
					val_after_rule = applyRule(value, false); 					
					applied_rules = "Filename Compability";
				}
			} else if ( Commons.hasFlag(rule_attribute , "filename_with_blanks")) {
				if ( tk instanceof Tk_TkDir || 
						tk instanceof Tk_TkFile ||
						tk instanceof Tk_TkMsg) {					
				}else {
					// apply rule filename_with_blank
					val_after_rule = applyRule(value, true); 
					applied_rules = "Filename Compability with Blanks";
				}
			}
			
			if ( val_after_rule != null && !value.equals(val_after_rule)) {
				// rule would change
				result.changed = true;
				result.newValue = val_after_rule;
				result.askMessage ="Reformating nessessary for '"+varLabel+"'"+
						 "\n\nOld value was: "+value+
						   "\nNew value is:  "+val_after_rule
						+"\n\nDo you accept that ?";
				result.askMessageHeader = "Rule: "+applied_rules+"/"+varLabel;
			}			
		}
		return result;
	}
	/*
	 * \\,\\^\\&\\@\\{\\}\\$\\=\\!\\#\\%
	 */
	public static String applyRule ( String in, boolean with_blanks ) {
		String val_after_rule = null;
		if ( with_blanks )
			val_after_rule = in.replaceAll("([\\,\\^\\&\\@\\{\\}\\$\\=\\!\\#\\/\\\\\\?\\%\\*\\:\\|\\\"\\<\\>\\'])","-");
		else
			val_after_rule = in.replaceAll("([\\,\\^\\&\\@\\{\\}\\$\\=\\!\\#\\/\\\\\\?\\%\\*\\:\\|\\\"\\<\\>\\'\\ ])","-");
		
		while ( val_after_rule.indexOf("..") > -1 ) {
			val_after_rule = val_after_rule.replaceFirst("\\.", "-");
		}
		return val_after_rule;		
	}
}