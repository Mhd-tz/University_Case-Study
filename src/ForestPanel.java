/*****************************************
 * 
 * 				Panel
 * 
 * This is the panel section where all classes are called to be used in the panel.
 *****************************************/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import processing.core.PVector;

public class ForestPanel extends JPanel implements ActionListener {
	
	//Fields and Properties
	
	//ArrayLists
	private ArrayList<Food> foods;
	private ArrayList<Rabbit> rabbits;
	//Forest Position and size
	public final static int FOREST_MG = 50;
	public final static int FOREST_X = 50;
	public final static int FOREST_Y = 50;
	public final static int FOREST_H = 800;
	public final static int FOREST_W = 1200;
	//Number of the food and rabbits in the game;
	public final static int FOOD_COUNT = 20;
	public final static int RABBIT_COUNT = (int) Random.random(5, 8);
	//Timer for respawning.
	private int t = 0;
	//Timer for returning to hunting.
	private int tm = 0;
	private int tmb = 0;

	//Referencing the classes;
	private Environment forest;
//	private Cat cat;
	private Dimension paneSize;
	//FPS
	private Timer timer;
	
	//Panel Constructor
	public ForestPanel() {
		super();
		this.setBackground(java.awt.Color.white);
		paneSize = new Dimension(1350, 800);
		this.setPreferredSize(paneSize);
		//Cat properties disabled
//		float cScale = Random.random(1.3f, 1.5f);
//		float cSpdX = Random.random(-6.0f, 6.0f);
//		float cSpdY = Random.random(-6.0f, 6.0f);
//		float cLocX = FOREST_X + Random.random(50 * cScale, FOREST_W - 50 * cScale);
//		float cLocY = FOREST_Y + Random.random(50 * cScale, FOREST_H - 50 * cScale);
//		float cBdyWH = Random.random(30f, 40f);
		//Get the cat and others
//		cat = new Cat(new PVector(cLocX,cLocY), new PVector(cSpdX,cSpdY), (int)cBdyWH,(int)cBdyWH, cScale);
		forest = new Environment(FOREST_X, FOREST_Y, FOREST_W, FOREST_H, paneSize);
		rabbits = new ArrayList<Rabbit>();
		for(int i =0; i<RABBIT_COUNT; i++) {
			float mScale = Random.random(1f, 1.4f);
			float mLocX = (float) Random.random(100, paneSize.width - 100);
			float mLocY = (float) Random.random(100, paneSize.height - 100);
			float mSpdX = Random.random(-20.0f, 20.0f);
			float mSpdY = Random.random(-20.0f, 20.0f);
			float mBdyWH = 25f;
			rabbits.add(new Rabbit(new PVector(mLocX,mLocY), new PVector(mSpdX,mSpdY), (int)mBdyWH,(int)mBdyWH, mScale, paneSize));
		}
		
		foods = new ArrayList<Food>();
		for(int i =0; i<FOOD_COUNT; i++) {
			float fLocX = (float) Random.random(100, paneSize.width - 100);
			float fLocY = (float) Random.random(100, paneSize.height - 100);
			float fBdyWH = Random.random(15f, 30f);
			
			foods.add(new Food(new PVector(fLocX, fLocY), (int)fBdyWH,(int)fBdyWH));
		}
		//Setting the FPS
		timer = new Timer(33,this);
		timer.start();
        
		addMouseListener(new MyMouseListener());
	}
	
