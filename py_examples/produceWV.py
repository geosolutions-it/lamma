from LammaUtils import parseFileLocations, produceGrayOutputImage

inputDir, out_filePath, baseFileName = parseFileLocations()
out_filePath = out_filePath + "WV.tif"

outputImage = produceGrayOutputImage(inputDir, baseFileName, "5")
outputImage.save(out_filePath)

