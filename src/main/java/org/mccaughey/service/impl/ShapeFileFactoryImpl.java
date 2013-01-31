package org.mccaughey.service.impl;

import java.io.File;
import java.io.FileInputStream;

import org.geotools.data.DataStore;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.mccaughey.layer.config.LayerMapping;
import org.mccaughey.service.DataStoreFactory;

public class ShapeFileFactoryImpl implements DataStoreFactory {
	
	
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
//		if (this.dataStore == null) {
			LayerMapping layerMapping = new LayerMapping();
			File file;
			String shapefilePath = LayerMapping.getPath(layerName);
			if (shapefilePath!=null) {
				file = new File(shapefilePath);

			} else {
				throw new Exception("don't know where find this layer :"
						+ layerName);
			}
			FileDataStore store = FileDataStoreFinder.getDataStore(file);
//			this.dataStore = store;
//		}
		return store;
	}

	public DataStore getExportableDataStore() throws Exception {
		return null;
	}
}
