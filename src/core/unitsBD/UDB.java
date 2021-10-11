package core.unitsBD;

import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import fox.adds.Out;
import unit.Unit;


public class UDB {
	private static final String url = "jdbc:mysql://localhost:3306/JavaDB?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String user = "root";
	private static final String password = "00000";
	
	private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private String optionsTable = "optionsData", unitsTable = "unitsData";
	
    
	public UDB() {
		init();
	}
	
	void init() {
		try {con = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			Out.Print("Connection to DB failed! Exit from the Programm...");
			e.printStackTrace();
			System.exit(1);
		}
		
		sqlExecuteCommander("CREATE TABLE IF NOT EXISTS `" + optionsTable + "` ("
	              + "`UnitsCount` 			MEDIUMINT(7) 		UNSIGNED 			NOT NULL, "
	              + "`WorldStatement` 	BIT(2) 						NOT NULL 			DEFAULT b'0', "
	              + "`WorldLive`		 		TIMESTAMP 			NOT NULL"
	              + ") "
	              + "COLLATE='utf8_general_ci';");
		
		sqlExecuteCommander("CREATE TABLE IF NOT EXISTS `" + unitsTable + "` ("
                + "`UnitID` 				MEDIUMINT(7) 		UNSIGNED 			NOT NULL											AUTO_INCREMENT		COMMENT 'ID of the Unit', "
                + "`UnitName` 			VARCHAR(30)			NOT NULL 			COMMENT 	'NAME of the Unit', "
                + "`UnitTitle` 			TINYTEXT 				NOT NULL			DEFAULT  	'savage'								COMMENT 'Title of the Unit', "
                + "`UnitHP` 				SMALLINT(3)		 	UNSIGNED 			NOT NULL 											DEFAULT '100' 				COMMENT 'HP of the Unit', "
                + "`UnitCredentials` BIT(2) 						NOT NULL 			DEFAULT b'0' 										COMMENT 'A Power level of Unit', "
                + "`UnitDimension` 	TINYTEXT 				NOT NULL 			DEFAULT '10x10' 								COMMENT 'Size dimension of Unit', "
                + "`UnitPoint` 			TINYTEXT 				NOT NULL			DEFAULT '100x100' 							COMMENT 'Center point2D of Unit', "
                + "`UnitBirthDate` 	TIMESTAMP 			NOT NULL 			COMMENT 'The birth day', "
                + "PRIMARY KEY (`UnitID`, `UnitName`), 							UNIQUE INDEX `UnitID` (`UnitID`)"
                + ") "
                + "COLLATE='utf8_general_ci';");
	}

	
	private void sqlExecuteCommander(String command) {
		try {
            stmt = con.createStatement();
	        stmt.execute(command);
		} catch (SQLException e) {e.printStackTrace();
		} finally {closeBD();}
	}

	void sqlQueryCommander(String query) {
		try {
            stmt = con.createStatement();
	        stmt.executeQuery(query);
		} catch (SQLException e) {e.printStackTrace();
		} finally {closeBD();}
	}
	
	ResultSet sqlGetResultSet(String setquery) {
		try {
            stmt = con.createStatement();
	        stmt.executeQuery(setquery);
	        return rs;
		} catch (SQLException e) {e.printStackTrace();
		} finally {closeBD();}
		
		return null;		
	}
	
	private void closeBD() {
        try {stmt.close();} catch(Exception se) {}
        try {rs.close();} catch(Exception se) {}
	}

	
	
	Point2D getUnitsPointByName(String name) {
		return null;
	}
	
	String getUnitsNameByPoint(Point2D point) {
		return "no data";
	}
	
	short getUnitsIDByPoint(Point2D point) {
		return -1;
	}

	Point2D getUnitsPointByID(int id) {
		return null;
	}
	
	
	void add(Unit unit) {
		try {
            stmt = con.createStatement();

            stmt.execute("INSERT INTO `javadb`.`unitsdata` ("
            		+ "`UnitName`, `UnitHP`, `UnitBirthDate`"
            		+ ") VALUES ("
            		+ "'" + unit.getName() + "', '100', '" + new Date(System.currentTimeMillis()) + "'"
            		+ ");");
		} catch (SQLException sqlEx) {sqlEx.printStackTrace();
        } finally {closeBD();}
	}

	void remove(Unit unit) {
		
	}

	
	boolean hasUnitName(String name) {
		return false;
	}
	
	boolean hasUnitID(int id) {
		return false;
	}

	int unitsCount() {
		return 0;		
	}

	List<Unit> unitsList() {
		return null;
	}

	void endSqlConnection() {
		try {con.close();} catch(Exception se) {se.printStackTrace();}
	}

	void removeUnitData(Unit unitToRemove) {
		
	}

	int getUnitsHPByName(String name) {
		return 0;
	}
	
	int getUnitsHPByID(short id) {
		return 0;
	}
	
	int getUnitsHPByPoint(Point2D point) {
		return 0;
	}

	short getUnitsCredentialsbyPoint(Point2D centerPoint) {
		return 0;
	}
	
	short getUnitsCredentialsbyName(String name) {
		return 0;
	}

	double getUnitsWidthByPoint(Point2D centerPoint) {
		return 0;
	}

	double getUnitsHeightByPoint(Point2D centerPoint) {
		return 0;
	}

	double getUnitsWidthByID(short unitsIDByPoint) {
		return 0;
	}

	double getUnitsHeightByID(short unitsIDByPoint) {
		return 0;
	}
}