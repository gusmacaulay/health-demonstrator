package org.mccaughey.health.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;

public class GeoJSONUtility {
  static final Logger LOGGER = LoggerFactory.getLogger(GeoJSONUtility.class);

  public static void writeFeatures(SimpleFeatureCollection features,
      OutputStream outStream) {
    FeatureJSON fjson = new FeatureJSON();

    try {

      try {
        if (features.getSchema().getCoordinateReferenceSystem() != null) {
          LOGGER.info("Encoding CRS?");
          fjson.setEncodeFeatureCollectionBounds(true);
         // fjson.setEncodeFeatureCollectionCRS(true);

          // fjson.writeCRS(features.getSchema().getCoordinateReferenceSystem(),
          // outStream);
        } else {
          LOGGER.info("CRS is null");
        }
        fjson.writeFeatureCollection(features, outStream);
      } finally {
        outStream.close();
      }
    } catch (FileNotFoundException e1) {
      LOGGER.error("Failed to write feature collection " + e1.getMessage());
    } catch (IOException e) {
      LOGGER.error("Failed to write feature collection " + e.getMessage());
    }
  }


}
