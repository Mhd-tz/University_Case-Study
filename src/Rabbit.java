/*
 * 
 * 				Rabbit Class
 * 
 * This is the Rabbit's section with all the properties and fields of it.
 * The Rabbit will move Randomly and will chase the cheese(Food) in the game.
 * It will escape from the cat if it has a chance to escape. The raspawning from other side of the map has been disabled due to Assignment requirement.
 * There is a range for the rabbit to chase the food or escape from the cat.
 * 
 * */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;

import javax.print.attribute.standard.MediaSize.Other;

import processing.core.*;

public class Rabbit {
	
	private Ellipse2D.Double lEar;
	private Ellipse2D.Double lEarIn;
	private Ellipse2D.Double rEar;
	private Ellipse2D.Double rEarIn;
	private Ellipse2D.Double head;
	private Ellipse2D.Double lEye;
	private Ellipse2D.Double rEye;
	private Ellipse2D.Double eyeDot;
	private Ellipse2D.Double eyeDotR;
	private Arc2D.Double nose;
	private Ellipse2D.Double body;
	private Ellipse2D.Double tail;
	private Area outline;
	private Arc2D.Double fov;
	private Dimension dimension;
	private Dimension panelSize;	//field to reference panel's dimension
	
	private PVector pos,vel;
	private int bodyW, bodyH;
	private float scale = 1;
	private int dir = 1;
	private double angle;
	private Color color;
	private float maxSpeed;
	
	public Rabbit(PVector pos, PVector vel, int w, int h, float scale, Dimension paneSize) {
		this.pos = pos;
		this.vel = vel;
		this.bodyW= w;
		this.bodyH = h;
		this.color = Random.randomColor();
		this.scale = scale;
		this.dimension = new Dimension(100,50);
		maxSpeed = (float) Random.random(4, 6);
		setShapeAttributes();
		
		this.panelSize = paneSize;
	}
	
	public void setShapeAttributes() {
		body = new Ellipse2D.Double(-bodyW*1.1,-bodyH /2.5, bodyW, bodyH/1.2);
		tail = new Ellipse2D.Double(-bodyW*1.3,-bodyH /5, bodyW/2.5, bodyH/2.5);
		head = new Ellipse2D.Double(-bodyW/2,-bodyH /2, bodyW, bodyH );
		lEar = new Ellipse2D.Double(-bodyW,-bodyH /2, bodyW, bodyH/2);
		lEarIn = new Ellipse2D.Double(-bodyW/1.4,-bodyH /2.5, bodyW/1.5, bodyH/3);
		rEar = new Ellipse2D.Double(-bodyW,bodyH /9, bodyW, bodyH/2);
		rEarIn = new Ellipse2D.Double(-bodyW/1.4,bodyH /5, bodyW/1.5, bodyH/3);
		rEye = new Ellipse2D.Double(bodyW/9,-bodyH /3, bodyW/4, bodyH/4);
		eyeDot = new Ellipse2D.Double(bodyW/5,-bodyH /3.5, bodyW/7, bodyH/7);
		lEye = new Ellipse2D.Double(bodyW/9,bodyH /9, bodyW/4, bodyH/4);
		eyeDotR = new Ellipse2D.Double(bodyW/5,bodyH /7, bodyW/7, bodyH/7);
		nose = new Arc2D.Double(bodyW/3, -bodyH/7, bodyW/3, bodyH/3, -90, 180, Arc2D.PIE);
		
		outline = new Area(head);
		outline.add(new Area(nose));
		outline.add(new Area(body));
		outline.add(new Area(tail));
		outline.add(new Area(rEar));
		outline.add(new Area(lEar));
		
		float sight = dimension.width * maxSpeed * .5f;
		fov = new Arc2D.Double(-sight, -sight, sight*2, sight*2, -55, 110, Arc2D.PIE);
	}
	
	
	public void render(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.rotate(vel.heading());
		g2.scale(scale, scale);
		if (vel.x < 0) g2.scale(1, -1);
		g2.setColor(Color.black);
		g2.draw(tail);
		g2.setColor(color);
		g2.fill(body);
		g2.fill(head);
		g2.setColor(Color.black);
		g2.draw(head);	
		g2.fill(nose);
		g2.draw(lEar);
		g2.draw(rEar);
		g2.fill(rEye);
		g2.fill(lEye);
		g2.setColor(color);
		g2.fill(lEar);
		g2.fill(rEar);
		g2.setColor(Color.white);
		g2.fill(eyeDot);
		g2.fill(eyeDotR);
		g2.setColor(color);
		g2.fill(tail);
		g2.setColor(new Color(255, 207, 221));
		g2.fill(lEarIn);
		g2.fill(rEarIn);
		
		g2.setColor(color.red);
		g2.draw(outline);
		g2.draw(fov);
		g2.setTransform(at);
	}
	
