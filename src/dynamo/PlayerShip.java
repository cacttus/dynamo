package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PlayerShip.java


public class PlayerShip extends Ship
{
    float MaxMissileDelay;
    float MaxRocketDelay;

   // int Num_Missiles;
    int MissileDelay;
    int RocketDelay;

    int Upgrade;
    int Current_Weapon;
    int Shield_Upgrade;
    int Booster_Upgrade;
    int Weapon_Upgrade;
    int ShieldAmt;
	
    int Damage;

	VECTOR2 saveSpeed;
    Missile Missiles[];
	int Room_Width, Room_Height;
        
    public PlayerShip(int rw, int rh)
    {
        Room_Width = rw;
        Room_Height = rh;
       // Num_Missiles = 0;
        MaxMissileDelay = 10F;
        MissileDelay = 10;
        MaxRocketDelay = 20F;
        RocketDelay = 20;
        Upgrade = 0;
        Current_Weapon = 0;
        Shield_Upgrade = 0;
        Booster_Upgrade = 0;
        Weapon_Upgrade = 0;
        ShieldAmt = 0;
        Damage = 10;
		saveSpeed= new VECTOR2();
    }
	
	public void warpAnimation( float t )
	{
		// - 205 - 210
		if( t>=1.0 )
			t=0.999F;
		this.Tile_Current = 219 + (int)java.lang.Math.floor( 3F * t *2 );
	}
	
    public void InitPlayer()
    {
        Missiles = new Missile[100];
        for(int i = 0; i < Missiles.length; i++)
        {
            Missiles[i] = new Missile(Room_Width, Room_Height);
            Missiles[i].Init();
        }

        Health = MaxHealth = 100;
        Pos.x = Room_Width/2;
        Pos.y = Room_Height-32;
    }
	/**
	*	@fn damage()
	*	@brief Compute the damage done to the player ship.
	*/
	public int damage( int damageAmt, int level ) {
		
		float damage = ( damageAmt - ShieldAmt );
		if( damage<=0.0f )
			damage=1.0f * level;
		this.Health -= damage;
		
		if( this.Health < 0 )
			this.Health = 0;
		return Health;
	}

	/**
	*	@fn update()
	*	@brief Main update function for the player ship.
	*/
	public void Update()
    {
        UpdateAnimation();
        if(MissileDelay > 0)
            MissileDelay--;
        if(RocketDelay > 0)
            RocketDelay--;
        for(int i = 0; i < Missiles.length; i++)
        {
            if(!Missiles[i].InUse)
                continue;
			if( Missiles[i].Type==1 )
			{
				float hsSpeed=0.02f;
				
				if( Missiles[i].t<1.0F )
				{
					Missiles[i].t += hsSpeed;
					if(Missiles[i].t>1.0F)
						Missiles[i].t=1.0F;
					Missiles[i].Pos = VECTOR2.bezierD4( Missiles[i].Keys, Missiles[i].t );
				}
				else if( Missiles[i].State==0 )
				{
					Missiles[i].State=1; // wait one frame to do collision detection, if possible
				}
				else if( Missiles[i].State==1 )
				{
					//Missiles[i].InUse=false;
					VECTOR2 v = VECTOR2.bezierD4( Missiles[i].Keys, 0.99F );
					VECTOR2 v2 = VECTOR2.bezierD4( Missiles[i].Keys, 1.0F );
					VECTOR2 dv = new VECTOR2();
					dv = v2.minus(v);
					float len = Missiles[i].Speed.length();
					dv.normalize();
					dv = dv.times(len);
					Missiles[i].Speed = dv;
					Missiles[i].Type = 0;	// - No more heat sink, it's over.
					Missiles[i].State=2;
				}
			}
			else
			{
				Missiles[i].Speed.x += Missiles[i].Gravity.x;
				Missiles[i].Speed.y += Missiles[i].Gravity.y;
				Missiles[i].Pos.x += Missiles[i].Speed.x;
				Missiles[i].Pos.y += Missiles[i].Speed.y;
			}
            if(Missiles[i].Speed.y > 18F)
                Missiles[i].Speed.y = 18F;
            if(Missiles[i].Speed.y < -18F)
                Missiles[i].Speed.y = -18F;
            if(Missiles[i].Pos.x < 16.0F)
                Missiles[i].InUse = false;
            if(Missiles[i].Pos.y < -16.0F)
                Missiles[i].InUse = false;
            if(Missiles[i].Pos.x > Room_Width+100)
                Missiles[i].InUse = false;
            if(Missiles[i].Pos.y > Room_Height+100)
                Missiles[i].InUse = false;
        }

    }

