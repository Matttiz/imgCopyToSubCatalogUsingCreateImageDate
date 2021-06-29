package pl.wit;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Scanner;

/**
 * Program do kopiowania plików jpg z katalogu źródłowego do miejsca docelowego
 * Tworząc jednocześnie podfoldery w miejscu docelowym o nazwach dat kopiowanych
 * plików
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		System.out.println("Program do kopiowania zdjęć z folderu docelowego do folderów odpowiadających datom.");
		System.out.println("W folderze docelowym zdjęcia są numerowane od 1.");
		System.out.println("Aby skorzystać z niego jako parametry");
		Scanner scan = new Scanner(System.in);
		Parameter parameters;
		String sourcePath;
		String destinationPath;
		String threadNumber;
		do {
			System.out.println("Podaj ścieżkę do folderu, z którego pliki mają być skopiowane:");
			sourcePath = scan.next();
			System.out.println("Podaj ścieżkę do folderu, do którego pliki mają być skopiowane:");
			destinationPath = scan.next();
			System.out.println("Podaj liczbę wątków, które mogą zostać użyte do kopiowania plików");
			threadNumber = scan.next();
			parameters = new Parameter(sourcePath, destinationPath, threadNumber);
		} while (!parameters.areParametersCorrect());
		scan.close();
		System.out.println("checkCreateFiles()");
		String extension = "jpg";
		FileExtended sourceFolder = new FileExtended(parameters.getSourcePathFolder());
		LinkedHashMap<String, String> fileMapByFileExtension = sourceFolder.getFileMapByFileExtension(extension);
		FileExtended destinationFolder = new FileExtended(parameters.getDestinationPathToFolder());
		if (!destinationFolder.exists()) {
			destinationFolder.mkdir();
		}
		DestinationStructure destinationStructure = new DestinationStructure(
				new HashSet<String>(fileMapByFileExtension.values()), parameters.getDestinationPathToFolder(),
				extension);
		MixData mixData = new MixData(fileMapByFileExtension, destinationStructure);
		mixData.createDestinationFolders();
		String fold = null;
		for (String folder : destinationStructure.getNames()) {
			fold = parameters.getDestinationPathToFolder() + FileExtended.separatorChar + folder;
			mixData.createFiles(parameters.getThreadNumber(), fold);
		}
		System.out.println("Program zakończył działanie.");
	}
}