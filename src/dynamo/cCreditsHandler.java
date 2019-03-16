package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   cCreditsHandler.java

import java.awt.Color;
import java.awt.Graphics;

public class cCreditsHandler
{

    int Num_Credits;
    String Strings[];
    int Images[];
    float Positions[];
    float Spd;
    boolean done;
    int CreateTimer;
    int MaxCreateTimer;
    int St_Num;
    int Cur_Num;
    int Room_Height;
    int Room_Width;
    cCreditsHandler(int rw, int rh)
    {
        Room_Height = rh;
        Room_Width = rw;
        
        Num_Credits = 21;
        Strings = new String[40];
        Images = new int[40];
        Positions = new float[40];
        Spd = 0.7F;
        done = false;
        CreateTimer = 120;
        MaxCreateTimer = 120;
        St_Num = 0;
        Cur_Num = 0;
		
        for(int i = 0; i < Strings.length; i++)
        {
            Strings[i] = new String();
            Positions[i] = Room_Height;
        }

        Strings[0] = "Credits";
        Strings[1] = "Cast ";
        Strings[2] = "Roger";
        Strings[3] = "Kelly";
        Strings[4] = "Vessel";
        Strings[5] = "Arms";
        Strings[6] = "Type-2";
        Strings[7] = "Pin";
        Strings[8] = "Ninja";
        Strings[9] = "Dephazer";
        Strings[10] = "Type-1";
        Strings[11] = "Ax";
        Strings[12] = "Gyroid";
        Strings[13] = "xA16-401";
        Strings[14] = "Centrifuge";
        Strings[15] = "Zoomer";
        Strings[16] = "Tiny";
        Strings[17] = "Bio";
        Strings[18] = "Angel";
		Strings[19] = "";
        Strings[20] = "The End";
        Images[0] = -1;
        Images[1] = -1;
        Images[2] = 0;
        Images[3] = 12;
        Images[4] = 14;
        Images[5] = 16;
        Images[6] = 18;
        Images[7] = 20;
        Images[8] = 22;
        Images[9] = 28;
        Images[10] = 30;
        Images[11] = 32;
        Images[12] = 34;
        Images[13] = 36;
        Images[14] = 38;
        Images[15] = 40;
        Images[16] = 26;
        Images[17] = 42;
        Images[18] = 45;
        Images[19] = -1;
		Images[20] = -1;
    }

	/**
	*	@fn RollCredits()
	*	@details Returns true when the credits are done.
	*	Rolls credits.
	*/
    boolean RollCredits(Graphics g)
    {
        g.setColor(Color.WHITE);
        if(!done)
        {
            CreateTimer--;
            if(CreateTimer <= 0)
			{
                if(Cur_Num == Num_Credits)
                {
                    done = true;
                } 
				else
                {
                    CreateTimer = MaxCreateTimer;
                    Positions[Cur_Num] = Room_Height;
                    Cur_Num++;
                }
			}
        }
		
        if( done && Positions[Cur_Num - 1] < -20F )
            return true;

		for(int i = 0; i < Cur_Num; i++)
            Positions[i] -= Spd;

        return false;
    }

}
