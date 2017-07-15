#include "SlpToolKit_NativeRetourRechercheValeurLexico.h"
#include "arbrelex.h"
#include <malloc.h>

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheValeurLexico
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeRetourRechercheValeurLexico_destructor
  (JNIEnv* env, jclass clazz, jlong native_data)
{
	Retour_recherche_valeur_lexico* h_retour = (Retour_recherche_valeur_lexico*)native_data;

	free(h_retour);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheValeurLexico
 * Method:    found
 * Signature: (J)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeRetourRechercheValeurLexico_found
  (JNIEnv* env, jclass clazz, jlong native_data)
{
	Retour_recherche_valeur_lexico* h_retour = (Retour_recherche_valeur_lexico*)native_data;

	return h_retour->recherche.trouve;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheValeurLexico
 * Method:    getLastMatchChar
 * Signature: (J)I
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_SlpToolKit_NativeRetourRechercheValeurLexico_getLastMatchChar
  (JNIEnv* env, jclass clazz, jlong native_data)
{
	Retour_recherche_valeur_lexico* h_retour = (Retour_recherche_valeur_lexico*)native_data;
	
	return h_retour->recherche.indice_chaine;	
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheValeurLexico
 * Method:    getKey
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeRetourRechercheValeurLexico_getKey
  (JNIEnv* env, jclass clazz, jlong native_data)
{
	Retour_recherche_valeur_lexico* h_retour = (Retour_recherche_valeur_lexico*)native_data;
	
	return h_retour->valeur;	
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeRetourRechercheValeurLexico
 * Method:    next
 * Signature: (JJ)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeRetourRechercheValeurLexico_next
  (JNIEnv* env, jclass clazz, jlong native_data, jlong arbre_lex)
{
	Retour_recherche_valeur_lexico* h_retour = (Retour_recherche_valeur_lexico*)native_data;
	Arbre_lexico* h_arbre_lex = (Arbre_lexico*)arbre_lex;	

	GetNext_Valeur_Lexico(h_arbre_lex, h_retour);	
}
