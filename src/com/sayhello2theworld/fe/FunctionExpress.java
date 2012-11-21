package com.sayhello2theworld.fe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class FunctionExpress extends Activity {
    /** Called when the activity is first created. */
    
	public static final int EDIT_FUNCTION = Menu.FIRST+1;
	public static final int GOTO0 = Menu.FIRST+2;
	public static final int SHOW_MIN_MAX = Menu.FIRST+3;
	public static final int COMPUTE_FX = Menu.FIRST+4;
	public static final int SETTINGS = Menu.FIRST+5;
	public static final int EXIT = Menu.FIRST+6;
	public static final int ABOUT = Menu.FIRST+7;
	
	private FunctionDialog fd = null;
	
    private TextView function;
    private TextView function_title;
    private LinearLayout screen;
    private Draw d;
    private SeekBar zoomslider;
    private TextView zoom;
    
    private Expression exp = null;
    private String sexp = ""; 
    
    private float a=-10;
    private float b=10;
    private float n=1;
    private int L=1;
    private int LX=1;
    private int LY=1;
    
    private float tx = 0;
    private float ty = 0;
    private float Ox = 0, Oy = 0;
    private int mx = 5;
    private int my = 5;
    
    private float lx = 0;
    private float ly = 0;
    private boolean showLines = false;
    
    private float max = Float.NEGATIVE_INFINITY;
    private float min = Float.POSITIVE_INFINITY;
    
    private float dmin = Float.NaN;
    private float dmax = Float.NaN;
    
    private boolean showMinMax = false;
    
    private void showFd ()
    {
    	if (fd == null) 
    	{
    		fd = new FunctionDialog(d.getContext(), FunctionExpress.this, sexp);
    		fd.show();
    	}
    }
    
    public void closeFd ()
    {
    	fd = null;
    }
    
    protected float d2fX (float dx)
    {
    	return (dx-LX+tx)*(b-a)/L;
    }
    
    protected float f2dX (float fx)
    {
    	return (fx*L/(b-a))-tx+LX;
    }
    
    protected float d2fY (float dy)
    {
    	return -(dy-ty-LY)*(b-a)/L;
    }
    
    protected float f2dY (float fy)
    {
    	return -(fy*L/(b-a))+ty+LY;
    }
    
    public class Draw extends View
    {
    	public Draw (Context context)
    	{
    		super (context);
    	}
    	
    	@Override
    	protected void onDraw(Canvas canvas) 
    	{
    		// zoom.setText("["+a+","+b+"]");
    		Paint paper = new Paint ();
    		paper.setColor(Color.WHITE);
    		paper.setAntiAlias(true);
    		int w = this.getWidth();
    		int h = this.getHeight();
    		
    		L = w-6;
    		LX = L/2;
    		LY = (h-6)/2;
    		
    		/*Ox = w/2 - tx;
    		Oy = h/2 - ty;*/
    		Ox = f2dX(0);
    		Oy = f2dY(0);
    		
    		max = Float.NEGATIVE_INFINITY;
    	    min = Float.POSITIVE_INFINITY;
    		
    		// System.out.println ("tx: "+tx+" ty: "+ty+" L:"+L+" LX:"+LX+" (b-a): "+(b-a));
    		
    		canvas.drawRect(0, 0, w, h, paper);
    		
    		Paint bp = new Paint ();
    		bp.setColor(Color.BLACK);
    		bp.setStyle(Style.STROKE);
    		bp.setAntiAlias(true);
    		canvas.drawRect(2,2, w-3, h-3, bp);
    		
    		Paint bl = new Paint ();
    		bl.setColor(Color.LTGRAY);
    		bl.setAntiAlias(true);
    		Paint bl2 = new Paint ();
    		bl2.setColor(Color.GRAY);
    		bl2.setAntiAlias(true);
    		// vertical lines
    		for (float i=Ox-5; i>2; i=i-5) canvas.drawLine(i, 3, i, h-3, bl);
    		for (float i=Ox+5; i<w-3; i=i+5) canvas.drawLine(i, 3, i, h-3, bl);
    		
    		// horizontal lines
    		for (float i=Oy-5; i>2; i=i-5) canvas.drawLine(3, i, w-3, i, bl);
    		for (float i=Oy+5; i<h-3; i=i+5) canvas.drawLine(3, i, w-3, i, bl);
    		
    		// marked vertical lines
    		for (float i=Ox-5*mx; i>2; i=i-5*mx) canvas.drawLine(i, 3, i, h-3, bl2);
    		for (float i=Ox+5*mx; i<w-3; i=i+5*mx) canvas.drawLine(i, 3, i, h-3, bl2);
    		
    		// marked horizontal lines
    		for (float i=Oy-5*my; i>2; i=i-5*my) canvas.drawLine(3, i, w-3, i, bl2);
    		for (float i=Oy+5*my; i<h-3; i=i+5*my) canvas.drawLine(3, i, w-3, i, bl2);
    		
    		Paint lp = new Paint();
    		lp.setColor(Color.BLACK);
    		lp.setAntiAlias(true);
    		// origins
    		canvas.drawLine(3, Oy-0.25f, w-3, Oy-0.25f, lp);
    		canvas.drawLine(3, Oy, w-3, Oy, lp);
    		canvas.drawLine(Ox, 3, Ox, h-3, lp);
    		canvas.drawLine(Ox-0.25f, 3, Ox-0.25f, h-3, lp);
    		
    		n = (b-a)/L;
    		if (exp!=null)
    		{
	    		Paint e = new Paint();
	    		e.setColor(Color.BLUE);
	    		e.setAntiAlias(true);
	    		e.setStrokeWidth(2);
	    		float x = a+tx*n; 
	    		float yold=0;
	    		float dyold = 0;
	    		boolean yout = false;
	    		boolean youtold = false;
	    		int points = 0;
	    		for (int i=3;i<w-3;i++)
	    		{
	    			float y = exp.evaluate(x);
	    			if (!Float.isInfinite(y) && !Float.isNaN(y))
	    			{
	    				if (y > max) max = y;
	    				if (y < min) min = y;
	    			}
	    			/*float s=0;
	    			int l =0;
	    			boolean inf = false;
	    			for (float x1 = x; x1<x+n; x1=x1+n/10)
	    			{
	    				l++;
	    				y = exp.evaluate(x1)*mfy;
	    				if (Float.isInfinite(y)) 
	    				{
	    					inf = true;
	    					break;
	    				}
	    				else
	    				if (Float.isNaN(y))
	    				{
	    					inf = true;
	    					break;
	    				}
	    				else s = s + y;
	    			}
	    			if (inf == false) y = s / l;*/
	    			// if (!Float.isNaN(yold) && !Float.isNaN(y))
	    			{
	    				float dy = f2dY(y);
		    			yout = false;
		    			if (Float.isNaN(y) && !Float.isNaN(yold))
		    			{
		    				if (y<0) dy = 3;
		    				else dy = h-3;
		    				yout = true;
		    			}
		    			else if (!Float.isNaN(y) && Float.isNaN(yold))
		    			{
		    				if (y<0) dyold = 3;
		    				else dyold = h-3;
		    				youtold = true;
		    			}
		    			else
		    			{
			    			if (dy<3) 
			    			{
			    				dy = 3;
			    				yout = true;
			    			}
			    			if (dy > h-3) 
			    			{
			    				dy = h-3;
			    				yout = true;
			    			}
		    			}
		    			if (min == y) dmin = dy;
		    			if (max == y) dmax = dy;
		    			// System.out.println ("onDraw(): "+values[i]);
		    			points ++;
		    			if (points>1 && !(yout && youtold)) 
		    			{
		    				canvas.drawLine(i-1, dyold, i, dy, e);
		    				//if (x >-1) System.out.println (x+": "+y+" "+dyold+" to "+dy);
		    			}
		    			dyold = dy;
		    			youtold = yout;
	    			}
	    			/*else 
	    			{
	    				yold = y;
	    				youtold = true;
	    			}*/
	    			x=x+n;
	    		}
    		}
    		
    		// min & max
    		if (showMinMax)
    		{
    			Paint gp = new Paint();
    			gp.setColor(Color.RED);
    			gp.setAntiAlias(true);
	    		gp.setStrokeWidth(2);
    			canvas.drawLine(3, dmin, w-3, dmin, gp);
    			canvas.drawLine(3, dmax, w-3, dmax, gp);
    		}
    		
    		// lines
    		if (showLines)
    		{
    			float dlx = lx;
    			float dly = ly;
	    		Paint rp = new Paint ();
	    		rp.setColor(Color.RED);
	    		rp.setTypeface(Typeface.MONOSPACE);
	    		rp.setTextSize(20);
	    		rp.setAntiAlias(true);
	    		rp.setStrokeWidth(2);
	    		rp.setSubpixelText(true);
	    		canvas.drawLine(3, dly, w-3, dly, rp);
	    		canvas.drawLine(dlx, 3, dlx, h-3, rp);
	    		canvas.drawText("X:"+Math.round(d2fX(lx)*1000)/1000.0, lx+3, ly+22, rp);
	    		canvas.drawText("Y:"+Math.round(d2fY(ly)*1000)/1000.0+"", lx+3, ly+38, rp);
    		}
    		
    		// sqare
    		int scalex = 100;
    		int scaley = 50;
    		
    		canvas.drawRect(w-scalex, h-scaley, w-3, h-3, paper);
    		canvas.drawRect(w-scalex, h-scaley, w-3, h-3, bp);
    		
    		// sqare vertical lines
    		for (int i=1; i<=4; i++) canvas.drawLine(w-scalex+(10+i*5), h-scaley+10, w-scalex+(10+i*5), h-scaley+35, bl);
    		// sqare horizontal lines
    		for (int i=1; i<=4; i++) canvas.drawLine(w-scalex+10, h-scaley+10+i*5, w-scalex+35, h-scaley+10+i*5, bl);
    		// sqare border
    		bl2.setStyle(Style.STROKE);
    		canvas.drawRect (w-scalex+10, h-scaley+10, w-scalex+35, h-scaley+35, bl2);
    		bp.setTypeface(Typeface.MONOSPACE);
    		bp.setTextSize(20);
    		bp.setAntiAlias(true);
    		bp.setSubpixelText(true);
    		canvas.drawText(""+(Math.round(n*mx*5*100)/100.0), w-scalex+45, h-scaley+25, bp);
    		// System.out.println("onDraw()");
    	}
    }
    
    //@Override
    //public void OnSavedInstance
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        function = (TextView)findViewById(R.id.function);
        function.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				showFd();
			}
		});
        function_title = (TextView)findViewById(R.id.function_title);
        function_title.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				showFd();
			}
		});
        screen = (LinearLayout)findViewById(R.id.screen);
        //draw = (Button)findViewById(R.id.draw);
        //goto0 = (Button)findViewById(R.id.goto0);
        zoomslider = (SeekBar)findViewById(R.id.zoomslider);
        zoom = (TextView)findViewById(R.id.zoom);
        /*draw.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				System.out.println("onClick()");
				new FunctionDialog(v.getContext(), FunctionExpress.this, sexp).show();
			}
		});
        goto0.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) 
			{
				System.out.println("onClick()");
				tx = 0;
				ty = 0;
				showLines = false;
				d.invalidate();
			}
		});*/
        zoomslider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
        {	
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) 
			{
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) 
			{
				if (fromUser==true)
				{
					if (progress==0)
					{
						a = -0.1f;
						b = 0.1f;
					}
					else
					{
						a = -progress;
						b = progress;
						// zoom.setText("["+(-progress)+","+(progress)+"]");
					}
					zoom.setText("["+a+","+b+"]");
					d.invalidate();
				}
			}
		});
        d = new Draw(screen.getContext());
        d.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        screen.addView(d);
        d.setOnTouchListener(new OnTouchListener() 
        {	
        	private int x;
        	private int y;
        	
        	private int dx=0, dy=0;
        	
        	private boolean move = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{	
				//if (event.getPointerCount()==1)
				{
					int action = event.getAction();
					switch (action)
					{
						case MotionEvent.ACTION_DOWN:
							move = false;
							showLines = false;
							x = (int)event.getX();
							y = (int)event.getY();
							break;
							
						case MotionEvent.ACTION_MOVE:
							int X = (int)event.getX();
							int Y = (int)event.getY();
							if (Math.abs(X-x)>3 || Math.abs(Y-y)>3)
							{
								move = true;
								dx = x - X;
								dy = y - Y;
								x = X;
								y = Y;
								tx = tx + dx;
								ty = ty - dy;
								// System.out.println (ty);
								d.invalidate();
							}
							break;
							
						case MotionEvent.ACTION_UP:
							if (move == false)
							{
								if (exp != null)
								{
									showLines = true;
									lx = event.getX();
									ly = event.getY();
									d.invalidate();
								}
								else showFd();
							}
							break;
					}
				}
				/*else if (event.getPointerCount()==2)
				{
					multi = true;
					int action = event.getAction();
					switch (action)
					{
						case MotionEvent.ACTION_DOWN:
							move = false;
							showLines = false;
							x1 = (int)event.getX(0);
							y1 = (int)event.getY(0);
							x2 = (int)event.getX(1);
							y2 = (int)event.getY(2);
							s = Math.abs (x1-x2)*Math.abs(y1-y2);
							a1 = a;
							b1 = b;
							break;
							
						case MotionEvent.ACTION_MOVE:
							int X1 = (int)event.getX(0);
							int Y1 = (int)event.getY(0);
							int X2 = (int)event.getX(1);
							int Y2 = (int)event.getY(1);
							
							if (x1> 0 && x2>0)
							{
								float f = (float)((Math.abs(X1-X2)-Math.abs(x1-x2))*(b1-a1)/L);
								System.out.println ("x1: "+x1+" x2: "+x2+" X1: "+X1+" X2: "+X2+" f: "+f);
								a = a-f;
								b = b+f;
								d.invalidate();
							}
							
							break;
							
						case MotionEvent.ACTION_UP:
							multi = false;
							break;
					}
				}*/
				return true;
			}
		});
        
        if (savedInstanceState!=null)
        {
        	sexp = savedInstanceState.getString("exp");
        	if (sexp == null) sexp = "";
        	tx = savedInstanceState.getFloat ("tx");
        	ty = savedInstanceState.getFloat("ty");
        	showLines = savedInstanceState.getBoolean("showLines");
        	lx = savedInstanceState.getFloat ("lx");
        	ly = savedInstanceState.getFloat("ly");
        	zoomslider.setProgress(savedInstanceState.getInt("progress"));
        	showMinMax = savedInstanceState.getBoolean("showMinMax");
        }
        else
        {
        	Toast.makeText(this, "Tap the drawing paper to add a function", Toast.LENGTH_LONG).show();
        }
        
        if (sexp.length()>0) setExpression(sexp);
        // d.refreshDrawableState();
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
    	savedInstanceState.putString("exp", sexp);
    	savedInstanceState.putFloat ("tx", tx);
    	savedInstanceState.putFloat("ty", ty);
    	savedInstanceState.putBoolean("showLines", showLines);
    	savedInstanceState.putFloat ("lx", lx);
    	savedInstanceState.putFloat("ly", ly);
    	savedInstanceState.putInt("progress", zoomslider.getProgress());
    	savedInstanceState.putBoolean("showMinMax", showMinMax);
    }
    
    public void setExpression (String s)
    {
    	sexp = s;
    	try
		{
    		//tx = 0;
    		//ty = 0;
    		//showLines = false;
			exp = new Expression(s);
			function.setText(exp.toString());
			for (int i=0;i<300;i++)
			{
				// values[i] = (float)exp.evaluate((i-150)*10);
				// System.out.println (values[i]);
			}
			d.invalidate();
			// d.refreshDrawableState();
		}
		catch (Exception ex)
		{
			Toast.makeText(d.getContext(), "Expression error "+ex, Toast.LENGTH_SHORT).show();
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
    	//new MenuInflater(getApplication()).inflate(R.layout.menu, menu);
    	menu.add(0, EDIT_FUNCTION, 0, "Edit Function").setIcon(R.drawable.draw_function);
    	menu.add(0, GOTO0, 0, "Goto (0,0)").setIcon(R.drawable.goto0);
    	menu.add(0, SHOW_MIN_MAX, 0, "Show/Hide Limits").setIcon(R.drawable.minmax);
    	menu.add(0, COMPUTE_FX, 0, "Compute f(x)").setIcon(R.drawable.funct);
    	// menu.add(0, SETTINGS, 0, "Settings").setIcon(R.drawable.configure);
    	// menu.add(0, EXIT, 0, "Exit");
    	menu.add(0, ABOUT, 0, "About").setIcon(R.drawable.about);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem menu)
    {
    	switch (menu.getItemId())
    	{
    		case EDIT_FUNCTION:
    			showFd();
    			// new FunctionDialog(d.getContext(), this, sexp).show();
    			return true;
    			
    		case GOTO0:
    			tx = 0;
    			ty = 0;
    			d.invalidate();
    			return true;
    			
    		case SHOW_MIN_MAX:
    			showMinMax = !showMinMax;
    			d.invalidate();
    			return true;
    			
    		case COMPUTE_FX:
    			AlertDialog.Builder fx = new AlertDialog.Builder(this);
    			fx.setTitle("Compute f(x) in");
    			fx.setIcon(R.drawable.funct);
    			final LinearLayout l = new LinearLayout(this);
    			l.setOrientation(LinearLayout.VERTICAL);
    			final EditText nr = new EditText(this);
    			nr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    			final TextView result = new TextView(this);
    			result.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    			result.setText("f(x)=");
    			result.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
    			l.addView(nr);
    			l.addView(result);
    			fx.setView(l);
    			
    			nr.addTextChangedListener(new TextWatcher() 
    			{	
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) 
					{	
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) 
					{
					}
					
					@Override
					public void afterTextChanged(Editable s) 
					{
						try
						{
							float x = Float.parseFloat(nr.getText().toString());
							float r = exp.evaluate(x);
							result.setText("f("+x+")="+r);
						}
						catch (Exception ex)
						{
						}
					}
				});
    			
    			fx.setPositiveButton("Show", new DialogInterface.OnClickListener() 
    			{	
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{	
						try
						{
							if (exp!=null)
							{
								float x = Float.parseFloat(nr.getText().toString());
								float y = exp.evaluate(x);
								tx = 0;
								ty = 0;
								tx = f2dX (x)-LX;
								ty = -(f2dY (y)-LY);
								lx = LX;
								ly = LY;
								showLines = true;
								d.invalidate();
							}
						}
						catch (Exception ex)
						{
						}
					}
				});
    			
    			fx.setNegativeButton("Cancel", null);
    			fx.show();
    			return true;
    			
    		case ABOUT:
    			AlertDialog.Builder about = new AlertDialog.Builder(FunctionExpress.this).setTitle("FunctionExpress").setIcon(R.drawable.icon).setNeutralButton("OK", null);
    			about.setMessage("Version 0.2\n\nWritten by Alexandru RADOVICI\nhttp://ilab.fils.pub.ro\n\nThank you for using it, suggestions are always welcome.");
    			about.show();
    			return true;
    			
    		case EXIT:
    			System.exit(0);
    			return true;
    	}
    	return super.onOptionsItemSelected(menu);
    }
}