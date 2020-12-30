
import java.io.*;
import java.util.*;

/* Word Hunt Solver v2
 * does all square boards nxn
 */

public class WordHunt_v2 {
	public static TrieNode_v2 root = new TrieNode_v2();
	public static int boardSize;
	public static String[] board;
	
	public static ArrayList<Answer> answers = new ArrayList<Answer>();

	public static void main(String[] args) throws IOException{
		// takes the dictionary and makes the trie
		BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"));
		makeTrie(br);
		
		// Scanner that takes in the board and uses it to compute the answers
		Scanner sc = new Scanner(System.in);
		System.out.print("size of board: ");
		boardSize = sc.nextInt();sc.nextLine();
		board = new String[boardSize];
		
		for(int i = 0; i < boardSize; i++) {
			board[i] = sc.nextLine();
		}
		
		// creates a visited array
		boolean visited[][] = new boolean[boardSize][boardSize];
		
		// finds the words based on the board
		for(int i = 0; i < boardSize; i++) {
			for(int j = 0; j < boardSize; j++) {
				recurse(i,j,"",new Coordinate(i,j),visited,root);
			}
		}
		
		// sorts and prints out answer
		quickSort(0,answers.size()-1);
		for(int i = 0; i < answers.size(); i++) {
			System.out.println((answers.size()-i) + ": " + answers.get(i));
		}
	}
	
	public static void makeTrie(BufferedReader br) throws IOException {
		String word = "";
		
		while((word = br.readLine()) != null) {
			TrieNode_v2 curr = root;

			for(int i = 0; i < word.length(); i++) {
				String letter = word.substring(i,i+1);
				if(curr.children.get(letter) == null) 
					curr.children.put(letter,new TrieNode_v2());
				
				curr = curr.children.get(letter);
				
				if(i == word.length()-1) {
					curr.isFullWord = true;
				}
			}
		}
	}
	
	public static void recurse(int row, int column, String word, Coordinate rootCoord, boolean[][] visited, TrieNode_v2 node) {
		// base cases
		if(row < 0 || row >= boardSize)
			return;
		if(column < 0 || column >= boardSize)
			return;
		if(visited[row][column] == true)
			return;
		
		// if word does not exist
		String nextLetter = board[row].substring(column,column+1);
		if(node.children.get(nextLetter) == null) 
			return;
		
		
		// word exists - adds to answer and continues checking
		word += nextLetter;
		visited[row][column] = true;
		
		if(word.length() > 3 && node.children.get(nextLetter).isFullWord == true) {
			boolean containsWord = false;
			for(int i = 0; i < answers.size(); i++) {
				if(answers.get(i).word.equals(word))
					containsWord = true;
			}
			if(containsWord == false)
				answers.add(new Answer(word,rootCoord));
		}
		
		// checks nearby words
		// left
		recurse(row,column-1,word,rootCoord,visited,node.children.get(nextLetter));
		// up-left
		recurse(row-1,column-1,word,rootCoord,visited,node.children.get(nextLetter));
		// up
		recurse(row-1,column,word,rootCoord,visited,node.children.get(nextLetter));
		// up-right
		recurse(row-1,column+1,word,rootCoord,visited,node.children.get(nextLetter));
		// right
		recurse(row,column+1,word,rootCoord,visited,node.children.get(nextLetter));
		// down-right
		recurse(row+1,column+1,word,rootCoord,visited,node.children.get(nextLetter));
		// down
		recurse(row+1,column,word,rootCoord,visited,node.children.get(nextLetter));
		// down-left
		recurse(row+1,column-1,word,rootCoord,visited,node.children.get(nextLetter));
		
		visited[row][column] = false;
	}
	
	public static void quickSort(int low, int high) {
		if(low < high) {
			int pi = partition(low,high);
			quickSort(low,pi-1);
			quickSort(pi+1,high);
		}
	}
	
	public static int partition(int low, int high) {
		int pivot = answers.get(high).wordLength; 
		int i = low-1;
		
		for(int j = low; j < high; j++) {
			if(answers.get(j).wordLength < pivot) {
				i++;
				Answer temp = answers.get(i);
				answers.set(i, answers.get(j));
				answers.set(j, temp);
			}
		}
		Answer temp = answers.get(i+1);
		answers.set(i+1, answers.get(high));
		answers.set(high, temp);
		return i+1;
	}
}

class TrieNode_v2 {
	public HashMap<String,TrieNode_v2> children = new HashMap<String,TrieNode_v2>();
	public boolean isFullWord = false;
}

class Answer {
	public String word;
	public Coordinate rootCoordinate;
	public int wordLength;
	
	public Answer(String word, Coordinate rootCoordinate) {
		this.word = word;
		this.rootCoordinate = rootCoordinate;
		wordLength = word.length();
	}
	
	public String toString() {
		return word + " | " + rootCoordinate;
	}
}

class Coordinate {
	int row;
	int column;
	
	public Coordinate(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public String toString() {
		return "(" + row + "," + column + ")";
	}
}
