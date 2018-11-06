#!/bin/bash

# Script to install FFmpeg on GNU/Linux
# Website: https://www.johnvansickle.com/ffmpeg/
# Created by q3aql (q3aql@openmailbox.org)
# Builds by John Van Sickle (john.vansickle@gmail.com)
# Licensed by GPL v.2
# Date: 02-10-2016
# --------------------------------------
parent=$(yum list installed | grep java)
if ["$parent" =  ""];then
    yum -y install java
fi

echo "java部分已完成，开始安装服务环境";

cd /usr/local/src/SimplePushStreamUtil/ && mv SimplePushStreamUtil-Server.service /etc/systemd/system/

systemctl disable SimplePushStreamUtil-Server.service

systemctl enable SimplePushStreamUtil-Server.service

systemctl start SimplePushStreamUtil-Server.service

echo "服务安装结束"

exit 0


