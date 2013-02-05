Ext.namespace('org.aurin.demonstratortools.health',
		'org.aurin.demonstratortools.health.constants',
		'org.aurin.demonstratortools.health.constants.operator',
		'org.aurin.demonstratortools.health.model',
		'org.aurin.demonstratortools.health.constants.UIOutputModel');
org.aurin.demonstratortools.health.init = function() {
	// Map
	var controls = [ new OpenLayers.Control.LayerSwitcher(),
		         		new OpenLayers.Control.Zoom() ];
		         map = new OpenLayers.Map("map", {
		         	controls : controls
		         });
		         var osm = new OpenLayers.Layer.OSM();
		         map.addLayers([ osm ]);
		         map.setCenter(new OpenLayers.LonLat(16093371, -4537265), 15);
		         
	// constants
	org.aurin.demonstratortools.health.constants.operator.LESS_THAN = "LESS_THAN";
	org.aurin.demonstratortools.health.constants.operator.EQUAL_OR_LESS_THAN = "EQUAL_OR_LESS_THAN";
	org.aurin.demonstratortools.health.constants.operator.EQUAL_OR_GREATER_THAN = "EQUAL_OR_GREATER_THAN";
	org.aurin.demonstratortools.health.constants.operator.GREATER_THAN = "GREATER_THAN";
	org.aurin.demonstratortools.health.constants.operator.EQUAL = "EQUAL";
	//
	org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME = "METRIC_NAME";
	org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION = "METRIC_INCLUSION";
	org.aurin.demonstratortools.health.constants.UIOutputModel.OPERATOR = "OPERATOR";
	org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_VALUE = "METRIC_VALUE";

	org.aurin.demonstratortools.health.constants.UIOutputModel.SEIFA_METRIC = "SEIFA_METRIC";
	org.aurin.demonstratortools.health.constants.UIOutputModel.TYPE2_DIABETES = "TYPE2_DIABETES";
	org.aurin.demonstratortools.health.constants.UIOutputModel.DEPRESSION = "DEPRESSION";
	org.aurin.demonstratortools.health.constants.UIOutputModel.OBESITY = "OBESITY";
	org.aurin.demonstratortools.health.constants.UIOutputModel.SMOKING = "SMOKING";
	org.aurin.demonstratortools.health.constants.UIOutputModel.NO_ACCESS_TO_GENERAL_PRACTICE = "NO_ACCESS_TO_GENERAL_PRACTICE";
	org.aurin.demonstratortools.health.constants.UIOutputModel.BULK_BILLING_AND_FEE_BASED_SERVICE = "BULK_BILLING_AND_FEE_BASED_SERVICE";
	org.aurin.demonstratortools.health.constants.UIOutputModel.BULK_BILLING_ONLY = "BULK_BILLING_ONLY";
	org.aurin.demonstratortools.health.constants.UIOutputModel.FEE_ONLY = "FEE_ONLY";
	org.aurin.demonstratortools.health.constants.UIOutputModel.AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS = "AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS";
	org.aurin.demonstratortools.health.constants.UIOutputModel.AFTER_8_PM_ON_WEEKDAYS = "AFTER_8_PM_ON_WEEKDAYS";
	org.aurin.demonstratortools.health.constants.UIOutputModel.ANY_SATURDAY_SERVICE_AFTER_12_NOON = "ANY_SATURDAY_SERVICE_AFTER_12_NOON";
	org.aurin.demonstratortools.health.constants.UIOutputModel.ANY_SUNDAY_SERVICE = "ANY_SUNDAY_SERVICE";
	org.aurin.demonstratortools.health.constants.UIOutputModel.COMMUNITY_HEALTH_CENTRE = "COMMUNITY_HEALTH_CENTRE";
	org.aurin.demonstratortools.health.constants.UIOutputModel.MENTAL_HEALTH_SERVICE_PROVIDER = "MENTAL_HEALTH_SERVICE_PROVIDER";

	// initializaiton
	org.aurin.demonstratortools.health.model.operator_store = [
			[ org.aurin.demonstratortools.health.constants.operator.LESS_THAN,
					"<" ],
			[
					org.aurin.demonstratortools.health.constants.operator.EQUAL_OR_LESS_THAN,
					"<=" ],
			[
					org.aurin.demonstratortools.health.constants.operator.EQUAL_OR_GREATER_THAN,
					"=>" ],
			[
					org.aurin.demonstratortools.health.constants.operator.GREATER_THAN,
					">" ],
			[ org.aurin.demonstratortools.health.constants.operator.EQUAL, "=" ] ];

	window['generateURL'] = function(_serviceName) {
		// :todo it needs to be configured based on server URL matching
		// bahaviour
		var _url = "";
		// _url= Ext.String.format('/agent-walkability/{0}', _serviceName);
		_url = Ext.String.format('{0}', _serviceName);
		return _url
	}

	org.aurin.demonstratortools.health.prepareUIParameteres = function() {
		var _UIObject = [];
		var _componentValue;
		// SEIFA
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.SEIFA_METRIC;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox11').getValue();
		if (Ext.getCmp('checkbox11').getValue()) {
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.OPERATOR] = Ext
					.getCmp('combo11').getValue();
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_VALUE] = Ext
					.getCmp('slider_value11').getValue();
		}
		_UIObject.push(_componentValue);

		// TYPE 2 Diabetes
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.TYPE2_DIABETES;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox12').getValue();
		if (Ext.getCmp('checkbox12').getValue()) {
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.OPERATOR] = Ext
					.getCmp('combo12').getValue();
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_VALUE] = Ext
					.getCmp('slider_value12').getValue();
		}
		_UIObject.push(_componentValue);

		// Depression
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.DEPRESSION;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox13').getValue();
		if (Ext.getCmp('checkbox13').getValue()) {
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.OPERATOR] = Ext
					.getCmp('combo13').getValue();
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_VALUE] = Ext
					.getCmp('slider_value13').getValue();
		}
		_UIObject.push(_componentValue);

		// Obesity
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.OBESITY;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox14').getValue();
		if (Ext.getCmp('checkbox14').getValue()) {
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.OPERATOR] = Ext
					.getCmp('combo14').getValue();
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_VALUE] = Ext
					.getCmp('slider_value14').getValue();
		}
		_UIObject.push(_componentValue);

		// Smoking
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.SMOKING;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox15').getValue();
		if (Ext.getCmp('checkbox15').getValue()) {
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.OPERATOR] = Ext
					.getCmp('combo15').getValue();
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_VALUE] = Ext
					.getCmp('slider_value15').getValue();
		}
		_UIObject.push(_componentValue);

		// No Access to General Practice
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.NO_ACCESS_TO_GENERAL_PRACTICE;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox21').getValue();
		if (Ext.getCmp('checkbox21').getValue()) {
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.OPERATOR] = Ext
					.getCmp('combo21').getValue();
			_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_VALUE] = Ext
					.getCmp('slider_value21').getValue();
		}
		_UIObject.push(_componentValue);

		//	
		// Bulk billing and fee based service
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.BULK_BILLING_AND_FEE_BASED_SERVICE;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox22').getValue();
		_UIObject.push(_componentValue);
		// Bulk billing only
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.BULK_BILLING_ONLY;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox23').getValue();
		_UIObject.push(_componentValue);
		// Fee only
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.FEE_ONLY;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox24').getValue();
		_UIObject.push(_componentValue);
		// After 5 up until 8 PM on weekdays
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.AFTER_5_UP_UNTIL_8_PM_ON_WEEKDAYS;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox25').getValue();
		_UIObject.push(_componentValue);
		// After 8 PM on weekdays
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.AFTER_8_PM_ON_WEEKDAYS;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox26').getValue();
		_UIObject.push(_componentValue);
		// Any Saturday service after 12 noon
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.ANY_SATURDAY_SERVICE_AFTER_12_NOON;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox27').getValue();
		_UIObject.push(_componentValue);
		// Any Sunday service
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.ANY_SUNDAY_SERVICE;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox28').getValue();
		_UIObject.push(_componentValue);
		// Community Health Centre
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.COMMUNITY_HEALTH_CENTRE;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox29').getValue();
		_UIObject.push(_componentValue);
		// Mental Health Service Provider
		_componentValue = {};
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_NAME] = org.aurin.demonstratortools.health.constants.UIOutputModel.MENTAL_HEALTH_SERVICE_PROVIDER;
		_componentValue[org.aurin.demonstratortools.health.constants.UIOutputModel.METRIC_INCLUSION] = Ext
				.getCmp('checkbox30').getValue();
		_UIObject.push(_componentValue);
		return _UIObject;

	}

	
}
