from osgeo.gdalconst import GA_ReadOnly
import gdal
import osr
use_osgeo = False
try:
  from osgeo import _gdal
  use_osgeo = True
except ImportError:
  import _gdal

def GetBlockSize(band):
  if (use_osgeo):
    return band.GetBlockSize()
  else:
    x = _gdal.ptrcreate('int', 0, 2)
    _gdal.GDALGetBlockSize(band._o, x, _gdal.ptradd(x, 1))
    result = (_gdal.ptrvalue(x, 0), _gdal.ptrvalue(x, 1))
    _gdal.ptrfree(x)
    return result


 
def printXY(ds):
    srs = osr.SpatialReference()
    srs.ImportFromWkt(ds.GetProjection())
    srsLatLong = srs.CloneGeogCS()
    ct = osr.CoordinateTransformation(srs,srsLatLong)
    xyz=[]
    rows = ds.RasterYSize
    cols = ds.RasterXSize
    xyz.append(ct.TransformPoint(0,0))
    xyz.append(ct.TransformPoint(0,rows))
    xyz.append(ct.TransformPoint(cols,rows))
    xyz.append(ct.TransformPoint(cols,0))
    xyz.append(ct.TransformPoint(0,0))
    index=1;
    for point3d in xyz:
        print 'P',index,' lat: ',point3d[0],' lon: ',point3d[1]
        index=index+1
        
# Build Spatial Reference object based on coordinate system, fetched from the
    # opened dataset
# http://svn.osgeo.org/gdal/trunk/gdal/swig/python/samples/tolatlong.py
def toLatLon(ds):
    srs = osr.SpatialReference()
    srs.ImportFromWkt(ds.GetProjection())
    srsLatLong = srs.CloneGeogCS()
    ct = osr.CoordinateTransformation(srs, srsLatLong)
    rows = ds.RasterYSize
    cols = ds.RasterXSize
    (int, lat, height) = ct.TransformPoint(0, 0)
    # Report results
#    print('pixel: %g\t\t\tline: %g' % (pixel, line))
    print('latitude: %fd\t\tlongitude: %fd' % (lat, int))
    print('latitude: %s\t\tlongitude: %s' % (gdal.DecToDMS(lat, 'Lat', 2), gdal.DecToDMS(int, 'Long', 2)))

# http://lists.osgeo.org/pipermail/gdal-dev/2010-December/026959.html
def LatLon(ds):
    cols = ds.RasterXSize
    rows = ds.RasterYSize
    geomatrix=ds.GetGeoTransform()
    
    (XUL,YUL,XLR,YLR)=getBB(ds)
    (success, inv_geometrix) = gdal.InvGeoTransform(geomatrix)
#    if (success is not True):
#        print 'invGeoTransform failure'
#        return False
    xUL = inv_geometrix[0] + inv_geometrix[1] * XUL + inv_geometrix[2] * YUL
    yUL = inv_geometrix[3] + inv_geometrix[4] * XUL + inv_geometrix[5] * YUL
    xLR = inv_geometrix[0] + inv_geometrix[1] * XLR + inv_geometrix[2] * YLR
    yLR = inv_geometrix[3] + inv_geometrix[4] * XLR + inv_geometrix[5] * YLR
    print xLR,yLR,xUL,yUL
    xdif = xLR - xUL
    ydif = yUL - yLR
    print xdif,ydif
    
# return (lon,lat)
def getBB(ds):
    (x, deltaX, rotationX, y, rotationY, deltaY) = ds.GetGeoTransform()
    Nx = ds.RasterXSize
    Ny = ds.RasterYSize
    lon = x + deltaX * Nx + rotationX * Ny
    lat = y + rotationY * Nx + deltaY * Ny
    print (x,y,lon,lat)
    return (x,y,lon,lat)


