package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Missile.java

import java.util.Random;
public class Missile extends Collideable
{
    int Room_Width;
    
    public Missile(int rw, int rh)
    {
        Room_Width=  rw;
        Type = 0;
        Damage = 0;
        MissileID = -1;
        InUse = false;
        t=0.0f;
        State=0;
    }

    public void Init()
    {
        Speed = new VECTOR2();
        Gravity = new VECTOR2();
        Pos = new VECTOR2();
        BoundBox = new BOUNDBOX();
        InUse = false;
        BoundBox.P1.x = BoundBox.P1.y = 0.0F;
        BoundBox.P2.x = 32F;
        BoundBox.P2.y = 0.0F;
        BoundBox.P3.x = 0.0F;
        BoundBox.P3.y = 32F;
        BoundBox.P4.x = BoundBox.P4.y = 32F;
		t=0.0f;
	}

    public void SetHeatSinkKeys( VECTOR2 ShipPos, VECTOR2 EnemyPos )
    {
        Random RAND = new Random();

        Keys = new VECTOR2[4];
        for( int i=0; i<4; ++i )
                Keys[i] = new VECTOR2();

        // - Create the beginning and end keys, and have a set of random keys inbetween.
        Keys[0] = ShipPos;
        Keys[1].x = RAND.nextFloat()*Room_Width;
        Keys[1].y = ShipPos.y;
        Keys[2].x = RAND.nextFloat()*Room_Width;
        Keys[2].y = EnemyPos.y;
        Keys[3] = EnemyPos;
        State=0;
    }
    // public void rv( VECTOR2 v, Random r )
    // {
            // v.x = 16F + r.nextFloat()*480F;
            // v.y = 16F + r.nextFloat()*480F;
    // }
    public void Create(int type)
    {
        Type = type;
        switch(Type)
        {
        case 0: // '\0'
            Img_Cur = Img_Off = 70;
            Img_Num = 1;
            break;
        }
    }

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
	
    // heat sink vars
    float t;
    VECTOR2 Keys[];
    int State;

    // other vars
    VECTOR2 Pos;
    BOUNDBOX BoundBox;
    VECTOR2 Speed;
    VECTOR2 Gravity;
    int Type;
    int Damage;
    int MissileID;
    int Img_Cur;
    int Img_Off;
    int Img_Num;
    boolean InUse;
    int creator;	// - Index into the array of enemies of the enemy controller, of who created the missile.
}
