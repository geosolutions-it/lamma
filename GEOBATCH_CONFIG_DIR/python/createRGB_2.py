#!/usr/bin/python 
# -*- coding: utf-8 -*-
# Daniele Romagnoli
# Carlo Cancellieri
# Riccardo Mari

# RGB AirMass
#-----------------------------
from osgeo import osr
from osgeo.gdalconst import GA_ReadOnly, GDT_Byte
import os
import sys, time
import utils 
import calc

try:
    from osgeo import gdal
    import numpy
    os.chdir('.')
except ImportError:
    import gdal
    os.chdir('.')



def intersection (geom1, geom2):
#    geom = CreateGeometryFromWkt('POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))')
    #geom2 = CreateGeometryFromWkt('POLYGON((5 0, 5 15, 15 15, 15 5, 5 0))')
    #print geom3
    return geom1.Intersection(geom2)



def warpAndCreateRGB(argv):
    # register all of the GDAL drivers
    gdal.AllRegister()
    
    inDs = []
    inPath = []
    if (len(sys.argv) < 4):
        print 'Usage:'
        print 'python ./createRGB_2.py OutFile.tif Channel_0.tif ... Channel_n.tif'
 
    
    # open channels skipping first 2 args
    # 0 == ./createRGB_X.py    [script file]
    # 1 == OutFile.tif    [output file]
    for image in sys.argv[2:]:
        print 'loding image ...'
        openImage = gdal.Open(image, GA_ReadOnly)
    #    printXY(openImage)
#        LatLon(openImage)
#        toLatLon(openImage)
#        getBB(openImage)
        if openImage is None:
            print 'Could not open ', image
            sys.exit(-1)
            break
        else :
            inDs.append(openImage)
            inPath.append(image)
            print 'loaded image ', image
    
    
    # get the bands and no data
    noData=9999
    inBand = []
    rows = []
    cols = []
    area=-1
    hyResImage=-1
    # if apply warp will be set to true warp will be applied
    applyWarp=False
    for b in range(len(inDs)):
        bands = inDs[0].RasterCount
        if (bands > 1):
            print 'WARNING error more than one band recognized'
        band=inDs[b].GetRasterBand(1)
        inBand.append(band)
        # get image size
        rows.append(inDs[b].RasterYSize)
        cols.append(inDs[b].RasterXSize)
        # calculate the bigger area to select
        # the highest image resolution
        areaBand=rows[b]*cols[b]
        if (area<areaBand):
            area=areaBand
            hyResImage=b
        # check if there are different areas 
        if (not applyWarp and areaBand<>area):
            applyWarp=True
        
        # calculate the nodata
        #TODO warn latest nodata is used -> warp???
        noDataBand=band.GetNoDataValue()
        if (noDataBand<>noData and noDataBand<>None):
            print 'New noData -> ',noDataBand
            noData=noDataBand
    -----------------------------------------------
    blockSizes = GetBlockSize(inBand[0])
    cBlockSize = blockSizes[0]
    rBlockSize = blockSizes[1]
    #src_geomatrix=inDs[hyResImage].GetGeoTransform()
    #dst_geomatrix=inDs[b].GetGeoTransform()
    srcPath=inPath[hyResImage]
    
    vrt_text=utils.warp(srcPath, cols[hyResImage], rows[hyResImage], inDs[hyResImage].GetGeoTransform(), inDs[b].GetProjection(), inDs[hyResImage].GetProjection(), inDs[b].GetGeoTransform(),  cBlockSize, rBlockSize)
    
    print vrt_text
    warped_image=gdal.Open(vrt_text,GA_ReadOnly)
    -----------------------------------------------
    #print yBlockSize, xBlockSize
    
    # create the output image
    driver = inDs[0].GetDriver()
    options = ["TILED=YES", "BLOCKXSIZE=256", "BLOCKYSIZE=256"]
    outMerged = driver.Create(sys.argv[1], cols, rows, 3, GDT_Byte, options)
    if outMerged is None:
        print 'Could not create ', sys.argv[1]
        sys.exit(-1)
    else:
        print 'Writing to ', sys.argv[1]
    
    outBandR = outMerged.GetRasterBand(1)
    outBandG = outMerged.GetRasterBand(2)
    outBandB = outMerged.GetRasterBand(3)

    # loop through the rows
    for r in range(0, rows, rBlockSize):
        # loop through the columns
        for c in range(0, cols, cBlockSize):
        
            values=[]
            # read the data
            for b in len(range(inBand)):
                # read size
                if (r + rBlockSize < rows[b]):
                    numRows = rBlockSize
                else:
                    numRows = rows[b] - r
                
                if (c + cBlockSize < cols[b]):
                    numCols = cBlockSize
                else:
                    numCols = cols[b] - c
