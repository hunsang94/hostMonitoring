# Host Monitoring Project

## 호스트들의 Alive 상태 체크 및 모니터링 API 서버

### 개발환경

- Java 17
- Spring boot 3.2
- Spring Security 6.2
- gradle 8.5
- JPA
- MariaDB 10.5

---

### 실행방법

1.  아래 경로에 있는 DDL 쿼리를 실행시켜 데이터베이스를 생성한다.

        ./package/ddl_query.sql

2.  아래 경로에 있는 property 파일을 열어 본인의 DB 환경에 맞게 수정한다.

        ./package/application.yml

        • datasource.url
        • datasource.username
        • datasource.password

3.  프로젝트 루트 경로에 있는 gradlew.bat을 실행하여 빌드한다.

        ./gradlew.bat build

4.  build/libs 경로에 생성된 jar파일을 /package 경로로 이동시킨다.

        mv ./build/libs/hostMonitoring-0.0.1-SNAPSHOT.jar ./package

5.  jar 파일을 실행시켜 서비스를 배포한다.

        java -jar ./package/hostMonitoring-0.0.1-SNAPSHOT.jar

---

### PostMan을 이용한 API 호출 테스트

아래의 json 파일을 postman에 컬렉션으로 추가하여 프로젝트에서 제공하는 API를 테스트할 수 있다.

        /package/Host Monitoring Project.postman_collection.json

---

### 비정상 접속 case에 대한 제한

1. accessToken의 탈취

- 방안) accessToken의 만료기한을 30분으로 제한하고 만료시 refreshToken을 이용해 갱신하도록 제공

2. 로그아웃한 accessToken으로 API 요청

- 방안) 로그아웃 시 해당 accessToken을 블랙리스트에 등록.
- 등록된 토큰으로 API를 요청할 수 없도록 제한
- 해당 기능은 Redis 등 in-memory DB를 활용하여 관리해야 하나 시스템 간소화를 위해 시스템 메모리에 등록하여 활용하도록 제약.
- 블랙리스트에 만료된 토큰이 있다면 제거하도록 기능 구현

---

## REST API 명세서

### Login API

로그인 API

로그인을 통해 accessToken과 refreshToken을 발급받는다.

다른 API를 요청 시 헤더에 다음의 값을 추가해서 요청해야 한다.

`"X-Authorization" : "Bearer " + accessToken`

- AccessToken 유효기간: 30분
- RefreshToken 유효기간: 1일

로그인 할 수 있는 계정은 아래 두개를 제공한다.

| userId | password | role  |
| ------ | -------- | ----- |
| admin  | 1234     | ADMIN |
| user   | 1234     | USER  |

- URL: `api/v1/auth/login`
- Method: `POST`
- Role: `NONE`
- URL Params: `none`
- Data Params:
  - userId [string]
  - password [string]
- Response:
  - accessToken [string]
  - refreshToken [string]
  - message [string]
- Sample Call

      $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/v1/auth/login",
        dataType: "json",
        data: JSON.stringify({
          userId: "admin",
          password: "1234"
        }),
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Logout API

로그아웃 API

로그아웃을 통해 발급받은 AccessToken을 블랙리스트에 등록시켜 폐기한다.

- URL: `api/v1/auth/logout`
- Method: `POST`
- Role: `ALL`
- URL Params: `none`
- Data Params: `none`
- Response:
  [string]
- Sample Call

      $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/v1/auth/logout",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### AccessToken Renewal API

액세스토큰 갱신 API

AccessToken이 만료됐을 때 RefreshToken으로 AccessToken을 갱신받는다.

- URL: `api/v1/auth/renewal`
- Method: `POST`
- Role: `NONE`
- URL Params: `none`
- Data Params:
  - refreshToken [string]
- Response:
  - accessToken [string]
  - refreshToken [string]
  - message [string]
- Sample Call

      $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/v1/auth/renewal",
        dataType: "json",
        data: JSON.stringify({
          refreshToken: refreshToken
        }),
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Get Audit List API

사건 목록 조회 API

사용자가 발생시키는 모든 이벤트를 AOP를 통해 기록하며 이를 조회한다.

