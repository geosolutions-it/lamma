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
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Airmass",
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
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_NatColours",
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
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Dust",
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
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_01",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_01"],
            "title":"MSG2_Channel_01",
            "transparent":true,
            "uuid":"598d39d7-fa2d-4b51-850e-6623f392f5b3",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_02",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_02"],
            "title":"MSG2_Channel_02",
            "transparent":true,
            "uuid":"598d39d7-fa2d-4b51-850e-6623f392f5b3",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_03",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_03"],
            "title":"MSG2_Channel_03",
            "transparent":true,
            "uuid":"598d39d7-fa2d-4b51-850e-6623f392f5b3",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_04",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_04"],
            "title":"MSG2_Channel_04",
            "transparent":true,
            "uuid":"598d39d7-fa2d-4b51-850e-6623f392f5b3",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_05",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_05"],
            "title":"MSG2_Channel_05",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_06",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_06"],
            "title":"MSG2_Channel_06",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_07",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_07"],
            "title":"MSG2_Channel_07",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_08",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_08"],
            "title":"MSG2_Channel_08",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_09",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_09"],
            "title":"MSG2_Channel_09",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_10",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_10"],
            "title":"MSG2_Channel_10",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_11",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_11"],
            "title":"MSG2_Channel_11",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG2",
            "name":"MSG2_Channel_12",
            "opacity":1,
            "selected":false,
            "source":"MSG2",
            "styles":["msg2_channel_12"],
            "title":"MSG2_Channel_12",
            "transparent":true,
            "uuid":"9e100217-29c5-4f86-9be6-728736ab6fa0",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Airmass",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["raster"],
            "title":"MSG3_Airmass",
            "transparent":true,
            "uuid":"734a9e42-3df7-4c11-bf30-61155c3ac415",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_NatColours",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["raster"],
            "title":"MSG3_NatColours",
            "transparent":true,
            "uuid":"034bc391-2cac-46f4-a10a-702e56298a12",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Dust",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["raster"],
            "title":"MSG3_Dust",
            "transparent":true,
            "uuid":"7cf877c4-f8b2-4b3d-a4e3-451522125175",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_01",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_01"],
            "title":"MSG3_Channel_01",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_02",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_02"],
            "title":"MSG3_Channel_02",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_03",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_03"],
            "title":"MSG3_Channel_03",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_04",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_04"],
            "title":"MSG3_Channel_04",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_05",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_05"],
            "title":"MSG3_Channel_05",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_06",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_06"],
            "title":"MSG3_Channel_06",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_07",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_07"],
            "title":"MSG3_Channel_07",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_08",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_08"],
            "title":"MSG3_Channel_08",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_09",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_09"],
            "title":"MSG3_Channel_09",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_10",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_10"],
            "title":"MSG3_Channel_10",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_11",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_11"],
            "title":"MSG3_Channel_11",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
            "visibility":false,
            "ratio":1,
            "srs":"EPSG:900913",
            "transitionEffect":"resize"
        },{
            "format":"image/gif",
            "group":"MSG3",
            "name":"MSG3_Channel_12",
            "opacity":1,
            "selected":false,
            "source":"MSG3",
            "styles":["msg3_channel_12"],
            "title":"MSG3_Channel_12",
            "transparent":true,
            "uuid":"8219b507-b5c0-4bce-bab2-c587edd68376",
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
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_${ELEVATION}"],
           "style":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_${ELEVATION}"],
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
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}"],
           "style":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}"],
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
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_${ELEVATION}"],
           "style":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}_${ELEVATION}"],
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
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["wind_arrow_palette"],
           "style":["wind_arrow_palette_raster"],
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
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}"],
           "style":["${root.LAYERNAME?substring(0,root.LAYERNAME?last_index_of("_"))}"],
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
           "name":"${root.LAYERNAME}",<#-- FIXED -->
           "opacity":1,
           "selected":false,
           "source":"${root.WORKSPACE}", <#-- FIXED -->
           "styles":["wind_arrow_palette"],
           "style":["wind_arrow_palette_raster"],
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
                       "displayInLayerSwitcher": true
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
                       "displayInLayerSwitcher": true
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
                       "displayInLayerSwitcher": true
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
                       "displayInLayerSwitcher": true
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"temparia_web",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["temperatura"],
                       "style":["temperatura"],
                       "title":"Temperatura (\u00b0C) freq. oraria",
                       "transparent":true,
                       "visibility":true,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "temparia_web",
                       "graphAttribute": "temp_c",
                       "cumulative": false,                        
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"umid_web",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["umidita"],
                       "style":["umidita"],
                       "title":"Umidit\u00e0 relativa (%) freq. oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "umid_web",
                       "graphAttribute": "umid_per",                       
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"umid_web",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["temperatura_rug"],
                       "style":["temperatura_rug"],
                       "title":"Temperatura di rugiada (\u00b0C) freq. oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "umid_web",
                       "graphAttribute": "trug_c",                       
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"vent_web",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["vento"],
                       "style":["vento"],
                       "title":"Vento - velocit\u00e0 (m/s) e direzione (0 - 360) freq. oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "vent_web",
                       "graphAttribute": "vven_ms",
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"pres_web",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["pressione"],
                       "style":["pressione"],
                       "title":"Pressione s.l.m. (hPa) - freq. oraria",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "pres_web",
                       "graphAttribute": "pres_hpa",
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"prec360_web",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["heatmap_pioggia"],
                       "style":["heatmap_pioggia"],
                       "title":"Pioggia cum. 6 h (mm)",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "prec360_web",
                       "graphAttribute": "prec_mm",
                       "cumulative": true, 
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"prec60_web",
                       "opacity":0.9,
                       "buffer": 2,
                       "selected":false,
                       "tiled":false,
                       "source":"LaMMA_Stazioni", 
                       "styles":["heatmap_pioggia"],
                       "style":["heatmap_pioggia"],
                       "title":"Pioggia cum. 1 h (mm)",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "prec60_web",
                       "graphAttribute": "prec_mm",
                       "cumulative": true,                        
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    },{
                       "format":"image/png8",
                       "group":"Stazioni",
                       "name":"prec15_web",
                       "opacity":0.9,
                       "selected":false,
                       "tiled":false,                       
                       "source":"LaMMA_Stazioni", 
                       "styles":["heatmap_pioggia"],
                       "style":["heatmap_pioggia"],
                       "title":"Pioggia cum. 15min (mm)",
                       "transparent":true,
                       "visibility":false,
                       "ratio":1,
                       "srs":"EPSG:900913",
                       "getGraph": true,
                       "graphTable": "prec15_web",
                       "graphAttribute": "prec_mm",
                       "cumulative": true, 
                       "elevation":"0.0",
                       "restrictedExtent": [1075735.7826,5192220.48427,1381771.78301,5538868.79933]
                    }
     ],
     "maxExtent":["-20037508.34","-20037508.34","20037508.34","20037508.34"],
     "maxResolution": 156543.0339,
     "projection":"EPSG:900913",
     "displayProjection":"EPSG:900913",
     "units":"m",
     "zoom":5
  },
  "modified":false,
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
        "url":"http://geoportale.lamma.rete.toscana.it/geoserver/lamma_stazioni/ows"
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
    "MSG2":{
        "ptype":"gxp_wmssource",
        "title":"LaMMA MSG2",
        "layerBaseParams":{
            "TILED":true,
            "TILESORIGIN":"-20037508.34,-20037508.34"
            },
        "url":"http://geoportale.lamma.rete.toscana.it/geoserver/MSG2/ows"
    },
    "MSG3":{
        "ptype":"gxp_wmssource",
        "title":"LaMMA MSG3",
        "layerBaseParams":{
            "TILED":true,
            "TILESORIGIN":"-20037508.34,-20037508.34"
            },
        "url":"http://geoportale.lamma.rete.toscana.it/geoserver/MSG3/ows"
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