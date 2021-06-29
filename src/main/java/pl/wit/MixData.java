package pl.wit;

import java.util.List;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Klasa zawierająca nazwę z folderami, jakie należy utworzyć oraz listą plików
 * do skopiowania
 * 
 * @author matti
 *
 */
public class MixData {
	private LinkedHashMap<String, String> filesPathAndCreateDateMap = new LinkedHashMap<>();
	private DestinationStructure destinationStructure;
	/**
	 * zmienna używana do numerowania kolejno tworzonych wątków
	 */
	static int k = 0;

	public MixData(LinkedHashMap<String, String> fileMapByFileExtension, DestinationStructure destinationStructure) {
		this.filesPathAndCreateDateMap = fileMapByFileExtension;
		this.destinationStructure = destinationStructure;
	}

	/**
	 * metoda zwracająca listę plików do skopiowania na podstawie daty utworzenia
	 * 
	 * @param folder data utworzenia pliku
	 * @return zwraca listę plików utworzonych danego dnia
	 * @throws IOException problemy wynikające ze streamem
	 */
	public List<String> filesToCreateInFolderStream(String folder) throws IOException {
		List<String> keys = filesPathAndCreateDateMap.entrySet().stream()
				.filter(entry -> folder.contains(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toList());
		return keys;
	}

	/**
	 * Kopiuje pliki z miejsca źródłowego do miejsca docelowego poprawnie je
	 * nazywając
	 * 
	 * @param numberThreads     liczba wątków
	 * @param destinationFolder folder docelowy
	 * @throws IOException if folder doesn't exist
	 */
	public void createFiles(int numberThreads, String destinationFolder) throws IOException {
		List<String> filesToCreateInFolderStream = new ArrayList<String>();
		String folderWithDateName = destinationFolder.substring(destinationFolder.lastIndexOf('/') + 1);
		filesToCreateInFolderStream = this.filesToCreateInFolderStream(folderWithDateName);
		ExecutorService es = Executors.newFixedThreadPool(numberThreads);
		for (int i = 0; i < filesToCreateInFolderStream.size(); i++) {
			/**
			 *  Zmodyfikowałem kreator by wypisywać numer wątku
			 */
			es.execute(new CounterMultiThread(filesToCreateInFolderStream.get(i), folderWithDateName,
					this.getDestinationStructure(), k + 1));
			k++;
		}
		es.shutdown();
		try {
			es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createDestinationFolders() {
		destinationStructure.createDestinationFolders();
	}

	public LinkedHashMap<String, String> getFilesPathAndCreateDateMap() {
		return filesPathAndCreateDateMap;
	}

	public DestinationStructure getDestinationStructure() {
		return destinationStructure;
	}

	public static int getK() {
		return k;
	}
}
