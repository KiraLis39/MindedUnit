package unit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import com.harium.etyl.sound.MultimediaLoader;
import com.harium.etyl.sound.model.Sound;
import core.Events.ColEvent;
import core.interfaces.Collable;
import enums.MediaConst;
import fox.Out;
import fox.ResourceCache;
import gui.UnitFrame;
import library.TechClassUNames;


public class Unit extends UnitsBase {
	Ellipse2D unitBody, unitAura;
	Random rVector, rDistance;
	int moveVector = 0, distanceLenght = 1;
	Boolean inCollision = false;
	Color auraColor = new Color(0.0f, 1.0f, 0.3f, 0.5f);
	Sound defaultSound, dieSound, hurtSound;
	MultimediaLoader mml = new MultimediaLoader();
	
	private Unit() {
		NAME = TechClassUNames.newFemaName();
		ID = universalRandom.nextInt(100);
		familyStamp = Math.abs(universalRandom.nextLong());
		Out.Print("\nCreating random parent " + NAME + " (ID:" + ID + "); familyStamp: " + familyStamp);
	}
	
	public Unit(int id, String name, short credentialsLevel, Unit unit0, Unit unit1) {
		this(id, "noTitle", name, Gender.FEMA, Color.MAGENTA, credentialsLevel, -1, new Point2D.Double(139, 139), unit0, unit1);
	}

	public Unit(int id, String title, String name, Gender sex, short credentialsLevel, Unit unit0, Unit unit1) {
		this(id, title, name, sex, sex == Gender.FEMA ? Color.MAGENTA : Color.CYAN, credentialsLevel, -1, new Point2D.Double(139, 139), unit0, unit1);
	}
	
	public Unit(int id, String title, String name, Gender sex, short credentialsLevel, int hp, Point2D coordinates, Unit unit0, Unit unit1) {
		this(id, title, name, sex, sex == Gender.FEMA ? Color.MAGENTA : Color.CYAN, credentialsLevel, hp, coordinates, unit0, unit1);
	}
	
	public Unit(int id, String title, String name, Gender sex, Color color, short credentialsLevel, int hp, Point2D coordinates, Unit unit0, Unit unit1) {
		ID = id;
		NAME = name;
		CREDENTIALS = credentialsLevel;
//		WIDTH = 20D;
//		HEIGHT = 20D;
		HP = hp;
		AGE = 0;
		GENDER = sex;
		centerPoint = new Point2D.Double((coordinates.getX() + WIDTH) / 2, (coordinates.getY() + HEIGHT) / 2);
		if (color == null) {
			if (sex == Gender.FEMA) {COLOR = Color.MAGENTA;
			} else {COLOR = Color.CYAN;}
		} else {COLOR = color;}
		
		if (unit0 != null && unit0.getGender().equals(Gender.FEMA)) {mother = unit0;
		} else if (unit0 != null && unit0.getGender().equals(Gender.MALE)) {
			father = unit0;
			familyStamp = father.familyStamp;
		} else {mother = new Unit();}
		
		if (unit1 != null && unit1.getGender().equals(Gender.MALE)) {
			father = unit1;
			familyStamp = father.familyStamp;
		} else if (unit1 != null && unit1.getGender().equals(Gender.FEMA)) {mother = unit1;
		} else {
			father = new Unit();
			familyStamp = father.familyStamp;
		}
		
		if (unit0 != null && unit1 != null) {
			unit0.addChild(this);
			unit1.addChild(this);
		}
		
		onCreate();
	}

	
	@Override
	public void onCreate() {
		rVector = new Random();
		rDistance = new Random();

		dieSound = new Sound(ResourceCache.getFile(MediaConst.dieVoiceUnitSound.name()).toString());
		defaultSound = new Sound(ResourceCache.getFile(MediaConst.defaultVoiceUnitSound.name()).toString());
		hurtSound = new Sound(ResourceCache.getFile(MediaConst.hurtVoiceUnitSound.name()).toString());

		mml.loadSound(dieSound.getPath());
		mml.loadSound(defaultSound.getPath());
		mml.loadSound(hurtSound.getPath());
		
		unitBody = new Ellipse2D.Double(centerPoint.getX() - WIDTH / 2, centerPoint.getY() - HEIGHT / 2, WIDTH, HEIGHT);
		unitAura = new Ellipse2D.Double(centerPoint.getX() - WIDTH / 2 - 2, centerPoint.getY() - HEIGHT / 2 - 2, WIDTH + 4, HEIGHT + 4);
	
		Out.Print("Unit: " + NAME + " (ID:" + ID + ") was created!");
		Out.Print("His parents is: " + mother.NAME + " as Mother, " + father.NAME + " as Father.\n");
	}
	
