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
	echo -e "${RED} There is uncommited or pushed changes in the local repository."
	exit -1
fi



echo -e "${CYAN}"
echo "****************************************************"
echo "Making a tag"
echo -e "****************************************************${ENDCOLOR}"

POM_VERSION=`cat pom.xml |grep "<version>"|head -1|cut -d">" -f2|cut -d"<" -f1`
echo "pom version ${POM_VERSION} is found"

exit -1

LATEST_TAG=`git tag|tail -1`

if git tag |grep -q "${POM_VERSION}"; then
	echo -e "${RED}${POM_VERSION} already exists"
	exit -1
fi

git tag -a ${POM_VERSION} -m "tagging ${POM_VERSION}..."
git push --tags



echo -e ${CYAN}
echo "****************************************************"
echo "Building mvn project..."
echo "****************************************************"
echo -e ${ENDCOLOR}

# mvn clean package

echo -e ${CYAN}
echo "****************************************************"
echo "Uploading to cloudbess..."
echo "****************************************************"
echo -e ${ENDCOLOR}

# bees app:deploy -a visualoozie/alpha target/visualoozie.war

echo -e "${GREEN}[SUCCESS!]${ENDCOLOR}"

