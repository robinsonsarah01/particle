//import org.lwjgl.*;

public class Main {
  
	public static int DISPLAY_HEIGHT = 600;
	public static int DISPLAY_WIDTH = 600;
	
	public static void main(String[] args) {
		GUI gui = new GUI(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		gui.instructions();
		gui.go();
	}
}
