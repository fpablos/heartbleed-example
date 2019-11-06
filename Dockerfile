FROM debian:jessie

# Environment
ENV LANG C.UTF-8
ENV LANGUAGE C.UTF-8
ENV LC_ALL C.UTF-8
ENV JAVA_VERSION 1.8.0_231
ENV MAVEN_VERSION 3.5.4
ENV PASS springboot

ARG DEBIAN_FRONTEND=noninteractive	

# Get current
RUN apt-get update -y
RUN apt-get install -y \
	wget \
	dpkg \ 
	gnupg2

WORKDIR /usr/lib/jvm
COPY ./java .
RUN tar -xzvf ./*.tgz

ENV PATH $PATH:/usr/lib/jvm/jdk${JAVA_VERSION}/bin:/usr/lib/jvm/jdk${JAVA_VERSION}/db/bin:/usr/lib/jvm/jdk${JAVA_VERSION}/jre/bin
ENV J2SDKDIR /usr/lib/jvm/jdk${JAVA_VERSION}
ENV J2REDIR /usr/lib/jvm/jdk${JAVA_VERSION}/jre
ENV JAVA_HOME /usr/lib/jvm/jdk${JAVA_VERSION}
ENV DERBY_HOME /usr/lib/jvm/jdk${JAVA_VERSION}/db

# Install Alternatives
RUN update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.8.0_231/bin/java" 0
RUN update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk1.8.0_231/bin/javac" 0
RUN update-alternatives --set java /usr/lib/jvm/jdk1.8.0_231/bin/java
RUN update-alternatives --set javac /usr/lib/jvm/jdk1.8.0_231/bin/javac
RUN update-alternatives --list java
RUN update-alternatives --list javac

RUN apt-get update
RUN apt-get install wget -y

# Setup the version and path for MAVEN
ENV PATH /opt/apache-maven-${MAVEN_VERSION}/bin:${PATH}
WORKDIR /opt

RUN wget http://www.eu.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz
RUN tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz
ENV PATH /opt/apache-maven-${MAVEN_VERSION}/bin:$PATH

RUN apt-get update -y
RUN apt-get dist-upgrade -y
RUN apt-get install -y \
	wget \
	dpkg \
	multiarch-support

# Install SSL vulnerable versions
RUN wget http://snapshot.debian.org/archive/debian/20130319T033933Z/pool/main/o/openssl/libssl1.0.0_1.0.1e-2_amd64.deb -O /tmp/libssl1.0.0_1.0.1e-2_amd64.deb && \
 dpkg -i /tmp/libssl1.0.0_1.0.1e-2_amd64.deb

RUN wget http://snapshot.debian.org/archive/debian/20130319T033933Z/pool/main/o/openssl/openssl_1.0.1e-2_amd64.deb -O /tmp/openssl_1.0.1e-2_amd64.deb && \
 dpkg -i /tmp/openssl_1.0.1e-2_amd64.deb

#Apache as Reverse Proxy
RUN apt-get install -y apache2 

RUN chown -R www-data:www-data /var/www

ENV APACHE_RUN_USER  www-data
ENV APACHE_RUN_GROUP www-data
ENV APACHE_LOG_DIR   /var/log/apache2
ENV APACHE_PID_FILE  /var/run/apache2/apache2.pid
ENV APACHE_RUN_DIR   /var/run/apache2
ENV APACHE_LOCK_DIR  /var/lock/apache2
ENV APACHE_LOG_DIR   /var/log/apache2

RUN mkdir -p $APACHE_RUN_DIR
RUN mkdir -p $APACHE_LOCK_DIR
RUN mkdir -p $APACHE_LOG_DIR

RUN a2enmod ssl
RUN a2enmod rewrite
RUN a2enmod proxy
RUN a2enmod proxy_http

ADD ./config/default-ssl.conf /etc/apache2/sites-available/default-ssl.conf
RUN echo "ServerName 0.0.0.0" >> /etc/apache2/apache2.conf

RUN a2dissite 000-default.conf
RUN a2ensite default-ssl

RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*

WORKDIR /var/app
COPY ./backend /var/app

#RUN $JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA -deststoretype pkcs12 -keystore keyStore.p12 -storepass ${PASS} -dname "C=AR, ST=Buenos Aires, L=Buenos Aires, O=Global Security, OU=IT Department, CN=*.segwebapp.com.ar"

RUN mvn -N io.takari:maven:wrapper

RUN mvn compile

RUN mvn package

EXPOSE 443

COPY init.sh /usr/sbin/
RUN chmod +x /usr/sbin/init.sh

CMD ["/usr/sbin/init.sh"]