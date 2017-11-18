import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


public class AnimeSuggestion {

	//Text document name
	public static final String animedatabase = ".\\src\\AnimeText";
	public static final String animeinput = ".\\src\\AnimeInput";
	public static final int DATA = 905; //The number of anime in the database
	public static final int DATAI = 1; //The number of anime in the input list
	
	public static void main(String[] args) throws FileNotFoundException {
		int count = 0;
		int countI = 0;
		Scanner file = new Scanner (new File(animedatabase), "UTF-8");
		String line;
		String[] info;
		String[] category;
		Anime[] database = new Anime[DATA];
		Anime[] input = new Anime[DATAI];
		
		String yearTemp;
		int year;
		int minYear=2017;
		
		double rating;
		
		String[] genreAll;
		String[] studioAll;		
		
		// Build input list
		String[] inputNames = new String[DATAI];
		Scanner file2 = new Scanner (new File(animeinput), "UTF-8");
		while (file2.hasNext()){
			inputNames[countI] = file2.nextLine();
			countI++;
		}
		
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
			try
	         {
	            year = Integer.parseInt(yearTemp);
	         }
	        catch (NumberFormatException e)
	         {
	            year = 0;
	         }
			if(year<minYear && year!=0)
				minYear=year;
			
			rating = Double.parseDouble(info[4]);
				
			Anime temp = new Anime(info[0], category, info[2], year, rating
					, info[5]);
			database[count] = temp;
			count++;
			
			for (int i=0; i<DATAI; i++)
			{
				if (info[0].equals(inputNames[i]))
				{
					Anime temp2 = new Anime (info[0], category, info[2], year, rating, info[5]);
					input[i] = temp;
				}
			}
		}
		
		// Uncomment for minYear
		// System.out.println (minYear);
		
		genreAll = sumG (database);
		studioAll = sumS (database);
		//for(int i=0; i<studioAll.length; i++){
		//	if (studioAll[i]!=null)
		//		System.out.println (studioAll[i]);
		//}
		
		System.out.println(2*database[0].compareGen(database[898], database[897]));
		System.out.println(2*database[0].compareGen(database[897], database[898]));
		
		// K-nearest neighbor test
		//String nntest = "Dragon Ball Z Special 2 : Zetsubou e no Hankou ! ! Nokosareta Chousenshi";		// Nearest neighbor test anime name
		Anime[] output = nearestNeighbor(database, input);
		//System.out.println(output[0].getName() + " - " + input[0].distance(output[0]));
		//System.out.println(output[1].getName() + " - " + input[0].distance(output[1]));
		//System.out.println(output[2].getName() + " - " + input[0].distance(output[2]));
		
		System.out.println(database[0].compareGen(database[1], database[0]));
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

	// K-nearest neighbor algorithm - returns top x (x is variable top)
	public static Anime[] nearestNeighbor (Anime[] database, Anime[] input)
	{
		int top = 3;
		Anime[] suggestion = new Anime[top];
		Anime[] temp = new Anime[DATAI];
		double[] minDist= new double[top];
		
		// Initialize all minDist to some arbitrary large number
		for (int i=0; i<top; i++){
			minDist[i] = 100;
		}
		
		// Separate input anime from anime database
		for (int i=0; i<DATAI; i++){
			for (int j=0; j<DATA; j++){
				if (database[j].equals(input[i])){
					temp[i] = database[j];
					database[i] = null;
				}
			}
		}
		
		
		for (int i=0; i<DATAI; i++){
			for(int j=0; j<DATA; j++){
				if(database[j]!=null)
				{
					if (temp[i].distance(database[j])<minDist[2]){
						if (temp[i].distance(database[j])<minDist[1]){
							if (temp[i].distance(database[j])<minDist[0]){
								minDist[0] = temp[i].distance(database[j]);
								suggestion[0] = database[j];
								//System.out.println(suggestion[0].getName() + " - " + minDist[0]);
								//break;
							}
							else{
								minDist[1] = temp[i].distance(database[j]);
								suggestion[1] = database[j];
								//break;
							}
						}
						else{
							minDist[2] = temp[i].distance(database[j]);
							suggestion[2] = database[j];
							//break;
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
