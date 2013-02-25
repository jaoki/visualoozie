#! /bin/bash -e

GREEN='\e[0;32m'
RED='\e[0;31m[ERROR] '
CYAN='\e[0;36m'
ENDCOLOR='\e[0m'

echo -e "${CYAN}"
echo "****************************************************"
echo "Checkign local repository"
echo -e "****************************************************${ENDCOLOR}"

if [[ $(git status -s) ]]; then 
	echo -e "${RED} There is uncommited or pushed changes in the local repository.${ENDCOLOR}"
	exit -1
else
	echo -e "The local repository is up-to-date and no changed has been made."
fi

POM_VERSION=`cat pom.xml |grep "<version>"|head -1|cut -d">" -f2|cut -d"<" -f1`
echo "pom version ${POM_VERSION} is found"

# LATEST_TAG=`git tag|tail -1`

if git tag |grep -q "${POM_VERSION}"; then
	echo -e "${RED}${POM_VERSION} already exists${ENDCOLOR}"
	exit -1
fi


echo -e ${CYAN}
echo "****************************************************"
echo "Building mvn project..."
echo -e "****************************************************${ENDCOLOR}"

mvn clean package

echo -e ${CYAN}
echo "****************************************************"
echo "Uploading to cloudbess..."
echo -e "****************************************************${ENDCOLOR}"

bees app:deploy -a visualoozie/alpha target/visualoozie.war


echo -e "${CYAN}"
echo "****************************************************"
echo "Making a tag"
echo -e "****************************************************${ENDCOLOR}"

git tag -a ${POM_VERSION} -m "tagging ${POM_VERSION}..."|true
echo "Tag ${POM_VERSION} was made"

git push --tags|true
echo "Tag ${POM_VERSION} was pushed"


echo -e "${GREEN}[SUCCESS!]${ENDCOLOR}"

