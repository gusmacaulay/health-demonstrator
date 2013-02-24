package org.mccaughey.health;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.mccaughey.constant.MetricOperator;
import org.mccaughey.layer.config.LayerMapping;
import org.mccaughey.service.Config;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsGreaterThan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;
import com.vividsolutions.jts.geom.Geometry;

public class HealthFilter {

    static final Logger LOGGER = LoggerFactory.getLogger(HealthFilter.class);

    public SimpleFeatureCollection filter(String queryJSON) throws Exception {

	List<JSONObject> metrics = JsonPath.read(queryJSON, "$[*]");

	for (JSONObject metric : metrics) {
	    LOGGER.info(metric.toJSONString());
	}

	SimpleFeatureSource seifaSource = ((FileDataStore) Config
		.getDefaultFactory().getDataStore(LayerMapping.SEIFA_Layer))
		.getFeatureSource();

	SimpleFeatureSource depressionSource = ((FileDataStore) Config
		.getDefaultFactory()
		.getDataStore(LayerMapping.DEPRESSION_Layer))
		.getFeatureSource();

	SimpleFeatureCollection depressionFeatures = getAttributeFiltered_FeatureCollection(
		depressionSource, MetricOperator.GREATERTHAN, "8", "RatePer100");

	SimpleFeatureCollection seifaFeatures = getAttributeFiltered_FeatureCollection(
		seifaSource, MetricOperator.GREATERTHAN, "2", "IRSD_Decil");

	return intersection(seifaFeatures, depressionFeatures);

    }

    private SimpleFeatureCollection getAttributeFiltered_FeatureCollection(
	    SimpleFeatureSource featureSource, MetricOperator operator,
	    String metricValue, String attributeName) throws Exception {

	FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
	Query query = new Query();

	PropertyIsGreaterThan filter = ff.greater(ff.property(attributeName),
		ff.literal(metricValue));

	query.setFilter(filter);

	// get a feature collection of filtered features
	// SimpleFeatureCollection fCollection = featureSource.getFeatures();
	SimpleFeatureCollection fCollection = featureSource.getFeatures(query);
	return fCollection;
    }

    private SimpleFeatureCollection intersection(SimpleFeatureCollection B,
	    SimpleFeatureCollection A) throws IOException {
	SimpleFeatureIterator AFeatures = A.features();
	SimpleFeatureIterator BFeatures = B.features();
	FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
	LOGGER.info(B.size() + " " + A.size());
	SimpleFeatureSource BSource = DataUtilities.source(B);
	List<SimpleFeature> intersectionFeatures = new ArrayList<SimpleFeature>();
	while (AFeatures.hasNext()) {
	    SimpleFeature featureA = AFeatures.next();
	    Geometry geometryA = (Geometry) featureA.getDefaultGeometry();
//	    while (BFeatures.hasNext()) {
//		SimpleFeature featureB = BFeatures.next();
//		Geometry geometryB = (Geometry) featureB.getDefaultGeometry();
//		if (geometryB.intersects(geometryA)) {
//		    LOGGER.info("ding!");
//		}
//	    }

//	   LOGGER.info(A.getSchema().getGeometryDescriptor().getName()
//		    .toString());
	    Filter filter = ff.intersects(ff.property(A.getSchema()
		    .getGeometryDescriptor().getName()), ff.literal(geometryA));
	    SimpleFeatureCollection i = BSource.getFeatures(filter);
	  //  LOGGER.info("found " + i.size());
	    intersectionFeatures.addAll(DataUtilities.list(BSource
		    .getFeatures(filter)));
	}
	//LOGGER.info("Ding!" + intersectionFeatures.size());
	return DataUtilities.collection(intersectionFeatures);
    }

}
