public class Anime
{
   private String name;
   private String genre[];
   private String studio;
   private double year;
   private double minYear = 1999;		// Change based on minimum year
   
   public Anime(String n, String g [], String s, int y)
   {
      name = n;
      genre = g;
      studio = s;
      year = y;
   }
   
   String getName()
   {
      return name;
   }
   
   String[] getGenre()
   {
      return genre;
   }
   
   String getStudio()
   {
	   return studio;
   }
   
   double getYear ()
   {
	   return year;
   }
   
   void setName(String a)
   {
      name = a;
   }
   
   void setGenre(String[] a)
   {
      genre = a;
   }
   
   void setStudio(String a)
   {
	   studio = a;
   }
   
   void setYear (double a)
   {
	   year = a;
   }
   
   double compareGen (Anime a, Anime b)
   {
	  boolean seen = false;
	  boolean cont = true;
	  int count = 0;
	  int icount = 0;
	  int ucount = 0;
	  int length = a.getGenre().length + b.getGenre().length;
	  String[] temp = new String[length];
	  String[] union = new String[length];
	  String[] intersection = new String[length];
	  
	  for(int i=0; i<a.getGenre().length; i++)
	  {
		  temp[i] = a.getGenre()[i];
		  union[i] = a.getGenre()[i];
		  count++;
	  }

	  // intersection
	  icount = 0;
	  for(int i=0; i<b.getGenre().length; i++)
	  {
		  for(int j=0; temp[j]!=null; j++)
		  {
			  if(temp[j].equals(b.getGenre()[i]))
			  {
				  intersection[icount] = temp[j];
				  icount++;
			  }
		  }
	  }

	  // union
	  ucount = count;
	  for(int i=0; i<b.getGenre().length; i++)
	  {
		  for(int j=0; union[j]!=null && cont==true; j++)
		  {
			  if (union[j].equals(b.getGenre()[i]))
			  {
				  seen=true;
			  }	  
		  }
		  if(!seen)
		  {
			  union[ucount] = b.getGenre()[i];
			  ucount++;
			  cont=false;
		  }
		  cont = true;
		  seen=false;
	  }

	  return ((double)icount/(double)ucount);
   }
   
   double compare (String a, String b)
   {
	   if (a.equals(b))
		   return 1;
	   else
		   return 0;
   }
   
   double yearFactor(Anime a)
   {
	   double output;
	   output = 1-(Math.abs(this.year-a.getYear())/(2017-minYear));
	   return output;
   }
   
   double distance (Anime a)
   {
	   double dist;
	   double total;
	   total = Math.pow(2*compareGen(this, a),2) + Math.pow(compare(this.studio,a.getStudio()),2) + Math.pow(this.yearFactor(a),2);
	   dist = Math.sqrt (total);
	   return dist;
   }
}