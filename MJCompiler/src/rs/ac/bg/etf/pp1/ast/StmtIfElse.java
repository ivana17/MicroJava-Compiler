// generated with ast extension for cup
// version 0.8
// 16/8/2021 1:54:16


package rs.ac.bg.etf.pp1.ast;

public class StmtIfElse extends Statement {

    private IfWord IfWord;
    private Cond Cond;
    private IfBody IfBody;
    private Statement Statement;

    public StmtIfElse (IfWord IfWord, Cond Cond, IfBody IfBody, Statement Statement) {
        this.IfWord=IfWord;
        if(IfWord!=null) IfWord.setParent(this);
        this.Cond=Cond;
        if(Cond!=null) Cond.setParent(this);
        this.IfBody=IfBody;
        if(IfBody!=null) IfBody.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public IfWord getIfWord() {
        return IfWord;
    }

    public void setIfWord(IfWord IfWord) {
        this.IfWord=IfWord;
    }

    public Cond getCond() {
        return Cond;
    }

    public void setCond(Cond Cond) {
        this.Cond=Cond;
    }

    public IfBody getIfBody() {
        return IfBody;
    }

    public void setIfBody(IfBody IfBody) {
        this.IfBody=IfBody;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfWord!=null) IfWord.accept(visitor);
        if(Cond!=null) Cond.accept(visitor);
        if(IfBody!=null) IfBody.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfWord!=null) IfWord.traverseTopDown(visitor);
        if(Cond!=null) Cond.traverseTopDown(visitor);
        if(IfBody!=null) IfBody.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfWord!=null) IfWord.traverseBottomUp(visitor);
        if(Cond!=null) Cond.traverseBottomUp(visitor);
        if(IfBody!=null) IfBody.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtIfElse(\n");

        if(IfWord!=null)
            buffer.append(IfWord.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Cond!=null)
            buffer.append(Cond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfBody!=null)
            buffer.append(IfBody.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtIfElse]");
        return buffer.toString();
    }
}