	@Override
	public Graphics2D draw(Graphics2D g2D) {
		if (DESTROYED) {throw new RuntimeException("This Unit has been destroyed already! How it draws?..");}
		
		unitBody = new Ellipse2D.Double(centerPoint.getX() - WIDTH / 2, centerPoint.getY() - HEIGHT / 2, WIDTH, HEIGHT);
		unitAura = new Ellipse2D.Double(centerPoint.getX() - WIDTH / 2 - 2, centerPoint.getY() - HEIGHT / 2 - 2, WIDTH + 4, HEIGHT + 4);
				
		moveVector = rVector.nextInt(4);
//		distanceLenght = rDistance.nextInt(4) + 1;
		distanceLenght = 1;
	
		switch (moveVector) {
			case 0:	if (centerPoint.getX() - distanceLenght > WIDTH / 2) centerPoint.setLocation(centerPoint.getX() - distanceLenght, centerPoint.getY());
				break;
			case 1:	if (centerPoint.getY() + distanceLenght < UnitFrame.getParentDimension().height - HEIGHT * 3) centerPoint.setLocation(centerPoint.getX(), centerPoint.getY() + distanceLenght);
				break;
			case 2:	if (centerPoint.getX() + distanceLenght < UnitFrame.getParentDimension().width - WIDTH / 2) centerPoint.setLocation(centerPoint.getX() + distanceLenght, centerPoint.getY());
				break;
			case 3:	if (centerPoint.getY() - distanceLenght > 30 + HEIGHT / 2) centerPoint.setLocation(centerPoint.getX(), centerPoint.getY() - distanceLenght);
				break;
		default: break; //to stay on the one place.
		}
	
		if (inCollision) {
			g2D.setColor(auraColor);
			g2D.fill(unitAura);
		}
		g2D.setColor(COLOR);
		g2D.fill(unitBody);
		
		g2D.setColor(Color.BLACK);
		g2D.drawString(getID() + "", (int) unitBody.getCenterX() - 4, (int) unitBody.getCenterY() + 5);

		inCollision = false;
		return g2D;
	}

	
	@Override
	public void onOutcomeDamage() {
		auraColor = new Color(1.0f, 0.0f, 0.0f, 0.5f);
	}

	@Override
	public void onIncomeDamage() {
		if (HP > 0) {
			hurtSound.play();
			HP--;
		} else {onDie();}		
	}

	@Override
	public void say() {defaultSound.play();}
	
	
	@Override
	public void onDie() {
		if (isDestroyed()) {return;}
		if (HP > 0) {throw new RuntimeException("Unit: destroy: HP > 0, but unit is die?..");}
		
		DESTROYED = true;
		dieSound.play();

		destroy();
	}

