#!/bin/bash

kill -9 $(pgrep -f java)
nohup java -jar ~/build/libs/e-commerce-api.jar > /tmp/nohup.log &
