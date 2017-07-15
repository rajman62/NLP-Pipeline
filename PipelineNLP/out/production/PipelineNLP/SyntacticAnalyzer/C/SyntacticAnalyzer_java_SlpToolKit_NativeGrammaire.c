#include "SlpToolKit_NativeGrammaire.h"
#include <lexique.h>
#include <grammaire.h>
#include <malloc.h>
#include "my_JNI.h"

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    ajouteNT
 * Signature: (JLjava/lang/String;J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_ajouteNT
  (JNIEnv* env, jclass clazz, jlong native_grammar, jstring nt, jlong native_lexicon)
{
	Grammaire*		h_gram		= (Grammaire*)native_grammar;
	Lexique*		h_lex		= (Lexique*)native_lexicon;
	char*			nt_string	= (char*)JNI_GET_STRING(env, nt);
	Type_morpho		result;
	
	// Exécution de la fonction native
	Ajoute_NT(nt_string, h_gram, h_lex, VRAI, &result, NULL);
	
	// Libération des ressource
	JNI_RELEASE_STRING(env, nt, nt_string);

	// Retour du résultat
	return (jlong)result;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    ajouteRegle
 * Signature: (JJ[JD)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_ajouteRegle
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong p_left_part, jlongArray p_right_part, jdouble p_proba)
{
	Grammaire*		h_gram		= (Grammaire*)native_grammar;
	Type_morpho		left_part	= (Type_morpho)p_left_part;
	Type_proba		proba		= p_proba;
	Dyntab_longint	right_part;
	Index			retour;
	jsize			array_length = (*env)->GetArrayLength(env, p_right_part);
	int				index;
	jlong			buffer;
	
	// Création du dyntab de la partie droite
	Init_Dyntab_longint(&right_part);

	for(index = 0; index < array_length; index++)
	{
		(*env)->GetLongArrayRegion(env, p_right_part, index, 1, &buffer);
		Augmente_Dyntab_longint((longint)buffer, &right_part);
	}

	// Ajouter la règle dans la grammaire native
	retour = Ajoute_Regle(left_part, &right_part, proba, h_gram);

	// Libérer les ressources
	Libere_Dyntab(&right_part);

	return retour;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    constructor
 * Signature: ()J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_constructor
  (JNIEnv* env, jclass clazz)
{
	Grammaire* h_gram = (Grammaire*)malloc(sizeof(Grammaire));

	Init_Grammaire(h_gram);

	return (jlong)h_gram;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeGrammaire_destructor
  (JNIEnv* env, jclass clazz, jlong native_grammar)
{
	Grammaire* h_gram = (Grammaire*)native_grammar;

	Libere_Grammaire(h_gram);

	free(h_gram);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    writeGrammaire
 * Signature: (JLjava/lang/String;Ljava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeGrammaire_writeGrammaire
  (JNIEnv* env, jclass clazz, jlong native_grammar, jstring p_base_filename, jstring p_lexique_filename)
{
	Grammaire*	h_gram				= (Grammaire*)native_grammar;
	char*		base_filename		= (char*)JNI_GET_STRING(env, p_base_filename);
	char*		lexique_filename;
	int			result;

	// Extraction du nom du fichier lexique s'il existe
	if (p_lexique_filename == NULL)
		lexique_filename = NULL;

	else
		lexique_filename = (char*)JNI_GET_STRING(env, p_lexique_filename);

	// Exécution de la fonction native
	result = Write_Grammaire(h_gram, lexique_filename, base_filename);

	// Libération des ressources
	JNI_RELEASE_STRING(env, p_base_filename, base_filename);
	JNI_RELEASE_STRING(env, p_lexique_filename, lexique_filename);

	// Si par d'erreur, retourner FAUX
	if (result == 0)
		return JNI_FALSE;

	else
		return JNI_TRUE;

}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    readGrammaire
 * Signature: (JLjava/lang/String;Ljava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeGrammaire_readGrammaire
  (JNIEnv* env, jclass clazz, jlong native_grammar, jstring p_base_filename, jstring p_lexique_filename)
{
	Grammaire* h_gram		= (Grammaire*)native_grammar;
	char* base_filename		= (char*)JNI_GET_STRING(env, p_base_filename);
	char* lexique_filename;
	int result;
	
	// Extraction du nom du fichier lexique s'il existe
	if (p_lexique_filename == NULL)
		lexique_filename = NULL;
	
	else
		lexique_filename = (char*)JNI_GET_STRING(env, p_lexique_filename);
	
	// Exécution de la fonction native
	result = Read_Grammaire(h_gram, base_filename, lexique_filename);

	// Libération des ressources
	JNI_RELEASE_STRING(env, p_base_filename, base_filename);
	JNI_RELEASE_STRING(env, p_lexique_filename, lexique_filename);

	if (result == 0)
		return JNI_FALSE;

	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    importeGrammaire
 * Signature: (JJLjava/lang/String;JZ)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeGrammaire_importeGrammaire
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong native_lexicon, jstring p_filename, jlong p_initial, jboolean p_probabilistic)
{
	Grammaire*		h_gram		= (Grammaire*)native_grammar;
	Lexique*		h_lex		= (Lexique*)native_lexicon;
	char*			filename	= (char*)JNI_GET_STRING(env, p_filename);
	Type_morpho		initial		= (Type_morpho)p_initial;
	Booleen			proba		= (p_probabilistic == JNI_TRUE) ? VRAI : FAUX;

	// Exécution de la fonction native
	int result = Importe_Grammaire(h_gram, h_lex, filename, initial, proba);

	// Libération des résultats
	JNI_RELEASE_STRING(env, p_filename, filename);

	if (result == 0)
		return JNI_FALSE;
	
	else
		return JNI_TRUE;	
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    convertNT
 * Signature: (JJJ)Ljava/lang/String;
 ******************************************************************************/
JNIEXPORT jstring JNICALL Java_SlpToolKit_NativeGrammaire_convertNT
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong nt, jlong native_lexicon)
{
	Grammaire*	h_gram	= (Grammaire*)native_grammar;
	Lexique*	h_lex	= (Lexique*)native_lexicon;
	jstring		output;
	XCString	buffer;

	StringInitNull(&buffer);

	// Exécution de la fonction native
	Convert_NT((unsigned long)nt, h_gram, h_lex, &buffer);

	// Conversion du résultat de XCString -> jstring
	output = (*env)->NewStringUTF(env, buffer.content);

	// Libération des ressources
	StringDel(&buffer);

	return output;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    convertNTchar
 * Signature: (JLjava/lang/String;J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_convertNTchar
  (JNIEnv* env, jclass clazz, jlong native_grammar, jstring p_nt, jlong native_lexicon)
{
	Grammaire* h_gram = (Grammaire*)native_grammar;
	Lexique* h_lex = (Lexique*)native_lexicon;
	char* nt = (char*)JNI_GET_STRING(env, p_nt);

	// Exécution de la fonction native
	unsigned long result = Convert_NT_char(nt, h_gram, h_lex);

	// Libération des résultats
	JNI_RELEASE_STRING(env, p_nt, nt);

	// Retour du résultat
	return (jlong)result;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    exporteGrammaire
 * Signature: (JJLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeGrammaire_exporteGrammaire
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong native_lexicon, jstring p_filename)
{
	Grammaire*	h_gram = (Grammaire*)native_grammar;
	Lexique*	h_lex = (Lexique*)native_lexicon;
	char*		filename = (char*)JNI_GET_STRING(env, p_filename);

	// Exécution de la fonction native
	int result = Exporte_Grammaire(h_gram, h_lex, filename);

	// Libération des ressources
	JNI_RELEASE_STRING(env, p_filename, filename);

	if (result == 0)
		return JNI_FALSE;

	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    regleVersNumero
 * Signature: (JJ[J)[J
 ******************************************************************************/
JNIEXPORT jlongArray JNICALL Java_SlpToolKit_NativeGrammaire_regleVersNumero
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong p_left_part, jlongArray p_right_part)
{
	Grammaire*			h_gram = (Grammaire*)native_grammar;
	jlongArray			result;
	Dyntab_longint		native_result;
	int					right_part_length = (*env)->GetArrayLength(env, p_right_part);
	unsigned long*		right_part = malloc((right_part_length + 1) * sizeof(unsigned long int));
	Type_morpho			left_part = (Type_morpho)p_left_part;
	unsigned int		u_index;
	int					s_index;
	jlong				buffer;


	// Conversion de right_part de jlongArray -> unsigned long int[]
	for(s_index = 0; s_index < right_part_length; s_index++)
	{
		(*env)->GetLongArrayRegion(env, p_right_part, s_index, 1, &buffer);
		right_part[s_index] = (unsigned long)buffer;
	}
	right_part[right_part_length] = 0;

	
	// Exécution de la fonction native
	native_result = Regle_vers_Numero(left_part, right_part, h_gram);

	// Conversion du résultat natif de Dyntab_longint -> jlongArray
	result = (*env)->NewLongArray(env, native_result.nb_elements);

	for(u_index = 0; u_index < native_result.nb_elements; u_index++)
	{
		buffer = native_result.tab[u_index];
		(*env)->SetLongArrayRegion(env, result, u_index, 1, &buffer);
	}

	// Libération des ressources
	free(right_part);

	return result;
}


/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    getLeftPart
 * Signature: (JJ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_getLeftPart
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong index)
{
	Grammaire*			h_gram = (Grammaire*)native_grammar;
	Type_morpho			left_part;
	unsigned long int*	right_part;
	Type_proba			proba;

	// Exécution de la fonction native
	Numero_vers_Regle((Index)index, &left_part, &right_part, &proba, h_gram);

	// Libération des ressources
	free(right_part);

	return (jlong)left_part;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    getRightPart
 * Signature: (JJ)[J
 ******************************************************************************/
JNIEXPORT jlongArray JNICALL Java_SlpToolKit_NativeGrammaire_getRightPart
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong p_index)
{
	Grammaire*			h_gram = (Grammaire*)native_grammar;
	Type_morpho			left_part;
	unsigned long int*	right_part;
	Type_proba			proba;
	jlongArray			output;
	int					length;
	int					index;
	jlong				buffer;
	
	// Exécution de la fonction native
	Numero_vers_Regle((Index)p_index, &left_part, &right_part, &proba, h_gram);

	// Extraction de la longueur de la partie droite
	for(length = 0; right_part[length] != 0;length++);

	output = (*env)->NewLongArray(env, length);

	for(index = 0; index < length; index++)
	{
		buffer = (jlong)right_part[index];
		(*env)->SetLongArrayRegion(env, output, index, 1, &buffer);
	}

	
	// Libération des ressources
	free(right_part);
	
	return output;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    getProba
 * Signature: (JJ)D
 ******************************************************************************/
JNIEXPORT jdouble JNICALL Java_SlpToolKit_NativeGrammaire_getProba
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong index)
{
	Grammaire*			h_gram = (Grammaire*)native_grammar;
	Type_morpho			left_part;
	unsigned long int*	right_part;
	Type_proba			proba;
	
	// Exécution de la fonction native
	Numero_vers_Regle((Index)index, &left_part, &right_part, &proba, h_gram);

	// Libération des ressources
	free(right_part);

	return (jdouble)proba;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    getSize
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_getSize
  (JNIEnv* env, jclass clazz, jlong native_grammar)
{
	Grammaire* h_gram = (Grammaire*)native_grammar;

	return (jlong)h_gram->regles->arbre_lexico->nb_entrees;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    getNativeRulesLexicon
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_getNativeRulesLexicon
  (JNIEnv* env, jclass clazz, jlong native_grammar)
{
	Grammaire* h_gram = (Grammaire*)native_grammar;

	return (jlong)h_gram->regles;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    getNativeArbreLex
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeGrammaire_getNativeArbreLex
  (JNIEnv* env, jclass clazz, jlong native_grammar)
{
	Grammaire* h_gram = (Grammaire*)native_grammar;

	return (jlong)h_gram->regles->arbre_lexico;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeGrammaire
 * Method:    setInitialNT
 * Signature: (JJ)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeGrammaire_setInitialNT
  (JNIEnv* env, jclass clazz, jlong native_grammar, jlong initial_nt)
{
	Grammaire* h_gram = (Grammaire*)native_grammar;

	h_gram->valeur_initiale = (Type_morpho)initial_nt;
}
