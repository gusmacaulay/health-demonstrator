package org.mccaughey.health;

import java.io.File;
import java.io.FileOutputStream;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.json.JSONArray;
import org.mccaughey.health.util.GeoJSONUtility;

public class HealthFilterTest {

    public void testHealthFilterSimple() throws Exception  {
	
	String fakearrayparameters =  "[{\"METRIC_NAME\":\"SEIFA_METRIC\",\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"120\"},{\"METRIC_NAME\":\"TYPE2_DIABETES\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"70\"},{\"METRIC_NAME\":\"DEPRESSION\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"280\"},{\"METRIC_NAME\":\"OBESITY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"SMOKING\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"NO_ACCESS_TO_GENERAL_PRACTICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_AND_FEE_BASED_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"FEE_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SATURDAY_SERVICE_AFTER_12_NOON\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SUNDAY_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"COMMUNITY_HEALTH_CENTRE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"MENTAL_HEALTH_SERVICE_PROVIDER\",\"METRIC_INCLUSION\":false}]";
	Object uiParameteresObj=fakearrayparameters;
	if (uiParameteresObj==null)
		throw new Exception("UI parameters doesn't exist in session's attribute");
	HealthFilter healthFilter = new HealthFilter();

	JSONArray jsonArray = new JSONArray(uiParameteresObj.toString());
	SimpleFeatureCollection outputfeatures = healthFilter.filter(jsonArray);
	GeoJSONUtility.writeFeatures(outputfeatures, new FileOutputStream(new File("./target/testHealthFilterSimple.json")));
    }
}
