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

echo "wget部分已完成，开始准备ffmpeg环境";

wget https://raw.githubusercontent.com/q3aql/ffmpeg-install/master/ffmpeg-install
echo "ffmpeg安装脚本下载完毕，开始设置权限"

chmod a+x ffmpeg-install
echo "权限设置完毕，开始执行安装脚本。预计需要5-6分钟"

./ffmpeg-install --install release
exit 0


