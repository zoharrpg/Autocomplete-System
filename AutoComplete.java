//TO-DO Add necessary imports
import java.util.*;



import java.io.*;


public class AutoComplete{

  //TO-DO: Add instance variable: you should have at least the tree root
  private DLBNode root;
 

  public AutoComplete(String dictFile) throws java.io.IOException {
    //TO-DO Initialize the instance variables  
    root = null;
    
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
    root=add(root,word,0);
  }

  private DLBNode add(DLBNode x,StringBuilder word,int pos)
  {
    DLBNode letter =x;
    if(letter==null)
    {
      
     letter=new DLBNode(word.charAt(pos),0);

      
      if(pos<word.length()-1)
      {
        letter.child=add(letter.child,word,pos+1);
      }
      else
      {
        letter.isWord=true;
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

    updatescore(root,word,0);

    


  }

  private void updatescore(DLBNode letter,StringBuilder word,int pos)
  {
    if(letter==null) return;

    if(letter.data==word.charAt(pos))
    {
      if(pos<word.length()-1)
      {
        updatescore(letter.child, word, pos+1);

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
      updatescore(letter.sibling,word,pos);
    }




  }
  
  //get the score of word
  public int getScore(StringBuilder word){
    //TO-DO Implement this method
    int score=getScore(root,word,0);


    return score;
  }

  private int getScore(DLBNode letter,StringBuilder word,int pos)
  {
    if(letter==null) return -1;

    int score;

    if(letter.data==word.charAt(pos))
    {
      if(pos<word.length()-1)
      {
          score=getScore(letter.child, word, pos+1);
      }
      else
      {
        score=letter.score;
        return score;
      }


      
    }
    else
    {
      score=getScore(letter.sibling,word,pos);
    
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
    ArrayList<Suggestion> record = new ArrayList<>();
    
    int length=word.length();
    getSuggestion(root,word,length,record,0);

    Collections.sort(record);
   
    Collections.reverse(record);
    
    return record;
  }

  private void getSuggestion(DLBNode letter,StringBuilder word,int length,ArrayList<Suggestion> record,int pos)
  {
    if(letter==null) return;

   
      if(pos<length-1)
      {
       if(letter.data==word.charAt(pos))
       {
        getSuggestion(letter.child,word,length,record,pos+1);
       }
       else
       {
         getSuggestion(letter.sibling,word,length,record,pos);
       }
       
        
      }
      else if(pos==length-1)
      {
        if(letter.data==word.charAt(pos))
        {
          if(letter.isWord)
          {
          
            record.add(new Suggestion(word,letter.score));
          
            getSuggestion(letter.child,word,length,record,pos+1);

          

           }
           else
           {
            getSuggestion(letter.child,word,length,record,pos+1);
           }


        }
        else
        {
          getSuggestion(letter.sibling,word,length,record,pos);

        }



      }
      else
      {
        word.append(letter.data);
        
        if(letter.isWord)
        {
         
          record.add(new Suggestion(word,letter.score));
          
          getSuggestion(letter.child,word,length, record, pos+1);
         
          
          word.deleteCharAt(pos);
         
          
          getSuggestion(letter.sibling,word,length,record,pos);
          
          
         
        }
        else
        {
          getSuggestion(letter.child,word,length,record,pos+1);
          
          word.deleteCharAt(pos);
         

          getSuggestion(letter.sibling,word,length,record,pos);

          



          

          

          
        }
        
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
      this.word = new StringBuilder(word);
      this.score=score;

    }
    public int compareTo(Suggestion w)
    {
      int score1=this.score;
      int score2=w.score;

      int order=score1-score2;

      if(order==0)
      {
        score1=this.word.length();
        score2=w.word.length();
        order=score2-score1;
        
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
