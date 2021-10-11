package unit;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;

import core.Events.ColEvent;
import core.interfaces.Collable;


public interface Units {
	enum Gender {MALE, FEMA};
	
	void onCreate();
	
	void onOutcomeDamage();
	void onIncomeDamage();
	
	Graphics2D draw(Graphics2D g2D);
	
	void onCollision(ColEvent event, Collable colladeObject);
	
	void say();
	void say(File sound);
	
	void onDie();
	
	int getID();
	int getAge();
	float getHP();
	float getEnergy();
	short getCredentials();
	
	String getName();
	Rectangle2D getBodyRect2D();
}
