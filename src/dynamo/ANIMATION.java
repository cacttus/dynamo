package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ANIMATION.java


public class ANIMATION
{

    public ANIMATION()
    {
        Tile_Offset = 0;
        Tile_Count = 0;
        MaxAnim_Speed = 10;
        Anim_Speed = MaxAnim_Speed = 10;
        Active = false;
        Looping = false;
    }

    public void Init(int Offset, int Speed, int Image_Count)
    {
        Pos = new VECTOR2();
        Anim_Speed = MaxAnim_Speed = Speed;
        Tile_Offset = Tile_Cur = Offset;
        Tile_Count = Image_Count;
    }

    public void Update()
    {
        Anim_Speed--;
        if(Anim_Speed <= 0)
        {
            Anim_Speed = MaxAnim_Speed;
            Tile_Cur++;
            if(Tile_Cur - Tile_Offset > Tile_Count)
                if(!Looping)
                    Active = false;
                else
                    Tile_Cur = Tile_Offset;
        }
    }

    int Tile_Offset;
    int Tile_Cur;
    int Tile_Count;
    int MaxAnim_Speed;
    int Anim_Speed;
    boolean Active;
    VECTOR2 Pos;
    boolean Looping;
}
