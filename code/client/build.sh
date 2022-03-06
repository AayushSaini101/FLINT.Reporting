#!/bin/bash

echo
echo "========================================================================"
echo "Entering Artifact Build Script"
echo "========================================================================"

# ------------------------------------------------------------------------
# INITIALIZE SHELL COLOR VARIABLES
# ------------------------------------------------------------------------

RED_COLOR='\033[0;31m'
GREEN_COLOR='\033[0;32m'
NO_COLOR='\033[0m'

# ------------------------------------------------------------------------
# INITIALIZE PATH VARIABLES
# ------------------------------------------------------------------------

BASEDIR="$( cd "$( dirname "$0" )" && pwd )"

# -------------------------------------------------------------------------------------
# SET WORKING DIRECTORY
# -------------------------------------------------------------------------------------

cd $BASEDIR

# -------------------------------------------------------------------------------------
# INSTALL DEPENDENCIES
# -------------------------------------------------------------------------------------

echo
echo "Installing npm dependencies"
echo
npm install
npm install --save jwt-decode
npm install --save @types/jwt-decode

# -------------------------------------------------------------------------------------
# BUILD COMPONENT
# -------------------------------------------------------------------------------------

echo
echo "Building component for production environment"
echo

#ng build --prod
ng build --configuration production --base-href /client/

echo
echo "========================================================================"
echo "Leaving Artifact Build Script"
echo "========================================================================"
echo