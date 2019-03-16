package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PowerUp.java

import java.util.Random;

public class PowerUp 
{
    public class PwrUp extends Collideable
    {
        VECTOR2 Pos;
        VECTOR2 Speed;
        int Cur_Img;
        int Img_Off;
        int Num_Img;
        int Img_Spd;
        int Img_MSpd;
        int Type;
        boolean Active;
        int Room_Width, Room_Height;
        
        public PwrUp(int rw, int rh)
        {
            super();
            Room_Width = rw;
            Room_Height = rh;
            Cur_Img = 0;
            Img_Off = 0;
            Num_Img = 0;
            Img_Spd = 0;
            Img_MSpd = 0;
            Type = 0;
            Active = false;
            boundCircle.setBoundRadius(16F);
        }
		
        public void Init()
        {
            Pos = new VECTOR2();
            Speed = new VECTOR2();
        }

        public void Create(int type)
        {
            Type = type;
            Num_Img = 0;
            Img_Spd = Img_MSpd = 1;
            Img_Off = Cur_Img = 0;
            switch(type)
            {
            case 0: // '\0'
                Active = false;
                break;

            case 1: // '\001'
                Img_Off = Cur_Img = 112;
                break;

            case 2: // '\002'
                Img_Off = Cur_Img = 113;
                break;

            case 3: // '\003'
                Img_Off = Cur_Img = 114;
                break;

            case 4: // '\004'
                Active = false;
                break;

            case 5: // '\005'
                Active = false;
                break;

            case 6: // '\006'
                Img_Off = Cur_Img = 120;
                break;

            case 7: // '\007'
                Img_Off = Cur_Img = 115;
                break;

            case 8: // '\b'
                Img_Off = Cur_Img = 129;
                break;

            case 9: // '\t'
                Img_Off = Cur_Img = 133;
                break;

            case 10: // '\n'
                Img_Off = Cur_Img = 121;
                break;

            case 11: // '\013'
                Img_Off = Cur_Img = 126;
                break;

            case 12: // '\f'
                Img_Off = Cur_Img = 130;
                break;

            case 13: // '\r'
                Img_Off = Cur_Img = 134;
                break;

            case 14: // '\016'
                Img_Off = Cur_Img = 122;
                break;

            case 15: // '\017'
                Img_Off = Cur_Img = 137;
                break;

            case 16: // '\020'
                Img_Off = Cur_Img = 131;
                break;

            case 17: // '\021'
                Img_Off = Cur_Img = 135;
                break;

            case 18: // '\022'
                Img_Off = Cur_Img = 123;
                break;

            case 19: // '\023'
                Img_Off = Cur_Img = 127;
                break;

            case 20: // '\024'
                Img_Off = Cur_Img = 132;
                break;

            case 21: // '\025'
                Img_Off = Cur_Img = 136;
                break;

            case 22: // '\026'
                Img_Off = Cur_Img = 106;
                Num_Img = 3;
                Img_Spd = Img_MSpd = 1;
                break;

            case 23: // '\027'
                Img_Off = Cur_Img = 92;
                Num_Img = 7;
                Img_Spd = Img_MSpd = 3;
                break;

            case 24: // '\030'
                Img_Off = Cur_Img = 110;
                break;

            case 25: // '\031'
                Img_Off = Cur_Img = 111;
                break;

            case 26: // '\032'
                Img_Off = Cur_Img = 124;
                break;

            case 27: // '\033'
                Img_Off = Cur_Img = 125;
                break;

            default:
                Active = false;
                break;
            }
        }


    }


    public PowerUp(int rw, int rh)
    {
        Room_Width = rw;
        Room_Height = rh;
        BoundBox = new BOUNDBOX();
        Timer = -1;
        MaxTimer = -1;
        RAND = new Random();
    }

    public void Init()
    {
        BoundBox.P1.x = BoundBox.P1.y = 0.0F;
        BoundBox.P2.x = 32F;
        BoundBox.P2.y = 0.0F;
        BoundBox.P3.x = 0.0F;
        BoundBox.P3.y = 32F;
        BoundBox.P4.x = BoundBox.P4.y = 32F;
        PowerUps = new PwrUp[500];
        for(int i = 0; i < PowerUps.length; i++)
        {
            PowerUps[i] = new PwrUp(Room_Width, Room_Height);
            PowerUps[i].Init();
        }

    }

    public void Update()
    {
        if(Timer <= 0 && Timer != -1)
        {
            Timer = MaxTimer;
            int b = Math.abs(RAND.nextInt() % 20);
            if(b < 7)
            {
                for(int i = 0; i < PowerUps.length; i++)
                    if(!PowerUps[i].Active)
                    {
                        PowerUps[i].Create(b);
                        PowerUps[i].Pos.y = 0.0F;
                        PowerUps[i].Active = true;
                        PowerUps[i].Speed.y = 4F;
                        i = PowerUps.length;
                    }

            }
        }
        for(int i = 0; i < PowerUps.length; i++)
        {
            if(!PowerUps[i].Active)
                continue;
            PowerUps[i].Img_Spd--;
            if(PowerUps[i].Img_Spd <= 0)
            {
                PowerUps[i].Img_Spd = PowerUps[i].Img_MSpd;
                PowerUps[i].Cur_Img++;
                if(PowerUps[i].Cur_Img > PowerUps[i].Num_Img + PowerUps[i].Img_Off)
                    if(PowerUps[i].Type == 23)
                        PowerUps[i].Active = false;
                    else
                        PowerUps[i].Cur_Img = PowerUps[i].Img_Off;
            }
            PowerUps[i].Pos.x += PowerUps[i].Speed.x;
            PowerUps[i].Pos.y += PowerUps[i].Speed.y;
            if(PowerUps[i].Pos.y > Room_Height || 
                    PowerUps[i].Pos.x > Room_Width || 
                    PowerUps[i].Pos.y < -32F || 
                    PowerUps[i].Pos.x < -32F)
                PowerUps[i].Active = false;
        }

    }

    BOUNDBOX BoundBox;
    int Timer;
    int MaxTimer;
    Random RAND;
    PwrUp PowerUps[];
    int Room_Width, Room_Height;
}
