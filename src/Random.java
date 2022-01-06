/******************************************************************
 * 
 * 		Random class for getting random colors and numbers
 * 
 * ****************************************************************/
import java.awt.Color;

import processing.core.PVector;

public class Random {
	public static double random(double low, double high) {
		return low + Math.random() * (high - low);
	}
	
	public static float random(float low, float high) {
		return (float) (low + Math.random() * (high - low));
	}
	
	public static float dist(PVector loc1, PVector loc2) {
		return Math.abs(loc1.x - loc2.x) + Math.abs(loc1.y - loc2.y) + Math.min(Math.abs(loc1.x - loc2.x), Math.abs(loc1.y - loc2.y))/2;
	}
	
	public static double random(int min, int max) {
		return Math.random()*(max-min)+min;
	}
	
	public static double random(int max) {
		return Math.random()*max;
	}
	
	public static Color randomColor() {
		int r = (int) random(255);
		int g = (int) random(255);
		int b = (int) random(255);
		
		return new Color(r,g,b);
	}
}
