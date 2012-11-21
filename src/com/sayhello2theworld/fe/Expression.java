package com.sayhello2theworld.fe;

import java.util.Vector;

class Item
{
	public String data;
	public int priority;
	
	public Item (String d, int p)
	{
		data = d;
		priority = p;
	}
	
	public String toString ()
	{
		return data+" "+priority;
	}
}

class ExpressionNode
{
	
	public static final int CONST = 0;
	public static final int X = 1;
	public static final int NONE = 2;
	public static final int PLUS = 3;
	public static final int MINUS = 4;
	public static final int MULTIPLY = 5;
	public static final int DIVIDE = 6;
	public static final int DIVIDE_INT = 7;
	public static final int MODULO = 8;
	public static final int ABS = 9;
	public static final int SIN = 10;
	public static final int COS = 11;
	public static final int LN = 12;
	public static final int LG = 13;
	public static final int LOG = 14;
	public static final int EXP = 15;
	public static final int POW = 16;
	public static final int FACT = 17;
	public static final int TAN = 18;
	public static final int ATAN = 19;
	public static final int ASIN = 20;
	public static final int ACOS = 21;
	public static final int SQRT = 22;
	public static final int SIGN = 23;
	
	private boolean evaluated = false;
	private float value;
	private int function = NONE; 
	private String data = "";
	private boolean func = false;
	
	private ExpressionNode left;
	private ExpressionNode right;
	
	public ExpressionNode (Vector<Item> items, int l, int r) throws Exception
	{
		if (l == r)
		{
			Item item = items.get(l);
			data = item.data;
			if (item.priority==0)
			{
				if (item.data.equalsIgnoreCase("x")) function = X;
				else 
				{
					function = CONST;
					if (item.data.equalsIgnoreCase("e")) value = (float)Math.E;
					else if (item.data.equalsIgnoreCase("pi")) value = (float)Math.PI;
					else value = Float.parseFloat(item.data);
				}
			}
			else System.out.println ("Error: priority "+item);
		}
		else
		if (l < r)
		{
			boolean oneArgument = false;
			int p = Integer.MAX_VALUE;
			int pos = -1;
			for (int i=l;i<=r;i++)
			{
				Item item = items.get(i);
				if (item.priority > 0 && p >= item.priority)
				{
					p = item.priority;
					pos = i;
				}
			}
			if (pos >= l)
			{
				Item item = items.get(pos);
				data = item.data;
				func = true;
				if (item.data.length()==1)
				{
					func = false;
					switch (item.data.charAt(0))
					{
						case '+':
							function = PLUS;
							if (l==pos) oneArgument = true;
							break;
							
						case '-':
							function = MINUS;
							if (l==pos) oneArgument = true;
							break;
							
						case '*':
							function = MULTIPLY;
							break;
							
						case '/':
							function = DIVIDE;
							break;
							
						case '%':
							function = MODULO;
							break;
							
						case '^':
							function = POW;
							break;
							
						case '!':
							function = FACT;
							break;
						
						default:
							throw new Exception("Error in expression, unknown function "+item);
					}
				}
				else
				if (item.data.equalsIgnoreCase("sin"))
				{
					function = SIN;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("cos"))
				{
					function = COS;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("abs"))
				{
					function = ABS;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("exp"))
				{
					function = EXP;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("ln"))
				{
					function = LN;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("lg"))
				{
					function = LG;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("tan"))
				{
					function = TAN;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("sqrt"))
				{
					function = SQRT;
					oneArgument = true;
				}
				else
				if (item.data.equalsIgnoreCase("sign"))
				{
					function = SIGN;
					oneArgument = true;
				}
				else throw new Exception ("Error in expression, uknown function "+item);
				
				if (oneArgument == false) left = new ExpressionNode(items, l, pos-1);
				if (function != FACT) right = new ExpressionNode(items, pos+1, r);
			}
			else System.out.println ("Expression error only priority 0 "+l+" "+r);
		}
		else System.out.println ("Expression error l>r: "+l+" "+r);
	}
	
	public int getFunction ()
	{
		return function;
	}
	
	public void print (int tabs)
	{
		if (right!=null) right.print(tabs+1);
		for (int i=0;i<tabs;i++) System.out.print("\t");
		System.out.println (data);
		if (left!=null) left.print(tabs+1);
	}
	
	public boolean isEvaluated ()
	{
		if (function == CONST) evaluated = true;
		if (evaluated == true) return evaluated;
		if (left==null && right == null) evaluated = true;
		if (left!=null) evaluated = left.isEvaluated();
		if (right!=null) evaluated = evaluated && right.isEvaluated();
		return evaluated;
	}
	
	public static float toFloat (double n)
	{
		float nf = 0;
		if (Double.isNaN(n)) nf = Float.NaN;
		else if (n == Double.POSITIVE_INFINITY) nf = Float.POSITIVE_INFINITY;
		else if (n == Double.NEGATIVE_INFINITY) nf = Float.NEGATIVE_INFINITY;
		else nf = (float)n;
		return nf;
	}
	
	public static double toDouble (float n)
	{
		double nd = 0;
		if (Float.isNaN(n)) nd = Double.NaN;
		else if (n == Float.POSITIVE_INFINITY) nd = Double.POSITIVE_INFINITY;
		else if (n == Float.NEGATIVE_INFINITY) nd = Double.NEGATIVE_INFINITY;
		else nd = n;
		return nd;
	}
	
