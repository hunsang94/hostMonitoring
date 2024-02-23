# hostMonitoring

빌드
프로젝트 루트경로에서 cmd창 열기
gradlew.bat build 명령어 입력

실행방법
직접 빌드 했을 경우
  /build/libs/hostMonitoring-0.0.1-SNAPSHOT.jar 생성 확인
  빌드된 jar 파일을 /package 경로로 이동

이미 빌드 된 jar 호출할 경우
  /package 디렉터리로 이동

application.yml 파일에서 DB에 연결할 수 있도록 설정값 수정
  datasource.url
  datasource.username
  datasource.password

ddl_query.sql 파일을 실행시켜 데이터베이스 및 테이블 생성

/package 경로로 이동
java -jar hostMonitoring-0.0.1-SNAPSHOT.jar 실행

접속 계정은 2개 제공
admin / 1234
user / 1234

테스트 방법
포스트맨을 이용해 호출 테스트
  /package/Host Monitoring Project.postman_collection.json 
  파일을 포스트맨 컬렉션 추가

  API 정의서를 참고하여 호출



