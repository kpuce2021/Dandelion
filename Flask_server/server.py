
from logging import debug
import flask
import json
import io
import torch
from flask import Flask, request, render_template
from flask.json import jsonify
from keras.models import load_model
import numpy as np
from PIL import Image
from flask_restful import Resource, Api
import translateProductName

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False  # jsonify에서 한글사용
api = Api(app)


# def translateName(enProductList, transProductList, productName):
#     if productName in enProductList:
#         return transProductList[enProductList.index(productName)]
#     else:
#         return "없는 내용 입니다"


# productEnName = ["Pepero_Amond", "Pepero_Crunch", "Pepero_Original"]

# productKorName = ["빼빼로 아몬드", "빼빼로 크런키", "빼빼로 오리지날"]

productEnName = translateProductName.productEnName
productKorName = translateProductName.productKorName


@app.route('/API', methods=['POST', 'GET'])
def pred():
    if not request.method == "POST":
        return

    if request.files.get("image"):
        image_file = request.files["image"]
        image_bytes = image_file.read()

        img = Image.open(io.BytesIO(image_bytes))

        results = model(img)  # reduce size=320 for faster inference
        js_result = json.loads(
            results.pandas().xyxy[0].to_json(orient="records"))

        # print(results.pandas().xyxy[0].to_json(orient="records"))
        # print(js_result[0]['name'])
        # print(translateName(productEnName, productKorName, js_result[0]['name']))
        for i in range(len(js_result)):
            js_result[i]['name'] = translateProductName.translateName(
                productEnName, productKorName, js_result[i]['name'])
        # print(js_result)
        # return results.pandas().xyxy[0].to_json(orient="records")
        return str(js_result)


if __name__ == '__main__':
    # 모델 로드
    model = torch.hub.load(
        "contea95/Yolo_Practice_Repository", "custom")
    model.conf = 0.60
    model.iou = 0.2
    # 공유기 사용 시 포트포워딩 필요
    app.run(host='0.0.0.0', port=5000, debug=True)
