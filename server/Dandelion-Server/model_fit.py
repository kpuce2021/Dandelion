from tensorflow-gpu import keras
import numpy as np
import cv2

# 분류 카테고리명이 되기에 데이터셋 만들 당시 폴더 명과 동일하게 설정
categories = ['Pepero_Amond', 'Pepero_Crunch', 'Pepero_Original']
num_classes = len(categories)

# 앞에서 만든 데이터셋 불러오기
X_train, X_test, Y_train, Y_test = np.load(
    './imageDataList.npy', allow_pickle=True)

model = keras.Sequential()
model.add(keras.layers.Conv2D(16, kernel_size=3, padding='same', activation='relu',
                              input_shape=X_train.shape[1:]))
model.add(keras.layers.MaxPooling2D(2))
model.add(keras.layers.Dropout(0.25))

model.add(keras.layers.Conv2D(64, kernel_size=3, activation='relu'))
model.add(keras.layers.MaxPooling2D(2))
model.add(keras.layers.Dropout(0.25))

model.add(keras.layers.Convolution2D(64, kernel_size=3))
model.add(keras.layers.MaxPooling2D(2))
model.add(keras.layers.Dropout(0.25))

model.add(keras.layers.Flatten())
model.add(keras.layers.Dense(256, activation='relu'))
model.add(keras.layers.Dropout(0.5))
model.add(keras.layers.Dense(num_classes, activation='softmax'))

model.compile(loss='sparse_categorical_crossentropy',
              optimizer='adam', metrics='accuracy')
checkpoing_cb = keras.callbacks.ModelCheckpoint('best-cnn-model.h5')
early_stopping_cb = keras.callbacks.EarlyStopping(
    patience=2, restore_best_weights=True)
history = model.fit(X_train, X_test, epochs=100,
                    validation_data=(Y_train, Y_test), callbacks=[checkpoing_cb, early_stopping_cb])

'''
model.fit(X_train, Y_train, batch_size=32, nb_epoch=100)
score = model.evaluate(X_test, Y_test)
print('loss==>', score[0]*100)
print('accuracy==>', score[1]*100)
'''
# 모델을 저장할 경로와 파일명을 지정한다.
model.save('cnnModel_25.h5')
