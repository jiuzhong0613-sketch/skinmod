<div align="center">

# 🎨 SkinSync

**Minecraft 1.20.1 Forge 皮肤上传与同步**

[中文](#中文) · [English](#english)

[![Build](https://img.shields.io/badge/build-GitHub%20Actions-2088FF?style=flat-square&logo=github-actions&logoColor=white)](https://github.com/jiuzhong0613-sketch/skinmod/actions)
![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-3C8527?style=flat-square)
![Forge](https://img.shields.io/badge/Forge-47.4.0-F16436?style=flat-square)
![Java](https://img.shields.io/badge/Java-17-007396?style=flat-square)
![Side](https://img.shields.io/badge/side-client%20%2B%20server-6f42c1?style=flat-square)
![License](https://img.shields.io/badge/license-MIT-green?style=flat-square)

</div>

---

<a id="中文"></a>

## 中文

SkinSync 让玩家在游戏里上传自己的 PNG 皮肤。服务端保存图片，通过 HTTP 提供下载地址，并把皮肤写进玩家的 textures 属性；装了模组的客户端和未装模组的客户端都能显示（前提是能进入服务器）。

### ✨ 功能

| 能力 | 说明 |
|---|---|
| 按 K 上传 | 客户端打开上传界面，填写本地 PNG 路径 |
| 手臂模型 | 支持经典和细手臂 |
| 服务端校验 | 校验 PNG 格式、尺寸和大小 |
| 按 UUID 保存 | 每个玩家一份皮肤，存在 config/skinsync/skins/ |
| HTTP 分发 | 通过 /skins/&lt;uuid&gt;.png 提供图片 |
| 未装模组可见 | 皮肤写进 textures 属性，客户端不需要本模组也能显示 |
| 在线刷新 | 上传后尝试刷新在线玩家列表 |

### 🏗️ 架构

<pre>
客户端 K 界面 ──上传包──▶ 服务端 ──保存──▶ config/skinsync/skins/
                                      │
                                      ├──HTTP /skins/&lt;uuid&gt;.png──▶ 客户端
                                      └──textures 属性注入──▶ 玩家列表
</pre>

### 📋 环境

- Minecraft 1.20.1
- Forge 47.4.0
- Java 17
- 服务端 25567/TCP 对玩家可达

### 📦 安装

服务端：

1. 把 jar 放进 mods/
2. 启动服务器，生成 world/serverconfig/skinsync-server.toml
3. 把 publicBaseUrl 改成玩家能访问的地址，例如 http://your-server:25567
4. 确保 25567/TCP 对玩家可达（ZeroTier 或公网）
5. 重启服务器

客户端：

1. 把同一个 jar 放进 mods/
2. 进游戏按 K

### 🎮 使用

1. 准备 PNG：64x64、64x32 或 128x128，默认不超过 256KB
2. 按 K
3. 填写图片完整路径
4. 选择细手臂或经典手臂
5. 点击上传
6. 上传成功后服务端会尝试刷新在线玩家；如果没刷新，重新登录

### ⚙️ 配置

文件：world/serverconfig/skinsync-server.toml（关服后修改）

| 配置项 | 默认值 | 说明 |
|---|---:|---|
| httpPort | 25567 | 皮肤图片 HTTP 端口 |
| publicBaseUrl | http://127.0.0.1:25567 | 客户端下载皮肤使用的地址，必须改成玩家可达地址 |
| maxKb | 256 | 上传 PNG 大小上限 |
| allowUploads | true | 是否允许玩家上传 |

### 🌐 HTTP 接口

- GET /skins/&lt;uuid&gt;.png
- 200：返回 image/png
- 400：UUID 格式错误
- 404：文件不存在，或后缀不是 .png
- 响应头带 Cache-Control: no-cache

### 💾 数据

- config/skinsync/skins/&lt;uuid&gt;.png
- config/skinsync/skins/&lt;uuid&gt;.json（name、slim、updated）

### 🔨 构建

GitHub Actions 自动构建，产物是 Actions 里的 skinsync-jar。

本地构建需要 Java 17：

    export JAVA_HOME="$HOME/java/jdk-17/Contents/Home"
    ./gradlew build --no-daemon

产物：

    build/libs/

### ⚠️ 已知限制

- 皮肤更新后，部分客户端可能需要重新登录才刷新
- 需要额外开放 25567/TCP
- 只处理皮肤，不处理披风/鞘翅
- HTTP 不是 HTTPS；能访问 25567 的人都能按 UUID 拉取皮肤
- 离线模式下玩家名和 UUID 可伪造，不能当强身份

### 🔐 安全建议

- 不需要上传时把 allowUploads 设为 false
- 只对 ZeroTier/白名单网络开放 25567，不要直接映射到公网
- 保持 maxKb 和尺寸校验，避免大文件或畸形图片
- 皮肤 HTTP 没有鉴权，按“可达网络内公开”设计

### 📄 License

MIT

---

<a id="english"></a>

## English

SkinSync lets players upload their own PNG skin in game. The server stores the image, serves it over HTTP and injects it into the player's textures property. Both modded and unmodded clients can display it, as long as they can join the server.

### ✨ Features

| Feature | Description |
|---|---|
| Press K to upload | Opens a client GUI and accepts a local PNG path |
| Arm model | Classic and slim arms |
| Server-side validation | Checks PNG format, dimensions and size |
| Per-UUID storage | One skin per player under config/skinsync/skins/ |
| HTTP delivery | Serves the image at /skins/&lt;uuid&gt;.png |
| Visible without the mod | The skin is injected into the textures property, so clients without this mod can display it |
| Online refresh | Tries to refresh online players after an upload |

### 🏗️ Architecture

<pre>
Client K screen ──upload packet──▶ server ──save──▶ config/skinsync/skins/
                                      │
                                      ├──HTTP /skins/&lt;uuid&gt;.png──▶ clients
                                      └──textures property──▶ player list
</pre>

### 📋 Requirements

- Minecraft 1.20.1
- Forge 47.4.0
- Java 17
- Server port 25567/TCP reachable by players

### 📦 Installation

Server:

1. Put the jar into mods/
2. Start the server; world/serverconfig/skinsync-server.toml is generated
3. Change publicBaseUrl to an address players can reach, for example http://your-server:25567
4. Make 25567/TCP reachable (ZeroTier or public)
5. Restart the server

Client:

1. Put the same jar into mods/
2. Press K in game

### 🎮 Usage

1. Prepare a PNG: 64x64, 64x32 or 128x128, 256KB or smaller by default
2. Press K
3. Enter the full path to the image
4. Choose slim or classic arms
5. Click upload
6. The server tries to refresh online players; if the skin does not update, relog

### ⚙️ Configuration

File: world/serverconfig/skinsync-server.toml (edit while the server is stopped)

| Option | Default | Description |
|---|---:|---|
| httpPort | 25567 | HTTP port for skin images |
| publicBaseUrl | http://127.0.0.1:25567 | Base URL clients use to download skins; must be reachable by players |
| maxKb | 256 | Maximum uploaded PNG size |
| allowUploads | true | Allow players to upload skins |

### 🌐 HTTP API

- GET /skins/&lt;uuid&gt;.png
- 200: image/png
- 400: invalid UUID
- 404: file not found, or the suffix is not .png
- Response includes Cache-Control: no-cache

### 💾 Data

- config/skinsync/skins/&lt;uuid&gt;.png
- config/skinsync/skins/&lt;uuid&gt;.json (name, slim, updated)

### 🔨 Build

GitHub Actions builds the jar automatically; download the skinsync-jar artifact from the Actions run.

Local build requires Java 17:

    export JAVA_HOME="$HOME/java/jdk-17/Contents/Home"
    ./gradlew build --no-daemon

Output:

    build/libs/

### ⚠️ Known limitations

- Some clients may need to relog before a skin update appears
- Port 25567/TCP must be reachable
- Skins only; no capes or elytra
- HTTP, not HTTPS; anyone who can reach 25567 can fetch a skin by UUID
- In offline mode names and UUIDs can be spoofed; do not treat them as strong identity

### 🔐 Security notes

- Set allowUploads to false when uploads are not needed
- Expose 25567 only to ZeroTier or a trusted network; do not map it directly to the public internet
- Keep maxKb and dimension validation enabled to reject large or malformed images
- The skin HTTP endpoint has no authentication; treat it as public within the reachable network

### 📄 License

MIT
