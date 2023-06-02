#!/bin/bash

echo "run java spring boot ${APPLICATION_NAME} with profile : ${CONTAINER_ENV}"

# define default attribute..
APPLICATION_JAR="${APPLICATION_NAME}.jar"

# prepared arguments allocation memory..
if [[ "x${XMSLimit}" == "x" ]]; then
    # default allocation memory minimum
    XMSLimit="128m"
fi

if [[ "x${XMXLimit}" == "x" ]]; then
    # default allocation memory maximum
    XMXLimit="256m"
fi

JAVA_OPTS="-Xms${XMSLimit} -Xmx${XMXLimit}"

# set up spring profile active.
if [[ "x${CONTAINER_ENV}" != "x" ]]; then
    # default environment variable
    JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=${CONTAINER_ENV,,}"
fi

if [[ "x${SPRING_CLOUD_CONFIG_URI}" != "x" ]]; then
	JAVA_OPTS="${JAVA_OPTS} -Dspring.cloud.config.enabled=true"
fi

#if [[ "x${ENABLE_DATADOG}" != "x" ]]; then
#    echo "Enable data dog agent"
#    AGENT_DATADOG="-javaagent:/apps/dd-java-agent.jar"
#fi

# run apps.
echo "java -jar ${JAVA_OPTS} ${APPLICATION_JAR}"
eval "java -jar ${JAVA_OPTS} ${APPLICATION_JAR}"
