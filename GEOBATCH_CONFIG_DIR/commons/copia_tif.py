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

    for image in sys.argv[1:]:
    
        dirname = '/opt/data/models/'+argv[1]+'/'      
        dirArray = []
        for f in os.listdir(dirname):
            if os.path.isdir(os.path.join(dirname, f)):
                dirArray.append(f)

        lastDir = sorted(dirArray,reverse=True)        
        
        src_dir = dirname+lastDir[0]
     
        for c in os.listdir(src_dir):
            if os.path.isdir(os.path.join(src_dir, c)):
            
                input_dir = src_dir+'/'+c
                
                output_dir = '/opt/geobatch/temp/zip_temp'
                
                remote_dir = '/var/www/html/download/'                
                
                newpath = output_dir+'/'+argv[1]+'/'+c
                
                newpath_remote_dir = remote_dir+'/'+argv[1]+'/'+c
                
                if not os.path.exists(newpath): os.makedirs(newpath)
                    
                os.system('ssh root@172.16.1.137 mkdir -p '+newpath_remote_dir)
                #os.system('ssh root@172.16.1.137 chmod 733 '+newpath_remote_dir)
                
                for a in os.listdir(input_dir):
                    
                    if "0200.000" in a:
                        path200 = newpath+'_200_0'
                        if not os.path.exists(path200): os.makedirs(path200)
                        shutil.copy2(input_dir+'/'+a, path200)
                    elif "0300.000" in a:
                        path300 = newpath+'_300_0'
                        if not os.path.exists(path300): os.makedirs(path300)
                        shutil.copy2(input_dir+'/'+a, path300)
                    elif "0500.000" in a:
                        path500 = newpath+'_500_0'
                        if not os.path.exists(path500): os.makedirs(path500)
                        shutil.copy2(input_dir+'/'+a, path500)
                    elif "0600.000" in a:
                        path600 = newpath+'_600_0'
                        if not os.path.exists(path600): os.makedirs(path600)
                        shutil.copy2(input_dir+'/'+a, path600)
                    elif "0700.000" in a:
                        path700 = newpath+'_700_0'
                        if not os.path.exists(path700): os.makedirs(path700)
                        shutil.copy2(input_dir+'/'+a, path700)
                    elif "0850.000" in a:
                        path850 = newpath+'_850_0'
                        if not os.path.exists(path850): os.makedirs(path850)
                        shutil.copy2(input_dir+'/'+a, path850)
                    elif "0925.000" in a:
                        path925 = newpath+'_925_0'
                        if not os.path.exists(path925): os.makedirs(path925)
                        shutil.copy2(input_dir+'/'+a, path925)
                    elif "1000.000" in a:
                        path1000 = newpath+'_1000_0'
                        if not os.path.exists(path1000): os.makedirs(path1000)
                        shutil.copy2(input_dir+'/'+a, path1000)
                    elif "0000.000" in a:
                        path0000 = newpath+'_0_0'
                        if not os.path.exists(path0000): os.makedirs(path0000)
                        shutil.copy2(input_dir+'/'+a, path0000)
                    elif "0002.000" in a:
                        path0002 = newpath+'_2_0'
                        if not os.path.exists(path0002): os.makedirs(path0002)
                        shutil.copy2(input_dir+'/'+a, path0002)
                    elif "0010.000" in a:
                        path0010 = newpath+'_10_0'
                        if not os.path.exists(path0010): os.makedirs(path0010)
                        shutil.copy2(input_dir+'/'+a, path0010)
                    elif "0005.000" in a:
                        path0005 = newpath+'_5_0'
                        if not os.path.exists(path0005): os.makedirs(path0005)
                        shutil.copy2(input_dir+'/'+a, path0005)  
                    elif "0025.000" in a:
                        path0025 = newpath+'_25_0'
                        if not os.path.exists(path0025): os.makedirs(path0025)
                        shutil.copy2(input_dir+'/'+a, path0025)  
                    elif "0070.000" in a:
                        path0070 = newpath+'_70_0'
                        if not os.path.exists(path0070): os.makedirs(path0070)
                        shutil.copy2(input_dir+'/'+a, path0070)  
                    elif "0150.000" in a:
                        path0150 = newpath+'_150_0'
                        if not os.path.exists(path0150): os.makedirs(path0150)
                        shutil.copy2(input_dir+'/'+a, path0150)                          
                    else:
                        pass
                        
        temp_dir = '/opt/geobatch/temp/zip_temp'+'/'+argv[1]    
        
        rem_dir = '/var/www/html/download/'                            
        
        for k in os.listdir(temp_dir):
            if "_200_0" in k:
                v200 = k.replace("_200_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v200) )
            elif "_300_0" in k:
                v300 = k.replace("_300_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v300) )                
            elif "_500_0" in k:
                v500 = k.replace("_500_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v500) )                
            elif "_600_0" in k:
                v600 = k.replace("_600_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v600) )                
            elif "_700_0" in k:
                v700 = k.replace("_700_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v700) )                
            elif "_850_0" in k:
                v850 = k.replace("_850_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v850) )                
            elif "_925_0" in k:
                v925 = k.replace("_925_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v925) )                
            elif "_1000_0" in k:     
                v1000 = k.replace("_1000_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v1000) )                
            elif "_0_0" in k:
                v0000 = k.replace("_0_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v0000) ) 
            elif "_2_0" in k:
                v0002 = k.replace("_2_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v0002) ) 
            elif "_10_0" in k:
                v0010 = k.replace("_10_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v0010) )                                    
            elif "_5_0" in k:
                v0005 = k.replace("_5_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v0005) )
            elif "_25_0" in k:
                v0025 = k.replace("_25_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v0025) )                                    
            elif "_70_0" in k:
                v0070 = k.replace("_70_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v0070) )                                    
            elif "_150_0" in k:
                v0150 = k.replace("_150_0","")
                os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+v0150) )                                                    
            else:
                pass
                #os.system('zip -r -j '+temp_dir+'/'+k+' '+temp_dir+'/'+k)                        
                #os.system('scp "%s" "%s:%s"' % (temp_dir+'/'+k+'.zip', 'root@172.16.1.137', rem_dir+argv[1]+'/'+k) ) 
                
        remove_dir = '/opt/geobatch/temp/zip_temp'+'/'+argv[1]
        os.system('rm -r -f '+remove_dir+'/*')

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