#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

extern "C" jint Java_com_xiaomi_dualbootswitch_MainActivity_setBootmode(JNIEnv * env, jobject thiz, jstring jBootmode) {
	jboolean isCopy;
	const char * bootmode = env->GetStringUTFChars(jBootmode, &isCopy);

	// open misc-partition
	FILE* misc = fopen("/dev/block/platform/msm_sdcc.1/by-name/misc", "wb");
	if (misc == NULL) {
		printf("Error opening misc partition.\n");
		return -1;
	}

	// write bootmode
	fseek(misc, 0x1000, SEEK_SET);
	if (fputs(bootmode, misc) < 0) {
		printf("Error writing bootmode to misc partition.\n");
		return -1;
	}

	// close
	fclose(misc);
	return 0;
}

extern "C" jstring Java_com_xiaomi_dualbootswitch_MainActivity_getBootmode(
		JNIEnv * env, jobject _this) {

	char bootmode[20];
	char *szResult;

	// open misc-partition
	FILE* misc = fopen("/dev/block/platform/msm_sdcc.1/by-name/misc", "rb");
	if (misc == NULL) {
		printf("Error opening misc partition.\n");
		return env->NewStringUTF(NULL);
	}

	// write bootmode
	fseek(misc, 0x1000, SEEK_SET);
	if (fgets(bootmode, 13, misc) == NULL) {
		printf("Error reading bootmode from misc partition.\n");
		return env->NewStringUTF(NULL);
	}

	// close
	fclose(misc);
	return env->NewStringUTF(bootmode);
}

