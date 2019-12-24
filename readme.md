# 文件夹MD5检查工具

检查生产环境中文件是否被篡改。

实现过程：首先读取正确环境下文件的MD5并存档，再以此MD5存档为标准比对异常环境下的文件。

```properties
/java: 	java版源码
/c++:	c++版源码
```

## 快速开始：

传递参数包含两种形式：从指定txt中读取 和 从控制台读取。

参数说明：

```pro
RunMode: 1或者2。1代表扫描文件夹下文件并记录md5；2代表用给定的md5记录文件,比较文件夹下文件的md5。
StandardMd5Path: 记录标准MD5列表的文件路径。
FolderPath: 要扫描或者检查的文件夹路径。
ExcludedPaths: 需要排除的子目录，可以是完整路径也可是相对于FolderPath的相对路径；各个目录间用||分割。
```

### 1、扫描文件夹下文件并记录md5。

- 从指定txt中读取参数方式 调用。

  ```cmd
  java -Dfile.encoding=utf-8 -jar folder-md5-check-tool-1.0.0.jar txt params.txt
  ```

  txt：表示从文本中读取。

  params.txt：参数文本路径，可以为相对路径也可为绝对路径。

  实例params.txt：

  ```pro
  RunMode=1
  StandardMd5Path=standard-md5.txt
  FolderPath=/home
  ExcludedPaths=test||/home/test
  ```

- 从控制台读取参数方式调用。

  ```cmd
  java -Dfile.encoding=utf-8 -jar folder-md5-check-tool-1.0.0.jar console 1 standard-md5.txt /home test||/home/test
  ```

- 输出示例：

  ```cmd
  [root@localhost folder-md5-check-tool]# ./folder-md5-check-tool.sh 
  os=Linux
  RunMode=1
  StandardMd5Path=standard-md5.txt
  FolderPath=/home
  ExcludedPaths=[test, /home/test]
  Done, records in /home/folder-md5-check-tool/standard-md5.txt
  ```

### 2、用给定的md5记录文件,比较文件夹下文件的md5。

- 从指定txt中读取参数方式 调用，检查结果输出在控制台以及当前目录下的checkResult_all.csv和
  checkResult_incorrect.csv文件中。

  ```cmd
  java -Dfile.encoding=utf-8 -jar folder-md5-check-tool-1.0.0.jar txt params.txt
  ```

  实例params.txt：

  ```pro
  RunMode=2
  StandardMd5Path=standard-md5.txt
  FolderPath=/home
  ExcludedPaths=test||/home/test
  ```

- 从控制台读取参数方式调用。

  ```cmd
  java -Dfile.encoding=utf-8 -jar folder-md5-check-tool-1.0.0.jar console 2 standard-md5.txt /home test||/home/test
  ```

- 输出示例：

  ```cmd
  [root@localhost folder-md5-check-tool]# ./folder-md5-check-tool.sh 
  os=Linux
  RunMode=2
  StandardMd5Path=standard-md5.txt
  FolderPath=/home
  ExcludedPaths=[test, /home/test]
  Done, some files are incorrect, Details in /home/folder-md5-check-tool/checkResult_incorrect.csv,/home/folder-md5-check-tool/checkResult_all.csv
  ```
  
  ```properties
  checkResult_incorrect.csv: 保存异常文件或文件夹信息
  checkResult_all.csv:所有文件的检查结果
  ```