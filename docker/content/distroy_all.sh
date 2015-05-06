#! /bin/bash
sudo docker kill $(sudo docker ps -a -q) && sudo docker rm $(sudo docker ps -a -q)
sudo docker rmi $(docker images -a | grep "^<none>" | awk '{print $3}')
