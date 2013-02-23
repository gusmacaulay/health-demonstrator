package org.mccaughey.health;

import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.json.JSONArray;
import org.mccaughey.constant.MetricOperator;
import org.mccaughey.layer.config.LayerMapping;
import org.mccaughey.service.Config;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsGreaterThan;

public class HealthFilter {
    private SimpleFeatureCollection getAttributeFiltered_FeatureCollection(
	    SimpleFeatureSource featureSource, MetricOperator operator, String metricValue,
	    String attributeName) throws Exception {

	FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
	Query query = new Query();

	PropertyIsGreaterThan filter = ff.greater(ff.property(attributeName),
		ff.literal(metricValue));

	query.setFilter(filter);

	// get a feature collection of filtered features
	SimpleFeatureCollection fCollection = featureSource.getFeatures(query);
	return fCollection;
    }

    public SimpleFeatureCollection filter(JSONArray uiParams) throws Exception {
	SimpleFeatureSource seifaSource = ((FileDataStore) Config
		.getDefaultFactory().getDataStore(LayerMapping.SEIFA_Layer))
		.getFeatureSource();
	
	return getAttributeFiltered_FeatureCollection(seifaSource,
		MetricOperator.GREATERTHAN, "3", "IRSD_Decil");

    }
}
