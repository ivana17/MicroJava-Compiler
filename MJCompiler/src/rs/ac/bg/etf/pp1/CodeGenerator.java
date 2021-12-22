package rs.ac.bg.etf.pp1;

import java.util.*;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPC;
	private Obj program = null;
	private Obj currMethod = null;
	
	enum AddOp { ADD, SUB };
	enum MulOp { MUL, DIV, REM };
	enum RelOp { GT, EQ, LT, GE, LE, NE };

	private Stack<AddOp> AddOpStack= new Stack<>();
	private Stack<MulOp> MulOpStack= new Stack<>();
	private Stack<RelOp> RelOpStack= new Stack<>();

	private Stack<LinkedList<Integer>> thenAddrStack = new Stack<>();
	private Stack<LinkedList<Integer>> elseAddrStack = new Stack<>();
	private Stack<LinkedList<Integer>> andAddrStack = new Stack<>();
	
	private Stack<LinkedList<Integer>> thenRelopStack = new Stack<>();
	private Stack<LinkedList<Integer>> elseRelopStack = new Stack<>();
	private Stack<LinkedList<Integer>> andRelopStack = new Stack<>();
	
	private int skipElseJmp = -1; // jmp at the and of ifBody, if else exists
	
	Logger log = Logger.getLogger(getClass());

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}

	public CodeGenerator(Obj program) {
		this.program = program;
	}

	public int getMainPc() {
		return mainPC;
	}
	
	
	//helpFunction
	public Obj getVarConstObj(String objName) {
		Obj obj = Tab.find(objName);
		if (obj == Tab.noObj) {
			obj = null;
			Iterator<Obj> i = null;

			if (currMethod != null) {
				Collection<Obj> localCollection = currMethod.getLocalSymbols();
				i = localCollection.iterator();		
				while (i.hasNext()) {
					obj = i.next();
					if (obj.getName().equals(objName)) 
						return obj;
				}
			}
			
			Collection<Obj> globalCollection = program.getLocalSymbols();
			i = globalCollection.iterator();			
			while (i.hasNext()) {
				obj = i.next();
				if (obj.getKind() != Obj.Meth && obj.getName().equals(objName)) 
					return obj;
			}
			
		} else 
			return obj;
		
		return null;
	}
	
	/*
	public Obj getMethObj(String methName) {
		Obj obj = Tab.find(methName);
		if (obj == Tab.noObj) {
			obj = null;
			Collection<Obj> coll = program.getLocalSymbols();
			Iterator<Obj> i = coll.iterator();			
			while (i.hasNext()) {
				obj = i.next();
				if (obj.getKind() == Obj.Meth && obj.getName().equals(methName)) {
					return obj;
				}
			}
		} else {
			return obj;
		}
		return null;
	}
	*/

	private int getRelopCode(RelOp relop) {
		switch(relop) {
		case GT: return Code.gt; 
		case EQ: return Code.eq;
		case LT: return Code.lt; 
		case GE: return Code.ge; 
		case LE: return Code.le;
		case NE: return Code.ne;
		default: return -1;
		}
	}
	
	//Method
	public void visit(MethodDecl methodDecl) {
		currMethod = null;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(MethodReturnType methodTypeName) {
		methodTypeName.obj.setAdr(Code.pc);
		currMethod = methodTypeName.obj; //getMethObj(methodTypeName.getMethName());

		Code.put(Code.enter);
		Code.put(methodTypeName.obj.getLevel());
		Code.put(methodTypeName.obj.getLocalSymbols().size());
	}

	public void visit(MethodVoidType methodTypeName) {
		if (methodTypeName.getMethName().equalsIgnoreCase("main"))
			mainPC = Code.pc;
		methodTypeName.obj.setAdr(Code.pc);
		currMethod = methodTypeName.obj; //getMethObj(methodTypeName.getMethName());

		Code.put(Code.enter);
		Code.put(methodTypeName.obj.getLevel());
		Code.put(methodTypeName.obj.getLocalSymbols().size());
	}

	
	//Factor
	public void visit(FactorNumConst factorNumConst) {
		Code.loadConst(factorNumConst.getValue());
	}

	public void visit(FactorCharConst factorCharConst) {
		Code.loadConst(factorCharConst.getValue());
	}

	public void visit(FactorBoolConst factorBoolConst) {
		Code.loadConst(factorBoolConst.getValue() ? 1 : 0);
	}

	public void visit(FactorNewExpr factorNew) {
		Code.put(Code.newarray);
        if (factorNew.getType().struct == Tab.charType) {
			Code.put(0); 
        } else { 
			Code.put(1);
        }
	}
	

	//Designator
	public void visit(Designator_1 designator) {
		SyntaxNode parent = designator.getParent();
		if(parent.getClass() == FactorDesignator.class)	
			Code.load(designator.obj);
	}
	//DesignArr
	public void visit(Designator_0 designator) {
		SyntaxNode parent = designator.getParent();
		Obj o = getVarConstObj(designator.getName());	
		if (o != null) {
			Code.load(o);			 // stack: ... indx
			Code.put(Code.dup_x1);	 // stack: ... indx addr
			Code.put(Code.pop);		 // stack: ... addr indx addr
									 // stack: ... addr indx
		}	
		if(parent.getClass() == FactorDesignator.class)
			Code.load(designator.obj);	 // stack: ... addr[indx]
	}

	
	//Statement
	public void visit(StmtPrint stmtPrint) {
		if (stmtPrint.getExpr().struct == Tab.charType) {
			Code.loadConst(1);
			Code.put(Code.bprint);
		} else {
			Code.loadConst(4);
			Code.put(Code.print);
		}
	}

	public void visit(StmtPrintNum stmtPrint) {
		if (stmtPrint.getExpr().struct == Tab.charType) {
			Code.loadConst(stmtPrint.getWidth());
			Code.put(Code.bprint);
		} else {
			Code.loadConst(stmtPrint.getWidth());
			Code.put(Code.print);
		}
	}

	public void visit(StmtRead stmtRead) {
        if (stmtRead.getDesignator().obj.getType() == Tab.charType)
            Code.put(Code.bread);
        else
            Code.put(Code.read);
        
		Code.store(stmtRead.getDesignator().obj);
	}

	//DesignatorStatement
	public void visit(Assignment assignment) {
		Code.store(assignment.getDesignator().obj);
	}
	
	public void visit(Increment Increment) {
		if(Increment.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(Increment.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.add);
		Code.store(Increment.getDesignator().obj);		
	}
	
	@Override
	public void visit(Decrement Decrement) {
		if(Decrement.getDesignator().obj.getKind() == Obj.Elem) {
			Code.put(Code.dup2);
		}
		Code.load(Decrement.getDesignator().obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(Decrement.getDesignator().obj);		
	}

	
	//Addop&Mulop
	public void visit(Addop addop) {
		AddOpStack.push(AddOp.ADD);
	}
	
	public void visit(Subop subop) {
		AddOpStack.push(AddOp.SUB);
	}
	
	public void visit(Mulop mulop) {
		MulOpStack.push(MulOp.MUL);
	}
	
	public void visit(Divop divop) {
		MulOpStack.push(MulOp.DIV);
	}
	
	public void visit(Modop modop) {
		MulOpStack.push(MulOp.REM);
	}
	
	
	//Expr&Term&Factor
	public void visit(Term term) {
		SyntaxNode parent = term.getParent();
		if (parent.getClass() == NegExpr.class)
			Code.put(Code.neg);
	}
	
	public void visit(MoreTerm moreTerm) {
		AddOp addop = AddOpStack.pop();
		if (addop != null) {			
			switch (addop) {
				case ADD: 
					Code.put(Code.add);
					break;
				case SUB: 
					Code.put(Code.sub);
					break;
			}
		}
	}
	
	public void visit(MoreFactor moreFactor) {
		MulOp mulop = MulOpStack.pop();
		if (mulop != null) {						
			switch (mulop) {
				case MUL: 
					Code.put(Code.mul);
					break;
				case DIV:
					Code.put(Code.div);
					break;
				case REM: 
					Code.put(Code.rem);
					break;
			}
		}		
	}
	
	
	//Relop
	public void visit(RelopGT relopGT) { 
		RelOpStack.push(RelOp.GT); 		
	}
	
	public void visit(RelopEQ relopEQ) { 
		RelOpStack.push(RelOp.EQ);	
	}
	
	public void visit(RelopLT relopLT) { 
		RelOpStack.push(RelOp.LT);	
	}
	
	public void visit(RelopGE relopGE) { 
		RelOpStack.push(RelOp.GE);
	}
	
	public void visit(RelopLE relopLE) { 
		RelOpStack.push(RelOp.LE);		
	}
	
    public void visit(RelopNE relopNE) { 
    	RelOpStack.push(RelOp.NE);
    }
    
    
    //If
	public void visit(IfWord ifWord) { // if start
		thenAddrStack.add(new LinkedList<Integer>());
		elseAddrStack.add(new LinkedList<Integer>());
		andAddrStack.add(new LinkedList<Integer>());
		
		thenRelopStack.add(new LinkedList<Integer>());
		elseRelopStack.add(new LinkedList<Integer>());
		andRelopStack.add(new LinkedList<Integer>());
	}
	
    public void visit(StmtIf stmtIf) { // if end
    	
    	if(!thenAddrStack.empty()) thenAddrStack.pop();
		if(!elseAddrStack.empty()) elseAddrStack.pop();
		if(!andAddrStack.empty())  andAddrStack.pop();
		
		if(!thenRelopStack.empty()) thenRelopStack.pop();
		if(!elseRelopStack.empty()) elseRelopStack.pop();
		if(!andRelopStack.empty())  andRelopStack.pop();
		
    }
    
	//IfElse
	public void visit(StmtIfElse stmtIfElse) { // else end
		//resolves unconditional jump at the end of IfBody
		Code.fixup(skipElseJmp);
		
		if(!thenAddrStack.empty()) thenAddrStack.pop();
		if(!elseAddrStack.empty()) elseAddrStack.pop();
		if(!andAddrStack.empty())  andAddrStack.pop();
		
		if(!thenRelopStack.empty()) thenRelopStack.pop();
		if(!elseRelopStack.empty()) elseRelopStack.pop();
		if(!andRelopStack.empty())  andRelopStack.pop();
		
	}	
	

	public void visit(IfBody ifBody) { // ifBody end, else start
		
		//if ELSE exists - put unconditional jump at the end of ifBody
		if (ifBody.getParent().getClass() == StmtIfElse.class) {
			
			skipElseJmp = Code.pc + 1; 
			Code.putJump(0);
		}
		
		if(!andAddrStack.peek().isEmpty()) andAddrStack.peek().forEach(Code::fixup);
		if(!elseAddrStack.peek().isEmpty()) elseAddrStack.peek().forEach(Code::fixup);
		
		andAddrStack.peek().clear();
		andRelopStack.peek().clear();			//relopStack
		elseAddrStack.peek().clear();
		elseRelopStack.peek().clear();			//relopStack
		
	}
	
    //Cond
	public void visit(Cond cond) { // "(" condition ")" // cond end, if start
		
		// if true jump to THEN, if not proceed
		if(!thenAddrStack.peek().isEmpty() && !thenRelopStack.peek().isEmpty()) {

			thenAddrStack.peek().forEach(Code::fixup);
			
			thenAddrStack.peek().clear();
			thenRelopStack.peek().clear();			//relopStack
		}
		
	}
	
    //Condition
	public void visit(ConditionOr condOr) { // condition OR term
		report_info("ConditionOr pc = " + Code.pc, condOr);

	}
	
	public void visit(Orr orr) {

		// ( [... ||]  CondFact && CondFact && ... && CondFact && CondFact  || ... )     // CondTermAnd
		if(!andAddrStack.peek().isEmpty() && !andRelopStack.peek().isEmpty()) { 
			
			//last CondFact in ANDexpr jumps to THEN if true 
			int oldPC = Code.pc;
			
			Code.pc = andAddrStack.peek().getLast() - 1;
			Code.put(Code.jcc + andRelopStack.peek().getLast());
			
			thenAddrStack.peek().add(andAddrStack.peek().removeLast());
			thenRelopStack.peek().add(andRelopStack.peek().removeLast());			//relopStack
			
			Code.pc = oldPC;
			
			//all others jump to next ORcond if false
			andAddrStack.peek().forEach(Code::fixup);
			andAddrStack.peek().clear();
			andRelopStack.peek().clear();			//relopStack
			
		}
		
		// ( [... ||] CondFact || ... )
		int oldPC = Code.pc;
		while(!elseAddrStack.peek().isEmpty() && !elseRelopStack.peek().isEmpty()) {
			Code.pc = elseAddrStack.peek().getLast() - 1;
			Code.put(Code.jcc + elseRelopStack.peek().getLast());
			
			thenAddrStack.peek().add(elseAddrStack.peek().removeLast());
			thenRelopStack.peek().add(elseRelopStack.peek().removeLast());			//relopStack
		}
		Code.pc = oldPC;
		
	}
    
	public void visit(ConditionTerm condTerm) {} // term
	
	//CondTerm																			
	public void visit(CondTermAnd condTermAnd) { // term AND fact
		report_info("CondTermAnd pc = " + Code.pc, condTermAnd);
		
		//moves unfixed addresses from elseStack to andAddrStack 
		while(!elseAddrStack.peek().isEmpty()) andAddrStack.peek().add(elseAddrStack.peek().removeFirst());
		while(!elseRelopStack.peek().isEmpty()) andRelopStack.peek().add(elseRelopStack.peek().removeFirst());		//relopStack
		
	}
    
	public void visit(CondTermFact condFact) {} // fact
	
	//CondFact
	public void visit(CondFactRelop condFactRelop) { // expr relop expr
		report_info("CondFactRelop pc = " + Code.pc, condFactRelop);
		
		int relop = getRelopCode(RelOpStack.pop());
		elseAddrStack.peek().add(Code.pc + 1);
		elseRelopStack.peek().add(relop);		//relopStack
		Code.putFalseJump(relop, 0); 	//if x inverse[relop] 1 -> jump
		
	}
	
	public void visit(CondFactExpr expr) { // expr
		report_info("CondFactExpr pc = " + Code.pc, expr);
			
		Code.loadConst(1);
		elseAddrStack.peek().add(Code.pc + 1);
		elseRelopStack.peek().add(Code.eq);		//relopStack
		Code.putFalseJump(Code.eq, 0); 	//if x inverse[EQ] 1 -> jump
		
	}

	
	
}








