from logging import debug
import flask
from flask import Flask, request, render_template
from keras.models import load_model
import numpy as np
from PIL import Image
from flask_restful import Resource, Api

app = Flask(__name__)
api = Api(app)


@app.route("/")
@app.route("/index")
def index():
    return flask.render_template('index.html')

# 데이터 예측 처리


@app.route('/predict', methods=['POST'])
def make_predictation():
    if request.method == 'POST':

        # 업로드 파일 처리 분기
        file = request.files['image']
        if not file:
            return render_template('index.html', ml_label="No Files")

        # 이미지 픽셀 정보 읽기
        img = Image.open(file)
        img = img.convert("RGB")
        img = img.resize((224, 224))
        img = np.array(img)/255
        img = img.reshape(1, 224, 224, 3)

        # 이미지 예측
        prediction = model.predict(img)

        label = str(np.squeeze(prediction))

        return render_template('index.html', ml_label=label)


if __name__ == '__main__':
    # 모델 로드
    model = load_model('Yolo_Practice_Repository/model/best-cnn-model.h5')
    # 공유기 사용 시 포트포워딩 필요
    app.run(host='0.0.0.0', port=5000, debug=True)
