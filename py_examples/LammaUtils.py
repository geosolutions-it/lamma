from optparse import OptionParser
import Image
import ImageChops
import ImageOps
import os
import sys

# ---------------------------------
#    Channels suffix definitions
# ---------------------------------
CHANNELS = {
  1 : lambda: "VIS006_01",
  2 : lambda: "VIS008_02",
  3 : lambda: "IR_016_03",
  4 : lambda: "IR_039_04",
  5 : lambda: "WV_062_05",
  6 : lambda: "WV_073_06",
  7 : lambda: "IR_087_07",
  8 : lambda: "IR_097_08",
  9 : lambda: "IR_108_09",
  10 : lambda: "IR_120_10",
  11 : lambda: "IR_134_11",
  12 : lambda: "HRV_12",
}

DEFAULT_DATA_EXTENSION = ".tif"
DEFAULT_CONTRAST_PERCENTAGE = 5
DEFAULT_REPROCESS = False

contrast = DEFAULT_CONTRAST_PERCENTAGE
reprocess = DEFAULT_REPROCESS 

# Rescale an image to byte values and then apply autocontrast using a
# specific percentage
def processInputImage(im):
    extrema = im.getextrema()
    return processInputImageExtrema(im, extrema)

def processInputImageExtrema(im, extrema):
    
    dataRange = extrema[1] - extrema[0]
    invRange = extrema[0] - extrema[1]
    scale = 255 / dataRange
    offset = (255*extrema[0]) / (invRange)
    # Rescale the image on the interval 0 - 255
    image2 = Image.eval(im, lambda i: i * scale + offset)
    
    # Convert the image to Byte
    im2 = image2.convert("L")
    
    # Autocontrast the image using the specified threshold
    im3 = ImageOps.autocontrast(im2, contrast)
    return im3

def parseFileLocations():
    parser = OptionParser()
    parser.add_option("-i", "--inputDir", dest="input",
                  help="Input folder containing meteo data", metavar="FILE")
    parser.add_option("-o", "--outputDir", dest="output",
                  help="Write the output file to that folder", metavar="FILE")
    parser.add_option("-c", "--contrastOutliers", dest="contrast", default = DEFAULT_CONTRAST_PERCENTAGE,
                  help="Specify the outliers percentage to be removed on the auto-contrast op")
    parser.add_option("-v", action="store_true", dest="verbose", default = False,
                  help="Set this flag for logging")
    parser.add_option("-r", "--reprocess", action="store_true", dest="reprocess", default = DEFAULT_REPROCESS,
                 help="Set this flag for rescaling again the resulting channel operations before merging")
    
    
    (options, args) = parser.parse_args()
    if options.input is None:
        print ("Input meteo data folder is missing. use -h for the help")
        sys.exit(0)
    elif options.output is None:
        print ("Output folder isn't specified. use -h for the help")
        sys.exit(0)
    verbose = options.verbose
    contrast = options.contrast
    reprocess = options.reprocess

    inputDir = options.input

    if (not os.path.isdir(inputDir)):
        print ("Specified Input path is not a folder. use -h for the help")
        sys.exit(0)
    dirList = os.listdir(inputDir)

    out_path = options.output
    baseName = os.path.basename(dirList[0])
  
    index = 0
    length = len(baseName)
    for i in range (0,3):
        index = baseName.find('_',index + 1,length)
    
    baseFileName = baseName[0:index + 1]
    if verbose:
        print "baseFileName:" + baseFileName

#---------------------------------
# Preparing the output folder
#---------------------------------
    if (not os.path.isdir(out_path)):
        os.mkdir(out_path)
    out_filePath = os.path.join(out_path, baseFileName)
    outputDir = out_path + os.sep
    if (not os.path.isdir(outputDir)):
        os.mkdir(outputDir)
    return inputDir, out_filePath, baseFileName
  
  
def getChannelSuffix(channelNumber):
    return CHANNELS.get(channelNumber)()

def getInputFileName (inputDir, baseFileName, channel):
    return inputDir + os.sep + baseFileName + getChannelSuffix(channel) + DEFAULT_DATA_EXTENSION

def produceRGBOutputImage (inputDir, baseFileName, r, g, b):
    rBand = produceBand(inputDir, baseFileName, r) 
    if (g == r):
        gBand = rBand
    else: 
        gBand = produceBand(inputDir, baseFileName, g)
    if (b == r):
        bBand = rBand
    elif (b == g):
        bBand = gBand
    else:
        bBand = produceBand(inputDir, baseFileName, b)
    return Image.merge("RGB", (rBand, gBand, bBand))

#def produceBand (inputDir, baseFileName, channel):
#    if '-' in channel:
#        channels = channel.split('-')
#        firstChannel = channels[0].strip()
#        secondChannel = channels[1].strip()
#        imageA = Image.open(getInputFileName(inputDir, baseFileName, int(firstChannel)))
#        imageB = Image.open(getInputFileName(inputDir, baseFileName, int(secondChannel)))
#        extremaA = imageA.getextrema()
#        extremaB = imageB.getextrema()
#        extrema = (min(extremaA[0],extremaB[0]),max(extremaA[0],extremaB[0]))
#        a = processInputImageExtrema(imageA, extrema)
#        b = processInputImageExtrema(imageB, extrema)
#        difference = ImageChops.subtract(a, b)
#        if reprocess:
#            return processInputImage(difference)
#        else:
#            return difference
#    elif '+' in channel:
#        channels = channel.split('+')
#        firstChannel = channels[0].strip()
#        secondChannel = channels[1].strip()
#        a = processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(firstChannel))))
#        b = processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(secondChannel))))
#        add = ImageChops.add(a, b)
#        if reprocess:
#            return processInputImage(add)
#        else:
#            return add
#    elif '>' in channel:
#        channels = channel.split('>')
#        firstChannel = channels[0].strip()
#        secondChannel = channels[1].strip()
#        a = (Image.open(getInputFileName(inputDir, baseFileName, int(firstChannel))))
#        hResImage = Image.open(getInputFileName(inputDir, baseFileName, int(secondChannel)))
#        hResImageSize = hResImage.size
#        resizedImage = a.resize((hResImageSize[0],hResImageSize[1]), Image.BILINEAR)
#        return processInputImage(resizedImage)
#    else:
#        return processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(channel))))

def produceBand (inputDir, baseFileName, channel):
    if '-' in channel:
        channels = channel.split('-')
        firstChannel = channels[0].strip()
        secondChannel = channels[1].strip()
        a = processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(firstChannel))))
        b = processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(secondChannel))))
        difference = ImageChops.subtract(a, b)
        return processInputImage(difference)
    elif '+' in channel:
        channels = channel.split('+')
        firstChannel = channels[0].strip()
        secondChannel = channels[1].strip()
        a = processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(firstChannel))))
        b = processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(secondChannel))))
        add = ImageChops.add(a, b)
        return processInputImage(add)
    elif '>' in channel:
        channels = channel.split('>')
        firstChannel = channels[0].strip()
        secondChannel = channels[1].strip()
        a = (Image.open(getInputFileName(inputDir, baseFileName, int(firstChannel))))
        hResImage = Image.open(getInputFileName(inputDir, baseFileName, int(secondChannel)))
        hResImageSize = hResImage.size
        resizedImage = a.resize((hResImageSize[0],hResImageSize[1]), Image.BILINEAR)
        return processInputImage(resizedImage)
    else:
        return processInputImage(Image.open(getInputFileName(inputDir, baseFileName, int(channel))))

def produceGrayOutputImage (inputDir, baseFileName, channel):
    return produceBand(inputDir, baseFileName, channel) 
