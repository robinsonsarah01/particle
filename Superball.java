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

public class Superball extends Particle {
	
	public Superball(){
		super();
		bounce = 0.95f;
		r = 3;
		type = "Superball";
	}
	
	public Superball(float xl, float yl) {
    	super(xl,yl);
    	bounce = 0.95f;
    	r = 3;
    	type = "Superball";
    }
	
	public Superball(float xloc, float yloc, float xvel, float yvel, float xacc, double yacc, 
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
		bounce = 0.95f;
		r = 3;
		type = "Superball";
	
	}
	
	public void display(){
		GL11.glColor3f(1.0f, 0.2f, 0.4f);
		super.display();
	}
	
}
