public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> res = new LinkedListDeque<Character>();
        for (int i = 0; i < word.length(); i++){
            Character item = new Character(word.charAt(i));
            res.addLast(item);
        }
        return res;
    }


    public boolean isPalindrome(String word){
        Deque<Character> dq = wordToDeque(word);

        while (!dq.isEmpty()){
            if (dq.size() == 1){
                return true;
            }
            Character first, last;
            first = dq.removeFirst();
            last = dq.removeLast();
            if (!first.equals(last)){
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc){
        Deque<Character> dq = wordToDeque(word);

        while (!dq.isEmpty()){
            if (dq.size() == 1){
                return true;
            }

            char first = dq.removeFirst();
            char last = dq.removeLast();
            if (!cc.equalChars(first,last)){
                return false;
            }
        }

        return true;
    }
}
