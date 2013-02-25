window['map_init'] = function() {
	// Map
	var controls = [  new OpenLayers.Control.Navigation(),
                        new OpenLayers.Control.PanZoomBar(),
                        new OpenLayers.Control.LayerSwitcher({'ascending':false}),
                        new OpenLayers.Control.Permalink(),
                        new OpenLayers.Control.ScaleLine(),
                        new OpenLayers.Control.Permalink('permalink'),
                        new OpenLayers.Control.MousePosition(),
                        new OpenLayers.Control.OverviewMap(),
                        new OpenLayers.Control.KeyboardDefaults()
						];
	map = new OpenLayers.Map('mappanel', {
		 controls : controls
	  });
	var osm = new OpenLayers.Layer.OSM();
	
 //var geographic = new OpenLayers.Projection("EPSG:3111");
 var geographic = new OpenLayers.Projection("EPSG:4326");
var mercator = new OpenLayers.Projection("EPSG:900913");

	window['paths'] = new OpenLayers.Layer.Vector("Region of Interest", {
		 //projection : geographic,
		 projection : mercator,
		strategies : [ new OpenLayers.Strategy.Fixed() ],

		protocol : new OpenLayers.Protocol.HTTP({
			// url: "paths_wgs84.geojson",
			// url : "/service/agent-paths/285752.0/5824386.0",
			// url : "/health-demonstrator/road_sample_wgs84.geojson",
			 url : "/health-demonstrator/service/health-filter/filterResult",
			format : new OpenLayers.Format.GeoJSON()
		}),
		// styleMap : new OpenLayers.StyleMap({
		//	"default" : pathStyle
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
	
		map.addLayers([ osm,window['paths'] ]);
	
 	map.setCenter(new OpenLayers.LonLat(16093371, -4537265), 10);
 /*map.setCenter(
            new OpenLayers.LonLat(-37.52372699908832, 144.583322).transform(
                new OpenLayers.Projection("EPSG:4328"),
                map.getProjectionObject()
            ), 6
        ); */
	////
	/*
	  window['mapPanel'] = new GeoExt.MapPanel({
        title: "GeoExt MapPanel",
        renderTo: "mappanel",
        stateId: "mappanel",
        height: 400,
        width: 600,
        map: map,
        center: new OpenLayers.LonLat(16093371, -4537265),
        zoom: 10,
        // getState and applyState are overloaded so panel size
        // can be stored and restored
        getState: function() {
            var state = GeoExt.MapPanel.prototype.getState.apply(this);
            state.width = this.getSize().width;
            state.height = this.getSize().height;
            return state;
        },
        applyState: function(state) {
            GeoExt.MapPanel.prototype.applyState.apply(this, arguments);
            this.width = state.width;
            this.height = state.height;
        }
    });*/
	
	
}
  window['map_init'] ();