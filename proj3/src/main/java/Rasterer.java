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
    public Result res;
    public double[] LonDPP;

    private class Result {
        public String[][] render_grid;
        public double raster_ul_lon;
        public double raster_ul_lat;
        public double raster_lr_lon;
        public double raster_lr_lat;
        public int depth;
        public boolean query_success;

        public Result(String[][] r_g, double ul_lo, double ul_la, double lr_lo, double lr_la, boolean q_s) {
            render_grid = r_g;
            raster_lr_lat = lr_la;
            raster_lr_lon = lr_lo;
            raster_ul_lat = ul_la;
            raster_ul_lon = ul_lo;
            query_success = q_s;
        }

        public Result() {
        }
    }

    public Rasterer() {
        res = new Result();
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
//        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
//        System.out.println("---------------Rasterer-----------------");

        if (paramValid(params)) {
            double queryLonDPP = calcLonDPP(params.get("ullon"), params.get("lrlon"), params.get("w"));
            int index = 0;
            while (LonDPP[index] > queryLonDPP) {
                index++;
                if (index == MAX_DEPTH) {
                    index = MAX_DEPTH - 1;
                    break;
                }
            }
            int xPos = calcXpos(params.get("ullon"), index), xxPos = calcXpos(params.get("lrlon"), index);
            int yPos = calcYpos(params.get("ullat"), index), yyPos = calcYpos(params.get("lrlat"), index);
            String[][] render_grid = new String[xxPos - xPos + 1][yyPos - yPos + 1];
            for (int i = 0; i < render_grid.length; i++) {
                for (int j = 0; j < render_grid[0].length; j++) {
                    render_grid[i][j] = generateFileName(index, xPos + i, yPos + j);
                }
            }
            res.query_success = true;
            res.depth = index;
            double longitudePerImage = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, index);
            double latitudePerImage = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, index);
            res.raster_ul_lon = xPos * longitudePerImage;
            res.raster_lr_lon = xxPos * longitudePerImage;
            res.raster_ul_lat = yPos * latitudePerImage;
            res.raster_lr_lat = yyPos * latitudePerImage;
            res.render_grid = render_grid;
            results.put("query_success", res.query_success);
            results.put("depth", res.depth);
            results.put("render_grid", res.render_grid);
            results.put("raster_ul_lon", res.raster_ul_lon);
            results.put("raster_ul_lat", res.raster_ul_lat);
            results.put("raster_lr_lon", res.raster_lr_lon);
            results.put("raster_lr_lat", res.raster_lr_lat);
        } else {
            results.put("query_success", false);
        }
        return results;
    }

    private String generateFileName(int depth, int x, int y) {
        String res = "d" + depth + "_x" + x + "_y" + y + ".png";
        return res;
    }

    private int calcXpos(double longitude, int depth) {
        double longitudeDis = longitude - MapServer.ROOT_ULLON;
        double longitudePerImage = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        int xPos = (int) (longitudeDis / longitudePerImage);
        return xPos;
    }

    private int calcYpos(double latitude, int depth) {
        double latitudeDis = MapServer.ROOT_ULLAT - latitude;
        double latitudePerImage = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int yPos = (int) (latitudeDis / latitudePerImage);
        return yPos;
    }


    private double calcLonDPP(double a, double b, double w) {
        return Math.abs(a - b) / w;
    }

    private boolean upperLowerRight(Map<String, Double> params) {
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        if (ullon >= lrlon || ullat <= lrlat) {
            return false;
        }
        return true;
    }

    //judge whether query box is completely out of the world or not

    private boolean completelyOut(Map<String, Double> params) {
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        boolean flag = upperLowerRight(params);
        if (!flag) {
            return false;
        }
        if (lrlon < MapServer.ROOT_ULLON || ullon > MapServer.ROOT_LRLON) {
            return false;
        }
        if (lrlat > MapServer.ROOT_ULLAT || ullat < MapServer.ROOT_LRLAT) {
            return false;
        }
        return true;
    }

    private boolean coorInBoudnsHelper(Double d, double left, double right) {
        if (d < left) {
            d = left + Math.abs(left) * edgeFactor;
            return false;
        } else if (d > right) {
            d = right - Math.abs(right) * edgeFactor;
            return false;
        }
        return true;
    }

    private boolean coorInBounds(Map<String, Double> params) {
        boolean ulflag = true, lrflag = true;
        Double ullon = params.get("ullon");
        Double ullat = params.get("ullat");
        Double lrlon = params.get("lrlon");
        Double lrlat = params.get("lrlat");
        ulflag = coorInBoudnsHelper(ullat, MapServer.ROOT_LRLAT, MapServer.ROOT_ULLAT) && coorInBoudnsHelper(ullon, MapServer.ROOT_ULLON, MapServer.ROOT_LRLON);
        lrflag = coorInBoudnsHelper(lrlat, MapServer.ROOT_LRLAT, MapServer.ROOT_ULLAT) && coorInBoudnsHelper(lrlon, MapServer.ROOT_ULLON, MapServer.ROOT_LRLON);
        if (!ulflag && !lrflag) {
            return false;
        }
        params.put("ullon", ullon);
        params.put("ullat", ullat);
        params.put("lrlon", lrlon);
        params.put("lrlat", lrlat);
        return true;
    }


    private boolean paramValid(Map<String, Double> params) {
        if (!upperLowerRight(params) || !coorInBounds(params)) {
            return false;
        }
        return true;
    }
}
