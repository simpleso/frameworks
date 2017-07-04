
#include "com_andnux_core_fix_DexManager.h"
#include "art_method.h"
#include "dalvik.h"

extern "C" {
JNIEXPORT void JNICALL Java_com_andnux_core_fix_DexManager_fixNative
  (JNIEnv *env, jobject obj,jint  versionCode,jobject wrongmethod, jobject method){
        if (versionCode == -1 || versionCode < 10){
            return;
        } else if (versionCode > 21){
            art::mirror::ArtMethod *wrong = ( art::mirror::ArtMethod *)env->FromReflectedMethod(wrongmethod);
            art::mirror::ArtMethod *rifht = ( art::mirror::ArtMethod *)env->FromReflectedMethod(method);
            wrong->declaring_class_ = rifht->declaring_class_;
            wrong->dex_code_item_offset_ = rifht->dex_code_item_offset_;
            wrong->method_index_ = rifht->method_index_;
            wrong->dex_method_index_ = rifht->dex_method_index_;
            wrong->dex_cache_resolved_methods_ = rifht->dex_cache_resolved_methods_;
            wrong->dex_cache_resolved_types_ = rifht->dex_cache_resolved_types_;
        } else{
            Method *meth = (Method *)env->FromReflectedMethod(wrongmethod);
            Method *taget = (Method *)env->FromReflectedMethod(method);
            meth->accessFlags |=ACC_PUBLIC;
            meth->noRef = taget->noRef;
            meth->clazz = taget->clazz;
            meth->shorty = taget->shorty;
            meth->fastJni = taget->fastJni;
            meth->name = taget->name;
            meth->methodIndex = taget->methodIndex;
            meth->jniArgInfo = taget->jniArgInfo;
            meth->registersSize = taget->registersSize;
            meth->outsSize = taget->outsSize;
            meth->insns = taget->insns;
            meth->prototype = taget->prototype;
            meth->insns = taget->insns;
            meth->nativeFunc = taget->nativeFunc;
        }
    }
}

