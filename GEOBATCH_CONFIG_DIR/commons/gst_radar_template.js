{
  "about":{
     "abstract":"Consorzio LaMMA",
     "contact":"<a href='http://www.lamma.rete.toscana.it/'>http://www.lamma.rete.toscana.it/<\/a>.",
     "title":"Dati Meteo - Consorzio LaMMA"
  },
  "defaultSourceType":"gxp_wmssource",
  "isLoadedFromConfigFile":true,
  "map":{
    "layoutConfig": {
    "monitorResize": false
    },
     "center":[
        "1250000.0000000",
        "5370000.0000000"
     ],
     "layers":[
        {
           "fixed":true,
           "group":"background",
           "name":"Aerial",
           "selected":false,
           "source":"bing",
           "title":"Bing Aerial",
           "visibility":false
        },
        {
           "fixed":true,
           "group":"background",
           "name":"mapnik",
           "selected":false,
           "source":"osm",
           "title":"Open Street Map",
           "visibility":false
        },
        {
           "fixed":true,
           "group":"background",
           "name":"osm",
           "selected":false,
           "source":"mapquest",
           "title":"MapQuest OpenStreetMap",
           "visibility":false
        },
        {
           "fixed":true,
           "group":"background",
           "name":"ROADMAP",
           "selected":false,
           "source":"google",
           "title":"Google Roadmap",
           "visibility":false
        },
        {
           "fixed":true,
           "group":"background",
           "name":"TERRAIN",
           "opacity":1,
           "selected":false,
           "source":"google",
           "title":"Google Terrain",
           "visibility":true
        },
        {
           "fixed":true,
           "group":"background",
           "name":"HYBRID",
           "selected":false,
           "source":"google",
           "title":"Google Hybrid",
           "visibility":false
        },{
            "source": "ol",
            "group": "background",
            "fixed": true,
            "type": "OpenLayers.Layer",
            "visibility": false,
            "args": [
                "None", {"visibility": false}
            ]
        }
        }<#list event as root>
	   <#-- NO MATCHES == NO CONTOUR -->
	   <#if !( root.LAYERNAME?matches('.*PIPPO.*') )>
	      <#if root.ELEVATION_DOMAIN?? >
		  <#list root.ELEVATION_DOMAIN as ELEVATION >
	,{
           "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME}",<#-- FIXED -->
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME}_${ELEVATION}"],
           "title":"${root.LAYERNAME}_${ELEVATION}",
           "transparent":true,
           <#if root.GN_UUID??>"uuid":"${root.GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"${ELEVATION}"
	}
		  </#list><#-- list ELEVATION -->
	      <#else><#-- ELSE NO ELEVATION_DOMAIN -->
	,{
	   "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME}",<#-- FIXED -->
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME}"],
           "title":"${root.LAYERNAME}",
           "transparent":true,
           <#if root.GN_UUID??>"uuid":"${root.GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
	}
	      </#if><#-- if ELEVATION_DOMAIN -->
	    <#else><#-- else if MATCHES -->
	    	      <#if root.ELEVATION_DOMAIN?? >
		  <#list root.ELEVATION_DOMAIN as ELEVATION >
	,{
           "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME}",<#-- FIXED -->
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME}_${ELEVATION}"],
           "title":"${root.LAYERNAME} ${ELEVATION}",
           "transparent":true,
           <#if root.GN_UUID??>"uuid":"${root.GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"${ELEVATION}"
	}
	,{
           "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME}",<#-- FIXED -->
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME}_${ELEVATION}_contour"],
           "title":"${root.LAYERNAME} ${ELEVATION} contour",
           "transparent":true,
           <#if root.GN_UUID??>"uuid":"${root.GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"${ELEVATION}"
	}
		  </#list><#-- list ELEVATION -->
	      <#else><#-- ELSE NO ELEVATION_DOMAIN -->
	,{
	   "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME}",<#-- FIXED -->
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME}"],
           "title":"${root.LAYERNAME}",
           "transparent":true,
           <#if root.GN_UUID??>"uuid":"${root.GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
	}
	,{
           "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME}",<#-- FIXED -->
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME}_contour"],
           "title":"${root.LAYERNAME} contour",
           "transparent":true,
           <#if root.GN_UUID??>"uuid":"${root.GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
	}
	      </#if><#-- if ELEVATION_DOMAIN -->
	    </#if><#-- if MATCHES -->
        </#list>
