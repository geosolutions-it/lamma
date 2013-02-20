<#function getRuntime(root)>
  <#if root.TIME_DOMAIN??>
    <#return root.TIME_DOMAIN[0]>
  </#if>
</#function>
{
           "format":"image/gif",
           "group":"${WORKSPACE}",<#-- FIXED -->
           "name":"${WORKSPACE}:${LAYERNAME}",<#-- FIXED -->
           "opacity":1.0,
           "selected":false,
           "source":"${WORKSPACE}", <#-- FIXED -->
           "styles":["${DEFAULT_STYLE}"],
           "title":"${LAYERNAME}", <#-- FIXED -->
           "transparent":true,
           <#if GN_UUID??>"uuid":"${GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
           <#if ELEVATION_DOMAIN??>,"elevation":"${ELEVATION_DOMAIN[0]}"</#if><#-- FIXED -->
},{
    "format":"image/png",
    "group":"Limiti mondiali",
    "name":"lamma:confini_mondiali",
    "selected":false,                   
    "source":"LaMMA_confini", 
    "styles":["confini"],
    "style":["confini"],
    "title":"Confini",
    "transparent":true,
    "visibility":true,
    "ratio":1,
    "getGraph": false,
    "srs":"EPSG:900913"
    } 
        