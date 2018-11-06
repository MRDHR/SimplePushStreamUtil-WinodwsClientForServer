#!/bin/bash

# Script to install FFmpeg on GNU/Linux
# Website: https://www.johnvansickle.com/ffmpeg/
# Created by q3aql (q3aql@openmailbox.org)
# Builds by John Van Sickle (john.vansickle@gmail.com)
# Licensed by GPL v.2
# Date: 02-10-2016
# --------------------------------------
parent=$(yum list installed | grep wget)
if ["$parent" =  ""];then
    yum -y install wget
fi

echo "wget部分已完成，开始准备youtube-dl环境";

wget https://yt-dl.org/downloads/latest/youtube-dl -O /usr/local/bin/youtube-dl
echo "youtube-dl下载完毕，开始设置权限"

chmod a+rx /usr/local/bin/youtube-dl
echo "权限设置完毕"

echo "youtube-dl安装结束"

exit 0


