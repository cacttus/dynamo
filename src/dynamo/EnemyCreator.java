package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EnemyCreator.java

//import java.applet.AudioClip;
import java.util.Random;

public class EnemyCreator
{

    public boolean Invincible;
    public int Room_Width, Room_Height;
    public EnemyCreator(int rw, int rh)
    {
        Room_Width = rw;
        Room_Height = rh;
        Num_Enemies = 0;
        EnemyTimer = 0;
        MaxEnemyTimer = 0;
        Num_Missiles = 2048;
        CanUpdate = true;
        CanMakeEnemies = true;
        v = new VECTOR2();
        Missiles = new Missile[Num_Missiles];
		Invincible = false;
	}

    public void Init(int Time, int N_Enemies, int PlayNumber)
    {
        CanMakeEnemies = CanUpdate = true;
        RAND = new Random();
        Time -= PlayNumber * 10;
        if(Time < 20)
            Time = 20;
        EnemyTimer = MaxEnemyTimer = Time;
        Num_Enemies = 0;
        Enemies = new EnemyShip[N_Enemies];
        for(int i = 0; i < Enemies.length; i++)
        {
            Enemies[i] = new EnemyShip();
            Enemies[i].Init(0);
        }
        for(int i = 0; i < Missiles.length; i++)
        {
            Missiles[i] = new Missile(Room_Width,Room_Height);
            Missiles[i].Init();
        }
    }

