FROM ubuntu:22.04 AS build

ARG JAVA_VERSION
RUN apt-get update && apt-get install -y \
    openjdk-${JAVA_VERSION}-jdk \
    maven

ARG ARCH
ENV JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}-openjdk-${ARCH}
ENV PATH=${JAVA_HOME}/bin:$PATH

WORKDIR /build
COPY . .
RUN mvn clean package

FROM ubuntu:22.04

ARG JAVA_VERSION
RUN apt-get update && apt-get install -y \
    openjdk-${JAVA_VERSION}-jdk curl iputils-ping dnsutils && apt-get clean

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]
