# Autocomplete-System

  The problem is trying to find all potential complete words based on the prefix. Also, the order of potential complete words also needs to base on whether type habits, which means the most commonly used word that users type will be shown in the first place.
To solve the problem, I need to decide what data structure is the best for finding the prefix. Obviously, the best option for finding words based on the prefix is de la Briandais Tries, which is very easy to find complete based on the prefix. To find the potential complete word, I can scan the dictionary by using de la Briandais Tries, which makes the dictionary become the de la Briandais Tries format. When the user types the word or the prefix, use the search method to find the position of the word and go through the DLB node to get all the complete words.

  The first part of the problem is very easy, the main problem is just the add method to create de la Briandais Tries. It is very easy to write and think of the recursive method in the tries data structure. When I first wrote the code, I tried to write all situations for DLB. The first condition is when the node is null and creates a new node. The second condition is that if there is a node and the character in the node is corresponding to the position of the word, then go to the child, otherwise go to the sibling. The third condition is for all other conditions, just go to the sibling to find another character. In each of the conditions, check whether the position is at the end of the word. If the position is at the end of the word, mark it as a completer by changing the isWord value in the node.
For the increment of the word, it is the almost same logic. I wrote a helper method for the increment of the score. I separated the condition. When the node is null, then return. When the character in the node equals the position of the word, recursive call to the child. If it is not, recursive to the sibling. When the character is a complete word, make the score increase.
For the second part of the code, the most difficult part is retrieveword method. To complete the method, I wrote the Suggestion Object first. In the Suggestion Object the most tricky part is the compare to method. At first, I just compare the score. When I test the code, I realize I should also make the order of equal score words.

  In the retrieWords method, the most difficult part is to think about all the conditions of the position of word and prefix that users may input by using recursive call. The basic logic is to try to get the position of the prefix in the DLB tries. When the position is less than the length of the word, make the recursive call on child and sibling to get the right position. When the position is at the end of the word, check whether it is a complete word. If it is a complete word, then add to array-list, and then go to the next child. 
For all other conditions, if the position is greater than the length of the word. The word appends the character of the node, and check whether is complete. If it is a complete word, then add the complete to the array-list, then go to the next child. After going to the child, delete the character in the current position, then go to the sibling to check every character in the DLB after the position.
Finally, when I completed the first version of the program, I tried to use a small dictionary to check the methods of the program. To check whether the methods include all the conditions, I tried to different values and type different words to find the condition. I also use the Print tree method to check whether my DLB tries are ok. In addition, I use JDB to check every step of the code. At first time, I did not consider a condition, which makes the find word method always cannot find the correct word. I print tree and JDB find the potential problem and condition in the method, make condition perfect.

# Asymptotic analysis

  Number of words in dictionary :N
  
  Number of characters in a word or prefix: W
  
  The alphabet size:R
  
  Number of matching word suggestions:K

worst runtime of insertion = Theta(WR)

worst runtime of score incrementing = Theta(WR)

worst runtime of score retrieval = Theta(WR)

worst runtime of suggestions retrieval = Theta(KWR)


