package org.mccaughey.health;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
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

		// List<JSONObject> metrics = JsonPath.read(queryJSON, "$[*]");
		//
		// for (JSONObject metric : metrics) {
		//
		// LOGGER.info(metric.toJSONString());
		// }

		LOGGER.info("JSON: {}", queryJSON);

		String seifaVal = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'SEIFA_METRIC')].METRIC_VALUE[0]");
		String diabetesValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'TYPE2_DIABETES')].METRIC_VALUE[0]");
		String depressionValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'DEPRESSION')].METRIC_VALUE[0]");
		String obesityValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'OBESITY')].METRIC_VALUE[0]");
		String smokingValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'SMOKING')].METRIC_VALUE[0]");
		LOGGER.info("seifa val:" + seifaVal);
		String gpDistance = JsonPath
				.read(queryJSON,
						"$[?(@['METRIC_NAME'] == 'NO_ACCESS_TO_GENERAL_PRACTICE')].METRIC_VALUE[0]");
		SimpleFeatureSource seifaSource = ((FileDataStore) Config
				.getDefaultFactory().getDataStore(LayerMapping.SEIFA_Layer))
				.getFeatureSource();

		SimpleFeatureSource diabetesSource = ((FileDataStore) Config
				.getDefaultFactory().getDataStore(
						LayerMapping.TYPE2_DIABETES_Layer)).getFeatureSource();

		SimpleFeatureSource depressionSource = ((FileDataStore) Config
				.getDefaultFactory()
				.getDataStore(LayerMapping.DEPRESSION_Layer))
				.getFeatureSource();
		SimpleFeatureSource obesitySource = ((FileDataStore) Config
				.getDefaultFactory().getDataStore(LayerMapping.OBESITY_Layer))
				.getFeatureSource();

		SimpleFeatureSource smokingSource = ((FileDataStore) Config
				.getDefaultFactory().getDataStore(LayerMapping.SMOKING_Layer))
				.getFeatureSource();

		String gpBuffersFile = LayerMapping.getPath(LayerMapping.GP_Buffers)
				.replace("DIST", gpDistance);
		LOGGER.info("Buffers file?" + gpBuffersFile);
		SimpleFeatureSource gpSource = FileDataStoreFinder.getDataStore(
				new File(gpBuffersFile)).getFeatureSource();

		List<SimpleFeatureCollection> layers = new ArrayList<SimpleFeatureCollection>();

		SimpleFeatureCollection seifaFeatures = getAttributeFiltered_FeatureCollection(
				seifaSource, MetricOperator.GREATERTHAN, seifaVal, "IRSD_Decil");
		layers.add(seifaFeatures);
		SimpleFeatureCollection diabetesFeatures = getAttributeFiltered_FeatureCollection(
				diabetesSource, MetricOperator.GREATERTHAN, diabetesValue,
				"RatePer100");
		layers.add(diabetesFeatures);
		SimpleFeatureCollection depressionFeatures = getAttributeFiltered_FeatureCollection(
				depressionSource, MetricOperator.GREATERTHAN, depressionValue,
				"RatePer100");
		layers.add(depressionFeatures);
		SimpleFeatureCollection obesityFeatures = getAttributeFiltered_FeatureCollection(
				obesitySource, MetricOperator.GREATERTHAN, obesityValue,
				"ObesePeopl");
		layers.add(obesityFeatures);
		SimpleFeatureCollection smokingFeatures = getAttributeFiltered_FeatureCollection(
				smokingSource, MetricOperator.GREATERTHAN, smokingValue,
				"SmokePop");
		layers.add(smokingFeatures);
		SimpleFeatureCollection gpBuffers = gpSource.getFeatures();
		//layers.add(gpBuffers);

		SimpleFeatureCollection filteredFeatures = null; // = seifaFeatures;
		for (SimpleFeatureCollection intersectFeatures : layers) {
			LOGGER.info("Layer size {}", intersectFeatures.size());
			if ((filteredFeatures == null)) { // && (intersectFeatures.size() > 0)) {
				filteredFeatures = intersectFeatures;
				LOGGER.info("Filtered features: {}", filteredFeatures.size());
			} else if ((filteredFeatures != null)) {
				//	&& (intersectFeatures.size() > 0)) {
				filteredFeatures = intersection(filteredFeatures,
						intersectFeatures);
				LOGGER.info("Filtered/intersected features: {}",
						filteredFeatures.size());
			}

		}
		LOGGER.info("Intersected total features: {}", filteredFeatures.size());
		SimpleFeatureCollection gpExcluded = difference(filteredFeatures,gpBuffers);
		LOGGER.info("Filtered total features: {}", gpExcluded.size());
		return gpExcluded;
		// return
		// intersection(intersection(intersection(intersection(seifaFeatures,
		// depressionFeatures),diabetesFeatures),obesityFeatures),smokingFeatures);

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
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
		// LOGGER.info(B.size() + " " + A.size());
		SimpleFeatureSource BSource = DataUtilities.source(B);
		List<SimpleFeature> intersectionFeatures = new ArrayList<SimpleFeature>();
		while (AFeatures.hasNext()) {
			SimpleFeature featureA = AFeatures.next();
			Geometry geometryA = (Geometry) featureA.getDefaultGeometry();

			Filter filter = ff.intersects(ff.property(A.getSchema()
					.getGeometryDescriptor().getName()), ff.literal(geometryA));
			// SimpleFeatureCollection i = BSource.getFeatures(filter);
			// LOGGER.info("found " + i.size());
			intersectionFeatures.addAll(DataUtilities.list(BSource
					.getFeatures(filter)));
		}
		AFeatures.close();
		// LOGGER.info("Ding!" + intersectionFeatures.size());
		return DataUtilities.collection(intersectionFeatures);
	}

	private SimpleFeatureCollection difference(SimpleFeatureCollection A,
			SimpleFeatureCollection B) throws IOException {
		SimpleFeatureIterator BFeatures = B.features();
		List<SimpleFeature> difference = new ArrayList<SimpleFeature>();
		SimpleFeatureSource ASource = DataUtilities.source(A);
		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
		Geometry union = null;
		while (BFeatures.hasNext()) {
			SimpleFeature featureB = BFeatures.next();
			Geometry geometryB = (Geometry) featureB.getDefaultGeometry();
			if (union == null) {
				union = geometryB;
			} else {
				union = union.union(geometryB);
			}
		}
		BFeatures.close();
		LOGGER.info("Union area {}", union.getArea());
		Filter filter = ff.not(ff.intersects(
				ff.property(B.getSchema().getGeometryDescriptor().getName()),
				ff.literal(union)));
		// SimpleFeatureCollection i = BSource.getFeatures(filter);
		// LOGGER.info("found " + i.size());
		return ASource.getFeatures(filter);

	}
}
