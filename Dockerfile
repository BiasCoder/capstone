FROM doku-images-registry-registry.ap-southeast-5.cr.aliyuncs.com/doku-registry/doku-jdk17-alpine
#FROM openjdk:17-jdk-slim

# don't forget to set final name project on pom.xml
ENV APPLICATION_NAME=da3-dua-web-service-api
ARG application_file_name=${APPLICATION_NAME}.jar

USER root
#-------------------------------------------------------------
# Prepare file and application
#-------------------------------------------------------------
RUN mkdir -p /var/log/$project_name/

ADD docker-cmd.sh /apps/
ADD target/deploy/$application_file_name /apps/
#ADD docker-target/dd-java-agent.jar /apps/

#-------------------------------------------------------------
# Set Permission in OS
#-------------------------------------------------------------
RUN cd /apps && chmod +rx $application_file_name && chmod +rx docker-cmd.sh
RUN chown -R 3000:3000 /apps
USER 3000

#-------------------------------------------------------------
# Set default working dir
#-------------------------------------------------------------
WORKDIR  /apps/
EXPOSE 8080

CMD ./docker-cmd.sh