# https://youtu.be/eLTLtUVuuy4
import cv2
import numpy as np
import matplotlib.pyplot as plt


def make_coordinates(image,line_parameter,i):
    try:
        slope, intercept = line_parameter
    except TypeError:
        slope, intercept = -0.5, 0.1
    y1 = i-100
    print(i)
    y2 = int(y1 * (0.80))
    x1 = int((y1 - intercept) / slope)
    x2 = int((y2 - intercept) / slope)
    return np.array([x1, y1, x2, y2])


def average_slope_intercept(image, lines):
    left_fit = []
    right_fit = []
    print(lines)
    for line in lines:
        x1, y1, x2, y2 = line.reshape(4)
        parameters = np.polyfit((x1, x2), (y1, y2), 1)
        slope = parameters[0]
        intercept = parameters[1]
        if slope < 0:
            left_fit.append((slope, intercept))
        else:
            right_fit.append((slope, intercept))
    left_fit_after = []
    final_left_line = []
    i=0
    print(left_fit)
    for x in left_fit:
        i+=x[1]
        left_fit_after.append(x)
        if len(left_fit_after) == 3:
            i = int(i/3)
            left_fit_average = np.average(left_fit_after, axis=0)
            left_line = make_coordinates(image,left_fit_average,i)
            i=0
            final_left_line.append([left_line])
            left_fit_after =[]
    a = np.array(final_left_line)
    # left_fit_average = np.average(left_fit, axis=0)
    # left_line = make_coordinates(image,left_fit_average)
    # right_fit_average = np.average(right_fit, axis=0)
    # right_line = make_coordinates(image,right_fit_average)
    print(a)
    return a


def canny(image):
    imgGray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 0)
    imgCanny = cv2.Canny(imgBlur, 50, 150)
    return imgCanny


def region_of_interest(image):
    height = image.shape[0]
    polygons = np.array([[(0, height), (900, height), (60, 280)]])  ##800,0,1500,700
    mask = np.zeros_like(image)
    cv2.fillPoly(mask, polygons, 255)
    maskedImage = cv2.bitwise_and(image, mask)
    return maskedImage


def displayLines(image, lines):
    line_image = np.zeros_like(image)
    if lines is not None:
        for line in lines:
            x1, y1, x2, y2 = line.reshape(4)
            cv2.line(line_image, (x1, y1), (x2, y2), (0, 255, 0), 8)
            # cv2.line(line_image, (725, 400), (725, 650), (255, 0, 0), 8)

        return line_image


img = cv2.imread("test8.jpg")
canny = canny(img)
croppedImage = region_of_interest(canny)
lines = cv2.HoughLinesP(croppedImage, 4, np.pi / 180, 50, np.array([]), minLineLength=10,
                        maxLineGap=10)  # 2.180.100.40.5
average_lines = average_slope_intercept(img, lines)
line_image = displayLines(img, average_lines)
line_image2 = displayLines(img, lines)
comboImage = cv2.addWeighted(img, 0.3, line_image, 1, 1)
comboImage2 = cv2.addWeighted(img, 0.3, line_image2, 1, 1)
cv2.imshow("before", comboImage2)
cv2.imshow("after", comboImage)
cv2.waitKey(0)
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
