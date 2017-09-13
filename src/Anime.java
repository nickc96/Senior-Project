import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;


public class Anime {

	//Text document name
	public static final String anime = ".\\src\\AnimeText";
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner file = new Scanner (new File(anime));
		String line;
		String[] info;
		String[] category;
		//LinkedList category = new LinkedList();
		
		while (file.hasNext()){
			line = file.nextLine();
			info = line.split("-");
			System.out.println("-"+info[0]+"-");			//Prints anime name
			
			category = info[1].split(",");
			for(int i=0; i<category.length; i++){			//Prints anime categories
				System.out.println(category[i]);
			}
		}
	}

}