def warp(srcPath, xSize, ySize, src_geomatrix, src_srs, dst_geomatrix, dst_srs, cBlockSize, rBlockSize):
    #http://code.google.com/p/tilers-tools/source/browse/tilers_tools/gdal_tiler.py
    warp_vrt='''<VRTDataset rasterXSize="%(xsize)d" rasterYSize="%(ysize)d" subClass="VRTWarpedDataset">
  <SRS>%(srs)s</SRS>
  %(geo_transform)s
  <VRTRasterBand dataType="Float32" band="1" subClass="VRTWarpedRasterBand" />
  <BlockXSize>%(blxsize)d</BlockXSize>
  <BlockYSize>%(blysize)d</BlockYSize>
  <GDALWarpOptions>
    <!-- <WarpMemoryLimit>6.71089e+07</WarpMemoryLimit> -->
    <ResampleAlg>%(ResampleAlg)s</ResampleAlg>
    <WorkingDataType>Float32</WorkingDataType>
    <SourceDataset relativeToVRT="0">%(src_path)s</SourceDataset>
    <Transformer>
      <ApproxTransformer>
        <MaxError>0.125</MaxError>
        <BaseTransformer>
          <GenImgProjTransformer>
%(src_transform)s
%(dst_transform)s
            <ReprojectTransformer>
              <ReprojectionTransformer>
                <SourceSRS>%(src_srs)s</SourceSRS>
                <TargetSRS>%(dst_srs)s</TargetSRS>
              </ReprojectionTransformer>
            </ReprojectTransformer>
          </GenImgProjTransformer>
        </BaseTransformer>
      </ApproxTransformer>
    </Transformer>
    <BandList>
      <BandMapping src="1" dst="1" />
    </BandList>
    </GDALWarpOptions>
</VRTDataset>
'''
    geotr_templ='  <GeoTransform> %r, %r, %r, %r, %r, %r</GeoTransform>\n'
    warp_dst_geotr= '            <DstGeoTransform> %r, %r, %r, %r, %r, %r</DstGeoTransform>'
    warp_dst_igeotr='            <DstInvGeoTransform> %r, %r, %r, %r, %r, %r</DstInvGeoTransform>'
    warp_src_geotr= '            <SrcGeoTransform> %r, %r, %r, %r, %r, %r</SrcGeoTransform>'
    warp_src_igeotr='            <SrcInvGeoTransform> %r, %r, %r, %r, %r, %r</SrcInvGeoTransform>'

#    if (applyWarp):
#        for b in len(range(inBand)):
#            if (b <> hyResImage):
                # apply warp
    
    (success,inv_src_geomatrix)=gdal.InvGeoTransform(src_geomatrix)
    src_transform='%s\n%s' % (warp_src_geotr % src_geomatrix,warp_src_igeotr % inv_src_geomatrix)
    (success,inv_dst_geomatrix)=gdal.InvGeoTransform(dst_geomatrix)
    dst_transform='%s\n%s' % (warp_dst_geotr % dst_geomatrix,warp_dst_igeotr % inv_dst_geomatrix)

    geotr_txt=geotr_templ % src_geomatrix

    vrt_text=warp_vrt % {
    'xsize':            xSize,
    'ysize':            ySize,
    'geo_transform':    geotr_txt,
    'srs':              dst_srs,
    'blxsize':          cBlockSize,
    'blysize':          rBlockSize,
    'ResampleAlg':   'NearestNeighbour',#GRA_NearestNeighbour
    'src_path':      srcPath,
    'src_srs':       src_srs,
    'dst_srs':       dst_srs,
    'src_transform': src_transform,
    'dst_transform': dst_transform,
    }
            
    #        temp_vrt=os.path.join(self.dest,self.base+'.tmp.vrt') # auxilary VRT file
    #        self.temp_files.append(temp_vrt)
    #        with open(temp_vrt,'w') as f:
    #            f.write(vrt_text)
            # warp base raster

    return vrt_text





######## UNUSED

    # http://www.gdal.org/gdalwarper_8h.html#a4ad252bc084421b47428973a55316421
    #GDALDatasetH GDALCreateWarpedVRT     (     GDALDatasetH      hSrcDS,
    #        int      nPixels,
    #        int      nLines,
    #        double *      padfGeoTransform,
    #        GDALWarpOptions *      psOptions     
    #    )             
    #
    #Create virtual warped dataset.
    #
    #This function will create a warped virtual file representing the input image warped based on a provided transformation. 
    # Output bounds and resolution are provided explicitly.
    #
    #Note that the constructed GDALDatasetH will acquire one or more references to the passed in hSrcDS.
    # Reference counting semantics on the source dataset should be honoured. That is, don't just GDALClose() 
    # it unless it was opened with GDALOpenShared().
    #
    #Parameters:
    #        hSrcDS     The source dataset.
    #        nPixels     Width of the virtual warped dataset to create
    #        nLines     Height of the virtual warped dataset to create
    #        padfGeoTransform     Geotransform matrix of the virtual warped dataset to create
    #        psOptions     Warp options. Must be different from NULL.
    #
    #Returns:
    #    NULL on failure, or a new virtual dataset handle on success.

