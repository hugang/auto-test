## how to use

- use selenium ide to record your test case and save as side file
- modify user.properties and exec auto-test.bat
```
input.media=side
side.file.path=C:/projs/auto-test/testcases/a.side
```

## errors
- 指定了默认的用户目录，需要指定一个不存在的目录，否则会报错

```
unknown error: DevToolsActivePort file doesn't exist
```

- 访问权限不足，需要添加参数

```
警告: Invalid Status code=403 text=Forbidden
java.io.IOException: Invalid Status code=403 text=Forbidden
```

```
options.addArguments("--remote-allow-origins=*")
```

- 版本不匹配，需要重新下载对应版本的chromedriver

```
警告: Unable to find an exact match for CDP version 111, so returning the closest version found: 110
```

```
https://chromedriver.chromium.org/downloads
```
