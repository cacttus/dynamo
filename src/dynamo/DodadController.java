package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DodadController.java


public class DodadController
{
    class Dodad extends Collideable
    {

        VECTOR2 LastPos;
        VECTOR2 Pos;
		VECTOR2 lastVelocity;
        boolean Dec;
        int Cur_Img;
        int Life;
        int ID;
        double Angle;
        double Radius;
        double AngleAdder;
        boolean Active;
		int curveType;
		
		public static final int LOOP = 0;			// - Basic circle curve
		public static final int EPICYCLOID = 1;		// - Epicycloid curve
		public static final int HYPOCYCLOID = 2;		// - Hypocycloid curve
		public static final int MAX_CURVES = 3;		// - Maximum number of curves.
		
		//public static final int
		
        Dodad()
        {
            super();
            Dec = false;
            Cur_Img = 118;
            Life = 0;
            ID = 0;
            Angle = 0.0D;
            Radius = 0.0D;
            AngleAdder = 0.0D;
            Active = false;
            Pos = new VECTOR2();
            LastPos = new VECTOR2();
			lastVelocity = new VECTOR2();
            LastPos.equals(0.0F);
            Pos.equals(0.0F);
        }
    }


    DodadController()
    {
        Damage = 10;
        // BoundBox = new BOUNDBOX();
        // BoundBox.P1.x = BoundBox.P1.y = 0.0F;
        // BoundBox.P2.x = 32F;
        // BoundBox.P2.y = 0.0F;
        // BoundBox.P3.x = 0.0F;
        // BoundBox.P3.y = 32F;
        // BoundBox.P4.x = BoundBox.P4.y = 32F;
        Dodads = new Dodad[100];
        for(int i = 0; i < Dodads.length; i++)
            Dodads[i] = new Dodad();

    }

    public void Update(VECTOR2 Pos)
    {
        for(int i = 0; i < Dodads.length; i++)
        {
            if(!Dodads[i].Active)
                continue;
            if(Dodads[i].Life > 70)
            {
                Dodads[i].Cur_Img = 117;
                Dodads[i].Radius = 30D;
				Dodads[i].boundCircle.setBoundRadius( 15F );
            } 
			else if(Dodads[i].Life > 40)
            {
                Dodads[i].Cur_Img = 118;
                Dodads[i].Radius = 20D;
				Dodads[i].boundCircle.setBoundRadius( 11F );
			} 
			else if(Dodads[i].Life > 10)
            {
                Dodads[i].Cur_Img = 119;
                Dodads[i].Radius = 20D;
				Dodads[i].boundCircle.setBoundRadius( 7F );
            }
			
            Dodads[i].Angle += Dodads[i].AngleAdder;
			
			float dx = Pos.x - Dodads[i].LastPos.x;
			float dy = Pos.y - Dodads[i].LastPos.y;
			Dodads[i].lastVelocity.x = dx;
			Dodads[i].lastVelocity.y = dy;
			
			if( Dodads[i].curveType == Dodad.LOOP ) {
				double px = Dodads[i].Radius * Math.cos(Dodads[i].Angle);
				double py = Dodads[i].Radius * Math.sin(Dodads[i].Angle);
				Dodads[i].lastVelocity.x+=px;
				Dodads[i].lastVelocity.y+=py;
			}
			else if( Dodads[i].curveType == Dodad.EPICYCLOID ) {
				Dodads[i].Radius/=2;
				double k = 2.5;
				double r = Dodads[i].Radius;
				double R = k*r;
				double x,y,th=Dodads[i].Angle;
				x = (R+r) * (Math.cos(th)) - r * Math.cos( (R+r)/r * th );
				y = (R+r) * (Math.sin(th)) - r * Math.sin( (R+r)/r * th );
				
				Dodads[i].lastVelocity.x+=(float)x;
				Dodads[i].lastVelocity.y+=(float)y;
			}
			else if( Dodads[i].curveType == Dodad.HYPOCYCLOID ) {
				double k = 2.5;
				double r = Dodads[i].Radius;
				double R = k*r;
				double x,y,th=Dodads[i].Angle;
				x = (R-r) * (Math.cos(th)) + r * Math.cos( (R-r)/r * th );
				y = (R-r) * (Math.sin(th)) - r * Math.sin( (R-r)/r * th );
				Dodads[i].lastVelocity.x+=(float)x;
				Dodads[i].lastVelocity.y+=(float)y;
			}
			
			Dodads[i].Pos.plusEquals(Dodads[i].lastVelocity);
			Dodads[i].LastPos.equals(Dodads[i].Pos);
			
			// - Much more concise way to do variable rollovers
			if(Dodads[i].Angle > 6.28D)
				Dodads[i].Angle -=6.28D;
			if(Dodads[i].Angle < 0.0D)
				Dodads[i].Angle +=6.28D;
		}
    }

    BOUNDBOX BoundBox;
    int Damage;
    Dodad Dodads[];
}
