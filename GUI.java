//import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL11;
//import java.util.*;
import java.lang.Math;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.util.*;

public class GUI {
        public static ArrayList<Particle> particles;
        //private ArrayList<Button> buttons;
        private int width;
        private int height;
        //private int trueHeight; //this is for if buttons are working, but buttons are not working
        private String currParticle;
        //private boolean justClicked;
        private boolean destroyed;
        private int particlecap;
        private JFrame instructions;
        private boolean instructionsVisible;
        public static boolean paused = false;
        public static LinkedList<Particle>[][] carrier;
        public static int xdiv;
        public static int ydiv;
        private int currentparticle; //number of particles currently in existence (approximately)
        private int numPowders; //number of powders in carrier
        private Fire smoky;
        //private Water unsmoky;
        private SaveToFile saver;
        private File saveFile;
        private String filepath = "particleSystem.txt";
        private boolean canSave;
        private boolean saved;
        private String imagepath = "nemo.jpg";
        private Picture picture;
        public static int PEN_SIZE = 1;
        // float r,g,b;

        public GUI(int w, int h) {
                currentparticle = 0;
                numPowders = 0;
                xdiv = 20;
                ydiv = 20;
                width = w;
                //trueHeight = h;
                height = h;
                currParticle = "powder";
                //justClicked = false;
                destroyed = false;
                width = w;
                //trueHeight = h;
                height = h;
                particles = new ArrayList<Particle>();
                currParticle = "powder";
                //justClicked = false;
                destroyed = false;
                particlecap = 50000;
                carrier = new LinkedList[width/xdiv][height/ydiv];
                for (int x = 0; x <carrier.length; x++){
                    for (int y = 0; y<carrier[0].length; y++){
                        carrier[x][y] = new LinkedList<Particle>();
                    }
                }
                smoky = new Fire();
                smoky.reset();
               // unsmoky = new Water();
               // unsmoky.reset();
                //saver = new SaveToFile("");
                saveFile = new File(filepath);
                try {
                	if (!saveFile.exists())
                    	saveFile.createNewFile();
                	canSave = true;
                }
                catch (IOException e){
                	e.printStackTrace();
                	canSave = false;
                }
                saved = false;
                
                picture = new Picture(imagepath);

        }

        public void go() {
        	try {
        		Display.setDisplayMode(new DisplayMode(width, height));
        		// the following line is DANGEROUS
        		// Display.setInitialBackground(.5f,.5f,.5f);
        		Display.setTitle("Particle System Simulator");
        		Display.create();
        	}
        	catch (LWJGLException e) {
        		e.printStackTrace();
        		System.exit(0);
            }

        	// initialization for rendering
        	GL11.glMatrixMode(GL11.GL_PROJECTION);
        	GL11.glLoadIdentity();
        	GL11.glOrtho(0, width, 0, height, 1, -1);
        	GL11.glMatrixMode(GL11.GL_MODELVIEW);
        	GL11.glDisable(GL11.GL_DEPTH_TEST); // disable depth - no 3d

        	while (!Display.isCloseRequested()) {

        		Display.sync(60);
        		// clear the screen
        		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT /*| GL11.GL_ACCUM_BUFFER_BIT*/);

        		//justClicked = false; //reset the value to allow particles to be created
        		//--WORKING CODE
        		// sets the color to blue
        		//GL11.glColor3f(0.2f, 0.5f, 0.7f);
        		// draws a circle at 100,100 with radius 10
        		//drawCircle((double)Mouse.getX(), (double)Mouse.getY(), 10.0);

        		// sets the color to something not blue
        		//GL11.glColor3f(0.3f, 0.4f, 0.6f);
        		// draws a square with side length 100 at 100,100
        		//drawQuad(100, 100, 100, 100);
        		//--END WORKING CODE


        		//poll keyboard input to change particle type
        		while (Keyboard.next() && Keyboard.getEventKeyState()){
        			if (Keyboard.getEventKey() == Keyboard.KEY_P) {
        				currParticle = "powder";
        				System.out.println("You are now using Powder.");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_F) {
        				currParticle = "fireworks";
        				System.out.println("You are now using Fireworks.");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
        				currParticle = "fire";
        				System.out.println("You are now using Fire.");
        			}
                                else if (Keyboard.getEventKey() == Keyboard.KEY_B) {
        				currParticle = "superball";
        				System.out.println("You are now using Superball");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_C) {
        				particles = new ArrayList<Particle>();
        				carrier = new LinkedList[width/xdiv][height/ydiv]; //n squared over some constant
        				for (int x = 0; x <carrier.length; x++){
        					for (int y = 0; y<carrier[0].length; y++){
        						carrier[x][y] = new LinkedList<Particle>();
        					}
        				}
        				smoky.reset();
        				numPowders = 0;
        				currentparticle = 0;
        				System.out.println("Particles cleared.");
        			}

        			else if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
        				paused = !paused;
        				GUI.pause(1);
        				System.out.println("Game is paused. Press the spacebar to resume.");
        			}

        			else if (Keyboard.getEventKey() == Keyboard.KEY_I)
        				toggleInstructions();

        			else if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
        				//Display.destroy(); //may cause an error at times, but works
        				destroyed = true;
        				//System.exit(0);
        				//break;
        			}
        			
