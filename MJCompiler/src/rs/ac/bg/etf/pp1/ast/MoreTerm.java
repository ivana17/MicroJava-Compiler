// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class MoreTerm extends MoreTermList {

    private MoreTermList MoreTermList;
    private AddOpers AddOpers;
    private Term Term;

    public MoreTerm (MoreTermList MoreTermList, AddOpers AddOpers, Term Term) {
        this.MoreTermList=MoreTermList;
        if(MoreTermList!=null) MoreTermList.setParent(this);
        this.AddOpers=AddOpers;
        if(AddOpers!=null) AddOpers.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public MoreTermList getMoreTermList() {
        return MoreTermList;
    }

    public void setMoreTermList(MoreTermList MoreTermList) {
        this.MoreTermList=MoreTermList;
    }

    public AddOpers getAddOpers() {
        return AddOpers;
    }

    public void setAddOpers(AddOpers AddOpers) {
        this.AddOpers=AddOpers;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MoreTermList!=null) MoreTermList.accept(visitor);
        if(AddOpers!=null) AddOpers.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MoreTermList!=null) MoreTermList.traverseTopDown(visitor);
        if(AddOpers!=null) AddOpers.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MoreTermList!=null) MoreTermList.traverseBottomUp(visitor);
        if(AddOpers!=null) AddOpers.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MoreTerm(\n");

        if(MoreTermList!=null)
            buffer.append(MoreTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddOpers!=null)
            buffer.append(AddOpers.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MoreTerm]");
        return buffer.toString();
    }
}
