// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class Modop extends MulOpers {

    public Modop () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Modop(\n");

        buffer.append(tab);
        buffer.append(") [Modop]");
        return buffer.toString();
    }
}
