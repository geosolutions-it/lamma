from LammaUtils import parseFileLocations, produceRGBOutputImage

inputDir, out_filePath, baseFileName = parseFileLocations()
out_filePath = out_filePath + "CVS.tif"

outputImage = produceRGBOutputImage(inputDir, baseFileName, "5-6", "4-9", "3-1")
outputImage.save(out_filePath)
