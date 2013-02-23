package org.mccaughey.health.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
					fjson.setEncodeFeatureCollectionCRS(true);
//					 fjson.writeCRS(features.getSchema().getCoordinateReferenceSystem(),
//							 outStream);
				} else {
					LOGGER.info("CRS is null");
				}
				fjson.writeFeatureCollection(features, outStream);
			} finally {
				outStream.close();
			}
		} catch (FileNotFoundException e1) {
			LOGGER.error("Failed to write feature collection "
					+ e1.getMessage());
		} catch (IOException e) {
			LOGGER.error("Failed to write feature collection " + e.getMessage());
		}
	}

}
