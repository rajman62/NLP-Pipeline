#include <jni.h>

#define JNI_GET_STRING(env, string)						((*env)->GetStringUTFChars(env, string, 0))
#define JNI_RELEASE_STRING(env, param, string)			((*env)->ReleaseStringUTFChars(env, param, string))

#define JNI_SET_INT(env, obj, c_param, java_param)		((*env)->SetIntField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "I"),(jint) c_param))
#define JNI_GET_INT(env, obj, java_param)				((*env)->GetIntField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "I")))

#define JNI_GET_BOOLEAN(env, obj, java_param)			((*env)->GetBooleanField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "Z")))
#define JNI_SET_BOOLEAN(env, obj, c_param, java_param)	((*env)->SetBooleanField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "Z"),(jboolean) c_param))

#define JNI_GET_LONG(env, obj, java_param)				((*env)->GetLongField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "J")))
#define JNI_SET_LONG(env, obj, c_param, java_param)		((*env)->SetLongField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "J"),(jlong) c_param))

#define JNI_GET_DOUBLE(env, obj, java_param)			((*env)->GetDoubleField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "D")))
#define JNI_SET_DOUBLE(env, obj, c_param, java_param)	((*env)->SetDoubleField(env, obj,(*env)->GetFieldID(env,(*env)->GetObjectClass(env, obj),java_param, "D"),(jdouble) c_param))

#define JNI_NEW_OBJECT(env, obj_class)					((*env)->NewObject(env, obj_class, (*env)->GetMethodID(env, obj_class, "<init>", "()V")))

#define JNI_GET_CLASS(env, class_name)					((*env)->FindClass(env, class_name))
#define JNI_GET_STRING_CLASS(env)						(JNI_GET_CLASS(env, "java/lang/String"))
#define JNI_GET_OBJECT_CLASS(env)						(JNI_GET_CLASS(env, "java/lang/Object"))


void myJniThrowCantWriteFileException(JNIEnv* env, const char* filename);
void myJniThrowCantReadFileException(JNIEnv* env, const char* filename);

long* myJniJlongArrayToArrayOfLong(JNIEnv* env, jlongArray input);
char* myJniJStringToStringOfChar(JNIEnv* env, jstring input);

