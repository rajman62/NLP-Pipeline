#include "SlpToolKit_NativeRetourRechercheLex.h"
#include "lexique.h"
#include <malloc.h>

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheLex
 * Method:    getProba
 * Signature: (J)D
 ******************************************************************************/
JNIEXPORT jdouble JNICALL Java_SlpToolKit_NativeRetourRechercheLex_getProba
  (JNIEnv* env, jclass clazz, jlong retour_recherche_lex)
{
	Retour_recherche_lex* h_retour = (Retour_recherche_lex*)retour_recherche_lex;

	return h_retour->acces.proba;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheLex
 * Method:    getFreq
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeRetourRechercheLex_getFreq
  (JNIEnv* env, jclass clazz, jlong retour_recherche_lex)
{
	Retour_recherche_lex* h_retour = (Retour_recherche_lex*)retour_recherche_lex;
	
	return h_retour->acces.freq;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheLex
 * Method:    getLemme
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeRetourRechercheLex_getLemme
  (JNIEnv* env, jclass clazz, jlong retour_recherche_lex)
{
	Retour_recherche_lex* h_retour = (Retour_recherche_lex*)retour_recherche_lex;
	
	return h_retour->acces.lemme;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheLex
 * Method:    getMorpho
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeRetourRechercheLex_getMorpho
  (JNIEnv* env, jclass clazz, jlong retour_recherche_lex)
{
	Retour_recherche_lex* h_retour = (Retour_recherche_lex*)retour_recherche_lex;
	
	return h_retour->acces.morpho;	
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheLex
 * Method:    found
 * Signature: (J)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeRetourRechercheLex_found
  (JNIEnv* env, jclass clazz, jlong retour_recherche_lex)
{
	Retour_recherche_lex* h_retour = (Retour_recherche_lex*)retour_recherche_lex;
	
	return h_retour->recherche.trouve;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheLex
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeRetourRechercheLex_destructor
  (JNIEnv* env, jclass clazz, jlong retour_recherche_lex)
{
	Retour_recherche_lex* h_retour = (Retour_recherche_lex*)retour_recherche_lex;

	free(h_retour);
}

