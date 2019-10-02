package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Circle;
import org.dyn4j.geometry.Convex;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.dyn4j.geometry.Vector2;

import model.Arena;
import model.Coins;
import model.Hurdles;
import model.Player;

/**
 * 
 * @author pezza
 *
 */
@SuppressWarnings("serial")
public class ArenaView extends JPanel implements IView {
	
    private static final int DIM_W = 500;
    private static final int DIM_H = 1000;
	private static final int INCREMENT = 1;
	
	private final double scaling = 80;
	
	private int dx1, dy1, dx2, dy2;
	private int srcx1, srcy1, srcx2, srcy2;
	private int IMAGE_HEIGHT;

	private Arena world;
	
	private BufferedImage backgroundImg;	//Da risolvere ?!
	private BufferedImage coinsImg;			//Da convertire in sprite sheet
	private BufferedImage hurdlImg;			//Da convertire in sprite sheet
	
	// Images for each animation of the player
	private BufferedImage[] walkingUp = {Sprite.getSprite(0,3), Sprite.getSprite(2,3)};
	private BufferedImage[] walkingDown = {Sprite.getSprite(0,0), Sprite.getSprite(2,0)};
	private BufferedImage[] walkingLeft = {Sprite.getSprite(0, 1), Sprite.getSprite(2, 1)}; 
	private BufferedImage[] walkingRight = {Sprite.getSprite(0, 2), Sprite.getSprite(2, 2)};
	private BufferedImage[] stands = {Sprite.getSprite(1, 3)};

	// Different player's animations 
	private Animation walkUp = new Animation(walkingUp, 5);
	private Animation walkDown = new Animation(walkingDown, 5);
	private Animation walkLeft = new Animation(walkingLeft, 5);
	private Animation walkRight = new Animation(walkingRight, 5);
	private Animation standing = new Animation(stands, 5);

	// Default position of the player
	private Animation player = standing;
	
	/**
	 * 
	 * @param world
	 */
	public ArenaView(Arena world) {

		this.world = world;
		loadImages();
		initImagePoints();
		setPreferredSize(new Dimension(DIM_W, DIM_H));
		setDoubleBuffered(true);

	}

	private void loadImages() {

		try {
			this.backgroundImg = ImageIO.read(new File("C:/Users/pezza/git/Zombie_Runs/res/background.jpg"));
			this.coinsImg = ImageIO.read(new File("C:/Users/pezza/git/Zombie_Runs/res/coin/0.5x/Tavola disegno 1@0.5x.png"));
			
            IMAGE_HEIGHT = backgroundImg.getHeight();
            
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
		
	public void setAnimation(String str) {
		
		if(str == "upPressed") {
			player = walkUp;
			player.start();
		}
		if(str == "downPressed") {
			player = walkDown;
			player.start();
		}
		if(str == "leftPressed") {
			player = walkLeft;
			player.start();
		}
		if(str == "rightPressed") {
			player = walkRight;
			player.start();
		}
		
	}
	
	public void resetAnimation() {
		player.stop();
		player.reset();
		player = standing;
	}

	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		transform(g2d);
		g.drawImage(backgroundImg, dx1, dy1, dx2, dy2, srcx1, srcy1, srcx2, srcy2, this);	// BACKGROUND

		drawPlayer(g2d, world.getPlayer(), scaling);

		for (Coins c : world.getCoins()) {
			drawImageOn(g2d,(Coins) c, coinsImg, scaling);
		}

		for (Hurdles h : world.getHurdles()) {
			drawImageOn(g2d,  h, hurdlImg, scaling);
		}
		
	}
	/**
	 * 
	 * @param g
	 */
	protected void transform(Graphics2D g) {

		final int w = this.getWidth();
		final int h = this.getHeight();

		// before we render everything im going to flip the y axis and move the
		// origin to the center (instead of it being in the top left corner)
		AffineTransform yFlip = AffineTransform.getScaleInstance(1, -1);
		AffineTransform move = AffineTransform.getTranslateInstance(w / 2, -h / 2);
		g.transform(yFlip);
		g.transform(move);

	}
	
	/**
	 * 
	 * @param g
	 * @param p
	 * @param scale
	 */
	private void drawPlayer(Graphics2D g, Player p, Double scale) {	
		drawImageOn(g, p, player.getSprite(), scale);
		drawLife(g, p);
	}
	
	/**
	 * 
	 * @param g
	 * @param p
	 * @param img
	 * @param scale
	 */
	private void drawImageOn(Graphics2D g, Body p, BufferedImage img, double scale) {

		AffineTransform ot = g.getTransform();

		Transform transform = p.getTransform();
		AffineTransform lt = new AffineTransform();
		lt.translate(transform.getTranslationX() * scale, transform.getTranslationY() * scale);
		lt.rotate(transform.getRotation());

		g.transform(lt);
		
		Convex convex = p.getFixture(0).getShape();
		
		if (convex instanceof Rectangle) {
			Rectangle r = (Rectangle)convex;
			Vector2 c = r.getCenter();
			double w = r.getWidth();
			double h = r.getHeight();
			g.drawImage(img, 
					(int)Math.ceil((c.x - w / 2.0) * scale),
					(int)Math.ceil((c.y + h / 2.0) * scale),
					(int)Math.ceil(w * scale),
					-(int)Math.ceil(h * scale),
					null);
		} else if (convex instanceof Circle) {
			// cast the shape to get the radius
			Circle c = (Circle) convex;
			double r = c.getRadius();
			Vector2 cc = c.getCenter();
			int x = (int)Math.ceil((cc.x - r) * scale);
			int y = (int)Math.ceil((cc.y + r) * scale);
			int w = (int)Math.ceil(r * 2 * scale);
				// lets us an image instead
				g.drawImage(img, x, y, w, -w, null);
		}

		g.setTransform(ot);

	}

	/**
	 * 
	 * @param g
	 * @param p
	 */
	private void drawLife(Graphics2D g, Player p) {

		Vector2 c = p.getWorldCenter();
		double rel = p.getCurrentHP()/p.getMaxHP();
		g.setColor(Color.BLACK);
		g.fillRect((int)((c.x-0.5)*scaling), 
				(int)((c.y-0.8)*scaling), 
				(int)scaling, 
				(int)(0.25*scaling));
		g.setColor(Color.GREEN);
		g.fillRect((int)((c.x-0.5)*scaling), 
				(int)((c.y-0.8)*scaling), 
				(int)(rel*scaling), 
				(int)(0.25*scaling));

	}
	
	/**
	 * 
	 */
	@Override
	public void update() {
		moveBackground();
		player.update();
		repaint();
	}
	
	/**
	 * DA FIXARE
	 */
    private void initImagePoints() {
        dx1 = -250;
        dy1 = -400;
        dx2 = -250 + DIM_W;
        dy2 = -400 + DIM_H;
        srcx1 = 0;
        srcy1 = 0;
        srcx2 = DIM_W;
        srcy2 = DIM_H;
    }
	
    /**
     * DA FIXARE
     */
	public void moveBackground() {
        if (srcy1 >  IMAGE_HEIGHT) {
            srcy1 = 0;
            srcy2 = DIM_H;
        } else {
            srcy1 += INCREMENT;
            srcy2 += INCREMENT;
        }
    }

}
