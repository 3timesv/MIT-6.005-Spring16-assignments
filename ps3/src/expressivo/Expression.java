/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.List;
import java.util.Stack;

import org.antlr.v4.gui.Trees;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import expressivo.parser.ExpressionLexer;
import expressivo.parser.ExpressionListener;
import expressivo.parser.ExpressionParser;

/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //   
    //   Expression = Number(double: value) + Variable(name: String)
    //                                      + Plus(left: Expression, right: Expression)
    //                                      + Multiply(left: Expression, right: Expression)

    /** @return an Expression consisting of number. */
    public static Expression make(double value) {
        return new Number(value);
    }

    /** @return an Expression consisting of just a single variable. */
    public static Expression make(String name) {
        return new Variable(name);
    }

    /** @return an Expression consisting of addition (+) operation. */
    public static Expression makePlus(Expression left, Expression right) {
        return new Plus(left, right);
    }

    /** @return an Expression consisting of multiplication (*) operation. */
    public static Expression makeMultiply(Expression left, Expression right) {
        return new Multiply(left, right);
    }

    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        try {
            // create a stream of characters from the input string
            CharStream stream = new ANTLRInputStream(input);

            // Make a lexer 
            ExpressionLexer lexer = new ExpressionLexer(stream);
            lexer.reportErrorsAsExceptions();

            // throw exception if lexer encounters an error
            TokenStream tokens = new CommonTokenStream(lexer);

            // Make a parser whose input comes from the token stream produced by the lexer
            ExpressionParser parser = new ExpressionParser(tokens);
            parser.reportErrorsAsExceptions();

            // Generate the parse tree using the starter rule.
            // root is the starter rule for this grammer.
            ParseTree tree = parser.root();

            MakeExpression exprMaker = new MakeExpression();
            new ParseTreeWalker().walk(exprMaker, tree);

            return exprMaker.getExpression();
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid expression");
        }
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
}

/** Make a Expression value from a parse tree. */
class MakeExpression implements  ExpressionListener {
    private Stack<Expression> stack = new Stack<>();
    // Invariant: Stack contains the Expression value of each parse subtree that has been fully-walked so far,
    //      but whose parent has not yet been exited by the walk.
    //      The stack is ordered by recency of visit.
    //
    //      The stack is empty iff the walk has not yet begun.
    //
    //      Whenever a node is exited by the walk, the Expression value of its children are on top of the stack,
    //          in order with the last child on the top.
    //      To preserve the invariant, the children Expression values are popped off the stack and
    //          combined with appropriate Expression producer, and pushed back on the stack representing 
    //          the entire subtree rooted at the node.
    //
    //      At the end of the walk, after all subtrees have been walked and the root has been exited, 
    //          only the entire tree satisfies the invariant, so the top of the stack is the
    //          Expression value of the entire parse tree.

    /**
     * Returns the Expression constructed by this listener object.
     * Requires that this listener has completely walked over the parse tree using ParseTreeWalker.
     * @return the Expression for the parse tree that was walked.
     */
    public Expression getExpression() {
        return stack.get(0);
    }

    @Override
    public void exitSum(ExpressionParser.SumContext context) {
        if (context.getChildCount() > 1) {
            Expression right = stack.pop();
            Expression left = stack.pop();

            stack.push(Expression.makePlus(left, right));
        }
    }

    @Override
    public void exitMul(ExpressionParser.MulContext context) {
        if (context.getChildCount() > 1) {
            Expression right = stack.pop();
            Expression left = stack.pop();

            stack.push(Expression.makeMultiply(left, right));
        }
    }

    @Override
    public void exitPrimitive(ExpressionParser.PrimitiveContext context) {
        if (context.NUMBER() != null) {
            // matched the NUMBER alternative
            double value = Double.valueOf(context.NUMBER().getText());
            Expression number = new Number(value);

            stack.push(number);
        } else if (context.VARIABLE() != null) {
            // matched the VARIABLE alternative
            String name = context.VARIABLE().getText();
            Expression variable = new Variable(name);

            stack.push(variable);
        } else {
            // matched the '(' expression ')' alternative
        }
    }

    // don't need the below methods, so leave them unimplemented
    @Override public void enterRoot(ExpressionParser.RootContext context) { }
    @Override public void exitRoot(ExpressionParser.RootContext context) { }
    @Override public void enterSum(ExpressionParser.SumContext context) { }
    @Override public void enterMul(ExpressionParser.MulContext context) { }
    @Override public void enterPrimitive(ExpressionParser.PrimitiveContext context) { }
    @Override public void visitTerminal(TerminalNode terminal) { }
    @Override public void enterEveryRule(ParserRuleContext context) { };
    @Override public void exitEveryRule(ParserRuleContext context) { };
    @Override public void visitErrorNode(ErrorNode node) { };
}
