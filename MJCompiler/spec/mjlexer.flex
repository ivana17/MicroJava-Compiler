
package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

// %function next_token
// %type java_cup.runtime.Symbol

// %class MJLexer
%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

<YYINITIAL> {

" " 		{ }
"\b" 		{ }
"\t" 		{ }
"\r\n" 		{ }
"\f" 		{ }

"program"   { return new_symbol(sym.PROGRAM, yytext());}
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read"      { return new_symbol(sym.READ, yytext()); }
"const"		{ return new_symbol(sym.CONST, yytext()); }
"void" 		{ return new_symbol(sym.VOID, yytext()); }
"new" 		{ return new_symbol(sym.NEW, yytext()); }

"&&" 		{ return new_symbol(sym.AND, yytext()); }
"||" 		{ return new_symbol(sym.OR, yytext()); }
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-" 		{ return new_symbol(sym.MINUS, yytext()); }
"*" 		{ return new_symbol(sym.MUL, yytext()); }
"/" 		{ return new_symbol(sym.DIV, yytext()); }
"%" 		{ return new_symbol(sym.MOD, yytext()); }
"{" 		{ return new_symbol(sym.LBRACE, yytext()); }
"}"			{ return new_symbol(sym.RBRACE, yytext()); }
"[" 		{ return new_symbol(sym.LSQUARE, yytext()); }
"]"			{ return new_symbol(sym.RSQUARE, yytext()); }
"(" 		{ return new_symbol(sym.LPAREN, yytext()); }
")" 		{ return new_symbol(sym.RPAREN, yytext()); }
"=" 		{ return new_symbol(sym.ASSIGNMENT, yytext()); }
"++" 		{ return new_symbol(sym.INC, yytext()); }
"--" 		{ return new_symbol(sym.DEC, yytext()); }
";" 		{ return new_symbol(sym.SEMICOLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"?" 		{ return new_symbol(sym.QMARK, yytext()); }
":" 		{ return new_symbol(sym.COLON, yytext()); }

"if" 		{ return new_symbol(sym.IF, yytext()); }
"else" 		{ return new_symbol(sym.ELSE, yytext()); }
"for" 		{ return new_symbol(sym.FOR, yytext()); }
"break" 	{ return new_symbol(sym.BREAK, yytext()); }
"continue" 	{ return new_symbol(sym.CONTINUE, yytext()); }
"return" 	{ return new_symbol(sym.RETURN, yytext()); }

">" 		{ return new_symbol(sym.GREATER, yytext()); }
"==" 		{ return new_symbol(sym.EQUAL, yytext()); }
"<" 		{ return new_symbol(sym.LESSER, yytext()); }
">=" 		{ return new_symbol(sym.GREATER_EQUAL, yytext()); }
"<=" 		{ return new_symbol(sym.LESSER_EQUAL, yytext()); }
"!=" 		{ return new_symbol(sym.NOT_EQUAL, yytext()); }

"true" | "false"     { return new_symbol(sym.BOOL_CONST, new Boolean (yytext())); }
(0|([1-9]([0-9]*)))  { return new_symbol(sym.NUM_CONST, new Integer (yytext())); }
"'"."'" 		     { return new_symbol(sym.CHAR_CONST, new Character (yytext().charAt(1))); }
([a-z]|[A-Z])[a-zA-Z0-9_]* 	{ return new_symbol (sym.IDENT, yytext()); }

"//" 		{ yybegin(COMMENT); }

. 			{ System.err.println("Leksicka greska (" + yytext() + ") u liniji " + (yyline + 1) + " i koloni " + (yycolumn + 1)); }

}

<COMMENT> {
"\r\n"  	{ yybegin(YYINITIAL); }
. 			{ yybegin(COMMENT); }
}









