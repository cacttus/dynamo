package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VECTOR2.java

import java.lang.Math;

public class VECTOR2
{

    float x;
    float y;
    // public VECTOR2 VECTOR2(float X, float Y)
    // {
        // x = X;
        // y = Y;
        // return this;
    // }

    VECTOR2()
    {
        x = 0.0F;
        y = 0.0F;
    }
	public VECTOR2 times(VECTOR2 t)
	{
	    VECTOR2 v = new VECTOR2();
        v.x = x * t.x;
        v.y = y * t.y;
        return v;	
	}
	
	public VECTOR2 dividedBy(VECTOR2 t)
    {
        VECTOR2 v = new VECTOR2();
        v.x = x / t.x;
        v.y = y / t.y;
        return v;
    }
    
    
	public VECTOR2 plus(VECTOR2 t)
    {
        VECTOR2 v = new VECTOR2();
        v.x = x + t.x;
        v.y = y + t.y;
        return v;
    }
    
	public VECTOR2 plusEquals(VECTOR2 t)
    {
        this.x += t.x;
		this.y += t.y;
        return this;
    }
	
    public VECTOR2 minus(VECTOR2 t)
    {
		VECTOR2 v = new VECTOR2();
		v.x = x - t.x;
		v.y = y - t.y;
        return v;
    }
	
	public VECTOR2 minusEquals(VECTOR2 t)
    {
        x -= t.x;
		y -= t.y;
        return this;
    }
	
	public VECTOR2 negative()
	{
		VECTOR2 v = new VECTOR2();
		v.x = -x;
		v.y = -y;
		return v;
	}

	// - DEPRECATED
    public void add(VECTOR2 t)
    {
        x = x + t.x;
        y = y + t.y;
    }

	// - DEPRECATED
    public VECTOR2 subtract(VECTOR2 t)
    {
        VECTOR2 v = new VECTOR2();
        v.x = x = x - t.x;
        v.y = y = y - t.y;
        return v;
    }

    public void equals(VECTOR2 t)
    {
        x = t.x;
        y = t.y;
    }

    public boolean isGreaterThan(VECTOR2 t)
    {
        return x >= t.x && y >= t.y;
    }

    public boolean isLessThan(VECTOR2 t)
    {
        return x <= t.x && y <= t.y;
    }

	//#############################
	// - FLOAT OPERATIONS
    public void assign(float f)
    {
        y = f;
        x = f;
    }
	
    public void equals(float f)
    {
        y = f;
        x = f;
    }
	
    public VECTOR2 plus(float f)
    {
        VECTOR2 v = new VECTOR2();
        v.x = y + f;
        v.y = x + f;
        return v;
    }

	// - WARNING - changed this from assignment to copy... may mess up somewhere
    public VECTOR2 minus(float f)
    {
		VECTOR2 v= new VECTOR2();
		v.x=x-f;
		v.y=y-f;
		return v;
    }
	public VECTOR2 minusEquals(float f)
	{
		x-=f;
		y-=f;
		return this;
	}
	
	public VECTOR2 times(float f)
    {
        VECTOR2 v = new VECTOR2();
        v.x = x * f;
        v.y = y * f;
        return v;
    }
    
	public VECTOR2 dividedBy(float f)
    {
        VECTOR2 v = new VECTOR2();
        v.x = x / f;
        v.y = y / f;
        return v;
    }
    
	public boolean isLessThan(float f)
	{
		return ( x < f && y < f );
	}
	//##############################
	// - Linear Algebra Operations
	
	public float length() {
		return (float)Math.sqrt((double)(this.dot(this)));
	}
	public float squaredLength() {
		float f = this.dot(this);
		return f;
	}	
	public VECTOR2 normalize() {
		float f = this.length();
		this.x /=f;
		this.y /=f;
		return this;
	}
	public float dot( VECTOR2 rhs )
	{
		return (x*rhs.x + y*rhs.y);
	}
	public float distanceTo( VECTOR2 rhs )
	{
		return this.minus(rhs).length();
	}
	public float squaredDistanceTo( VECTOR2 rhs )
	{
		return this.minus(rhs).squaredLength();
	}
		
	/**
	*	@fn lerpTo()
	*	@brief Lerps between this vector and the input vector, t must be between 0 and 1.
	*/
	public VECTOR2 lerpTo( VECTOR2 v, float t )
	{
		VECTOR2 vx = new VECTOR2();
		vx.x = this.x + ( v.x - this.x )*t;
		vx.y = this.y + ( v.y - this.y )*t;
		return vx;
	}
	
	/**
	*	@fn bezierD4()
	*	@brief Returns a vector that represents a position on a bezier curve, from a set of control points.
	*	@return A Zero vector if there are not at least two points.
	*	@return The interpolated point on the bezier' curve.
	*	@param t Must be between 0 and 1.
	*/
	public static VECTOR2 bezierD4( VECTOR2[] dv, float t )
	{
		VECTOR2 v = new VECTOR2();
		if( dv.length <2 )
			return v;
		
		float omt = 1.0F-t, te1, te2, dte, ni, ii, co;
		int a, b, c, j, n = dv.length-1;
		//			    [ n ]
		// - Sum[n,i=1] [ i ] * (1-t)^(n-i) * t^i * p[i]
		
		// degree = n points -1
		for( int i=0 ;i<dv.length; ++i )
		{
			a=b=c=1;

			for (j=2; j<=n; j++)
				a*=j;
			for (j=2; j<=i; j++)
				b*=j;
			for (j=2; j<=(n-i); j++)
				c*=j;
    
			co = a/(b*c);	// - Bezier Coefficient n! / (i! * (n-i)!)
			
			te1 = (float) Math.pow( (double)omt, (double)(n-i) );
			te2 = (float) Math.pow( (double)t, (double)i );
			dte = te1*te2;
			ni=n*dte;
			ii=i*dte;
			v.x += co * dte * dv[i].x;
			v.y += co * dte * dv[i].y;
		}
		return v;
	}
}
