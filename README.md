# cordova-plugin-nativeloader
A fullscreen native simple loader for using when opening pages using inappbrowser with '_self', so you avoid having a white screen before the new page is loaded.

Installing:

cordova plugin add https://github.com/dganzella/cordova-plugin-nativeloader

Usage Example:

Cordova.exec(successCallback, errorCallback, 'NativeLoaderPlugin', 'showView', [true]); //true shows view, false hides view