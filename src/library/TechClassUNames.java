package library;

import java.util.ArrayList;
import java.util.Random;


public class TechClassUNames {
	//variables:
	private static Random nameRand = new Random();
	
	
	@SuppressWarnings("serial")
	private static ArrayList<String> femaPrefix = new ArrayList<String>() {
		{
			add("Ма");
			add("О");
			add("На");
			add("Ю");
			add("А");
			add("И");
		}
	};
	
	@SuppressWarnings("serial")
	private static ArrayList<String> femaBase = new ArrayList<String>() {
		{
			add("рин");
			add("кс");
			add("та");
			add("ня");
			add("ли");
			add("ль");
		}
	};
	
	@SuppressWarnings("serial")
	private static ArrayList<String> femaPostfix = new ArrayList<String>() {
		{
			add("я");
			add("а");
			add("н");
			add("та");
			add("га");
			add("ша");
		}
	};
	
	
	@SuppressWarnings("serial")
	private static ArrayList<String> malePrefix = new ArrayList<String>() {
		{
			add("А");
			add("Ро");
			add("Ки");
			add("Ан");
			add("Ги");
			add("Де");
		}
	};
	
	@SuppressWarnings("serial")
	private static ArrayList<String> maleBase = new ArrayList<String>() {
		{
			add("лекс");
			add("м");
			add("ри");
			add("дер");
			add("де");
			add("ре");
		}
	};
	
	@SuppressWarnings("serial")
	private static ArrayList<String> malePostfix = new ArrayList<String>() {
		{
			add("ан");
			add("он");
			add("ми");
			add("г");
			add("ин");
			add("ст");
		}
	};
	
	
	//fema names:
	private static String femaNamePrefix() {return femaPrefix.get(nameRand.nextInt(femaPrefix.size()));}
	
	private static String femaNameBase() {return femaBase.get(nameRand.nextInt(femaBase.size()));}
	
	private static String femaNamePostfix() {return femaPostfix.get(nameRand.nextInt(femaPostfix.size()));}
	
	//male names:
	private static String maleNamePrefix() {return malePrefix.get(nameRand.nextInt(malePrefix.size()));}
	
	private static String maleNameBase() {return maleBase.get(nameRand.nextInt(maleBase.size()));}
	
	private static String maleNamePostfix() {return malePostfix.get(nameRand.nextInt(malePostfix.size()));}

	//returns:
	public static String newFemaName() {return femaNamePrefix() + femaNameBase() + femaNamePostfix();}

	public static String newMaleName() {return maleNamePrefix() + maleNameBase() + maleNamePostfix();}
}
