import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


public class AnimeSuggestion {

	//Text document name
	public static final String anime = ".\\src\\AnimeText";
	public static final int DATA = 45; //The number of anime in the database
	
	public static void main(String[] args) throws FileNotFoundException {
		int count = 0;
		Scanner file = new Scanner (new File(anime));
		String line;
		String[] info;
		String[] category;
		Anime[] database = new Anime[DATA];
		String yearTemp;
		int year;
		int minYear=2017;
		
		while (file.hasNext()){
			line = file.nextLine();
			info = line.split("~");
			
			//System.out.println("-"+info[0]+"-");			//Prints anime name
			category = info[1].split(",");
			for(int i=0; i<category.length; i++){			//Prints anime categories
				//System.out.println(category[i]);
			}
			
			yearTemp = info[3];
			yearTemp = yearTemp.replaceAll("\\s","");
			year = Integer.parseInt(yearTemp);
			if(year<minYear)
				minYear=year;
				
			Anime temp = new Anime(info[0], category, info[2], year);
			database[count] = temp;
			count++;
		}
		
		// K-nearest neighbor
		Anime suggestion = null;
		double maxDist=0;
		for (int i=0; i<DATA; i++){
			if (database[i].getName().equals("Spice and Wolf ")){		//
				Anime temp = database[i];
				database[i] = null;
				for(int j=0; j<DATA; j++){
					if(database[j]!=null)
					{
						temp.distance(database[j]);
						if (temp.distance(database[j])>maxDist){
							maxDist = temp.distance(database[j]);
							suggestion = database[j];
						}
					}	
				}
			}

		}
		System.out.println(suggestion.getName());
		//System.out.println(minYear);
		
		//Test compareGen method
		//System.out.println("\n" + database[2].compareGen(database[2], database[3]));
		
		//Test randomizer
		//Anime[] test = randomizer(database);
		//for (int i=0; i<test.length; i++){
		//	System.out.println (test[i].getName());
		//}
		
	}

	public static Anime[] randomizer(Anime[] a)
	   {
	      int num = 0;
	      Anime[] temp = new Anime[DATA];
	      Random rand = new Random();
	      int random = 0;
	      int[] generatedNums = new int[DATA];
	      boolean repeat = false;
	      
	      for (int i=0; i<DATA; i++)   //initialize array to all -1
	         generatedNums[i] = -1;
	      
	      while (num < DATA)
	      {
	         repeat = false;
	         random = rand.nextInt(DATA);  // generate random int
	         for (int i=0; i<DATA; i++)  //check for duplicates
	            if (generatedNums[i] == random)
	               repeat = true;
	         
	         if (repeat == false) //if no repeat, update arrays
	         {
	            generatedNums[num] = random;
	            temp[num] = a[random];
	            num++;
	         }
	      }
	      return temp;
	   }

}
