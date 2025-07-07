#!/bin/bash

echo "Visual Studio Code の拡張機能をインストール中..."

VSCODE="/Applications/Visual Studio Code.app/Contents/Resources/app/bin/code"

extensions=(
    vscjava.vscode-maven
    vmware.vscode-boot-dev-pack
    yzhang.markdown-all-in-one
    bierner.markdown-mermaid
    davidanson.vscode-markdownlint
)

for ext in "${extensions[@]}"
do
    "$VSCODE" --install-extension "$ext" || echo "$ext のインストールに失敗しました"
done

echo "拡張機能のインストールが完了しました。"
read -p "Enter キーを押すと終了します。"
