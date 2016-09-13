/* File: ArticleTable.java
 * Author: Chi Zhang (chizhang@bu.edu)
 * Date: 4/29/2015
 * Description: This is a program that stores article in a hash table using the
 * The technique of seperate chaining.
 */

public class ArticleTable {
  
  private final int SIZE = 2503;
  private Node [] T = new Node[SIZE];
  
  // master list
  private Node head = null; 
  private Node n = null;
  
  // node class
  public static class Node {
    public Article data;
    public String key;
    public Node next;
    public Node next2;
    
    // constructor
    public Node(Article a, Node n) {
      this.key = a.getTitle();
      this.data = a;
      this.next = null;
      this.next2 = n;
    }
    
    public Node(Article a) {
      this.key = a.getTitle();
      this.data = a;
      this.next = null;
      this.next2 = null;
    }
  }
  
  // print out a bucket for testing purpose
  public void printBucket(Article a) {
    System.out.println("[" + toStringBucket(T[hash(a.getTitle())]) + "]");
  }
  
  public void printBucket(String key) {
    System.out.println("[" + toStringBucket(T[hash(key)]) +"]");
  }
  
  private String toStringBucket(Node p) {
    if (p == null)
      return "";
    else if (p.next == null)
      return p.key;
    else
      return p.key + ", " + toStringBucket(p.next);
  }
  
  // print method for the iteration for testing purpose
  public void printIteration(Article a){
    System.out.println(a.getTitle() + "=" + a.getBody());
  }
  
  // print out the master list for testing purpose
  public void printMasterList(){
    System.out.println("[" + toStringMasterList(head) + "]");
  }
  