	public float evaluate (float x)
	{
		float result = 0;
		switch (function)
		{
			case CONST:
				result = value;
				break;
			
			case X:
				result = x;
				break;
				
			case PLUS:
				{
					if (left!=null) result = left.evaluate(x);
					result = result+right.evaluate(x);
				}
				break;
				
			case MINUS:
				{
					if (left!=null) result = left.evaluate(x);
					result = result-right.evaluate(x);
				}
				break;
				
			case MULTIPLY:
				{
					result = left.evaluate(x)*right.evaluate(x);
				}
				break;
				
			case DIVIDE:
				{
					float b = right.evaluate(x);
					//if (b==0) throw new Exception ("Division by 0");
					result = left.evaluate(x)/b;
				}
				break;
				
			case MODULO:
				{
					float b = right.evaluate(x);
					//if (b==0) throw new Exception ("Division by 0");
					result = left.evaluate(x)%b;
				}
				break;
				
			case POW:
				{
					float b = right.evaluate(x);
					if (b == toFloat (Math.floor(toDouble(b))))
					{
						float a = left.evaluate(x);
						result = 1;
						for (int i=1; i<=b;i++) result = result * a;
					}
					else 
					{
						result = toFloat (Math.pow(toDouble (left.evaluate(x)), toDouble (b)));
					}
				}
				break;
			
			case SQRT:
				{
					result = toFloat (Math.sqrt(toDouble (right.evaluate(x))));
				}
				break;
			
			case ABS:
				{
					result = Math.abs(right.evaluate(x));
				}
				break;
			
			case SIGN:
				{
					result = Math.signum(right.evaluate(x));
				}
				break;
			
			case SIN:
				{
					result = toFloat (Math.sin(toDouble (right.evaluate(x))));
				}
				break;
				
			case COS:
				{
					result = toFloat (Math.cos(toDouble (right.evaluate(x))));
				}
				break;
				
			case LN:
				{
					result = toFloat (Math.log(toDouble (right.evaluate(x))));
				}
				break;
				
			case LG:
				{
					result = toFloat (Math.log10(toDouble (right.evaluate(x))));
				}
				break;
				
			case EXP:
				{
					result = toFloat (Math.exp(toDouble (right.evaluate(x))));
				}
				break;
				
			case FACT:
				{
					long r = 1;
					long l = (long)left.evaluate(x);
					if (l<0) result = Float.NaN;
					else
					{
						for (int i=1; i<=l;i++) r = r * i;
						result = r;
					}
				}
				break;
				
			case TAN:
				{
					result = toFloat (Math.tan(toDouble (right.evaluate(x))));
				}
				break;
		}
		if (function != CONST)
		{
			boolean leval = false;
			boolean reval = false;
			if (left==null || left.function == CONST)  leval = true;
			if (right==null || right.function != CONST) reval = false;
			if (leval && reval)
			{
				value = result;
				function = CONST;
			}
		}
		return result;
	}
	
	public String toString ()
	{
		StringBuffer e = new StringBuffer ();
		if (function != CONST && function != X && func == false) e.append("(");
		if (left!=null) e.append(left);
		e.append(data);
		if (func == true) e.append("(");
		if (right!=null) e.append(right);
		if (function != CONST && function != X) e.append(")");
		return e.toString();
	}
	
	public void clear ()
	{
		if (left!=null) left.clear();
		left = null;
		if (right!=null) right.clear();
		right = null;
	}
}

public class Expression 
{
	private String exp;
	private ExpressionNode e;
	private Vector<Item> items = new Vector<Item>();
	
	public Expression (String _exp) throws Exception
	{
		if (_exp == null) throw new Exception ("Expression is null");
		this.exp = _exp;
		StringBuffer buffer = new StringBuffer (10);
		int level = 0;
		boolean wasFunctionBefore = false;
		for (int i=0;i<exp.length();i++)
		{
			char v = exp.charAt(i);
			int p=0;
			// items
			if ("+-*/%^!()".indexOf(v)>=0)
			{
				if (buffer.length()>0)
				{
					char x=buffer.charAt(0);
					if ((buffer.length()==1 && (x=='x' || x=='X' || x=='e' || x == 'E')) || (x >= '0' && x<='9') || (buffer.toString().equalsIgnoreCase("pi"))) 
					{
						wasFunctionBefore = false;
						items.add(new Item (buffer.toString(), 0));
					}
					else
					{
						wasFunctionBefore = true;
						level = level + 10;
						items.add(new Item (buffer.toString(), level));
					}
					buffer.delete(0, buffer.length());
				}
			}
			else if (v!=' ') buffer.append(v);
			// priority
			switch (v)
			{
				case '+':
				case '-':
					p = 1;
					break;
				
				case '*':
				case '/':
				case '%':
					p = 2;
					break;
				
				case '^':
					p = 3;
					break;
					
				case '!':
					p = 4;
					break;
				
				case '(':
					if (!wasFunctionBefore) level = level + 10;
					break;
					
				case ')':
					level = level - 10;
					break;
					
				//case ' ':
				//	break;
					
				default:
					p = -1;
					break;
			}
			// semne
			if ("+-*/%^!".indexOf(v)>=0)
			{
				items.add(new Item (""+v,level+p));
			}
			wasFunctionBefore = false;
		}
		if (buffer.length()>0) items.add(new Item (buffer.toString(), 0));
		// for (int i=0;i<items.size();i++) System.out.println (i+": "+items.get(i));
		e = new ExpressionNode(items, 0, items.size()-1);
		items = null;
	}
	
	public void print ()
	{
		e.print(0);
	}
	
	public float evaluate (float x)
	{
		return e.evaluate(x);
	}
	
	public String toString ()
	{
		return e.toString();
	}
	
	public void clear ()
	{
		e.clear ();
		e = null;
	}
	
	public static void main (String[] args) throws Exception
	{
		Expression exp = new Expression ("2.3+3!*sin(cos(x)-2*3)*(2+4)");
		System.out.println (exp);
		System.out.println (exp.evaluate (2));
	}
}
