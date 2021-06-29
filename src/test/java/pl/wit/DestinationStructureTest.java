package pl.wit;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DestinationStructureTest {
	/**
	 * currentDir zmienna do bieżącego folderu
	 * 
	 * workFolabsolutPathToWorkFolder zmienna gdzie zostaną przeprowadzone testy
	 * 
	 * sourcePathFolder zmienna skąd mają być kopiowane pliki
	 * 
	 * destinationPathToFolder folder dokąd mają być kopiowane pliki
	 */
	final String currentDir = System.getProperty("user.dir");
	final String workPathMainFolder = currentDir + FileExtended.separatorChar + "src" + FileExtended.separatorChar
			+ "test" + FileExtended.separatorChar + "resources" + FileExtended.separatorChar + "Test";
	final String sourcePathFolder = workPathMainFolder + FileExtended.separatorChar + "source";
	final String destinationPathToFolder = workPathMainFolder + FileExtended.separatorChar + "destination";

	@Before
	public void setCatalog() throws IOException {
		System.out.println("setCatalog()");
		FileExtended workMainFolder = new FileExtended(workPathMainFolder);
		if (!workMainFolder.exists()) {
			workMainFolder.mkdir();
		}
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		if (!sourceFolder.exists()) {
			throw new IOException("This file does not exist.");
		}
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		System.out.println();
	}

	@Test
	public void checkCreateFolderFromFileDate() {
		System.out.println("checkCreateFolderFromFileDate()");
		String extension = "jpg";
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		Map<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		DestinationStructure destinationStructure = new DestinationStructure(
				new HashSet<String>(fileMapByFileExtension.values()), destinationPathToFolder, extension);
		destinationStructure.createDestinationFolders();
		LinkedHashSet<String> folderNamesHashSet = destinationStructure.getNames();
		ArrayList<Boolean> directoryExists = destinationStructure.exists();
		assertTrue(folderNamesHashSet.size() == directoryExists.size());
		Iterator<String> stringIterator = folderNamesHashSet.iterator();
		Iterator<Boolean> booleanIterator = directoryExists.iterator();
		String folderName = null;
		String message = null;
		boolean isExist;
		while (stringIterator.hasNext()) {
			folderName = stringIterator.next();
			isExist = booleanIterator.next();
			message = "Directory " + folderName;
			while (message.length() < 23) {
				message += " ";
			}
			message += "exist: " + isExist;
			assertTrue(isExist);
			System.out.println(message);
		}
		System.out.println();
	}

	@Test
	public void checkCreateFolderFromFileDate2() {
		System.out.println("checkCreateFolderFromFileDate2()");
		String extension = "jpg";
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		Map<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		DestinationStructure destinationStructure = new DestinationStructure(
				new HashSet<String>(fileMapByFileExtension.values()), destinationPathToFolder, extension);
		LinkedHashMap<String, Boolean> isFolderExistMap = destinationStructure.isFolderExist();
		Iterator<String> stringIterator = isFolderExistMap.keySet().iterator();
		String folderName = null;
		String message = null;
		boolean exist = false;
		while (stringIterator.hasNext()) {
			folderName = stringIterator.next();
			exist = isFolderExistMap.get(folderName);
			message = "Directory " + folderName;
			while (message.length() < 23) {
				message += " ";
			}
			message += "exist: " + exist;
			System.out.println(message);
		}
		destinationStructure.createDestinationFolders();
		isFolderExistMap = destinationStructure.isFolderExist();
		stringIterator = isFolderExistMap.keySet().iterator();
		while (stringIterator.hasNext()) {
			folderName = stringIterator.next();
			exist = isFolderExistMap.get(folderName);
			message = "Directory " + folderName;
			while (message.length() < 23) {
				message += " ";
			}
			message += "exist: " + exist;
			assertTrue(exist);
			System.out.println(message);
		}
	}

	@Test
	public void checkHighestFileNameAsNumber() throws IOException {
		System.out.println("checkHighestFileNameAsNumber()");
		String extension = "jpg";
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		Map<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		DestinationStructure destinationStructure = new DestinationStructure(
				new HashSet<String>(fileMapByFileExtension.values()), destinationPathToFolder, extension);
		LinkedHashMap<String, Integer> folderAndHighestFileMap = destinationStructure
				.getFolderAndHighestFileWithExtenionMap();
		Set<String> folders = folderAndHighestFileMap.keySet();
		Iterator<String> it = folders.iterator();
		String folder = null;
		while (it.hasNext()) {
			folder = it.next();
			assertTrue(folderAndHighestFileMap.get(folder) == 0);
		}
		destinationStructure.createDestinationFolders();
		creatTwoFileOneByFolder();
		folderAndHighestFileMap = destinationStructure.getFolderAndHighestFileWithExtenionMap("txt");
		folders = folderAndHighestFileMap.keySet();
		it = folders.iterator();
		it = folders.iterator();
		while (it.hasNext()) {
			folder = it.next();
			assertTrue(folderAndHighestFileMap.get(folder) == 1);
		}
		System.out.println();
	}

	@After
	public void cleanUp() {
		System.out.println("cleanUp()");
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		destinationFolder.deleteDirectory();
		assertTrue(!destinationFolder.exists());
		System.out.println();
	}

	private void creatTwoFileOneByFolder() throws IOException {
		String firstPathFileToCreate = destinationPathToFolder + FileExtended.separatorChar + "2017-01-31"
				+ FileExtended.separatorChar + "1.txt";
		String secondPathFileToCreate = destinationPathToFolder + FileExtended.separatorChar + "2019-10-10"
				+ FileExtended.separatorChar + "1.txt";
		FileExtended firstFileToCreate = new FileExtended(firstPathFileToCreate);
		FileExtended secondFileToCreate = new FileExtended(secondPathFileToCreate);
		firstFileToCreate.createNewFile();
		secondFileToCreate.createNewFile();
	}
}
