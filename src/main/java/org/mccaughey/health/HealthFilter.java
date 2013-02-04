package org.mccaughey.health;

import java.io.IOException;
import java.util.Map;

import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.mccaughey.constant.MetricOperator;
import org.mccaughey.layer.config.LayerMapping;
import org.mccaughey.service.Config;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;

public class HealthFilter {
	private SimpleFeatureCollection getAttributeFiltered_FeatureCollection(
			String layerName, MetricOperator operator, String metricValue,
			String attributeName) {

		SimpleFeatureSource featureSource = null;
		try {
			featureSource = ((FileDataStore) Config.getDefaultFactory()
					.getDataStore(layerName)).getFeatureSource();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
		FeatureType schema = featureSource.getSchema();

		String geometryPropertyName = schema.getGeometryDescriptor()
				.getLocalName();

		Query query = new Query();

		PropertyIsEqualTo filter = ff.equals(ff.property(attributeName),
				ff.literal(metricValue));// inundation

		query.setFilter(filter);

		// /

		// get a feature collection of filtered features
		try {
			SimpleFeatureCollection fCollection = featureSource
					.getFeatures(query);
			return fCollection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public SimpleFeatureCollection doAnalyze(Map<String, Object> uiParameteres) {
		return getAttributeFiltered_FeatureCollection(LayerMapping.SEIFA_Layer, MetricOperator.GREATERTHAN, "1", "IRSD_Decil");

	}
	public static void main(String[] args) {
		HealthFilter h = new HealthFilter();
		h.doAnalyze(null);
	}

}
