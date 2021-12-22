// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class PosExpr extends Expr {

    private Term Term;
    private MoreTermList MoreTermList;

    public PosExpr (Term Term, MoreTermList MoreTermList) {
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.MoreTermList=MoreTermList;
        if(MoreTermList!=null) MoreTermList.setParent(this);
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public MoreTermList getMoreTermList() {
        return MoreTermList;
    }

    public void setMoreTermList(MoreTermList MoreTermList) {
        this.MoreTermList=MoreTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Term!=null) Term.accept(visitor);
        if(MoreTermList!=null) MoreTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(MoreTermList!=null) MoreTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(MoreTermList!=null) MoreTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PosExpr(\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MoreTermList!=null)
            buffer.append(MoreTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PosExpr]");
        return buffer.toString();
    }
}
