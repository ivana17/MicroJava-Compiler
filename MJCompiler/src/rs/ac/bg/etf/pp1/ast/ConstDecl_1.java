// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl_1 extends ConstVars {

    private ConstVars ConstVars;
    private SingleConst SingleConst;

    public ConstDecl_1 (ConstVars ConstVars, SingleConst SingleConst) {
        this.ConstVars=ConstVars;
        if(ConstVars!=null) ConstVars.setParent(this);
        this.SingleConst=SingleConst;
        if(SingleConst!=null) SingleConst.setParent(this);
    }

    public ConstVars getConstVars() {
        return ConstVars;
    }

    public void setConstVars(ConstVars ConstVars) {
        this.ConstVars=ConstVars;
    }

    public SingleConst getSingleConst() {
        return SingleConst;
    }

    public void setSingleConst(SingleConst SingleConst) {
        this.SingleConst=SingleConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstVars!=null) ConstVars.accept(visitor);
        if(SingleConst!=null) SingleConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstVars!=null) ConstVars.traverseTopDown(visitor);
        if(SingleConst!=null) SingleConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstVars!=null) ConstVars.traverseBottomUp(visitor);
        if(SingleConst!=null) SingleConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl_1(\n");

        if(ConstVars!=null)
            buffer.append(ConstVars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleConst!=null)
            buffer.append(SingleConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl_1]");
        return buffer.toString();
    }
}
