# https://youtu.be/eLTLtUVuuy4
import cv2
import numpy as np
import matplotlib.pyplot as plt


def make_coordinates(line_parameter):
    try:
     slope, intercept = line_parameter
    except TypeError:
        slope,intercept =-0.5,0.1
    y1= 640
    y2 = int(y1*(0.75))
    x1= int((y1-intercept)/slope)
    x2= int((y2-intercept)/slope)
    return np.array([x1,y1,x2,y2])
def average_slope_intercept(image, line, backup=None, x11=None):
    left_fit = []
    right_fit = []
    for line in line:
        x1 ,y1 ,x2 ,y2 = line.reshape(4)
        parameters = np.polyfit((x1,x2),(y1,y2),1)
        slope = parameters[0]
        intercept = parameters[1]
        if slope < 0:
            left_fit.append((slope, intercept))
        else:
            right_fit.append((slope,intercept))
    left_fit_average = np.average(left_fit,axis=0)
    right_fit_average = np.average(right_fit,axis=0)
    left_line = make_coordinates(left_fit_average)
    right_line = make_coordinates(right_fit_average)

    return np.array([left_line,right_line])

def canny(image):
    imgGray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 0)
    imgCanny = cv2.Canny(imgBlur, 50, 150)
    return imgCanny


def region_of_interest(image):
    height = image.shape[0]
    polygons = np.array([[(100, height), (750, height), (370, 250)]])##800,0,1500,700
    mask = np.zeros_like(image)
    cv2.fillPoly(mask, polygons, 255)
    maskedImage = cv2.bitwise_and(image, mask)
    return maskedImage


def displayLines(image, lines):
    line_image = np.zeros_like(image)
    if lines is not None:

        for x1, y1, x2, y2 in lines:
            cv2.line(line_image, (x1, y1), (x2, y2), (0, 255, 0), 8)
               # cv2.line(line_image, (725, 400), (725, 650), (255, 0, 0), 8)


        return line_image


img = cv2.imread("road5.jpeg")
#canny = canny(img)
#croppedImage = region_of_interest(canny)
# lines = cv2.HoughLinesP(croppedImage, 2, np.pi / 180, 100, np.array([]), minLineLength=40, maxLineGap=5)
# print(lines)
# line_image = displayLines(img, lines)
# comboImage = cv2.addWeighted(img, 0.3, line_image, 1, 1)
# cv2.imshow("before", croppedImage)
cv2.imshow("after", img)
# cv2.waitKey(0)
### to get size
# plt.imshow(canny)
# plt.show()
# cv2.waitKey(0)


# cap = cv2.VideoCapture("test3.mp4")
#
# while (cap.isOpened()):
#     _, frame = cap.read()
#     gray = canny(frame)
#     croppedImage = region_of_interest(gray)
#     lines = cv2.HoughLinesP(croppedImage, 3, np.pi / 180, 50, np.array([]), minLineLength=40, maxLineGap=20)
#     average_lines = average_slope_intercept(frame,lines)
#     line_image = displayLines(frame, average_lines)
#     comboImage = cv2.addWeighted(frame, 0.5, line_image, 1, 1)
#     cv2.imshow("after", comboImage)
#     cv2.waitKey(1)

