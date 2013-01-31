package org.mccaughey.service;

public class Config {
	public static DataStoreFactory getDefaultFactory() {
		return DataStoreFactoryBuilder.getBuilder("ShapeFileFactory");
	}
	
	public static DataStoreFactory getWFSFactory() {
		return DataStoreFactoryBuilder.getBuilder("WFSDataStoreFactory");
	}
	
	public static DataStoreFactory getGeoJSONFileFactory() {
		return DataStoreFactoryBuilder.getBuilder("GeoJSONFileFactory");
	}
	
	public static DataStoreFactory getShapeFileFactory() {
		return DataStoreFactoryBuilder.getBuilder("ShapeFileFactory");
	}
}
