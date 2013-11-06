#!/usr/bin/python 
# -*- coding: utf-8 -*-
# Riccardo Mari

import os
import sys, time
import calc
import glob
import shutil
import zipfile
import operator

try: 
    from osgeo import gdal
    import numpy
    os.chdir('.')
except ImportError:
    #import gdal
    os.chdir('.')

def copy_zip(argv):

    if (len(sys.argv) == 0):
        print 'Usage:'
        print 'python ./createRGB_2.py calc.function() OutFile.tif Channel_0.tif ... Channel_n.tif'

    for image in sys.argv[2:]:
        remote_dir = '/var/www/html/download/' + argv[1].lower()
        newpath_remote_dir = remote_dir+'/'+image
        os.system('ssh root@172.16.1.137 rm -r -f '+newpath_remote_dir)
        #print newpath_remote_dir
        

ret=copy_zip(sys.argv)

#def findNewestDir(directory):
#    os.chdir(directory)
#    dirs = {}
#    for dir in glob.glob('*'):
#        if os.path.isdir(dir):
#            dirs[dir] = os.path.getctime(dir)
#
#    lister = sorted(dirs.iteritems(), key=operator.itemgetter(1))
#    return lister[-1][0]
#
#print "The newest directory is", findNewestDir('/opt/data/models/gfs_50km_run00')