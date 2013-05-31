package org.mccaughey.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.mccaughey.health.HealthFilter;
import org.mccaughey.layer.config.LayerMapping;
import org.mccaughey.service.DataStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ShapeFileFactoryImpl implements DataStoreFactory {
	
	static final Logger LOGGER = LoggerFactory.getLogger(ShapeFileFactoryImpl.class);
	private DataStore dataStore = null;
	private SimpleFeatureSource featureSource = null;

	public void dipose() {
		dataStore.dispose();
	}

	public SimpleFeatureSource getFeatureSource(String layerName)
			throws Exception {
 
		featureSource = ((FileDataStore)getDataStore(layerName)).getFeatureSource();
		return featureSource;
	}

	public DataStore getDataStore(String layerName) throws Exception {
			
			File file;
			Resource rsrc = new ClassPathResource(LayerMapping.getPath(layerName));
			String shapefilePath =rsrc.getFile().getAbsolutePath();
			
			if (shapefilePath!=null) {
				file = new File(shapefilePath);
				//LOGGER.info("=== load shpfile : {}",file.getAbsolutePath());
			} else {
				throw new Exception("don't know where find this layer :"
						+ layerName);
			}
			
			FileDataStore store = FileDataStoreFinder.getDataStore(file);

		return store;
	}

	public DataStore getExportableDataStore() throws Exception {
		return null;
	}
}
