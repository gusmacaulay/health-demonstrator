package org.mccaughey.layer.config;

import java.util.HashMap;
import java.util.Map;

public class LayerMapping {

	public static final String SEIFA_Layer = "SEIFA_Layer";
	public static final String TYPE2_DIABETES_Layer = "TYPE2_DIABETES_Layer";
	public static final String DEPRESSION_Layer = "DEPRESSION_Layer";
	public static final String OBESITY_Layer = "OBESITY_Layer";
	public static final String SMOKING_Layer = "SMOKING_Layer";
	public static final String GENERAL_PRACTICE_Layer = "GENERAL_PRACTICE_Layer";
	public static final String COMMUNITY_HEALTH_CENTRE_Layer = "COMMUNITY_HEALTH_CENTRE_Layer";
	public static final String MENTAL_HEALTH_SERVICE_PROVIDER_Layer = "MENTAL_HEALTH_SERVICE_PROVIDER_Layer";
	public static final String GP_Buffers_100m = "GP_Buffers_100m";
	public static final String GP_Buffers_200m = "GP_Buffers_200m";
	public static final String GP_Buffers_300m = "GP_Buffers_300m";
	public static final String GP_Buffers_400m = "GP_Buffers_400m";
	public static final String GP_Buffers_500m = "GP_Buffers_500m";
	public static final String GP_Buffers_600m = "GP_Buffers_600m";
	public static final String GP_Buffers_700m = "GP_Buffers_700m";
	public static final String GP_Buffers_800m = "GP_Buffers_800m";
	public static final String GP_Buffers_900m = "GP_Buffers_900m";
	public static final String GP_Buffers_1000m = "GP_Buffers_1000m";
	public static final String GP_Buffers_1100m = "GP_Buffers_1100m";
	public static final String GP_Buffers_1200m = "GP_Buffers_1200m";

	private static Map<String, String> shapeFileLocationMap;
	static {
		shapeFileLocationMap = new HashMap<String, String>();
		shapeFileLocationMap
				.put(SEIFA_Layer, "datasets/" + "SEIFA_vicgrid.shp");
		// shapeFileLocationMap.put(SEIFA_Layer, "datasets/"+"SEIFA.shp");
		shapeFileLocationMap.put(TYPE2_DIABETES_Layer, "datasets/"
				+ "Type_2_Diabetes_vicgrid.shp");
		shapeFileLocationMap.put(DEPRESSION_Layer, "datasets/"
				+ "Mood_Problems_vicgrid.shp");
		shapeFileLocationMap.put(OBESITY_Layer, "datasets/"
				+ "Obesity_vicgrid.shp");
		shapeFileLocationMap.put(SMOKING_Layer, "datasets/"
				+ "Smoking_vicgrid.shp");
		shapeFileLocationMap.put(GENERAL_PRACTICE_Layer, "datasets/"
				+ "GPs_INWMML.shp");
		shapeFileLocationMap.put(COMMUNITY_HEALTH_CENTRE_Layer, "datasets/"
				+ "CommHealthCentres_ML.shp");
		shapeFileLocationMap.put(MENTAL_HEALTH_SERVICE_PROVIDER_Layer,
				"datasets/" + "MentalHealth.shp");
		shapeFileLocationMap.put(GP_Buffers_100m, "datasets/DistFromGPs/"
				+ "100m.shp");
		shapeFileLocationMap.put(GP_Buffers_200m, "datasets/DistFromGPs/"
				+ "200m.shp");
		shapeFileLocationMap.put(GP_Buffers_300m, "datasets/DistFromGPs/"
				+ "300m.shp");
		shapeFileLocationMap.put(GP_Buffers_400m, "datasets/DistFromGPs/"
				+ "400m.shp");
		shapeFileLocationMap.put(GP_Buffers_500m, "datasets/DistFromGPs/"
				+ "500m.shp");
		shapeFileLocationMap.put(GP_Buffers_600m, "datasets/DistFromGPs/"
				+ "600m.shp");
		shapeFileLocationMap.put(GP_Buffers_700m, "datasets/DistFromGPs/"
				+ "700m.shp");
		shapeFileLocationMap.put(GP_Buffers_800m, "datasets/DistFromGPs/"
				+ "800m.shp");
		shapeFileLocationMap.put(GP_Buffers_900m, "datasets/DistFromGPs/"
				+ "900m.shp");
		shapeFileLocationMap.put(GP_Buffers_1000m, "datasets/DistFromGPs/"
				+ "1000m.shp");
		shapeFileLocationMap.put(GP_Buffers_1100m, "datasets/DistFromGPs/"
				+ "1100m.shp");
		shapeFileLocationMap.put(GP_Buffers_1200m, "datasets/DistFromGPs/"
				+ "1200m.shp");
	}

	public static String getPath(String layerName) {
		return shapeFileLocationMap.get(layerName);
	}

}