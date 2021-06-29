package pl.wit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FileExtendedTest {
	/**
	 * currentDir zmienna do bieżącego folderu
	 * 
	 * workFolabsolutPathToWorkFolder zmienna gdzie zostaną przeprowadzone testy
	 */
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
	public void checkGetFileListByFileExtension() {
		System.out.println("checkGetFileListByFileExtension()");
		String extension = "jpg".toLowerCase();
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		Map<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);

		Iterator<String> keySetIterator = fileMapByFileExtension.keySet().iterator();
		String key = null;
		String message = null;
		while (keySetIterator.hasNext()) {
			key = keySetIterator.next();
			message = "Key : " + key;
			while (message.length() < 130) {
				message += " ";
			}
			message += "   Value : " + fileMapByFileExtension.get(key);
			System.out.println(message);
		}
		assertTrue(fileMapByFileExtension.size() == 3);
		System.out.println();
	}

	@Test
	public void checkGetExtension() {
		System.out.println("checkGetExtension()");
		String extension = "txt".toLowerCase();;
		String filePath = sourcePathFolder + "/podkatalog/Plik.txt";
		FileExtended file = new FileExtended(filePath);
		assertTrue(file.exists());
		assertTrue(file.getExtension().equals(extension));
		System.out.println();
	}

	@Test
	public void checkIsProperExtension() {
		System.out.println("checkIsProperExtension()");
		String extension = "txt";
		String filePath = sourcePathFolder + "/podkatalog/Screenshot_20180504_235342.png";
		FileExtended file = new FileExtended(filePath);
		assertTrue(file.exists());
		assertFalse(file.isPropertyExtension(extension));
	}

	@Test
	public void checkCountFiles() {
		System.out.println("checkCountFiles()");
		FileExtended workFolder = new FileExtended(sourcePathFolder);
		System.out.println(workFolder.countFiles());
		assertTrue(workFolder.countFiles() == 8);
		System.out.println();
	}

	@Test
	public void checkCopyFolder() {
		System.out.println("checkCopyFolder()");
		String sourceFilePath = sourcePathFolder + "/podkatalog/Screenshot_20180504_235342.png";
		String destinationFilePath = destinationPathToFolder + "/dest.png";
		FileExtended sourceFile = new FileExtended(sourceFilePath);
		FileExtended destinationFile = new FileExtended(destinationFilePath);
		try {
			sourceFile.copyFile(destinationFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(destinationFile.exists());
		System.out.println();
	}

	@Test
	public void checkDeleteDirectory() {
		System.out.println("checkDeleteDirectory()");
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		destinationFolder.deleteDirectory();
		System.out.print(destinationFolder.getAbsolutePath());
		assertTrue(!destinationFolder.exists());
		System.out.println();
	}

	@Test
	public void checkCreateFolderFromFileDate() {
		System.out.println("checkCreateFolderFromFileDate()");
		String extension = "jpg".toLowerCase();
		FileExtended sourceFolder = new FileExtended(sourcePathFolder);
		Map<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		FileExtended dateFolder;
		Set<String> dateSet = new HashSet<String>(fileMapByFileExtension.values());
		for (String item : dateSet) {
			dateFolder = new FileExtended(destinationPathToFolder + "/" + item + "/");
			if (!dateFolder.exists()) {
				dateFolder.mkdir();
			}
		}
		System.out.println();
	}

	@Test
	public void checkGetFileNameAsNumber() {
		System.out.println("checkGetFileNameAsNumber()");
		String filePath = sourcePathFolder + "/pod/pod2/wisla_krakow.jpg";
		FileExtended file = new FileExtended(filePath);
		assertTrue(file.exists());
		assertTrue(file.getFileNameAsNumber() == -1);
		filePath = sourcePathFolder + "/podkatalog/1.txt";
		file = new FileExtended(filePath);
		assertTrue(file.exists());
		assertTrue(file.getFileNameAsNumber() == 1);
		System.out.println();
	}

	@Test
	public void checkGetLastFileInFolder() throws IOException {
		System.out.println("checkGetLastFileInFolder");
		System.out.println(sourcePathFolder);
		String path = sourcePathFolder + "/podkatalog";
		int i = FileExtended.getHighestFileInFolder(path, "txt");
		assertTrue(i == 3);
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
