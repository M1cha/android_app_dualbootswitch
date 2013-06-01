LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := DualbootSwitch
LOCAL_SRC_FILES := DualbootSwitch.cpp

include $(BUILD_SHARED_LIBRARY)
