package pl.wit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * Klasa ma na celu bardziej przystępne operowanie na plikach Zapewnia wyższy
 * poziom abstrakcji
 * 
 * @author matti
 *
 */
public class FileExtended extends File {

	private File file = null;
	private static final long serialVersionUID = 1L;

	public FileExtended(String pathname) {
		super(pathname);
		this.file = new File(pathname);
	}

	public FileExtended(File file) {
		super(file.getAbsolutePath());
		this.file = new File(file.getAbsolutePath());
	}

	/**
	 * Metoda kopiująca plik
	 * 
	 * @param sourceFile      plik źródłowy
	 * @param destenyFileName String ze ścieżką do pliku docelowego
	 */
	public FileExtended(File sourceFile, String destenyFileName) {
		super(sourceFile.getAbsolutePath(), destenyFileName);
		this.file = new File(sourceFile.getAbsolutePath(), destenyFileName);
	}

	/**
	 * Metoda pobiera datę utworzenia pliku
	 * 
	 * @return String
	 */
	public String getCreationDate() {
		String formatted = null;
		try {
			BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			FileTime time = attrs.creationTime();
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			formatted = simpleDateFormat.format(new Date(time.toMillis()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return formatted;
	}

	/**
	 * Ma na celu policzyć wszystkie pliki w podanym folderze i podfolderów
	 * 
	 * @return int Liczba plików w danym folderze wraz z podfolderami
	 */
	int countFiles() {
		int i = 0;
		List<FileExtended> allContents = getContains();
		if (allContents != null) {
			for (FileExtended file : allContents) {
				if (file.isDirectory()) {
					i += file.countFiles();
				}
				if (file.isFile()) {
					i++;
				}
			}
		}
		return i;
	}

	/**
	 * Ma na celu policzyć wszystkie wydobyć ścieżki do wszystkich plików
	 * 
	 * @param extension rozszerzenie wyszukiwanych plików
	 * @return LinkedHashMap String, String zawierająca ścieżki absolutne do
	 *         wyszukanych plików
	 */
	public LinkedHashMap<String, String> getFileMapByFileExtension(String extension) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		List<FileExtended> allContents = getContains();
		if (allContents != null) {
			for (FileExtended file : allContents) {
				if (file.isDirectory()) {
					map.putAll(file.getFileMapByFileExtension(extension));
				}
				if (file.isFile() && file.isPropertyExtension(extension)) {
					map.put(file.getAbsolutePath(), file.getCreationDate());
				}
			}
		}
		return map;
	}

	/**
	 * Metoda zwraca rozszerzenie danego pliku
	 * 
	 * @return String
	 */
	String getExtension() {
		File file = getFile();
		String name = file.getName();
		return name.substring(name.lastIndexOf(".") + 1).toLowerCase();
	}

	/**
	 * Metoda zwracająca samą nazwę pliku bez rozszerzenia
	 * 
	 * @return String
	 */
	String getFileNameWithoutExtension() {
		File file = getFile();
		String name = file.getName();
		return name.substring(0, name.lastIndexOf("."));
	}

	/**
	 * Matoda zwracająca nazwę pliku w postaci liczby
	 * 
	 * @return int
	 */
	int getFileNameAsNumber() {
		String name = getFileNameWithoutExtension();
		int i = -1;
		try {
			i = Integer.parseInt(name);
		} catch (Exception e) {
			System.out.println(
					"Metoda getFileNameAsNumber została wywołana na " + name + ", którego nazwa nie jest liczbą");
		}
		return i;
	}

	/**
	 * Metoda sprawdza czy plik ma rozszerzenie zgodne z żądanym
	 * 
	 * @param extension rozszerzenie pliku
	 * @return boolean
	 */
	boolean isPropertyExtension(String extension) {
		return (this.getExtension().equals(extension)) ? true : false;
	}

	/**
	 * Metoda znaleziona w internecie w celu usunięcia rekursywnego folderu
	 * roboczego
	 * 
	 * @param directoryToBeDeleted folder który ma zostać usunięty wraz z
	 *                             zawartością
	 * @return boolean
	 */
	void deleteDirectory() {
		File[] allContentsFile = this.getAbsoluteFile().listFiles();
		FileExtended[] allContents = null;
		if (allContentsFile != null) {
			allContents = new FileExtended[allContentsFile.length];
			for (int i = 0; i < allContentsFile.length; i++) {
				allContents[i] = new FileExtended(allContentsFile[i]);
			}
			for (FileExtended file : allContents) {
				if (file.isDirectory()) {
					file.deleteDirectory();
				} else {
					file.delete();
				}
			}
		}
		this.delete();
	}

	/**
	 * Metoda zwracająca listę plików z folderu na którym została wywołana
	 * 
	 * @return List<FileExtended>
	 */
	List<FileExtended> getContains() {
		File[] files = this.getFile().listFiles();
		List<FileExtended> allContents = new ArrayList<FileExtended>();
		if (files == null) {
			return null;
		}
		for (File ordinery : files) {
			allContents.add(new FileExtended(ordinery.getAbsolutePath()));
		}
		return allContents;
	}

	/**
	 * Metoda kopiująca plik wywoływana z pliku źródłowego
	 * 
	 * @param destination plik docelowy
	 * @throws IOException
	 */
	void copyFile(FileExtended destination) throws IOException {
		FileUtils.copyFile(this.file, destination.file);
	}

	/**
	 * Metoda kopiująca plik wywoływana z pliku źródłowego
	 * 
	 * @param destination adres pliku docelowego przekazany w postaci Stringa
	 * @return boolean
	 * @throws IOException
	 */
	void copyFile(String destination) throws IOException {
		FileExtended destinationFile = new FileExtended(destination);
		copyFile(destinationFile);
	}

	/**
	 * Metoda zwracająca nazwę pliku(jeśli pliki mają nazwy numerecze)
	 * 
	 * @param String ścieżka do pliku
	 * @param String rozszerzenie pliku
	 * @return int
	 * @throws IOException
	 */
	static int getHighestFileInFolder(String path, String extension) throws IOException {
		FileExtended fileExtended = new FileExtended(new File(path));
		if (!fileExtended.isDirectory()) {
			throw new IOException("Plik " + path + " nie jest folderem");
		}
		File[] allContentsFile = fileExtended.getAbsoluteFile().listFiles();
		FileExtended[] allContents = null;
		if (allContentsFile == null) {
			return 0;
		}
		allContents = new FileExtended[allContentsFile.length];
		for (int i = 0; i < allContentsFile.length; i++) {
			allContents[i] = new FileExtended(allContentsFile[i]);
		}
		int maxFileNameAsNumber = 0;
		int i = 0;
		for (FileExtended file : allContents) {
			if (file.isFile() && file.isPropertyExtension(extension)) {
				i = file.getFileNameAsNumber();
				if (maxFileNameAsNumber < i) {
					maxFileNameAsNumber = i;
				}
			}
		}
		return maxFileNameAsNumber;
	}

	/**
	 * Metoda pobiera zwykły obiekt File
	 * 
	 * @return File
	 */
	public File getFile() {
		return file;
	}

}
