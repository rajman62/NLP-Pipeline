#include "my_JNI.h"
#include <jni.h>
#include <malloc.h>

const char*		DEFAULT_EXCEPTION_CLASS_NAME = "java/lang/RuntimeException";

#define BUFFER_SIZE 80

/******************************************************************************/
void myJniThrowException(JNIEnv* env, const char* class_name, const char* message)
{
	jclass	classe = JNI_GET_CLASS(env, class_name);		
	(*env)->ThrowNew(env, classe, message);
}

/******************************************************************************/
void myJniThrowCantWriteFileException(JNIEnv* env, const char* filename)
{
	char	buffer[BUFFER_SIZE];

	int j	=	sprintf(buffer,		"Can't write file '");
	j		+=	sprintf(buffer + j,	"%s", filename);
	j		+=	sprintf(buffer + j,	"'\n");
		
	myJniThrowException(env, DEFAULT_EXCEPTION_CLASS_NAME, buffer);
}

/******************************************************************************/
void myJniThrowCantReadFileException(JNIEnv* env, const char* filename)
{
	char	buffer[BUFFER_SIZE];

	int j	=	sprintf(buffer,		"Can't read file '");
	j		+=	sprintf(buffer + j,	"%s", filename);
	j		+=	sprintf(buffer + j,	"'\n");

	myJniThrowException(env, DEFAULT_EXCEPTION_CLASS_NAME, buffer);
}

/******************************************************************************/
long* myJniJlongArrayToArrayOfLong(JNIEnv* env, jlongArray input)
{
	const jlong*	ARRAY_OF_JLONG	= (*env)->GetLongArrayElements(env, input, 0);
	const int		ARRAY_SIZE		= (*env)->GetArrayLength(env, input);
	long*			output			= (long*)malloc(sizeof(long)*(ARRAY_SIZE+1));
	int				index;
	
	for(index = 0; index < ARRAY_SIZE; index++)
		output[index] = (long)ARRAY_OF_JLONG[index];
	
	output[ARRAY_SIZE] = 0;
	
	return output;
}

/******************************************************************************/
char* myJniJStringToStringOfChar(JNIEnv* env, jstring input)
{
	// Si la chaîne jstring n'est pas assignée, retourner NULL
	if (input)
	{
		const jsize		LENGTH = (*env)->GetStringLength(env, input);
		const jbyte*	BUFFER = (*env)->GetStringUTFChars(env, input, JNI_FALSE);
		
		char*			output = (char*)malloc(sizeof(char)*(LENGTH+1));
		int				index;
		
		for(index = 0; index < LENGTH; index++)
			output[index] = BUFFER[index];
		
		output[LENGTH] = 0;
		
		(*env)->ReleaseStringUTFChars(env, input, BUFFER);

		return output;
	}
	else
		return NULL;
}