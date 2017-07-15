#include "SlpToolKit_NativeCyk_InfosPhrase.h"
#include <cyk.h>
#include <malloc.h>
#include "my_JNI.h"

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_InfosPhrase
 * Method:    constructor
 * Signature: (JJLjava/lang/String;J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_00024InfosPhrase_constructor
  (JNIEnv* env, jclass clazz, jlong native_grammaire, jlong native_lexique, jstring sentence, jlong native_decoupage)
{
	Infos_phrase* h_info_phrase = (Infos_phrase*)malloc(sizeof(Infos_phrase));
	const char* SENTENCE = JNI_GET_STRING(env, sentence);

	h_info_phrase->decoup		= (Type_decoupage*)native_decoupage;
	h_info_phrase->grammaire	= (Grammaire*)native_grammaire;
	h_info_phrase->lexique		= (Lexique*)native_lexique;
	h_info_phrase->phrase		= SENTENCE;
	
	return (jlong)h_info_phrase;	
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_InfosPhrase
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeCyk_00024InfosPhrase_destructor
  (JNIEnv* env, jclass clazz, jlong native_infos_phrase)
{
	Infos_phrase* h_info_phrase = (Infos_phrase*)native_infos_phrase;

	free(h_info_phrase);
}
