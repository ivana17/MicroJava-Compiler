package rs.ac.bg.etf.pp1;
 
import java.util.ArrayList;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {
 
	boolean errorDetected = false;
	boolean mainFound = false;
	boolean returnFound = false;
	boolean isVoid = false;
	int actualParams = 0;
	Obj currentMethod = null;
	int nVars;
	
	private Obj program = null; 
	Scope programScope = null;
	Object valueObject = null;
	
	public static Struct boolType = Tab.insert(Obj.Type, "bool", new Struct(Struct.Bool)).getType();
	
	private ArrayList<MyVar> myVars = new ArrayList<MyVar>();	// VarDecl
	private ArrayList<MyVar> myConsts = new ArrayList<MyVar>(); // ConstDecl

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public boolean isErrDetected(){
    	return errorDetected;
    }

	public int getnVars() {
		return nVars;
	}

	public Obj getProgram() {
		return program;
	}
	
	
	//Program
	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getName(), Tab.noType);
		this.program = progName.obj; 
		Tab.openScope();
		programScope = Tab.currentScope();
	}

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
		if(!mainFound) {
			report_error("Semanticka greska - funkcija main nije pronadjena", null);
		}
	}
	
	
	//VarDecl
	public void visit(VarDecl varDecl) { 
    	Struct varType = varDecl.getType().struct;
    	String typeName = varDecl.getType().getTypeName();
    	for (int i = 0; i < myVars.size(); i++) {
    		String varName = myVars.get(i).getName();
	    	if (myVars.get(i).isArray()) {
	    		report_info("Deklarisan niz " + varName + " (tip: " + typeName + ")", varDecl); 
	    		Obj obj = Tab.insert(Obj.Var, varName, new Struct(Struct.Array, varType));
	    	} else {
	    		report_info("Deklarisana promenljiva " + varName + " (tip: " + typeName + ")", varDecl);
	    		Obj obj = Tab.insert(Obj.Var, varName, varType);
	    	}    	
    	}
    	myVars.clear();
    } 
	
    public void visit(VarArray varArray) { 
    	String varName = varArray.getName();
    	Obj obj = Tab.currentScope.findSymbol(varName);
		if (obj == null) {
			for (int i = 0; i < myVars.size(); i++) {
				if(myVars.get(i).getName().equals(varName)) {
					report_error("Semanticka greska - promenljiva sa imenom " + varName + " vec postoji", varArray);
					return;
				}
			}
			myVars.add(new MyVar(varName, MyVar.VarType.ARRAY));			
		} else { 
			if(obj.getKind() == Obj.Type)
				report_error("Semanticka greska - promenljiva koristi rezervisanu rec " + varName + " za ime", varArray);
			else
				report_error("Semanticka greska - promenljiva sa imenom " + varName + " vec postoji", varArray);
		}
    }

    public void visit(Variable varStd) {
    	String varName = varStd.getName();
    	Obj obj = Tab.currentScope.findSymbol(varName);
		if (obj == null) {
			for (int i = 0; i < myVars.size(); i++) {
				if(myVars.get(i).getName().equals(varName)) {
					report_error("Semanticka greska - promenljiva sa imenom " + varName + " vec postoji", varStd);
					return;
				}
			}
			myVars.add(new MyVar(varName, MyVar.VarType.STD_VAR));			
		} else {
			if(obj.getKind() == Obj.Type)
				report_error("Semanticka greska - promenljiva koristi rezervisanu rec " + varName + " za ime", varStd);
			else 
				report_error("Semanticka greska - promenljiva sa imenom " + varName + " vec postoji", varStd);
		}
	}
    
    //Type
    public void visit(Type type) {
		String typeName = type.getTypeName();
		Obj obj = Tab.find(typeName);
		if (obj == Tab.noObj) {
			report_error("Semanticka greska - nije pronadjen tip " + typeName + " u tabeli simbola! ", type);
			type.struct = Tab.noType;
		} else {
			if (obj.getKind() == Obj.Type) {
				type.struct = obj.getType();
			} else {
				report_error("Semanticka greska - ime " + typeName + " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
		}

	}
    
    //ConstDecl
    public void visit(ConstDecl constDecl) {
    	Struct constType = constDecl.getType().struct;
    	String typeName = constDecl.getType().getTypeName();
		for (int i = 0; i < myConsts.size(); i++) {
			String constName = myConsts.get(i).getName();
			Struct kind = myConsts.get(i).getKind();
			if(kind != constType) {
				report_error("Semanticka greska - tip konstante " + constName + " i njena vrednost nekompatibilni", constDecl);
				continue;
			} else {
	    		Obj obj = Tab.insert(Obj.Con, constName, constType);	
	    		if(kind == Tab.intType)
	    			obj.setAdr((Integer)myConsts.get(i).getValue());
	    		else if(kind == Tab.charType)
	    			obj.setAdr((Character)myConsts.get(i).getValue());
	    		else 
	    			obj.setAdr((Boolean)myConsts.get(i).getValue() ? 1 : 0);	
				report_info("Deklarisana konstanta " + constName + " (tip: " + typeName + ", vrednost: " + myConsts.get(i).getValue() + ")", constDecl);
			}
		}
		myConsts.clear();
	}
    
    public void visit(SingleConst singleConst) {
		String constName = singleConst.getName();
		Obj obj = Tab.find(constName); 
		if (obj == Tab.noObj) {
			for (int i = 0; i < myConsts.size(); i++) {
				if(myConsts.get(i).getName().equals(constName)) {
					report_error("Semanticka greska - konstanta sa imenom " + constName + " vec postoji", singleConst);
					return;
				}
			}
			myConsts.add(new MyVar(constName, singleConst.getValue().struct, valueObject));
		} else {
			if(obj.getKind() == Obj.Type)
				report_error("Semanticka greska - konstanta koristi rezervisanu rec " + constName + " za ime", singleConst);
			else
				report_error("Semanticka greska - konstanta sa imenom " + constName + " vec postoji", singleConst);
		}
	}

    //Value
    public void visit(NumConst value){
    	value.struct = Tab.intType;
    	valueObject = value.getVal();
    }
    
    public void visit(CharConst value){
    	value.struct = Tab.charType;
    	valueObject = value.getVal();
    }
    
    public void visit(BoolConst value){
    	value.struct = boolType;
    	valueObject = value.getVal();
    }
    
    
    //MethodDecl
  	public void visit(MethodDecl methodDecl) {
      	if(!isVoid && !returnFound && !currentMethod.getName().equals("main")){
  			report_error("Semanticka greska - funkcija " + currentMethod.getName() + " nema return iskaz", null);
      	}
        
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

      	returnFound = false;
      	isVoid = false;
  		currentMethod = null;
  	}
  	
  	public void visit(MethodReturnType methodTypeName) {
  		String methodName = methodTypeName.getMethName();
  		Type methodType = methodTypeName.getType();
  		if(methodName.equals("main")) {
  			mainFound = true;
  			report_error("Semanticka greska - funkcija main ne sme imati povratnu vrednost", methodTypeName);
  		} else
  			report_info("Obradjuje se funkcija " + methodName + " (povratni tip: " + methodType.getTypeName() + ")", methodTypeName);
  		currentMethod = Tab.insert(Obj.Meth, methodName, methodType.struct);
  		methodTypeName.obj = currentMethod;
  		Tab.openScope();
  	}
  	
  	public void visit(MethodVoidType methodVoidName) {
  		String methodName = methodVoidName.getMethName();
  		if(methodName.equals("main")) mainFound = true;
  		currentMethod = Tab.insert(Obj.Meth, methodName, Tab.noType);
  		methodVoidName.obj = currentMethod;
  		Tab.openScope(); 
  		isVoid = true;
  		report_info("Obradjuje se funkcija " + methodName + " (povratni tip: void)", methodVoidName);
  	}
  	
	
  	//FormalParam
  	public void visit(FormalParDeclArr formalParDeclArr) {
  		if(currentMethod.getName().equals("main")) 
  			report_error("Semanticka greska - main funkcija nema formalne parametre", formalParDeclArr);
  		String name = formalParDeclArr.getName();
		if (Tab.currentScope.findSymbol(name) == null){
			Obj obj = Tab.insert(Obj.Var, name, new Struct(Struct.Array, formalParDeclArr.getType().struct));
			obj.setFpPos(currentMethod.getLevel());
			currentMethod.setLevel(currentMethod.getLevel() + 1);
		} else {
			report_error("Semanticka greska - ime " + name + " vec postoji", formalParDeclArr);
		}
	}
	
    public void visit(FormalParDecl formalParDecl) {
		if(currentMethod.getName().equals("main")) 
			report_error("Semanticka greska - main funkcija nema formalne parametre", formalParDecl);
    	String name = formalParDecl.getName();
		if (Tab.currentScope.findSymbol(name) == null) {
	    	Obj obj = Tab.insert(Obj.Var, name, formalParDecl.getType().struct);
	    	obj.setFpPos(currentMethod.getLevel());
	    	currentMethod.setLevel(currentMethod.getLevel() + 1);
		} else {
			report_error("Semanticka greska - ime " + name + " vec postoji", formalParDecl);
		}
    } 
  	
  	
	//Factor
	public void visit(FactorNumConst cnst){
    	cnst.struct = Tab.intType;
    }
    
    public void visit(FactorCharConst cnst){
    	cnst.struct = Tab.charType;
    }
    
    public void visit(FactorBoolConst cnst){
    	cnst.struct = boolType;
    }
    
    public void visit(FactorDesignator designator){
    	designator.struct = designator.getDesignator().obj.getType();
    }
    
    public void visit(FactorExpr expr){
    	expr.struct = expr.getExpr().struct;
    }
    
    public void visit(FactorNewExpr factorNewExpr){
    	if(factorNewExpr.getExpr().struct != Tab.intType)
    		report_error("Semanticka greska - Dozvoljen samo int tip za alokaciju niza", factorNewExpr);
    	factorNewExpr.struct = new Struct(Struct.Array, factorNewExpr.getType().struct);
    } 
	
    
	//Term
	public void visit(Term term) {
		term.struct = term.getFactor().struct;
	}

	
	//Expr
	public void visit(PosExpr posExpr) {
		posExpr.struct = posExpr.getTerm().struct;
	}

	public void visit(NegExpr negExpr) {
		negExpr.struct = negExpr.getTerm().struct;
	}

	//Mul_Add_List
	public void visit(MoreTerm moreTerm) {
		if (moreTerm.getTerm().struct != Tab.intType) {
			report_error("Semanticka greska - Samo tip int dozvoljen u operacijama + i -", moreTerm);
		}
	}

	public void visit(MoreFactor moreFactor) {
		if (moreFactor.getFactor().struct != Tab.intType) {
			report_error("Semanticka greska - Samo tip int dozvoljen u operacijama *, / i %", moreFactor);
		}
	}
	
	
	//Designator
    public void visit(Designator_0 designator_0) {
    	String designName = designator_0.getName(); 
		Obj obj = Tab.find(designName);
		if (obj == Tab.noObj) { 
			report_error("Semanticka greska - ime " + designName + " nije deklarisano", designator_0);
		}
		if (designator_0.getExpr().struct != Tab.intType) {
			report_error("Semanticka greska - invalidan pristup elementu niza " + designName, designator_0);
		}
		if (obj.getType().getKind() != Struct.Array){
			report_error("Semanticka greska - " + designName + " se ocekuje da bude niz", designator_0);
		}
		designator_0.obj = new Obj(Obj.Elem, obj.getName(), obj.getType().getElemType());
	} 
    
    public void visit(Designator_1 designator_1) { 
    	String designName = designator_1.getName();
		Obj obj = Tab.find(designName);
		if (obj == Tab.noObj) { 
			report_error("Semanticka greska - ime " + designName + " nije deklarisano", designator_1);
		}		 
		designator_1.obj = obj;
	}
    
    
    //Matched_Statement
    public void visit(StmtRead stmtRead) {
		Obj obj = stmtRead.getDesignator().obj;
		if (obj.getKind() == Obj.Var) {
			if (!obj.getType().equals(Tab.intType) && !obj.getType().equals(Tab.charType) && !obj.getType().equals(boolType)) {
				report_error("Semanticka greska - argument read naredbe nije tipa(int/char/bool)", stmtRead);
			}
		} else {
			report_error("Semanticka greska - argument read naredbe nije promenljiva", stmtRead);
		}
	}
	
	public void visit(StmtPrint stmtPrint) {
		Struct exprType = stmtPrint.getExpr().struct;
		if(exprType != Tab.intType && exprType != Tab.charType && exprType != boolType) 
			report_error ("Semanticka greska - operand instrukcije PRINT mora biti char, int ili bool tipa", stmtPrint );
	}
	
	public void visit(StmtPrintNum stmtPrint) {
		Struct exprType = stmtPrint.getExpr().struct;
		if(exprType != Tab.intType && exprType != Tab.charType && exprType != boolType) 
			report_error ("Semanticka greska - operand instrukcije PRINT mora biti char, int ili bool tipa", stmtPrint );
	}
	
  	public void visit(StmtReturnExpr returnExpr) { 
		String methodName = currentMethod.getName();
		Struct currMethType = currentMethod.getType();
		if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
			if(isVoid)
				report_error("Semanticka greska - funkcija void ne moze imati povratnu vrednost (" + methodName + ")", null); 
			else 
				report_error("Semanticka greska - tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + methodName, null);
		}
		returnFound = true;
	} 

	public void visit(StmtReturn returnNoExpr) {
		if(!isVoid) 
			report_error("Semanticka greska - povratni tip funkcije NIJE void", null);
		returnFound = true;
	}
	
	//DesignatorStmt
	public void visit(Assignment assignment) {		
		Struct leftSide = assignment.getDesignator().obj.getType();
		Struct rightSide = assignment.getExpr().struct;	
		if (!rightSide.assignableTo(leftSide)) 
			report_error("Semanticka greska - nekompatibilni tipovi za dodelu vrednosti", assignment);
	}
  
	public void visit(Increment increment) { 
		if (increment.getDesignator().obj.getType() != Tab.intType && increment.getDesignator().obj.getKind() != Obj.Elem) 
			report_error("Semanticka greska - operand za inkrement moze biti jedino int", increment);
	}
	
	public void visit(Decrement decrement) { 
		if (decrement.getDesignator().obj.getType() != Tab.intType && decrement.getDesignator().obj.getKind() != Obj.Elem) 
			report_error("Semanticka greska - operand za dekrement moze biti jedino int", decrement);
	}
	
	public void visit(FuncCall funcCall) {
		String funcName = funcCall.getDesignator().obj.getName();
		int formalParams = Tab.find(funcName).getLevel();
		report_info("Poziva se funkcija "+ funcName, funcCall);
		if(actualParams != formalParams)
			report_error("Semanticka greska - funkcija " + funcName + " zahteva " + formalParams + " argumenta", funcCall);
		actualParams = 0;		
	} 
	
	//ActualParam
	public void visit(ActualParams params) {
		actualParams++;
	}
	
	public void visit(ActualParam param) {
		actualParams++;
	}
	
	
	//CondFact
	public void visit(CondFactExpr condFact) {
		if(condFact.getExpr().struct.getKind() != Struct.Bool) {
			report_error("Semanticka greska - izraz u if naredbi mora biti bool tipa", condFact);
		}
	}
	
}













