import tensorflow as tf
from keras_preprocessing.image import ImageDataGenerator, array_to_img, img_to_array, load_img
import numpy as np
import os
from os import listdir
from os.path import isfile, join
from PIL import Image

with tf.device('/gpu:0'):
    np.random.seed(3)


    data_datagen = ImageDataGenerator(rescale=1./255)

    data_datagen = ImageDataGenerator(rescale=1./255, rotation_range=30, shear_range=5.5,
                                      zoom_range=0., horizontal_flip=False, vertical_flip=False, fill_mode='nearest')


    filename_in_dir = []

    for root, dirs, files in os.walk('/root/Dandelion/Dataset/Pepero_Original'):
        for fname in files:
            full_fname = os.path.join(root, fname)
            filename_in_dir.append(full_fname)

    for file_image in filename_in_dir:
        print(file_image)
        img = load_img(file_image)
        x = img_to_array(img)
        x = x.reshape((1,) + x.shape)

        i = 0
        for batch in data_datagen.flow(x, save_to_dir='/root/Dandelion/Dataset_append', save_prefix='Pepero_Original', save_format='jpg'):
            i += 1
            if i > 3:
                break
