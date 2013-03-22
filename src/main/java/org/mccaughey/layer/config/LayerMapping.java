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
	public static final String GP_Buffers = "GP_Buffers";

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
		shapeFileLocationMap.put(GP_Buffers, "datasets/DistFromCommHealth/"
				+ "DISTm.shp");
	}

	public static String getPath(String layerName) {
		return shapeFileLocationMap.get(layerName);
	}

}