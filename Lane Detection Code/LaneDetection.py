# https://youtu.be/eLTLtUVuuy4
import cv2
import numpy as np
import time
import matplotlib.pyplot as plt


def make_coordinates(line_parameter):
    # try:
    slope, intercept = line_parameter
    # except TypeError:
    #     slope, intercept = 0.1, 0.1
    ##print("it happend ")
    y1 = 650
    y2 = 500
    x1 = int((y1 - intercept) / slope)
    x2 = int((y2 - intercept) / slope)

    return np.array([x1, y1, x2, y2])


def average_slope_intercept(image, lines):
    global left_line, right_line
    left_fit = []
    right_fit = []
    if lines is not None:
        for line in lines:
            x1, y1, x2, y2 = line.reshape(4)
            parameters = np.polyfit((x1, x2), (y1, y2), 1)
            slope = parameters[0]
            intercept = parameters[1]
            if slope < 0:
                if 750 < intercept < 1200:
                    left_fit.append((slope, intercept))
            else:
                if -50 < intercept < 150:
                    right_fit.append((slope, intercept))

        if left_fit and right_fit:
            left_fit_average = np.average(left_fit, axis=0)
            left_line = make_coordinates(left_fit_average)
            right_fit_average = np.average(right_fit, axis=0)
            right_line = make_coordinates(right_fit_average)
            # print(right_line[0]-left_line[0])
        elif left_fit and not right_fit:
            print("right not av")
            left_fit_average = np.average(left_fit, axis=0)
            left_line = make_coordinates(left_fit_average)
            right_line = np.array([right_line[0], left_line[1], right_line[2], left_line[3]])
        elif right_fit and not left_fit:
            print("left not av")
            right_fit_average = np.average(right_fit, axis=0)
            right_line = make_coordinates(right_fit_average)
            left_line = np.array([left_line[0], right_line[1], left_line[2], right_line[3]])
        else:
            print("get 2 lanes")
    return np.array([left_line, right_line])


def getSteeringAngle(averagedLines):
    leftX1, leftY1, leftX2, leftY2 = averagedLines[0]
    rightX1, rightY1, rightX2, rightY2 = averagedLines[1]
    temp = rightX2 - leftX2
    centerTop = (temp / 2) + leftX2
    temp2 = rightX1 - leftX1
    centerBottom = (temp2 / 2) + leftX1

    print(centerBottom - centerTop)
    return np.array([centerTop, centerBottom])


def canny(image):
    imgGray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 0)
    imgCanny = cv2.Canny(imgBlur, 50, 150)
    return imgCanny


def region_of_interest(image):
    height = 650
    h, w = image.shape

    polygons = np.array([[(200, height), (1200, height), (620, 430)]])  ##800,0,1500,700
    mask = np.zeros_like(image)
    cv2.fillPoly(mask, polygons, 255)
    maskedImage = cv2.bitwise_and(image, mask)
    return maskedImage


def displayLines(image, lines, steering):
    line_image = np.zeros_like(image)
    if lines is not None:
        leftTurn = 0
        rightTurn = 0
        for line in lines:
            x1, y1, x2, y2 = line.reshape(4)
            parameters = np.polyfit((x1, x2), (y1, y2), 1)
            slope = parameters[0]
            intercept = parameters[1]

            cv2.line(line_image, (x1, y1), (x2, y2), (0, 255, 0), 8)
        cv2.line(line_image, (int(steering[1]) - 30, y1), (int(steering[0]), y2), (0, 0, 255), 8)

        return line_image


cap = cv2.VideoCapture("videoTest2.mp4")

while cap.isOpened():
    _, frame = cap.read()
    gray = canny(frame)
    croppedImage = region_of_interest(gray)
    lines = cv2.HoughLinesP(croppedImage, 3, np.pi / 180, 100, np.array([]), minLineLength=50, maxLineGap=50)
    average_lines = average_slope_intercept(frame, lines)
    steeringAngle = getSteeringAngle(average_lines)
    line_image = displayLines(frame, average_lines, steeringAngle)
    if (lines is not None):
        comboImage = cv2.addWeighted(frame, 0.5, line_image, 1, 1)
    cv2.imshow("after", comboImage)
    # cv2.imshow("sss", croppedImage)

    # time.sleep(0.1)
    cv2.waitKey(1)
    ### to get size
    # plt.imshow(frame)
    # plt.show()
    # cv2.waitKey(0)
