           <#-- NO MATCHES == NO CONTOUR -->
           <#if ( LAYERNAME?matches('.*PIPPO.*') )>
              <#if ELEVATION_DOMAIN?? >
                  <#list ELEVATION_DOMAIN as ELEVATION >
        {
           "format":"image/png",
           "group":"${WORKSPACE} ${LAYERNAME}",<#-- FIXED -->
           "name":"${LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${WORKSPACE}", <#-- FIXED -->
           "styles":["${LAYERNAME}_${ELEVATION}"],
           "title":"${LAYERNAME}_${ELEVATION}",
           "transparent":true,
           <#if GN_UUID??>"uuid":"${GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"${ELEVATION}"
        }
                  </#list><#-- list ELEVATION -->
              <#else><#-- ELSE NO ELEVATION_DOMAIN -->
        ,{
           "format":"image/png",
           "group":"${WORKSPACE} ${LAYERNAME}",<#-- FIXED -->
           "name":"${LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${WORKSPACE}", <#-- FIXED -->
           "styles":["${LAYERNAME}"],
           "title":"${LAYERNAME}",
           "transparent":true,
           <#if GN_UUID??>"uuid":"${GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
        }
              </#if><#-- if ELEVATION_DOMAIN -->
            <#else><#-- else if MATCHES -->
                      <#if ELEVATION_DOMAIN?? >
                  <#list ELEVATION_DOMAIN as ELEVATION >
        ,{
           "format":"image/png",
           "group":"${WORKSPACE} ${LAYERNAME}",<#-- FIXED -->
           "name":"${LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${WORKSPACE}", <#-- FIXED -->
           "styles":["${LAYERNAME}_${ELEVATION}"],
           "title":"${LAYERNAME} ${ELEVATION}",
           "transparent":true,
           <#if GN_UUID??>"uuid":"${GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"${ELEVATION}"
        }
        ,{
           "format":"image/png",
           "group":"${WORKSPACE} ${LAYERNAME}",<#-- FIXED -->
           "name":"${LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${WORKSPACE}", <#-- FIXED -->
           "styles":["${LAYERNAME}_${ELEVATION}_contour"],
           "title":"${LAYERNAME} ${ELEVATION} contour",
           "transparent":true,
           <#if GN_UUID??>"uuid":"${GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize",
           "elevation":"${ELEVATION}"
        }
                  </#list><#-- list ELEVATION -->
              <#else><#-- ELSE NO ELEVATION_DOMAIN -->
        ,{
           "format":"image/png",
           "group":"${WORKSPACE} ${LAYERNAME}",<#-- FIXED -->
           "name":"${LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${WORKSPACE}", <#-- FIXED -->
           "styles":["${LAYERNAME}"],
           "title":"${LAYERNAME}",
           "transparent":true,
           <#if GN_UUID??>"uuid":"${GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
        }
        ,{
           "format":"image/png",
           "group":"${WORKSPACE} ${LAYERNAME}",<#-- FIXED -->
           "name":"${LAYERNAME}",<#-- FIXED -->
           "opacity":0.7,
           "selected":false,
           "source":"${WORKSPACE}", <#-- FIXED -->
           "styles":["${LAYERNAME}_contour"],
           "title":"${LAYERNAME} contour",
           "transparent":true,
           <#if GN_UUID??>"uuid":"${GN_UUID}",</#if><#-- UUID from GeoNetwork -->
           "visibility":false,
           "ratio":1,
           "srs":"EPSG:900913",
           "transitionEffect":"resize"
        }
              </#if><#-- if ELEVATION_DOMAIN -->
            </#if><#-- if MATCHES -->
