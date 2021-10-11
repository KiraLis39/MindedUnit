package door;

import java.io.File;
import enums.MediaConst;
import fox.adds.Out;
import fox.builders.ResourceManager;
import gui.UnitFrame;


public class MainClass {	
	public static void main(String[] args) {
		Out.setDebugOn(false);
		init();
		showFrame();
	}

	private static void init() {
		Out.Print("Initialization...");

		try {
			ResourceManager.add(MediaConst.defaultVoiceUnitSound.name(), new File("./media/sound/units/default.wav"));
			ResourceManager.add(MediaConst.dieVoiceUnitSound.name(), new File("./media/sound/units/die.wav"));
			ResourceManager.add(MediaConst.hurtVoiceUnitSound.name(), new File("./media/sound/units/hurt.wav"));
		} catch (Exception e) {e.printStackTrace();}
		
		try {
			ResourceManager.add("buttonIcon00", new File("./media/pictures/icons/buttonIcon00.png"));
			ResourceManager.add("buttonIcon01", new File("./media/pictures/icons/buttonIcon01.png"));
			ResourceManager.add("buttonIcon02", new File("./media/pictures/icons/buttonIcon02.png"));
			ResourceManager.add("buttonIcon03", new File("./media/pictures/icons/buttonIcon03.png"));
		} catch (Exception e) {e.printStackTrace();}
	}

	private static void showFrame() {
		new UnitFrame();
	}
}
