import numpy as np
import cv2
from  PIL import Image, ImageDraw
import base64
import io
import face_recognition
from math import hypot
#############################################
def transparentOverlay(src , overlay ,pos=(0,0),eyebrow_width = 1,eyebrow_height = 1):
    overlay = cv2.resize(overlay,(eyebrow_width,eyebrow_height))
    h,w,_ = overlay.shape  # Size of foreground
    rows,cols,_ = src.shape  # Size of background Image
    y,x = pos[0],pos[1]    # Position of foreground/overlay image
    #loop over all pixels and apply the blending equation
    for i in range(h):
        for j in range(w):
            if x+i >= rows or y+j >= cols:
                continue
            alpha = float(overlay[i][j][3]/255) # read the alpha channel
            src[x+i][y+j] = alpha*overlay[i][j][:3]+(1-alpha)*src[x+i][y+j]
    return src
#############################################

def main(data,eyebrows):

    decoded_data1=base64.b64decode(data)
    np_data1=np.fromstring(decoded_data1,np.uint8)
    img=cv2.imdecode(np_data1,cv2.IMREAD_UNCHANGED)

    #img = cv2.resize(img, (0,0),None,0.8,0.8)

    decoded_data2=base64.b64decode(eyebrows)
    np_data2=np.fromstring(decoded_data2,np.uint8)
    brow=cv2.imdecode(np_data2,cv2.IMREAD_UNCHANGED)

    #convert gray image
    #img=cv2.cvtColor(img1,cv2.COLOR_BGR2RGB)
    img_gray=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)
    
    eyebrow_image_R=cv2.cvtColor(brow,cv2.COLOR_BGR2RGB)
    eyebrow_image = cv2.flip(eyebrow_image_R, 1)
    brow2 = cv2.flip(brow, 1)

    #here we find face location with the help of face recognition
    face_landmarks_list = face_recognition.face_landmarks(img_gray)
    if len(face_landmarks_list) == 0:
        return "null"

    mask = np.zeros_like(img_gray)
    for face_landmarks in face_landmarks_list:
        center_eyebrow = (int(face_landmarks['left_eyebrow'][2][0]),int(face_landmarks['left_eyebrow'][2][1])) # 19
        left_eyebrow = (int(face_landmarks['left_eyebrow'][1][0]),int(face_landmarks['left_eyebrow'][1][1])) #18
        right_eyebrow = (int(face_landmarks['left_eyebrow'][4][0]),int(face_landmarks['left_eyebrow'][4][1])) #21
        
        eyebrow_width = int(hypot(left_eyebrow[0] - right_eyebrow[0],left_eyebrow[1]-right_eyebrow[1])*1.2) ########## size eyebrows
        eyebrow_height = int(eyebrow_width * 0.27)  #H / W
        
        eyebrow_width_L = eyebrow_width
        eyebrow_height_L = eyebrow_height       
        
        top_left  = (int(center_eyebrow[0]- eyebrow_width/2),int(center_eyebrow[1]-eyebrow_height/2))
        bottom_right  = (int(center_eyebrow[0]+eyebrow_width/2),int(center_eyebrow[1]+eyebrow_height/2))
        position_left = top_left
        
        #cv2.circle(img_copy,(landmarks.part(17).x,landmarks.part(19).y),2,(0,255,0),cv2.FILLED)
       
        position_top_L =(int(face_landmarks['left_eyebrow'][0][0]),int(face_landmarks['left_eyebrow'][2][1]-5)) ######## ตีกรอบที่จะลบคิ้ว 17 19
        position_bottom_L =(int(face_landmarks['left_eyebrow'][4][0]),int(face_landmarks['left_eyebrow'][0][1]+3)) ##### มาร์กเพื่อจะลบคิ้ว 21 17
        
        position_top_L_new =(int(face_landmarks['left_eyebrow'][0][0]),int(face_landmarks['left_eyebrow'][2][1])) ########
        position_bottom_L_new =(int(face_landmarks['left_eyebrow'][4][0]),int(face_landmarks['left_eyebrow'][0][1])) ##### 
        
    # =============================================================================
    #     cv2.rectangle(img_copy, position_top_L, position_bottom_L, 255,1)
        cv2.rectangle(mask, position_top_L, position_bottom_L, 255,-1)
    # =============================================================================
        ####################################
        
        center_eyebrow = (face_landmarks['right_eyebrow'][2][0],face_landmarks['right_eyebrow'][2][1]) #24 2
        left_eyebrow = (face_landmarks['right_eyebrow'][0][0],face_landmarks['right_eyebrow'][0][1]) #22 0
        right_eyebrow = (face_landmarks['right_eyebrow'][3][0],face_landmarks['right_eyebrow'][3][1]) #25 3
        
        eyebrow_width = int(hypot(left_eyebrow[0] - right_eyebrow[0],left_eyebrow[1]-right_eyebrow[1])*1.2) ########## size eyebrows
        eyebrow_height = int(eyebrow_width * 0.27)  #H / W
        
        eyebrow_width_R = eyebrow_width
        eyebrow_height_R = eyebrow_height
        
        top_left  = (int(center_eyebrow[0]- eyebrow_width/2),int(center_eyebrow[1]-eyebrow_height/2))
        bottom_right  = (int(center_eyebrow[0]+eyebrow_width/2),int(center_eyebrow[1]+eyebrow_height/2))
        position_right = top_left
           
        #cv2.circle(img_copy,(landmarks.part(22).x,landmarks.part(24).y),2,(0,255,0),cv2.FILLED)
        
        position_top_R =(int(face_landmarks['right_eyebrow'][0][0]),int(face_landmarks['right_eyebrow'][2][1]-5)) ######## ตีกรอบที่จะลบคิ้ว 22 24
        position_bottom_R =(int(face_landmarks['right_eyebrow'][4][0]),int(face_landmarks['right_eyebrow'][4][1]+3)) ##### มาร์กเพื่อจะลบคิ้ว 26 26
        
        position_top_R_new =(int(face_landmarks['right_eyebrow'][0][0]),int(face_landmarks['right_eyebrow'][2][1])) ######## ตีกรอบที่จะลบคิ้ว 22 24
        position_bottom_R_new =(int(face_landmarks['right_eyebrow'][4][0]),int(face_landmarks['right_eyebrow'][4][1])) ##### มาร์กเพื่อจะลบคิ้ว 26 26
        
    # =============================================================================
    #     cv2.rectangle(img_copy, position_top_R, position_bottom_R, 255,1)
        cv2.rectangle(mask, position_top_R, position_bottom_R, 255,-1)
    # =============================================================================
        #########################
        Remove_Eyebrows = cv2.inpaint(img, mask, 15, cv2.INPAINT_TELEA) #### Remove Eyebrows
        Remove_Eyebrows_copy = Remove_Eyebrows.copy()
        #########################
        #L
        new_eyebrow_L = cv2.resize(eyebrow_image,(eyebrow_width_L,eyebrow_height_L))
        new_eyebrow_gray_L = cv2.cvtColor(new_eyebrow_L, cv2.COLOR_BGR2GRAY)
        _, new_eyebrow_mask_L1 = cv2.threshold(new_eyebrow_gray_L, 0, 255, cv2.THRESH_BINARY_INV)
        _, new_eyebrow_mask_L2 = cv2.threshold(new_eyebrow_gray_L, 0, 255, cv2.THRESH_BINARY)
        
        eyebrow_area_L1 = img[position_top_L_new[1] : position_top_L_new[1]+eyebrow_height_L, #********
                           position_top_L_new[0] : position_top_L_new[0] + eyebrow_width_L]   #********
        eyebrow_area_no_eyebrow_L1 = cv2.bitwise_and(eyebrow_area_L1, eyebrow_area_L1, mask=new_eyebrow_mask_L2)
        
        eyebrow_area_L2 = Remove_Eyebrows[position_top_L_new[1] : position_top_L_new[1]+eyebrow_height_L, #********
                           position_top_L_new[0] : position_top_L_new[0] + eyebrow_width_L]   #********
        eyebrow_area_no_eyebrow_L2 = cv2.bitwise_and(eyebrow_area_L2, eyebrow_area_L2, mask=new_eyebrow_mask_L1)
        final_eyebrow = cv2.add(eyebrow_area_no_eyebrow_L2,eyebrow_area_no_eyebrow_L1)
        
        Remove_Eyebrows[position_top_L_new[1] : position_top_L_new[1]+eyebrow_height_L
                        ,position_top_L_new[0] : position_top_L_new[0] + eyebrow_width_L] = final_eyebrow
        #########################
        #R
        new_eyebrow_R = cv2.resize(eyebrow_image_R,(eyebrow_width_R,eyebrow_height_R))
        new_eyebrow_gray_R = cv2.cvtColor(new_eyebrow_R, cv2.COLOR_BGR2GRAY)
        _, new_eyebrow_mask_R1 = cv2.threshold(new_eyebrow_gray_R, 0, 255, cv2.THRESH_BINARY_INV)
        _, new_eyebrow_mask_R2 = cv2.threshold(new_eyebrow_gray_R, 0, 255, cv2.THRESH_BINARY)
        
        eyebrow_area_R1 = img[position_top_R_new[1] : position_top_R_new[1]+eyebrow_height_R, #********
                           position_top_R_new[0] : position_top_R_new[0] + eyebrow_width_R]   #********
        eyebrow_area_no_eyebrow_R1 = cv2.bitwise_and(eyebrow_area_R1, eyebrow_area_R1, mask=new_eyebrow_mask_R2)
        
        eyebrow_area_R2 = Remove_Eyebrows[position_top_R_new[1] : position_top_R_new[1]+eyebrow_height_R, #********
                           position_top_R_new[0] : position_top_R_new[0] + eyebrow_width_R]   #********
        eyebrow_area_no_eyebrow_R2 = cv2.bitwise_and(eyebrow_area_R2, eyebrow_area_R2, mask=new_eyebrow_mask_R1)
        final_eyebrow = cv2.add(eyebrow_area_no_eyebrow_R2,eyebrow_area_no_eyebrow_R1)
        
        Remove_Eyebrows[position_top_R_new[1] : position_top_R_new[1]+eyebrow_height_R
                        ,position_top_R_new[0] : position_top_R_new[0] + eyebrow_width_R] = final_eyebrow   
        #####################
        result = transparentOverlay(Remove_Eyebrows_copy,brow2,(face_landmarks['left_eyebrow'][0][0],face_landmarks['left_eyebrow'][2][1]),int(eyebrow_width_L),int(eyebrow_height_L))
        result = transparentOverlay(Remove_Eyebrows_copy,brow,(face_landmarks['right_eyebrow'][0][0],face_landmarks['right_eyebrow'][2][1]),int(eyebrow_width_R),int(eyebrow_height_R))
        
        #alpha = 60/100
        #beta = (1.0 - alpha)
        #test = cv2.addWeighted(Remove_Eyebrows_copy, alpha, Remove_Eyebrows, beta, 0.0)
        test1=cv2.cvtColor(result,cv2.COLOR_BGR2RGB)

    #convert image to PIL image
    pil_im=Image.fromarray(test1)
    #convert image to Byte
    buff=io.BytesIO()
    pil_im.save(buff,format="PNG")
    #conver it again to base64
    img_str=base64.b64encode(buff.getvalue())
    return ""+str(img_str,'utf-8')
