{
  "about":{
     "abstract":"Consorzio LaMMA",
     "contact":"<a href='http://www.lamma.rete.toscana.it/'>http://www.lamma.rete.toscana.it/<\/a>.",
     "title":"Dati Meteo - Consorzio LaMMA"
  },
  "defaultSourceType":"gxp_wmssource",
  "isLoadedFromConfigFile":true,
  "appType":"private",
  "map":{                   
    "plugins": [
    {
        "ptype": "gxp_loadingindicator",
        "onlyShowOnFirstLoad": true
    }
    ],
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
           "name":"HYBRID",
           "selected":false,
           "source":"google",
           "title":"Google Hybrid",
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
        }

	,
        {
            "format":"image/png",
            "group":"MSG1",
            "name":"MSG1:MSG1_Channel_05",
            "opacity":1,
            "selected":false,
            "source":"MSG1",
            "styles":["raster"],
            "title":"MSG1 MSG1_Channel_05",
            "transparent":true,
            "uuid":"598d39d7-fa2d-4b51-850e-6623f392f5b3",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG1",
            "name":"MSG1:MSG1_Channel_06",
            "opacity":1,
            "selected":false,
            "source":"MSG1",
            "styles":["raster"],
            "title":"MSG1 MSG1_Channel_06",
            "transparent":true,
            "uuid":"598d39d7-fa2d-4b51-850e-6623f392f5b3",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG1",
            "name":"MSG1:MSG1_Channel_09",
            "opacity":1,
            "selected":false,
            "source":"MSG1",
            "styles":["raster"],
            "title":"MSG1 MSG1_Channel_09",
            "transparent":true,
            "uuid":"598d39d7-fa2d-4b51-850e-6623f392f5b3",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG1",
            "name":"MSG1:MSG1_Channel_12",
            "opacity":1,
            "selected":false,
            "source":"MSG1",
            "styles":["raster"],
            "title":"MSG1 MSG1_Channel_12",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },
        {
            "format":"image/png",
            "group":"MSG2",
            "name":"MSG2:MSG2_Airmass",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["raster"],
            "title":"MSG2_Airmass",
            "transparent":true,
            "uuid":"734a9e42-3df7-4c11-bf30-61155c3ac415",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG2",
            "name":"MSG2:MSG2_NatColours",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["raster"],
            "title":"MSG2_NatColours",
            "transparent":true,
            "uuid":"034bc391-2cac-46f4-a10a-702e56298a12",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG2",
            "name":"MSG2:MSG2_Dust",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["raster"],
            "title":"MSG2_Dust",
            "transparent":true,
            "uuid":"7cf877c4-f8b2-4b3d-a4e3-451522125175",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG2",
            "name":"MSG2:MSG2_Channel_05",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["raster"],
            "title":"MSG2_Channel_05",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG2",
            "name":"MSG2:MSG2_Channel_06",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["raster"],
            "title":"MSG2_Channel_06",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/png",
            "group":"MSG2",
            "name":"MSG2:MSG2_Channel_10",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["raster"],
            "title":"MSG2_Channel_10",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
	   "format":"image/png8",
           "group":"RADAR VMI",
           "name":"RADAR:VMI",
           "opacity":1,
           "selected":false,
           "source":"RADAR", 
           "styles":["VMI"],
           "title":"VMI",
           "transparent":true,
           "uuid":"72ef8ff1-1506-49ae-95ad-7c229f3f282c",
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913"
       },{
           "format":"image/png8",
           "group":"RADAR CAPPI",
           "name":"RADAR:CAPPI",
           "opacity":1,
           "selected":false,
           "source":"RADAR", 
           "styles":["CAPPI_2000.0"],
           "title":"CAPPI_2000.0",
           "transparent":true,
           "uuid":"ff6970ed-f409-4a8e-9ce7-85554d45a62e",
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"2000.0"
	}
	,{
           "format":"image/png8",
           "group":"RADAR CAPPI",
           "name":"RADAR:CAPPI",
           "opacity":1,
           "selected":false,
           "source":"RADAR", 
           "styles":["CAPPI_3000.0"],
           "title":"CAPPI_3000.0",
           "transparent":true,
           "uuid":"ff6970ed-f409-4a8e-9ce7-85554d45a62e",
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"3000.0"
	}
	,{
           "format":"image/png8",
           "group":"RADAR CAPPI",
           "name":"RADAR:CAPPI",
           "opacity":1,
           "selected":false,
           "source":"RADAR", 
           "styles":["CAPPI_5000.0"],
           "title":"CAPPI_5000.0",
           "transparent":true,
           "uuid":"ff6970ed-f409-4a8e-9ce7-85554d45a62e",
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"5000.0"
	}

	,{
	   "format":"image/png8",
           "group":"RADAR SRI",
           "name":"RADAR:SRI",
           "opacity":1,
           "selected":false,
           "source":"RADAR", 
           "styles":["SRI"],
           "title":"SRI",
           "transparent":true,
           "uuid":"bd08cc8b-0d45-485c-9faa-74264376bb0f",
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
	}

	,{
	   "format":"image/png8",
           "group":"RADAR SRT",
           "name":"RADAR:SRT",
           "opacity":1,
           "selected":false,
           "source":"RADAR", 
           "styles":["SRT"],
           "title":"SRT",
           "transparent":true,
           "uuid":"e96febb2-83cd-42a9-942a-cee6b0865351",
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
	}<#list event as root>
	   <#-- NO MATCHES == NO CONTOUR -->
	   <#if !( root.LAYERNAME?matches('.*Wind.*') )>
	      <#if root.ELEVATION_DOMAIN?? >
		  <#list root.ELEVATION_DOMAIN as ELEVATION >

	,{
           "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${root.LAYERNAME?substring(root.LAYERNAME?last_index_of("_")+1,root.LAYERNAME?last_index_of("T"))}",<#-- FIXED -->
           "name":"${root.WORKSPACE}:${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_${ELEVATION}"],
           "title":"${root.LAYERNAME?substring(8,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${ELEVATION}",
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
           "group":"${root.WORKSPACE} ${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${root.LAYERNAME?substring(root.LAYERNAME?last_index_of("_")+1,root.LAYERNAME?last_index_of("T"))}",<#-- FIXED -->
           "name":"${root.WORKSPACE}:${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}"],
           "title":"${root.LAYERNAME?substring(8,root.LAYERNAME?last_index_of("_"))?replace("_"," ")}",
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
           "group":"${root.WORKSPACE} ${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${root.LAYERNAME?substring(root.LAYERNAME?last_index_of("_")+1,root.LAYERNAME?last_index_of("T"))}",<#-- FIXED -->
           "name":"${root.WORKSPACE}:${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_${ELEVATION}"],
           "title":"${root.LAYERNAME?substring(8,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${ELEVATION}",
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
           "group":"${root.WORKSPACE} ${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${root.LAYERNAME?substring(root.LAYERNAME?last_index_of("_")+1,root.LAYERNAME?last_index_of("T"))}",<#-- FIXED -->
           "name":"${root.WORKSPACE}:${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_${ELEVATION}_contour"],
           "title":"${root.LAYERNAME?substring(8,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${ELEVATION} direction",
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
           "group":"${root.WORKSPACE} ${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${root.LAYERNAME?substring(root.LAYERNAME?last_index_of("_")+1,root.LAYERNAME?last_index_of("T"))}",<#-- FIXED -->
           "name":"${root.WORKSPACE}:${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}"],
           "title":"${root.LAYERNAME?substring(8,root.LAYERNAME?last_index_of("_"))?replace("_"," ")}",
           "transparent":true,
           <#if root.GN_UUID??>"uuid":"${root.GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
	}
	,{
           "format":"image/png8",
           "group":"${root.WORKSPACE} ${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} ${root.LAYERNAME?substring(root.LAYERNAME?last_index_of("_")+1,root.LAYERNAME?last_index_of("T"))}",<#-- FIXED -->
           "name":"${root.WORKSPACE}:${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_contour"],
           "title":"${root.LAYERNAME?substring(8,root.LAYERNAME?last_index_of("_"))?replace("_"," ")} direction",
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
                       "format":"image/gif",
                       "group":"Stazioni",
                       "name":"lamma_stazioni:prec60_web",
                       "opacity":1,
                       "selected":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":[],
                       "title":"Pioggia oraria",
                       "transparent":true,
                       "visibility":true,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "transitionEffect":"resize",
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"lamma_stazioni:prec360_web",
                       "opacity":1,
                       "selected":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":[],
                       "title":"Pioggia 6 ore",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "transitionEffect":"resize",
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"lamma_stazioni:temparia_web",
                       "opacity":1,
                       "selected":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":[],
                       "title":"Temperatura oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "transitionEffect":"resize",
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"lamma_stazioni:pres_web",
                       "opacity":1,
                       "selected":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":[],
                       "title":"Pressione oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "transitionEffect":"resize",
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"lamma_stazioni:umid_web",
                       "opacity":1,
                       "selected":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":[],
                       "title":"Umidit&agrave; oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "transitionEffect":"resize",
                       "elevation":"0.0"
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"lamma_stazioni:vent_web",
                       "opacity":1,
                       "selected":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":[],
                       "title":"Vento orario",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "transitionEffect":"resize",
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
        "url":"http://159.213.57.108/geoserver/ows?namespace=${event[0].WORKSPACE}"
     },</#if>
     "LaMMA_Stazioni":{
        "ptype": "gxp_wmssource",
        "title":"LaMMA Stazioni",
        "layerBaseParams":{
           "TILED":false,
           "TILESORIGIN":"-20037508.34,-20037508.34"
        },
        "url":"http://159.213.57.108/geoserver/ows?namespace=lamma_stazioni"
     },
    "MSG1":{
        "ptype":"gxp_wmssource",
        "title":"LaMMA MSG1",
        "layerBaseParams":{
            "TILED":true,
            "TILESORIGIN":"-20037508.34,-20037508.34"
        },
        "url":"http://159.213.57.108/geoserver/ows?namespace=MSG1"
    },
    "MSG2":{
        "ptype":"gxp_wmssource",
        "title":"LaMMA MSG2",
        "layerBaseParams":{
            "TILED":true,
            "TILESORIGIN":"-20037508.34,-20037508.34"
            },
        "url":"http://159.213.57.108/geoserver/ows?namespace=MSG2"
    },
    "RADAR":{
        "ptype": "gxp_wmssource",
        "title":"LaMMA RADAR",
        "layerBaseParams":{
           "TILED":true,
           "TILESORIGIN":"-20037508.34,-20037508.34"
        },
        "url":"http://159.213.57.108/geoserver/ows?namespace=RADAR"
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
                 "FORMAT":"image/png",
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