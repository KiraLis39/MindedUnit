package door;

import java.io.File;
import enums.MediaConst;
import fox.Out;
import fox.ResourceCache;
import gui.UnitFrame;


public class MainClass {	
	public static void main(String[] args) {
		init();
		showFrame();
	}

	private static void init() {
		Out.Print("Initialization...");

		Out.setErrorLevel(Out.LEVEL.DEBUG);
		Out.setLogsCoutAllow(3);

		try {
			ResourceCache.add(MediaConst.defaultVoiceUnitSound.name(), new File("./media/sound/units/default.wav"));
			ResourceCache.add(MediaConst.dieVoiceUnitSound.name(), new File("./media/sound/units/die.wav"));
			ResourceCache.add(MediaConst.hurtVoiceUnitSound.name(), new File("./media/sound/units/hurt.wav"));
		} catch (Exception e) {e.printStackTrace();}
		
		try {
			ResourceCache.add("buttonIcon00", new File("./media/pictures/icons/buttonIcon00.png"));
			ResourceCache.add("buttonIcon01", new File("./media/pictures/icons/buttonIcon01.png"));
			ResourceCache.add("buttonIcon02", new File("./media/pictures/icons/buttonIcon02.png"));
			ResourceCache.add("buttonIcon03", new File("./media/pictures/icons/buttonIcon03.png"));
		} catch (Exception e) {e.printStackTrace();}
	}

	private static void showFrame() {
		new UnitFrame();
	}
}
