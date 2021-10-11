package unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import core.interfaces.Collable;
import kuusisto.tinysound.TinySound;


public abstract class UnitsBase implements Units, Collable {
	
	protected Gender GENDER = Gender.MALE;
	protected CollableType TYPE = CollableType.UNIT;
	
	protected Random universalRandom;
	
	protected UnitsBase mother;
	protected UnitsBase father;
	protected ArrayList<UnitsBase> children = new ArrayList<UnitsBase>();
	
	protected Graphics2D g2D;
	protected Point2D centerPoint;
	protected Boolean DESTROYED = false;
	
	protected UnitsBase() {
		TinySound.init();
		universalRandom = new Random();
	}
	
	public void say(File sound) {TinySound.loadSound(sound).play();}
	
	//identification number:
	protected int ID;

	//health:
	protected float HP = 100;
	
	//energy:
	protected float ENERGY = 100;
	
	//age:
	protected int AGE = 0;
	
	//love level:
	protected int LOVE = 0;
	
	//access and power level:
	protected short CREDENTIALS;
	
	//units name:
	protected String NAME;
	
	//units name:
	protected Color COLOR;
	
	//starts dimension:
	protected Double WIDTH = 10.0D;
	protected Double HEIGHT = 10.0D;
	
	// build DNA long:
	protected Long GENOTYPE;
	
	// marker of family:
	protected Long familyStamp;
	
	
	public Gender getGender() {return GENDER;}
	public CollableType getType() {return TYPE;}
	
	public UnitsBase getFather() {return father;}
	public UnitsBase getMother() {return mother;}
	
	public int getID() {return ID;}

	public float getHP() {return HP;}

	public float getEnergy() {return ENERGY;}
	public void changeEnergy(float en) {ENERGY += en;}
	
	public int getAge() {return AGE;}
	
	public int getLoveLevel() {return LOVE;}
	public void changeLoveLevel(int llevelModificator) {LOVE += llevelModificator;}
	
	public String getName() {return NAME;}
	
	public short getCredentials() {return CREDENTIALS;}
	
	public Point2D getCenterPoint2D() {return centerPoint;}
	
	public Boolean isDestroyed() {return DESTROYED;}
	
	public Object getGenotype() {return GENOTYPE;}
	
	public Long getFamilyStamp() {return familyStamp;}
}