#                values.append(inBand[b].ReadAsArray(c+cOffset[b], r+rOffset[b], numCols, numRows).astype(numpy.float32))
            
            # do the calculations    
            rgb = calculateRGB(values)
            
            # write the data
            outBandR.WriteArray(rgb[0], c, r)
            outBandG.WriteArray(rgb[1], c, r)
            outBandB.WriteArray(rgb[2], c, r)

    # flush data to disk, set the NoData value and calculate stats
    outBandR.FlushCache()
    outBandG.FlushCache()
    outBandB.FlushCache()
    
    outBandR.SetNoDataValue(noData)
    outBandG.SetNoDataValue(noData)
    outBandB.SetNoDataValue(noData)
    
    # georeference the image and set the projection
    outMerged.SetGeoTransform(inDs[0].GetGeoTransform())
    
    outMerged.SetProjection(inDs[0].GetProjection())
    
    # build pyramids
    outMerged.BuildOverviews('NEAREST', overviewlist=[2, 4, 8, 16, 32])
    
    for IN in inDs:
#        IN.CloseDS()
        IN = None
    
#    outMerged.CloseDS()
    outMerged = None



#---------------------------------------------------------------------------------

def simpleCreateRGB(argv):
    # register all of the GDAL drivers
    gdal.AllRegister()
    
    inDs = []
    
    if (len(sys.argv) < 4):
        print 'Usage:'
        print 'python ./createRGB_2.py OutFile.tif Channel_0.tif ... Channel_n.tif'
    
    # open channels skipping first 2 args
    # 0 == ./createRGB_X.py	[script file]
    # 1 == OutFile.tif	[output file]
    for image in sys.argv[2:]:
        print 'loding image ...'
        openImage = gdal.Open(image, GA_ReadOnly)
    #    printXY(openImage)
        utils.LatLon(openImage)
        utils.toLatLon(openImage)
        utils.getBB(openImage)
        if openImage is None:
            print 'Could not open ', image
            sys.exit(-1)
            break
        else :
            inDs.append(openImage)
            print 'loaded image ', image
    
    # get image size
    rows = inDs[0].RasterYSize
    cols = inDs[0].RasterXSize
    
    # get the bands and no data
    noData=9999
    inBand = []
    for image in range(len(inDs)):
        band=inDs[image].GetRasterBand(1)
        inBand.append(band)
        bandNoData=band.GetNoDataValue()
        if (bandNoData<>noData and bandNoData<>None):
            print 'New noData -> ',bandNoData
            noData=bandNoData
    
    blockSizes = utils.GetBlockSize(inBand[0])
    cBlockSize = blockSizes[0]
    rBlockSize = blockSizes[1]
    
    # create the output image
    driver = inDs[0].GetDriver()
    options = ["TILED=YES", "BLOCKXSIZE=256", "BLOCKYSIZE=256"]
    outMerged = driver.Create(sys.argv[1], cols, rows, 3, GDT_Byte, options)
    if outMerged is None:
        print 'Could not create ', sys.argv[1]
        return -1
    else:
        print 'Writing to ', sys.argv[1]
    
    outBandR = outMerged.GetRasterBand(1)
    outBandG = outMerged.GetRasterBand(2)
    outBandB = outMerged.GetRasterBand(3)
    
    # loop through the rows
    for r in range(0, rows, rBlockSize):
        # read size
        if (r + rBlockSize < rows):
            numRows = rBlockSize
        else:
            numRows = rows - r
    
        # loop through the columns
        for c in range(0, cols, cBlockSize):
        
            if (c + cBlockSize < cols):
                numCols = cBlockSize
            else:
                numCols = cols - c
    
            values=[]
            # read the data
            for b in range(len(inBand)):
                
                values.append(inBand[b].ReadAsArray(c, r, numCols, numRows).astype(numpy.float32))
            
            # do the calculations    
            rgb = calc.calculateRGB('airmass', values) #TODO
            
            values=None;
            
            # write the data
            outBandR.WriteArray(rgb[0], c, r)
            outBandG.WriteArray(rgb[1], c, r)
            outBandB.WriteArray(rgb[2], c, r)
    
    # flush data to disk, set the NoData value and calculate stats
    outBandR.FlushCache()
    outBandG.FlushCache()
    outBandB.FlushCache()
    
    outBandR.SetNoDataValue(noData)
    outBandG.SetNoDataValue(noData)
    outBandB.SetNoDataValue(noData)
    
    # geo-reference the image and set the projection
    outMerged.SetGeoTransform(inDs[0].GetGeoTransform())
    
    outMerged.SetProjection(inDs[0].GetProjection())
    
    # build pyramids
    outMerged.BuildOverviews('NEAREST', overviewlist=[2, 4, 8, 16, 32])
    
    for IN in inDs:
#        IN.CloseDS()
        IN = None
    
#    outMerged.CloseDS()
    outMerged = None

# MAIN

startTime = time.time()

warpAndCreateRGB(sys.argv)
#ret=simpleCreateRGB(sys.argv)


endTime = time.time()

print "The calc took " + str(endTime-startTime) + " seconds"