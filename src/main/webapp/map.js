window['map_init'] = function() {
	// Map
	var controls = [ new OpenLayers.Control.Navigation(),
			new OpenLayers.Control.PanZoomBar(),
			new OpenLayers.Control.LayerSwitcher({
				'ascending' : false
			}), new OpenLayers.Control.Permalink(),
			new OpenLayers.Control.ScaleLine(),
			new OpenLayers.Control.Permalink('permalink'),
			new OpenLayers.Control.MousePosition(),
			new OpenLayers.Control.OverviewMap(),
			new OpenLayers.Control.KeyboardDefaults()];
	map = new OpenLayers.Map('mappanel', {
		controls : controls
	});
	
	var osm = new OpenLayers.Layer.OSM();
	osm.setIsBaseLayer(true);
	
	var mercator = new OpenLayers.Projection("EPSG:900913");
	var saveStrategy = new OpenLayers.Strategy.Save();

	lyr_results = new OpenLayers.Layer.Vector("Results", {
		projection : mercator,
		strategies : [ new OpenLayers.Strategy.Fixed() ],

		protocol : new OpenLayers.Protocol.HTTP({
			url : "/health-demonstrator/service/health-filter/filterResult",
			format : new OpenLayers.Format.GeoJSON()
		}),
		// styleMap : new OpenLayers.StyleMap({
		// "default" : pathStyle
		// "default" : new OpenLayers.Style({
		// graphicName : "circle",
		// pointRadius : 10,
		// fillOpacity : 0.5,
		// fillColor : "#9e69e3",
		// strokeColor : "#8e45ed",
		// strokeWidth : 1
		// })
		// }),
		renderers : [ "Canvas", "SVG", "VML" ]
	});

	// define SEIFA layers
	lyr_SEIFA = new OpenLayers.Layer.WMS("SEIFA",
			"/health-demonstrator/geoserver/wms", {
				LAYERS : 'CSDILA_local:seifa',
				STYLES : '',
				format : 'image/png',
				tiled : true,
				transparent : true,
				tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom
			}, {
				buffer : 0,
				displayOutsideMaxExtent : true,
				projection : mercator,
				reproject : true
			});
	lyr_SEIFA.setIsBaseLayer(false);
	lyr_SEIFA.setVisibility(false);
	
	
	//define GPs layers 		srsName : "EPSG:900913",
	lyr_GP = new OpenLayers.Layer.WMS("GP",
			"/health-demonstrator/geoserver/wms", {
				LAYERS : 'CSDILA_local:gps_inwmml',
				STYLES : '',
				format : 'image/png',
				tiled : true,
				transparent : true,
				tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom
			}, {
				buffer : 0,
				displayOutsideMaxExtent : true,
				projection : mercator,
				reproject : true
			});
	lyr_GP.setIsBaseLayer(false);
	lyr_GP.setVisibility(false);
	
	
	wfs = new OpenLayers.Layer.Vector(
			"gp-wfs",
			{
				strategies : [ new OpenLayers.Strategy.BBOX(), saveStrategy ],
				projection : mercator,
				styleMap : '',
				protocol : new OpenLayers.Protocol.WFS(
						{
							version : "1.1.0",
							srsName : "EPSG:900913",
							url : "/health-demonstrator/geoserver/wfs",
							featureType : "gps_inwmml",
							geometryName : "geom",
							schema : "/health-demonstrator/geoserver/wfs/DescribeFeatureType?version=1.1.0&typename=CSDILA_local:gps_inwmml"
						})
			});

	map.addLayers([osm,lyr_results, lyr_SEIFA, lyr_GP, wfs]);

	map.setCenter(new OpenLayers.LonLat(16133371, -4544265), 12);

};
window['map_init']();