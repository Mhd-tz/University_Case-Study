/*****************************************
 * 
 * 				Cat Section
 * 
 * This is the panel section where all cat's method and rendering has been set.
 * The cat will rotate to the target's position and also its eyes and ears will rotate by moving to left or right.
 * The spots on its back will be randomly respawn.
 * It will chase the rabbits and its speed will decrease by gaining weight(scaling up).
 * 
 *****************************************/

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;


public class Cat {
	
	private Arc2D.Double lEar;
	private Arc2D.Double lEarIn;
	private Arc2D.Double rEar;
	private Arc2D.Double rEarIn;
	private Ellipse2D.Double head;
	private Ellipse2D.Double lEye;
	private Ellipse2D.Double rEye;
	private Ellipse2D.Double eyeDot;
	private Ellipse2D.Double eyeDotR;
	private Arc2D.Double nose;
	private Ellipse2D.Double body;
	private Rectangle2D.Double tail;
	private Ellipse2D.Double dot1;
	private Ellipse2D.Double dot2;
	private Ellipse2D.Double dot3;
	
	private PVector pos,vel;
	private int bodyW, bodyH;
	private float scale = 1;
	private int dir = 1;
	private double angle;
	private int rand;
	private float maxSpeed; // speed limit
	
	public Cat(PVector pos, PVector vel, int w, int h, float scale) {
		this.pos = pos;
		this.vel = vel;
		this.bodyW= w;
		this.bodyH = h;
		this.scale = scale;
		lEar = new Arc2D.Double();
		lEarIn = new Arc2D.Double();
		rEar = new Arc2D.Double();
		rEarIn = new Arc2D.Double();
		head = new Ellipse2D.Double();
		lEye = new Ellipse2D.Double();
		rEye = new Ellipse2D.Double();
		eyeDot = new Ellipse2D.Double();
		eyeDotR = new Ellipse2D.Double();
		nose = new Arc2D.Double();
		body = new Ellipse2D.Double();
		tail = new Rectangle2D.Double();
		dot1 = new Ellipse2D.Double();
		dot2 = new Ellipse2D.Double();
		dot3 = new Ellipse2D.Double();
		rand = (int) Random.random(100);
		// attributes set by default
		maxSpeed = 4;
	}
	
	public void setHeadAttributes() {
		head.setFrame(-bodyW/2,-bodyH /2, bodyW, bodyH );
		lEar.setArc(-bodyW/4,-bodyH/2.5, bodyW/2, bodyH/3, 90, 180, Arc2D.PIE);
		lEarIn.setArc(-bodyW/6,-bodyH/3, bodyW/3, bodyH/5, 90, 180, Arc2D.PIE);
		rEar.setArc(-bodyW/4,-bodyH/8, bodyW/2, bodyH/3, 90, 180, Arc2D.PIE);
		rEarIn.setArc(-bodyW/6,-bodyH/9, bodyW/3, bodyH/5, 90, 180, Arc2D.PIE);
		rEye.setFrame(bodyW/9,-bodyH /3, bodyW/4, bodyH/4);
		eyeDot.setFrame(bodyW/5,-bodyH /3.5, bodyW/7, bodyH/7);
		lEye.setFrame(bodyW/9,bodyH /9, bodyW/4, bodyH/4);
		eyeDotR.setFrame(bodyW/5,bodyH /7, bodyW/7, bodyH/7);
		nose.setArc(bodyW/3, -bodyH/7, bodyW/3, bodyH/3, -90, 180, Arc2D.PIE);
	}
	
	public void setBodyAttributes() {
		body.setFrame(-bodyW*1.1,-bodyH /2.5, bodyW, bodyH/1.2);
		tail.setRect(-bodyW*1.3,-bodyH /7, bodyW/2, bodyH/5);
		dot1.setFrame(-bodyW/1.1, -bodyH/3.5, bodyW/5, bodyH/5);
		dot2.setFrame(-bodyW/1.2, bodyH/9, bodyW/5, bodyH/5);
		dot3.setFrame(-bodyW/1.5, -bodyH/7, bodyW/5, bodyH/5);
	}
	
