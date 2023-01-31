public class OffByN implements CharacterComparator {

    private int offN;


    public OffByN(int N) {
        this.offN = N;
    }

    public boolean equalChars(char x, char y) {
        if (x == y + this.offN || y == x + this.offN) {
            return true;
        }
        return false;
    }
}
