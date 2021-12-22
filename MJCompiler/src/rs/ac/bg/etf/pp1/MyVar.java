package rs.ac.bg.etf.pp1;

import rs.etf.pp1.symboltable.concepts.Struct;

public class MyVar {
	
	public enum VarType { STD_VAR, ARRAY };
//	public enum Modifiability { CONST, VARIABLE };
	
	private String name;
	private VarType varType;
//	private Modifiability modifiability;
	private Object value;
	private Struct kind;
	
	public MyVar(String name, Struct kind, Object value) {		//Const
		this.name = name;
		this.value = value;
		this.kind = kind;
	}	
	
	public MyVar(String name, VarType varType) { 				//Var
		this.name = name;
		this.varType = varType;
		this.value = null;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Object getValue() {
		return value;
	}

	public Struct getKind() {
		return kind;
	}
	
	public boolean isArray() {
		return (varType == VarType.ARRAY);
	}
	
}
