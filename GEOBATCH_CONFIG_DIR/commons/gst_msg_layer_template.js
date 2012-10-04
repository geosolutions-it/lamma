<#function getRuntime(root)>
  <#if root.TIME_DOMAIN??>
    <#return root.TIME_DOMAIN[0]>
  </#if>
</#function>
{
           "format":"image/png",
           "group":"${WORKSPACE}",<#-- FIXED -->
           "name":"${WORKSPACE}:${LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
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
}
        