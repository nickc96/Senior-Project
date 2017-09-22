import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;


public class AnimeSuggestion {

	//Text document name
	public static final String anime = ".\\src\\AnimeText";
	
	public static void main(String[] args) throws FileNotFoundException {
		int DATA = 4;										//The number of anime in the database
		int count = 0;
		Scanner file = new Scanner (new File(anime));
		String line;
		String[] info;
		String[] category;
		Anime[] database = new Anime[DATA];
		
		while (file.hasNext()){
			line = file.nextLine();
			
			info = line.split("-");
			//System.out.println("-"+info[0]+"-");			//Prints anime name
			
			category = info[1].split(",");
			for(int i=0; i<category.length; i++){			//Prints anime categories
				//System.out.println(category[i]);
			}
			
			Anime temp = new Anime(info[0], category);
			database[count] = temp;
			count++;
		}
		System.out.println("\n" + database[2].compareGen(database[2], database[3]));
	}

}
