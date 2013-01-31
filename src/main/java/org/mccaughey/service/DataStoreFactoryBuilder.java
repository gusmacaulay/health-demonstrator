package org.mccaughey.service;

import org.mccaughey.service.impl.PostGISDataStoreFactoryImpl;
import org.mccaughey.service.impl.ShapeFileFactoryImpl;

public class DataStoreFactoryBuilder {
	
	private static DataStoreFactory _WFSDataStoreFactoryImpl = null;
	private static DataStoreFactory _PostGISDataStoreFactoryImpl = null;
	private static DataStoreFactory _GeoJSONFileFactoryImpl = null;
	private static DataStoreFactory _ShapeFileFactoryImpl = null;
	
	public static DataStoreFactory getBuilder(String buildername){
//		if ("WFSDataStoreFactory".equals(buildername)){
//			if (_WFSDataStoreFactoryImpl==null)	
//				_WFSDataStoreFactoryImpl = new WFSDataStoreFactoryImpl();
//			return _WFSDataStoreFactoryImpl;
//					 
//		}
		if ("PostGISDataStoreFactory".equals(buildername)){
			if (_PostGISDataStoreFactoryImpl==null)	
				_PostGISDataStoreFactoryImpl = new PostGISDataStoreFactoryImpl();
			return _PostGISDataStoreFactoryImpl;
		}
//		if ("GeoJSONFileFactory".equals(buildername)){
//			if (_GeoJSONFileFactoryImpl==null)	
//				_GeoJSONFileFactoryImpl = new GeoJSONFileFactoryImpl();
//			return _GeoJSONFileFactoryImpl;
//		}
		if ("ShapeFileFactory".equals(buildername)){
			if (_ShapeFileFactoryImpl==null)	
				_ShapeFileFactoryImpl = new ShapeFileFactoryImpl();
			return _ShapeFileFactoryImpl;
		}
		return null;
	}

}
