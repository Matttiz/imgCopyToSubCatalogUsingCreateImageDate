package pl.wit;

/**
 * Klasa zawierająca parametry
 * @author matti
 *
 */
public class Parameter {
	private String sourcePathFolder;
	private String destinationPathToFolder;
	private int threadNumber;
	
	public Parameter(String sourcePathFolder, String destinationPathToFolder, String numberThreads) {
		this.sourcePathFolder = sourcePathFolder;
		this.destinationPathToFolder = destinationPathToFolder;
		int number = -1;
		try {
			number =Integer.valueOf(numberThreads);
		} catch(Exception e){
		      System.out.println("Liczba wątków nie mogła zostać skonwertowana poprawnie");
	    }
		this.threadNumber = number;		
	}
	
	/**
	 * Sprawdza czy istnieje folder docelowy oraz źródłowy oraz czy folder źródłowy nie jest pusty
	 * @return boolean
	 */
	public boolean isPathCorrect() {
		FileExtended sourceDir = new FileExtended(this.sourcePathFolder);
		FileExtended destinationDir = new FileExtended(this.destinationPathToFolder);
		if (!sourceDir.exists()) {
			System.out.println("Folder \""+ sourceDir +"\" źródłowy nie istnieje.");
			return false;
		}
		if (!sourceDir.isDirectory()) {
			System.out.println("Folder \""+ sourceDir +"\" źródłowy nie jest folderem.");
			return false;
		}
		if(!(sourceDir.length() > 0)) {
			System.out.println("Folder \""+ sourceDir +"\" źródłowy jest pusty.");
			return false;
		}
		if (!destinationDir.exists()) {
			System.out.println("Folder \""+ destinationDir +"\" docelowy nie istnieje.");
			return false;
		}
		if (!destinationDir.isDirectory()) {
			System.out.println("Folder \""+ destinationDir +"\" docelowy nie jest folderem.");
			return false;
		}
		return true;
	}
	
	/**
	 * Sprawcza czy podana liczba wątków jest poprawna
	 * @return boolean
	 */
	public boolean isNumberThreadCorrect() {
		if(this.threadNumber <1) {
			System.out.println("Liczba wątków \""+ threadNumber +"\" nie jest prawidłowa");
			return false;
		}
		if(this.threadNumber > 10) {
			System.out.println("Liczba wątków \""+ threadNumber +"\" nie jest prawidłowa");
			return false;
		}
		return true;
	}
	
	/**
	 * Sprawcza czy podane parametry są poprawne
	 * @return boolean
	 */
	public boolean areParametersCorrect() {
		return this.isNumberThreadCorrect() && this.isPathCorrect();
	}

	public String getSourcePathFolder() {
		return sourcePathFolder;
	}

	public String getDestinationPathToFolder() {
		return destinationPathToFolder;
	}

	public int getThreadNumber() {
		return threadNumber;
	}

}
