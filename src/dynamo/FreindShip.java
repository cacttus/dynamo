package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FreindShip.java

import java.util.Random;

class FreindShip extends Ship
{
	Random rand;
	
    FreindShip()
    {
        Targeted_Enemy = 0;
        Enabled = false;
        HasTarget = false;
        at_tmr = 12;
        max_at_tmr = 12;
		rand = new Random();
    }
	
	public void warpAnimation( float t )
	{
		// - 205 - 210
		if( t>=1.0 )
			t=0.999F;
		this.Tile_Current = 238 + (int)java.lang.Math.floor( 3F * t *2 );
	}
	
    void Update(EnemyCreator ec, PlayerShip ps)
    {
        at_tmr--;
        if(!HasTarget)
        {
			float hpos=0.0F;
			int htar=0;
			// - Find a new target
            for(int i = 0; i < ec.Enemies.length; i++)
			{
				// - Find the highest enemy
                if(ec.Enemies[i].InUse)
                {
					if( ec.Enemies[i].Pos.y > hpos )
					{
						hpos = ec.Enemies[i].Pos.y;
						htar=i;
					}
				}
			}
			if( hpos <= 480.0 )
			{
				Targeted_Enemy = htar;
				HasTarget = true;
			}
        } 
		else 
		if( Targeted_Enemy!=-1 )
		{
			if( ec.Enemies[Targeted_Enemy].InUse && ( ec.Enemies[Targeted_Enemy].Pos.y <= 480F ) )
			{
				float PAD = (Speed.x+(1F));
				if( 
				( Pos.x > ec.Enemies[Targeted_Enemy].Pos.x-(Speed.x+(1F)) ) && 
				( Pos.x < ec.Enemies[Targeted_Enemy].Pos.x+(Speed.x+(1F)) ) )
				{
					if(at_tmr <= 0)
					{
						at_tmr = max_at_tmr;
						for(int i = 0; i < ps.Missiles.length; i++)
						{
							if(!ps.Missiles[i].InUse)
							{
								ps.Missiles[i].Speed.x = 0.0F;
								ps.Missiles[i].Speed.y = -14F;
								ps.Missiles[i].Pos.x = Pos.x;
								ps.Missiles[i].Pos.y = Pos.y;
								ps.Missiles[i].InUse = true;
								ps.Missiles[i].Damage = 30;
								ps.Missiles[i].Img_Cur = 139;
								if( rand.nextInt(10) == 1 )	// 1 in ten chance of heat sink missile.
								{
									ps.Missiles[i].Type=1;
									ps.Missiles[i].t=0.0F;
									ps.Missiles[i].SetHeatSinkKeys( Pos, ec.Enemies[Targeted_Enemy].Pos );
								}
								else
									ps.Missiles[i].Type=0;
								
								i = ps.Missiles.length;
							}
						}
					}
					if(ec.Enemies[Targeted_Enemy].Health <= 0 || !ec.Enemies[Targeted_Enemy].InUse)
						HasTarget = false;
				} 
				else
				{
					if(ec.Enemies[Targeted_Enemy].Pos.x < (Pos.x+PAD) )
						Pos.x -= Speed.x;
					else if(ec.Enemies[Targeted_Enemy].Pos.x > (Pos.x-PAD) )
						Pos.x += Speed.x;
					
					if(Pos.x < 16.0F)
						Pos.x = 16.0F;
					if(Pos.x > 496.0F)
						Pos.x = 496.0F;
				}
			}
		}
		else
		{
			HasTarget=false;
			Targeted_Enemy=-1;
		}
		// else
        // {

        // }
    }

    int Targeted_Enemy;
    boolean Enabled;
    boolean HasTarget;
    int at_tmr;
    int max_at_tmr;
}
