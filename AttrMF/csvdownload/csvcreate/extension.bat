@echo off
echo Visual Studio Code の拡張機能をインストール中...

call code --install-extension vscjava.vscode-maven
call code --install-extension vmware.vscode-boot-dev-pack
call code --install-extension yzhang.markdown-all-in-one
call code --install-extension bierner.markdown-mermaid
call code --install-extension davidanson.vscode-markdownlint

echo 拡張機能のインストールが完了しました。
pause