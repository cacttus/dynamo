package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParticleHandler.java

import java.util.Random;

public class ParticleHandler
{

    public ParticleHandler()
    {
        Num_Particles = 5000;
        Colors = new int[Num_Particles];
        crazyparticles = false;
    }

    public void Init()
    {
        RAND = new Random();
        Pos = new VECTOR2[Num_Particles];
        Speed = new VECTOR2[Num_Particles];
        Gravity = new VECTOR2[Num_Particles];
        Active = new boolean[Num_Particles];
        Life = new int[Num_Particles];
        Type = new int[Num_Particles];
        for(int i = 0; i < Num_Particles; i++)
        {
            Active[i] = false;
            Pos[i] = new VECTOR2();
            Gravity[i] = new VECTOR2();
            Speed[i] = new VECTOR2();
            Colors[i] = 0;
            Life[i] = 0;
            Type[i] = 0;
            Speed[i].x = (float)(-5000 + RAND.nextInt() % 10000) * 0.001F;
            Speed[i].y = (float)(-5000 + RAND.nextInt() % 10000) * 0.001F;
            Colors[i] = Math.abs(RAND.nextInt() % 6);
        }

    }

    public void Update()
    {
        for(int i = 0; i < Num_Particles; i++)
        {
            if(!Active[i])
                continue;
            Speed[i].x += Gravity[i].x;
            Speed[i].y += Gravity[i].y;
            Pos[i].x += Speed[i].x;
            Pos[i].y += Speed[i].y;
            Life[i]--;
            if(Life[i] <= 0)
                Active[i] = false;
        }

    }

    public void Particles(int num, VECTOR2 pos, int type)
    {
        float ang,spd;
        for(int i = 0; i < Num_Particles; i++)
        {
            if(Active[i])
                continue;
            
            // - Create random float
            if(crazyparticles)
            {
                Gravity[i].x = (float)(-500 + RAND.nextInt(1000)) * 0.001F;
                Gravity[i].y = (float)(-500 + RAND.nextInt(1000)) * 0.001F;
            }
			
            ang=RAND.nextFloat()*(6.28F);
            spd=RAND.nextFloat()*10F;
			
            Speed[i].x = (float)java.lang.Math.cos(ang)*spd;
            Speed[i].y = (float)java.lang.Math.sin(ang)*spd;
			
            //Speed[i].x = (float)(-5000 + RAND.nextInt() % 10000) * 0.001F;
            //Speed[i].y = (float)(-5000 + RAND.nextInt() % 10000) * 0.001F;
            Life[i] = 1 + Math.abs(RAND.nextInt(40));
            Type[i] = type;
            Pos[i].x = pos.x;
            Pos[i].y = pos.y;
            Active[i] = true;
            if(--num <= 0)
                i = Num_Particles;
        }

    }

    int Num_Particles;
    VECTOR2 Pos[];
    VECTOR2 Speed[];
    VECTOR2 Gravity[];
    int Colors[];
    int Type[];
    boolean Active[];
    int Life[];
    Random RAND;
    boolean crazyparticles;
}
