package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DRAWTILECLASS.java

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class DRAWTILECLASS
{

    public DRAWTILECLASS()
    {
    }

    public void DrawTile(Graphics g, Image image, ImageObserver o, int XPOS, int YPOS, int xpad, int ypad, 
            int TileOffset, int HTile)
    {
        int CurY = TileOffset / HTile;
        int CurX = TileOffset % HTile;
        CurX = CurX * xpad + 1 + CurX * 32;
        CurY = CurY * ypad + 1 + CurY * 32;
        g.drawImage(image, XPOS, YPOS, XPOS + 32, YPOS + 32, CurX, CurY, CurX + 32, CurY + 32, o);
    }
}
