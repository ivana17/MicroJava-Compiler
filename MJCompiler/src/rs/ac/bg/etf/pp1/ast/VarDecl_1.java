// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class VarDecl_1 extends Vars {

    private Vars Vars;
    private SingleVar SingleVar;

    public VarDecl_1 (Vars Vars, SingleVar SingleVar) {
        this.Vars=Vars;
        if(Vars!=null) Vars.setParent(this);
        this.SingleVar=SingleVar;
        if(SingleVar!=null) SingleVar.setParent(this);
    }

    public Vars getVars() {
        return Vars;
    }

    public void setVars(Vars Vars) {
        this.Vars=Vars;
    }

    public SingleVar getSingleVar() {
        return SingleVar;
    }

    public void setSingleVar(SingleVar SingleVar) {
        this.SingleVar=SingleVar;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Vars!=null) Vars.accept(visitor);
        if(SingleVar!=null) SingleVar.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Vars!=null) Vars.traverseTopDown(visitor);
        if(SingleVar!=null) SingleVar.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Vars!=null) Vars.traverseBottomUp(visitor);
        if(SingleVar!=null) SingleVar.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl_1(\n");

        if(Vars!=null)
            buffer.append(Vars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleVar!=null)
            buffer.append(SingleVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDecl_1]");
        return buffer.toString();
    }
}
