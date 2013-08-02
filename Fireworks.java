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
import java.util.*;

public class Fireworks extends Particle {
    //thanks to the NetLogo Fireworks demo by Uri Wilensky
	//for the fireworks setup/idea
	//colors from: http://lslwiki.net/lslwiki/wakka.php?wakka=color (nice chart)
	
	//FIREWORKS CONSTANTS
	private double terminal_y_vel = -4;
	private float dim = 0.0005f;
	private float fade_amt = 0.0001f;
	//FIREWORKS VARIABLES
	private int NUM_FRAGMENTS = 5;
	private boolean isFrag;
	private float R;
	private float G;
	private float B;
	//private Random randy = new Random();
	//private ArrayList<Fireworks> trail;
	//private Fireworks before;
	//private int trailcount;
	//private String color;
		
	public Fireworks(boolean frag, float xvel, float yvel, float xloc, float yloc, 
			float radius, float red, float green, float blue) {
		super();
		this.xloc = xloc;
		this.yloc = yloc;
		isFrag = frag;
		this.xvel = xvel;
		this.yvel = yvel;
		yacc = -.075;
		r = radius;
		R = red;
		G = green;
		B = blue;
		type = "Fireworks";
		/*this.color = color;
		if (color.equals("red")){
			R = 1.0f;
			G = 0.0f;
			B = 0.0f;
		}
		else if (color.equals("blue")) {
			R = 0.0f;
			G = 0.0f;
			B = 1.0f;
		}
		else {
			R = 0.0f;
			G = 1.0f;
			B = 0.0f;
		} */
            
	}
	
	public Fireworks(float xloc, float yloc, float red, float green, float blue){
		super();
		this.xloc = xloc;
		this.yloc = yloc;
		isFrag = false;	
		alive = true;
		Random randy = new Random();
		yvel = 3 + 5 * randy.nextFloat();
		xvel = 7 * randy.nextFloat() - (float)3.5;
		yacc = -.075;
		R = red;
		G = green;
		B = blue;
		type = "Fireworks";
	}
	
	/*public Fireworks(boolean frag, float xvel, float yvel, float xloc, float yloc, 
			float radius, float red, float green, float blue, Fireworks maker, int tc) {
		super();
		this.xloc = xloc;
		this.yloc = yloc;
		isFrag = frag;
		this.xvel = xvel;
		this.yvel = yvel;
		yacc = -.075;
		r = radius;
		R = red;
		G = green;
		B = blue;
		type = "Fireworks";
		before = maker;
		trailcount = tc;
	} */
	public Fireworks() {
		r = 4;
		yacc = -.075;
		Random randy = new Random();
		yvel = 3 + 5 * randy.nextFloat() + 0.5f * Mouse.getDY();
		xvel = 7 * randy.nextFloat() - (float)3.5 + 0.5f * Mouse.getDX();
		isFrag = false;
		setRandomColor();
		type = "Fireworks";
	}
        
        public Fireworks(float x1, float y1) {
            xloc = x1;
            yloc = y1;
            r = 4;
            yacc = -.075;
            Random randy = new Random();
            yvel = 3 + 5 * randy.nextFloat() + 0.5f * Mouse.getDY();
            xvel = 7 * randy.nextFloat() - 3.5f + 0.5f*Mouse.getDX();
            isFrag = false;
            setRandomColor();
            type = "Fireworks";
        }
	
	public Fireworks(float radius) {
		super();
		r = radius;
		yacc = -.075;
		isFrag = false;
		setRandomColor();
		type = "Fireworks";
	}
	
        /*
	public Fireworks(double radius, int f, double x, double y, 
	double fade, double g) {
		super();
		r = radius;
		fragments = f;
		xvel = x;
		yvel = y;
		fade_amt = fade;
	}
	*/
        
