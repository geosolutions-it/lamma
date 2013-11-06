#!/bin/bash

FILEOUT="/opt/GEOBATCH_CONFIG_DIR/commons/prova.log"
date > $FILEOUT
echo $1 >> $FILEOUT

exit 0
