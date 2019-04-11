FROM php:7.0-apache

# install PHP-JWT library via composer in order to decode JWT token
RUN apt-get update && \
    apt-get install -y --no-install-recommends git zip
RUN curl --silent --show-error https://getcomposer.org/installer | php
RUN php composer.phar require firebase/php-jwt

# https://stackoverflow.com/questions/10144634/
RUN a2enmod rewrite

COPY src /var/www/html/

EXPOSE 80
