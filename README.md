# Video Confernece(화상 회의 서비스)  
###
##
#### Stack



#### Install
```sh
docker run -d -p 8888:8888/tcp -p 5000-5050:5000-5050/udp -e KMS_MIN_PORT=5000 -e KMS_MAX_PORT=5050 kurento/kurento-media-server

git clone https://github.com/wodyd202/video-conferencing.git
mvn package
mvn spring-boot:run
```

###


#### Features
- 채팅시 이전 메시지에 대한 조회 성능을 향상시키고, 처리량을 높이기 위해 Redis 사용해 구현
- 누적된 채팅 메시지의 개수가 많아지면 많아질 수록 필요하지 않은 이전 채팅 메시지들로 인해 메모리 낭비가 발생할 것이라 판단하여 해당 화상 회의의 누적 채팅 개수가 80개가 되면 20개만 남겨두고 나머지 60개를 DB에 저장하는 방식을 선택
- 회의방에 존재하던 모든 회의자가 나갔을 경우 기존에 남아있던 채팅을 모두 Redis에서 제거하고 DB에 저장
- 회의자는 필요에따라 DB에서 이전 채팅 기록을 조회가능
- 클라이언트의 부하를 덜기위해 Kurento Media Server를 도입하여 SFU 방식으로 재구현

#### Tech
- Spring Boot
- Redis
- h2 database
- Kurento Media Server
- WebRTC

#### Views

|  |  |
| ------ | ------ |
| /login | 로그인 |
| /main | 메인 |
| /conference | 화상 회의 |
