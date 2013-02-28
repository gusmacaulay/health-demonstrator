package org.mccaughey.health;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.mccaughey.health.util.GeoJSONUtility;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HealthFilterTest {

  public void testHealthFilterSimple() throws Exception {

    String fakearrayparameters = "[{\"METRIC_NAME\":\"SEIFA_METRIC\",\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"120\"},{\"METRIC_NAME\":\"TYPE2_DIABETES\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"70\"},{\"METRIC_NAME\":\"DEPRESSION\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"280\"},{\"METRIC_NAME\":\"OBESITY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"SMOKING\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"NO_ACCESS_TO_GENERAL_PRACTICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_AND_FEE_BASED_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"FEE_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SATURDAY_SERVICE_AFTER_12_NOON\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SUNDAY_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"COMMUNITY_HEALTH_CENTRE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"MENTAL_HEALTH_SERVICE_PROVIDER\",\"METRIC_INCLUSION\":false}]";
    Object uiParameteresObj = fakearrayparameters;
    if (uiParameteresObj == null)
      throw new Exception("UI parameters doesn't exist in session's attribute");
    HealthFilter healthFilter = new HealthFilter();
    SimpleFeatureCollection outputfeatures = healthFilter
        .filter(uiParameteresObj.toString());
    GeoJSONUtility.writeFeatures(outputfeatures, new FileOutputStream(new File(
        "./target/testHealthFilterSimple.json")));
  }

  public void testHealthFilterResource() throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    
    String jsonString = "{\"params\" : [{\"METRIC_NAME\":\"SEIFA_METRIC\",\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"120\"},{\"METRIC_NAME\":\"TYPE2_DIABETES\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"70\"},{\"METRIC_NAME\":\"DEPRESSION\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"280\"},{\"METRIC_NAME\":\"OBESITY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"SMOKING\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"NO_ACCESS_TO_GENERAL_PRACTICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_AND_FEE_BASED_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"FEE_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SATURDAY_SERVICE_AFTER_12_NOON\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SUNDAY_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"COMMUNITY_HEALTH_CENTRE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"MENTAL_HEALTH_SERVICE_PROVIDER\",\"METRIC_INCLUSION\":false}]}";
    
    ObjectMapper m = new ObjectMapper();

    Map<String, Object> parameters = m.readValue(jsonString, Map.class);
    
    HealthFilterResource hr = new HealthFilterResource();
    hr.handleFilteringRequest(parameters, request, response);
  }
}
