package tests;


import java.io.File;

import org.junit.jupiter.api.Test;

import editor.EditorController;
import editor.RoomMap;
import persistence.LoadGameXML;
import persistence.LoadMapFile;
import persistence.SaveGameXML;
import persistence.SaveMapFile;
import world.Game;


public class PersistenceTesting {
	
	@Test
	public void test() {
		try {
			Game g = LoadGameXML.load(new File("testingLevel.xml"));
			SaveGameXML.save(g, "testingOutput.xml");
			
		}catch(Exception e) {
			assert(true);
		}
	}

	@Test
	public void save_map_editor() {
		try {	
		RoomMap map = LoadMapFile.load(new File("testingMap.xml"));
		SaveMapFile.save(map, "testingMapOutput.xml");
		
		}
		catch(Exception e) {
			assert(true);
		}
	}
}
