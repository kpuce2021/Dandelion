import os

for root, dirs, files in os.walk('/root/Dandelion/Dataset_append/Pepero_Amond'):
    for fname in files:
        full_fname = os.path.join(root, fname)
        print(full_fname)
