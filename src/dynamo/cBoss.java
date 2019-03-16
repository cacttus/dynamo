package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   cBoss.java

//import java.applet.AudioClip;
import java.util.Random;
import java.lang.Math;
//import java.applet.AudioClip;
import javax.swing.JOptionPane;
public class cBoss
{
    int Room_Width, Room_Height;
    VECTOR2 	Pos;
    VECTOR2 	Speed;
    Random 	RAND;
    boolean 	Done;
    boolean 	PlayedDestroy;
    int 	Num_Parts;
    Part 	Parts[];
    int 	Type;
    int 	Num_Attacks;
    int 	AttackTimers[];
    int 	MaxAttackTimers[];
    //boolean Lasering;
    Missile 	Missiles[];
    Missile 	Beam[];
	int 		GlobalAnimationTimer;
	//boolean LaserCharging;
	
	
    public class Part
    {

        public boolean Collide(BOUNDBOX otherBB, VECTOR2 otherPOS)
        {
            if(BoundBox.P1.plus(Pos).isGreaterThan(otherBB.P1.plus(otherPOS)) && BoundBox.P1.plus(Pos).isLessThan(otherBB.P4.plus(otherPOS)))
                return true;
            if(BoundBox.P2.plus(Pos).isGreaterThan(otherBB.P1.plus(otherPOS)) && BoundBox.P2.plus(Pos).isLessThan(otherBB.P4.plus(otherPOS)))
                return true;
            if(BoundBox.P3.plus(Pos).isGreaterThan(otherBB.P1.plus(otherPOS)) && BoundBox.P3.plus(Pos).isLessThan(otherBB.P4.plus(otherPOS)))
                return true;
            if(BoundBox.P4.plus(Pos).isGreaterThan(otherBB.P1.plus(otherPOS)) && BoundBox.P4.plus(Pos).isLessThan(otherBB.P4.plus(otherPOS)))
                return true;
            if(otherBB.P1.plus(otherPOS).isGreaterThan(BoundBox.P1.plus(Pos)) && otherBB.P1.plus(otherPOS).isLessThan(BoundBox.P4.plus(Pos)))
                return true;
            if(otherBB.P2.plus(otherPOS).isGreaterThan(BoundBox.P1.plus(Pos)) && otherBB.P2.plus(otherPOS).isLessThan(BoundBox.P4.plus(Pos)))
                return true;
            if(otherBB.P3.plus(otherPOS).isGreaterThan(BoundBox.P1.plus(Pos)) && otherBB.P3.plus(otherPOS).isLessThan(BoundBox.P4.plus(Pos)))
                return true;
            return otherBB.P4.plus(otherPOS).isGreaterThan(BoundBox.P1.plus(Pos)) && otherBB.P4.plus(otherPOS).isLessThan(BoundBox.P4.plus(Pos));
        }

        VECTOR2 Pos;
        int Health;
        boolean Active;
        boolean CanDestroy;
        int Tile_Offset;
        BOUNDBOX BoundBox;
		boolean Switch;
        Part()
        {
            super();
			Switch = false;
            Pos = new VECTOR2();
            Health = 1;
            Active = false;
            CanDestroy = false;
            Tile_Offset = 0;
            BoundBox = new BOUNDBOX();
            BoundBox.P1.x = BoundBox.P1.y = 0.0F;
            BoundBox.P2.x = 32F;
            BoundBox.P2.y = 0.0F;
            BoundBox.P3.x = 0.0F;
            BoundBox.P3.y = 32F;
            BoundBox.P4.x = BoundBox.P4.y = 32F;
        }
    }
	
	
    public static final int LASERWAIT_TIMER = 0;		// - Wait time for laser beam to charge
    public static final int MISSILESHOOT_TIMER = 1;		// - Wait time to shoot another missile.		
    public static final int LASERSHOOT_TIMER = 3;		// - Time that laser fires for.
    public static final int LASERCHARGE_TIMER = 2;		// - Timer used to charget the laser ( play laser animation ).
    public static final int COOLDOWN_TIMER = 4;			// - Timer used to cool down the ship after lasering.
    public static final int COOLUP_TIMER = 5;			// - Timer used to cool up the ship.

    int ANIMATION_LENGTH;		// - Speed of the ship animation
    public static final int ANIMATION_NUMFRAMES = 6;	// - The number of frames in an animation.

    public MP3 LongLaserClip;
    public MP3 DieSound;
    public MP3 bossWasHitSound;

