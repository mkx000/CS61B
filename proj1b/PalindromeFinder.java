/**
 * This class outputs all palindromes in the words file in the current directory.
 */
public class PalindromeFinder {

    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-sp18/data/words.txt");
        Palindrome palindrome = new Palindrome();
        OffByOne off = new OffByOne();
        OffByN off2 = new OffByN(25);

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength) {
                if (palindrome.isPalindrome(word, off2)) {
                    System.out.println(word);
                    palindrome.isPalindrome(word, off2);
                }
            }
        }
    }// Uncomment this class once you've written isPalindrome.
}
