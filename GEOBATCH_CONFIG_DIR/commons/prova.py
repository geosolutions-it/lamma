#!/usr/bin/python 
# -*- coding: utf-8 -*-
# Daniele Romagnoli
# Carlo Cancellieri
# Riccardo Mari

# RGB AirMass
#-----------------------------
#from osgeo import osr
#from osgeo.gdalconst import GA_ReadOnly, GDT_Byte
import os
import sys, time
import calc
import glob
import shutil
import zipfile

try: 
    from osgeo import gdal
    import numpy
    os.chdir('.')
except ImportError:
    #import gdal
    os.chdir('.')


def simpleCreateRGB(argv):
    # register all of the GDAL drivers
    #gdal.AllRegister()
    
    inDs = []
    
    if (len(sys.argv) == 0):
        print 'Usage:'
        print 'python ./createRGB_2.py calc.function() OutFile.tif Channel_0.tif ... Channel_n.tif'
    
    # open channels skipping first 2 args
    # 0 == ./createRGB_X.py	[script file]
    # 1 == OutFile.tif	[output file]
    for image in sys.argv[1:]:
        out_file = open("/opt/geobatch/conf/commons/pippo.txt","w")
        out_file.write("Argomento: "+image+"\n")
        out_file.close()
        #shutil.copy2('/opt/data/msg3/MSG3_Airmass/*.tif', '/opt/geobatch/temp/pippo')
        #if openImage is None:
            #print 'Could not open ', image
            #sys.exit(-1)
            #break
        #else :
            #inDs.append(openImage)
            #print 'loaded image ', image

# MAIN

ret=simpleCreateRGB(sys.argv)

#src_dir = '/opt/data/msg3/MSG3_Airmass/'
#dst_dir = '/opt/geobatch/temp/pippo'

#for jpgfile in glob.glob(os.path.join(src_dir, "*.tif")):
#    shutil.copy(jpgfile, dst_dir)


#physicalPathOfFile = '/opt/geobatch/temp/pippo'
#logicalPathOfFileInZip = 'pippo'

#restoreZip = zipfile.ZipFile("test.zip", "w")
#restoreZip.write(physicalPathOfFile, logicalPathOfFileInZip)
#restoreZip.close()

#restoreZip = zipfile.ZipFile("test.zip", "w", zipfile.ZIP_DEFLATED)