    BossState	bossState;
	
	
    public cBoss(int rw, int rh)
    {
        Room_Width = rw; Room_Height = rh;
        ANIMATION_LENGTH=30;
        bossState = new BossState();
        bossState.set(BossState.ATTACKING);
        Pos = new VECTOR2();
        Speed = new VECTOR2();
        RAND = new Random();
        Done = false;
        PlayedDestroy = false;
        Num_Parts = 0;
        Parts = new Part[8];
        Type = -1;
        Num_Attacks = 0;
        //Lasering = false;
        Missiles = new Missile[50];
        Beam = new Missile[Room_Height/32]; 
        Speed.x = 5.5F;
        AttackTimers = new int[10];
        MaxAttackTimers = new int[10];
        GlobalAnimationTimer = ANIMATION_LENGTH;
        for(int i = 0; i < Parts.length; i++)
            Parts[i] = new Part();

        for(int i = 0; i < Missiles.length; i++)
        {
            Missiles[i] = new Missile(Room_Width,Room_Height);
            Missiles[i].Init();
        }

        for(int i = 0; i < Beam.length; i++)
        {
            Beam[i] = new Missile(Room_Width,Room_Height);
            Beam[i].Init();
        }

    }
	
	
    // - Method to manually stop the boss laser
    public void stopLasering(){
        //Lasering = false;
        AttackTimers[LASERWAIT_TIMER] = MaxAttackTimers[LASERWAIT_TIMER];
        Parts[PART_LASER].Tile_Offset = 49;
        Speed.x*=3;
        //LaserCharging = false;
    }
	
    /**
    *	@fn CreateMissile()
    *	@brief Creates a missile for the boss.
    */
    public void CreateMissile(VECTOR2 pPos, int bdamage, VECTOR2 bspeed, int bimage)
    {
        for(int i = 0; i < Missiles.length; i++)
	{
            if(!Missiles[i].InUse)
            {
                Missiles[i].InUse = true;
                Missiles[i].Pos.x = pPos.x;
                Missiles[i].Pos.y = pPos.y;
                Missiles[i].Damage = bdamage;
                Missiles[i].Speed.y = bspeed.y;
                Missiles[i].Speed.x = bspeed.x;
                Missiles[i].Img_Cur = Missiles[i].Img_Off = bimage;
                i = Missiles.length;
            }
	}
    }

    /**
    *	@fn Create()	
    *	@brief Initializes boss variables
    *	@param Level - The level of the boss, increases his health.
    */
    void Create(int type, int Level)
    {
	Level++;	// - Hack, should ++ it in the arg list.
        PlayedDestroy = false;
        Type = type;
	for(int i=0;i<Parts.length;++i)
            Parts[i].Active=false;
        
        switch(Type)
        {
        case 0: // '\0'
            Num_Parts = 4;
            Pos.x = 256F;
            Pos.y = 18.0F;
            Parts[PART_BODY].Health = 20 * Level;
            Parts[PART_LWING].Health = 8000 * Level;
            Parts[PART_RWING].Health = 8000 * Level;
            Parts[PART_LASER].Health = 16000 * Level;
            Parts[PART_BODY].Active = Parts[PART_LWING].Active = Parts[PART_RWING].Active = Parts[PART_LASER].Active = true;
            Parts[PART_BODY].Tile_Offset = 43;
            Parts[PART_BODY].CanDestroy = false;
            Parts[PART_LWING].Tile_Offset = 42;
            Parts[PART_LWING].CanDestroy = true;
            Parts[PART_RWING].Tile_Offset = 44;
            Parts[PART_RWING].CanDestroy = true;
            Parts[PART_LASER].Tile_Offset = 49;
            Parts[PART_LASER].CanDestroy = true;
            Num_Attacks = 2;
            AttackTimers[LASERWAIT_TIMER] = MaxAttackTimers[LASERWAIT_TIMER] = 800;
            AttackTimers[MISSILESHOOT_TIMER] = MaxAttackTimers[MISSILESHOOT_TIMER] = 45;
            AttackTimers[LASERCHARGE_TIMER] = MaxAttackTimers[LASERCHARGE_TIMER] = 30;
            AttackTimers[LASERSHOOT_TIMER] = MaxAttackTimers[LASERSHOOT_TIMER] = 350;
            AttackTimers[COOLDOWN_TIMER] = MaxAttackTimers[COOLDOWN_TIMER] = 200;
            AttackTimers[COOLUP_TIMER] = MaxAttackTimers[COOLUP_TIMER] = 60;
            break;

        case 1: // '\001'
            Num_Parts = 7;
            Pos.x = 256F;
            Pos.y = 1.0F;
			
            Parts[PART_HEAD0].Health = 100000 * Level;
            Parts[PART_HEAD1].Health = 100000 * Level;
            Parts[PART_HEAD2].Health = 100000 * Level;
            Parts[PART_HEAD3].Health = 100000 * Level;
            Parts[PART_MOUTH0].Health = 100000 * Level;
            Parts[PART_MOUTH1].Health = 100000 * Level;
            
            Parts[PART_HEAD0].Active = 
            Parts[PART_HEAD1].Active = 
            Parts[PART_HEAD2].Active = 
            Parts[PART_HEAD3].Active = 
            Parts[PART_MOUTH0].Active = 
            Parts[PART_MOUTH1].Active = true;

            Parts[PART_HEAD0].Tile_Offset = 154;
            Parts[PART_HEAD0].CanDestroy = false;
            Parts[PART_HEAD1].Tile_Offset = 155;
            Parts[PART_HEAD1].CanDestroy = true;
            Parts[PART_HEAD2].Tile_Offset = 156;
            Parts[PART_HEAD2].CanDestroy = true;
            Parts[PART_HEAD3].Tile_Offset = 157;
            Parts[PART_HEAD3].CanDestroy = true;
            Parts[PART_MOUTH0].Tile_Offset = 158;
            Parts[PART_MOUTH0].CanDestroy = true;
            Parts[PART_MOUTH1].Tile_Offset = 159;
            Parts[PART_MOUTH1].CanDestroy = true;
			
            Num_Attacks = 2;
            AttackTimers[LASERWAIT_TIMER] = MaxAttackTimers[LASERWAIT_TIMER] = 600;
            AttackTimers[MISSILESHOOT_TIMER] = MaxAttackTimers[MISSILESHOOT_TIMER] = 20;
            break;
        }
    }
	
