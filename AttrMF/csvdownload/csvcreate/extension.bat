@echo off
echo Visual Studio Code の拡張機能をインストール中...

code --install-extension vscjava.vscode-maven
code --install-extension vmware.vscode-boot-dev-pack
code --install-extension yzhang.markdown-all-in-one
code --install-extension bierner.markdown-mermaid
code --install-extension davidanson.vscode-markdownlint

echo 拡張機能のインストールが完了しました。
pause