public class Test{
  public static void main(String args[]){
    
    String s = "Cats, also called domestic cats or house cats (Felis catus), are carnivorous";

    String lowerS = s.toLowerCase();
    char term[] = lowerS.toCharArray();
    String newS = "";
    
    for(int i = 0; i < term.length; i++) {
      if(Character.isWhitespace(term[i]) == true || 
         Character.isLetter(term[i]) == true ||
         Character.isDigit(term[i]) == true){
        newS += term[i];
      }
    }
    
    System.out.println(newS);
  }
}