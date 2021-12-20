import numpy as np
import cv2
import base64
from  PIL import Image, ImageDraw
import io

def main(img1,img2):
    decoded_data1=base64.b64decode(img1)
    np_data1=np.fromstring(decoded_data1,np.uint8)
    img1=cv2.imdecode(np_data1,cv2.IMREAD_UNCHANGED)

    decoded_data2=base64.b64decode(img2)
    np_data2=np.fromstring(decoded_data2,np.uint8)
    img2=cv2.imdecode(np_data2,cv2.IMREAD_UNCHANGED)
    
    # font
    font = cv2.FONT_HERSHEY_SIMPLEX
    # org
    org = (20, 40)
    # fontScale
    fontScale = 1
    # Blue color in BGR
    color = (255, 0, 0)
    # Line thickness of 2 px
    thickness = 2
    # Using cv2.putText() method
    cv2.putText(img1, 'Before', org, font, fontScale, color, thickness, cv2.LINE_AA)
    
    # font
    font = cv2.FONT_HERSHEY_SIMPLEX
    # org
    org = (20, 40)
    # fontScale
    fontScale = 1
    # Blue color in BGR
    color = (0, 0, 255)
    # Line thickness of 2 px
    thickness = 2
    # Using cv2.putText() method
    cv2.putText(img2, 'After', org, font, fontScale, color, thickness, cv2.LINE_AA)
    
    res = np.hstack((img1,img2))
    res = cv2.cvtColor(res,cv2.COLOR_BGR2RGB)
    
    pil_im=Image.fromarray(res)
    #convert image to Byte
    buff=io.BytesIO()
    pil_im.save(buff,format="PNG")
    #conver it again to base64
    img_str=base64.b64encode(buff.getvalue())
    return ""+str(img_str,'utf-8')