    /**
    *	Update function that checks for collisions with player missiles, and updates the ship state, animation, etc.
    */
    public boolean Update(PlayerShip player, ParticleHandler phandler, MP3 BigLaser, PowerUp pu, MP3 Destroy, boolean UsingSFX)
    {
		
        if( Type==0 ) 
        {
            if( updateRegular(player, phandler, BigLaser, pu, Destroy, UsingSFX) ) 
            {
                return true;
            }
        }
        else if( Type==1 ) 
        { 
            // - Type 1 is a 'special' ship :)
            if( updateSpecial(player, phandler, BigLaser, pu, Destroy, UsingSFX) ) 
            {
                    return true;
            }
        }
        else 
        {
            java.lang.System.out.println("Failed to update...?");
        }
		
        for(int i = 0; i < Parts.length; i++)
        {
            if(!Parts[i].Active || !Parts[i].CanDestroy)
                continue;
			
            for(int j = 0; j < player.Missiles.length; j++)
			{
                if(player.Missiles[j].InUse && Parts[i].Collide(player.Missiles[j].BoundBox, player.Missiles[j].Pos))
                {
                    Parts[i].Health -= player.Missiles[j].Damage;
                    player.Missiles[j].InUse = false;
                    phandler.Particles(10, Parts[i].Pos, 0);
                    if( bossState.get()==BossState.COOLDOWN )
                    {
                        bossWasHitSound.play();
                        if( Type==0 )
                        {
                            if( i==PART_LASER )
                            {
                                Parts[i].Tile_Offset=245;
                                Parts[i].Switch=true;
                            }
                            else if( i== PART_LWING )
                            {
                                Parts[i].Tile_Offset=246;
                                Parts[i].Switch=true;
                            }
                            else if( i== PART_RWING )
                            {
                                Parts[i].Tile_Offset=248;
                                Parts[i].Switch=true;
                            }								
                            else if( i== PART_BODY )
                            {
                                Parts[i].Tile_Offset=247;
                                Parts[i].Switch=true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
	
	
	
    public static final int PART_BODY 	= 0;
    public static final int PART_LWING	= 1;
    public static final int PART_RWING 	= 2;
    public static final int PART_LASER 	= 3;
	
    /**
    *	@fn updateRegular
    *	@brief The update routine for the regular boss.
    *
    */
    private boolean updateRegular( PlayerShip player, ParticleHandler phandler, MP3 BigLaser, PowerUp pu, MP3 Destroy, boolean UsingSFX )
    {
            //#############################
            // - Update part positions and health.
            if(Parts[PART_BODY].Active)
            {
                Pos.x += this.Speed.x;
                if(Pos.x > (Room_Width-(512-448)) || Pos.x < 32f)
                    this.Speed.x = -this.Speed.x;
            }

            //#############################
            // - Update the position of the body first, then update the positions of all the rest of the parts.
            Parts[PART_BODY].Pos = Pos;
            Parts[PART_LWING].Pos.x = Parts[PART_BODY].Pos.x - 32F;
            Parts[PART_LWING].Pos.y = Parts[PART_BODY].Pos.y;
            Parts[PART_RWING].Pos.x = Parts[PART_BODY].Pos.x + 32F;
            Parts[PART_RWING].Pos.y = Parts[PART_BODY].Pos.y;
            Parts[PART_LASER].Pos.x = Parts[PART_BODY].Pos.x;
            Parts[PART_LASER].Pos.y = Parts[PART_BODY].Pos.y + 32F;

            for(int i = 0; i < Parts.length; i++)
            {
                if(Parts[i].Active && Parts[i].Health <= 0)
                {
                    Parts[i].Active = false;
                    phandler.Particles(10, Parts[i].Pos, 1);
                }
            }


            //#########################
            // - Update the Boss State.
            if( bossState.get() == BossState.LASERCHARGING )
            {
                Parts[PART_LASER].Tile_Offset = 
                        188 - ((int)( 5 * AttackTimers[LASERCHARGE_TIMER] / MaxAttackTimers[LASERCHARGE_TIMER] ));
                AttackTimers[LASERCHARGE_TIMER]--;

                if( AttackTimers[LASERCHARGE_TIMER]<=0 )
                {
                    bossState.set(BossState.LASERING);
                    Parts[PART_LASER].Tile_Offset = 189;
                    AttackTimers[LASERSHOOT_TIMER] = MaxAttackTimers[LASERSHOOT_TIMER]; // - A time of 100 for the laser beam.  
                    Speed.x/=3;
                    // - Enable all of the laser beam missile objects
                    for(int i = 0; i < Beam.length; i++)
                    {
                        Beam[i].InUse = true;
                        Beam[i].Pos.x = Pos.x;
                        Beam[i].Pos.y = 32F + (float)(i * 32);
                        Beam[i].Speed.x = 0.0F;
                        Beam[i].Speed.y = 0.0F;
                        Beam[i].Damage = 1;
                        if(i == 0)
                            Beam[i].Img_Off = 77;
                        else
                            Beam[i].Img_Off = 76;
                    }
                }
            }
            else if( bossState.get()==BossState.LASERING )
            {
                // - We are playing the laser beam.
                AttackTimers[LASERSHOOT_TIMER]--;
                if(AttackTimers[LASERSHOOT_TIMER] <= 0)
                {
                        Parts[PART_LASER].Tile_Offset = 49;
                        bossState.set(BossState.COOLDOWN);
                        AttackTimers[COOLDOWN_TIMER] = MaxAttackTimers[COOLDOWN_TIMER];
                        // - Divide the health to a reasonable percentage
                        for( int i=0; i<Parts.length; ++i )
                            Parts[i].Health/=20;

                        if( LongLaserClip != null )
                            LongLaserClip.stop();

                        for(int i = 0; i < Beam.length; i++)
                            Beam[i].InUse = false;			

                        ANIMATION_LENGTH*=2;
                } 
                else
                {

                    // - Update beam position
                    for(int i = 0; i < Beam.length; i++)
                    {
                        Beam[i].Pos.x = Pos.x-16;
                        Beam[i].Pos.y = 32F + (float)(i * 32);
                    }
                }	
            }
            else if( bossState.get()==BossState.COOLDOWN )
            {
                // - We finished lasering, now cooldown mode, and vulnerable to player attacks
                AttackTimers[COOLDOWN_TIMER]--;
                if( AttackTimers[COOLDOWN_TIMER] <=0 )
                {
                    AttackTimers[COOLUP_TIMER] = MaxAttackTimers[COOLUP_TIMER];
                    bossState.set(BossState.COOLUP);

                    for( int i=0; i<Parts.length; ++i )
                        if( Parts[i].Active )
                            Parts[i].Health*=20;
                }
                else
                {
                    // - Subtract from bossie's speed.
                    if( Speed.x<0.04F && Speed.x>-0.04F )
                        Speed.x=0.0F;
                    else if(Speed.x<0.0F)Speed.x+=0.03F;
                    else Speed.x-=0.03F;
                }
            }
            else if( bossState.get()==BossState.COOLUP )
            {
                // - Finished cooling down, now start to move and shoot again, at the player.
                AttackTimers[COOLUP_TIMER]--;
                if( AttackTimers[COOLUP_TIMER] <=0 )
                {
                    AttackTimers[LASERWAIT_TIMER] = MaxAttackTimers[LASERWAIT_TIMER];
                    AttackTimers[MISSILESHOOT_TIMER] = MaxAttackTimers[MISSILESHOOT_TIMER];				
                    bossState.set(BossState.ATTACKING);
                    ANIMATION_LENGTH/=2;
                }
                else
                {
                    // - Subtract from bossie's speed.
                    if( Speed.x!=0.0 )
                    {
                        if( Speed.x<0.0F )
                        {
                            Speed.x-=0.06F;
                            if( Speed.x<-5.5F )
                                Speed.x = -5.5F;
                        }
                        else if( Speed.x>0.0F )
                        {
                            Speed.x+=0.06F;
                            if( Speed.x>5.5F )
                                Speed.x=5.5F;
                        }
                    }
                    else
                        Speed.x=0.01F;
                }
            }
            else if( bossState.get()==BossState.ATTACKING )	
            {
                AttackTimers[LASERWAIT_TIMER]--;
                if(AttackTimers[LASERWAIT_TIMER] <= 0)
                {
                    if(Parts[PART_LASER].Active) // - We can only lase if the laser is not blown up.
                    {
                        AttackTimers[LASERCHARGE_TIMER] = MaxAttackTimers[LASERCHARGE_TIMER];
                        bossState.set(BossState.LASERCHARGING);
                        if( LongLaserClip != null )
                            LongLaserClip.play();
                        else
                            JOptionPane.showMessageDialog(null,"NULL Sound Reference" );

                    }
                    else
                    {
                        // - Do a cooldown anyway so the player can hit me.
                        bossState.set(BossState.COOLDOWN);
                        AttackTimers[COOLDOWN_TIMER] = MaxAttackTimers[COOLDOWN_TIMER];
                        // - Divide the health to a reasonable percentage
                        for( int i=0; i<Parts.length; ++i )
                            if( Parts[i].Active )
                                Parts[i].Health/=20;

                        ANIMATION_LENGTH*=2;
                    }
                }

                if( Parts[PART_LWING].Active || Parts[PART_RWING].Active )
                {
                    AttackTimers[MISSILESHOOT_TIMER]--;
                    if(AttackTimers[MISSILESHOOT_TIMER] <= 0)
                    {
                        AttackTimers[MISSILESHOOT_TIMER] = MaxAttackTimers[MISSILESHOOT_TIMER];

                        VECTOR2 Speed = new VECTOR2();

                        Speed.x = 0.0F;
                        Speed.y = 12F;

                        if(Parts[PART_LWING].Active)
                            CreateMissile(Parts[PART_LWING].Pos, 100, Speed, 190);
                        if(Parts[PART_RWING].Active)
                            CreateMissile(Parts[PART_RWING].Pos, 100, Speed, 190);

                    }
                }
            }

        //##############################
        // UPDATE PARTS ANIMATION

        // - Cycle the animation timer
        GlobalAnimationTimer++;
        if( GlobalAnimationTimer>=ANIMATION_LENGTH )	// - Important that it does not ever equal ANIMATION_LENGTH or another last frame would show for a second as a "blink".
            GlobalAnimationTimer = 0;

        int Cur_Frame = (int)
        ( 
            (
                (int)Math.floor
                (
                    (
                        ((float)GlobalAnimationTimer / (float)ANIMATION_LENGTH)
                        *(float)(ANIMATION_NUMFRAMES)
                    )
                ) 
                * 3
            )
        );

        // - LEFT WING
        if(!Parts[PART_LWING].Active)
            Parts[PART_LWING].Tile_Offset = 48; // Dead 
        else if( Parts[PART_LWING].Switch )
            Parts[PART_LWING].Switch=false;
        else
            Parts[PART_LWING].Tile_Offset = 201 + Cur_Frame;

        // - RIGHT WING
        if(!Parts[PART_RWING].Active)
            Parts[PART_RWING].Tile_Offset = 50; // Dead
        else if( Parts[PART_RWING].Switch )
            Parts[PART_RWING].Switch=false;
        else
            Parts[PART_RWING].Tile_Offset = 203 + Cur_Frame;

        // - LASER
        if(!Parts[PART_LASER].Active)
        {
            Parts[PART_LASER].Tile_Offset = 51;	// Dead
            if( bossState.get()==BossState.LASERING || bossState.get()==BossState.LASERCHARGING )
            {
                if( bossState.get()==BossState.LASERING )
                {
                    if( LongLaserClip != null )
                        LongLaserClip.stop();

                    for(int i = 0; i < Beam.length; i++)
                        Beam[i].InUse = false;

                    Speed.x*=3;
                }
                bossState.set(BossState.ATTACKING);

            }
        }
        else if( bossState.get()==BossState.COOLDOWN )
        {
            if( Parts[PART_LASER].Switch )
                Parts[PART_LASER].Switch = false;
            else
                Parts[PART_LASER].Tile_Offset = 49;
        }

        // - ACTIVATES DESTROY
        if(!Parts[PART_LWING].Active && !Parts[PART_RWING].Active && !Parts[PART_LASER].Active)
            Parts[PART_BODY].CanDestroy = true;

        // - BODY
        if(!Parts[PART_BODY].Active)
        {
            Parts[PART_BODY].Tile_Offset = 202;
            bossState.set(BossState.DYING);
            if(UsingSFX && !PlayedDestroy)
            {
                DieSound.play();
                PlayedDestroy = true;
                for(int k = 0; k < pu.PowerUps.length; k++)
                {
                    if(!pu.PowerUps[k].Active)
                    {
                        int xxx = RAND.nextInt() % 3;
                        xxx += 24;
                        pu.PowerUps[k].Active = true;
                        pu.PowerUps[k].Create(xxx);
                        pu.PowerUps[k].Pos.x = Pos.x;
                        pu.PowerUps[k].Pos.y = Pos.y;
                        k = pu.PowerUps.length;
                    }
                }
            }
            Pos.x += RAND.nextInt() % 2;
            Pos.y += 1.5F + Math.abs(RAND.nextInt() % 1);
            VECTOR2 rPOS = new VECTOR2();
            rPOS.x = (Pos.x + (float)Math.abs(RAND.nextInt() % 32)) - 10F;
            rPOS.y = (Pos.y + (float)Math.abs(RAND.nextInt() % 32)) - 10F;
            for(int k = 0; k < pu.PowerUps.length; k++)
            {
                if(!pu.PowerUps[k].Active)
                {
                    pu.PowerUps[k].Active = true;
                    pu.PowerUps[k].Create(23);
                    pu.PowerUps[k].Pos.x = rPOS.x;
                    pu.PowerUps[k].Pos.y = rPOS.y;
                    k = pu.PowerUps.length;
                }
            }
        }
        else // The ship is still active.
        {
            if( Parts[PART_BODY].Switch )
                Parts[PART_BODY].Switch = false;	// - This boolean allows a hit part to flash red for one frame.
            else
                Parts[PART_BODY].Tile_Offset = 202 + Cur_Frame;;
        }

            // - The ship will reach this position in the destroy sequence.
        if(Pos.y > 256F)
            return (Done=true) ;

        // - Update all boss missiles in the scene.
        for(int i = 0; i < Missiles.length; i++)
        {
            if(Missiles[i].InUse)
            {

                Missiles[i].Pos.x += Missiles[i].Speed.x;
                Missiles[i].Pos.y += Missiles[i].Speed.y;

                if(Missiles[i].Pos.y < 0.0F || Missiles[i].Pos.y > Room_Height || Missiles[i].Pos.x < 0.0F || Missiles[i].Pos.x > Room_Width)
                    Missiles[i].InUse = false;

                Missiles[i].Img_Off++;

                if( Missiles[i].Img_Off > 195 )
                        Missiles[i].Img_Off = 190;

            }
        }
        return false;
    }




    static final int PART_HEAD0 = 0;
    static final int PART_HEAD1 = 1;
    static final int PART_HEAD2 = 2;
    static final int PART_HEAD3 = 3;
    static final int PART_HEAD4 = 4;
    static final int PART_MOUTH0 = 5;
    static final int PART_MOUTH1 = 6;

    /**
    *	@fn updateSpecial()
    *	@brief Update the boss in special mode (the secret boss).
    */
    private boolean updateSpecial(PlayerShip player, ParticleHandler phandler, MP3 BigLaser, PowerUp pu, MP3 Destroy, boolean UsingSFX)
    {
            //############################3
            // - update part positions and health
        if(Parts[PART_HEAD0].Active)
        {
            Pos.x += this.Speed.x;
            if(Pos.x > 448f)
                this.Speed.x = -this.Speed.x;
            if(Pos.x < 32f)
                this.Speed.x = -this.Speed.x;
        }

        // - Update the position of the body first, then update the positions of all the rest of the parts.
        Parts[PART_HEAD0].Pos = Pos;
        Parts[PART_HEAD1].Pos.x = Parts[PART_HEAD0].Pos.x + 32F;
        Parts[PART_HEAD1].Pos.y = Parts[PART_HEAD0].Pos.y;
        Parts[PART_HEAD2].Pos.x = Parts[PART_HEAD0].Pos.x;
        Parts[PART_HEAD2].Pos.y = Parts[PART_HEAD0].Pos.y+32F;
        Parts[PART_HEAD3].Pos.x = Parts[PART_HEAD0].Pos.x+32F;
        Parts[PART_HEAD3].Pos.y = Parts[PART_HEAD0].Pos.y+32F;
        Parts[PART_MOUTH0].Pos.x = Parts[PART_HEAD0].Pos.x;
        Parts[PART_MOUTH0].Pos.y = Parts[PART_HEAD0].Pos.y+64F;		
        Parts[PART_MOUTH1].Pos.x = Parts[PART_HEAD0].Pos.x+32F;
        Parts[PART_MOUTH1].Pos.y = Parts[PART_HEAD0].Pos.y+64F;	

        for(int i = 0; i < Parts.length; i++)
        {
            if(Parts[i].Active && Parts[i].Health <= 0)
            {
                Parts[i].Active = false;
                Parts[i].Tile_Offset+=14;
                phandler.Particles(10, Parts[i].Pos, 1);
            }
        }


        //#########################
        // - Update the laserbeam code.
        if( bossState.get()==BossState.LASERCHARGING )
        {
            //Parts[PART_MOUTH1].Tile_Offset = 188 - ((int)( 5 * AttackTimers[LASERCHARGE_TIMER] / MaxAttackTimers[LASERCHARGE_TIMER] ));
            AttackTimers[LASERCHARGE_TIMER]--;

            if( AttackTimers[LASERCHARGE_TIMER]<=0 )
            {
                bossState.set(BossState.LASERING);

                AttackTimers[LASERSHOOT_TIMER] = MaxAttackTimers[LASERSHOOT_TIMER];		// - A time of 100 for the laser beam.  
                Speed.x/=3;
                // - Enable all of the laser beam missile objects
                for(int i = 0; i < Beam.length; i++)
                {
                    Beam[i].InUse = true;
                    Beam[i].Pos.x = Pos.x;
                    Beam[i].Pos.y = 32F + (float)(i * 32);
                    Beam[i].Speed.x = 0.0F;
                    Beam[i].Speed.y = 0.0F;
                    Beam[i].Damage = 1;
                    if(i == 0)
                        Beam[i].Img_Off = 77;
                    else
                        Beam[i].Img_Off = 76;
                }
            }
        }
        else if( bossState.get()==BossState.LASERING )
        {
            // - We are playing the laser beam.
            AttackTimers[LASERSHOOT_TIMER]--;
            if(AttackTimers[LASERSHOOT_TIMER] <= 0)
            {
                AttackTimers[LASERWAIT_TIMER] = MaxAttackTimers[LASERWAIT_TIMER];
                Speed.x*=3;
                if(Parts[PART_MOUTH1].Active==true)
                Parts[PART_MOUTH0].Tile_Offset -=8;
                Parts[PART_MOUTH1].Tile_Offset -=8;

                if( LongLaserClip != null )
                    LongLaserClip.stop();

                for(int i = 0; i < Beam.length; i++)
                    Beam[i].InUse = false;

                bossState.set(BossState.ATTACKING);
            } 
            else
            {
                for(int i = 0; i < Beam.length; i++)
                {
                    Beam[i].Pos.x = Pos.x+9;
                    Beam[i].Pos.y = 32F + (float)(i * 32);
                }
            }	
        }
        else	// - Not lasering, just firing missiles.
        {

            if(Parts[PART_MOUTH1].Active) // - We can only lase if the laser is not blown up.
            {
                AttackTimers[LASERWAIT_TIMER]--;
                if(AttackTimers[LASERWAIT_TIMER] <= 0)
                {
                    AttackTimers[LASERCHARGE_TIMER] = MaxAttackTimers[LASERCHARGE_TIMER];
                    bossState.set(BossState.LASERCHARGING);
                    Parts[PART_MOUTH0].Tile_Offset +=8;
                    Parts[PART_MOUTH1].Tile_Offset +=8;

                    if( LongLaserClip != null )
                            LongLaserClip.play();
                    else
                        java.lang.System.out.println("Null Sound Reference.");
                }
            }

            if( Parts[PART_HEAD0].Active || Parts[PART_HEAD1].Active )
            {
                AttackTimers[MISSILESHOOT_TIMER]--;
                if(AttackTimers[MISSILESHOOT_TIMER] <= 0)
                {
                    AttackTimers[MISSILESHOOT_TIMER] = MaxAttackTimers[MISSILESHOOT_TIMER];
                    VECTOR2 Speed = new VECTOR2();
                    Speed.x = 0.0F;
                    Speed.y = 12F;
                    if(Parts[PART_HEAD0].Active)
                        CreateMissile(Parts[PART_HEAD0].Pos, 100, Speed, 190);
                    if(Parts[PART_HEAD1].Active)
                        CreateMissile(Parts[PART_HEAD1].Pos, 100, Speed, 190);
                }
            }
        }

        //##############################
        // UPDATE PARTS ANIMATION
        // - Cycle the animation timer
        GlobalAnimationTimer++;
        if( GlobalAnimationTimer>=ANIMATION_LENGTH )	// - Important that it does not ever equal ANIMATION_LENGTH or another last frame would show for a second as a "blink".
                GlobalAnimationTimer = 0;

        int Cur_Frame = (int)
        ( 
            (
                (int)Math.floor
                (
                    (
                        ((float)GlobalAnimationTimer / (float)ANIMATION_LENGTH)
                        *(float)(ANIMATION_NUMFRAMES)
                    )
                ) 
                * 3
            )
        );

        // - ACTIVATES DESTROY
        if( 
        !Parts[PART_HEAD1].Active 	&& 
        !Parts[PART_HEAD2].Active	&& 	
        !Parts[PART_HEAD3].Active 	&& 
        !Parts[PART_MOUTH0].Active 	&& 
        !Parts[PART_MOUTH1].Active 
        )
            Parts[PART_HEAD0].CanDestroy = true;

        // - BODY
        if( Parts[PART_HEAD0].CanDestroy)
        if(!Parts[PART_HEAD0].Active)
        {
            if(UsingSFX && !PlayedDestroy)
            {
                if( DieSound != null )
                    DieSound.play();
                else
                    java.lang.System.out.println("Null sound 'diesound' " );
                PlayedDestroy = true;
                for(int k = 0; k < pu.PowerUps.length; k++)
                {
                    if(!pu.PowerUps[k].Active)
                    {
                        int xxx = RAND.nextInt() % 3;
                        xxx += 24;
                        pu.PowerUps[k].Active = true;
                        pu.PowerUps[k].Create(xxx);
                        pu.PowerUps[k].Pos.x = Pos.x;
                        pu.PowerUps[k].Pos.y = Pos.y;
                        k = pu.PowerUps.length;
                    }
                }
            }
            Pos.x += RAND.nextInt() % 2;
            Pos.y += 1 + Math.abs(RAND.nextInt() % 1);
            VECTOR2 rPOS = new VECTOR2();
            rPOS.x = (Pos.x + (float)Math.abs(RAND.nextInt() % 32)) - 10F;
            rPOS.y = (Pos.y + (float)Math.abs(RAND.nextInt() % 32)) - 10F;
            for(int k = 0; k < pu.PowerUps.length; k++)
            {
                if(!pu.PowerUps[k].Active)
                {
                    pu.PowerUps[k].Active = true;
                    pu.PowerUps[k].Create(23);
                    pu.PowerUps[k].Pos.x = rPOS.x;
                    pu.PowerUps[k].Pos.y = rPOS.y;
                    k = pu.PowerUps.length;
                }
            }
        }

        // - The ship will reach this position in the destroy sequence.
        if(Pos.y > Room_Height/2)
            return (Done=true) ;

        // - Update all boss missiles in the scene.
        for(int i = 0; i < Missiles.length; i++)
        {
            if(Missiles[i].InUse)
            {

                Missiles[i].Pos.x += Missiles[i].Speed.x;
                Missiles[i].Pos.y += Missiles[i].Speed.y;

                if(Missiles[i].Pos.y < 0.0F || 
                Missiles[i].Pos.y > Room_Height || 
                Missiles[i].Pos.x < 0.0F || 
                Missiles[i].Pos.x > Room_Height)
                    Missiles[i].InUse = false;

                Missiles[i].Img_Off++;

                if( Missiles[i].Img_Off > 195 )
                    Missiles[i].Img_Off = 190;

            }
        }
        return false;
    }


}