  private String toStringMasterList(Node p) {
    if (p == null)
      return "";
    else if (p.next2 == null)
      return p.key;
    else {
      return p.key + ", " + toStringMasterList(p.next2);
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
  
  // insert Article a into the table using its title as the hash key
  public void insert(Article a) {
    if (member(a) == false) {
      String t = a.getTitle();
      T[hash(t)] = insertHelper(a, T[hash(t)]);
      head = new Node(a, head);
    }
  }
  
  private Node insertHelper(Article a, Node p) {
    if (p == null)
      return new Node(a);
    else if (p.next == null) {
      p.next = new Node(a);
      return p;
    }
    else {
      p.next = insertHelper(a, p.next);
      return p;
    }
  }
  
  // delete method
  public void delete(String title) {
    Article a = lookup(title);
    if (a != null) {
      T[hash(title)] = deleteHelper(title, T[hash(title)]);
      head = deleteML(title, head);
    }
  }
  
  private Node deleteHelper(String title, Node p){
    if (p == null)
      return p;
    else if (title.compareTo(p.data.getTitle()) == 0) {
      return p.next;
    }
    else {
      p.next = deleteHelper(title, p.next);
      return p;
    }
  }
  
  // delete the node from the master list
  private Node deleteML(String title, Node p) {
    if( p == null)
      return p;
    else if (title.compareTo(p.key) ==0)
      return p.next2;
    else {
      p.next2 = deleteML(title, p.next2);
      return p;
    }
  }
  
  // return the article with the given title or null if not found
  public Article lookup(String title) {
    Node n = lookup(title, T[hash(title)]);
    if( n != null)
      return n.data;
    return null;
  }
  
  // lookup an article from a list of nodes with specified title
  private Node lookup(String title, Node p) {
    if (p == null)
      return null;
    else if (title.compareTo(p.key) == 0)
      return p;
    else 
      return lookup(title, p.next);
  }
  
  // lookup an article from a list of nodes
  public  Node lookup(Article a, Node t) {
    if (t == null)
      return null;
    else if (a.getTitle().compareTo(t.key) == 0) {
      return t;
    } else 
      return lookup(a, t.next);
  }
  
  // initializer: copy articles from the array A
  public void initialize(Article[] A) {
    for (int i = 0; i < A.length; i++) {
      insert(A[i]);
    }
  }
  
  // Iterator methods
  // Initialize the interator
  public void reset() {
    n = head; // direct n to the head
  }
  
  // return true if n is not null
  public boolean hasNext() {
    return (n != null);
  }
  
  // return node n
  public Article next() {
    Article a = n.data;
    n = n.next2; // update the node
    return a;
  }
  
  // check if Article a is in the master list
  public boolean member(Article a) {
    return (lookup(a, T[hash(a.getTitle())]) != null); 
  }
  
  // check if an article with that title is in the master list
  public boolean member(String title) {
    return (lookup(title, T[hash(title)]) != null); 
  }
    
  public int length() {
    return length(head); 
  }
  
  public int length(Node t) {
    if(t == null)
      return 0;
    else
      return 1 + length(t.next2); 
  }
  
  // unit test
  public static void main(String[] args) {
    ArticleTable L = new ArticleTable(); 
    
    Article a = new Article("a", "apple");
    Article b = new Article("b", "banana");
    Article c = new Article("c", "car");
    Article d = new Article("d", "dinner");
    Article e = new Article("e", "egg");
    Article f = new Article("f", "fruit");
    Article g = new Article("g", "glue");
    Article h = new Article("h", "hello");
    
    Article A[] = {a, b, c, d, e, f, g, h};
    
    L.initialize(A);
    
    //L.printMasterList();
  
    System.out.println("Test begin...");
    
    // Test the master list
    System.out.println("\nTest[1]: Printing the master list, should be:");
    System.out.println("[h, g, f, e, d, c, b, a]");
    L.printMasterList();
    
    System.out.println("\nTest[2]: Testing length, should be:");
    System.out.println("8");
    System.out.println(L.length());
   
    System.out.println("\nTest[3]: Testing member() method, should be:");
    System.out.println("true");
    System.out.println(L.member("b"));
    
    System.out.println("\nTest[4]: Testing member() method, should be:");
    System.out.println("false");
    System.out.println(L.member("v"));
    
    System.out.println("\nTest[5]: Testing member() method, should be:");
    System.out.println("true");
    Article test1 = new Article("a", "apple");
    System.out.println(L.member(test1));
    
    System.out.println("\nTest[6]: Testing member() method, should be:");
    System.out.println("false");
    Article test2 = new Article("l", "apple");
    System.out.println(L.member(test2));
    
    // Test insert
    System.out.println("\nTest[7]: Testing insert, should be:");
    System.out.println("[z, h, g, f, e, d, c, b, a]");
    Article z = new Article("z", "zebra");
    L.insert(z);
    L.printMasterList();
    
    
    Article abc = new Article("abc", "this is abc");
    Article acb = new Article("acb", "this is acb");
    Article bac = new Article("bac", "this is bac");
    Article bca = new Article("bca", "this is bca");
    Article cab = new Article("cab", "this is cab");
    Article cba = new Article("cba", "this is cba");
    
    Article B[] = {abc, acb, bac, bca, cab, cba};
    for (int i = 0; i < B.length; i++) {
      L.insert(B[i]);
    }
    
    System.out.println("\nTest[8]: Testing insert, should be:");
    System.out.println("[abc, acb, bac, bca, cab, cba] ");
    L.printBucket(abc);
    
    // Test delete
    System.out.println("\nTest[9]: Testing delete, should be:");
    System.out.println("[abc, acb, bca, cab, cba]");
    L.delete("bac");
    L.printBucket(abc);
    
    System.out.println("\nTest[10]: Testing delete using the master list, should be:");
    System.out.println("[cba, cab, bca, acb, abc, z, h, g, f, e, d, b, a]");
    L.delete("c");
    L.printMasterList();
    
    // Test look up
    System.out.println("\nTest[11]: Testing lookup() method, should be:");
    System.out.println("b\n=\nbanana");
    Article result1 = L.lookup("b");
    System.out.println(result1);
    
    System.out.println("\nTest[12]: Testing lookup() method, should be:");
    System.out.println("null");
    Article result2 = L.lookup("haha");
    System.out.println(result2);
    
    // Test iteratioin
    System.out.println("\nTest[13]: Testing lookup() method, should be:");
    System.out.println("cba=this is cba\ncab=this is cab\nbca=this is bca\nacb=this is acb\nabc=this is abc");
    System.out.println("z=zebra\nh=hello\ng=glue\nf=fruit\ne=egg\nd=dinner\nb=banana\na=apple ");
    System.out.println();
    
    L.reset();
    while(L.hasNext()) {
      Article m = L.next(); 
      L.printIteration(m);
    }
  }
}
