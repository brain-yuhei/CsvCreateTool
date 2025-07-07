#!/bin/bash

echo "Visual Studio Code の拡張機能をインストール中..."

extensions=(
    vscjava.vscode-maven
    vmware.vscode-boot-dev-pack
    yzhang.markdown-all-in-one
    bierner.markdown-mermaid
    davidanson.vscode-markdownlint
)

echo " 拡張機能のインストールが完了しました。"
read -p "Enter キーを押すと終了します。"
