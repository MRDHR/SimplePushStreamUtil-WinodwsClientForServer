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

echo "wget部分已完成，开始下载pip安装脚本";

wget https://bootstrap.pypa.io/get-pip.py
echo "pip安装脚本下载已完成，开始安装python pip环境"

python get-pip.py
echo "python pip环境安装完成，开始安装streamlink环境"

pip install streamlink

echo "streamlink安装结束"

exit 0


