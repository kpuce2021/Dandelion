from flask import Flask
from flask.json import jsonify
app = Flask(__name__)


@app.route('/')
def hello():
    return 'Hello World!'


@app.route('/API', methods=['POST', 'GET'])
def pred():
    return jsonify({'1st': 'TEST', '2nd': 'POST'})


if __name__ == '__main__':
    app.run()
