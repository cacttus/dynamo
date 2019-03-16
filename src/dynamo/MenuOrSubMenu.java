package dynamo;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Avicenna
 */
public class MenuOrSubMenu
{
    
    
    public ArrayList _ar = new ArrayList();
    public VECTOR2 _pos = new VECTOR2();
    public String _str;
    public int _indent;
    public int _charYSpacing;
    MenuOrSubMenu _cursorIndex;// - where the cursor is
    MenuOrSubMenu _menuLevel;   // - The menu item that contains the given level.
    public MenuOrSubMenu _parentMenuItem; // - Allows us to navigate back up the menu tree.
    public boolean _visible;    // - Visible is also enabled.  We skip input and rendering on disabled items.
    public String _name = new String();
    
    float _cursorWobble=0F;
    float _cursorWobbleAdd=0.1F;
    float _maxCursorWobble=4F;

    // - Advances one level in the menu.
    public void pushMenuLevelForSelectedMenuItem()
    {
        if(_cursorIndex._ar.size()==0)
        {
            System.out.println(" [CLOG] Failed to push menu level - the given sub-item has no children.");
            return;
        }
        _menuLevel = _cursorIndex;
        _cursorIndex = (MenuOrSubMenu)_menuLevel._ar.get(0);
    }
    public void popMenuLevel()
    {
        _menuLevel = _menuLevel._parentMenuItem;
    }
    // - Tells you what menu item is currently under selection.
    public MenuOrSubMenu getMenuItemAtCursorPosition()
    {
        return _cursorIndex;
    }
    public String getMenuItemNameAtCursorPosition()
    {
        return _cursorIndex._name;
    }
    public void advanceCursorDown()
    {
        while(true)
        {
            int ind = _menuLevel._ar.indexOf(_cursorIndex);
            ind++;
            if(ind>=_menuLevel._ar.size())
                ind=0;
            _cursorIndex = (MenuOrSubMenu)_menuLevel._ar.get(ind);
            if(_cursorIndex._visible)
                return;
        }
        
    }
    public void advanceCursorUp()
    {
        while(true)
        {
            int ind = _menuLevel._ar.indexOf(_cursorIndex);
            ind--;
            if(ind<0)
                ind=_menuLevel._ar.size()-1;
            _cursorIndex = (MenuOrSubMenu)_menuLevel._ar.get(ind);
            if(_cursorIndex._visible)
                return;
        }
    }

    // get the cursor position
    public VECTOR2 getCursorPosition()
    {
        VECTOR2 r = new VECTOR2();
        if(_cursorIndex==null)
        {
            return r;
        }
        
        // - Make the cursor wobble.
        _cursorWobble+=_cursorWobbleAdd;
        if(_cursorWobbleAdd <0 && _cursorWobble <= -_maxCursorWobble)
            _cursorWobbleAdd=-_cursorWobbleAdd;
        else if(_cursorWobbleAdd>0 && _cursorWobble >= _maxCursorWobble)
            _cursorWobbleAdd=-_cursorWobbleAdd;
        
        r.x = _cursorIndex._pos.x - 29 + (int)_cursorWobble;
        r.y = _cursorIndex._pos.y-22;

        return r;
    }
    public void findItemByName_r(String n,MenuOrSubMenu[] m)
    {
        if(_name.equals(n))
        {
            m[0] = this;
            return;
        }
        for(int i=0; i<_menuLevel._ar.size(); ++i)
        {
            ((MenuOrSubMenu)_menuLevel._ar.get(i)).findItemByName_r(n,m);
        }
    }
    public void enableItemByName(String n) 
    {
        MenuOrSubMenu[] found = { null };
        findItemByName_r(n,found);
        if(found[0]==null)
        {
            System.out.println("Could not find menu item. "+ n);
            return;
        }
        if(found[0]._visible)
            return;
        
        found[0]._visible = true;
        
        //adjustPositionsForNewItem(found);
    }
    public void disableItemByName(String n)
    {
        MenuOrSubMenu[] found = {null};
        findItemByName_r(n,found);
        MenuOrSubMenu f = found[0];
        if(f==null)
        {
            System.out.println("Could not find menu item. " + n);
            return;
        }
        if(f._visible==false)
            return;
        f._visible = false;
        
    }					//drawBevelStringMenu("Continue", 230, 213, g);					//drawBevelStringMenu("Continue", 230, 213, g);
    public void changeItemTextByName(String name, String newText)
    {
        MenuOrSubMenu[] found = {null};
        findItemByName_r(name,found);
        if(found[0]==null)
        {
            System.out.println("Could not find menu item. "+ name);
            return;
        }
        found[0]._str = newText;
    }		  
    public void addMenuItem(String name, String str)
    {
        MenuOrSubMenu m = new MenuOrSubMenu(_pos.x,_pos.y+_charYSpacing*_menuLevel._ar.size());
        m._str = str;
        m._name = name;

        if(_cursorIndex==null)
            _cursorIndex = m;
        
        _menuLevel._ar.add(m);
    }
    public void addSubMenuItem(String name, String str)
    {
        MenuOrSubMenu m = new MenuOrSubMenu(
        _pos.x+_indent,
        _pos.y+_charYSpacing*_menuLevel._ar.size());
        m._str = str;
        m._name = name;
        _menuLevel._ar.add(m);
    }
    // - Render the menu and all other stuff.
    public void render(Graphics g)
    {
        drawstr(g);
        for(int i=0; i<_menuLevel._ar.size(); ++i)
        {
            ((MenuOrSubMenu)_menuLevel._ar.get(i)).render(g);
        }
    }
    void drawstr(Graphics g)
    {
        bevelString(_str,(int)_pos.x,(int)_pos.y,g,3);
    }
    private void bevelString( String s, int x, int y, Graphics g, int level ) 
    {
        if("".equals(s))
            return;
        if(!_visible)
            return;
        
        Color c = g.getColor();
        for( int i=0; i<=level; ++i ) {
            g.setColor(
                new Color( 
                    (int)((float)c.getRed()*((float)i/(float)level)),
                    (int)((float)c.getGreen()*((float)i/(float)level)),
                    (int)((float)c.getBlue()*((float)i/(float)level))
                )
            );
            g.drawString(s,(x+(i-level)),(y+(i-level)));
        }
        g.setColor(c);
    }
    
    
    public MenuOrSubMenu( float x, float y)
    {
        _parentMenuItem=null;
        _menuLevel = this;
        _indent = 12;
        _charYSpacing = 18;
        _pos.x = x;
        _pos.y = y;
        _str = "";
        _visible = true;
    }
}
