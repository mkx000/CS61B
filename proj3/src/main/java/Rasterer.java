import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private final static int MAX_DEPTH = 8;
    private final static double edgeFactor = 1E-10;
    private final static double feetPerLongitude = 288200;
    private double[] LonDPP;
    private double ullon;
    private double ullat;
    private double lrlon;
    private double lrlat;
    private double width, height;

    public boolean query_success;
    public int depth;
    public String[][] render_grid;
    public double raster_ul_lon;
    public double raster_ul_lat;
    public double raster_lr_lon;
    public double raster_lr_lat;

    public Rasterer() {
        LonDPP = new double[MAX_DEPTH];
        double lpp = calcLonDPP(MapServer.ROOT_LRLON, MapServer.ROOT_ULLON, 256);
        for (int i = 0; i < MAX_DEPTH; i++) {
            LonDPP[i] = lpp;
            lpp /= 2;
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
//        System.out.println("---------------Rasterer-----------------");
//        System.out.println(params);
        ullon = params.get("ullon");
        ullat = params.get("ullat");
        lrlon = params.get("lrlon");
        lrlat = params.get("lrlat");
        width = params.get("w");
        height = params.get("h");
        Map<String, Object> results = new HashMap<>();
        if (!completelyOut()) {
            coorInBounds(params);
            double queryLonDPP = calcLonDPP(params.get("ullon"), params.get("lrlon"), width);
            depth = 0;
            while (LonDPP[depth] > queryLonDPP) {
                depth++;
                if (depth == MAX_DEPTH) {
                    depth = MAX_DEPTH - 1;
                    break;
                }
            }
            int ulRow = calcLatPos(ullat, depth), lrRow = calcLatPos(lrlat, depth);
            int ulCol = calcLonPos(ullon, depth), lrCol = calcLonPos(lrlon, depth);
            render_grid = new String[lrRow - ulRow + 1][lrCol - ulCol + 1];
            for (int i = 0; i < render_grid.length; i++) {
                for (int j = 0; j < render_grid[0].length; j++) {
                    render_grid[i][j] = generateFileName(j + ulCol, i + ulRow);
                }
            }
            query_success = true;
            double longitudePerImage = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
            double latitudePerImage = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
            raster_ul_lon = ulCol * longitudePerImage + MapServer.ROOT_ULLON;
            raster_lr_lon = (lrCol + 1) * longitudePerImage + MapServer.ROOT_ULLON;
            raster_ul_lat = -ulRow * latitudePerImage + MapServer.ROOT_ULLAT;
            raster_lr_lat = -(lrRow + 1) * latitudePerImage + MapServer.ROOT_ULLAT;
            results.put("query_success", query_success);
            results.put("depth", depth);
            results.put("render_grid", render_grid);
            results.put("raster_ul_lon", raster_ul_lon);
            results.put("raster_ul_lat", raster_ul_lat);
            results.put("raster_lr_lon", raster_lr_lon);
            results.put("raster_lr_lat", raster_lr_lat);
        } else {
            results.put("query_success", false);
        }
        return results;
    }

    private String generateFileName(int x, int y) {
        String res = "d" + depth + "_x" + x + "_y" + y + ".png";
        return res;
    }

    private int calcLonPos(double longitude, int depth) {
        double longitudeDis = longitude - MapServer.ROOT_ULLON;
        double longitudePerImage = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        int Pos = (int) (longitudeDis / longitudePerImage);
        return Pos;
    }

    private int calcLatPos(double latitude, int depth) {
        double latitudeDis = MapServer.ROOT_ULLAT - latitude;
        double latitudePerImage = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int Pos = (int) (latitudeDis / latitudePerImage);
        return Pos;
    }

    private double calcLonDPP(double a, double b, double w) {
        return Math.abs(a - b) / w;
    }

    private boolean upperLowerRight() {
        if (ullon >= lrlon || ullat <= lrlat) {
            return false;
        }
        return true;
    }

    //judge whether query box is completely out of the world or not
    private boolean completelyOut() {
        boolean flag = upperLowerRight();
        if (!flag) {
            return true;
        }
        if (lrlon < MapServer.ROOT_ULLON || ullon > MapServer.ROOT_LRLON) {
            return true;
        }
        if (lrlat > MapServer.ROOT_ULLAT || ullat < MapServer.ROOT_LRLAT) {
            return true;
        }
        return false;
    }

    private double coorInBoudnsHelper(double d, double left, double right) {
        if (d < left) {
            d = left + Math.abs(left) * edgeFactor;
        } else if (d > right) {
            d = right - Math.abs(right) * edgeFactor;
        }
        return d;
    }

    private void coorInBounds(Map<String, Double> params) {
        ullat = coorInBoudnsHelper(ullat, MapServer.ROOT_LRLAT, MapServer.ROOT_ULLAT);
        ullon = coorInBoudnsHelper(ullon, MapServer.ROOT_ULLON, MapServer.ROOT_LRLON);
        lrlat = coorInBoudnsHelper(lrlat, MapServer.ROOT_LRLAT, MapServer.ROOT_ULLAT);
        lrlon = coorInBoudnsHelper(lrlon, MapServer.ROOT_ULLON, MapServer.ROOT_LRLON);
    }
}
