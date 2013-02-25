package org.mccaughey.health;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.mccaughey.health.util.GeoJSONUtility;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vividsolutions.jts.geom.Geometry;

@Controller
@RequestMapping("/health-filter")
public class HealthFilterResource {
  static final Logger LOGGER = LoggerFactory
      .getLogger(HealthFilterResource.class);

  @RequestMapping(method = RequestMethod.POST, value = "/runAnalysis", consumes = "application/json")
  public void handleFilteringRequest(
      @RequestBody Map<String, Object> uiParameteres,
      HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    System.out.println("handleFilteringRequest");
    System.out.println("set uiParameteres as session's attribute");
    request.getSession().setAttribute("uiParameteres", uiParameteres);

  }

  @RequestMapping(method = RequestMethod.GET, value = "filterResult", produces = "application/json")
  public void getFilterResult(HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    System.out.println("getFilterResult");
    Object uiParameteresObj = request.getSession()
        .getAttribute("uiParameteres");
    String fakearrayparameters = "[{\"METRIC_NAME\":\"SEIFA_METRIC\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"120\"},{\"METRIC_NAME\":\"TYPE2_DIABETES\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"70\"},{\"METRIC_NAME\":\"DEPRESSION\",\"METRIC_INCLUSION\":true,\"OPERATOR\":\"LESS_THAN\",\"METRIC_VALUE\":\"280\"},{\"METRIC_NAME\":\"OBESITY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"SMOKING\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"NO_ACCESS_TO_GENERAL_PRACTICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_AND_FEE_BASED_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"BULK_BILLING_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"FEE_ONLY\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"AFTER_8_PM_ON_WEEKDAYS\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SATURDAY_SERVICE_AFTER_12_NOON\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"ANY_SUNDAY_SERVICE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"COMMUNITY_HEALTH_CENTRE\",\"METRIC_INCLUSION\":false},{\"METRIC_NAME\":\"MENTAL_HEALTH_SERVICE_PROVIDER\",\"METRIC_INCLUSION\":false}]";
    uiParameteresObj = fakearrayparameters;
    if (uiParameteresObj == null)
      throw new Exception(
          "Internal Server Error: UI parameters doesn't exist in session's attribute");
    HealthFilter healthFilter = new HealthFilter();

    // JSONArray jsonArray = new JSONArray();

    SimpleFeatureCollection outputfeatures = healthFilter
        .filter(uiParameteresObj.toString());
    CoordinateReferenceSystem fromCRS = outputfeatures.getSchema()
        .getCoordinateReferenceSystem();
    CoordinateReferenceSystem toCRS = CRS.decode("EPSG:4326");
    GeoJSONUtility.writeFeatures(outputfeatures, response.getOutputStream());

  }

  private SimpleFeatureCollection reproject(SimpleFeatureCollection collection,
      CoordinateReferenceSystem fromCRS, CoordinateReferenceSystem toCRS) {

    boolean lenient = true; // allow for some error due to different datums
    MathTransform transform = CRS.findMathTransform(fromCRS, toCRS, lenient);

    FeatureIterator iter = collection.features();

    try {
      while (iter.hasNext()) {
        SimpleFeature feature = (SimpleFeature)iter.next();
        Geometry geom = (Geometry)feature.getDefaultGeometry();
        Geometry geometry2 = JTS.transform(geometry, transform);

        copy.setDefaultGeometry(geometry2);
      }
    } finally {
      DataUtilities.close(iter);
    }

  }

}
