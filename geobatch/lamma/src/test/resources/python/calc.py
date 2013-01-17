#!/usr/bin/python 
# -*- coding: utf-8 -*-
# Carlo Cancellieri

def calculateRGB(case, values):
    if (case == 'airmass'):
        return airMass(values)
    elif (case == 'Hrv_Fog'):
        return hrvFog(values)
    elif (case == 'Dust'):
        return dust(values)
    elif (case == 'NatColours'):
        return natColours(values)
    else:
        return None
    
def airMass(values):
    data1 = values[0]
    data2 = values[1]
    data3 = values[2]
    data4 = values[3]
    red = data1 - data2
    green = data3 - data4
    blue = data1
    byte_red = 255 * ((red - (-25)) / (0 - (-25))) ** 1 / 1
    byte_green = 255 * ((green - (-40)) / (5 - (-40))) ** 1 / 1
    byte_blue = 255 * ((blue - (243)) / (208 - (243))) ** 1 / 1    
    return [byte_red, byte_green, byte_blue]

def dust(values):
    data1 = values[0]
    data2 = values[1]
    data3 = values[2]
    red = data3 - data2
    green = data2 - data1
    blue = data2
    byte_red = 255 * ((red - (-4)) / (2 - (-4)))**1/1
    byte_green = 255 * ((green - (0)) / (15 - (-0)))**1/2.5
    byte_blue = 255 * ((blue - (261)) / (289 - (261)))**1/1
    return [byte_red,byte_green,byte_blue]

def hrvFog(values):
    data1 = values[0]
    data2 = values[1]
    data3 = values[2]
    red = data3
    green = data1
    blue = data2
    byte_red = 255 * ((red - (0.0)) / (0.7 - (0.0)))**1/1.7
    byte_green = 255 * ((green - (0.0)) / (1.0 - (0.0)))**1/1.7
    byte_blue = 255 * ((blue - (0.0)) / (1.0 - (0.0)))**1/1.7
    return [byte_red, byte_green, byte_blue]

def natColours(values):
    data1 = values[0]
    data2 = values[1]
    data3 = values[2]
    red = data3
    green = data2
    blue = data1
    byte_red = 255 * ((red - (0.0)) / (1.0 - (0.0)))**1/1
    byte_green = 255 * ((green - (0.0)) / (1.0 - (0.0)))**1/1
    byte_blue = 255 * ((blue - (0.0)) / (1.0 - (0.0)))**1/1
    return [byte_red, byte_green, byte_blue]