# http://www.gdal.org/gdalwarper_8h.html#ab5a8723d68786e7554f1ad4c0a6fa8d3
#GDALDatasetH GDALAutoCreateWarpedVRT     (     GDALDatasetH      hSrcDS,
#        const char *      pszSrcWKT,
#        const char *      pszDstWKT,
#        GDALResampleAlg      eResampleAlg,
#        double      dfMaxError,
#        const GDALWarpOptions *      psOptionsIn     
#    )             
#This function will create a warped virtual file representing the input image warped into the target coordinate system.
# A GenImgProj transformation is created to accomplish any required GCP/Geotransform warp and reprojection to the target coordinate system.
# The output virtual dataset will be "northup" in the target coordinate system. The GDALSuggestedWarpOutput() function is used to determine
# the bounds and resolution of the output virtual file which should be large enough to include all the input image
#
#Note that the constructed GDALDatasetH will acquire one or more references to the passed in hSrcDS.
#Reference counting semantics on the source dataset should be honoured. That is, don't just GDALClose() it unless it was opened with GDALOpenShared().
#
#The returned dataset will have no associated filename for itself.
#If you want to write the virtual dataset description to a file, use the GDALSetDescription() function (or SetDescription() method) on
#the dataset to assign a filename before it is closed.
#Parameters:
#        hSrcDS     The source dataset.
#        pszSrcWKT     The coordinate system of the source image. If NULL, it will be read from the source image.
#        pszDstWKT     The coordinate system to convert to. If NULL no change of coordinate system will take place.
#        eResampleAlg     One of GRA_NearestNeighbour, GRA_Bilinear, GRA_Cubic or GRA_CubicSpline. Controls the sampling method used.
#        dfMaxError     Maximum error measured in input pixels that is allowed in approximating the transformation (0.0 for exact calculations).
#        psOptionsIn     Additional warp options, normally NULL.
#Returns:
#    NULL on failure, or a new virtual dataset handle on success.




# ds - dataset to warp
# driver - the driver to use to write the image
# dst_path - path to write the warped image
# dst_wkt - The coordinate system to convert to. If NULL no change of coordinate system will take place.
def warpToFile(ds, driver, dst_path, dst_wkt):
    
    error_threshold = 0.125  # error threshold --> use same value as in gdalwarp
    resampling = gdal.GRA_NearestNeighbour
    
    tmp_ds = gdal.AutoCreateWarpedVRT( ds, \
        None, # src_wkt : left to default value --> will use the one from source \
        dst_wkt, \
        resampling, \
        error_threshold)
    dst_xsize = tmp_ds.RasterXSize
    dst_ysize = tmp_ds.RasterYSize
    dst_gt = tmp_ds.GetGeoTransform()
    tmp_ds = None
    
    # Desfine target SRS
#    dst_srs = osr.SpatialReference()
#    dst_srs.ImportFromWkt(ds.GetProjectionRef())
#    dst_wkt = dst_srs.ExportToWkt()
    
    # Now create the true target dataset
    dst_ds = driver.Create(dst_path, dst_xsize, dst_ysize,ds.RasterCount)
    dst_ds.SetProjection(ds.GetProjectionRef())
    dst_ds.SetGeoTransform(dst_gt)
    
    # And run the reprojection
    cbk=progress_callback
    cbk_user_data = None # value for last parameter of above warp_27_progress_callback
    
    gdal.ReprojectImage( ds, \
        dst_ds, \
        None, # src_wkt : left to default value --> will use the one from source \
        None, # dst_wkt : left to default value --> will use the one from destination \
        resampling, \
        0,  # WarpMemoryLimit : left to default value \
        error_threshold, \
        cbk, # Progress callback : could be left to None or unspecified for silent progress
        cbk_user_data)  # Progress callback user data
    
    # Done !
    dst_ds = None
    
    # Check that we have the same result as produced by 'gdalwarp -rb -t_srs EPSG:4326 ../gcore/data/byte.tif tmp/warp_27.tif'
#    ds = gdal.Open('tmp/warp_27.tif')
#    cs = ds.GetRasterBand(1).Checksum()
#    ds = None
    
def progress_callback(pct, message, user_data):
    #print(pct)
    return 1