	private void destroy() {
		Out.Print("Unit " + getName() + " is dead now...");
		closeMemory();

//		Удаление данных с БД.
//		String deleteTableSQL = "DELETE DBUSER WHERE USER_ID = 1";
		
//		Обновление данных в БД.
//		String updateTableSQL = "UPDATE DBUSER SET USERNAME = 'mkyong_new' WHERE USER_ID = 1";
		
//		Получение данных с БД.
//		String selectTableSQL = "SELECT USER_ID, USERNAME from DBUSER";
		
//		// И если что-то было получено то цикл while сработает   
//		while (rs.next()) {
//			String userid = rs.getString("USER_ID");
//			String username = rs.getString("USERNAME");
//
//			System.out.println("userid : " + userid);
//			System.out.println("username : " + username);
//		}
		
//		Интерфейс PreparedStatement расширяет Statement 
//		и используется для прекомпилируемых sql запросов.
//		Он используется при множественном выполнении 
//		одного и того же запроса (с одинаковыми или разными параметрами):
		
//		//создаем PreparedStatement. Знаки вопроса - это параметры прекомпилируемого запроса
//		PreparedStatement ps = con.prepareStatement(
//			"INSERT INTO payment (contract_id, summa, date) VALUES (?, ?, '2007-02-12')"
//		);
//		ps.setInt( 1, 5 );	//устанавливаем первое значение (contract_id)
//		ps.setFloat( 2, 25.50 );	//устанавливаем второе значение (summa)
//		ps.executeUpdate();	//выполняем sql запрос.
	}

	public void defeat() {
		// for kill the Unit by special case:
		destroy();
	}
	
	
	public Boolean isInCollision() {return inCollision;}
	
	@Override
	public Rectangle2D getBodyRect2D() {
		if (unitBody == null) {throw new RuntimeException("Unit: getBodyRect2D: unitBody of " + getName() + " is NULL. You are can`t get his bounds.");}
		return unitBody.getBounds2D();
	}

	@Override
	public void onCollision(ColEvent event, Collable colladeObject) {
		inCollision = true;
		
		if (event.collisionType().equals(CollableType.UNIT)) {
			if (this.isFamily((Unit) colladeObject)) {
				//IS family:
				auraColor = new Color(0.0f, 1.0f, 0.3f, 0.5f);
//				Out.Console(NAME + " and " + ((Unit) colladeObject).getName() + " is family.\n"
//						+ "Long is " + ((Unit) colladeObject).familyStamp + "x" + this.familyStamp + "\n");
			} else {
				if (getGender().equals(((Unit) colladeObject).getGender())) {
					//if collable Unit has some sex as mine:
					((Unit) colladeObject).onIncomeDamage();
					this.onOutcomeDamage();
				} else {
					//if collable Unit has another sex as mine:
					auraColor = new Color(1.0f, 0.25f, 1.0f, 0.5f);
					if (getLoveLevel() < 100) {changeLoveLevel(1);
					} else {
						UnitFrame.createNewOne(Unit.this, (Unit) colladeObject);
						changeLoveLevel(-99);
					}
				}
			}
		} else if (event.collisionType().equals(CollableType.CONSTRUCTION)) {
			Out.Print("Unit: onCollision: methode 1 CONSTRUCTION");
		} else if (event.collisionType().equals(CollableType.GROUND)) {
			Out.Print("Unit: onCollision: methode 1 GROUND");
		} else {
			Out.Print("Unit: onCollision: methode 1 NONE");
			throw new RuntimeException("UNKNOWN COLLIZION TYPE in Unit:onCollision():");
		}
	}

	private void addChild(Unit unit) {children.add(unit);}
	void removeChild(Unit unit) {children.remove(unit);}
	public boolean isChildExist() {	return !children.isEmpty();}
	
	public boolean isParent(Unit unit) {return this.equals(unit.getMother()) || this.equals(unit.getFather());}

	public void changeFamilyStamp(Long familyStamp) {this.familyStamp = familyStamp;}
	public Boolean hasFamily() {return father != null || mother != null || !children.isEmpty();}
	public Boolean isFamily(Unit unit) {
		if (this.getFamilyStamp() == null) {throw new RuntimeException("familyStamp of unit " + NAME + " is NULL");}
		return unit.getFamilyStamp() == this.getFamilyStamp();
	}

	public void closeMemory() {
		g2D = null;
		unitBody = null;
		unitAura = null;
		centerPoint = null;
		rVector = null;
		rDistance = null;
		inCollision = null;
		defaultSound = null;
		dieSound = null;
		
		NAME = null;
		COLOR = null;
		WIDTH = null;
		HEIGHT = null;
		centerPoint = null;
	}
}
