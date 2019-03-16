package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Ship.java


public class Ship extends Collideable
{
    int Max_Anim_Speed;
    int Anim_Speed;
    int Num_Images;
    int Tile_Offset;
    int Tile_Current;
    int Health;
    int MaxHealth;
    VECTOR2 Pos;
    VECTOR2 Speed;
    BOUNDBOX BoundBox;
    VECTOR2 Gravity;
    Missile Missiles[];
	VECTOR2 lastVelocity;
	float	boundRadius;		// - The radius of the bound circle;
	float weight;	// - Used to calculate the amount of nockback in the Collide() function (1.0=Immoveable 0.0=FullNockback).  .. Not implemented
    public Ship()
    {
		super();
		boundCircle.setBoundRadius(10F);
        Max_Anim_Speed = 0;
        Anim_Speed = 0;
        Num_Images = 0;
        Tile_Offset = 0;
        Tile_Current = 0;
        Health = 10;
        MaxHealth = Health;
        Gravity = new VECTOR2();
		//lastVelocity = new VECTOR2();
		boundRadius = 4F;
		weight = 0.01f;
	}
	
	/**
	*	@fn move()
	*	@brief Moves the player along the input velocity vector.
	*/
    public void move( VECTOR2 velocity ) {
			this.Pos.plusEquals(velocity);
			this.lastVelocity=velocity;
	}
	
	/**
	*	@fn init()
	*	@brief Initialize.
	*/
    public void init(int TileOffset, int NumTiles, int cSpeed)
    {
        Missiles = new Missile[1000];
        Pos = new VECTOR2();
        Speed = new VECTOR2();
        BoundBox = new BOUNDBOX();
        BoundBox.P1.x = BoundBox.P1.y = 0.0F;
        BoundBox.P2.x = 32F;
        BoundBox.P2.y = 0.0F;
        BoundBox.P3.x = 0.0F;
        BoundBox.P3.y = 32F;
        BoundBox.P4.x = BoundBox.P4.y = 32F;
        Speed.equals(6F);
        Tile_Offset = Tile_Current = TileOffset;
        Num_Images = NumTiles;
        Anim_Speed = Max_Anim_Speed = cSpeed;
    }

    public void UpdateAnimation()
    {
        Anim_Speed--;
        if(Anim_Speed <= 0)
        {
            Anim_Speed = Max_Anim_Speed;
            Tile_Current++;
            if(Tile_Current > (Tile_Offset + Num_Images) - 1)
                Tile_Current = Tile_Offset;
        }
    }

    public boolean CollideBox(BOUNDBOX otherBB, VECTOR2 otherPOS)
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

    public void CheckIfOutside(int RoomWidth, int RoomHeight)
    {
        if(Pos.x < 16.0F)
            Pos.x = 16.0F;
        if(Pos.y < 16.0F)
            Pos.y = 16.0F;
        if(Pos.x > (float)(RoomWidth -16))
            Pos.x = RoomWidth - 16;
        if(Pos.y+32 > (float)(RoomHeight ))
            Pos.y = RoomHeight - 32;
    }

    public void Wrap(int RoomWidth, int RoomHeight)
    {
        if(Pos.x < 0.0F)
            Pos.x = RoomWidth;
        if(Pos.y < 0.0F)
            Pos.y = RoomHeight;
        if(Pos.x > (float)RoomWidth)
            Pos.x = 0.0F;
        if(Pos.y > (float)RoomHeight)
            Pos.y = 0.0F;
    }
}