        			else if (Keyboard.getEventKey() == Keyboard.KEY_S){
        				paused = true;
        				if (canSave){
        					saver = new SaveToFile(filepath);
        					saver.save();
        					saved = true;
        				}
        				//paused = false;
        			}
        			
        			else if (Keyboard.getEventKey() == Keyboard.KEY_L){
        				paused = true;
        				if (saved) {
        					saver.readFromFile();
        				}
        				//paused = false;
        			}
        			
        			else if (Keyboard.getEventKey() == Keyboard.KEY_M){
        				paused = true;
        				picture.readFromPicture();
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_1) {
        				PEN_SIZE = 1;
        				System.out.println("Pen size is now 1");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_2) {
        				PEN_SIZE = 2;
        				System.out.println("Pen size is now 2");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_3) {
        				PEN_SIZE = 3;
        				System.out.println("Pen size is now 3");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_4) {
        				PEN_SIZE = 4;
        				System.out.println("Pen size is now 4");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_5) {
        				PEN_SIZE = 5;
        				System.out.println("Pen size is now 5");
        			}
        			else if (Keyboard.getEventKey() == Keyboard.KEY_6) {
        				PEN_SIZE = 6;
        				System.out.println("Pen size is now 6");
        			}
                                        			
        			else //default particle is powder
        				currParticle = "powder";
        		}

        		if (destroyed){
        			//break;
        			Display.destroy();
        			System.exit(0);
        		}

        		// creates a new particle at mousex,mousey
        		//when the mouse is pressed
        		if (Mouse.isButtonDown(0) && /*!justClicked &&*/ currentparticle <= particlecap) {
        			int x = Mouse.getX();
        			int y = Mouse.getY();
        			switch (currParticle) {
        			case "powder":
        				//particles.add(new Powder());
        				//System.out.println(xdiv);
        				for (double ix = x-PEN_SIZE*1.5; ix<x+PEN_SIZE*1.5 && currentparticle < particlecap;ix+=1.5) {
        					for (double iy = y-PEN_SIZE*1.5;iy<y+PEN_SIZE*1.5;iy+=1.5) {
        						int a = (int)ix/xdiv;
        						int b = (int)iy/ydiv;
        						carrier[a][b].add(new Powder((float)ix,(float)iy));
        						currentparticle++;
        						numPowders++;
        					}
        				}
        				break;
        			case "fireworks":
        				if (PEN_SIZE==1) {
        					particles.add(new Fireworks());
        					currentparticle++;
        					break;
        				}
        				else {
        					int tx = Mouse.getX();
        					int ty = Mouse.getY();
        					for (int ix = tx-PEN_SIZE*2; ix<tx+PEN_SIZE*2 && currentparticle < particlecap;ix+=2) {
        						for (int iy = ty-PEN_SIZE*2; iy<ty+PEN_SIZE*2;iy+=2) {
        							particles.add(new Fireworks(ix,iy));
        							currentparticle++;
        						}
        					}
        					break;
        				}
        			case "superball":
        				for (double ix = x-PEN_SIZE*1.5; ix<x+PEN_SIZE*1.5 && currentparticle < particlecap;ix+=1.5) {
        					for (double iy = y-PEN_SIZE*1.5;iy<y+PEN_SIZE*1.5;iy+=1.5) {
        						int a = (int)ix/xdiv;
        						int b = (int)iy/ydiv;
        						carrier[a][b].add(new Superball((float)ix,(float)iy));
        						currentparticle++;
        						numPowders++;
        					}
        				}
                                    break;
        			case "fire":
        				smoky.mousePressed();
        				//currentparticle++;
        				//currentparticle isn't really relevant here since
        				//fire isn't a particle, it's a field of velocities
        				//justClicked = true; //use this later when we need more pauses
        				break;
        			}
        		}


        		// displays the particles
