import cv2
import numpy as np


def canny(image):
    imgGray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    imgBlur = cv2.GaussianBlur(imgGray, (5, 5), 0)
    imgCanny = cv2.Canny(imgBlur, 50, 150)
    return imgCanny


def region_of_interest(image):
    height = image.shape[0]
    polygons = np.array([[(50, height), (555, height), (275, 75)]])
    mask = np.zeros_like(image)
    cv2.fillPoly(mask, polygons, 255)
    maskedImage = cv2.bitwise_and(image, mask)
    return maskedImage


def displayLines(image, lines):
    lineImage = np.zeros_like(image)
    if lines is not None:
        for line in lines:
            x1, y1, x2, y2 = line.reshape(4)
            cv2.line(lineImage, (x1, y1), (x2, y2), (255, 0, 0), 10)
            print(lines)
            return lineImage


img = cv2.imread("road1.jpg")
kernel = np.ones((5, 5), np.uint8)
canny = canny(img)
croppedImage = region_of_interest(canny)
lines = cv2.HoughLinesP(croppedImage, 3, np.pi / 180, 100, np.array([]), minLineLength=40, maxLineGap=2)
line_image = displayLines(img, lines)
comboImage = cv2.addWeighted(img,0.8,line_image,1,1)
cv2.imshow("before", croppedImage)
cv2.imshow("after", comboImage)
cv2.waitKey(0)
