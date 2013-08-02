import java.io.*;
import java.util.*;

import org.lwjgl.opengl.Display;

public class SaveToFile { //slight misnomer; it also reads from files
	
	private String path;
	private boolean append_to_file;
	FileWriter writer;
	PrintWriter printer;
	BufferedReader reader;
	FileReader fr;
	
	public SaveToFile(String filepath){
		path = filepath;
		append_to_file = false; //each save will overwrite the file
	}
	
	public void save() {
		try {
			writer = new FileWriter(path, append_to_file);
			printer = new PrintWriter(writer);
			
			for (int i=0; i<GUI.particles.size(); i++){
				writeLine(GUI.particles.get(i));
			}
			
			System.out.println("Fireworks saved!");
			printer.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void writeLine(Particle p){
		//PrintWriter printer = new PrintWriter(writer);
		String particleLine = p.getData();
		
		//printer.printf("%s" + "%n" , particleLine );
		printer.println(particleLine);
		
	}
	
	public void readFromFile() { //if any particles exist in current game, this overwrites them
		
		try{
			fr = new FileReader(path);
			reader = new BufferedReader(fr);
			
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
			return; //stop trying to read from file
		}
		
		
		try {
			GUI.particles = new ArrayList<Particle>();
			GUI.carrier = new LinkedList[Display.getWidth()/GUI.xdiv][Display.getHeight()/GUI.ydiv];
            for (int x = 0; x < GUI.carrier.length; x++){
                for (int y = 0; y < GUI.carrier[0].length; y++){
                    GUI.carrier[x][y] = new LinkedList();
                }
            } 
			String particleInfo = "";
			while ( (particleInfo = reader.readLine()) != null){ //has next line, set pI to next line
				Particle p = parseLine(particleInfo);
				if ( p != null && (p.getType().equals("Fireworks")) )
					GUI.particles.add(p); //can be edited for use of powder in carrier (may be difficult)
			}
			System.out.println("File read and particles initialized! Well, the fireworks are, at least.");
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public Particle parseLine(String line){
		String[] data = line.split("[ ]"); 
		Particle p = null;
		String className = data[0];
		//Float xLoc = (Float.valueOf(data[1])).floatValue();
		if (className.equals("Fireworks") && data.length >= 10){ //has all the info for fireworks
			boolean isFrag = Boolean.parseBoolean(data[1]);
			float xvel = Float.parseFloat(data[2]);
			float yvel = Float.parseFloat(data[3]);
			float xloc = Float.parseFloat(data[4]);
			float yloc = Float.parseFloat(data[5]);
			float radius = Float.parseFloat(data[6]);
			float R = Float.parseFloat(data[7]);
			float G = Float.parseFloat(data[8]);
			float B = Float.parseFloat(data[9]);
			/*if (isFrag){
				Fireworks before = null; //data[10]; //this... is not good.
				int trailcount = Integer.parseInt(data[11]);
				
				p = new Fireworks(isFrag, xvel, yvel, xloc, yloc, radius, R, G, B, before, trailcount);
			}
			else */
			p = new Fireworks(isFrag, xvel, yvel, xloc, yloc, radius, R, G, B);
		}
		else if (className.equals("Powder") && data.length >= 11){ //has all the info for powder
			float xloc = Float.parseFloat(data[1]); 
			float yloc = Float.parseFloat(data[2]);
			float xvel = Float.parseFloat(data[3]);
			float yvel = Float.parseFloat(data[4]); 
			//float r = Float.parseFloat(data[5]); //not truly necessary for powder
			float xacc = Float.parseFloat(data[6]);   
			double yacc = Double.parseDouble(data[7]);
			boolean contactx = Boolean.parseBoolean(data[8]);
			boolean contacty = Boolean.parseBoolean(data[9]);
			boolean settled = Boolean.parseBoolean(data[10]);
			
			p = new Powder(xloc, yloc, xvel, yvel, xacc, yacc, contactx, contacty, settled);
		}
		
		return p;
	}
}
