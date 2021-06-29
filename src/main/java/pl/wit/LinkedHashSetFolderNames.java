package pl.wit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
/**
 * Klasa zawierająca Set nazw folderów
 */
public class LinkedHashSetFolderNames extends LinkedHashSet<String> {
	private static final long serialVersionUID = 1L;
	/**
	 * Set z nazwami folderów
	 */
	private final LinkedHashSet<String>  names;
	
	public LinkedHashSetFolderNames(HashSet <String> filesPathAndCreateDateList) {
		names = new LinkedHashSet<String>(filesPathAndCreateDateList);
	}
	
	/**
	 * Tworzymy wszystkie katalogi z listy jeśli nie istnieją
	 * @param destinationPathToFolder 
	 */
	void createDestinationFolder(String destinationPathToFolder){
		FileExtended dateFolder;
		for (String item : names) {
			dateFolder = new FileExtended(destinationPathToFolder + "/" + item + "/");			
			if (!dateFolder.exists()) {
				dateFolder.mkdir();
			}
		}
	}
	
	/**
	 * Metoda zwraca HashSet z informacją czy dany folder istnieje
	 * @param destinationPathToFolder wartość ścieżki dla jakiej jest zwracany boolean z infomracją czy folder istnieje
	 * @return ArrayList boolean 
	 */
	public ArrayList<Boolean> exists(String destinationPathToFolder){
		FileExtended dateFolder;
		ArrayList<Boolean>  exists = new ArrayList<Boolean> (names.size());
		for (String item : names) {
			dateFolder = new FileExtended(destinationPathToFolder + "/" + item + "/");
			exists.add(dateFolder.exists());
		}
		return exists;
	}

	public LinkedHashSet<String> getNames() {
		return names;
	}
}
