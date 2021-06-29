package pl.wit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Test;

public class ParameterTest {

	/**
	 * currentDir zmienna do bieżącego folderu
	 * 
	 * workFolabsolutPathToWorkFolder zmienna gdzie zostaną przeprowadzone testy
	 * 
	 * sourcePathFolder zmienna skąd mają być kopiowane pliki
	 * 
	 * destinationPathToFolder folder dokąd mają być kopiowane pliki
	 * 
	 * sourcePathEmptyFolder folder bez zawartości
	 * 
	 * sourcePathNotExist nieistniejący folder
	 */
	final String currentDir = System.getProperty("user.dir");
	final String workPathMainFolder = currentDir + FileExtended.separatorChar + "src" + FileExtended.separatorChar
			+ "test" + FileExtended.separatorChar + "resources" + FileExtended.separatorChar + "Test";
	final String sourcePathFolder = workPathMainFolder + FileExtended.separatorChar + "source";
	final String destinationPathToFolder = workPathMainFolder + FileExtended.separatorChar + "destination";
	final String sourcePathEmptyFolder = workPathMainFolder + FileExtended.separatorChar + "source"
			+ FileExtended.separatorChar + "empty";
	final String sourcePathNotExist = workPathMainFolder + FileExtended.separatorChar + "source"
			+ FileExtended.separatorChar + "empty1";

	@Test
	public void sourceIsEmpty() {
		String threadNumber = "1";
		FileExtended sourceEmpty = new FileExtended(sourcePathEmptyFolder);
		sourceEmpty.mkdir();
		Parameter parameter = new Parameter(sourcePathEmptyFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isPathCorrect());
	}

	@Test
	public void sourceIsNotDirectory() throws IOException {
		String threadNumber = "1";
		FileExtended sourceEmpty = new FileExtended(sourcePathEmptyFolder);
		sourceEmpty.createNewFile();
		Parameter parameter = new Parameter(sourcePathEmptyFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isPathCorrect());
	}

	@Test
	public void sourceIsNotExists() {
		String threadNumber = "1";
		FileExtended sourceEmpty = new FileExtended(sourcePathNotExist);
		sourceEmpty.deleteDirectory();

		Parameter parameter = new Parameter(sourcePathNotExist, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isPathCorrect());
	}

	@Test
	public void destinationIsNotCorrect() {
		String threadNumber = "1";
		Parameter parameter = new Parameter(sourcePathFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isPathCorrect());
	}

	@Test
	public void destinationIsCorrect() {
		String threadNumber = "1";
		Parameter parameter = new Parameter(sourcePathFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isPathCorrect());
		FileExtended destination = new FileExtended(destinationPathToFolder);
		destination.mkdir();
		assertTrue(parameter.isPathCorrect());
	}

	@Test
	public void destinationIsFile() throws IOException {
		String threadNumber = "1";
		FileExtended destinationFile = new FileExtended(destinationPathToFolder);
		destinationFile.createNewFile();
		Parameter parameter = new Parameter(sourcePathFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isPathCorrect());

	}

	@Test
	public void threadStringNotCorrect() {
		String threadNumber = "Kot";
		Parameter parameter = new Parameter(sourcePathFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isNumberThreadCorrect());
	}

	public void threadNumberIsToLow() {
		String threadNumber = "0";
		Parameter parameter = new Parameter(sourcePathFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isNumberThreadCorrect());
	}

	public void threadNumberIsToBig() {
		String threadNumber = "11";
		Parameter parameter = new Parameter(sourcePathFolder, destinationPathToFolder, threadNumber);
		assertFalse(parameter.isNumberThreadCorrect());
	}

	@After
	public void cleanUp() {
		System.out.println("cleanUp()");
		FileExtended destinationFolder = new FileExtended(destinationPathToFolder);
		destinationFolder.deleteDirectory();
		assertTrue(!destinationFolder.exists());
		System.out.println();
		FileExtended destination = new FileExtended(destinationPathToFolder);
		destination.deleteDirectory();
		FileExtended sourceEmpty = new FileExtended(sourcePathEmptyFolder);
		sourceEmpty.deleteDirectory();
		FileExtended sourceFile1 = new FileExtended(sourcePathEmptyFolder);
		sourceFile1.delete();
		FileExtended destinationFile = new FileExtended(destinationPathToFolder);
		destinationFile.delete();
	}

}
