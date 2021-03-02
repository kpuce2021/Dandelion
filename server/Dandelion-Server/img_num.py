import os

path_dir = '/root/Dandelion/Dataset_append'
dir_list = os.listdir(path_dir)

for i in dir_list:
    if '.ipynb_' in i:
        dir_list.remove(i)

for i in dir_list:
    searchPath = path_dir + '/' + i
    FileList = os.listdir(searchPath)
    print("{} 이미지 개수 : {}".format(i, str(len(FileList))))