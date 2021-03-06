# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
#
# Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
#
# GlassFish on Docker with Oracle Linux and OpenJDK
FROM oraclelinux:7-slim

# Maintainer
MAINTAINER Arindam Bandyopadhyay<arindam.bandyopadhyay@oracle.com>
MAINTAINER Víctor Fernández "vfrico@gmail.com"

# Set environment variables and default password for user 'admin'
ENV GLASSFISH_PKG=glassfish-5.0.1-b01-06_27_2018.zip \
    GLASSFISH_URL=http://download.oracle.com/glassfish/5.0.1/nightly/glassfish-5.0.1-b01-06_27_2018.zip \
    GLASSFISH_HOME=/glassfish5 \
    MD5=7591eb7577eab5e3d6b2bba04b2c6a39 \
    PATH=$PATH:/glassfish5/bin \
    JAVA_HOME=/usr/lib/jvm/java-openjdk \
    JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF8"

# Install packages, download and extract GlassFish
# Install JDK-devel (needed for JSP pages)
# Setup password file
# Enable DAS
RUN yum -y install unzip java-1.8.0-openjdk-devel.x86_64 && \
    curl -O $GLASSFISH_URL && \
    echo "$MD5 *$GLASSFISH_PKG" | md5sum -c - && \
    unzip -o $GLASSFISH_PKG && \
    rm -f $GLASSFISH_PKG && \
    yum -y remove unzip && \
    rm -rf /var/cache/yum

COPY docker-entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/entrypoint.sh"]

# Ports being exposed
EXPOSE 4848 8080 8181

# Start asadmin console and the domain
CMD ["asadmin", "start-domain", "-v"]

# Copy the compiled WAR to autodeploy folder
COPY target/predictor.war $GLASSFISH_HOME/glassfish/domains/domain1/autodeploy/

# Build with:
# docker build .
# Run with:
# docker run -p 8080:8080 -p 8181:8181 -p 4848:4848 -v /home/vfrico:/home/vfrico f2fe36e684d1