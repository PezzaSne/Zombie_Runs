package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Timer;

import org.dyn4j.geometry.Vector2;

import model.Arena;
import model.Player;
import view.ArenaView;

public class Controller   {

	private int fps = 25;

	private Arena world;
	private ArenaView view;
	private Timer timer;
	private Player runner;

	private boolean upPressed, downPressed, leftPressed, rightPressed;

	public KeyListener keyListener;
	private ActionListener timerListener;

	public Controller(Arena world, ArenaView view) {

		this.world = world;
		this.view = view;
		this.runner = this.world.getPlayer();
		
		upPressed = false;
		downPressed = false;
		leftPressed = false;
		rightPressed = false;

		keyListener = createKeyListener();
		timerListener = createTimerListener();

		this.timer = new Timer(1000/fps, timerListener);

	}

	public KeyListener getKeyListener() {
		return keyListener;
	}


	public void setKeyListener(KeyListener keyListener) {
		this.keyListener = keyListener;
	}

	private KeyListener createKeyListener() {
		return new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
				{
					
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_W) {
					upPressed = false;
					view.resetAnimation();
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_S) {
					downPressed = false;
					view.resetAnimation();
				}		
				if (e.getExtendedKeyCode() == KeyEvent.VK_D) {
					rightPressed = false;
					view.resetAnimation();
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_A) {
					leftPressed = false;
					view.resetAnimation();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
				if (e.getExtendedKeyCode() == KeyEvent.VK_W) {
					upPressed = true;
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_S) {
					downPressed = true;
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_D) {
					rightPressed = true;
				}
				if (e.getExtendedKeyCode() == KeyEvent.VK_A) {
					leftPressed = true;
				}
			}
		};
		
	}

	private ActionListener createTimerListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(upPressed) {
					view.setAnimation("upPressed");
					runner.applyForce(new Vector2(0,1));
				}
				if (downPressed) {
					view.setAnimation("downPressed");
					runner.applyForce(new Vector2(0,-1));
				}
				if(leftPressed) {
					runner.applyForce(new Vector2(-1,0));
					view.setAnimation("leftPressed");
				}
				if (rightPressed) {
					runner.applyForce(new Vector2(1,0));
					view.setAnimation("rightPressed");
				}

				world.updatev(1.0/fps);
			}
		};
		
	}

	public void start() {
		timer.start();
	}
	
	public void stop() {
		timer.stop();
	}

}
