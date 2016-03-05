#!/bin/bash

cd $HOME
mkdir -p installprefix

if [ ! -d "$HOME/LucenePlusPlus" ]; then
    git clone https://github.com/jkozera/LucenePlusPlus.git
    cd LucenePlusPlus
    mkdir build
    cd build
    cmake .. -DCMAKE_INSTALL_PREFIX:PATH=$HOME/installprefix
    make install
    cd ../..
fi

if [ ! -d "$HOME/libarchive" ]; then
    git clone https://github.com/libarchive/libarchive.git
    cd libarchive
    git checkout 884f82a93ee5eb932b1e0fb74b6893708a43dc6d
    mkdir -p zest_build/installprefix
    cd zest_build
    cmake .. -DENABLE_OPENSSL=OFF -DCMAKE_INSTALL_PREFIX:PATH=$HOME/installprefix
    make install
    cd ../..
fi

if [ ! -d "$HOME/node-sqlite3" ]; then
    git clone https://github.com/jkozera/node-sqlite3.git
    cd node-sqlite3
    npm install
    cd deps
    tar -zxvf sqlite-autoconf-3090100.tar.gz
    cd ../..
fi