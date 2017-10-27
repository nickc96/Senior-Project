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
		
		String[] genreAll;
		String[] studioAll;
		
		// Build database and gather data
		while (file.hasNext()){
			line = file.nextLine();
			info = line.split("~");
			
			//System.out.println("-"+info[0]+"-");			//Prints anime name
			
			category = info[1].split(",");
			for (int i=0; i<category.length; i++){
				category[i] = category[i].replaceAll("\\s","");
			}
			
			for (int i=0; i<DATA; i++)
			{
				info[2] = info[2].replaceAll("\\s", "");
			}
			
			//for(int i=0; i<category.length; i++){			//Prints anime categories
				//System.out.println(category[i]);
			//}
			
			// Find minYear
			yearTemp = info[3];
			yearTemp = yearTemp.replaceAll("\\s","");
			year = Integer.parseInt(yearTemp);
			if(year<minYear)
				minYear=year;
				
			Anime temp = new Anime(info[0], category, info[2], year);
			database[count] = temp;
			count++;
		}
		
		genreAll = sumG (database);
		studioAll = sumS (database);
		for(int i=0; i<studioAll.length; i++){
			if (studioAll[i]!=null)
				System.out.println (studioAll[i]);
		}
		
		// K-nearest neighbor test
		String nntest = "Spice and Wolf ";		// Nearest neighbor test anime name
		System.out.println(nearestNeighbor(database, nntest).getName());
			
	}

	// Creates list of all genres in database
	public static String[] sumG (Anime[] database)
	{
		Anime gAll = database[0];
		String[] temp = database[0].getGenre();
		gAll.setGenre(temp);
		
		for(int i=1; i<database.length; i++)
		{
			temp = gAll.union(database[i]);
			gAll.setGenre(temp);
		}
		
		gAll.removeDup(gAll.getGenre());
		
		return gAll.getGenre();
	}
	
	// Creates list of all studios in database
	public static String[] sumS (Anime[] database)
	{
		String[] sAll = new String[DATA];
		
		for (int i=0; i<sAll.length; i++)
		{
				sAll[i] = database[i].getStudio();
		}
		
		database[0].removeDup(sAll);
		
		return sAll;
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

	// K-nearest neighbor algorithm
	public static Anime nearestNeighbor (Anime[] input, String anime)
	{
		Anime suggestion = null;
		double maxDist=0;
		for (int i=0; i<DATA; i++){
			if (input[i].getName().equals(anime)){		//
				Anime temp = input[i];
				input[i] = null;
				for(int j=0; j<DATA; j++){
					if(input[j]!=null)
					{
						temp.distance(input[j]);
						if (temp.distance(input[j])>maxDist){
							maxDist = temp.distance(input[j]);
							suggestion = input[j];
						}
					}	
				}
			}
		}
		
		return suggestion;
	}	
	
	public static Anime[] means (Anime[] input, int k, String anime)
	{
		Anime[] suggestions = null;
		Anime[] groups = new Anime[k];
		Random rand = new Random();
		
		//initialize random starting points for groups
		for (int i = 0; i<k; i++)
		{
			groups[i].setYear(rand.nextInt(18)+1999);
			//groups[i].setStudio(a);
		}
		
		return suggestions;
	}
}