	public Shape getFOV() {
	      AffineTransform at = new AffineTransform();
	      at.translate(pos.x, pos.y);
	      at.rotate(vel.heading());
	      at.scale(scale, scale);
	      return at.createTransformedShape(fov);
	}
	
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(outline);
	}
	
	public void move() {
		vel.normalize().mult(maxSpeed);
		pos.add(vel);
		edgeDetection();
	}
	
	private void edgeDetection() {
		
		int forestX = ForestPanel.FOREST_X;
		int forestY = ForestPanel.FOREST_Y;
		int forestMG = ForestPanel.FOREST_MG;
		int forestW = panelSize.width-(forestX + forestMG);
		int forestH = panelSize.height-(forestY + forestMG);
		Rectangle2D.Double top = new Rectangle2D.Double(forestX, forestY-10, forestW, 10);
		Rectangle2D.Double bottom = new Rectangle2D.Double(forestX, panelSize.height-forestMG, forestW, 10);
		Rectangle2D.Double left = new Rectangle2D.Double(forestX-10, forestY, 10, forestH);
		Rectangle2D.Double right = new Rectangle2D.Double(panelSize.width-forestMG, forestY, 10, forestH);
		
		float coef = .1f;
		PVector accel = new PVector();
		
		if (getFOV().intersects(left)) accel.add(1,0);
		else if (getFOV().intersects(right)) accel.add(-1,0);
		else if (getFOV().intersects(top)) accel.add(0,1);
		else if (getFOV().intersects(bottom)) accel.add(0,-1);
		
		accel.mult(coef*maxSpeed);
		vel.add(accel);
		
		if (getBoundary().intersects(left) && vel.x < 0) {
			vel.x *= -1;
		}

		if (getBoundary().intersects(right) && vel.x > 0) {
			vel.x *= -1;
		}
		if (getBoundary().intersects(top) && vel.y < 0) {
			vel.y *= -1;
		}
		if (getBoundary().intersects(bottom) && vel.y > 0) {
			vel.y *= -1;
		}
	}
	public void checkOther(Rabbit target) {
		if(scale < target.scale) {
			PVector Target = new PVector(-target.getPos().x, -target.getPos().y);
			float coef = .1f;
			PVector direction = PVector.sub(Target, pos).normalize();
			PVector acceleration = PVector.mult(direction, maxSpeed*coef);
			vel.add(acceleration);
		} 
		else {
			PVector Target = new PVector(-target.getPos().x, -target.getPos().y);
			float coef = .1f;
			PVector direction = PVector.sub(Target, pos).normalize();
			PVector acceleration = PVector.mult(direction, maxSpeed*coef + (float) Math.PI);
			target.vel.add(acceleration);
			
		}
	}
	
	public boolean collides(Food food) {
		return (getBoundary().intersects(food.getBoundary().getBounds2D()) &&
				food.getBoundary().intersects(getBoundary().getBounds2D()) );
	}
	
	public boolean detectCollision(Rabbit other) {
		boolean hit = false;

		if (getFOV().intersects(other.getBoundary().getBounds2D())
				&& other.getFOV().intersects(getBoundary().getBounds2D()))
			hit = true;

		return hit;
	}
	
	public void bounceOther(Rabbit other) {
		float angle = (float) Math.atan2(pos.y - other.pos.y, pos.x - other.pos.x);
		if(scale < other.scale) {	
			vel = PVector.fromAngle(angle);
			vel.mult(maxSpeed);
		}
		else {
			other.vel = PVector.fromAngle(angle+(float)Math.PI);
			other.vel.mult(maxSpeed);
		}	
	}
	
	public boolean detectColl(Rabbit other) {
		boolean hit = false;

		if (getBoundary().intersects(other.getBoundary().getBounds2D())
				&& other.getBoundary().intersects(getBoundary().getBounds2D()))
			hit = true;

		return hit;
	}
	
	private float getAttraction(Food f) {
	    return (float) (f.getScale()*50/PVector.dist(pos, f.getPos()));
	}

	public void attractedBy(Food target) {
		float coef = .2f;	// coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector acceleration = PVector.mult(direction, maxSpeed*coef);
		vel.add(acceleration);
	}
	
	public Food traceBestFood(ArrayList<Food> fList) {
		Food target = null;
	    if (fList.size()>0) {
	        target = fList.get(0);
	        float targetAttraction = this.getAttraction(target);
	
	        for (Food f:fList)
	            if (this.getAttraction(f) > targetAttraction) {
	                target = f;
	                targetAttraction = this.getAttraction(target);
	            }
	        this.attractedBy(target);
	    }
	    return target;
	}
	
	public void hunt(Food target) { 
			if (collides(target)) {
				if(scale < 1.8)
					scale += target.getScale()*0.05*scale;
			}
	}
	
	public boolean checkHit(MouseEvent e) {
		return getBoundary().contains(e.getX(), e.getY());
	}
	
	public float getPosX() {
		return pos.x;
	}
	public float getPosY() {
		return pos.y;
	}
	
	public PVector getPos() {
		return pos;
	}
	public int getBodyH() {
		return bodyH;
	}
	public int getBodyW() {
		return bodyW;
	}
	
	public void setScale(double d) {
		scale += d;
	}
	public float getScale() {
		return scale;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
}
