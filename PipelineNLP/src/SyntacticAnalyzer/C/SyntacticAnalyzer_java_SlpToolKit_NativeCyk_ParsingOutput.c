#include "SlpToolKit_NativeCyk_ParsingOutput.h"
#include <parsing_output.h>
#include <malloc.h>
#include "my_JNI.h"

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_ParsingOutput
 * Method:    constructor
 * Signature: ()J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_00024ParsingOutput_constructor
  (JNIEnv* env, jclass clazz)
{
	Parsing_Output* h_parsing_output = (Parsing_Output*)malloc(sizeof(Parsing_Output));

	Init_Parsing_Output_XCString(h_parsing_output, VRAI);
	
    h_parsing_output->regles = (Dyntab_longint *) monmalloc(sizeof(Dyntab_longint));
    Init_Dyntab_longint(h_parsing_output->regles);
	
	return (jlong)h_parsing_output;
}	

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_ParsingOutput
 * Method:    getAnalyse
 * Signature: (J)Ljava/lang/String;
 ******************************************************************************/
JNIEXPORT jstring JNICALL Java_SlpToolKit_NativeCyk_00024ParsingOutput_getAnalyse
  (JNIEnv* env, jclass clazz, jlong native_parsing_output)
{
	Parsing_Output* h_parsing_output = (Parsing_Output*)native_parsing_output;

	if (h_parsing_output->analyse == NULL)
		return NULL;

	else
		return (*env)->NewStringUTF(env, h_parsing_output->analyse->content);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_ParsingOutput
 * Method:    getRulesList
 * Signature: (J)[J
 ******************************************************************************/
JNIEXPORT jlongArray JNICALL Java_SlpToolKit_NativeCyk_00024ParsingOutput_getRulesList
  (JNIEnv* env, jclass clazz, jlong native_parsing_output)
{
	Parsing_Output*		h_parsing_output	= (Parsing_Output*)native_parsing_output;

	if (h_parsing_output->regles == NULL)
		return NULL;

	else
	{
		jlongArray		result = (*env)->NewLongArray(env, h_parsing_output->regles->nb_elements);
		int				index;

		for(index = 0; index < (*env)->GetArrayLength(env, result); index++)
		{
			jlong buffer = h_parsing_output->regles->tab[index];
			(*env)->SetLongArrayRegion(env, result, index, 1, &buffer);
		}

		return result;
	}
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_ParsingOutput
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeCyk_00024ParsingOutput_destructor
  (JNIEnv* env, jclass clazz, jlong native_parsing_output)
{
	Parsing_Output* h_parsing_output = (Parsing_Output*)native_parsing_output;

	Delete_Parsing_Output(h_parsing_output);

	free(h_parsing_output);
}