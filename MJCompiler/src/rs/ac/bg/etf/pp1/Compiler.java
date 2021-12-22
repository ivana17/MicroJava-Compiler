package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;

public interface Compiler {
	
	List<CompilerError> compile(String sourceFilePath, String outputFilePath);
	
	public static Logger logError = Logger.getLogger("error");
	public static Logger logInfo = Logger.getLogger("info");
	
	public static FileAppender fileAppender = new FileAppender();
	public static FileAppender fileAppenderError = new FileAppender();

	
	public static void main(String[] args) throws Exception {
		
		if (args.length < 2) {
			logInfo.error("Nema dovoljno argumenata! ( Ocekivani ulaz: <ulaz>.mj <izlaz>.obj )");
			return;
		}
		
		fileAppender.setLayout(new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN));
		fileAppender.setFile("test/info.out");
		fileAppender.activateOptions();
		logInfo.addAppender(fileAppender);

		fileAppenderError.setLayout(new PatternLayout(PatternLayout.DEFAULT_CONVERSION_PATTERN));
		fileAppenderError.setFile("test/error.err");
		fileAppenderError.activateOptions();
		logError.addAppender(fileAppenderError);
		
		File sourceFile = new File(args[0]);
		if (!sourceFile.exists()) {
			logInfo.error("Ulazni fajl [" + args[0] + "] ne postoji!");
			return;
		}
		
		logInfo.info("Kompajliranje ulaznog fajla " + sourceFile.getPath()); //getAbsolutePath()
		

		try {
			
			FileReader fileReader = new FileReader(sourceFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Yylex lexer = new Yylex(bufferedReader);			
			MJParser parser = new MJParser(lexer);
			
	        if (parser.isErrDetected()) {
				logError.info("Ulazni fajl [" + args[0] + "] ima sintaksnih gresaka!"); 
				return;
	        }
	        
	        Symbol symbol = parser.parse(); 
			Program program = (Program)(symbol.value); 

			logInfo.info("\n================SINTAKSNO STABLO====================\n");
			logInfo.info(program.toString(""));

			Tab.init(); 
			logInfo.info("\n================SEMANTICKA OBRADA====================\n");			

            SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer();
            program.traverseBottomUp(semanticAnalyzer);
            
            tsdump();
			
			if (semanticAnalyzer.isErrDetected()) {
	        	logError.error("Ulazni fajl [" + args[0] + "] ima semantickih gresaka!\nParsiranje nije uspesno zavrseno!");
			} else {
				
				logInfo.info("\n================GENERISANJE KODA====================\n");
				
	        	File objFile = new File(args[1]);
	        	if (objFile.exists()) {
	        		objFile.delete();
	        	}

//	        	CodeGenerator codeGenerator = new CodeGenerator();
	        	
//	        	CodeGenerator codeGenerator = new CodeGenerator(semanticAnalyzer.getProgram());
//	        	program.traverseBottomUp(codeGenerator);
//                
//                Code.dataSize = semanticAnalyzer.getnVars();
//                Code.mainPc = codeGenerator.getMainPc();
//	        	Code.write(new FileOutputStream(objFile));
				logInfo.info("Parsiranje uspesno zavrseno!");
	        } 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void tsdump() {
		MySymbolTableVisitor stv = new MySymbolTableVisitor();
        Tab.dump(stv);
	}
}
