/* File: TermFrequencyTable.java
 * Author: Chi Zhang (chizhang@bu.edu)
 * Date: 4/29/2015
 * Description: This a program that stores the words from two Strings into a table and
 * calculate the cosine similarity of the two.
 */

public class TermFrequencyTable{
  
  private final int SIZE = 103;
  private Node [] T = new Node[SIZE];
 
  // toString methods
  private String termToString(Node p) {
    if (p == null)
      return "";
    else
      return p.term + termFreqToString(p) + ", " + termToString(p.next);
  }
  
  private String termFreqToString(Node p) {
    if (p == null)
      return "";
    else
      return "[" + p.termFreq[0] + "][" + p.termFreq[1] + "]";
  }
  
  private class Node{
    String term;
    Node next;
    int[] termFreq = new int[2]; 
    // that gives the term frequency in each of the two documents of this term
    
    public Node(String t){
      this.term = t;
    }
  }
  
  // hash function copied from the website
  private int hash(String s) {
    char ch[] = s.toCharArray();
    int sum = 0;
    for(int i = 0; i < ch.length; i++){
      sum += ch[i];
    }
    return sum % SIZE;
  }
  
  // enter two Strings into the frequency table
  public void initialize(String a, String b){
    String []A = a.split(" ");
    String []B = b.split(" ");
    
    for (int i = 0; i < A.length; i++) {
      insert(A[i], 0);
    }
    
    for (int j = 0; j < B.length; j++) {
      insert(B[j], 1);
    } 
  }
  
  // Insert a term from a document into the table and note their document numbers
  public void insert(String term, int docNum){
    if (member(term) == false) {
      T[hash(term)] = insertHelper(term, T[hash(term)]);
    }
    
    Node p = lookup(term, T[hash(term)]);
    p.termFreq[docNum] ++;
  }
  
  private Node insertHelper(String term, Node p) {
    if (p == null)
      return new Node(term);
    else if (p.next == null) {
      p.next = new Node(term);
      return p;
    }
    else {
      p.next = insertHelper(term, p.next);
      return p;
    }
  }
  
  // check if the term is already existing in the table
  private boolean member(String term){
    Node p = lookup(term, T[hash(term)]);
    return p != null;                
  }
  
  // return the Node that has the term as the title
  private Node lookup(String term, Node p) {
    if (p == null)
      return null;
    else if (term.compareTo(p.term) == 0)
      return p;
    else 
      return lookup(term, p.next);
  }
  
  // return the cosine similarity of the terms for the two documents stored in this table
  public double cosineSimilarity(){
    int sumProduct = 0;
    int aSumSquare = 0;
    int bSumSquare = 0;
    
    for (int i = 0; i < T.length; i++) {
      sumProduct += sumProductHelper(T[i]);
    }
    
    for (int j = 0; j < T.length; j++) {
      aSumSquare += sumSquareHelper(T[j], 0);
      bSumSquare += sumSquareHelper(T[j], 1);
    }
    
    double sim = sumProduct / (Math.sqrt(aSumSquare) * Math.sqrt(bSumSquare));
    
    return sim;
  }
  
  // some calculating methods that help to get the cosine similarity
  private double sumProductHelper(Node p) {
    if (p == null)
      return 0.0;
    else
      return p.termFreq[0]*p.termFreq[1] + sumProductHelper(p.next);
  }
  
  private double sumSquareHelper(Node p, int docNum) {
    if (p == null)
      return 0.0;
    else 
      return p.termFreq[docNum]*p.termFreq[docNum] + sumSquareHelper(p.next, docNum);
  }
  
  
  
  // unit test
  public static void main(String[] args) {
    
    TermFrequencyTable T1 = new TermFrequencyTable();
    
    String aTest1 = "A B";
    String bTest1 = "A A B B";
    
    T1.initialize(aTest1, bTest1);
    
    System.out.println("\nTest[1]: compute the cosine similarity between \"A B\" and \"A A B B\", should be:");
    System.out.println("1.0");
    System.out.println(T1.cosineSimilarity());
    
    
    TermFrequencyTable T2 = new TermFrequencyTable();
    
    String aTest2 = "A B";
    String bTest2 = "C D";
    
    T2.initialize(aTest2, bTest2);
    
    System.out.println("\nTest[2]: compute the cosine similarity between \"A B\" and \"C D\", should be:");
    System.out.println("0.0");
    System.out.println(T2.cosineSimilarity());
    
    
    TermFrequencyTable T3 = new TermFrequencyTable();
    
    String aTest3 = "CS112 HW10";
    String bTest3 = "CS112 HW10 HW10";
    
    T3.initialize(aTest3, bTest3);
    
    System.out.println("\nTest[3]: compute the cosine similarity between \"CS112 HW10\" and \"CS112 HW10 HW10\", should be:");
    System.out.println("0.9487");
    System.out.println(T3.cosineSimilarity());
   
    
//    TermFrequencyTable T4 = new TermFrequencyTable();
//    
//    String aTest4 = "dogs";
//    String bTest4 = "cats";
//    
//    T4.initialize(aTest4, bTest4);
//    System.out.println(T4.cosineSimilarity());
  }
  
}