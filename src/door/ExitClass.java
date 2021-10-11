package door;

import fox.adds.Out;


public class ExitClass {
	public static void Exit(int exitCode) {
		
		Out.Print("Programm is exit with the ErrorCode: " + exitCode);
		
		try {
//			UnitsBD.endSqlConnection();
			System.exit(exitCode);
		} catch(Exception se) {
			se.printStackTrace();
			System.exit(-exitCode);
		}
		
	}
}
