package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EnemyShip.java

import java.util.Random;

public class EnemyShip extends Ship
{

    public EnemyShip()
    {
        RAND = new Random();
        Type = 0;
        TimeToMissile = 40;
        MaxTimeToMissile = 40;
        InUse = false;
        MissileQueue = 0;
        Damage = 0;
        MissileImg = 0;
        Points = 0;
    }

    public void Init(int type)
    {
        Type = type;
        InUse = false;
        switch(Type)
        {
        case 0: // lv1 ship1
        {
            Health = MaxHealth = 30;
            Tile_Offset = Tile_Current = 14;
            Damage = 5;
            TimeToMissile = MaxTimeToMissile = 50;
            MissileImg = 73;
            Points = 6;
            Num_Images = 2;
            weight = 0.01F;
			break;
        }

        case 1: // lv1 ship2
        {
            Health = MaxHealth = 50;
            Tile_Offset = Tile_Current = 16;
            TimeToMissile = MaxTimeToMissile = 50;
            Damage = 8;
            MissileImg = 74;
            Points = 9;
            Num_Images = 2;
			weight = 0.02F;
            break;
        }

        case 2: // lv2 ship1
        {
            Health = MaxHealth = 70;
            Tile_Offset = Tile_Current = 18;
            Damage = 19;
            TimeToMissile = MaxTimeToMissile = 70;
            MissileImg = 75;
            Points = 10;
            Num_Images = 2;
			weight = 0.03F;
            break;
        }

        case 3: // lv2 ship2'
        {
            Health = MaxHealth = 50;
            Tile_Offset = Tile_Current = 20;
            TimeToMissile = MaxTimeToMissile = 70;
            Damage = 26;
            MissileImg = 74;
            Points = 12;
			weight = 0.04F;
            break;
        }

        case 4: // lv3 ship1 
        {
            Health = MaxHealth = 100;
            Tile_Offset = Tile_Current = 22;
            Damage = 30;
            TimeToMissile = MaxTimeToMissile = 50;
            MissileImg = 73;
            Num_Images = 4;
            Anim_Speed = Max_Anim_Speed = 20;
            Points = 15;
			weight = 0.05F;
            break;
        }

        case 5: // lv3 ship 2
        {
            Health = MaxHealth = 110;
            Tile_Offset = Tile_Current = 28;
            Damage = 26;
            TimeToMissile = MaxTimeToMissile = 90;
            MissileImg = 75;
            Num_Images = 2;
            Points = 18;
			weight = 0.06F;
            break;
        }

        case 6: // lv4 ship 1
        {
            Health = MaxHealth = 150;
            Tile_Offset = Tile_Current = 30;
            Damage = 30;
            TimeToMissile = MaxTimeToMissile = 70;
            MissileImg = 69;
            Num_Images = 2;
            Points = 25;
			weight = 0.07F;
            break;
        }

        case 7: // lv4 ship 2 (ax)
        {
            Health = MaxHealth = 100;
            Tile_Offset = Tile_Current = 32;
            Damage = 6;
            TimeToMissile = MaxTimeToMissile = 19;
            MissileImg = 73;
            Num_Images = 2;
            Points = 22;
			weight = 0.08F;
            break;
        }

        case 8: // Ball Ship
        {
            Health = MaxHealth = 200;
            Tile_Offset = Tile_Current = 34;
            Damage = 40;
            TimeToMissile = MaxTimeToMissile = 100;
            MissileImg = 79;
            Num_Images = 2;
            Points = 30;
			weight = 0.09F;
            break;
        }

        case 9: // lv5 Ax-3 (white wing ship)
        {
            Health = MaxHealth = 250;
            Tile_Offset = Tile_Current = 36;
            Damage = 50;
            TimeToMissile = MaxTimeToMissile = 50;
            MissileImg = 52;
            Num_Images = 2;
            Points = 40;
			weight = 0.1F;
            break;
        }

        case 10: // lv 6 ship 1 (centrifuge)
        {
            Health = MaxHealth = 300;
            Tile_Offset = Tile_Current = 38;
            Damage = 60;
            TimeToMissile = MaxTimeToMissile = 50;
            MissileImg = 53;
            Num_Images = 2;
            Points = 50;
			weight = 0.11F;
            break;
        }

        case 11: // lv6 ship 2 Multiple beam guy ( with antenna)
        {
            Health = MaxHealth = 350;
            Tile_Offset = Tile_Current = 40;
            Damage = 65;
            TimeToMissile = MaxTimeToMissile = 50;
            int x = RAND.nextInt() % 2;
            if(x == 0)
                MissileImg = 54;
            if(x == 1)
                MissileImg = 55;
            Num_Images = 2;
            Points = 60;
			weight = 0.12F;
            break;
        }

        case 12: // lv 7 ship 1 (tiny)
        {
            Health = MaxHealth = 400;
            Tile_Offset = Tile_Current = 26;
            Damage = 70;
            TimeToMissile = MaxTimeToMissile = 50;
            MissileImg = 69;
            Num_Images = 2;
            Points = 70;
			weight = 0.13F;
            break;
        }

        case 13: // lv 7 ship 2 (crab, spider (blue ship) guy)
        {
            Health = MaxHealth = 500;
            Tile_Offset = Tile_Current = 180;
            Damage = 75;
            TimeToMissile = MaxTimeToMissile = 50;
            MissileImg = 200;
            Num_Images = 2;
            Points = 80;
			weight = 0.14F;
            break;
        }

        case 14: // lv 8 ship 1
        {
            Health = MaxHealth = 210;
            Tile_Offset = Tile_Current = 42;	// Bio-Prototype N~1
            Damage = 90;
            TimeToMissile = MaxTimeToMissile = 40;
            MissileImg = 44;
            Num_Images = 2;
            Points = 100;
			weight = 0.15F;
            break;
        }

        case 15: // lv 8 ship 2 Angel
        {
            Health = MaxHealth = 360;
            Tile_Offset = Tile_Current = 45;
            Damage = 120;
            TimeToMissile = MaxTimeToMissile = 80;
            MissileImg = 47;
            Num_Images = 2;
            Points = 200;
			weight = 0.16F;
            break;
        }

        case 1000: 
        {
			//???
            Health = MaxHealth = 500;
            Tile_Offset = Tile_Current = 100;
            Damage = 100;
            TimeToMissile = MaxTimeToMissile = 80;
            MissileImg = 105;
            Num_Images = 2;
            Points = 100;
            break;
        }

        default:
        {
            Health = MaxHealth = 1000;
            Tile_Offset = Tile_Current = 0;
            Damage = 5;
            TimeToMissile = MaxTimeToMissile = 1;
            MissileImg = 73;
            Points = 6;
            Num_Images = 2;
            break;
        }
        }
    }

