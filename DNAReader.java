import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.*;

/**
 * A class that reads in a user-provided file and DNA sequence and returns a
 * person in the file who matches or closely-matches that sequence.
 * 
 * CSC 321: HW 1
 * 
 * @author Natalie Schwartzenberger and Kendrick Smith
 * @version 9/04/2020
 */
public class DNAReader {
	// Fields
	public String sequenceString;
	public String fileName;
	public String firstLine;
	public boolean isQ = false;
	public ArrayList<String> fileData;
	public ArrayList<String> sequenceList;
	public ArrayList<String> data = new ArrayList<>();
	private final Scanner sc = new Scanner(System.in);

	// Constructor
	public DNAReader() {
		this.fileName = fileDatabasePrompt();
		readDataBase(this.fileName);
		
		this.sequenceString = sequencePrompt();
		this.fileData = breakLine(this.firstLine);
		this.sequenceList = readSequence(this.sequenceString, this.fileData);
	}

	// Methods
	/**
	 * Reads in a file, separating the first line and adding each next line to an
	 * arrayList
	 * 
	 * @param fileName the name of the file taken from the user
	 */
	public void readDataBase(String fileName) {
		try {
			Scanner inFile = new Scanner(new File(fileName));
			firstLine = inFile.nextLine();

			while (inFile.hasNextLine()) {
				this.data.add(inFile.nextLine());
			}
			inFile.close();
		} catch (java.io.FileNotFoundException e) {
			System.out.println("No such file: " + fileName);
		}
	}

	/**
	 * Asks the user for a file name
	 * 
	 * @return String of the filename (user input)
	 */
	public String fileDatabasePrompt() {
		System.out.println("What file has the DNA Database?");
		String input = sc.nextLine();
		return input;
	}

	/**
	 * Asks the user for a DNA string or Q if they want to quit
	 * 
	 * @return String of the file name or empty string if Q
	 */
	public String sequencePrompt() {
		System.out.println("What is the DNA string? or type 'Q' to quit");
		String input = sc.nextLine();
		if (input.equals("Q") || input.equals("q")) {
			this.isQ = true;
			return "";
		} else {
			return input;
		}
	}

	/**
	 * Breaks the user-inputed DNA sequence it separate STRs
	 * 
	 * @param sequenceString user-inputed DNA sequence
	 * @param fileData       an ArrayList of given STRs
	 * @return ArrayList of the number of STRs in a given DNA sequence
	 */
	public ArrayList<String> readSequence(String sequenceString, ArrayList<String> fileData) {
		ArrayList<String> list = new ArrayList<>();
		list.add("test");
		String last = "";
		int count = 0;
		int increment = 4;
		for (int x = 1; x < fileData.size(); x++) {
			list.add("0");
		}
		for (int i = 0; i <= sequenceString.length() - 4; i += increment) {
			String str = sequenceString.substring(i, i + 4);
			if (fileData.contains(str)) {
				increment = 4;
				for (int n = 0; n < fileData.size(); n++) {
					if (str.equals(fileData.get(n)) && last == str) {
						count++;
						if (Integer.parseInt(list.get(n)) < count) {
							list.set(n, Integer.toString(Integer.parseInt(list.get(n)) + 1));
						}
					} else if (str.equals(fileData.get(n)) && last != str) {
						if (Integer.parseInt(list.get(n)) == 0) {
							list.set(n, "1");
						}
						count = 1;
					}
					last = str;
				}

			} else {
				increment = 1;
				count = 1;
			}
		}
		return list;
	}

	/**
	 * Makes the first Line of data from the user-inputed file into an ArrayList in
	 * the format of [name, STR, STR, STR]
	 * 
	 * @param line a string to be broken into an ArrayList
	 * @return ArrayList of the first line of data
	 */
	public static ArrayList<String> breakLine(String line) {
		ArrayList<String> fileData = new ArrayList<>(Arrays.asList(line.split(",")));
		return fileData;
	}

	/**
	 * Returns the name of the person who matches the DNA sequence or the person w/
	 * the closest match
	 * 
	 * @param sequenceList an ArrayList of the DNA sequence
	 */
	public void match(ArrayList<String> sequenceList) {

		if (this.isQ != true) {
			// Breaks the strings within an the data ArrayList into ArrayLists
			ArrayList<ArrayList<String>> dataBase = new ArrayList<>(getData().size());
			for (int i = 0; i < getData().size(); i++) {
				dataBase.add(null);
				dataBase.set(i, breakLine(getData().get(i)));
			}
			// Makes a list of all names in the database
			ArrayList<String> names = new ArrayList<>(getData().size());
			for (int i = 0; i < getData().size(); i++) {
				names.add(dataBase.get(i).get(0));
			}
			// compares sequenceList to dataBase until all but one name is removed
			int m = 1;
			int n = 0;
			while (m < sequenceList.size()) {
				while (names.size() > 1) {
					if (dataBase.get(n).get(m).equals(sequenceList.get(m))) {
						if (n < dataBase.size()) {
							n++;
						} else {
							n = 0;
							m++;
						}
					} else {
						names.remove(names.indexOf(dataBase.get(n).get(0)));
						n++;
					}
				}
				break;
			}
			// we need to find the index of the array list remaining
			int ind = -1;
			for (int i = 0; i < dataBase.size(); i++) {
				if (names.contains(dataBase.get(i).get(0))) {
					ind = i;
				}
			}
			// no Match:
			sequenceList.set(0, names.get(0));
			if (!(dataBase.get(ind).equals(sequenceList))) {

				// make two array lists one with names one with str counts
				ArrayList<String> newNames = new ArrayList<>(getData().size());
				for (int i = 0; i < getData().size(); i++) {
					newNames.add(dataBase.get(i).get(0));
				}

				ArrayList<Integer> matchedSTR = new ArrayList<>();
				for (int i = 0; i < getData().size(); i++) {
					matchedSTR.add(0);
				}
				// compare each str amount and increment count for each match
				for (int i = 1; i < dataBase.get(0).size(); i++) {
					for (int k = 0; k < dataBase.size(); k++) {
						if (dataBase.get(k).get(i).equals(sequenceList.get(i))) {
							matchedSTR.set(k, matchedSTR.get(k) + 1);
						}
					}
				}
				System.out.println("The sequence matches NO MATCH" + "\n" + "Closest Matches are: " + "\n"
						+ newNames.get(matchedSTR.indexOf(Collections.max(matchedSTR))) + "\n" + "With "
						+ Collections.max(matchedSTR) + " STR(s) in common.");
			} else {
				System.out.println("The sequence matches " + names.get(0) + ".");
			}
		} else {
			System.out.println(""); // prints empty string if user types Q or q
		}
	}

	/**
	 * An accessor method for the list of people and their STR counts in a
	 * user-inputed file
	 * 
	 * @return ArrayList of people and their STR counts
	 */
	public ArrayList<String> getData() {
		return this.data;
	}

	/**
	 * An accessor method for first line of data in the user-inputed file (the STRs
	 * we are looking for)
	 * 
	 * @return ArrayList of STRs to match to people in the data list
	 */
	public ArrayList<String> getFileData() {
		return this.fileData;
	}

	/**
	 * An accessor method for the DNA sequence broken up by STRs
	 * 
	 * @return ArrayList of STRs
	 */
	public ArrayList<String> getSequenceList() {
		return this.sequenceList;
	}

}
