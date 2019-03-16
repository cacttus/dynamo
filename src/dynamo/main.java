package dynamo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.lang.*;
import javax.swing.JFrame;
/**
 *
 * @author Avicenna
 */
public class main {
    public static void main(String[] args)
    {
        int rw = 600, rh = 600 - 32;
        int sw = 600, sh = 600;
        

        JFrame frame = new JFrame("Dynamo");
        
        Insets in = frame.getInsets();
        frame.resize(in.left+in.right+sw,in.bottom+in.top+sh);
        //f.setExtendedState(JFrame.MAXIMIZED_BOTH); //gay
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        frame.getGraphics().setClip(0, 0, rw, rh);
        
        // - Make full screen
      //  GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
      //  device.setFullScreenWindow(frame);
        
        Dynamo d = new Dynamo(rw,rh,sw,sh);
        frame.add(d);
        //d.initDisplay(800,600);
        d.start();

    }
}