    public int Update(PlayerShip player, ParticleHandler phandler, PowerUp pu, int Level, int playnumber, MP3 explode, boolean playingaudio)
    {
        int RetScore = 0;
        boolean MakeEnemy = false;
        if(CanUpdate)
        {
            if(CanMakeEnemies)
            {
                if( Num_Enemies==0 )
                {
                    MakeEnemy=true;						
                    if(Level < 1000)
                        EnemyTimer = MaxEnemyTimer;
                    else
                        EnemyTimer = 20;
                }
                else
                {
                    EnemyTimer--;
                    if(EnemyTimer <= 0)
                    {
                        MakeEnemy = true;
                        if(Level < 1000)
                            EnemyTimer = MaxEnemyTimer;
                        else
                            EnemyTimer = 20;
                    }
                }
            }
			
            for(int i = 0; i < Enemies.length; i++)
            {
                int xx;
				
                // - Update enemies
                if( Enemies[i]!=null )
                {
                    if(Enemies[i].InUse)
                    {
                        Enemies[i].UpdateAnimation();

                        //#################################
                        // - Update enemy movement.
                        VECTOR2 vel = new VECTOR2();
                        vel.y = Enemies[i].Speed.y;
                        Enemies[i].move( vel );

                        //#################################
                        // - Perform individual enemy update.
                        v = Enemies[i].Update(player, phandler);	// - Returns the location that a player's missile hit the enemy at.
                        if(v.x != 0.0F || v.y != 0.0F)
                        {
                            for(int k = 0; k < pu.PowerUps.length; k++)
                            {
                                if(!pu.PowerUps[k].Active)
                                {
                                    pu.PowerUps[k].Active = true;
                                    pu.PowerUps[k].Create(23);
                                    pu.PowerUps[k].Pos.x = v.x;
                                    pu.PowerUps[k].Pos.y = v.y;
                                    k = pu.PowerUps.length;
                                }
                            }	
                        }
                        //################################
                        // - Create a drop.
                        if(Enemies[i].Health <= 0)
                        {
                            if( !Invincible )
                            {
                                if(playingaudio)
                                        explode.play();
                                RetScore += Enemies[i].Points;
                                Enemies[i].InUse = false;
                                phandler.Particles(30, Enemies[i].Pos, 0);
                                Num_Enemies--;
                                xx = -1;
                                xx = Math.abs(RAND.nextInt() % (20 + 10 * playnumber));
                                if(Level == 1000)
                                {
                                    for(int l = 0; l < 10; l++)
                                    {
                                        for(int k = 0; k < pu.PowerUps.length; k++)
                                            if(!pu.PowerUps[k].Active)
                                            {
                                                pu.PowerUps[k].Create(22);
                                                pu.PowerUps[k].Pos.x = Enemies[i].Pos.x + 16F + (float)(RAND.nextInt() % 100);
                                                pu.PowerUps[k].Pos.y = Enemies[i].Pos.y + 16F + (float)(RAND.nextInt() % 100);
                                                pu.PowerUps[k].Active = true;
                                                pu.PowerUps[k].Speed.y = (float)(RAND.nextInt() % 1000) * 0.01F;
                                                pu.PowerUps[k].Speed.x = (float)(RAND.nextInt() % 1000) * 0.01F;
                                                k = pu.PowerUps.length;
                                            }

                                    }

                                }
                                if(xx < 16)
                                {
                                    if(xx > 11 && xx < 16)
                                        xx = 6;
                                    else
                                    if(xx < 12)
                                        if(Level > 7)
                                            xx = Math.abs(RAND.nextInt() % 22);
                                        else
                                        if(Level > 5)
                                            xx = Math.abs(RAND.nextInt() % 18);
                                        else
                                        if(Level > 3)
                                            xx = Math.abs(RAND.nextInt() % 14);
                                        else
                                        if(Level > 1)
                                            xx = Math.abs(RAND.nextInt() % 10);
                                        else
                                            xx = Math.abs(RAND.nextInt() % 6);
                                    for(int k = 0; k < pu.PowerUps.length; k++)
                                    {
                                        if(!pu.PowerUps[k].Active)
                                        {
                                            pu.PowerUps[k].Active = true;
                                            pu.PowerUps[k].Create(xx);
                                            pu.PowerUps[k].Pos.x = Enemies[i].Pos.x;
                                            pu.PowerUps[k].Pos.y = Enemies[i].Pos.y;
                                            pu.PowerUps[k].Speed.y = 4F;
                                            k = pu.PowerUps.length;
                                        }
                                    }
                                }
                            }
                            else
                            {
                                Enemies[i].Health = Enemies[i].MaxHealth;
                            }
                        }
                        if(Enemies[i].MissileQueue > 0)
                        {
                            for(int k = 0; k < Missiles.length; k++)
                            {
                                if(Missiles[k].InUse)
                                    continue;
                                Enemies[i].MissileQueue--;
                                Missiles[k].Create(0);
                                Missiles[k].Gravity.x = Missiles[k].Gravity.y = 0.0F;
                                Missiles[k].InUse = true;
                                Missiles[k].Pos.x = Enemies[i].Pos.x;
                                Missiles[k].Pos.y = Enemies[i].Pos.y;
                                Missiles[k].creator=i;
                                if(Enemies[i].Type == 6)
                                {
                                    Missiles[k].Gravity.y = 1.2F;
                                    Missiles[k].Speed.y = -8F;
                                } 
                                else
                                {
                                    Missiles[k].Speed.y = 14F;
                                }
                                if(Enemies[i].Type == 8)
                                    Missiles[k].Speed.x = 3+RAND.nextInt() % 3;
                                else
                                    Missiles[k].Speed.x=0.0F;
                                Missiles[k].Damage = Enemies[i].Damage * (playnumber + 1);
                                Missiles[k].Img_Cur = Enemies[i].MissileImg;
                                if(Enemies[i].MissileQueue <= 0)
                                    k = Missiles.length;
                            }
                        }
                        if(Enemies[i].Pos.y > Room_Height+10)
                        {
                                Enemies[i].InUse = false;
                                Num_Enemies--;
                        }
                        if( Enemies[i].Pos.x > Room_Width-20 )
                                Enemies[i].Pos.x = Room_Width-20;
                        else if( Enemies[i].Pos.x < 16 )
                                Enemies[i].Pos.x = 16;
                        continue;
                    }// </update enemies>
                }
				
                if(!MakeEnemy)
                    continue;
				
                Enemies[i].init(0, 2, 4);

                //  - Initialize with the type of enemey.
                k = Math.abs(RAND.nextInt() % 2);

                if(Level == 1000)
                        k += 1000;
                else if(Level == 2000)
                        ;
                else
                        k += (Level - 1) * 2;

                // - Initialize the position of the enemy
                Enemies[i].Init(k);
                Enemies[i].Pos.x = (float)Math.abs(RAND.nextInt() % (Room_Width*100)) * 0.01F;
                Enemies[i].Pos.y = -32F;

                // - Set the speed to random.
                if(Level != 1000)
                        Enemies[i].Speed.y = 1.0F + (float)Math.abs(RAND.nextInt() % 300) * 0.01F;
                else
                {
                        Enemies[i].Speed.y = 1.0F + (float)Math.abs(RAND.nextInt() % 1000) * 0.01F;
                        Enemies[i].Speed.x = 1.0F + (float)Math.abs(RAND.nextInt() % 1000) * 0.01F;
                        Enemies[i].Type = 15;
                }

                // - Level 7 Enemies move sideways
                if(Enemies[i].Type == 7)
                    Enemies[i].Speed.x = -3 + RAND.nextInt() % 6;
                else
                    Enemies[i].Speed.x = 0.0F;

                Enemies[i].InUse = true;
                MakeEnemy = false;
                Num_Enemies++;
            }
			
			
            // - Update missiles.
            for(int i = 0; i < Missiles.length; i++)
            {
                if(!Missiles[i].InUse)
                    continue;
                Missiles[i].Speed.x += Missiles[i].Gravity.x;
                Missiles[i].Speed.y += Missiles[i].Gravity.y;
                Missiles[i].Pos.y += Missiles[i].Speed.y;
                Missiles[i].Pos.x += Missiles[i].Speed.x;
                if(Missiles[i].Pos.y > Room_Height)
                    Missiles[i].InUse = false;
                if(Missiles[i].Pos.y < 0.0F)
                    Missiles[i].InUse = false;
                if(Missiles[i].Pos.x < 0.0F)
                    Missiles[i].InUse = false;
                if(Missiles[i].Pos.x > Room_Width)
                    Missiles[i].InUse = false;
            }

        }
        return RetScore;
    }

    int Num_Enemies;
    int EnemyTimer;
    int MaxEnemyTimer;
    int Num_Missiles;
    boolean CanUpdate;
    boolean CanMakeEnemies;
    int k;
    VECTOR2 v;
    Random RAND;
    EnemyShip Enemies[];
    Missile Missiles[];
}
