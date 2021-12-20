import numpy as np
import cv2
import base64
from  PIL import Image, ImageDraw
import io

def main(imgOriginal,img,val):
    decoded_data1=base64.b64decode(imgOriginal)
    np_data1=np.fromstring(decoded_data1,np.uint8)
    imgOriginal=cv2.imdecode(np_data1,cv2.IMREAD_UNCHANGED)

    decoded_data2=base64.b64decode(img)
    np_data2=np.fromstring(decoded_data2,np.uint8)
    img=cv2.imdecode(np_data2,cv2.IMREAD_UNCHANGED)
    
    alpha = val/100
    beta = (1.0 - alpha)
    img_alpha = cv2.addWeighted(img, alpha, imgOriginal, beta, 0.0)
    img_alpha = cv2.cvtColor(img_alpha,cv2.COLOR_BGR2RGB)
    
    pil_im=Image.fromarray(img_alpha)
    #convert image to Byte
    buff=io.BytesIO()
    pil_im.save(buff,format="PNG")
    #conver it again to base64
    img_str=base64.b64encode(buff.getvalue())
    return ""+str(img_str,'utf-8')