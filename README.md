# SkinSync

Minecraft 1.20.1 Forge 的客户端/服务端皮肤同步 mod。

## 功能

- 客户端按 K 打开上传界面，选择本地 PNG
- 客户端把图片发给服务端
- 服务端保存并按 UUID 提供 HTTP 图片地址
- 服务端把皮肤写进玩家的 textures 属性
- 原版客户端（没装 mod）也能看到皮肤
- 支持 64x64、64x32、128x128 PNG，支持细手臂模型

## 构建

推荐用 GitHub Actions：

1. 在 GitHub 新建一个空仓库
2. 把本目录推上去
3. Actions 会自动运行 Build SkinSync
4. 在 Actions 运行记录的 Artifacts 里下载 skinsync-jar

本地构建需要能访问 Forge maven，执行：

    ./gradlew build

产物在 build/libs/。

## 安装

服务端：
- 把 jar 放进 mods/
- 启动后生成 config/skinsync-server.toml
- 把 publicBaseUrl 改成玩家能访问的地址，例如：
  http://10.227.127.36:25567
- 确保 25567/TCP 对玩家可达（ZeroTier 或公网）
- 重启服务器

客户端：
- 把同一个 jar 放进 mods/
- 进游戏按 K

## 使用

1. 准备一张 64x64 的 PNG
2. 按 K，填写文件路径
3. 选择是否细手臂
4. 点击上传
5. 其他玩家重新登录后可见；原版客户端也会显示

## 配置

config/skinsync-server.toml：

- httpPort：皮肤图片 HTTP 端口，默认 25567
- publicBaseUrl：客户端下载皮肤使用的地址
- maxKb：上传大小上限，默认 256
- allowUploads：是否允许玩家上传

## 已知限制

- 皮肤变更后，其他在线玩家可能要重新登录才能刷新
- 需要额外的 HTTP 端口可达
- 只处理皮肤，不处理披风/鞘翅
