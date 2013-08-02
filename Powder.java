import org.lwjgl.*;
import org.lwjgl.opengl.*;
import java.util.*;
import java.lang.Math;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


public class Powder extends Particle {
    //static float bounce = 0;
    public Powder() {
    	super();
    	bounce = 0;
    	r = 3;
    	type = "Powder";
    }
    
    public Powder(float radius) {
    	super();
    	bounce = 0;
    	r = radius;
    	type = "Powder";
    }
    
    public Powder(float xl, float yl) {
    	super(xl,yl);
    	bounce = 0;
    	r = 3;
    	//System.out.println("X: "+ Mouse.getDX());
        //System.out.println("Y: "+ Mouse.getDY());
        //this code SHOULD impart velocity to the powder
        //however, it gives me an ArrayIndexOutofBoundsException...
        //uncomment it and check it out? I don't know why.
        //whattt?
        //xvel = (float)Mouse.getDX()*.5f;
        //yvel = (float)Mouse.getDY()*.5f;
        type = "Powder";
    }
    public Powder(float xloc, float yloc, float xvel, float yvel, float xacc, double yacc, 
    			boolean contactx, boolean contacty, boolean settled){
    	this.xloc = xloc;
    	this.yloc = yloc;
    	this.xvel = xvel;
    	this.yvel = yvel;
    	this.xacc = xacc;
    	this.yacc = yacc;
    	this.contactx = contactx;
    	this.contacty = contacty;
    	this.settled = settled;
    	bounce = 0;
    	r = 3;
    	type = "Powder";
    	
    }
    
    public void display(){
    	GL11.glColor3f(1.0f, 1.0f, 0.5f);
    	super.display();
    }
   public void collide(){
        for (int i = 0; i < check.size(); i++){
            Particle opposite = check.get(i);
            float xdif = xloc - opposite.getxloc();
            float ydif = yloc - opposite.getyloc();
            if (xdif * xdif + ydif*ydif <= (r+opposite.getRadius())*(r+opposite.getRadius())){
            	xloc = previousx;
                yloc = previousy;
               if (opposite.getSettled() ){
                   settled = true;
                   xvel = 0;
                   yvel = 0;
                   yacc = 0;
                   xacc = 0;
                   break;
               }
            }
        }
    }
   
    /*
    public void display() {
    	//drawCircle(xloc,yloc,r,50);
    	//GUI.drawCircle(xloc,yloc,r);
        GUI.drawQuad(xloc,yloc,1,1);
    }
    
    public void update() {

    }
    * 
    * 
    */
}
