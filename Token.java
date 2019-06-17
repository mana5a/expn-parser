package parser;

import java.util.*;

public class Token {
	
	public TokenType type;
	public String val;
		
	public enum TokenType
	{
		LBRACE,
		RBRACE,
		AND,
		OR,
		NOT,
		LITERAL
	}
	
	public Token(TokenType type, String val)
	{
		this.type=type;
		this.val=val;
	}
	
	public HashMap<Character,TokenType> buildMap()
	{
		HashMap<Character,TokenType> dict= new HashMap<Character, TokenType>();
		dict.put('(',TokenType.LBRACE);
		dict.put(')',TokenType.RBRACE);
		dict.put('&',TokenType.AND);
		dict.put('!',TokenType.NOT);
		dict.put('|',TokenType.OR);
		return dict;
	}
	
	
	public List<Token> tokenise(String ip)
	{
		List<Token> expr= new ArrayList<Token>();
		int i=0;
		HashMap<Character,TokenType> dict= buildMap();
		while(i<ip.length())
		{
			if(dict.containsKey(ip.charAt(i)))
			{
				val=Character.toString(ip.charAt(i));
				type=dict.get(ip.charAt(i));
				Token t= new Token(type,val);
				expr.add(t);
				i++;
			}
			else
			{
				String str=""+ip.charAt(i);
				while(dict.get(ip.charAt(i+1))!=null)
				{
					i++;
					str+=ip.charAt(i);
				}
				val=str;
				type=TokenType.LITERAL;
				Token t=new Token(type,val);
				expr.add(t);
			}
		}
		return expr;
	}
	
	public List<Token> infixToPrefix(List<Token> tokens)
	{
		List<Token> prefix= new ArrayList<Token>();
		Stack<Token> stack= new Stack<Token>();
		
		HashMap<Character,Integer> prec= new HashMap<Character,Integer>();
		prec.put('!',3);
		prec.put('&',2);
		prec.put('|',1);
		
		Token lbr=new Token(TokenType.LBRACE,"(");
		Token rbr=new Token(TokenType.RBRACE,")");
		tokens.add(0,lbr);
		tokens.add(tokens.size()-1,rbr);
		
		for(int i=0;i<tokens.size();i++)
		{
			if(tokens.get(i).type==TokenType.LITERAL)
			{
				prefix.add(tokens.get(i));
			}
			else if(tokens.get(i).type==TokenType.LBRACE)
			{
				stack.push(tokens.get(i));
			}
			else if(tokens.get(i).type==TokenType.RBRACE)
			{
				while(stack.peek().type!=TokenType.LBRACE)
				{
					prefix.add(stack.pop());
				}
				stack.pop();
			}
			else
			{
				Token top=stack.peek();
				if(top.type==TokenType.AND || top.type==TokenType.OR || top.type==TokenType.NOT)
				{
					while(prec.get(tokens.get(i).val)<prec.get(top.val))
					{
						prefix.add(stack.pop());
					}
					stack.pop();
				}
			}
				
		}
		return prefix;
	}
	
	
	public static void main(String args[])
	{
		
	}
}
