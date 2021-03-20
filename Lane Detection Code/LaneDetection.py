# https://youtu.be/eLTLtUVuuy4
import cv2
import numpy as np
import time
import matplotlib.pyplot as plt


def make_coordinates(image, line_parameter):
    try:
        slope, intercept = line_parameter
    except TypeError:
        slope, intercept = 0.1, 0.1
    y1 = 650
    y2 = int(y1 * (0.80))
    x1 = int((y1 - intercept) / slope)
    x2 = int((y2 - intercept) / slope)
    return np.array([x1, y1, x2, y2])



def average_slope_intercept(image, lines):
    left_fit = []
    right_fit = []
    if lines is not None:
        for line in lines:
            x1, y1, x2, y2 = line.reshape(4)
            parameters = np.polyfit((x1, x2), (y1, y2), 1)
            slope = parameters[0]
            intercept = parameters[1]
            print(x1)
            if slope < 0:
                left_fit.append((slope, intercept))
            else:
                 right_fit.append((slope, intercept))

    if left_fit:
        left_fit_average = np.average(left_fit, axis=0)
        left_line = make_coordinates(image, left_fit_average)
    if right_fit:
        right_fit_average = np.average(right_fit, axis=0)
        right_line = make_coordinates(image, right_fit_average)
    return np.array([left_line, right_line])


def canny(image):
    imgGray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 0)
    imgCanny = cv2.Canny(imgBlur, 50, 150)
    return imgCanny


def region_of_interest(image):
    height = 650
    polygons = np.array([[(200, height), (1200, height), (620, 430)]])  ##800,0,1500,700
    mask = np.zeros_like(image)
    cv2.fillPoly(mask, polygons, 255)
    maskedImage = cv2.bitwise_and(image, mask)
    return maskedImage


def displayLines(image, lines):
    line_image = np.zeros_like(image)
    if lines is not None:
        for line in lines:
            x1, y1, x2, y2 = line.reshape(4)
            parameters = np.polyfit((x1, x2), (y1, y2), 1)
            slope = parameters[0]
            intercept = parameters[1]
           # if y2 > 580 and 100 < x2 < 400 or 1000 < x2 < 1200 and 250 < x1 < 650 or 650 < x1 < 1200 and slope is not 0 and intercept < 700 or intercept >700 :
            cv2.line(line_image, (x1, y1), (x2, y2), (0, 255, 0), 8)
                # cv2.line(line_image, (725, 400), (725, 650), (255, 0, 0), 8)

        return line_image


# img = cv2.imread("test8.jpg")
# canny = canny(img)
# croppedImage = region_of_interest(canny)
# lines = cv2.HoughLinesP(croppedImage, 4, np.pi / 180, 50, np.array([]), minLineLength=10,
#                         maxLineGap=10)  # 2.180.100.40.5
# average_lines = average_slope_intercept(img, lines)
# line_image = displayLines(img, average_lines)
# line_image2 = displayLines(img, lines)
# comboImage = cv2.addWeighted(img, 0.3, line_image, 1, 1)
# comboImage2 = cv2.addWeighted(img, 0.3, line_image2, 1, 1)
# cv2.imshow("before", comboImage2)
# cv2.imshow("after", comboImage)
# cv2.waitKey(0)


cap = cv2.VideoCapture("videoTest2.mp4")

while cap.isOpened():
    _, frame = cap.read()
    gray = canny(frame)
    croppedImage = region_of_interest(gray)
    lines = cv2.HoughLinesP(croppedImage, 3, np.pi / 180, 100, np.array([]), minLineLength=50, maxLineGap=50)
    average_lines = average_slope_intercept(frame, lines)
    line_image = displayLines(frame, average_lines)
    if(lines is not None):
        comboImage = cv2.addWeighted(frame, 0.5, line_image, 1, 1)
    cv2.imshow("after", comboImage)
    cv2.imshow("sss", croppedImage)

    time.sleep(0.1)
    cv2.waitKey(1)
    ### to get size
    #plt.imshow(frame)
    #plt.show()
    #cv2.waitKey(0)
