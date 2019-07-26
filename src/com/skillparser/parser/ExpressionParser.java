package com.skillparser.parser;

import java.util.ArrayList;
import java.util.Stack;

public class ExpressionParser{
	public static class Node{
		Node left, right;
		String value;
		public Node(String value) {
			this.value = value;
			left = right = null;
		}
		public boolean isLeaf() {
			return left == null || right == null;
		}
		public String toString() {
			if(isLeaf())
				return value;
			else
				return value + "("+left+" "+right+")";
		}
		public double operation(double l, double r){
			switch(value) {
			case "+":
				return l+r;
			case "-":
				return l-r;
			case "*":
				return l*r;
			case "/":
				return l/r;
			default: 
				return 1;
			}
		}
		
	}
	public static class Tokenizer{
		public static final  char operators[] = {'+','-','*','/','(',')'};
		public static boolean isOperator(char c) {
			for(int i=0;i<operators.length;++i)
				if(c == operators[i])
					return true;
			return false;
		}
		
		public static ArrayList<String> tokenize(String expression){
			String expr = expression.replaceAll(" ", "");
			ArrayList<String> result = new ArrayList<String>();
			String current = new String();
			char c;
			for(int i=0;i<expr.length();++i) {
				c = expr.charAt(i);
				if(c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'){
					current+=c;
				} else if(isOperator(c)){
					if(current.length()!=0) {
						result.add(current);
						current = new String();	
					}
					result.add(String.valueOf(c));		
				}
			}
			if(current.length() !=0)
				result.add(current);
			return result;
		}
	}
	public static class Converter {
		
		public static final String operators[] = {"+","-","*","/"};
		public static boolean isOperator(String token) {
			for(int i=0;i<operators.length;++i)
				if(token.equals(operators[i]))
					return true;
			return false;
		}
		
		public static int precedence(String op) {
			if(op.equals("+") || op.equals("-"))
				return 1;
			if(op.equals("*") || op.equals("/"))
				return 2;
			return -1;
		}
		
		
		public static ArrayList<String> postfix(ArrayList<String> tokens){
			ArrayList<String> result = new ArrayList<String>();
			Stack<String> opStack = new Stack<String>();
			for(String token : tokens){
				if(!Tokenizer.isOperator(token.charAt(0))) {
					result.add(token);
				} else {
					if(token.equals("("))
						opStack.push(token);
					else if(token.equals(")")) {
						while(!opStack.peek().equals("(")) {
							result.add(opStack.pop());
						}
						opStack.pop();
					} else if (isOperator(token)) {
						while( !opStack.isEmpty() && !opStack.peek().equals("(") && precedence(token) <= precedence(opStack.peek())) {
							result.add(opStack.pop());
						}
						opStack.push(token);
					}
				}
			}
			while(!opStack.isEmpty())
				result.add(opStack.pop());
			return result;
		}
	}
	public static class TreeConverter {
		public static Node convert(ArrayList<String> postfix){
			Stack<Node> tokenStack = new Stack<Node>();
			for(String token : postfix){
				if(Converter.isOperator(token)) {
					Node operator = new Node(token);
					operator.left = tokenStack.pop();
					operator.right = tokenStack.pop();
					tokenStack.add(operator);
				} else {
					tokenStack.add(new Node(token));
				}
			}
			return tokenStack.pop();
			
		}
	}
}