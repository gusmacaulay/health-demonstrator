package org.mccaughey.health;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.geotools.data.simple.SimpleFeatureCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/health-filter")
public class HealthFilterResource {
  static final Logger LOGGER = LoggerFactory
      .getLogger(HealthFilterResource.class);

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public void getRegions(HttpServletResponse response, @PathVariable String id)
      throws Exception {
    LOGGER.info("Got id:" + id);
  }

  /*
   * @RequestMapping(value = "/runAnalysis", method = RequestMethod.POST,
   * headers = "Content-Type=text/plain")//, consumes="application/json",
   * produces="application/json")
   * @ResponseStatus(HttpStatus.OK) public @ResponseBody Map<String, Object>
   * handleFilteringRequest(
   * @RequestBody Map<String, Object> uiParameteres) throws Exception {
   */
  //
  // @RequestMapping(method = RequestMethod.POST, value = "/runAnalysis",
  // headers = "Content-Type=application/json")
  // @ResponseStatus(HttpStatus.OK)
  // public @ResponseBody void developmentAssessment(@RequestBody Map<String,
  // Object> assessmentParams) throws Exception {
  //
  // System.out.println("handleFilteringRequest");
  // HealthFilter healthFilter = new HealthFilter();
  //
  // // SimpleFeatureCollection outputfeatures = healthFilter
  // // .doAnalyze(uiParameteres);
  // // GeoJSONUtility
  // // .writeFeatures(outputfeatures, response.getOutputStream());
  // return;
  // }

  @RequestMapping(method = RequestMethod.POST, value = "/runAnalysis")
  public @ResponseBody
  void addEmp(@RequestBody Map<String, Object> filterParameters) {
    LOGGER.info("Post received");
    return;
  }

  @RequestMapping(value = "ccc", method = RequestMethod.GET)
  public @ResponseBody
  Map<String, ? extends Object> loadLGAs() throws Exception {
    System.out.println("fgf");
    return null;
  }
}
