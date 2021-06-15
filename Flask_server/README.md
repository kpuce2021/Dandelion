# Yolo_Practice_Repository
Yolo 객체인식을 연습하는 레포지토리

## 사용방법

1. /Users/(유저이름)/.cache/torch/hub/contea95_Yolo_Practice_Repository_master 경로에 있는 hubconf.py 수정
- def custom(path = ' ') 에서 path를 학습 모델이 저장되어 있는 경로로 지정한다.
- Ex) path = '/Users/han/Documents/GitHub/Yolo_test/model/best.pt'

2. model = custom(path = ' ') 부분도 위와 같이 바꿔준다.
