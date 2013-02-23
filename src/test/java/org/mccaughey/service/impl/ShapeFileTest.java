package org.mccaughey.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.geotools.data.FileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.mccaughey.layer.config.LayerMapping;
import org.mccaughey.service.Config;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

public class ShapeFileTest {

    public void testShapeFilesAccessibility() throws Exception {

	String layerName = LayerMapping.SEIFA_Layer;
	SimpleFeatureSource featureSource = ((FileDataStore) Config
		.getDefaultFactory().getDataStore(layerName))
		.getFeatureSource();
	SimpleFeatureCollection features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

	layerName = LayerMapping.TYPE2_DIABETES_Layer;
	featureSource = ((FileDataStore) Config.getDefaultFactory()
		.getDataStore(layerName)).getFeatureSource();
	features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

	layerName = LayerMapping.OBESITY_Layer;
	featureSource = ((FileDataStore) Config.getDefaultFactory()
		.getDataStore(layerName)).getFeatureSource();
	features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

	layerName = LayerMapping.SMOKING_Layer;
	featureSource = ((FileDataStore) Config.getDefaultFactory()
		.getDataStore(layerName)).getFeatureSource();
	features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

	layerName = LayerMapping.DEPRESSION_Layer;
	featureSource = ((FileDataStore) Config.getDefaultFactory()
		.getDataStore(layerName)).getFeatureSource();
	features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

	layerName = LayerMapping.GENERAL_PRACTICE_Layer;
	featureSource = ((FileDataStore) Config.getDefaultFactory()
		.getDataStore(layerName)).getFeatureSource();
	features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

	layerName = LayerMapping.COMMUNITY_HEALTH_CENTRE_Layer;
	featureSource = ((FileDataStore) Config.getDefaultFactory()
		.getDataStore(layerName)).getFeatureSource();
	features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

	layerName = LayerMapping.MENTAL_HEALTH_SERVICE_PROVIDER_Layer;
	featureSource = ((FileDataStore) Config.getDefaultFactory()
		.getDataStore(layerName)).getFeatureSource();
	features = featureSource.getFeatures();
	System.out.println("layer Name:+" + layerName + ", size:"
		+ features.size());

    }
}
