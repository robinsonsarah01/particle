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

public class Fire {
    //dimensions of the display
    int DISPLAY_HEIGHT = Main.DISPLAY_HEIGHT;
    int DISPLAY_WIDTH = Main.DISPLAY_WIDTH;
    
    //frame dimensions
    int d = DISPLAY_WIDTH;
    //solver variables
    int n = 100;
    float dt = .2f;
    FluidSolver fs = new FluidSolver();
    
    //flag to display velocity field
    //boolean vkey = false;
    
    //drawing thread
    //Thread artist = null;
    
    //mouse position
    int x, xOld;
    int y, yOld;
    
    //cell indices
    int i, j;
    
    //cell dimensions
    int dg, dg_2;
    //cell position
    int dx, dy;
    
    //fluid velocity
    int u, v;
    
    //color
    double c;
    int button;
    
    //BufferedImage bi;
    //Graphics2D big;
    
    public void reset() {
        //calculate cell dimensions
        dg = d / n;
        dg_2 = dg / 2;
        //sets up the size and timestep of the fluidsolver
        fs.setup(n, dt);
    }
    
    /*Since we're displaying things in a different way and
     *getting input in a different way, init, start and stop aren't 
     *actually useful to us.
     */
    /*
    public void init() {
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        bi = (BufferedImage) createImage(d,d);
        big = bi.createGraphics();
    }
    */
    /*
    public void start() {
        if (artist == null) {
            artist = new Thread(this);
            artist.start();
            reset();
        }
    }*/
    /*
    public void run() {
        while (artist != null) {
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e) {}
            repaint()
        }
    }
    artist = null;
    * 
    */
    public void paint() {
        //clear screen
        //big.setColor(color.white);
        //big.fillRect(0,0,d,d);
        
        //solve fluid
        //System.out.println("checkpoint A");
        if (!GUI.paused) {
        fs.velocitySolver();
        fs.densitySolver();
        }
        for (int i=1;i<=n;i++) {
            //x position of current cell
            dx = (int) ( (i - 0.5f) * dg);
            for (int j=1;j<=n;j++) {
                //y position of current cell
                dy = (int) ( (j - 0.5f) * dg );
                
                //System.out.println("Checkpoint D");
                //System.out.println((fs.d[I(i,j)]));
                //draw density
                if (fs.d[I(i,j)]>0) {
                    
                    c = (double) ( (1.0 - fs.d[I(i, j)]) * 255);
                    if (c < 0) c = 0;
                    c = (double)(255-c)/255.0;
                    GL11.glColor3d(c,.388*c,.278*c);
                    try {
                        GUI.drawQuad(dx-dg_2, DISPLAY_WIDTH-dy-dg_2, dg, dg);
                        //System.out.println("Drawing successful");
                    }
                    catch (Exception e) {
                    System.out.println("Drawing failed");
                    }
                    //big.setColor(new Color(c,c,c));
                    //big.fillRect(dx-dg_2, dy-dg_2, dg, dg)
                    
                }
                
                // draw velocity field
                //We don't really need this.
                /*
                if (vkey && i % 5 == 1 && j % 5 == 1)
                {
                    u = (int)( 50 * fs.u[I(i,j)] );
                    v = (int)( 50 * fs.v[I(i,j)] );
                    big.setColor(Color.red);
                    big.drawLine(dx, dy, dx+u, dy+v);
                }
                * 
                */
            }
        }
    }
    
    public void mousePressed() {
        if (Mouse.isButtonDown(0))
            button = 0;
        else if (Mouse.isButtonDown(1))
            button = 1;
        else
            button = 2;
        xOld = x;
        yOld = y;
        x = Mouse.getX();
        y = Mouse.getY();
        //System.out.println("checkpoint B");
        updateLocation();
    }
    
    public void updateLocation()
    {
        y = DISPLAY_WIDTH - y;
        // get index for fluid cell under mouse position
        i = (int) ((x / (float) d) * n + 1);
        j = (int) ((y / (float) d) * n + 1);
        //System.out.println(j);

        // set boundries
        if (i > n) i = n;
        if (i < 1) i = 1;
        if (j > n) j = n;
        if (j < 1) j = 1;

        // add density or velocity
        if (button == 0) 
            fs.dOld[I(i, j)] = 5 + GUI.PEN_SIZE * 100;
        //if (button == 3 && e.getID() == MouseEvent.MOUSE_DRAGGED)
        if (button == 1)
        {
            fs.uOld[I(i, j)] = (x - xOld) * 5;
            fs.vOld[I(i, j)] = (y - yOld) * 5;
        }
        //System.out.println("Checkpoint C");
    }
    //util function for indexing
    private int I(int i, int j){ return i + (n + 2) * j; }
}
