#include <jni.h>

extern int bspatch_main(char *oldfile,char *newfile,char *patchfile);

JNIEXPORT jint JNICALL
Java_com_andnux_core_other_BspatchNative_bspatch(JNIEnv *env, jobject instance, jstring oldfile_,
                                                 jstring newfile_, jstring patchfile_) {
    const char *oldfile = (*env)->GetStringUTFChars(env,oldfile_,JNI_FALSE);
    const char *newfile = (*env)->GetStringUTFChars(env,newfile_,JNI_FALSE);
    const char *patchfile = (*env)->GetStringUTFChars(env,patchfile_,JNI_FALSE);
    int  ret = -1;
    ret = bspatch_main(oldfile,newfile,patchfile);
    (*env)->ReleaseStringUTFChars(env, oldfile_, oldfile);
    (*env)->ReleaseStringUTFChars(env, newfile_, newfile);
    (*env)->ReleaseStringUTFChars(env, patchfile_, patchfile);
    return  ret;
}
