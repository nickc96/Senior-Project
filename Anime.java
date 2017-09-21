public class Anime
{
   private String name;
   private String genre[];
   
   public Anime(String n, String g [])
   {
      String name = n;
      String genre [] = g;
   }
   
   String getName(Anime a)
   {
      return a.name;
   }
   
   String[] getGenre(Anime a)
   {
      return a.genre;
   }
}