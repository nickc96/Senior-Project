public class Anime
{
   private String name;
   private String genre[];
   
   public Anime(String n, String g [])
   {
      name = n;
      genre = g;
   }
   
   String getName()
   {
      return name;
   }
   
   String[] getGenre()
   {
      return genre;
   }
   
   void setName(String a)
   {
      name = a;
   }
   
   void setGenre(String[] a)
   {
      genre = a;
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
	  System.out.println("icount (should be 4)= " + icount +"\nucount (should be 7)= " + ucount);
	  return ((double)icount/(double)ucount);
   }
}