	public void explode() {
		Random randy = new Random();
		double theta = Math.atan(yvel/xvel);
		for (int i=0;i<NUM_FRAGMENTS;i++) { //floats are needy, lots of casting needed
			// GUI.particles.add(new Fireworks(true, 
			//       this.xvel * (xvel*.5+ Math.sin(theta)+randy.nextDouble()-1.0),
			//     this.yvel * (yvel*.3+ Math.cos(theta)+randy.nextDouble()))-1.0);
			Fireworks squandarlo = new Fireworks(true,
					(float)(xvel * (xvel * .5 + Math.sin(theta) + 2*randy.nextFloat()-1)),
					(float)(yvel * (yvel * .3 + Math.cos(theta) + 2*randy.nextFloat()-1)),
					xloc, yloc, 2, R, G, B /*, null, 15 */ );
			GUI.particles.add(squandarlo);
			
		}
		alive = false;
	
	}
	
	//This should be written according to the NetLogo thing
	//in order to make pretty fading trails.
	//However, OpenGL colors annoy me, so someone else can figure this out -Daryl
	//The amounts can be edited up top, but they shouldn't exceed certain limits -Sarah
	public void fade() {
		dim += fade_amt; //fade faster as they fall
		if (R > 0.0f) R -= dim;
		if (G > 0.0f) G -= dim;
		if (B > 0.0f) B -= dim;
	}
	
	public void update() {
		//super.update();
		
		xvel += xacc;
		yvel +=	yacc;
		xloc += xvel;
		yloc += yvel;
		
		if (!isFrag && yvel<=terminal_y_vel) {
			explode();
		}
		if (isFrag && yvel<=terminal_y_vel /* && trailcount <= 0 */) {
			//if ( before != null)
				//before.setAlive(false);
			alive = false;
        }
		if (isFrag && alive){
			fade();
			/*trailcount--;
			GUI.particles.add(new Fireworks(true, xvel + xacc, (float)(yvel + yacc), xloc + xvel, yloc + yvel,
					r, R - dim, B - dim, G - dim, this, trailcount  )); */
			
		}
            //System.out.println(yvel<=terminal_y_vel);
	}
	
	public void display() {
		//GUI.drawCircle(xloc,yloc,r);
		//GUI.drawQuad(xloc,yloc,1,1);
		GL11.glColor3f(R, G, B);
		super.display();
	}
	
	
	public void setRandomColor(){ //gives birth to Godzilla except not really
		Random r = new Random();
		int c = r.nextInt(8);
		if (c == 0){ //red
			R = 1.0f;
			G = 0.0f;
			B = 0.0f;
		}
		else if (c == 1){ //blue
			R = 0.0f;
			G = 0.0f;
			B = 1.0f;
		}
		else if (c == 2) { //green
			R = 0.2f;
			G = 0.8f;
			B = 0.0f;
		}
		else if (c == 3) { //silver
			R = 0.753f; //192 / 255
			G = 0.753f;
			B = 0.753f;
		}
		else if (c == 4) { //purple
			R = 0.6f;
			G = 0.0f;
			B = 0.6f;
		}
		else if (c == 5){ //orange
			R = 1.0f;
			G = 0.4f;
			B = 0.0f;
		}
		else if (c == 6){ //yellow
			R = 1.0f;
			G = 1.0f;
			B = 0.0f;
		}
		else { //pink
			R = 1.0f;
			G = 0.2f;
			B = 0.4f;
		}
	}
	
	//-- this is technically a toString method. do not mess with it.
	
	public String getData(){ //number comments refer to savetofile stuff
		String data = this.getType() + " "; // 0 = type (String) - id purposes
		data += isFrag + " "; //1 = is it a fragment (boolean)
		data += xvel + " "; //2 = x velocity (float)
		data += yvel + " "; //3 = y velocity (float)
		data += xloc + " "; //4 = x location (float)
		data += yloc + " "; //5 = y location (float)
		data += r + " "; //6 = radius (float)
		data += R + " "; //7 = red (float)
		data += G + " "; //8 = green (float)
		data += B; //9 = blue (float)
		
		return data;
	}
}
