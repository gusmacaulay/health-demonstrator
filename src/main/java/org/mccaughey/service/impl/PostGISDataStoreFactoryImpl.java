package org.mccaughey.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.mccaughey.service.DataStoreFactory;

public class PostGISDataStoreFactoryImpl implements DataStoreFactory {
	private DataStore dataStore = null;
	
	public void dipose(){
		dataStore.dispose();
	}

	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException {
		return getDataStore(layerName).getFeatureSource(layerName);
	}

	public DataStore getDataStore(String layername) throws IOException {
			return getPOSTGISDataStore();
	}
	
	public DataStore getPOSTGISDataStore() throws IOException {
		if (this.dataStore == null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dbtype", "postgis");
			params.put("host", "localhost");
			params.put("port", 5432);
			params.put("schema", "public");
//			 params.put("database", "housing_4283");
			params.put("database", "housingmetric");
			params.put("user", "postgres");
			params.put("passwd", "1q2w3e4r");
			dataStore = DataStoreFinder.getDataStore(params);
		}
		return this.dataStore;
	}

	public DataStore getExportableDataStore() throws Exception {
		return getPOSTGISDataStore();
	}	

}
