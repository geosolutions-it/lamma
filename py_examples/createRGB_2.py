#!/usr/bin/python 
# -*- coding: utf-8 -*-
# Daniele Romagnoli
# Carlo Cancellieri
# Riccardo Mari

# RGB AirMass
import os, sys, string, utils
import time
import datetime
from glob import glob

#----------------------------- 

try:
  from osgeo import ogr, gdal
  from osgeo.gdalconst import *
  from math import sqrt
  import numpy
  os.chdir('.')
except ImportError:
  import ogr, gdal
  from gdalconst import *
  from math import sqrt
  import Numeric
  os.chdir('.')

# register all of the GDAL drivers
gdal.AllRegister()

inDs=[]
#		0		1		2						3				4					5
#python ./createRGB_2.py AirMass_Merged.tif MSG2_201109230915_eurafr_WV_062_05.tif MSG2_201109230915_eurafr_WV_073_06.tif MSG2_201109230915_eurafr_IR_097_08.tif MSG2_201109230915_eurafr_IR_108_09.tif

if (len(sys.argv)< 4):
   print 'Usage:'
   print 'python ./createRGB_2.py OutFile.tif Channel_0.tif ... Channel_n.tif'

# open channels skipping first 2 args
# 0 == ./createRGB_X.py	[script file]
# 1 == OutFile.tif	[output file]
for image in sys.argv[2:]:
  print 'loding image ...'
  openImage=gdal.Open(image, GA_ReadOnly)  
  if openImage is None:
      print 'Could not open ',image
      sys.exit(-1)
      break
  else :
      inDs.append(openImage)
      print 'loaded image ', image

# get image size
rows = inDs[0].RasterYSize
cols = inDs[0].RasterXSize
bands = inDs[0].RasterCount

# get the bands and block sizes
inBand=[]
for image in range(len(inDs)):
  inBand.append(inDs[image].GetRasterBand(1))


blockSizes = utils.GetBlockSize(inBand[0])
xBlockSize = blockSizes[0]
yBlockSize = blockSizes[1]
#print yBlockSize, xBlockSize

# create the output image
driver = inDs[0].GetDriver()
options = ["TILED=YES","BLOCKXSIZE=256","BLOCKYSIZE=256"]
outMerged = driver.Create(sys.argv[1], cols, rows, 3, GDT_Byte, options)
if outMerged is None:
  print 'Could not create ',sys.argv[1]
  sys.exit(-1)
else:
  print 'Writing to ',sys.argv[1]

outBandR = outMerged.GetRasterBand(1)
outBandG = outMerged.GetRasterBand(2)
outBandB = outMerged.GetRasterBand(3)

# loop through the rows
for i in range(0, rows, yBlockSize):
  if i + yBlockSize < rows:
	numRows = yBlockSize
  else:
	numRows = rows - i

  # loop through the columns
  for j in range(0, cols, xBlockSize):
	if j + xBlockSize < cols:
	  numCols = xBlockSize
	else:
	  numCols = cols - j

	# read the data
	for data in inBand:
	    values=data.ReadAsArray(j, i, numCols, numRows).astype(numpy.float)
	
	# do the calculations    
	rgb=calculateRGB(values)
	
	# write the data
	outBandR.WriteArray(rgb[0], j, i)
	outBandG.WriteArray(rgb[1], j, i)
	outBandB.WriteArray(rgb[2], j, i)

# flush data to disk, set the NoData value and calculate stats
outBandR.FlushCache()
outBandG.FlushCache()
outBandB.FlushCache()

noData=9999

outBandR.SetNoDataValue(noData)
outBandG.SetNoDataValue(noData)
outBandB.SetNoDataValue(noData)

# georeference the image and set the projection
outMerged.SetGeoTransform(inDs[0].GetGeoTransform())

outMerged.SetProjection(inDs[0].GetProjection())


# build pyramids
outMerged.BuildOverviews('NEAREST', overviewlist = [2,4,8,16,32])

inDs[0] = None
inDs[1] = None
inDs[2] = None

outMerged = None

return 1


def calculateRGB(*values)
	data1 = values[0]
	data2 = values[1]
	data3 = values[2]
	data4 = values[3]

	red = data1 - data2
	green = data3 - data4
	blue = data1
	
	byte_red = 255 * ((red - (-25)) / (0 - (-25)))**1/1
	byte_green = 255 * ((green - (-40)) / (5 - (-40)))**1/1
	byte_blue = 255 * ((blue - (243)) / (208 - (243)))**1/1
	
	return {byte_red,byte_green,byte_blue}