//        		for (int x = 0; x < carrier.length; x++) {
//        			for (int y = 0; y < carrier[0].length ; y++){
//        				for (Node bob : carrier){
//        					double a = bob.data.getxloc();
//        					double b = bob.data.getxloc();
//        					a = a/xdiv;
//        					b = b/ydiv;
//        					if (a != x || b != y){
//        						carrier[a][b].add(bob.remove());
//        					}
//        					bob.data.display();
//        					if (!paused){
//        						bob.data.update();
//        					}
//        					if (!bob.data.isAlive())
//        						bob.remove();
//        				}
//        			}
//
//        		}
        		
        		if (numPowders > 0){
        			for (int x = 0; x < carrier.length; x++){ //n squared over some constant
        				for (int y = 0; y < carrier[0].length; y++){
        					LinkedList<Particle> current = carrier[x][y];
        					//LinkedList me = current;
        					for (int i = 0; i < current.size() && i >= 0; i++){
        						int a = (int) (((Particle)((current.get(i)))).getxloc())/xdiv;
        						int b = (int) (((Particle)((current.get(i)))).getyloc())/ydiv;
        						//System.out.println(x);
        						//System.out.println(y);
        						//System.out.println(me.data.getxloc());
        						//System.out.println(me.data.getyloc());
        						//System.out.println(me.data);
        						if (a != x || b != y){
        							carrier[a][b].add((Particle)(current.remove(i)));   
        							//i--;
        						}
        						
        						if (i < current.size()){
        							current.get(i).setlist(carrier[a][b]);
        							((Particle)(current.get(i))).display();
        							if (!paused)
        								((Particle)(current.get(i))).update();
        							if (!((Particle)(current.get(i))).isAlive()){
        								current.remove(i);
        								i--;
        								currentparticle--;
        								numPowders--;
        							}
        						}
        					}
                                
        				}
        			}
        		}
                        
        		for (int i=0; i<particles.size(); i++){ //linear
        			(particles.get(i)).display();
        			if (!paused){
        				(particles.get(i)).update();
        			}
        			if (!particles.get(i).isAlive()){
        				particles.remove(i);
        				currentparticle--;
        			}
        		}

        		//checking particle type and keyboard input
        		//System.out.println(currParticle); //it works!
        		if (currParticle.equals("fire")) {
                            smoky.paint();
                        }
        		//unsmoky.paint();
        		
        		Display.update();
        		// update the adjacency list after each display update
        		// updateAdjacency();


            	}

                Display.destroy(); //apparently even if the Display has been destroyed
                                                        //you can destroy it again without errors
        }

        // We'll probably use this to display the particles.
        /* THIS DRAWS A HOLLOW CIRCLE. OVERLORD DARYL DOES NOT LIKE HOLLOW CIRCLES.
        public static void drawCircle(double centerx, double centery, double r,
                        int num_segments) {
                GL11.glBegin(GL11.GL_LINE_LOOP);
                for (int i = 0; i < num_segments; i++) {
                        double theta = 2.0 * 3.1415926 * i / (double) num_segments; // get the
                                                                                                                                                // current angle,
                                                                                                                                                // yay polar
                        double x = r * Math.cos(theta); // x component
                        double y = r * Math.sin(theta); // y component
                        GL11.glVertex2d(x + centerx, y + centery); // the output vertex
                }
                GL11.glEnd();

        }
        */

        //THIS DRAWS A FILLED CIRCLE BUT IS KIND OF BROKEN, JUST A LITTLE BIT
        public static void drawCircle(double cx, double cy, double radius) {
                double vectory1 = cy;
                double vectorx1 = cx;
                GL11.glBegin(GL11.GL_TRIANGLES);
                for (int i=0;i<=360;i++) {
                        double angle = (double)i/57.29577957795135;
                        double vectorx = cx+(radius*Math.sin(angle));
                        double vectory = cy+(radius*Math.cos(angle));
                        GL11.glVertex2d(cx,cy);
                        GL11.glVertex2d(vectorx1,vectory1);
                        GL11.glVertex2d(vectorx,vectory);
                        vectory1=vectory;
                        vectorx1=vectorx;

                }

        }
        // THIS IS BEING USED TO DISPLAY EVERYTHING
        public static void drawQuad(float startx, float starty, float width,
                        float height) {
                GL11.glBegin(GL11.GL_QUADS);
                        GL11.glVertex2f(startx, starty);
                        GL11.glVertex2f(startx + width, starty);
                        GL11.glVertex2f(startx + width, starty + height);
                        GL11.glVertex2f(startx, starty + height);
                GL11.glEnd();
        }

        public static void pause(int seconds){ //seconds is in milliseconds
                try {
                	Thread.sleep(seconds);
                }
                catch (Exception e) {
                	e.printStackTrace();
                }
        }

        public void instructions() { //create a window for instructions for a short time initially
        	instructions = new JFrame("How to use the particle system");
        	instructions.setLayout(new GridLayout(10,1));
        	//instructions.setLocation(350,350);
        	instructions.setBounds(300,300,600,300);
        	instructions.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        	JLabel a = new JLabel("Press P to set the current particle to powder");
        	JLabel b = new JLabel("Press F to set the current particle to fireworks");
        	JLabel c = new JLabel("Press C to clear the current particles");
        	JLabel d = new JLabel("Press space to pause/resume the game");
        	JLabel e = new JLabel("Press Q to switch to fire");
        	JLabel f = new JLabel("Press I to see these instructions again later");
        	JLabel g = new JLabel("Press S to save fireworks to file");
        	JLabel h = new JLabel("Press L to load saved fireworks from file"); 
        	JLabel i = new JLabel("Press M to load fireworks from a picture");
        	JLabel j = new JLabel("Press 1,2,3,4,5,6 to set the pen size");
        	JLabel k = new JLabel("Press B to set the current particle to superball");
        	//JPanel d = new JPanel(); d.add(a);
        	//JPanel e = new JPanel(); e.add(b);
        	//JPanel f = new JPanel(); f.add(c);
        	instructions.add(a); instructions.add(b); instructions.add(c);
        	instructions.add(d); instructions.add(e); instructions.add(f);
        	instructions.add(g); instructions.add(h); instructions.add(i);
        	instructions.add(j); instructions.add(k);
        	//instructions.pack();
        	instructions.setVisible(true);
        	//instructionsVisible = true;
        	GUI.pause(3750); //time to read the instructions
        	instructions.setVisible(false);
        	//instructionsVisible = false;
        }	

        public void toggleInstructions() {
            paused = true;    
        	instructions.setVisible(true); //closed by user 
        	//instructionsVisible = !instructionsVisible;
        }


}
