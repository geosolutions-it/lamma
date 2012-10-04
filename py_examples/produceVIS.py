from LammaUtils import parseFileLocations, produceRGBOutputImage

inputDir, out_filePath, baseFileName = parseFileLocations()
out_filePath = out_filePath + "VIS.tif"

outputImage = produceRGBOutputImage(inputDir, baseFileName, "1", "2", "3")
outputImage.save(out_filePath)

