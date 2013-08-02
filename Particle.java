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
import java.lang.Math;

public class Particle {
    
    protected int x = Main.DISPLAY_WIDTH;
    protected int y = Main.DISPLAY_HEIGHT;
;
    protected float xloc;
    protected float yloc;
    protected float xvel;
    protected float yvel;
    protected int[][] adjList;
    protected float r;
    protected float xacc;
    protected double yacc;
    protected boolean alive;
    protected static final double gravity = -.25;
    protected boolean contactx;
    protected boolean contacty;
    protected boolean settled;
    protected float bounce;
    protected String type;
    protected LinkedList<Particle> check;
    protected float previousx;
    protected float previousy;
    protected static int ycount;
    protected static int count;
    
    
    public Particle() {
        count = 10;
        ycount = 1;
        previousx = 0;
        previousy = 0;
    	xloc = (float)(Mouse.getX());
    	yloc = (float)(Mouse.getY());
    	//DEBUG
    	//System.out.println("XCOORD: "+xloc);
    	//System.out.println("YCOORD: "+yloc);
	   	xvel = 0;
	   	yvel = 0;
	   	yvel = 0;
	   	xacc = 0;
	   	yacc = 0;
	   	contactx = false;
	   	settled = false;
	   	alive = true;
	   	r = 0;
	   	contacty = false;
	   	//bounce = 1;
	   	//adjList = new int[Display.getWidth()][Display.getHeight()];
	   	check = new LinkedList<Particle>();
    }
      
    public Particle(float xl, float yl){
    	count = 10;
        ycount = 1;
        previousx = 0;
        previousy = 0;
    	xloc = xl;
    	yloc = yl;
    	//DEBUG
    	//System.out.println("XCOORD: "+xloc);
    	//System.out.println("YCOORD: "+yloc);
	   	xvel = 0;
	   	yvel = 0;
	   	xacc = 0;
	   	yacc = 0;
	   	alive = true;
	   	contactx = false;
	   	settled = false;
	   	r = 0;
	   	contacty = false;
	   	//bounce = 1;
	   	//adjList = new int[Display.getWidth()][Display.getHeight()];
	   	check = new LinkedList<Particle>();
  }
	   
    public void display() {
        GUI.drawQuad(xloc,yloc,r,r);
    }
    public void contactborder(){
        if (settled)
            return;
        if (xloc <= r + 3 || xloc >= x-3){
            xvel = xvel * bounce * -1;
            contactx = true;
        }
        if (yloc <= r + 3 || yloc >= y-3){
            yvel = yvel * bounce * -1;
            contacty = true;
        }
    }
    public void update() {
        if (settled)
            return;
        if (killme())
            return;
        if (count == 0){
            collide();
            count = 10;
        }
        contactborder();
        if (contacty && Math.abs(yvel) <= 0.5){
            settled = true;
            xvel = 0;
            yvel = 0;
            yacc = 0;
            xacc = 0;
        } 
    	xvel += xacc;
    	yvel +=	yacc;
        if(ycount == 0){
            previousx = xloc;
            previousy = yloc;
            ycount = 1;
        }
        xloc += xvel;
    	yloc += yvel;
    	if (!settled){ //this is actually redundant b/c of an earlier check
           yvel += gravity;
           contacty = false;
        }
        contactx = false;
    	//if (yloc > Display.getHeight()) //temporary fix - change when stacking implemented
    		//alive = false;
    	//else if (xloc < 0 || xloc > Display.getWidth())
    		//alive = false;
        count--;
        ycount--;
    }
    public void collide(){
        for (int i = 0; i < check.size(); i++){
            Particle opposite = check.get(i);
            float xdif = xloc - opposite.getxloc();
            float ydif = yloc - opposite.getyloc();
            if (xdif * xdif + ydif*ydif <= (r+opposite.getRadius())*(r+opposite.getRadius())){
            	//xloc = previousx;
                //yloc = previousy;
                boolean ans = opposite.getSettled();
               
               // opposite.setSettled(false);
               float xhim = opposite.getxvel();
               float yhim = opposite.getyvel();
               float xcenter = (xvel + xhim)/2;
               float ycenter = (yvel + yhim)/2;
               xhim -= xcenter;
               xvel -= xcenter;
               yhim -= ycenter;
               yvel -= ycenter;
               float b = min(opposite.getBounce(), bounce);
               xhim = xhim * b * -1;
               yvel = xhim * b * -1;
               yhim = xhim * b * -1;
               xvel = xhim * b * -1;
               xhim += xcenter;
               yhim += ycenter;
               xvel += xcenter;
               yvel += ycenter;
               opposite.setxvel(xhim);
               opposite.setyvel(yhim);
               opposite.contactborder();
               if (ans ){
                   settled = true;
                   xvel = 0;
                   yvel = 0;
                   yacc = 0;
                   xacc = 0;
                   break;
               }
               contactborder();
            }
        }
    }
    private boolean killme(){
        if (xloc < 0 || xloc > x || yloc < 0 || yloc > y ){
            alive = false;
            return true;
        }
        return false;
    }
    private float min(float a, float b){
        if (a > b){
            return b;
        } else
            return a;
    }
    public float getBounce(){
        return bounce;
    }
    public void setxvel(float vel){
        xvel = vel;
    }
    public void setyvel(float vel){
        yvel = vel;
    }
    public float getxloc() {
    	return xloc;
    }
    public float getyloc() {
    	return yloc;
    }
    public float getxvel() {
    	return xvel;
    }
    public float getyvel() {
    	return yvel;
    }
    public boolean isAlive() {
    	return alive;
    }
    public void setAlive(boolean b){
    	alive = b;
    }
    public String getType(){
    	return type;
    }
    public float getRadius(){
        return r;
    }
    public boolean getSettled(){
        return settled;
    }
    public void setSettled(boolean truth){
        settled = truth;
    }
    //-- this is technically a toString method. godzilla will eat you if you mess with it   
    public String getData() { //number comments refer to savetofile stuff
    	String data = this.getType() + " "; // 0 = type (String) - id purposes
    	data += xloc + " "; // 1 = xloc (float)      
    	data += yloc + " "; // 2 = yloc (float)      
    	data += xvel + " "; // 3 = xvel (float)      
    	data += yvel + " "; // 4 = yvel (float)      
    	data += r + " "; // 5 = radius (float)        
    	data += xacc + " "; // 6 = xacc (float)      
    	data += yacc + " "; // 7 = yacc (double) ***       
    	data += contactx + " "; //8 = contactx (boolean)
    	data += contacty + " "; //9 = contacty (boolean)
    	data += settled; //10 = settled (boolean)    
    	
    	return data;
    }
    public void setlist(LinkedList<Particle> s){
        check = s;
    }
        
	
	/*public void drawCircle(double centerx, double centery, double r, int num_segments) {
		GL11.glColor3f(0.3f, 0.4f, 0.6f);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for (int i = 0; i < num_segments; i++) {
			double theta = 2.0 * 3.1415926 * i / (double) num_segments; // get the
																		// current
																		// angle,
																		// yay
																		// polar
			double x = r * Math.cos(theta); // x component
			double y = r * Math.sin(theta); // y component
			GL11.glVertex2d(x + centerx, y + centery); // the output vertex
		}
		GL11.glEnd();

	} */
	
}
