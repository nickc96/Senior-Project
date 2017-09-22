import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Random;

public class AnimeSuggestion {

	//Text document name
	public static final String anime = ".\\src\\AnimeText";
   public static final int DATA = 4; //The number of anime in the database
	
	public static void main(String[] args) throws FileNotFoundException {
												
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
		Anime[] tester = new Anime[DATA];
      tester = randomizer(database);
      for (int i=0; i<DATA; i++)
         System.out.println(getName(tester[i]));
      
      System.out.println("\n" + database[2].compareGen(database[2], database[3]));
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