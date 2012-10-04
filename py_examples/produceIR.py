from LammaUtils import parseFileLocations, produceRGBOutputImage

inputDir, out_filePath, baseFileName = parseFileLocations()
out_filePath = out_filePath + "IR.tif"

outputImage = produceRGBOutputImage(inputDir, baseFileName, "4", "9", "10")
outputImage.save(out_filePath)