	//paintComponent to render the objects
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		pnlSize = getSize();
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		
		forest.render(g2);
		for (int i=0; i<rabbits.size(); i++) {
			rabbits.get(i).render(g2);
		}
		for(int i=0; i<foods.size();i++) {
			foods.get(i).render(g2);
		}
//		cat.render(g2);
		forest.respawnFeatures(g2);
	}
	
	//Setting the actions all transition in game.
	public void actionPerformed(ActionEvent e) {
		for(Rabbit r:rabbits) {
			Food foodi = null;
			Food rFood = r.traceBestFood(foods);
			rFood.setColor(r.getColor());
			for(int i =0; i < foods.size(); i++) {
				if(r.collides(foods.get(i))) {
					foodi = foods.get(i);
					break;
				}	
			}
			if(foodi != null) {
				foods.remove(foodi);
			}
			else {
				respawnNewFood();
			}
			for(Rabbit r2:rabbits) {
				if(r2 != r && r2.detectCollision(r)) {
					r2.checkOther(r);
					if(tm < 40) {
						tm++;
					}
					if(tm >= 40) {
						r2.traceBestFood(foods);
						tm = 0;
					}
				}
				else if(r2 != r && r2.detectColl(r)) {
					r2.bounceOther(r);
					if(tmb < 40) {
						tmb++;
					}
					if(tmb >= 40) {
						r2.traceBestFood(foods);
						tmb = 0;
					}
				}
			}
			r.hunt(rFood);
			r.move();
		}
		
		/*
		 * The cat feature has been disabled for this assignment due to the assignment requirements and stability.
		 * */
//		cat.move();
//		Rabbit mousy = null;
//		/* Get all the elements of the mouse/rabbit in rabbit's ArrayList size.
//		 * set the color of the first mouse/rabbit in the ArrayList to white so that the user knows which is first element.
//		 * let the mouse/rabbit move and do its usual methods.
//		 * The cat will find the closest mouse/rabbit as its target.
//		 * The cat will be get attracted to the first mouse/rabbit in rabbit's ArrayList.
//		 * The cat also attracts to its targets.
//		 * Once the cat has been collided with the mouse/ rabbit the rabbit/mouse in the ArrayList will be set as null.
//		 * and then destroyed(removed).*/
//		for(int i =0; i<rabbits.size(); i++) {
//			rabbits.get(i).move();
//			rabbits.get(i).escape(cat);
////			cat.chaseMouse(rabbits.get(i));
//			/* chase the first element of ArraylList by getting the element with index 0.*/
////			cat.attractedBy(rabbits.get(0));
////			Rabbit target = cat.findClosestFood(rabbits);
////			cat.attractedBy(target);
//			if(rabbits.get(i).getHit(cat)) {
//				cat.getBigger();
//				mousy = rabbits.get(i);
//				break;
//			}
//		}
//		if(mousy != null) {
//			rabbits.remove(mousy);
//		}
//		else {
//			respawnNewRabbit();
//		}
		
		/* Get all the elements of the Food in food's ArrayList size.
		 * Set the first element of the food array list to green so that the user will know which food is first in food's ArrayList
		 * The mouse/rabbit will be get attracted to the first food( green food) in food's ArrayList.
		 * The mouse/rabbit will get the closest food as its target.
		 * The mouse/rabbit also attracts to its targets.
		 * Once the food has been collided with the mouse/ rabbit the food in the ArrayList will be set as null
		 *  and then destroyed(removed).*/
//		Food foodi = null;
//		for(int i =0; i <foods.size();i++) {
//			for(int j =0; j< rabbits.size(); j++) {	
//				Food rFood = rabbits.get(j).traceBestFood(foods);
//				/* Set the desired food of ArrayList to same rabbits/mouse color.*/
//				rFood.setColor(rabbits.get(j).getColor());
//				rabbits.get(j).hunt(rFood);
//				if(rabbits.get(j).collides(foods.get(i))) {
//					foodi = foods.get(i);
//					break;
//				}	
//			}
//		}
//		if(foodi != null) {
//			foods.remove(foodi);
//		}
//		else {
//			respawnNewFood();
//		}
		repaint();
	}
	
	//respawnFood
	void respawnNewFood() {
		float scale = Random.random(1.2f, 1.5f);
		float fLocX = (float) Random.random(100, paneSize.width - 100);
		float fLocY = (float) Random.random(100, paneSize.height - 100);
		float fBdyWH = Random.random(15f, 30f);
		if(t < 60) {
			t++;
		}
		if(t>=60) {
			foods.add(new Food(new PVector(fLocX, fLocY), (int)fBdyWH,(int)fBdyWH));
			t = 0;
		}
	}
	
	//RespawnRabbits disabled
//	void respawnNewRabbit() {
//		float mScale = Random.random(1.1f, 1.3f);
//		float mLocX = FOREST_X + Random.random(50 * mScale, FOREST_W - 50 * mScale);
//		float mLocY = FOREST_Y + Random.random(50 * mScale, FOREST_H - 50 * mScale);
//		float mSpdX = Random.random(-10.0f, 10.0f);
//		float mSpdY = Random.random(-10.0f, 10.0f);
//		float mBdyWH = Random.random(20f, 27f);
//		
//		if(mT<25) {
//			mT++;
//		}
//		if(mT>=25) {
//			if(rabbits.size()<RABBIT_COUNT) {
//				rabbits.add(new Rabbit(new PVector(mLocX,mLocY), new PVector(mSpdX,mSpdY), (int)mBdyWH,(int)mBdyWH, mScale));
//				mT = 0;	
//			}
//		}
//	}
	
	private class MyMouseListener extends MouseAdapter{
		
		/* First we get all the elements of the food's ArrayList.
		 * Check the mouse if it hits a specified element using a public method in food class that can detect if the mouse has hit the element it is hitting. (checkHit())
		 * checkHit method has a parameter MouseEvent that passes all mouse actions.
		 * Get the element scale and pass it to a double variable.
		 * Check if the scale is less than the creature then add some to the scale.
		 * Set the scale variable to the element scale.
		 */
		public void mousePressed(MouseEvent e) {
			for(int i =0 ;i< foods.size(); i++) {
					if(foods.get(i).checkHit(e)) {
					double scale = foods.get(i).getScale();
					if(scale <= 2.6) {
						scale += 0.2;
						foods.get(i).setScale(scale);
					}
				}
			}
		}
		public void mouseClicked(MouseEvent e) {
			float fBdyWH = Random.random(15f, 30f);
			float mScale = Random.random(1f, 1.4f);
			float mSpdX = Random.random(-20.0f, 20.0f);
			float mSpdY = Random.random(-20.0f, 20.0f);
			float mBdyWH = 25f;
			if(e.getClickCount()==2) {
				foods.add(new Food(new PVector(e.getX(), e.getY()), (int)fBdyWH,(int)fBdyWH));	
			}
			if(e.getClickCount() == 2 && e.isAltDown()) {
				rabbits.add(new Rabbit(new PVector(e.getX(),e.getY()), new PVector(mSpdX,mSpdY), (int)mBdyWH,(int)mBdyWH, mScale, paneSize));
			}

			for(int i = 0; i <foods.size(); i++) {
				if(foods.get(i).checkHit(e)) {
					if(e.isControlDown())
					foods.remove(i);
				}
			}
			for (int j =0; j<rabbits.size(); j++) {
				if(rabbits.get(j).checkHit(e)) {
					if(e.isControlDown()) {
						rabbits.remove(j);
					}
				}
			}
		}
		
	}
		
}
