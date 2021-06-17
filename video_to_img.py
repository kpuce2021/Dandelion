import cv2
import os


def Rotate(src, degrees):
    if degrees == 90:
        dst = cv2.transpose(src)
        dst = cv2.flip(dst, 1)

    elif degrees == 180:
        dst = cv2.flip(src, -1)

    elif degrees == 270:
        dst = cv2.transpose(src)
        dst = cv2.flip(dst, 0)
    else:
        dst = null
    return dst


def createFolder(directory):
    try:
        if not os.path.exists(directory):
            os.makedirs(directory)
    except OSError:
        print('Error: Creating directory. ' + directory)


filecount = 0
for i in range(1, 37):
    name = str(i)

    videofile = '/Users/han/Downloads/mov/' + name + ".MOV"

    cam = cv2.VideoCapture(videofile)

    currentFrame = 0
    while(True):
        ret, frame = cam.read()
        if ret:
            #createFolder('/Users/han/Downloads/save/' + name)
            img = Rotate(frame, 90)
            cv2.imwrite('/Users/han/Downloads/save/' +
                        str(filecount) + '.jpg', img)
            filecount += 1
            currentFrame += 1
            print(currentFrame)
        else:
            break
    cam.release()
