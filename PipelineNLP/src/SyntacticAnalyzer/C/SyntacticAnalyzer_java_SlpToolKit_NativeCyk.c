#include "SlpToolKit_NativeCyk.h"
#include <cyk.h>
#include <lexique.h>
#include <grammaire.h>
#include <default_charset.h>
#include <malloc.h>
#include "my_JNI.h"

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk
 * Method:    analyseSyntaxique
 * Signature: (Ljava/lang/String;JJ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_analyseSyntaxique
  (JNIEnv* env, jclass clazz, jstring sentence, jlong native_lexicon, jlong native_grammar)
{
	Grammaire*	h_gram	= (Grammaire*)native_grammar;
	Lexique*	h_lex	= (Lexique*)native_lexicon;
	const char*	INPUT	= JNI_GET_STRING(env, sentence);

	Retour_lexicalise* retour = Analyse_Syntaxique(	INPUT, 
													h_lex, 
													h_gram, 
													&Default_GetInput_Function);

	JNI_RELEASE_STRING(env, sentence, INPUT);

	return (jlong)retour;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk
 * Method:    lexicalise
 * Signature: (Ljava/lang/String;JJD)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_lexicalise
  (JNIEnv* env, jclass clazz, jstring sentence, jlong native_lexicon, jlong native_grammar, jdouble cost)
{
	Grammaire*	h_gram	= (Grammaire*)native_grammar;
	Lexique*	h_lex	= (Lexique*)native_lexicon;
	const char*	INPUT	= JNI_GET_STRING(env, sentence);
	
	Retour_lexicalise* retour = Lexicalise(	INPUT, 
											h_lex, 
											h_gram, 
											(Type_cout)cost, 
											Default_GetInput_Function.strict_sep, 
											Default_GetInput_Function.space, 
											Default_GetInput_Function.homogene, 
											NULL);

	JNI_RELEASE_STRING(env, sentence, INPUT);

	return (jlong)retour;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk
 * Method:    decoupeSimple
 * Signature: (Ljava/lang/String;JJ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_decoupeSimple
  (JNIEnv* env, jclass clazz, jstring sentence, jlong native_lexicon, jlong native_grammar)
{
	Grammaire*	h_gram	= (Grammaire*)native_grammar;
	Lexique*	h_lex	= (Lexique*)native_lexicon;
	const char*	INPUT	= JNI_GET_STRING(env, sentence);

	Retour_lexicalise* retour =	Decoupe_Simple(	INPUT, 
												h_lex,
												h_gram,
												(Type_cout)0, // useless
												NULL, // useless
												Default_GetInput_Function.space,
												Default_GetInput_Function.homogene,
												Default_GetInput_Function.collable);

	JNI_RELEASE_STRING(env, sentence, INPUT);
	
	return (jlong)retour;
}


static Type_proba	persistent_proba;

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk
 * Method:    extraitPlusProbable
 * Signature: (JJIIZ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_extraitPlusProbable
  (JNIEnv* env, jclass clazz, jlong native_table_cyk, jlong native_infos_phrase, jint i, jint j, jboolean p_only_p)
{
	Table_CYK*		h_table_cyk			= (Table_CYK*)native_table_cyk;
	Infos_phrase*	h_infos_phrase		= (Infos_phrase*)native_infos_phrase;
	Booleen			only_p				= (p_only_p == JNI_TRUE) ? VRAI : FAUX;
	Parsing_Output* h_parsing_output	= (Parsing_Output*)malloc(sizeof(Parsing_Output));
	
	// Initialiser la structure Parsing_Output
	Init_Parsing_Output_XCString(h_parsing_output, VRAI);

    h_parsing_output->regles = (Dyntab_longint *) monmalloc(sizeof(Dyntab_longint));
    Init_Dyntab_longint(h_parsing_output->regles);

	persistent_proba = Extrait_Plus_Probable(h_table_cyk, h_infos_phrase, (int)i, (int)j, h_parsing_output, only_p);

	return (jlong)h_parsing_output;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk
 * Method:    parcoursCykIteratif
 * Signature: (JJIIJJZ)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeCyk_parcoursCykIteratif
  (JNIEnv* env, jclass clazz, jlong native_table_cyk, jlong native_infos_phrase, jint i, jint j, jlong native_parsing_output, jlong native_liste_l1, jboolean p_only_p)
{
	Table_CYK*		h_table_cyk			= (Table_CYK*)native_table_cyk;
	Infos_phrase*	h_infos_phrase		= (Infos_phrase*)native_infos_phrase;
	Booleen			only_p				= (p_only_p == JNI_TRUE) ? VRAI : FAUX;
	Parsing_Output* h_parsing_output	= (Parsing_Output*)native_parsing_output;
	Liste_L1**		h_h_liste_l1		= (Liste_L1**)native_liste_l1;
	Booleen			result;	

	Libere_Dyntab(h_parsing_output->regles);

	result = Parcours_Cyk_Iteratif(	h_table_cyk, 
									h_infos_phrase, 
									(int)i, 
									(int)j, 
									h_parsing_output,
									h_h_liste_l1,
									only_p,
									&persistent_proba);

	return (result == VRAI) ? JNI_TRUE : JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk
 * Method:    getProbability
 * Signature: ()D
 ******************************************************************************/
JNIEXPORT jdouble JNICALL Java_SlpToolKit_NativeCyk_getProbability
(JNIEnv* env, jclass clazz)
{
	return (jdouble)persistent_proba;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk
 * Method:    razParcoursCykIteratif
 * Signature: (JJ)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeCyk_razParcoursCykIteratif
  (JNIEnv* env, jclass clazz, jlong native_table_cyk, jlong native_liste_l1)
{
	Table_CYK*	h_table_cyk		= (Table_CYK*)native_table_cyk;
	Liste_L1**	h_h_liste_l1	= (Liste_L1**)native_liste_l1;

	RaZ_Parcours_Cyk_Iteratif(h_table_cyk, h_h_liste_l1);
}
