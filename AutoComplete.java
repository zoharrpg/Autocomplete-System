//TO-DO Add necessary imports
import java.util.*;



import java.io.*;


public class AutoComplete{

  //TO-DO: Add instance variable: you should have at least the tree root
  private DLBNode root;
 

  public AutoComplete(String dictFile) throws java.io.IOException {
    //TO-DO Initialize the instance variables  
    root = null;   // Initalize the variables
    
    Scanner fileScan = new Scanner(new FileInputStream(dictFile));
    while(fileScan.hasNextLine()){
      StringBuilder word = new StringBuilder(fileScan.nextLine());
                 this.add(word);//TO-DO call the public add method or the private helper method if you have one
    }
    fileScan.close();
  }

  /**
   * Part 1: add, increment score, and get score
   */

  //add word to the tree
  public void add(StringBuilder word){
    if (word == null) throw new IllegalArgumentException("calls put() with a null key");//TO-DO Implement this method
    
    root=add(root,word,0); //helper method
  }

  private DLBNode add(DLBNode x,StringBuilder word,int pos)
  {
    DLBNode letter =x;
    if(letter==null)
    {
      
     letter=new DLBNode(word.charAt(pos),0);      // add the character to the new node

      
      if(pos<word.length()-1)
      {
        letter.child=add(letter.child,word,pos+1);           // change to child
      }
      else
      {
        letter.isWord=true;                                // when node is a complete word---change isWord to false as sign  of completer word
      }
      
      
    }else if(letter.data==word.charAt(pos))
      {
        if(pos<word.length()-1)
        {
          letter.child=add(letter.child,word,pos+1);
        }
        else
        {
          letter.isWord=true;
        }
        
      }
      else
      {
        letter.sibling=add(letter.sibling,word,pos);
      }

      return letter;

      

  }

  //increment the score of word
  public void notifyWordSelected(StringBuilder word)
  {
    //TO-DO Implement this method

    updatescore(root,word,0);      //helper method

  }

  private void updatescore(DLBNode letter,StringBuilder word,int pos) // actuall increment the score of word
  {
    if(letter==null) return;     

    if(letter.data==word.charAt(pos))   // if nodelet == character
    {
      if(pos<word.length()-1)         
      {
        updatescore(letter.child, word, pos+1);   //recursive call on the child 

      }
      else
      { 
        if(letter.isWord)                       
        {
          letter.score+=1;  
        }
      }
    }
    else
    {
      updatescore(letter.sibling,word,pos);   // if the node is not equal character, change to sibling
    }




  }
  
  //get the score of word
  public int getScore(StringBuilder word){
    //TO-DO Implement this method
    int score=getScore(root,word,0);  //helper method


    return score;
  }

  private int getScore(DLBNode letter,StringBuilder word,int pos)
  {
    if(letter==null) return -1;   //if node is null or no complete word then return -1

    int score;

    if(letter.data==word.charAt(pos))
    {
      if(pos<word.length()-1)
      {
          score=getScore(letter.child, word, pos+1); // next child
      }
      else
      {
        score=letter.score;
        return score;
      }


      
    }
    else
    {
      score=getScore(letter.sibling,word,pos);   //check sibling
    
    }
    return score;

  }
 
 
  /**
   * Part 2: retrieve word suggestions in sorted order.
   */
  
