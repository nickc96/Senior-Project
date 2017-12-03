import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


public class AnimeSuggestion {

	//Text document name
	public static final String animedatabase = "AnimeText.txt";
   public static final int MAXNUMSEEN = 50;
	//public static final String animeinput = ".\\src\\AnimeInput";
	public static final int DATA = 14033; //The number of anime in the database
	public static final int DATAI = 1; //The number of anime in the input list
	
	public static void main(String[] args) throws FileNotFoundException {
		int count = 0;
		int countI = 0;
		String[] info;
		Scanner file = new Scanner (new File(animedatabase), "UTF-8");
		String line;
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
		//Scanner file2 = new Scanner (new File(animeinput), "UTF-8");
		/*while (file2.hasNext()){
			inputNames[countI] = file2.nextLine();
			countI++;
		}*/
      
      System.out.println("Building the database...\n");
      
		int blah = 0;
      char first = ' ';
		// Build database and gather data
		while (file.hasNext()){
			line = file.nextLine();
			info = line.split("~");
         
         
         if (blah == 0)
            first = info[0].charAt(0);
         
         //System.out.println(blah+" - "+info[0]);
            
         if (info[0].charAt(0) == first)
            info[0] = info[0].substring(1);
         blah++;
			
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
			if (year < 1900)
            year = 2017;
         
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
      /*for (int i=0; i<DATA; i++)
         if (database[i].getYear() > 2018 || database[i].getYear() < 1900)
            System.out.println(i+": "+database[i].getName()+" - "+database[i].getYear());*/
      //
      
      shell(database, DATA);
		//for(int i=0; i<studioAll.length; i++){
		//	if (studioAll[i]!=null)
		//		System.out.println (studioAll[i]);
		//}
		
		//System.out.println(2*database[0].compareGen(database[898], database[897]));
		//System.out.println(2*database[0].compareGen(database[897], database[898]));
		
		// K-nearest neighbor test
		//String nntest = "Dragon Ball Z Special 2 : Zetsubou e no Hankou ! ! Nokosareta Chousenshi";		// Nearest neighbor test anime name
		//Anime[] output = nearestNeighbor(database, input);
		//System.out.println(output[0].getName() + " - " + input[0].distance(output[0]));
	   //System.out.println(output[1].getName() + " - " + input[0].distance(output[1]));
		//System.out.println(output[2].getName() + " - " + input[0].distance(output[2]));
		//System.out.println(output[3].getName() + " - " + input[0].distance(output[3]));
		
		//System.out.println(database[0].compareGen(database[1], database[0]));
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
	// Note that we look at maxDist because of how distance is calculated in Anime.java
	public static Anime[] nearestNeighbor (Anime[] database, Anime input, int seen)
	{
		Anime[] suggestion = new Anime[DATA];
		Anime[] temp = new Anime[seen];
		double[] maxDist= new double[DATA];
		
		// Copy database to suggestion
		for (int i=0; i<DATA; i++){
			Anime a = new Anime (database[i].getName(), database[i].getGenre(), database[i].getStudio(), 
					database[i].getYear(), database[i].getRating(), database[i].getType());
			suggestion[i] = a;
      }
		//System.out.println(suggestion[1].getName());
      // Separate input anime from anime database
		for (int i=0; i<seen; i++){
      //System.out.println(i+": "+input[i].getName());
			for (int j=0; j<DATA; j++){
         //System.out.println(j+": "+suggestion[j].getName());
				try {
            if (suggestion[j].getName().equals(input.getName())){
					Anime a = suggestion[seen-i];
					temp[i] = suggestion[j];
					suggestion[seen-i] = null;
					suggestion[j] = a;
               //suggestion[j] = null;
				} } catch (NullPointerException e) {}
			}
		}
		
		// Calculate all distances
		for (int i=0; i<seen; i++){
			for (int j=0; j<DATA; j++){
				try 
            {
               if (suggestion[j]!=null){
					   maxDist[j] = temp[i].distance(suggestion[j]);
               } 
            }catch (NullPointerException e)  {}      
				
			}
		}
		
		for (int i=0; i<seen; i++){
			for(int j=DATA-1; j>=0; j--){
				for (int k=1; k<j; k++){
					if(suggestion[k]!=null)
					{
						if (maxDist[k-1] < maxDist[k]){
							double m = maxDist[k-1];
							Anime b = suggestion[k-1];
							maxDist[k-1] = maxDist[k];
							suggestion[k-1] = suggestion[k];
							maxDist[k] = m;
							suggestion[k] = b;
						}
					}
					
					/*if (temp[i].distance(database[j])>maxDist[2]){
						if (temp[i].distance(database[j])>maxDist[1]){
							if (temp[i].distance(database[j])>maxDist[0]){
								if (temp[i].distance(database[j])>maxDist[1]){
									maxDist[1] = maxDist[0];
									suggestion[1] = database[0];
									System.out.println("1---");
								}
								maxDist[0] = temp[i].distance(database[j]);
								suggestion[0] = database[j];
								System.out.println("1. " + suggestion[0].getName() + " - " + maxDist[0]);
								//break;
							}
							else{
								if (temp[i].distance(database[j])>maxDist[2]){
									maxDist[2] = maxDist[1];
									suggestion[2] = database[1];
									System.out.println("2---");
								}
								maxDist[1] = temp[i].distance(database[j]);
								suggestion[1] = database[j];
								System.out.println("2. " + suggestion[1].getName() + " - " + maxDist[1]);
								//break;
							}
						}
						else{
							maxDist[2] = temp[i].distance(database[j]);
							suggestion[2] = database[j];
							System.out.println("3. " + suggestion[2].getName() + " - " + maxDist[2]);
							//break;
						}
					}*/
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

   
   public static void shell(Anime[] a, int b)
   {
      Scanner scan = new Scanner(System.in);
      String response = ""; //store user responses
      int len = b;
      boolean quit = false;
      boolean tsundere = false; //easter egg
      
      Anime[] animeOptions = new Anime[b]; //creates array of options based on user's search
      int numOptions = 0; //stores number of matches for user search
      int count = 0;
      
      Anime[] seen = new Anime[MAXNUMSEEN];//stores anime user has decided to use for searching
      int numSeen = 0; //keeps track of number of anime user has decided to use for searching
      
      System.out.println("Hello!\nWelcome to Nick & John's\nSuper Happy Mega Fun Heart Lovey-Dovey Anime Suggester!"); // USER INSTRUCTIONS
      options(tsundere);
      System.out.println("\nTo begin, type in one of the above commands.");
            
      while (true)
      {
         if (tsundere == false)
            System.out.println("\nWhat would you like to do?");
         else
            System.out.println("\nSo you're still here, huh?\nWell I guess if you're still here, I have no choice.\nWhat do you want to do next?");
         response = scan.nextLine(); //read input from user
         response = response.toLowerCase();
         
         switch (response)
         {
            case "quit": 
               quit = true;
               break;
            case "search": 
               seen = search(a, seen, len, numSeen, tsundere);
               break;
            case "remove": 
               seen = remove(seen, numSeen, tsundere);
               break;
            case "options":
               options(tsundere);
               break;
            case "view":
               view(seen, numSeen, tsundere);
               break;
            case "run":
               run(a, seen, numSeen, tsundere);
               break;
            case "clear":
               clear(seen, numSeen, tsundere);
               numSeen = 0;
               break;
            case "tsundere":
               tsundere = true;
               break;
            case "normal":
               tsundere = false;
               break;
            default: 
               if (tsundere == false)
                  System.out.println("Error: invalid input.");
               else
                  System.out.println("What are you trying to pull you idiot! That's not a valid input!\nUgh! Why do you have to be such an idiot all the time!"); 
         }
         
         numSeen = 0;
         for (int i=0; i<MAXNUMSEEN; i++)
            if (seen[i] != null)
               numSeen++;
                  
         if (quit == true)
         {
            if (tsundere == false)
               System.out.println("Goodbye!");
            else
               System.out.println("You're leaving already?\nDon't get the wrong idea!\nIt's n-not like I w-wanted you to stay or anything...\nBut you better come back!\nI mean, b-because otherwise...\nWho's going to use my amazing program?");
            return;
         }
         
         if (numSeen != 0)
         {
            System.out.println("\nYour list of anime so far is:\n"); // prints the user's anime selections thus far
            for(int i=0; i<numSeen; i++)
               System.out.println(seen[i].getName() + " - " + seen[i].getURating());               
         }
       }
   }

public static Anime[] search (Anime[] a, Anime[] options, int b, int c, boolean t)
{
   if (t == false)
   {
      System.out.println("You have decided to search for anime."); // user instructions
      System.out.println("\nWhatever you type in next, this program will search our database for it.");
      System.out.println("It could be a single word, a whole title, or even a few letters.");
      System.out.println("Please keep in mind though, that some special characters might not be compatible with the database.");
      System.out.println("You may enter a maximum of 50 anime in your list.");
   }
   else
   {
      System.out.println("Well you big dummy, it looks like you want to search for anime.");
      System.out.println("I know you're kind of stupid, so I guess I'll help you out just this once.");
      System.out.println("But don't get the wrong idea!");
      System.out.println("It's not because I l-like you or anything!");
      
      System.out.println("\nWhatever you type in next, my amazing program will search our huge database for it.");
      System.out.println("It could be a single word, a whole title, or even a few letters.");
      System.out.println("The size doesn't really matter.");
      System.out.println("W-wait! I didn't mean it like that!");
      System.out.println("Y-you p-pervert!");
      
      System.out.println("\nAnyway, try not to forget that some special characters might not be compatible with the database.");
      System.out.println("Also, you can only add up to 50 anime in your list.\nIf that's not enough then that's really sad.\nI mean jeez! How boring can you be?");
      System.out.println("D-did you get all that, because I am NOT going to repeat it!");
   }
   
   String response = "";
   int len = b;
   Scanner scan = new Scanner(System.in);
   Anime[] animeOptions = new Anime[len];
   int numOptions = 0;
   int count = 0;
   Anime[] seen = options;
   int numSeen = c;
   int rating = 0;
   boolean validRating = false;
   
   if (t == false)
      System.out.println("\nYou can enter \"quit\" if you changed your mind.\nOtherwise, please enter the name of an anime to search:");
   else
      System.out.println("\nI know how indecisive you can be, so just type \"quit\" if you want to stop searching.\nOtherwise, just hurry up and enter the name of an anime to search:");
   while(true)
   {
      response = scan.nextLine(); //read input from user
      response = response.toLowerCase();
      
      if (response.equals("quit"))
         return(seen);
            
         //System.out.println(len);
         for (int i=0; i<len; i++)//search database for name of anime
         {
            if (a[i].getName().toLowerCase().contains(response))
            {
               animeOptions[numOptions] = a[i];
               numOptions++;
               //System.out.println(animeOptions[numOptions-1].getName());
            }
            //System.out.println(i);
         }
         
         if (animeOptions[0] == null) // checks if search results list is empty
            if (t == false)
               System.out.println("Sorry, no results were found matching your search. Please try again.");
            else
               System.out.println("What are you? Stupid? We don't have that anime! Try searching for something we actually HAVE!\nIdiot!");
         else
         {
            if (t == false)
               System.out.println("\nYour options are:\n");//print results of search
            else
               System.out.println("\nHere's what my amazing code came up with.\nFeel free to praise me!\n");
            for (int i=0; i<numOptions; i++)
            {
               System.out.println(count + ": " + animeOptions[i].getName());
               count++;
            }
         
            while(true) // allows users to add search results to the list of seen anime
            {
               if (t == false)
               {
                  System.out.println("\nPlease enter the number next to an anime to add it to your list.");
                  System.out.println("Enter \"done\" at any time to start another search");
               }
               else
               {
                  System.out.println("\nWasn't that incredible?\nDon't worry, it's only natural to want to praise me.\nJust don't forget to make good use of my code by typing one of those numbers next an anime.\nThat'll add it to your list, in case you were too stupid to tell.");
                  System.out.println("Of course, if you ARE satisfied, you can just type \"done\" at any time to start another search.");
               }
               response = scan.nextLine();
               if(response.toLowerCase().equals("quit")) // if user types "quit", exits shell
                  return seen;
               if(response.toLowerCase().equals("done")) // if user types "done", breaks loop and allows them to search again
                  break;
               try 
               {
                  if (Integer.parseInt(response) < count && numSeen < MAXNUMSEEN && Integer.parseInt(response) > -1) // checks to make sure a valid number has been entered
                  {
                     if (numSeen > 0) // checks if seen[] is empty
                     {  
                        int oldNumSeen = numSeen;
                        boolean duplicate = false;
                        for (int i=0; i<oldNumSeen; i++) // compares user selction with everything in the seen[] array to prevent duplicates
                           if (seen[i].getName().equals(animeOptions[Integer.parseInt(response)].getName()))
                              duplicate = true;
                           
                        if (duplicate == true)// checks for duplicates
                           if (t == false)
                              System.out.println("Error: You have already selected that anime.");
                           else
                              System.out.println("Are you an idiot?\nYou already added that anime to your list! BAKA!");
                        else
                        {
                           seen[numSeen] = animeOptions[Integer.parseInt(response)]; // if number is valid, adds selected anime to array
                           numSeen++;
                           
                           
                           while (validRating == false) // allows users to enter a rating
                           {
                              if (t == false) 
                                 System.out.println("\nNow please rate this anime on a scale from 1 to 10, with 10 being the highest.");
                              else
                                 System.out.println("\nWow! I'm actually impressed you made it this far!\nThere's just one more thing left: you have to rate the anime on a scale from 1 to 10.\nTen is obviously the highest, in case you were too stupid to figure that out.");
                           
                              response = scan.nextLine();
                           
                              try
                              {
                                 if (Integer.parseInt(response) > 0 && Integer.parseInt(response) < 11) // checks if rating is valid
                                 {
                                    rating = Integer.parseInt(response);
                                    validRating = true;
                                    seen[numSeen-1].setURating(rating);
                                 }
                                 else
                                    if (t == false)
                                       System.out.println("\nError: Please enter an integer between 1 and 10");
                                    else
                                       System.out.println("\nAre you an idiot?\nI said a number between 1 and 10!\nThis isn't that difficult!\nI can't believe you're this stupid!");
                              } catch (NumberFormatException e)
                              {
                                 if (t == false)
                                    System.out.println("Error: Invalid entry. Please enter a number between 1 and 10.");
                                 else
                                    System.out.println("Incredible! You really are a complete moron!\nI said a NUMBER between 1 and 10!\nBut you couldn't even get the number part right!\nIDIOT!");
                              }
                           }
                        }   
                        
                     }
                     else
                     {
                        seen[numSeen] = animeOptions[Integer.parseInt(response)]; // if number is valid, adds it to array
                        numSeen++;
                        
                        while (validRating == false) // allows users to enter a rating
                        {
                           if (t == false)
                                 System.out.println("\nNow please rate this anime on a scale from 1 to 10, with 10 being the highest.");
                              else
                                 System.out.println("\nWow! I'm actually impressed you made it this far!\nThere's just one more thing left: you have to rate the anime on a scale from 1 to 10.\nTen is obviously the highest, in case you were too stupid to figure that out.");
                           
                              response = scan.nextLine();
                           
                              try
                              {
                                 if (Integer.parseInt(response) > 0 && Integer.parseInt(response) < 11) // checks if rating is valid
                                 {
                                    rating = Integer.parseInt(response);
                                    validRating = true;
                                    seen[numSeen-1].setURating(rating);
                                 }
                                 else
                                    if (t == false)
                                       System.out.println("\nError: Please enter an integer between 1 and 10");
                                    else
                                       System.out.println("\nAre you an idiot?\nI said a number between 1 and 10!\nThis isn't that difficult!\nI can't believe you're this stupid!");
                              } catch (NumberFormatException e)
                              {
                                 if (t == false)
                                    System.out.println("Error: Invalid entry. Please enter a number between 1 and 10.");
                                 else
                                    System.out.println("Incredible! You really are a complete moron!\nI said a NUMBER between 1 and 10!\nBut you couldn't even get the number part right!\nIDIOT!");
                              }
                           }
                     }
                  }
                  else if (numSeen < MAXNUMSEEN)
                     if (t == false)
                        System.out.println("Error: Invalid entry. Please enter a number next to an anime that has been printed.");
                     else
                        System.out.println("ANTA BAKA? Enter one of the numbers that appears on the screen!\nJeez, you really are an idiot!");
                  else
                     if (t == false)
                        System.out.println("Error: Your list is full. Please delete an anime before adding more.");
                     else
                        System.out.println("Your list is already full!\nJeez, how stupid can you be!");
               } catch (NumberFormatException e)
                  {
                     if (t ==false)
                        System.out.println("Error: Invalid entry. Please enter a number next to an anime that has been printed."); 
                     else
                        System.out.println("Jeez! Just how much of an IDIOT are you?\nThat's not even a number!");
                  }
               validRating = false;
            }
         
            if (t == false)
               System.out.println("\nYour list of anime so far is:\n"); // prints the user's anime selections thus far
            else
               System.out.println("\nHere's your list so far, and it's all thanks to my AMAZING programming skills:");
            for(int i=0; i<numSeen; i++)
               System.out.println(seen[i].getName() + " - " + seen[i].getURating());
            if (t == true)
               System.out.println("Feel free to shower me with praise!");
                 
            if (t == false)
            {
               System.out.println("\nPlease enter another anime name to search again.");
               System.out.println("Or enter \"quit\" to stop searching.");
            }
            else
            {
               System.out.println("\nIsn't my program simply AMAZING!");
               System.out.println("I hope you realize now just how superior I am compared to an idiot like you!");
               System.out.println("\nI realize you probably can't get enough of my genius, so feel free to search for more anime.");
               System.out.println("Of course, if your tiny brain is on overload, you can always type \"quit\". Of course, that would just make you a coward.");
            }   
            for(int i=0; i<numOptions; i++) // resets items necessary for another search
               animeOptions[i] = null;
            numOptions = 0;
            count = 0;
            validRating = false;
            
            //System.out.println("Rating: " + rating);
         }
            
            
  
   }
}

public static Anime[] remove (Anime [] a, int b, boolean t) // removes anime from the user's list
{
   if (t == false)
   {
      System.out.println("You have decided to remove anime from your list."); // user instructions
      System.out.println("\nPlease follow the instructions below to remove items properly.");
   }
   else
   {
      System.out.println("See! I knew you'd mess something up you IDIOT!");
      System.out.println("\nGuess I'll have to tell you how to remove anime properly.");
      System.out.println("You better listen up though!\nI can't have you messing up my masterful code!");
   }
   
   Anime[] seen = a;
   int numSeen = b;
   Scanner scan = new Scanner(System.in);
   String response = "";
   
   if (numSeen > 0)
   {
      while(true)
      {
         if (t == false)
            System.out.println("\nHere is your list so far:\n");//print current list
         else
            System.out.println("\nHere's the list you managed to mess up:\n");
         for(int i=0; i<numSeen; i++)
            System.out.println(i + ": " + seen[i].getName());
            
         if (t == false)
         {
            System.out.println("\nPlease enter the number of the anime you would like to remove.");
            System.out.println("Or enter \"done\" to stop removing anime");
         }
         else
         {
            System.out.println("\nNow I'll keep this really simple so your tiny brain can handle it.");
            System.out.println("All you have to do is enter the number next to the anime you want to remove.");
            System.out.println("Or, if you managed to fix this mess that YOU caused, enter \"done\".");
            System.out.println("Got it?");
            System.out.println("Now try not to mess this up!");
         }
               
         response = scan.nextLine(); //read input from user
         response = response.toLowerCase();
         if (response.equals("done"))
            break;
                          
         try
         {
            if (Integer.parseInt(response) > -1 && Integer.parseInt(response) < numSeen)
            {
               int deleted = Integer.parseInt(response);
               seen[deleted] = null; // delete selected anime
                        
               if (deleted == (numSeen - 1))// if deleted anime is last in array
                  numSeen--;
               else if (deleted == 0) // if deleted anime is first in array
               {
                  for (int i=0; i<numSeen; i++)
                  seen[i] = seen[i+1];
                  numSeen--;
               }
               else //if deleted anime is in middle of list
               {
                  for (int i=deleted; i<numSeen; i++)
                  seen[i] = seen[i+1];
                  numSeen--;
               }
            }
            else
            {
               if (t == false)
                  System.out.println("Error: Please enter a number next to one of the anime.");
               else
                  System.out.println("Jeez! The numbers are on the freaking screen!\nCan't you read?");
            }
         } catch (NumberFormatException e)
            {
               if (t == false)
                  System.out.println("Error: Invalid entry. Please enter a number.");
               else
                  System.out.println("Are you kidding!\nI said to enter a NUMBER!\nA NUMBER!\nJUST HOW STUPID CAN YOU BE!");
            }
      }
   }
   else // if user tries to reomove from an empty array
      if (t == false)
         System.out.println("Error: your list is already empty.");
      else
         System.out.println("What are you? Stupid?\nYour list is EMPTY!\nYou can't remove something from an empty list!\nBAKA!");
   return seen;
}

public static void options(boolean t)
{
   if (t == false)
   {
      System.out.println("Here are your options:\n");
      System.out.println("\"quit\": Exits the program");
      System.out.println("\"search\": Lets you search for anime to add to your list");
      System.out.println("\"remove\": Lets you remove anime from your list");
      System.out.println("\"options\": Displays the controls");
      System.out.println("\"view\": Displays the current list of anime");
      System.out.println("\"run\": Runs the program to give suggestions based on the current anime in the list");
      System.out.println("\"clear\": Clears the user's anime list");
      System.out.println("\nHope this helped!");
   }
   else
   {
      System.out.println("Jeez!\nI knew you'd need my help!");
      System.out.println("You better listen up, because I'm only going to say this once!\nHere are the controls:\n");
      System.out.println("\"quit\": Exits the program");
      System.out.println("\"search\": Lets you search for anime to add to your list");
      System.out.println("\"remove\": Lets you remove anime from your list");
      System.out.println("\"options\": Displays the controls");
      System.out.println("\"view\": Displays the current list of anime");
      System.out.println("\"run\": Runs the program to give suggestions based on the current anime in the list");
      System.out.println("\"clear\": Clears the user's anime list");
      System.out.println("\nThey're pretty self-explanatory, but maybe you'd better write them down or something.");
      System.out.println("Anyway, don't ask me again.\nI mean, it's not like I HATED helping you or anything...\nB-but don't get the wrong idea!\nI mean...\nIt's not like I l-liked helping you either.\nDummy.");
   }
   return;
}

public static void view(Anime[] a, int b, boolean t)
{
   if (b == 0)
      if (t == false)
         System.out.println("Your list is currently empty.");
      else
         System.out.println("Are you a complete idiot?\nYour list is EMPTY!\nIDIOT!");
   else
      for (int i=0; i<b; i++)
         System.out.println(a[i].getName() + " - " + a[i].getURating());
   return;
}

public static Anime[] run(Anime[] a, Anime[] b, int c, boolean t) // takes user list of seen anime and suggests new anime
	{
	   if (c == 0) // checks if user list is empty
	   {
	      if (t == false)
	         System.out.println("Error: Your list is empty. Please add items to your list before attempting to run the suggestion algorithm.");
	      else
	         System.out.println("What are you, stupid or something? I can't suggest any new anime if don't tell me what you like!\nGo back and add some anime to your list first!\nD-dummy!");
	      return null;
	   }
	   else // if list is not empty, runs code
	   {
	      if (t == false)
	         System.out.println("We will now search our database for other anime you might like. Please wait a moment.");
	      else
	         System.out.println("Get ready to see how amazing my program is!\nIn no time at all, I'll have a bunch of new anime for you to try!");
	      
	      int num = 3*c; // number of outputs
	      //System.out.println(num);
	      Anime[] output = new Anime[num];
	      String[] out1 = new String[num];
	      double[] out2 = new double[num];
	      
	      for (int i=0; i<c; i++)
	         try
	         {
	            for (int j=0; j<3; j++){
	            	output = nearestNeighbor(a, b[i], c);
	            	out1[3*i+j] = output[j].getName();
	            	out2[3*i+j] = b[i].distance((output[j]));
	            	//System.out.println(out1[3*i+j] + " - " + out2[3*i+j]);
	            }
	         } catch (NullPointerException e) {}
	      
	      // Remove duplicates
	      for (int i=0; i<num; i++)
		   {
			   for (int j=i+1; j<num; j++)
			   {
				   if (out1[i]!=null && out1[j]!=null)
					   if (out1[i].equals(out1[j])){
						   if (out2[i]<out2[j]){
							   out1[i] = null;
							   out2[i] = 0;
							   }
						   else{
							   out1[j] = null;
							   out2[j] = 0;
						   }
					   }
				   }
		   }
	      
         // Remove suggestions that are already on list
	      int count = 0;
	      for (int i=0; i<c; i++){
	    	  for (int j=0; j<num; j++){
	    		  if (b[i].getName().equals(out1[j])){
	    			  out1[j]=null;
                 out2[j]=0;
	    			  count++;
	    		  }
	    	  }
	      }
         
	      // Sort
	      for(int j=num-1; j>=0; j--){
				for (int k=1; k<j+1; k++){
					//if(out1[k]!=null)
					//{
						if (out2[k-1] < out2[k]){
							double m = out2[k-1];
							String z = out1[k-1];
							out2[k-1] = out2[k];
							out1[k-1] = out1[k];
							out2[k] = m;
							out1[k] = z;
						}
					//}
				}
	      }
	      
	      // Print results
	      
         if (t == false)
	         System.out.println("\nHere are the suggestions the algorithm came up with:\n");
	      else
	         System.out.println("\nAlright, here's what my genius algorithm came up with!\nI guarantee that they'll all become your new favorite shows!\n");
            
         System.out.println("\n-------------\n");
	      for (int i=0; i<num; i++){
           if (out1[i]!=null && i<30)
	    		  System.out.println((i+1) + ".  " + out1[i] + " - " + out2[i]);
	      }
	      System.out.println("Number suggested that is already on list: " + count);
	      
	   }
	   return null;
	}


public static void clear (Anime[] a, int b, boolean t) // clears the entire list of anime
{
   if (b == 0) // prints error message if list is empty
      if (t == false)
         System.out.println("Error: List is already empty.");
      else
         System.out.println("Anta Baka? The list is already empty you moron!");
   else
   {
      if (t == false)
         System.out.println("\nYour anime list will now be cleared.");
      else
         System.out.println("\nJeez! I just knew you'd mess this up!\nGuess I have no choice. I'll delete your anime list for you.");
      
      for (int i=0; i<b; i++) // deletes all user entries
         a[i] = null;
         
      if (t == false)
         System.out.println("\nYour anime list has been successfully cleared.");
      else
         System.out.println("\nThere! Your list is all clear again!\nJust try not to mess it up again.");
   }
}

}

