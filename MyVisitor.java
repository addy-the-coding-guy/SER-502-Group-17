package my.buildcompiler.compiler;

import java.util.*;

import my.buildcompiler.parser.ADPBaseVisitor;
import my.buildcompiler.parser.ADPParser.AssignmentstatementContext;
import my.buildcompiler.parser.ADPParser.ConditionalexpressionContext;
import my.buildcompiler.parser.ADPParser.ConditionaloperatorContext;
import my.buildcompiler.parser.ADPParser.ConditionalstatementContext;
import my.buildcompiler.parser.ADPParser.DeclarationstatementContext;
import my.buildcompiler.parser.ADPParser.ExprContext;
import my.buildcompiler.parser.ADPParser.FunctioncallContext;
import my.buildcompiler.parser.ADPParser.FundeclarationContext;
import my.buildcompiler.parser.ADPParser.LiteralContext;
import my.buildcompiler.parser.ADPParser.LiterallistContext;
import my.buildcompiler.parser.ADPParser.LoopingstatementContext;
import my.buildcompiler.parser.ADPParser.ParamlistContext;
import my.buildcompiler.parser.ADPParser.PrintstatementContext;
import my.buildcompiler.parser.ADPParser.ProgramContext;
import my.buildcompiler.parser.ADPParser.ReturnstatementContext;
import my.buildcompiler.parser.ADPParser.StatementsContext;
import my.buildcompiler.parser.ADPParser.VardeclarationContext;
import my.buildcompiler.parser.ADPParser.VarnameContext;

public class MyVisitor extends ADPBaseVisitor<String> {
	public static List<String> list = new ArrayList<String>();
	public static List<String> temp = new ArrayList<String>();

	@Override
	public String visitProgram(ProgramContext ctx) {
		list.add("PSTART" + "\n");
		visitChildren(ctx);
		list.add("PEND");
		return "";
	}

	public String visitVardeclaration(VardeclarationContext ctx) {
		if (ctx.getChildCount() > 3)
			list.add("EQL" + " " + ctx.getChild(1).getText() + ","
					+ ctx.getChild(3).getText() + "\n");
		return "";
	}

	@Override
	public String visitDeclarationstatement(DeclarationstatementContext ctx) {
		//System.out.println(ctx.getChild(3).getChildCount());
		
		if (ctx.getChild(3).getText().length() > 5) {
			if (ctx.getChild(3).getText().substring(0,6).equals("funcal")) {
				list.add("EQL" + " " + ctx.getChild(1).getText() + ",");	
			}
			visitChildren(ctx);
		} 
		
		if(ctx.getChild(3).getChildCount() == 1){
			list.add("EQL" + " " + ctx.getChild(1).getText() + ","
					+ ctx.getChild(3).getText() + "\n");
		}
		//if(ctx.getChild(3).getChildCount() > 1) 
		/*else{
			//list.add("EQL" + " " + ctx.getChild(1).getText() + ",");
			visitChildren(ctx);	
		}*/
		return "";
	}

	@Override
	public String visitConditionalstatement(ConditionalstatementContext ctx) {

		if (ctx.getChild(0).getText().equals("if")) {
			list.add("IFT" + " " + ctx.getChild(2).getText() + "\n");
			visitChildren(ctx);
			list.add("ENDIFT" + "\n");

		} else {
			list.add("ELS" + '\n');
			visitChildren(ctx);
			list.add("ENDELS" + "\n");
		}

		return "";
	}

	@Override
	public String visitPrintstatement(PrintstatementContext ctx) {
		list.add("PRINT" + " " + ctx.getChild(1).getText() + "\n");
		return null;
	}

	@Override
	public String visitExpr(ExprContext ctx) {

		if (ctx.getChildCount() > 2) {
			if (ctx.getChild(1).getText().equals("-")) {
				list.add("SUB" + " " + ctx.getChild(0).getText() + ","
						+ ctx.getChild(2).getText() + "\n");
			}
			if (ctx.getChild(1).getText().equals("+")) {
				list.add("ADD" + " " + ctx.getChild(0).getText() + ","
						+ ctx.getChild(2).getText() + "\n");
			}
			if (ctx.getChild(1).getText().equals("*")) {
				list.add("MUL" + " " + ctx.getChild(0).getText() + ","
						+ ctx.getChild(2).getText() + "\n");
			}
			if (ctx.getChild(1).getText().equals("/")) {
				list.add("DIV" + " " + ctx.getChild(0).getText() + ","
						+ ctx.getChild(2).getText() + "\n");
			}
			if (ctx.getChild(1).getText().equals("%")) {
				list.add("REM" + " " + ctx.getChild(0).getText() + ","
						+ ctx.getChild(2).getText() + "\n");
			}
			
		}
		
		return "";
	}

	public MyVisitor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String visitLoopingstatement(LoopingstatementContext ctx) {
		// System.out.println(ctx.getChild(2));
		list.add("loop" + " " + ctx.getChild(2).getText() + "\n");
		visitChildren(ctx);
		list.add("ELOP" + "\n");
		return null;
	}

	@Override
	public String visitAssignmentstatement(AssignmentstatementContext ctx) {
		visitChildren(ctx);
		return null;

	}

	@Override
	public String visitStatements(StatementsContext ctx) {
		visitChildren(ctx);
		return "";
	}

	@Override
	public String visitFundeclaration(FundeclarationContext ctx) {
		list.add(ctx.getChild(1).getText() + " " + ctx.getChild(2).getText()
				+ ctx.getChild(3).getText() );
		visitChildren(ctx);
		list.add("ENDFUN" + "\n");
		return null;
	}

	@Override
	public String visitParamlist(ParamlistContext ctx) {
		int count = ctx.getChildCount();
		if (count > 2) {
			list.add(ctx.getChild(1).getText() + ",");
		} else {
			list.add(ctx.getChild(1).getText() + "\n");
		}
		visitChildren(ctx);
		return "";
	}

	@Override
	public String visitReturnstatement(ReturnstatementContext ctx) {
		String temp = visitChildren(ctx);
		list.add("RET" + " " + ctx.getChild(1).getText() + "\n");
		return "";
	}

	@Override
	public String visitFunctioncall(FunctioncallContext ctx) {
		if(ctx.getChildCount() == 7){
		list.add("CALL" + " " + ctx.getChild(1).getText()
				+ ctx.getChild(2).getText() + ctx.getChild(3).getText()
				+ ctx.getChild(4).getText() + ctx.getChild(5).getText() + "\n");
		}
		else
		{
			list.add("CALL" + " " + ctx.getChild(1).getText()
					+ ctx.getChild(2).getText() + ctx.getChild(3).getText()
					+ ctx.getChild(4).getText() + "\n");
		}
		return "";
	}

	@Override
	protected String aggregateResult(String aggregate, String nextResult) {
		if (aggregate == null && nextResult == null)
			return "";
		if (aggregate == null)
			return nextResult;
		if (nextResult == null)
			return aggregate;
		return aggregate + "\n" + nextResult;
	}
}
