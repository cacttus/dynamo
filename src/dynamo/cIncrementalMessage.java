package dynamo;

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   cIncramentalMessage.java

import java.awt.Color;
import java.awt.Graphics;

public class cIncrementalMessage
{
    int Timer;	// - This is not actually used in this class, but is used by the game.. to do some wait processing.

    boolean lineWaiting;	// - Lines stay a bit longer, so this says that we are waiting for the line timer.
	
	int nextCharTimer;		// - frames until the next char is displayed
	int maxNextCharTimer;
	int nextLineTimer;		// - frames until the next line is displayed
	int maxNextLineTimer;
	
	int charPos;		// - Position in the string that we are printing.

	float mySpeed;			// - The speed at which the messages run.
	
	String topLine;
	String botLine;	// - The two lines of text.
	
	boolean canPrint;	// - If we are done waiting, and can print the next character.
		
	static int maxCharsPerLine = 35;
	
    public cIncrementalMessage()
    {
		NewMessage(0F);
	}

	/**
	*	@fn NewMessage
	*	@brief Resets the messages
	*/
    public void NewMessage(float Speed)
    {
		mySpeed = Speed;

		nextCharTimer = maxNextCharTimer = 2;
		nextLineTimer = maxNextLineTimer = 20;
		Timer=-1;
		
		charPos = 0;
		
		topLine=" ";
		botLine=" ";
		
		Timer=-1;
		lineWaiting=false;
		canPrint=true;
	}

	/**
	*	@fn Message()
	*	@brief Returns true or false corresponding to the message being printed.
	*	All messages being printed are delimited with semicolons ';'
	*	ex:
	*		string s = "dude;this is a separate line;so is this;";
	*/
    public boolean Message(float x, float y, Graphics g, String s)
    {
        g.setColor(Color.WHITE);

		if( lineWaiting )
		{
			if( --nextLineTimer <=0 )
			{
				canPrint=true;
				lineWaiting=false;
				topLine = botLine;
				botLine = " ";
			}
		}
		else if( !canPrint )
		{
			if( --nextCharTimer <=0 )
			{
				canPrint=true;
			}
		}
		else
		{

			if( charPos ==s.length() )
			{
				// - we are done printing
				return true;
			}
			else if( s.charAt(charPos) == ';' )
			{
				// - We need to go to the next line anyway.
				//botLine = s.charAt(charPos);	// don't print the ;
				// - Put ourselves in a wait for this line.
				lineWaiting=true;
				canPrint=false;
				nextLineTimer = maxNextLineTimer;
			}
			else
			{
				// - Simply append to this line, or start on the second line if we need a line break
				if( botLine.length() == maxCharsPerLine )
				{
					topLine = botLine;
					botLine = " ";
				}
				botLine += s.charAt(charPos);
			}
			++charPos;
			canPrint=false;
			nextCharTimer = maxNextCharTimer;
		}
		
	    g.drawString(topLine, (int)x, (int)y);
        g.drawString(botLine, (int)x, (int)y + 17);
        
		return false;

    }
}
