

public class DNADriver {
	
	public static String testFile = "smallSample.txt";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		DNAReader read = new DNAReader();
		
		while(read.isQ != true) {
			read.match(read.getSequenceList());
			read.sequencePrompt();
		}
		
	}

}
