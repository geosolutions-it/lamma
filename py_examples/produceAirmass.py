from LammaUtils import parseFileLocations, produceRGBOutputImage

inputDir, out_filePath, baseFileName = parseFileLocations()
out_filePath = out_filePath + "Airmass.tif"

outputImage = produceRGBOutputImage(inputDir, baseFileName, "5-6", "8-9", "5")
outputImage.save(out_filePath)
