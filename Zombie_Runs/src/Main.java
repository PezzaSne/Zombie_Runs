import javax.swing.JFrame;

import view.*;
import control.Controller;
import model.*;

public class Main {
	
	private static void createAndShowGui() {
		
		Arena world = new Arena();
		ArenaView vista = new ArenaView(world);
		//WorldViewStatus stato = new WorldViewStatus(world);
		Controller controllore = new Controller(world, vista);
		
		world.addView(vista);
		//world.addView(stato);
		
		JFrame frame = new JFrame("GAME NAME");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(vista);
		//frame.add(stato, BorderLayout.SOUTH);
		frame.addKeyListener(controllore.keyListener);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		controllore.start();
		
	}
	

	public static void main(String[] args) {
		
		createAndShowGui();

	}

}
