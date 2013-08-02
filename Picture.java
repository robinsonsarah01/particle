import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Image.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.JComponent.*;

/** note: some of this code is from last term's Picture homework assignment
 * found at http://code.google.com/p/ml1x-z-src/source/browse/trunk/src/Picture/Picture.java
 * credit for constructor and getArray goes to Mike Zamansky
 *
 */

public class Picture {
	
	protected BufferedImage img;
    protected int height;
    protected int width;
    private boolean readable;
    
    public Picture(String filename){
    	try {
    		img = ImageIO.read(new File(filename));
    		width = img.getWidth();
    		height = img.getHeight();
    		if (width <= Main.DISPLAY_WIDTH - 200 && height <= Main.DISPLAY_HEIGHT - 200)
    			readable = true;
    		else readable = false;
    	}
    	catch (IOException e){
    		e.printStackTrace();
    		System.out.println("Picture unreadable, game will continue");
    		img = null;
    		readable = false;
    	}
    	//troubleshooting
    	//System.out.println(readable);
    }
    
    public Color[][] getArray(){
    	Color[][] a = new Color[width][height];
    	int x,y;
    	
    	for (x=0; x<width; x++){
    		for (y=0; y<height; y++){
    			int p = img.getRGB(x, y);
    			int r = (p & 0x00ff0000) >> 16;
    			int g = (p & 0x0000ff00) >> 8;
    			int b = p & 0x000000ff;
    			a[x][y] = new Color(r,g,b);
    		}
    	}
    	
    	return a;
    }
    
    public void readFromPicture(){
    	if (!readable) return;
    	Color[][] a = getArray();
    	//int i = 0;
    	GUI.particles = new ArrayList<Particle>();
    	for (int x=0; x<width; x+=5){
    		for (int y=0; y<height; y+=5){
    			float xloc = (float)(x);
    			float yloc = (float)(y);
    			float r = (float) ( a[x][y].getRed() / 255.0 ); //+ 0.5f;
    			float g = (float) ( a[x][y].getGreen() / 255.0 ); //+ 0.5f;
    			float b = (float) ( a[x][y].getBlue() / 255.0 ); //+ 0.5f;
    			
    			//if (r > 1) r = 1.0f; //shouldn't happen
    			//if (g > 1) g = 1.0f; //but just in case
    			//if (b > 1) b = 1.0f;
    			
    			//if ( x % 10 == 0 && y % 10 == 0){
    				//System.out.println(r + " " + g + " " + b);
    			//}
    			
    			//if ( ( r != 0 && b != 0 && g != 0 ) && (r != 1.0 && b != 1.0 && g != 1.0) ) //no black or white
    			Fireworks f = new Fireworks( xloc, yloc, r, g, b );
    			f.display();
    			GUI.particles.add(f);
    			
    		}
    	}
    	//troubleshooting
    	System.out.println("Picture loaded! " + GUI.particles.size() + " particles ready to go");
    	
    }

}