	public void render(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.rotate(vel.heading());
		g2.scale(scale, scale);
		if (vel.x < 0) g2.scale(1, -1);
		setHeadAttributes();
		setBodyAttributes();
		g2.setColor(Color.black);
		g2.draw(tail);
		g2.draw(body);
		g2.setColor(Color.DARK_GRAY);
		g2.fill(body);
		g2.fill(tail);
		g2.fill(head);
		g2.setColor(Color.black);
		g2.draw(head);
		g2.fill(nose);
		g2.draw(lEar);
		g2.draw(rEar);
		g2.fill(rEye);
		g2.fill(lEye);
		g2.setColor(Color.DARK_GRAY);
		g2.fill(lEar);
		g2.fill(rEar);
		g2.setColor(Color.white);
		g2.fill(eyeDot);
		g2.fill(eyeDotR);
		if(rand > 60) {
			g2.fill(dot1);	
		}
		if(rand < 50 || rand>60) {
			g2.fill(dot2);	
		}
		if(rand < 20 || rand > 60) {
			g2.fill(dot3);	
		}
		g2.setColor(new Color(255, 207, 221));
		g2.fill(lEarIn);
		g2.fill(rEarIn);
		g2.setTransform(at);
	}
	
	public void move() {
		pos.add(vel);
		edgeDetection();
		
		// walk randomly
		angle += 0.04 * dir;
		if (Math.random() * 32 < 1) {
			dir *= -1;
		}
		vel.set((float) (1 * Math.cos(angle)), (float) (1 * Math.sin(angle)));
	}
	
	private void edgeDetection() {
		if (pos.x + (bodyW / 2 + bodyW / 4) * scale > (ForestPanel.FOREST_X + ForestPanel.FOREST_W)) {
			pos.x = ForestPanel.FOREST_X + ForestPanel.FOREST_W - (bodyW / 2 + bodyW / 4) * scale;
			vel.x = -vel.x;
		}
		if (pos.x - (bodyW / 2 + bodyW / 4) * scale < ForestPanel.FOREST_X) {
			pos.x = ForestPanel.FOREST_X + (bodyW / 2 + bodyW / 4) * scale;
			vel.x = -vel.x;
		}
		if (pos.y + (bodyW / 2 + bodyW / 4) * scale > (ForestPanel.FOREST_Y + ForestPanel.FOREST_H)) {
			pos.y = ForestPanel.FOREST_Y + ForestPanel.FOREST_H - (bodyW / 2 + bodyW / 4) * scale;
			vel.y = -vel.y;
		}
		if (pos.y - (bodyW / 2 + bodyW / 4) * scale < ForestPanel.FOREST_Y) {
			pos.y = ForestPanel.FOREST_Y + (bodyW / 2 + bodyW / 4) * scale;
			vel.y = -vel.y;
		}
	}
	
	public void attractedBy(Rabbit target) {
		PVector path = PVector.sub(target.getPos(), pos);
		vel = path.limit(maxSpeed);

	}
	
	public Rabbit findClosestFood(ArrayList<Rabbit> fList) {
		Rabbit closestFood = null;

		if (fList.size() > 0) {
			closestFood = fList.get(0);
			float closestDist = PVector.dist(this.getPos(), closestFood.getPos());

			for (Rabbit f : fList)
				if (PVector.dist(this.getPos(), f.getPos()) < closestDist) {
					closestFood = f;
					closestDist = PVector.dist(this.getPos(), closestFood.getPos());
				}
		}

		return closestFood;
	}
	//Chase the Rabbits/Mouse
//	public void chaseMouse(Rabbit other) {
//		float diffX = pos.x - other.getPosX() - 8;
//		float diffY = pos.y - other.getPosY() - 8;
//		float distance = (float)Math.sqrt((pos.x - other.getPosX()) * (pos.x-other.getPosX()) + (pos.y - other.getPosY()) * (pos.y - other.getPosY()));
//		
//		if(distance < 1000) {
//			if(scale > 1.9) {
//				vel.x = (float)((-5.0/distance) *diffX);
//				vel.y = (float)((-5.0/distance) *diffY);
//			}
//			vel.x = (float)((-10.0/distance) *diffX);
//			vel.y = (float)((-10.0/distance) *diffY);
//		}
//	}
	
	public void setPos(int x, int y) {
		pos.x = x;
		pos.y = y;
	}
	public void getBigger() {
		if(scale < 2.1) {
			scale = (float) (scale + 0.1);
			}
	}
	
	public float getPosX() {
		return pos.x;
	}
	public float getPosY() {
		return pos.y;
	}
	public int getBodyH() {
		return bodyH;
	}
	public int getBodyW() {
		return bodyW;
	}
	
	public float getScale() {
		return scale;
	}
	
	public PVector getPos() {
		return pos;
	}
}
