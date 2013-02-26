package org.mccaughey.health;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.mccaughey.health.util.GeoJSONUtility;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
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
    CoordinateReferenceSystem toCRS = CRS.decode("EPSG:3857");
    GeoJSONUtility.writeFeatures(reproject(outputfeatures, fromCRS, toCRS),
        response.getOutputStream());

  }

  private SimpleFeatureCollection reproject(SimpleFeatureCollection collection,
      CoordinateReferenceSystem fromCRS, CoordinateReferenceSystem toCRS)
      throws MismatchedDimensionException, TransformException, FactoryException {

    boolean lenient = true; // allow for some error due to different datums
    MathTransform transform = CRS.findMathTransform(fromCRS, toCRS, lenient);
    SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
    ftb.setName("reprojected");
    ftb.crs(toCRS);
    ftb.setAttributes(collection.getSchema().getAttributeDescriptors());
   
   
    SimpleFeatureType ft = ftb.buildFeatureType();
    LOGGER.info("NEW CRS " + ft.getCoordinateReferenceSystem());
    FeatureIterator iter = collection.features();
    List<SimpleFeature> reprojected = new ArrayList();
    try {
      while (iter.hasNext()) {
        SimpleFeature feature = (SimpleFeature) iter.next();
        Geometry fromGeom = (Geometry) feature.getDefaultGeometry();
        Geometry toGeom = JTS.transform(fromGeom, transform);

        reprojected.add(buildNewFeature(feature, toGeom, ft));

      }
      return DataUtilities.collection(reprojected);
    } finally {
      iter.close();
    }

  }

  private SimpleFeature buildNewFeature(SimpleFeature feature, Geometry geom,
      SimpleFeatureType ft) {

    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(ft);
    featureBuilder.addAll(feature.getAttributes());
    featureBuilder.set(feature.getDefaultGeometryProperty().getName(), geom);
    return featureBuilder.buildFeature(feature.getID());

  }

}
