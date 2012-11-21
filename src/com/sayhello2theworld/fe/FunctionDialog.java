package com.sayhello2theworld.fe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;
import android.text.InputType;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

public class FunctionDialog extends AlertDialog implements View.OnClickListener 
{
	private Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b0, bp, bx;
	private Button be, bpi, bplus, bminus, bmultip, bdiv, bmod, bpow, bexp, bln, blg, bsin, bcos, btan, bsign, bfact, bsqrt, babs;
	private Button bbo, bbc;
	private Button bbackspace, bleft, bright;
	private TableLayout buttons, buttons_func;
	private EditText function_edit;
	StringBuffer fs = new StringBuffer ();
	private FunctionExpress fexp;
	private Vibrator vibrator;
	
	public FunctionDialog (Context context, FunctionExpress _fexp, String sexp)
	{
		super (context);
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();  
		
		int width = display.getWidth();  
		int height = display.getHeight();  
		
		vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		fexp = _fexp;
		setTitle ("Function");
		setIcon(R.drawable.draw_function);
		setCancelable(true);
		setButton("Draw", new OnClickListener() 
		{	
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				fexp.setExpression(function_edit.getText().toString());
				dismiss();
			}
		});
		setButton2("Cancel", new OnClickListener() 
		{	
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dismiss();
			}
		});
		
		this.setOnDismissListener(new OnDismissListener() 
		{	
			@Override
			public void onDismiss(DialogInterface dialog) 
			{
				fexp.closeFd();
			}
		});
		
		LayoutInflater inflater=((Activity)context).getLayoutInflater();
		View function=inflater.inflate(R.layout.functiondialog, null);
		setView(function);
		function_edit = (EditText)function.findViewById(R.id.function_edit);
		function_edit.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				function_edit.setText("");
				fs.delete(0, fs.length());
				return true;
			}
		});
		function_edit.setText(sexp);
		
		if (height < 480 || width < 320)
		{
			function_edit.setInputType(InputType.TYPE_CLASS_TEXT);
			buttons = (TableLayout)function.findViewById(R.id.fd_buttons);
			buttons.setVisibility(View.GONE);
			buttons_func = (TableLayout)function.findViewById(R.id.fd_buttons_func);
			buttons_func.setVisibility(View.GONE);
			bbackspace = (Button)function.findViewById(R.id.bbackspace);
			bbackspace.setVisibility(View.GONE);
		}
		else
		{
			fs.append(sexp);
			b1 = (Button)function.findViewById(R.id.b1);
			b1.setOnClickListener(this);
			
			b2 = (Button)function.findViewById(R.id.b2);
			b2.setOnClickListener(this);
			
			b3 = (Button)function.findViewById(R.id.b3);
			b3.setOnClickListener(this);
			
			b4 = (Button)function.findViewById(R.id.b4);
			b4.setOnClickListener(this);
			
			b5 = (Button)function.findViewById(R.id.b5);
			b5.setOnClickListener(this);
			
			b6 = (Button)function.findViewById(R.id.b6);
			b6.setOnClickListener(this);
			
			b7 = (Button)function.findViewById(R.id.b7);
			b7.setOnClickListener(this);
			
			b8 = (Button)function.findViewById(R.id.b8);
			b8.setOnClickListener(this);
			
			b9 = (Button)function.findViewById(R.id.b9);
			b9.setOnClickListener(this);
			
			b0 = (Button)function.findViewById(R.id.b0);
			b0.setOnClickListener(this);
			
			bx = (Button)function.findViewById(R.id.bx);
			bx.setOnClickListener(this);
			
			bp = (Button)function.findViewById(R.id.bp);
			bp.setOnClickListener(this);
			
			be = (Button)function.findViewById(R.id.be);
			be.setOnClickListener(this);
			
			bpi = (Button)function.findViewById(R.id.bpi);
			bpi.setOnClickListener(this);
			
			bplus = (Button)function.findViewById(R.id.bplus);
			bplus.setOnClickListener(this);
	
			bminus = (Button)function.findViewById(R.id.bminus);
			bminus.setOnClickListener(this);
			
			bmultip = (Button)function.findViewById(R.id.bmultip);
			bmultip.setOnClickListener(this);
			
			bdiv = (Button)function.findViewById(R.id.bdiv);
			bdiv.setOnClickListener(this);
			
			bmod = (Button)function.findViewById(R.id.bmod);
			bmod.setOnClickListener(this);
			
			bpow = (Button)function.findViewById(R.id.bpow);
			bpow.setOnClickListener(this);
			
			bexp = (Button)function.findViewById(R.id.bexp);
			bexp.setOnClickListener(this);
			
			bln = (Button)function.findViewById(R.id.bln);
			bln.setOnClickListener(this);
			
			blg = (Button)function.findViewById(R.id.blg);
			blg.setOnClickListener(this);
			
			bsin = (Button)function.findViewById(R.id.bsin);
			bsin.setOnClickListener(this);
			
			bcos = (Button)function.findViewById(R.id.bcos);
			bcos.setOnClickListener(this);
			
			btan = (Button)function.findViewById(R.id.btan);
			btan.setOnClickListener(this);
			
			bsign = (Button)function.findViewById(R.id.bsign);
			bsign.setOnClickListener(this);
			
			bfact = (Button)function.findViewById(R.id.bfact);
			bfact.setOnClickListener(this);
			
			bsqrt = (Button)function.findViewById(R.id.bsqrt);
			bsqrt.setOnClickListener(this);
			
			babs = (Button)function.findViewById(R.id.babs);
			babs.setOnClickListener(this);
			
			bbo = (Button)function.findViewById(R.id.bbo);
			bbo.setOnClickListener(this);
			
			bbc = (Button)function.findViewById(R.id.bbc);
			bbc.setOnClickListener(this);
			
			bleft = (Button)function.findViewById(R.id.bleft);
			bleft.setOnClickListener(this);
			
			bright = (Button)function.findViewById(R.id.bright);
			bright.setOnClickListener(this);
			
			bbackspace = (Button)function.findViewById(R.id.bbackspace);
			bbackspace.setOnClickListener(this);
		}
	}
	
	public void function_add (int n)
	{
		int i = function_edit.getSelectionStart();
		final String signs = "+-*/%^";
		if (i>0)
		{
			
			if ((fs.charAt(i-1) <'0' || fs.charAt(i-1) > '9') && fs.charAt(i-1)!='.' && fs.charAt(i-1)!='(' && signs.indexOf(fs.charAt(i-1))==-1) 
			{
				fs.insert(i, '*');
				i++;
			}
		}
		fs.insert(i,n);
		function_edit.setText(fs.toString());
		function_edit.setSelection(i+1);
	}
	
	public void function_add (String s)
	{
		// .
		// +-*/%^!
		// ()
		// e pi
		// functie
		final String signs = "+-*/%^!";
		int i = function_edit.getSelectionStart();
		if (i>0 && !s.equals(".") && !s.equals(")"))
		{
			char v = s.charAt(0);
			if (s.length()==1 && signs.indexOf(v)>=0)
			{
				if ((signs.indexOf(fs.charAt(i-1))>=0 && v!='-' && fs.charAt(i-1)!='!'))
				{
					fs.delete(i-1, i);
					i--;
				}
				else
				if (fs.charAt(i-1)=='!')
				{
					fs.insert(i, '*');
					i++;
				}
			}
			else if (signs.indexOf(fs.charAt(i-1))==-1 && fs.charAt(i-1)!='(' && fs.charAt(i-1)!='.')
			{
				fs.insert(i, '*');
				i++;
			}
			else
			if (fs.charAt(i-1)=='!')
			{
				fs.insert(i, '*');
				i++;
			}
			/*if ((fs.charAt(i-1)>='0' && fs.charAt(i-1) <= '9')) 
			{
				fs.insert(i,'*');
				i++;
			}
			else if (fs.charAt(i-1) == '.') fs.setCharAt(i-1, '*');*/
		}
		fs.insert(i, s);
		function_edit.setText(fs.toString());
		if (s.length()>1 && !s.equals("pi")) function_edit.setSelection(i+s.length()-1);
		else function_edit.setSelection(i+s.length());
	}

	@Override
	public void onClick(View v) 
	{
		if (v instanceof Button)
		{
			vibrator.vibrate(50);
			Button b = (Button)v;
			if (b==b1) function_add (1);
			else 
			if (b==b2) function_add (2);
			else
			if (b==b3) function_add (3);
			else
			if (b==b4) function_add (4);
			else
			if (b==b5) function_add (5);
			else
			if (b==b6) function_add (6);
			else
			if (b==b7) function_add (7);
			else
			if (b==b8) function_add (8);
			else
			if (b==b9) function_add (9);
			else
			if (b==b0) function_add (0);
			else
			if (b==bp) function_add (".");
			else
			if (b==bx) function_add ("x");
			else
			if (b==bplus) function_add ("+");
			else
			if (b==bminus) function_add ("-");
			else
			if (b==bmultip) function_add ("*");
			else
			if (b==bdiv) function_add ("/");
			else
			if (b==bmod) function_add ("%");
			else
			if (b==bpow) function_add ("^");
			else
			if (b==bfact) function_add ("!");
			else
			if (b==bbo) function_add ("(");
			else
			if (b==bbc) function_add (")");
			else
			if (b==bpi) function_add ("pi");
			else
			if (b==be) function_add ("e");
			else
			if (b==bexp) function_add ("exp()");
			else
			if (b==bln) function_add ("ln()");
			else
			if (b==blg) function_add ("lg()");
			else
			if (b==bsin) function_add ("sin()");
			else
			if (b==bcos) function_add ("cos()");
			else
			if (b==btan) function_add ("tan()");
			else
			if (b==bsign) function_add ("sign()");
			else
			if (b==bsqrt) function_add ("sqrt()");
			else
			if (b==babs) function_add ("abs()");
			else
			if (b==bleft) 
			{
				int i = function_edit.getSelectionStart();
				if (i>0) 
				{
					i--;
					function_edit.setSelection(i);
				}
			}
			else
			if (b==bright) 
			{
				int i = function_edit.getSelectionStart();
				if (i<function_edit.getText().length()) 
				{
					i++;
					function_edit.setSelection(i);
				}
			}
			else
			if (b==bbackspace) 
			{
				int i = function_edit.getSelectionStart();
				if (i>0) 
				{
					fs.delete(i-1, i);
					i--;
					function_edit.setText(fs.toString());
					function_edit.setSelection(i);
				}
			}
		}
	}
}