    public boolean AddMissile(float xspd, float yspd, int type)
    {
        if(MissileDelay <= 0 || RocketDelay <= 0)
        {
            for(int i = 0; i < Missiles.length; i++)
            {
                if(Missiles[i].InUse)
                    continue;
                if(MissileDelay <= 0)
                {
                    if(Weapon_Upgrade == 0)
                        Missiles[i].Img_Cur = 81;
                    if(Weapon_Upgrade == 1)
                        Missiles[i].Img_Cur = 70;
                    if(Weapon_Upgrade == 2)
                        Missiles[i].Img_Cur = 71;
                    if(Weapon_Upgrade == 3)
                        Missiles[i].Img_Cur = 72;
                    if(Weapon_Upgrade == 4)
                        Missiles[i].Img_Cur = 80;
                    if(Weapon_Upgrade == 5)
                        Missiles[i].Img_Cur = 138;
                    Missiles[i].Speed.x = xspd;
                    Missiles[i].Speed.y = yspd;
                    Missiles[i].Pos.x = Pos.x;
                    Missiles[i].Pos.y = Pos.y;
                    Missiles[i].InUse = true;
                    Missiles[i].Damage = Damage;
                    MissileDelay = (int)MaxMissileDelay;
                    continue;
                }
                if(RocketDelay > 0)
                    continue;
                if(Upgrade > 0)
                {
                    if(Upgrade == 1 || Upgrade==2 || Upgrade==3)
                    {
                        Missiles[i].Img_Cur = 56;
                        Missiles[i].Damage = Damage / 2;
                    }
                    else if(Upgrade == 4 || Upgrade==5 || Upgrade==6)
                    {
                        Missiles[i].Img_Cur = 58;
                        Missiles[i].Damage = Damage;
                    }
                    else if(Upgrade == 7 || Upgrade==8 || Upgrade==9)
                    {
                        Missiles[i].Img_Cur = 60;
                        Missiles[i].Damage = Damage * 2;
                    }
                    else if(Upgrade == 10 || Upgrade==11 || Upgrade==12)
                    {
                        Missiles[i].Img_Cur = 63;
                        Missiles[i].Damage = Damage * 3;
                    }
                    else if( Upgrade >12 && Upgrade <21 )
                    {
                        Missiles[i].Img_Cur = 63;
                        Missiles[i].Damage = Damage * 4 + Upgrade;
                    }
                    else if( Upgrade >20 && Upgrade <41 )
                    {
                        Missiles[i].Img_Cur = 63;
                        Missiles[i].Damage = Damage * 4 + Upgrade;
                    }                 
                    else if( Upgrade >40 && Upgrade <351 )
                    {
                        Missiles[i].Img_Cur = 63;
                        Missiles[i].Damage = Damage * 4 + Upgrade*4;
                    }           					
					else if( Upgrade >350 )
                    {
                        Missiles[i].Img_Cur = 63;
                        Missiles[i].Damage = Damage*Upgrade;
                    }           					
					
					Missiles[i].Speed.x = 0.0F;
                    Missiles[i].Speed.y = 5F;
                    Missiles[i].Pos.x = Pos.x;
                    Missiles[i].Pos.y = Pos.y;
                    Missiles[i].Gravity.y = -0.6F;
                    Missiles[i].InUse = true;
                }
                RocketDelay = (int)MaxRocketDelay;
                i = Missiles.length;
            }

            return true;
        } 
		else
        {
            return false;
        }
    }
}
