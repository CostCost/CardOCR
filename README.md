
##
#### app
```
https://github.com/153437803/CardOCR/blob/master/app-release.apk
```

##
#### 效果预览
![image](https://github.com/153437803/Ocr_IDCard/blob/master/ScreenRecord_20181116181123.gif )
![image](https://github.com/153437803/Ocr_IDCard/blob/master/ScreenRecord_20181116181153.gif )

##
#### 参考
```
https://github.com/opencv/opencv [图像处理, 卡片边框定位识别]
```
##
#### 规划
```
1. 基于opencv344, 实现LSD卡片边框直线检测 => 未完成
2. 依赖光学识别开源库，编译实现光学字符识别 => 未完成
```

##
#### 问题
```
1. 证件扫描界面黑屏（没有适配动态权限，自己手动打开相机权限）
2. 无法扫描识别信息（确认画面是否挤压变形，默认横屏640*480，自己修改试试，或者提Issues）
```
