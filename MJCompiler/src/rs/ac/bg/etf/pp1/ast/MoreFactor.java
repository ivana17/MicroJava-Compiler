// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class MoreFactor extends MoreFactorList {

    private MoreFactorList MoreFactorList;
    private MulOpers MulOpers;
    private Factor Factor;

    public MoreFactor (MoreFactorList MoreFactorList, MulOpers MulOpers, Factor Factor) {
        this.MoreFactorList=MoreFactorList;
        if(MoreFactorList!=null) MoreFactorList.setParent(this);
        this.MulOpers=MulOpers;
        if(MulOpers!=null) MulOpers.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public MoreFactorList getMoreFactorList() {
        return MoreFactorList;
    }

    public void setMoreFactorList(MoreFactorList MoreFactorList) {
        this.MoreFactorList=MoreFactorList;
    }

    public MulOpers getMulOpers() {
        return MulOpers;
    }

    public void setMulOpers(MulOpers MulOpers) {
        this.MulOpers=MulOpers;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MoreFactorList!=null) MoreFactorList.accept(visitor);
        if(MulOpers!=null) MulOpers.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MoreFactorList!=null) MoreFactorList.traverseTopDown(visitor);
        if(MulOpers!=null) MulOpers.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MoreFactorList!=null) MoreFactorList.traverseBottomUp(visitor);
        if(MulOpers!=null) MulOpers.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MoreFactor(\n");

        if(MoreFactorList!=null)
            buffer.append(MoreFactorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MulOpers!=null)
            buffer.append(MulOpers.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MoreFactor]");
        return buffer.toString();
    }
}
