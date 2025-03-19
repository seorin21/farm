# Cryos/farm

[![Kotlin](https://img.shields.io/badge/java-17-ED8B00.svg?logo=java)](https://www.azul.com/)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-585DEF.svg?logo=kotlin)](http://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.2.1-02303A.svg?logo=gradle)](https://gradle.org)
[![GitHub](https://img.shields.io/github/license/seorin21/paper-sample-complex)](https://www.gnu.org/licenses/gpl-3.0.html)
[![Kotlin](https://img.shields.io/badge/youtube-서린-red.svg?logo=youtube)](https://www.youtube.com/@seorin021)

---

> \`[Vultr](https://vultr.com/)\`에서의
> VFS(Cloud Storage -> File System) 사용을 가정하고 제작됐습니다.
> 
> ~~이 플러그인의 저장 파일을 자유롭게 접근하고 싶다면, `mnt/vfs`를 다른 경로로 변경해주십시오.~~
> 

````
   `$ROOT/farm/region/config.yml`
   `$ROOT/farm/region/{$x}:{$z}/` > `info.yml`, `item/${x}:${y}:${z}.yml`
   
   `%ROOT/farm/crops/config/{%CROPS}.yml`
````

<br>

## Cryos Protocol

> 오직, Cryos 서버들끼리만 소통하는 방식입니다. 절대 다른 프로그램과 호환되지 않습니다.


cryos://{server_name}:{server_type}/{target_name}:{target_type}/info/status=full <br>
\>> IF {server_type} = 'bridge' THEN 'bridge' >> 'lobby' || 'farm', {target_name}.info <br>
\>> !IF THEN 'lobby' || 'farm' >>SAVE 'bridge', {target_name}.info

cryos://{server_name}:{server_type}/{target_name}:{target_type}/