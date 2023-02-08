package byog.lab5;

import org.junit.Test;

import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
//    private static final int HEIGHT = 80;
//    private static final int WIDTH = 100;
//
//    private static final int SEED = 100;
//
//    private static
//    private static Random RANDOME = new Random(SEED);
//
//    private class Position {
//        private int x = 0;
//        private int y = 0;
//
//        public Position(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//    }
//
//    private static TETile randomTetile() {
//        int telNum = RANDOME.nextInt(5);
//        switch (telNum) {
//            case 0:
//                return Tileset.FLOOR;
//            case 1:
//                return Tileset.TREE;
//            case 2:
//                return Tileset.MOUNTAIN;
//            case 3:
//                return Tileset.WATER;
//            case 4:
//                return Tileset.GRASS;
//            default:
//                return Tileset.SAND;
//        }
//    }
//
//    private void fillHexagon(TETile[][] world, Position p, int size) {
//        TETile tet = randomTetile();
//        int rows = 2 * size;
//        for (int i = p.x; i < p.x + rows; i++) {
//            int start, num, index;
//            index = i - p.x;
//            if (index < size) {
//                start = size - index - 1 + p.y;
//                num = size + 2 * index;
//                for (int j = start; j < start + num; j++) {
//                    world[i][j] = tet;
//                }
//            } else {
//                start = p.y + index - size;
//                num = 2 * size - 2 * (index - size);
//                for (int j = start; j < start + num; j++) {
//                    world[i][j] = tet;
//                }
//            }
//        }
//    }
//
//    private ArrayList<Position> calcCenter(Position p, int size) {
//        ArrayList<Position> res = new ArrayList<>();
//        res.addAll(calcHelper(p, size));
//        int step = 2 * size;
//        res.addAll(calcCenter(new Position(p.x + step, p.y + step), size))
//    }
//
//    private ArrayList<Position> calcHelper(Position p, int size) {
//        int step = 2 * size;
//        ArrayList<Position> res = new ArrayList<>();
//        res.add(p);
//        if (size % 2 == 1) {
//            for (int i = 1; i < size / 2 + 1; i++) {
//                res.add(new Position(p.x, p.y + step * i));
//                res.add(new Position(p.x, p.y - step * i));
//            }
//        } else {
//            res.add(new Position(p.x, p.y + step));
//            res.add(new Position(p.x, p.y - step));
//            res.add(new Position(p.x, p.y - 2 * step));
//        }
//        return res;
//    }
//
//    public void addHexagon(TETile[][] world, Position p, int size) {
//        for (int i = 0; i < WIDTH; i++) {
//            for (int j = 0; j < WIDTH; j++) {
//                world[i][j] = Tileset.NOTHING;
//            }
//        }
//
//
//    }


    public static void main(String[] args) {

    }

}
