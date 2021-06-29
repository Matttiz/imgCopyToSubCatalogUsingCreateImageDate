package pl.wit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class DestinationStructure extends LinkedHashSet<String> {

	/**
	 * Klasa zawierająca LinkedHashSet nazw folderów
	 */
	private static final long serialVersionUID = 1L;
	private String  destination;
	private final String extension;
	private LinkedHashSet<String> names = new LinkedHashSet<String>();
	
	/**
	 *
	 * @param filesPathAndCreateFoldersList lista ścieżek wraz z informacją o folderze, do którego należy skopiować plik
	 * @param destination ścieżka do folderu, w którym mają zostać utworzone foldery i przekopiowane pliki
	 * @param extension rozszerzenie pliku
	 */
	public DestinationStructure(HashSet <String> filesPathAndCreateFoldersList, String destination, String extension) {
		this.destination = destination;
		this.extension = extension;
		for(String path:filesPathAndCreateFoldersList) {
			names.add(path);
		}
	}
	
	public String getExtension() {
		return extension;
	}

	/**
	 * Konstruktor kopujący
	 * @param destinationStructure obiekt zawierający informację o tym gdzie jest folder docelowy oraz jakie foldery powinn zostać utworzone
	 */
	public DestinationStructure(DestinationStructure destinationStructure) {
		this.destination =destinationStructure.getDestination();
		this.extension = destinationStructure.getExtension();
		this.names = destinationStructure.getNames();
	}
	
	/**
	 * Tworzymy wszystkie katalogi z listy jeśli nie istnieją
	 * @param destinationPathToFolder 
	 */
	void createDestinationFolders(){
		FileExtended dateFolder = null;
		for (String item : names) {
			dateFolder = new FileExtended(this.destination + "/" + item);			
			if (!dateFolder.exists()) {
				dateFolder.mkdir();
			}
		}
	}
	
	/**
	 * Metoda zwraca HashSet z informacją czy dane foldery istnieją
	 * @return inforamacja czy odpowiadający jej pozycji na liście folder istnieje
	 */
	public ArrayList<Boolean> exists(){
		FileExtended dateFolder;
		ArrayList<Boolean>  exists = new ArrayList<Boolean> (names.size());
		for (String item : names) {
			dateFolder = new FileExtended(this.destination + "/" + item + "/");
			exists.add(dateFolder.exists());
		}
		return exists;
	}
	
	/**
	 * Metoda zwracająca ścieżkę absolutną do folderu wraz z inforacją czy folder istnieje
	 * @return zwraca mapę z nazwą folderu oraz informacją czy folder istnieje
	 */
	public LinkedHashMap<String, Boolean> isFolderExist(){
		LinkedHashMap<String, Boolean> map = new LinkedHashMap<String, Boolean>();
		String folder = null;
		FileExtended dateFolder;
		Iterator<String> it = names.iterator();
		while(it.hasNext()) {
			folder = it.next();
			dateFolder = new FileExtended(this.destination + "/" + folder);
			map.put(folder, dateFolder.exists());
		}
		return map;
	}
	
	/**
	 * Metoda wylicza wartość pliku o w folderze o najwyższej wartośli liczbowej w przypadku gdyby folder 
	 * docelowy istnial przed rozpoczęciem działania programu
	 * @return LinkedHashMap String,Integer nazwa folderu wraz z informacją o najwyższym numerze 
	 * pliku się w nim znajdującym
	 * @throws IOException może zostać wygenerowany przez metodę wewnętrzną getHighestFileInFolder
	 */
	public LinkedHashMap<String,Integer> getFolderAndHighestFileWithExtenionMap() throws IOException {
		LinkedHashMap<String,Integer> mapFolderAndHighestFile = new LinkedHashMap<>();
		LinkedHashMap<String, Boolean> isFolderExistMap = isFolderExist();
		Set<String> folderToCheckExistedFiles = isFolderExistMap.keySet();
		int highestFileNumber = 0;
		String destination = getDestination();
		for(String folder:folderToCheckExistedFiles) {
			if(isFolderExistMap.get(folder)) {
				highestFileNumber =  FileExtended.getHighestFileInFolder(destination + "/" +folder, this.extension);
				if(highestFileNumber ==-1) {
					highestFileNumber = 0;
				}
			}else {
				highestFileNumber = 0;
			}
			mapFolderAndHighestFile.put(folder, highestFileNumber);
		}
		return mapFolderAndHighestFile;
	}
	
	/**
	 * Metoda wylicza wartość pliku o w folderze o najwyższej wartośli liczbowej
	 * @param extension rozszerzenie pliku
	 * @return LinkedHashMap String,Integer nazwa folderu wraz z informacją o najwyższym numerze 
	 * pliku się w nim znajdującym
	 * @throws IOException może zostać wygenerowany przez metodę wewnętrzną getHighestFileInFolder
	 */
	public LinkedHashMap<String,Integer> getFolderAndHighestFileWithExtenionMap(String extension) throws IOException {
		LinkedHashMap<String,Integer> mapFolderAndHighestFile = new LinkedHashMap<>();
		LinkedHashMap<String, Boolean> isFolderExistMap = isFolderExist();
		Set<String> folderToCheckExistedFiles = isFolderExistMap.keySet();
		int highestFileNumber = 0;
		String destination = getDestination();
		for(String folder:folderToCheckExistedFiles) {
			if(isFolderExistMap.get(folder)) {
				highestFileNumber =  FileExtended.getHighestFileInFolder(destination + "/" +folder, extension);
				if(highestFileNumber ==-1) {
					highestFileNumber = 0;
				}
			}else {
				highestFileNumber = 0;
			}
			mapFolderAndHighestFile.put(folder, highestFileNumber);
		}
		return mapFolderAndHighestFile;
	}
	
	/**
	 * Metoda wypisująca kolejne wpisy w mapie
	 */
	public void println() {
		System.out.println("println()");
		Iterator<String> it = names.iterator();
		String folder = null;
		while(it.hasNext()) {
			folder=it.next();
			System.out.println(folder);
		}
		
	}

	public LinkedHashSet<String> getNames() {
		return names;
	}
	
	@Override
	public int size() {
		return names.size();
	}
	
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
}