- URL: `api/v1/admin/audits`
- Method: `GET`
- Role: `ADMIN`
- URL Params: `none`
- Data Params: `none`
- Response:
  - List[Object]
    - id [Long]
    - eventType [string]
    - clientAddr [string]
    - userId [string]
    - eventResult [string]
    - eventeDateTime [string]
- Sample Call

      $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/auth/audits",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Get Host List API

호스트 목록 조회 API

- URL: `api/v1/hosts`
- Method: `GET`
- Role: `ADMIN`
- URL Params: `none`
- Data Params: `none`
- Response:
  - List[Object]
    - ip [string]
    - name [string]
    - createDateTime [string]
    - updateDateTime [string]
- Sample Call

      $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/hosts",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Get Host API

호스트 단건 조회 API

- URL: `api/v1/host/{ip}/{name}`
- Method: `GET`
- Role: `ADMIN`
- URL Params:
  - ip [string]
  - name [string]
- Data Params: `none`
- Response:
  - ip [string]
  - name [string]
  - createDateTime [string]
  - updateDateTime [string]
- Sample Call

      $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/host/192.168.0.1/domain",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Regist Host API

호스트 등록 API

ip와 name을 복합키로 가지는 호스트 객체를 등록한다.

최대 100개까지 등록이 가능하다.

- URL: `api/v1/host`
- Method: `POST`
- Role: `ADMIN`
- URL Params: `none`
- Data Params:
  - ip [string]
  - name [string]
- Response:
  - success [boolean]
  - message [string]
- Sample Call

      $.ajax({
        type: "POST",
        url: "http://localhost:8080/api/v1/host",
        dataType: "json",
        data: JSON.stringify({
          ip: "192.168.0.1",
      		name: "domain"
        }),
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Update Host API

호스트 수정 API

- URL: `api/v1/host`
- Method: `PUT`
- Role: `ADMIN`
- URL Params: `none`
- Data Params:
  - ip [string]
  - name [string]
- Response:
  - success [boolean]
  - message [string]
- Sample Call

      $.ajax({
        type: "PUT",
        url: "http://localhost:8080/api/v1/host",
        dataType: "json",
        data: JSON.stringify({
          ip: "192.168.0.1",
      		name: "domain"
        }),
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Delete Host API

호스트 삭제 API

- URL: `api/v1/host/{ip}/{name}`
- Method: `DELETE`
- Role: `ADMIN`
- URL Params:
  - ip [string]
  - name [string]
- Data Params: `none`
- Response: [string]
- Sample Call

      $.ajax({
        type: "DELETE",
        url: "http://localhost:8080/api/v1/host/192.168.0.1/domain",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Crruent Host-Status API

호스트 현재 상태 단건 조회 API

현재 호스트의 연결 상태를 조회한다.

연결상태는 "Reachable" 또는 "Unreachable"으로 반환한다.

- URL: `/api/v1/status/current/{ip}/{name}`
- Method: `GET`
- Role: `ALL`
- URL Params:
  - ip [string]
  - name [string]
- Data Params: `none`
- Response:
  - "Reachable" or "Unreachable"
- Sample Call

      $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/status/current/192.168.0.1/domain",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

---

### Host Monitoring API

호스트 상태 모니터링 조회 API

- URL: `api/v1/monitoring/hosts`
- Method: `GET`
- Role: `ALL`
- Query Params:
  - ip [string] [required=false]
  - name [string] [required=false]
- Data Params: `none`
- Response:
  - List[Object]
    - ip [string]
    - name [string]
    - status [string]
    - lastAliveTime [string]
- Sample Call

      • 단건 조회
      $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/monitoring/hosts?ip=192.168.0.1&name=domain",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });

      • 전체 조회
      $.ajax({
        type: "GET",
        url: "http://localhost:8080/api/v1/monitoring/hosts",
        beforeSend: function (xhr) {
          xhr.setRequestHeader("Content-type","application/json");
          xhr.setRequestHeader("X-Authorization","Bearer " + accessToken);
        },
        success: function (res) {
          console.log(res);
        }
      });
