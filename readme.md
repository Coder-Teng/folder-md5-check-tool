# 文件夹MD5检查工具

​	检查生产环境中文件是否被篡改。

​	实现过程：首先读取正确环境下文件的MD5并存档，再以此MD5存档为标准比对异常环境下的文件。

```properties
/java: 	java版源码
/c++:	c++版源码
/tool:	已打包的工具
/tool/linux:	linux平台java版本工具
/tool/windows/c++:	windows平台c++版本工具
/tool/windows/java:	windows平台java版本工具
```



