package com.example.lightningalertapi.util;

public class MapPoint {
    public static class TileSystem{
        private static double EarthRadius = 6378137;
        private static double MinLatitude = -85.05112878;
        private static double MaxLatitude = 85.05112878;
        private static double MinLongitude = -180;
        private static double MaxLongitude = 180;

        /// <summary>
        /// Clips a number to the specified minimum and maximum values.
        /// </summary>
        /// <param name="n">The number to clip.</param>
        /// <param name="minValue">Minimum allowable value.</param>
        /// <param name="maxValue">Maximum allowable value.</param>
        /// <returns>The clipped value.</returns>
        private static double clip(double n, double minValue, double maxValue)
        {
            return Math.min(Math.max(n, minValue), maxValue);
        }

        /// <summary>
        /// Determines the map width and height (in pixels) at a specified level
        /// of detail.
        /// </summary>
        /// <param name="levelOfDetail">Level of detail, from 1 (lowest detail)
        /// to 23 (highest detail).</param>
        /// <returns>The map width and height in pixels.</returns>
        public static long mapSize(int levelOfDetail)
        {
            return (long) 256 << levelOfDetail;
        }

        /// <summary>
        /// Determines the ground resolution (in meters per pixel) at a specified
        /// latitude and level of detail.
        /// </summary>
        /// <param name="latitude">Latitude (in degrees) at which to measure the
        /// ground resolution.</param>
        /// <param name="levelOfDetail">Level of detail, from 1 (lowest detail)
        /// to 23 (highest detail).</param>
        /// <returns>The ground resolution, in meters per pixel.</returns>
        public static double groundResolution(double latitude, int levelOfDetail)
        {
            latitude = clip(latitude, MinLatitude, MaxLatitude);
            return Math.cos(latitude * Math.PI / 180) * 2 * Math.PI * EarthRadius / mapSize(levelOfDetail);
        }
        /// <summary>
        /// Determines the map scale at a specified latitude, level of detail,
        /// and screen resolution.
        /// </summary>
        /// <param name="latitude">Latitude (in degrees) at which to measure the
        /// map scale.</param>
        /// <param name="levelOfDetail">Level of detail, from 1 (lowest detail)
        /// to 23 (highest detail).</param>
        /// <param name="screenDpi">Resolution of the screen, in dots per inch.</param>
        /// <returns>The map scale, expressed as the denominator N of the ratio 1 : N.</returns>
        public static double mapScale(double latitude, int levelOfDetail, int screenDpi)
        {
            return groundResolution(latitude, levelOfDetail) * screenDpi / 0.0254;
        }

        /// <summary>
        /// Converts a point from latitude/longitude WGS-84 coordinates (in degrees)
        /// into pixel XY coordinates at a specified level of detail.
        /// </summary>
        /// <param name="latitude">Latitude of the point, in degrees.</param>
        /// <param name="longitude">Longitude of the point, in degrees.</param>
        /// <param name="levelOfDetail">Level of detail, from 1 (lowest detail)
        /// to 23 (highest detail).</param>
        /// <param name="pixelX">Output parameter receiving the X coordinate in pixels.</param>
        /// <param name="pixelY">Output parameter receiving the Y coordinate in pixels.</param>
        public static int latToPixelY(double latitude, int levelOfDetail)
        {
            latitude = clip(latitude, MinLatitude, MaxLatitude);

            double sinLatitude = Math.sin(latitude * Math.PI / 180);
            double y = 0.5 - Math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

            long mapSize = mapSize(levelOfDetail);
            return (int) clip(y * mapSize + 0.5, 0, mapSize - 1);
        }

        public static int longToPixelX(double longitude, int levelOfDetail)
        {
            longitude = clip(longitude, MinLongitude, MaxLongitude);

            double x = (longitude + 180) / 360;

            long mapSize = mapSize(levelOfDetail);
            return (int) clip(x * mapSize + 0.5, 0, mapSize - 1);
        }

