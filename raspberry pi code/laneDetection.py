# https://youtu.be/eLTLtUVuuy4
import cv2
import numpy as np
import time
import matplotlib.pyplot as plt
import concurrent.futures
import serial




def make_coordinates(line_parameter):
    slope, intercept = line_parameter
    y1 = 500
    y2 = 250
    x1 = int((y1 - intercept) / slope)
    x2 = int((y2 - intercept) / slope)

    return np.array([x1, y1, x2, y2])


def average_slope_intercept(image, lines):
    global left_line, right_line, missingLane 
    left_fit = []
    right_fit = []
    if lines is not None:
        for line in lines:
            x1, y1, x2, y2 = line.reshape(4)
            parameters = np.polyfit((x1, x2), (y1, y2), 1)
            slope = parameters[0]
            intercept = parameters[1]
            if slope < 0:
                #if 300 < intercept < 500:
                    left_fit.append((slope, intercept))
            else:
                #if -500 < intercept < 200:
                    right_fit.append((slope, intercept))
        if left_fit and right_fit:
            left_fit_average = np.average(left_fit, axis=0)
            left_line = make_coordinates(left_fit_average)
            right_fit_average = np.average(right_fit, axis=0)
            right_line = make_coordinates(right_fit_average)
            missingLane = 0
        elif left_fit and not right_fit:
            left_fit_average = np.average(left_fit, axis=0)
            left_line = make_coordinates(left_fit_average)
            right_line = np.array([right_line[0], right_line[1], right_line[2], right_line[3]])
            missingLane = 1
        elif right_fit and not left_fit:
            right_fit_average = np.average(right_fit, axis=0)
            right_line = make_coordinates(right_fit_average)
            left_line = np.array([left_line[0], left_line[1], left_line[2], left_line[3]])   
            missingLane = 2
    return np.array([left_line, right_line])


def getSteeringAngle(averagedLines):
    global lastSteering
    leftX1, leftY1, leftX2, leftY2 = averagedLines[0]
    rightX1, rightY1, rightX2, rightY2 = averagedLines[1]
    centerPoint = 150
    if missingLane == 1:
        centerBottom = (centerPoint +leftX2) 
        print(centerBottom)
    elif missingLane == 2:
        centerBottom = rightX2 - centerPoint
    else:
        centerBottom = ((rightX2 - leftX2) / 2) + leftX2
        
    return int(((300-centerBottom) + lastSteering)/2)


def canny(image):
    imgGray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 0)
    imgCanny = cv2.Canny(imgBlur, 50, 150)
    return imgCanny


def region_of_interest(image):
    height = 500
    h, w = image.shape
    polygons = np.array([[(-300, height), (900, height), (550, 200) , (100, 200)]])  ##800,0,1500,700
    mask = np.zeros_like(image)
    cv2.fillPoly(mask, polygons, 255)
    maskedImage = cv2.bitwise_and(image, mask)
    return maskedImage

def displayLines(image, lines, steering):
    line_image = np.zeros_like(image)
    if lines is not None:
        try:
            for line in lines:
                x1, y1, x2, y2 = line.reshape(4)
                cv2.line(line_image, (x1, y1), (x2, y2), (0, 255, 0), 8)
            cv2.line(line_image, ( 250, 400+steering), (350, 400-steering), (0, 0, 255), 8)
        except OverflowError:
            print("No lanes to display")
    return line_image
count =0
lastSteering =0
ser = serial.Serial("/dev/ttyS0",9600)
cap = cv2.VideoCapture(-1)
while cap.isOpened():
    _, frame = cap.read()
    #frame=cv2.flip(frame,-1)
    gray = canny(frame)
    croppedImage = region_of_interest(gray)
    lines = cv2.HoughLinesP(croppedImage, 2, np.pi / 180, 20, np.array([]), minLineLength=5, maxLineGap=50)
    if (lines is not None):
        average_lines = average_slope_intercept(frame, lines)
        steeringAngle = getSteeringAngle(average_lines)
        ser.write(str.encode(str(steeringAngle)))
        ser.write(str.encode('E'))
        line_image = displayLines(frame, average_lines, steeringAngle)
        comboImage = cv2.addWeighted(frame, 0.5, line_image, 1, 1)
    else:
        count +=1
        comboImage = frame
        if count == 5:
            count =0
            ser.write(str.encode("Autopilot is Disabled"))
        
    cv2.imshow("after", comboImage)

    #time.sleep(0.1)
    cv2.waitKey(1)
    ### to get size
    # plt.imshow(frame)
    # plt.show()
    # cv2.waitKey(0)
