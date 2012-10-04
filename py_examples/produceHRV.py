from LammaUtils import parseFileLocations, produceRGBOutputImage

inputDir, out_filePath, baseFileName = parseFileLocations()
out_filePath = out_filePath + "HRV.tif"

outputImage = produceRGBOutputImage(inputDir, baseFileName, "12", "12", "9>12")
outputImage.save(out_filePath)