        /// <summary>
        /// Converts a pixel from pixel XY coordinates at a specified level of detail
        /// into latitude/longitude WGS-84 coordinates (in degrees).
        /// </summary>
        /// <param name="pixelX">X coordinate of the point, in pixels.</param>
        /// <param name="pixelY">Y coordinates of the point, in pixels.</param>
        /// <param name="levelOfDetail">Level of detail, from 1 (lowest detail)
        /// to 23 (highest detail).</param>
        /// <param name="latitude">Output parameter receiving the latitude in degrees.</param>
        /// <param name="longitude">Output parameter receiving the longitude in degrees.</param>
//        public static void pixelXYToLatLong(int pixelX, int pixelY, int levelOfDetail, double latitude, double longitude)
//        {
//            double mapSize = mapSize(levelOfDetail);
//            double x = (clip(pixelX, 0, mapSize - 1) / mapSize) - 0.5;
//            double y = 0.5 - (clip(pixelY, 0, mapSize - 1) / mapSize);
//
//            latitude = 90 - 360 * Math.atan(Math.exp(-y * 2 * Math.PI)) / Math.PI;
//            longitude = 360 * x;
//        }

        /// <summary>
        /// Converts pixel XY coordinates into tile XY coordinates of the tile containing
        /// the specified pixel.
        /// </summary>
        /// <param name="pixelX">Pixel X coordinate.</param>
        /// <param name="pixelY">Pixel Y coordinate.</param>
        /// <param name="tileX">Output parameter receiving the tile X coordinate.</param>
        /// <param name="tileY">Output parameter receiving the tile Y coordinate.</param>
        public static int pixelToTile(int pixel)
        {
            return pixel / 256;
        }

        /// <summary>
        /// Converts tile XY coordinates into pixel XY coordinates of the upper-left pixel
        /// of the specified tile.
        /// </summary>
        /// <param name="tileX">Tile X coordinate.</param>
        /// <param name="tileY">Tile Y coordinate.</param>
        /// <param name="pixelX">Output parameter receiving the pixel X coordinate.</param>
        /// <param name="pixelY">Output parameter receiving the pixel Y coordinate.</param>
        public static int tileToPixel(int tile)
        {
            return tile * 256;
        }

        /// <summary>
        /// Converts tile XY coordinates into a QuadKey at a specified level of detail.
        /// </summary>
        /// <param name="tileX">Tile X coordinate.</param>
        /// <param name="tileY">Tile Y coordinate.</param>
        /// <param name="levelOfDetail">Level of detail, from 1 (lowest detail)
        /// to 23 (highest detail).</param>
        /// <returns>A string containing the QuadKey.</returns>
        public static String tileXYToQuadKey(int tileX, int tileY, int levelOfDetail)
        {
            StringBuilder quadKey = new StringBuilder();
            for (int i = levelOfDetail; i > 0; i--)
            {
                char digit = '0';
                int mask = 1 << (i - 1);
                if ((tileX & mask) != 0)
                {
                    digit++;
                }
                if ((tileY & mask) != 0)
                {
                    digit++;
                    digit++;
                }
                quadKey.append(digit);
            }
            return quadKey.toString();
        }

        public static String getQuadKey(double latitude, double longitude,int levelOfDetail) {
            int pixelY = latToPixelY(latitude, levelOfDetail);
            int pixelX = longToPixelX(longitude, levelOfDetail);
            int tileX = pixelToTile(pixelX);
            int tileY = pixelToTile(pixelY);
            String quadKey = tileXYToQuadKey(tileX, tileY, levelOfDetail);
            return quadKey;
        }
        /// <summary>
        /// Converts a QuadKey into tile XY coordinates.
        /// </summary>
        /// <param name="quadKey">QuadKey of the tile.</param>
        /// <param name="tileX">Output parameter receiving the tile X coordinate.</param>
        /// <param name="tileY">Output parameter receiving the tile Y coordinate.</param>
        /// <param name="levelOfDetail">Output parameter receiving the level of detail.</param>
//        public static void quadKeyToTileXY(char[] quadKey, int tileX, int tileY, int levelOfDetail)
//        {
//            tileX = tileY = 0;
//            levelOfDetail = quadKey.length;
//            for (int i = levelOfDetail; i > 0; i--)
//            {
//                int mask = 1 << (i - 1);
//                switch (quadKey[levelOfDetail - i])
//                {
//                    case '0':
//                        break;
//
//                    case '1':
//                        tileX |= mask;
//                        break;
//
//                    case '2':
//                        tileY |= mask;
//                        break;
//
//                    case '3':
//                        tileX |= mask;
//                        tileY |= mask;
//                        break;
//
//                    default:
//                        System.out.println("Invalid QuadKey digit sequence.");
//                }
//            }
//        }
    }
}