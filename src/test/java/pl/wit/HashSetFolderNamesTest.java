package pl.wit;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HashSetFolderNamesTest {
	String currentDir;
	String sourcePathFolder;
	String destinationPathToFolder;
	String workPathMainFolder;

	@Before
	public void setCatalog() throws IOException {
		System.out.println("setCatalog()");
		currentDir = System.getProperty("user.dir");
		workPathMainFolder = currentDir + "/src/test/resources/Test";
		FileExtended workMainFolder = new FileExtended(workPathMainFolder);
		if (!workMainFolder.exists()) {
			workMainFolder.mkdir();
		}
		sourcePathFolder = workPathMainFolder + "/source";
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		if (!sourceFolder.exists()) {
			throw new IOException("This file does not exist.");
		}
		destinationPathToFolder = workPathMainFolder + "/destination";
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
		LinkedHashMap<String,String> filesPathAndCreateDateList = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if(!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		LinkedHashSetFolderNames hashSetFolderNames = new LinkedHashSetFolderNames(new HashSet<String>(filesPathAndCreateDateList.values()));
		hashSetFolderNames.createDestinationFolder(destinationPathToFolder);
		LinkedHashSet<String> folderNamesHashSet= hashSetFolderNames.getNames();
		ArrayList<Boolean> directorExists=hashSetFolderNames.exists(destinationPathToFolder);
		assertTrue(folderNamesHashSet.size() == directorExists.size());
		Iterator <String> stringIterator = folderNamesHashSet.iterator();
		Iterator <Boolean> booleanIterator = directorExists.iterator();
		String stringSetElement;
		String message;
		boolean booleanSetElement;
		while(stringIterator.hasNext()) {
			stringSetElement = stringIterator.next();
			booleanSetElement = booleanIterator.next();
			message = "Directory " + stringSetElement;
			while(message.length() < 130) {
            	message +=" ";
            }
			message += "exist: " + booleanSetElement;
			assertTrue(booleanSetElement);
			System.out.println(message);
		}
		System.out.println();
    }
	
	@Test
    public void checkSomething() {
		System.out.println("checkCreateFolderFromFileDate()");
    	String extension = "jpg".toLowerCase();
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		Map<String,String> filesPathAndCreateDateList = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if(!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		LinkedHashSetFolderNames hashSetFolderNames = new LinkedHashSetFolderNames(new HashSet<String>(filesPathAndCreateDateList.values()));
		hashSetFolderNames.createDestinationFolder(destinationPathToFolder);
		
//		Map<String,Integer> 
		
		
		
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
