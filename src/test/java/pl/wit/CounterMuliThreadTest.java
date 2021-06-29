package pl.wit;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;

public class CounterMuliThreadTest {
	/**
	 * currentDir zmienna do bieżącego folderu
	 * 
	 * workFolabsolutPathToWorkFolder zmienna gdzie zostaną przeprowadzone testy
	 * 
	 * sourcePathFolder zmienna skąd mają być kopiowane pliki
	 * 
	 * destinationPathToFolder folder dokąd mają być kopiowane pliki
	 * 
	 * extension rozszerzenie pliku
	 */
	final String currentDir = System.getProperty("user.dir");
	final String workPathMainFolder = currentDir + FileExtended.separatorChar + "src" + FileExtended.separatorChar
			+ "test" + FileExtended.separatorChar + "resources" + FileExtended.separatorChar + "Test";
	final String sourcePathFolder = workPathMainFolder + FileExtended.separatorChar + "source";
	final String destinationPathToFolder = workPathMainFolder + FileExtended.separatorChar + "destination";
	final String extension = "jpg";

	@Test
	public void executorsThreadTest2() throws IOException {
		System.out.println("executorsThreadTest2");
		String extension = "jpg";
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		LinkedHashMap<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);

		System.out.println("Destination " + destinationPathToFolder);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		DestinationStructure destinationStructure = new DestinationStructure(
				new HashSet<String>(fileMapByFileExtension.values()), destinationPathToFolder, extension);
		MixData mixData = new MixData(fileMapByFileExtension, destinationStructure);
		String folder = "2019-10-10";
		List<String> filesToCreateInFolder = mixData.filesToCreateInFolderStream(folder);

		String destinationFolderDatePath = destinationPathToFolder + FileExtended.separatorChar + folder;
		FileExtended destinationFolderDate = new FileExtended(destinationFolderDatePath);

		System.out.println("destinationFolderDate " + destinationFolderDate);
		if (destinationFolderDate.exists()) {
			destinationFolderDate.deleteDirectory();
		}
		destinationFolderDate.mkdir();
		ExecutorService es = Executors.newFixedThreadPool(10);
		int k = 0;
		for (int i = 0; i < filesToCreateInFolder.size(); i++) {
			es.execute(new CounterMultiThread(filesToCreateInFolder.get(i), destinationFolderDatePath,
					destinationStructure, i + 1));
			k++;
		}
		es.shutdown();
		try {
			es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Tworzenie plików zakończone");
		int filesNumber = countFiles(destinationFolder);
		System.out.println("Zsumowana liczba plików: " + filesNumber);
		assertTrue(k == 2);
		System.out.println("Wątek główny kończy działanie");
		System.out.println();
	}

	/**
	 * Metoda znaleziona w internecie w celu usunięcia rekursywnego folderu
	 * roboczego https://www.baeldung.com/java-delete-directory
	 * 
	 * @param File directoryToBeDeleted główny katalog który ma zostać usunięty
	 * @return boolean
	 */
	boolean deleteDirectory(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectory(file);
			}
		}
		return directoryToBeDeleted.delete();
	}

	/**
	 * Metoda napisana na podsawie metody deleteDirectory Ma na celu policzyć
	 * wszystkie pliki w podanym folderze i podfolderów
	 * 
	 * @param directory położenie folderu głównego
	 * @return
	 */
	int countFiles(File directory) {
		int i = 0;
		File[] allContents = directory.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				if (file.isDirectory()) {
					i += countFiles(file);
				}
				if (file.isFile()) {
					i++;
				}
			}
		}
		return i;
	}

	@After
	public void cleanUp() {
		System.out.println("cleanUp()");
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		destinationFolder.deleteDirectory();
		assertTrue(!destinationFolder.exists());
		System.out.println();
	}
}
