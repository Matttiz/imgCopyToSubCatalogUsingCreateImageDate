package pl.wit;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MixDataTest {
	final String currentDir = System.getProperty("user.dir");
	final String workPathMainFolder = currentDir + "/src/test/resources/Test";
	final String sourcePathFolder = workPathMainFolder + "/source";
	final String destinationPathToFolder = workPathMainFolder + "/destination";

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
	public void  checkCreateFiles() throws IOException {
		System.out.println("checkCreateFiles()");
		String extension = "jpg";
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		LinkedHashMap<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		DestinationStructure destinationStructure = new DestinationStructure(
				new HashSet<String>(fileMapByFileExtension.values()), destinationPathToFolder, extension);
		MixData mixData = new MixData(fileMapByFileExtension, destinationStructure);
		mixData.createDestinationFolders();
		String fold = null;
		int numberThreads = 2;
		for(String folder:destinationStructure.getNames()) {
			fold = destinationPathToFolder +"/" + folder;
			mixData.createFiles(numberThreads,fold);
		}
		assertTrue(mixData.getK() == 3);
		System.out.println();
	}
	
	@After
	public void cleanUp() {
		System.out.println("cleanUp()");
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		destinationFolder.deleteDirectory();
		System.out.println(destinationFolder.getAbsolutePath());
		assertTrue(!destinationFolder.exists());
		System.out.println();
	}
}
