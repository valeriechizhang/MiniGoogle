/* File: MiniGoggle.java
 * Author: Chi Zhang (chizhang@bu.edu)
 * Date: 4/29/2015
 * Description: This is a client program that allows the user to search articles 
 * by with phrases instead of just titles.
 * 
 * MiniGoogle.java
 *
 * A client program that uses the DatabaseIterator
 * and Article classes, along with additional data
 * structures, to allow a user to create, modify
 * and interact with a encyclopedia database.
 *
 * Author: Alexander Breen (abreen@bu.edu) and Wayne Snyder (waysnyder@gmail.com)
 * Date: March 24, 2014
 */

import java.util.*;

public class MiniGoogle {
  
  private static final String [] blackList = { "the", "of", "and", "a", "to", "in", "is", 
    "you", "that", "it", "he", "was", "for", "on", "are", "as", "with", 
    "his", "they", "i", "at", "be", "this", "have", "from", "or", "one", 
    "had", "by", "word", "but", "not", "what", "all", "were", "we", "when", 
    "your", "can", "said", "there", "use", "an", "each", "which", "she", 
    "do", "how", "their", "if", "will", "up", "other", "about", "out", "many", 
    "then", "them", "these", "so", "some", "her", "would", "make", "like", 
    "him", "into", "time", "has", "look", "two", "more", "write", "go", "see", 
    "number", "no", "way", "could", "people",  "my", "than", "first", "water", 
    "been", "call", "who", "oil", "its", "now", "find", "long", "down", "day", 
    "did", "get", "come", "made", "may", "part" }; 
  

  // initializers
  public static Article[] getArticleList(DatabaseIterator db) {
    int count = db.getNumArticles();
    Article[] list = new Article[count];
    for(int i = 0; i < count; i++) 
      list[i] = db.next();
    return list;
  }
  
  private static DatabaseIterator setupDatabase(String path) {
    return new DatabaseIterator(path);
  }

  // add method
  private static void addArticle(Scanner s, ArticleTable AT) {
    System.out.println("\nAdd an article\n==============");
    
    System.out.print("Enter article title: ");
    String title = s.nextLine();
    
    System.out.println("You may now enter the body of the article.");
    System.out.println("Press return two times when you are done.");
    
    String body = "";
    String line = "";
    do {
      line = s.nextLine();
      body += line + "\n";
    } while (!line.equals(""));
    
    AT.insert(new Article(title, body));                    
  }

  // remove method
  private static void removeArticle(Scanner s, ArticleTable AT) {
    System.out.println("\nRemove an article\n=================");
    
    System.out.print("Enter article title: ");
    String title = s.nextLine();
    
    AT.delete(title);
  }
  
  // search with a title
  private static void titleSearch(Scanner s, ArticleTable AT) {
    System.out.println("\nSearch by article title\n=======================");
    
    System.out.print("Enter article title:");
    String title = s.nextLine();
    
    Article a = AT.lookup(title);
    if (a != null)
      System.out.println(a);
    else {
      System.out.println("Article not found!");
      return;
    }
    
    System.out.println("Press return when finished reading.");
    s.nextLine();
  }
  
  // Take a string, turn it into all lower case, and remov all characters
  // except for letters, digits, and whitespace
  private static String preprocess(String s) {
    String lowerS = s.toLowerCase();
    char term[] = lowerS.toCharArray(); // break them down to characters
    
    String newS = "";
    
    for(int i = 0; i < term.length; i++) {
      if(Character.isWhitespace(term[i]) == true || 
         Character.isLetter(term[i]) == true ||
         Character.isDigit(term[i]) == true){
        newS += term[i]; // put qualified characters back into a string
      }
    }   
    return newS;
  }
  
  // determine if the string a is member of the blacklist
  private static boolean memberBlackList(String s) {
    for (int i = 0; i < blackList.length; i++) {
      if (s.compareTo(blackList[i]) == 0)
        return true;
    }
    return false;
  }
  
  // Create a TermFrequencyTable of the terms into the table with its docNum
  // return the cosine similarity
  private static double getCosineSimilarity(String s, String t) {
    s = preprocess(s);
    t = preprocess(t);
    
    StringTokenizer stS = new StringTokenizer(s);
    StringTokenizer stT = new StringTokenizer(t);
    
    TermFrequencyTable TFT = new TermFrequencyTable();
    
    // insert the first string
    while (stS.hasMoreTokens()){
      String temp = stS.nextToken();
      if (memberBlackList(temp) == false)
        TFT.insert(temp, 0);
    }
    
    // insert the second string
    while (stT.hasMoreTokens()){
      String temp = stT.nextToken();
      if (memberBlackList(temp) == false)
        TFT.insert(temp, 1);
    }
    
    double cs = TFT.cosineSimilarity(); // calculate the cosine similarity
    return cs;
  }
  
  
  // Take an ArticleTable and search it for articles most similar to the phrase
  // return a string reponse that includes the top three
  public static String phraseSearch(String phrase, ArticleTable T) {
    
    MaxHeap H = new MaxHeap();
    
    // interator
    T.reset();
    while(T.hasNext()) {
      Article m = T.next();
      double cs = getCosineSimilarity(phrase, m.getBody());
      m.setCS(cs);
      if (cs > 0.0001) {
        H.insert(m);
      }
    }
   
    String result = "";
    
    int count = 0;
    
    Article first = H.getMax();
    if (first != null) {
      count ++;
      result += "\nMatch 1 with cosine similarity of " + first.getCS() + "\n\n" + first; 
    }
    
    Article second = H.getMax();
    if (second != null){
      count ++;
      result += "\nMatch 2 with cosine similarity of " + second.getCS() + "\n\n" + second; 
    }
    
    Article third = H.getMax();
    if (third != null){
      count ++;
      result += "\nMatch 3 with cosine similarity of " + third.getCS() + "\n\n" + third; 
    }
    
    if (count == 0)
      return "There are no matching articles.";
    else
      return "Top " + count + " Matches: \n" + result;
  }
  

  public static void main(String[] args) {
    Scanner user = new Scanner(System.in);
    
    String dbPath = "articles/";
    
    DatabaseIterator db = setupDatabase(dbPath);
    System.out.println("Read " + db.getNumArticles() + " articles from disk.");
    
    ArticleTable L = new ArticleTable(); 
    Article[] A = getArticleList(db); 
    L.initialize(A); 
    
    int choice = -1;
    do {
      System.out.println("\nWelcome to MiniGoogle!");
      System.out.println("=====================");
      System.out.println("Make a selection from the following options:");
      
      System.out.println("\nManipulating the database");
      System.out.println("-------------------------");
      System.out.println("    1. add a new article");
      System.out.println("    2. remove an article");
      System.out.println("    3. Search by article title");
      System.out.println("    4. Search by phrase (list of keywords");
      
      System.out.print("\nEnter a selection (1-4, or 0 to quit): ");
      
      choice = user.nextInt();
      user.nextLine();
      
      switch (choice) {
        case 0:
          System.out.println("Bye!");
          return;
        
        case 1:
          addArticle(user, L);/////////////////
          break;
        
        case 2:
          removeArticle(user, L);
          break;
        
        case 3:
          titleSearch(user, L);
          break;
          
        case 4:
          System.out.println("\nSearch by article content\n=======================");
          System.out.print("Enter phrase:");
          String phrase = user.nextLine();
          
          String result = phraseSearch(phrase, L);
          System.out.println(result);
          break;
      }
      
      choice = -1;
      
    } while (choice < 0 || choice > 4);
  }
  
}