  //retrieve a sorted list of autocomplete words for word. The list should be sorted in descending order based on score.
  public ArrayList<Suggestion> retrieveWords(StringBuilder word)
  {
    //TO-DO Implement this method
    if(word.length()==0) throw new IllegalArgumentException("Empty prefix! Please type one or more letters");// if user did not type words and  press enter, pop up exception.
    
    ArrayList<Suggestion> record = new ArrayList<>();    // potential complete word list 
    
    int length=word.length();
    
    getSuggestion(root,word,length,record,0);       //helper method for add potential complete words

    Collections.sort(record);                     // sort
   
    Collections.reverse(record);                 //descending 
    
    return record;
  }

  
  private void getSuggestion(DLBNode letter, StringBuilder word, int length, ArrayList<Suggestion> record, int pos) // use length  check the position of the character
  {
    if(letter==null) return; // if there is no node just return 

   
      if(pos<length-1)       
      {
       if(letter.data==word.charAt(pos))
       {
        getSuggestion(letter.child,word,length,record,pos+1); 
       }
       else
       {
         getSuggestion(letter.sibling,word,length,record,pos);       // get the position of prefix
       }
       
        
      }
      else if(pos==length-1)                          // if the prefix is a valid word
      {
        if(letter.data==word.charAt(pos))
        {
          if(letter.isWord)
          {
          
            record.add(new Suggestion(word,letter.score));   // add the word to list 
          
          }



           getSuggestion(letter.child,word,length,record,pos+1);    // check child 
          

        }
        else
        {
          getSuggestion(letter.sibling,word,length,record,pos);    // check sibling

        }



      }
      else    // get the complete words that have prefix 
      {
        word.append(letter.data);    // update words
        
        if(letter.isWord)          
        {
         
          record.add(new Suggestion(word,letter.score));        //add the words
        
        }


        getSuggestion(letter.child,word,length, record, pos+1);   // go over the child 
         
          
        word.deleteCharAt(pos);        // after recursive call,delete the character 
         
          
        getSuggestion(letter.sibling,word,length,record,pos);  // go over the sibling 
          

        
        
      }
      
    }


  /**
   * Helper methods for debugging.
   */

  //Print the subtree after the start string
  public void printTree(String start){
    System.out.println("==================== START: DLB Tree Starting from "+ start + " ====================");
    DLBNode startNode = getNode(root, start, 0);
    if(startNode != null){
      printTree(startNode.child, 0);
    }
    System.out.println("==================== END: DLB Tree Starting from "+ start + " ====================");
  }

  //A helper method for printing the tree
  private void printTree(DLBNode node, int depth){
    if(node != null){
      for(int i=0; i<depth; i++){
        System.out.print(" ");
      }
      System.out.print(node.data);
      if(node.isWord){
        System.out.print(" *");
      }
        System.out.println(" (" + node.score + ")");
      printTree(node.child, depth+1);
      printTree(node.sibling, depth);
    }
  }

  //return a pointer to the node at the end of the start string. Called from printTree.
  private DLBNode getNode(DLBNode node, String start, int index){
    DLBNode result = node;
    if(node != null){
      if((index < start.length()-1) && (node.data.equals(start.charAt(index)))) {
          result = getNode(node.child, start, index+1);
      } else if((index == start.length()-1) && (node.data.equals(start.charAt(index)))) {
          result = node;
      } else {
          result = getNode(node.sibling, start, index);
      }
    }
    return result;
  }


  //A helper class to hold suggestions. Each suggestion is a (word, score) pair. 
  //This class should be Comparable to itself.
  public class Suggestion implements Comparable<Suggestion> /*.....*/ 
  {
    //TO-DO Fill in the fields and methods for this class. Make sure to have them public as they will be accessed from the test program A2Test.java.
    public StringBuilder word;   
    public int score;
    
    public Suggestion(StringBuilder word,int score)  
    {
      this.word = new StringBuilder(word); // create new word
      this.score=score;

    }
    public int compareTo(Suggestion w)  //
    {
      
      int score1=this.score;
      int score2=w.score;

      int order=score1-score2;  // put higher score in the first

      if(order==0)          // if scores are eaual, put small word in the first
      {
        
       
        order=w.word.toString().compareTo(this.word.toString());
        
      }

      return order;

    }
  }

  //The node class.
  private class DLBNode{
    private Character data;
    private int score;
    private boolean isWord;
    private DLBNode sibling;
    private DLBNode child;

    private DLBNode(Character data, int score){
        this.data = data;
        this.score = score;
        isWord = false;
        sibling = child = null;
    }
  }
}
