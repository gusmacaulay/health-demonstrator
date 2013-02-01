package org.mccaughey.health;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/health-filter")
public class HealthFilterResource {
  static final Logger LOGGER = LoggerFactory.getLogger(HealthFilterResource.class);
  	
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public void getRegions(HttpServletResponse response, @PathVariable String id) throws Exception {
    LOGGER.info("Got id:" + id);
  }
}
