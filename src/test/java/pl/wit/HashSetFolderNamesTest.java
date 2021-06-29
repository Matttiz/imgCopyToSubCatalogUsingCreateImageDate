package pl.wit;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HashSetFolderNamesTest {
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
		String extension = "jpg".toLowerCase();
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		LinkedHashMap<String, String> filesPathAndCreateDateList = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		LinkedHashSetFolderNames hashSetFolderNames = new LinkedHashSetFolderNames(
				new HashSet<String>(filesPathAndCreateDateList.values()));
		hashSetFolderNames.createDestinationFolder(destinationPathToFolder);
		LinkedHashSet<String> folderNamesHashSet = hashSetFolderNames.getNames();
		ArrayList<Boolean> directorExists = hashSetFolderNames.exists(destinationPathToFolder);
		assertTrue(folderNamesHashSet.size() == directorExists.size());
		Iterator<String> stringIterator = folderNamesHashSet.iterator();
		Iterator<Boolean> booleanIterator = directorExists.iterator();
		String stringSetElement;
		String message;
		boolean booleanSetElement;
		while (stringIterator.hasNext()) {
			stringSetElement = stringIterator.next();
			booleanSetElement = booleanIterator.next();
			message = "Directory " + stringSetElement;
			while (message.length() < 130) {
				message += " ";
			}
			message += "exist: " + booleanSetElement;
			assertTrue(booleanSetElement);
			System.out.println(message);
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
}
