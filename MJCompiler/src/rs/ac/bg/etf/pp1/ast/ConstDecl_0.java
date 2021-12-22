// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl_0 extends ConstVars {

    private SingleConst SingleConst;

    public ConstDecl_0 (SingleConst SingleConst) {
        this.SingleConst=SingleConst;
        if(SingleConst!=null) SingleConst.setParent(this);
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
        if(SingleConst!=null) SingleConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SingleConst!=null) SingleConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SingleConst!=null) SingleConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl_0(\n");

        if(SingleConst!=null)
            buffer.append(SingleConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl_0]");
        return buffer.toString();
    }
}