    public VECTOR2 Update(PlayerShip player, ParticleHandler phandler)
    {
        VECTOR2 v = new VECTOR2();
        TimeToMissile--;
        if(TimeToMissile <= 0)
        {
            TimeToMissile = MaxTimeToMissile;
            if(Type == 8)
                MissileQueue++;
            MissileQueue++;
        }
        for(int i = 0; i < player.Missiles.length; i++)
        {
            if(!player.Missiles[i].InUse )
				continue;
			VECTOR2 v2 = new VECTOR2();
			if( !Collideable.collide(
			player.Missiles[i].boundCircle, 
			player.Missiles[i].Pos, 
			player.Missiles[i].Speed,
			boundCircle, 
			Pos,
			Speed,
			v2) )
                continue;
            Health -= player.Missiles[i].Damage;
            if(player.Missiles[i].Gravity.y == 0.0F)
            {
                phandler.Particles(4, player.Missiles[i].Pos, 0);
            } else
            {
                v.x = player.Missiles[i].Pos.x;
                v.y = player.Missiles[i].Pos.y;
            }
            player.Missiles[i].InUse = false;
        }

        return v;
    }

    Random RAND;
    int Type;
    int TimeToMissile;
    int MaxTimeToMissile;
    boolean InUse;
    int MissileQueue;
    int Damage;
    int MissileImg;
    int Points;
}
