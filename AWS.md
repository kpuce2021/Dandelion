# AWS로 서버 구동하는 법

## EC2 생성
EC2를 생성할 때, 기본 Free Tier를 사용하는 경우, tensorflow 라이브러리가 설치가 되지 않음.
DeepLearning AMI로 생성을 해야함.
- EC2 생성 시 해야하는 설정
	> 보안규칙: TCP, 포트번호 5000, 어디에서나

## 키 생성
EC2를 생성할 때 처음 발급 받는 키는 pem 확장자이다.
PuTTY를 이용하여 EC2에 접근하기 위해서는 pem이 아닌 private key인 ppk 확장자가 필요하다.
- PuTTY 다운로드
	> https://www.chiark.greenend.org.uk/~sgtatham/putty/
- PuTTYgen 실행
	> Type of key to generate에서 RSA 선택
	> -  RSA 항목이 없는 경우, SHA-2 RSA 선택
	
	> 그 후, load를 누르면 파일 확장자가 ppk만 보여주도록 설정되어 있을텐데, 해당 부분을 All Files로 바꾼 후, pem 파일을 load 한다.
	
	> Save private key를 선택하고 뜨는 팝업 창에서 yes를 선택

## PuTTY를 이용한 EC2 연결
- PuTTY 실행
	> Session 설정
	> - Host Name (or IP address) : EC2 대시보드에서 퍼블릭 DNS 이름 또는 퍼블릭 IPv4 주소 입력
	> -  Port: 22
	> - Connection type: SSH
	
	> Connection - SSH - Auth 설정
	> - Authentication parameters - Browse에서 ppk 파일 로드
	
	> Session에서 Open 
	
- EC2 연결
	> 사용자 이름으로 'ubuntu' 입력

## 첫 연결 시 필요한 설정
- 패키지 업데이트 확인 및 진행
	```
	sudo apt-get update
	sudo apt-get upgrade
	```
- 파이썬 버전 확인 및 업데이트, 가상환경 설치
	```
	python3 --version
	sudo apt-get install python 3.5
	sudo apt-get install pyhon3-pip
	sudo apt-get install virtualenv
	```

- 가상환경 실행
	```
	pyenv activate dandelion
	```

- 가상환경 내부 설정
	```
	pip install flask
	pip install tensorflow
	```
	> 그 외 필요한 라이브러리 설치

## 이후 접속할 경우
- 가상환경 실행
	```
	pyenv activate dandelion
	```
- 서버 배포
	```
	python server.py
	```
	> 여기서 server.py는 배포할 파일 명을 입력하면 된다.
- 서버 종료
	> 인스턴스 중지

## 가상환경 내에서 파일을 수정하길 원하는 경우
- 코드
	```
	sudo nano server.py
	```
- 단축키
	```
	저장: ctrl+o
	나가기: ctrl+x
	```

## 로컬 컴퓨터에서 EC2로 파일을 옮기길 원하는 경우
- WinSCP로 접속
	> 로그인 설정
	> - 파일 프로토콜: SFTP
	> - Host Name (or IP address) : EC2 대시보드에서 퍼블릭 DNS 이름 또는 퍼블릭 IPv4 주소 입력
	> - Port: 22
	> - 사용자 이름: ubuntu
	> - 비밀번호: 공란
	> - 고급: SSH-인증 탭의 인증 매개변수에서 ppk 파일 로드 후 확인
- WinSCP로 접속 후
	> 로컬 컴퓨터에서 EC2로 파일 드래그 앤 드롭으로 옮길 수 있음.
