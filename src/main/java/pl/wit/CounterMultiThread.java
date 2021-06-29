package pl.wit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * zmienna obsługująca wątki odpowiadające za tworzenie plików
 * 
 * @author matti
 *
 */
public class CounterMultiThread implements Runnable {
	/**
	 * Mapa globalna prechowuje nr katalogu i aktualną liczbę plików wewnątrz
	 */
	public static Map<String, Integer> mapFolders = new HashMap<String, Integer>();
	/**
	 * Adres pliku, który będzie kopiowany
	 */
	protected String sourceFilePath;
	/**
	 * Rozszerzenie plików jakie powinien tworzyć wątek
	 */
	protected String extension;
	/**
	 * Ścieżkado folderu do którego kopiujemy plik
	 */
	private String destinationFolderPath;
	/**
	 * Numer wątku dodany by wykorzystać tą zmienną w teście jednostkowym
	 */
	private final int number;

	public CounterMultiThread(String sourceFilePath, String destinationPath,
			DestinationStructure destinationStructure) {
		this.sourceFilePath = sourceFilePath;
		this.destinationFolderPath = destinationStructure.getDestination() + FileExtended.separatorChar
				+ destinationPath;
		this.extension = destinationStructure.getExtension();
		this.number = 0;
	}

	public CounterMultiThread(String sourceFilePath, String destinationPath, DestinationStructure destinationStructure,
			int number) {
		this.sourceFilePath = sourceFilePath;
		this.destinationFolderPath = destinationStructure.getDestination() + FileExtended.separatorChar
				+ destinationPath;
		this.extension = destinationStructure.getExtension();
		this.number = number;
	}

	public CounterMultiThread(String sourceFilePath, String destinationPath, String extension) {
		this.sourceFilePath = sourceFilePath;
		this.destinationFolderPath = destinationPath;
		this.extension = extension;
		this.number = 0;
	}

	public CounterMultiThread(String sourceFilePath, String destinationPath, String extension, int number) {
		this.sourceFilePath = sourceFilePath;
		this.destinationFolderPath = destinationPath;
		this.extension = extension;
		this.number = number;
	}

	/**
	 * /** Metoda zwracająca nazwę nowego pliku
	 * 
	 * @param directotry nazwa katalogu w którym powinna zostać utworzona
	 * @param extension  rozszerzenie pliku
	 * @return pełna ścieżka do nowego pliku
	 */
	public synchronized String getNewFileName(String directotry, String extension) {
		/**
		 * Parsujemy numer folderu ze stringa do int
		 */
		int fileNumber = 0;
		/**
		 * Sprawdzamy czy już istnieje w w mapFolders wpis z kluczem o numerze danego
		 * folderu Jeśli istnieje pobieramy go i zapisujemy w zmiennej fileNumber
		 */
		if (CounterMultiThread.mapFolders.get(directotry) != null) {
			fileNumber = CounterMultiThread.mapFolders.get(directotry);
		}
		/**
		 * iterujemy zmienną fileNumber i umieszczamy w mapFolders używając
		 * odpowiedniego klucza
		 */
		fileNumber++;
		CounterMultiThread.mapFolders.put(directotry, fileNumber);
		/**
		 * Tworzymy ciąg znaków z nazwą pliku i następnie zwracamy ją
		 */
		String fileToCreate = String.valueOf(fileNumber) + "." + extension;
		return fileToCreate;
	}

	/**
	 * blokuje dostęp do folderu z nazwami plików w celu wpisania do niego nazwy
	 * nowego pliku
	 * 
	 */
	public void run() {
		System.out.println("Wątek numer " + this.number + " rozpoczął działanie");
		String fileName;
		/**
		 * Blokujemy dostęp do mapFolders w celu zsynchronizowania tego obiektu
		 */
		synchronized (CounterMultiThread.mapFolders) {
			fileName = getNewFileName(this.destinationFolderPath, extension);
		}
		String fileToCreatePath = this.destinationFolderPath + FileExtended.separatorChar + fileName;
		FileExtended fileToCreate = new FileExtended(fileToCreatePath);
		FileExtended source = new FileExtended(this.sourceFilePath);
		try {
			source.copyFile(fileToCreate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Wątek numer " + this.number + " zakończył działanie");
	}
}
