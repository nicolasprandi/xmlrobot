/* Copyright (c) 2015-2023 Nicolas Prandstaetter. All rights reserved. */package at.nicolas.x.int_interpreter;

import java.util.ArrayList;

import at.nicolas.x.pub.Vcar;

public interface IFunEvalHandler {

	public Vcar executeFunction(String functionname, ArrayList<Vcar> vcar_list) throws Exception;
		
}