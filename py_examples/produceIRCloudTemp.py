from LammaUtils import parseFileLocations, produceGrayOutputImage

inputDir, out_filePath, baseFileName = parseFileLocations()
out_filePath = out_filePath + "IRCloudTemp.tif"

outputImage = produceGrayOutputImage(inputDir, baseFileName, "10")
outputImage.save(out_filePath)

