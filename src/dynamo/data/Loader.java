/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamo.data;
import java.net.*;

/**
 *
 * @author squid
 */
public class Loader {
    public static String getResource(String fn){
        String f = Loader.class.getResource(fn).toString();
        return f;
    }
    public static URL getResourceURL(String fn){
        URL f = Loader.class.getResource(fn);
        return f;
    }
}
