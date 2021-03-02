import os
import re
import glob
import cv2
import numpy as np
from sklearn.model_selection import train_test_split

# 현재 로컬 이미지 폴더 구조
# /root/Dandelion/Dataset_append/Pepero_Amond, Pepero_Crunch, Pepero_Original

imagePath = '/root/Dandelion/Dataset_append'
categories = ['Pepero_Amond', 'Pepero_Crunch', 'Pepero_Original']

# 하위 폴더 이름이 카테고리가 된다. 동일하게 맞춘다.
nb_classes = len(categories)

image_w = 28
image_h = 28

X = []
Y = []

for idx, cate in enumerate(categories):
    label = [0 for i in range(nb_classes)]
    label[idx] = 1
    image_dir = imagePath+'/'+cate+'/'

    for top, dir, f in os.walk(image_dir):
        for filename in f:
            print(image_dir+filename)
            img = cv2.imread(image_dir+filename)
            img = cv2.resize(img, None, fx=image_w /
                             img.shape[1], fy=image_h/img.shape[0])
            X.append(img/256)
            Y.append(label)

X = np.array(X)
Y = np.array(Y)

X_train, X_test, Y_train, Y_test = train_test_split(X, Y)
xy = (X_train, X_test, Y_train, Y_test)

np.save("./imageDataList.npy", xy)
