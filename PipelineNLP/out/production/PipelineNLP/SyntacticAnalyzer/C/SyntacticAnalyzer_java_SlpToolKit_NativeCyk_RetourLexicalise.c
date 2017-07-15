#include "SlpToolKit_NativeCyk_RetourLexicalise.h"
#include <cyk.h>
#include <malloc.h>

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_RetourLexicalise
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeCyk_00024RetourLexicalise_destructor
  (JNIEnv* env, jclass clazz, jlong native_retour_lexicalise)
{
	Retour_lexicalise* h_retour_lexicalise = (Retour_lexicalise*)native_retour_lexicalise;

	Libere_Resultat_lexicalise(&h_retour_lexicalise);

	free(h_retour_lexicalise);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_RetourLexicalise
 * Method:    getTableCYK
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_00024RetourLexicalise_getTableCYK
  (JNIEnv* env, jclass clazz, jlong native_retour_lexicalise)
{
	Retour_lexicalise* h_retour_lexicalise = (Retour_lexicalise*)native_retour_lexicalise;

	return (jlong)h_retour_lexicalise->table;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_RetourLexicalise
 * Method:    getDecoupage
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_00024RetourLexicalise_getDecoupage
  (JNIEnv* env, jclass clazz, jlong native_retour_lexicalise)
{
	Retour_lexicalise* h_retour_lexicalise = (Retour_lexicalise*)native_retour_lexicalise;
	
	return (jlong)h_retour_lexicalise->decoupage;	
}