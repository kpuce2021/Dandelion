from keras.models import Sequential
from keras.layers import Dropout, Activation, Dense
from keras.layers import Flatten, Convolution2D, MaxPooling2D
from keras.models import load_model
import numpy as np

# 분류 카테고리명이 되기에 데이터셋 만들 당시 폴더 명과 동일하게 설정
categories = ['Pepero_Amond', 'Pepero_Crunch', 'Pepero_Original']
num_classes = len(categories)

# 앞에서 만든 데이터셋 불러오기
X_train, X_test, Y_train, Y_test = np.load(
<<<<<<< HEAD
    './imageDataList.npy', allow_pickle=True)

model = Sequential()
model.add(Convolution2D(16, 3, 3, padding='same', activation='relu',
                        input_shape=X_train.shape[1:]))
model.add(MaxPooling2D(2))
model.add(Dropout(0.25))

model.add(Convolution2D(64, 3, 3,  activation='relu'))
model.add(MaxPooling2D(2))
model.add(Dropout(0.25))

model.add(Convolution2D(64, 3, 3))
model.add(MaxPooling2D(2))
=======
    './imageDataList_25.npy', allow_pickle=True)

model = Sequential()
model.add(Convolution2D(16, 3, 3, border_mode='same', activation='relu',
                        input_shape=X_train.shape[1:]))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Dropout(0.25))

model.add(Convolution2D(64, 3, 3,  activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))
model.add(Dropout(0.25))

model.add(Convolution2D(64, 3, 3))
model.add(MaxPooling2D(pool_size=(2, 2)))
>>>>>>> 3f4638c8c22eaf324c15daf5789bbc2670169fba
model.add(Dropout(0.25))

model.add(Flatten())
model.add(Dense(256, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(num_classes, activation='softmax'))

model.compile(loss='binary_crossentropy',
              optimizer='Adam', metrics=['accuracy'])
model.fit(X_train, Y_train, batch_size=32, nb_epoch=100)
score = model.evaluate(X_test, Y_test)
print('loss==>', score[0]*100)
print('accuracy==>', score[1]*100)

# 모델을 저장할 경로와 파일명을 지정한다.
model.save('cnnModel_25.h5')
