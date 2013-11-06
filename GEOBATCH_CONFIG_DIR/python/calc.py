#!/usr/bin/python 
# -*- coding: utf-8 -*-
# Carlo Cancellieri

def calculateRGB(case, values):
    if (case == 'airmass'):
        return airMass(values)
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
