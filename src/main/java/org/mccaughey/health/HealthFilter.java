package org.mccaughey.health;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.mccaughey.layer.config.LayerMapping;
import org.mccaughey.service.Config;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
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
		String seifaOp = JsonPath.read(queryJSON,
				"[?(@['METRIC_NAME'] == 'SEIFA_METRIC')].OPERATOR[0]");
		// LOGGER.info("SEIFA OP {}", seifaOp);
		String diabetesValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'TYPE2_DIABETES')].METRIC_VALUE[0]");
		String diabetesOp = JsonPath.read(queryJSON,
				"[?(@['METRIC_NAME'] == 'TYPE2_DIABETES')].OPERATOR[0]");
		String depressionValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'DEPRESSION')].METRIC_VALUE[0]");
		String depressionOp = JsonPath.read(queryJSON,
				"[?(@['METRIC_NAME'] == 'DEPRESSION')].OPERATOR[0]");
		String obesityValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'OBESITY')].METRIC_VALUE[0]");
		String obesityOp = JsonPath.read(queryJSON,
				"[?(@['METRIC_NAME'] == 'OBESITY')].OPERATOR[0]");
		String smokingValue = JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'SMOKING')].METRIC_VALUE[0]");
		String smokingOp = JsonPath.read(queryJSON,
				"[?(@['METRIC_NAME'] == 'SMOKING')].OPERATOR[0]");
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

		SimpleFeatureSource gpSource = ((FileDataStore) Config
				.getDefaultFactory().getDataStore(
						"GP_Buffers_" + gpDistance + "m")).getFeatureSource();
		// /
		List<SimpleFeatureCollection> layers = new ArrayList<SimpleFeatureCollection>();

		SimpleFeatureCollection seifaFeatures = getAttributeFiltered_FeatureCollection(
				seifaSource, seifaOp, seifaVal, "IRSD_Decil");
		layers.add(seifaFeatures);
		SimpleFeatureCollection diabetesFeatures = getAttributeFiltered_FeatureCollection(
				diabetesSource, diabetesOp, diabetesValue, "RatePer100");
		layers.add(diabetesFeatures);
		SimpleFeatureCollection depressionFeatures = getAttributeFiltered_FeatureCollection(
				depressionSource, depressionOp, depressionValue, "RatePer100");
		layers.add(depressionFeatures);
		SimpleFeatureCollection obesityFeatures = getAttributeFiltered_FeatureCollection(
				obesitySource, obesityOp, obesityValue, "ObesePeopl");
		layers.add(obesityFeatures);
		SimpleFeatureCollection smokingFeatures = getAttributeFiltered_FeatureCollection(
				smokingSource, smokingOp, smokingValue, "SmokePop");
		layers.add(smokingFeatures);
		SimpleFeatureCollection gpBuffers = filterGPAttributes(
				gpSource, queryJSON);
		// layers.add(gpBuffers);

		SimpleFeatureCollection filteredFeatures = null; // = seifaFeatures;
		for (SimpleFeatureCollection intersectFeatures : layers) {
			LOGGER.info("Layer size {}", intersectFeatures.size());
			if ((filteredFeatures == null)) { // && (intersectFeatures.size() >
												// 0)) {
				filteredFeatures = intersectFeatures;
				LOGGER.info("Filtered features: {}", filteredFeatures.size());
			} else if ((filteredFeatures != null)) {
				// && (intersectFeatures.size() > 0)) {
				filteredFeatures = intersection(filteredFeatures,
						intersectFeatures);
				LOGGER.info("Filtered/intersected features: {}",
						filteredFeatures.size());
			}

		}
		LOGGER.info("Intersected total features: {}", filteredFeatures.size());
		SimpleFeatureCollection gpExcluded = difference(filteredFeatures,
				gpBuffers);
		LOGGER.info("Filtered total features: {}", gpExcluded.size());
		return gpExcluded;
		// return
		// intersection(intersection(intersection(intersection(seifaFeatures,
		// depressionFeatures),diabetesFeatures),obesityFeatures),smokingFeatures);

	}

	private SimpleFeatureCollection filterGPAttributes(
			SimpleFeatureSource source, String queryJSON) throws IOException {

		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
		Query query = new Query();
		List<Filter> filters = new ArrayList<Filter>();
		Boolean BULK_BILLING_AND_FEE_BASED_SERVICE = ((Boolean) (JsonPath
				.read(queryJSON,
						"$[?(@['METRIC_NAME'] == 'BULK_BILLING_AND_FEE_BASED_SERVICE')].METRIC_INCLUSION[0]")));
		if (BULK_BILLING_AND_FEE_BASED_SERVICE) {
			filters.add(ff.notEqual(ff.property("FreeProvis"),
					ff.literal("Other")));
			
		}
		Boolean BULK_BILLING_ONLY = ((Boolean) (JsonPath
				.read(queryJSON,
						"$[?(@['METRIC_NAME'] == 'BULK_BILLING_ONLY')].METRIC_INCLUSION[0]")));
		if (BULK_BILLING_ONLY) {
			filters.add(ff.equal(ff.property("FreeProvis"), ff.literal("\"Bulkbilling only\"")));
		}
		Boolean FEE_ONLY = ((Boolean) (JsonPath.read(queryJSON,
				"$[?(@['METRIC_NAME'] == 'FEE_ONLY')].METRIC_INCLUSION[0]")));
		if (FEE_ONLY) {
			filters.add(ff.equal(ff.property("FreeProvis"), ff.literal("Fee only")));
		}
		Boolean AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS = ((Boolean) (JsonPath
				.read(queryJSON,
						"$[?(@['METRIC_NAME'] == 'AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS')].METRIC_INCLUSION[0]")));
		if (AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS) {
			filters.add(ff.equal(ff.property("FreeProvis"), ff.literal("Fee only")));
		}
//		Boolean AFTER_8_PM_ON_WEEKDAYS = ((Boolean) (JsonPath
//				.read(queryJSON,
//						"$[?(@['METRIC_NAME'] == 'AFTER_8_PM_ON_WEEKDAYS')].METRIC_INCLUSION[0]")));
//		Boolean ANY_SATURDAY_SERVICE_AFTER_12_NOON = ((Boolean) (JsonPath
//				.read(queryJSON,
//						"$[?(@['METRIC_NAME'] == 'ANY_SATURDAY_SERVICE_AFTER_12_NOON')].METRIC_INCLUSION[0]")));
		Boolean ANY_SUNDAY_SERVICE = ((Boolean) (JsonPath
				.read(queryJSON,
						"$[?(@['METRIC_NAME'] == 'ANY_SUNDAY_SERVICE')].METRIC_INCLUSION[0]")));
		if (ANY_SUNDAY_SERVICE) {
			filters.add(ff.notEqual(ff.property("Sunday"), ff.literal("None")));
		}
//		Boolean COMMUNITY_HEALTH_CENTRE = ((Boolean) (JsonPath
//				.read(queryJSON,
//						"$[?(@['METRIC_NAME'] == 'COMMUNITY_HEALTH_CENTRE')].METRIC_INCLUSION[0]")));
//		Boolean MENTAL_HEALTH_SERVICE_PROVIDER = ((Boolean) (JsonPath
//				.read(queryJSON,
//						"$[?(@['METRIC_NAME'] == 'MENTAL_HEALTH_SERVICE_PROVIDER')].METRIC_INCLUSION[0]")));
		query.setFilter(ff.and(filters));
		LOGGER.info("Query: {}", query.toString());
		SimpleFeatureCollection gpClinics = source.getFeatures(query);
		LOGGER.info("Found {} gp clinics", gpClinics.size());
		return gpClinics;
	}

	private SimpleFeatureCollection getAttributeFiltered_FeatureCollection(
			SimpleFeatureSource featureSource, String operator,
			String metricValue, String attributeName) throws Exception {

		FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
		Query query = new Query();

		if (operator.equals("EQUAL_OR_GREATER_THAN")) {
			PropertyIsGreaterThanOrEqualTo filter = ff.greaterOrEqual(
					ff.property(attributeName), ff.literal(metricValue));
			query.setFilter(filter);
		}
		if (operator.equals("EQUAL_OR_LESS_THAN")) {
			PropertyIsLessThanOrEqualTo filter = ff.lessOrEqual(
					ff.property(attributeName), ff.literal(metricValue));
			query.setFilter(filter);
		}
		if (operator.equals("GREATER_THAN")) {
			PropertyIsGreaterThan filter = ff.greater(
					ff.property(attributeName), ff.literal(metricValue));
			query.setFilter(filter);
		}
		if (operator.equals("LESS_THAN")) {
			PropertyIsLessThan filter = ff.less(ff.property(attributeName),
					ff.literal(metricValue));
			query.setFilter(filter);
		}
		if (operator.equals("EQUAL")) {
			PropertyIsEqualTo filter = ff.equals(ff.property(attributeName),
					ff.literal(metricValue));
			query.setFilter(filter);
		}
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