,{
                       "format":"image/png",
                       "group": "Limiti Mondiali",
                       "name":"confini_mondiali_stati",
                       "selected":false,                   
                       "source":"LaMMA_confini", 
                       "styles":["confini"],
                       "style":["confini"],
                       "title":"Stati",
                       "transparent":true,
                       "visibility":true,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "queryable": false,
                       "displayInLayerSwitcher": true,
                    },{
                       "format":"image/png",
                       "group": "Limiti Mondiali",
                       "name":"confini_mondiali_regioni",
                       "selected":false,                   
                       "source":"LaMMA_confini", 
                       "styles":["confini"],
                       "style":["confini"],
                       "title":"Regioni",
                       "transparent":true,
                       "visibility":true,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "queryable": false,
                       "displayInLayerSwitcher": true,
                    },{
                       "format":"image/png",
                       "group": "Limiti Mondiali",
                       "name":"confini_mondiali_provincie",
                       "selected":false,                   
                       "source":"LaMMA_confini", 
                       "styles":["confini"],
                       "style":["confini"],
                       "title":"Province",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "queryable": false,
                       "displayInLayerSwitcher": true,
                    },{
                       "format":"image/png",
                       "group": "Limiti Mondiali",
                       "name":"comuni",
                       "selected":false,                   
                       "source":"LaMMA_confini", 
                       "styles":["confini"],
                       "style":["confini"],
                       "title":"Comuni Italia",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "queryable": false,
                       "displayInLayerSwitcher": true,
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"prec60_l",
                       "opacity":0.9,
                       "buffer": 2,
                       "selected":false,
                       "tiled":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":["heatmap_pioggia"],
                       "style":["pioggia"],
                       "title":"Pioggia oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "prec60_l",
                       "graphAttribute": "prec_mm",
                       "cumulative": true,                        
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"prec360_l",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["heatmap_pioggia"],
                       "style":["pioggia"],
                       "title":"Pioggia 6 ore",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "prec360_l",
                       "graphAttribute": "prec_mm",
                       "cumulative": true, 
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"temparia_l",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["temperatura"],
                       "style":["temperatura_legend"],
                       "title":"Temperatura oraria",
                       "transparent":true,
                       "visibility":true,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "temparia_l",
                       "graphAttribute": "temp_c",
                       "cumulative": false,                        
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"umid_l",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["umidita"],
                       "style":["umidita"],
                       "title":"Umidita oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "umid_l",
                       "graphAttribute": "umid_per",                       
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"umid_l",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["temp_rugiada_legend"],
                       "style":["temp_rugiada_legend"],
                       "title":"Temp rugiada oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "umid_l",
                       "graphAttribute": "trug_c",                       
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"vent_l",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["vento"],
                       "style":["vento"],
                       "title":"Vento",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "vent_l",
                       "graphAttribute": "vven_ms",
                       "elevation":"0.0"
                    }         
     ],
     "maxExtent":["-20037508.34","-20037508.34","20037508.34","20037508.34"],
     "maxResolution": 156543.0339,
     "projection":"EPSG:900913",
     "displayProjection":"EPSG:900913",
     "units":"m",
     "zoom":2
  },
  "modified":false,
  "printService":"http://demo1.geo-solutions.it/geoserver/pdf/",
  "proxy":"/proxy/?url=",
  "renderToTab":"appTabs",
  "sources":{
            <#if event[0]??>"${event[0].WORKSPACE}":{
                "ptype": "gxp_wmssource",
                "title":"LaMMA ${event[0].WORKSPACE}",
                "layerBaseParams":{
                    "TILED":true,
                    "TILESORIGIN":"-20037508.34,-20037508.34"
                },
                "url":"http://geoportale.lamma.rete.toscana.it/geoserver/${event[0].WORKSPACE}/ows"
            },</#if>
             "LaMMA_Stazioni":{
                "ptype": "gxp_wmssource",
                "title":"LaMMA Stazioni",
                "layerBaseParams":{
                   "TILED":false,
                   "TILESORIGIN":"-20037508.34,-20037508.34"
                },
                "url":"http://geoportale.lamma.rete.toscana.it/geoserver/lamma_staz/ows"
             },
             "LaMMA_confini":{
                "ptype": "gxp_wmssource",
                "title":"LaMMA Confini",
                "layerBaseParams":{
                   "TILED":false,
                   "TILESORIGIN":"-20037508.34,-20037508.34"
                },
                "url":"http://geoportale.lamma.rete.toscana.it/geowebcache/service/wms"
             },     
     "bing":{
        "projection":"EPSG:900913",
        "ptype":"gxp_bingsource"
     },
     "google":{
        "projection":"EPSG:900913",
        "ptype":"gxp_googlesource"
     },
     "mapquest":{
        "projection":"EPSG:900913",
        "ptype":"gxp_mapquestsource"
     },
     "ol":{
        "projection":"EPSG:900913",
        "ptype":"gxp_olsource"
     },
     "osm":{
        "projection":"EPSG:900913",
        "ptype":"gxp_osmsource"
     }
  },
  "tools":[
     {
        "outputConfig":{
           "id":"layertree"
        },
        "outputTarget":"tree",
        "ptype":"gxp_layertree"
     },
     {
        "legendConfig":{
           "defaults":{
              "baseParams":{
                 "FORMAT":"image/png8",
                 "HEIGHT":12,
                 "LEGEND_OPTIONS":"forceLabels:on;fontSize:10",
                 "WIDTH":12
              },
              "style":"padding:5px"
           },
           "legendPanelId":"legendPanel"
        },
        "outputConfig":{
           "autoScroll":true,
           "title":"Show Legend"
        },
        "outputTarget":"legend",
        "ptype":"gxp_legend"
     },
     {
        "actionTarget":"tree.tbar",
        "ptype":"gxp_addlayers",
        "upload":true
     },
     {
        "actionTarget":[
           "tree.tbar",
           "layertree.contextMenu"
        ],
        "ptype":"gxp_removelayer"
     },
     {
        "actionTarget":"tree.tbar",
        "ptype":"gxp_removeoverlays"
     },
     {
        "actionTarget":"tree.tbar",
        "ptype":"gxp_addgroup"
     },
     {
        "actionTarget":"tree.tbar",
        "ptype":"gxp_removegroup"
     },
     {
        "actionTarget":[
           "tree.tbar"
        ],
        "ptype":"gxp_groupproperties"
     },
     {
        "actionTarget":[
           "tree.tbar",
           "layertree.contextMenu"
        ],
        "ptype":"gxp_layerproperties"
     },
     {
        "actionTarget":{
           "index":0,
           "target":"layertree.contextMenu"
        },
        "ptype":"gxp_zoomtolayerextent"
     },
     {
        "actionTarget":[
           "layertree.contextMenu"
        ],
        "ptype":"gxp_geonetworksearch"
     },
     {
        "actionTarget":{
           "index":15,
           "target":"paneltbar"
        },
        "ptype":"gxp_navigation",
        "toggleGroup":"toolGroup"
     },
     {
        "actionTarget":{
           "index":7,
           "target":"paneltbar"
        },
        "ptype":"gxp_wmsgetfeatureinfo",
        "toggleGroup":"toolGroup"
     },
     {
        "actionTarget":{
           "index":12,
           "target":"paneltbar"
        },
        "ptype":"gxp_measure",
        "toggleGroup":"toolGroup"
     },
     {
        "actionTarget":{
           "index":20,
           "target":"paneltbar"
        },
        "ptype":"gxp_zoom"
     },
     {
        "actionTarget":{
           "index":24,
           "target":"paneltbar"
        },
        "ptype":"gxp_zoombox",
        "toggleGroup":"toolGroup"
     },
     {
        "actionTarget":{
           "index":22,
           "target":"paneltbar"
        },
        "ptype":"gxp_navigationhistory"
     },
     {
        "actionTarget":{
           "index":26,
           "target":"paneltbar"
        },
        "ptype":"gxp_zoomtoextent"
     },
     {
        "actionTarget":{
           "index":40,
           "target":"paneltbar"
        },
        "needsAuthorization":true,
        "ptype":"gxp_saveDefaultContext"
     },
     {
        "actionTarget":{
           "index":4,
           "target":"paneltbar"
        },
        "customParams":{
           "outputFilename":"fdh-print"
        },
        "legendPanelId":"legendPanel",
        "printService":"http://demo1.geo-solutions.it/geoserver/pdf/",
        "ptype":"gxp_print"
     }, {
        "ptype":"gxp_playback"
    }
  ],
  "viewerTools":[

  ],
  "xmlJsonTranslateService":"http://demo1.geo-solutions.it/xmlJsonTranslate/"
}
