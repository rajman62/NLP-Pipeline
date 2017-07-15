#include "SlpToolKit_NativeArbreLex.h"
#include "arbrelex.h"
#include <malloc.h>
#include "my_JNI.h"

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    constructor
 * Signature: ()J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_constructor
  (JNIEnv* env, jclass clazz)
{
	Arbre_lexico* h_arbre_lex = (Arbre_lexico*)malloc(sizeof(Arbre_lexico));
	
	Init_Arbre_Lexico(h_arbre_lex);	

	return (jlong)h_arbre_lex;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeArbreLex_destructor
  (JNIEnv* env, jclass clazz, jlong native_data)
{
	Arbre_lexico* h_arbre_lex = (Arbre_lexico*)native_data;
	
	Libere_Arbre_Lexico(h_arbre_lex);
	free(h_arbre_lex);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    insert
 * Signature: (JJ[J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_insert__JJ_3J
  (JNIEnv* env, jclass clazz, jlong native_data, jlong p_key, jlongArray element)
{
	Arbre_lexico*			h_arbre_lex		= (Arbre_lexico*)native_data;
	long*					array_of_long	= myJniJlongArrayToArrayOfLong(env, element);
	Feuille_arbre_lexico	key				= (Feuille_arbre_lexico)p_key;

	Indice result = Insere_Lexico_Ulong(h_arbre_lex, array_of_long, key);

	free(array_of_long);

	return result;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    insert
 * Signature: (JJLjava/lang/String;)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_insert__JJLjava_lang_String_2
  (JNIEnv* env, jclass clazz, jlong native_data, jlong p_key, jstring element)
{
	Arbre_lexico*			h_arbre_lex		= (Arbre_lexico*)native_data;
	const char*				ARRAY_OF_CHAR	= JNI_GET_STRING(env, element);
	Feuille_arbre_lexico	key				= (Feuille_arbre_lexico)p_key;
	
	Indice result = Insere_Lexico(h_arbre_lex, ARRAY_OF_CHAR, key);
	
	JNI_RELEASE_STRING(env, element, ARRAY_OF_CHAR);
	
	return result;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    getSize
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_getSize
  (JNIEnv* env, jclass clazz, jlong native_data)
{
	return (((Arbre_lexico*)native_data)->nb_entrees);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    saveToFile
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeArbreLex_saveToFile
  (JNIEnv* env, jclass clazz, jlong native_data, jstring p_filename)
{
	const char*	FILENAME = JNI_GET_STRING(env, p_filename);

	int result = Write_Arbre_Lexico((Arbre_lexico*)native_data, FILENAME);

	JNI_RELEASE_STRING(env, p_filename, FILENAME);

	if (result == 0)
		return JNI_FALSE;
	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    loadFromFile
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeArbreLex_loadFromFile
  (JNIEnv* env, jclass clazz, jlong native_data, jstring p_filename)
{
	const char*	FILENAME = JNI_GET_STRING(env, p_filename);

	int result = Read_Arbre_Lexico((Arbre_lexico*)native_data, FILENAME);

	JNI_RELEASE_STRING(env, p_filename, FILENAME);

	if (result == 0)
		return JNI_FALSE;
	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    importContents
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeArbreLex_importContents
  (JNIEnv* env, jclass clazz, jlong native_data, jstring p_filename)
{
	const char*	FILENAME = JNI_GET_STRING(env, p_filename);

	int result = Importe_Arbre_Lexico((Arbre_lexico*)native_data, FILENAME);

	JNI_RELEASE_STRING(env, p_filename, FILENAME);

	if (result == 0)
		return JNI_FALSE;
	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    exportContents
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeArbreLex_exportContents
  (JNIEnv* env, jclass clazz, jlong native_data, jstring p_filename)
{
	const char*	FILENAME = JNI_GET_STRING(env, p_filename);

	int result = Exporte_Arbre_Lexico((Arbre_lexico*)native_data, FILENAME, Convert_Lexico_Pile_Int);

	JNI_RELEASE_STRING(env, p_filename, FILENAME);

	if (result == 0)
		return JNI_FALSE;
	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    listContents
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeArbreLex_listContents
  (JNIEnv* env, jclass clazz, jlong native_data)
{
	Liste_Lexico((Arbre_lexico*)native_data, &printf, VRAI, FAUX, Convert_Lexico_Pile_Int);	
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    endOfWord
 * Signature: (JJ)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeArbreLex_endOfWord
  (JNIEnv* env, jclass clazz, jlong native_data, jlong position)
{
	Arbre_lexico*	h_arbre_lex	= (Arbre_lexico*)native_data;
	Indice			father		= (Indice)position;

	if (position == 0)
		return JNI_FALSE;


	if (Pere_Feuille(father, h_arbre_lex))
		return JNI_TRUE;
	
	else
		return JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    decodeChar
 * Signature: (JJ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_decodeChar
  (JNIEnv* env, jclass clazz, jlong native_data, jlong position)
{
	Arbre_lexico*	h_arbre_lex = (Arbre_lexico*)native_data;

	return Decode_Char(&((*(h_arbre_lex->arbre))[position]));;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    getNextSon
 * Signature: (JJJ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_getNextSon
  (JNIEnv* env, jclass clazz, jlong native_data, jlong position, jlong next_position)
{
	Arbre_lexico*	h_arbre_lex	= (Arbre_lexico*)native_data;
	Indice			father		= (Indice)position;
	Indice			son			= (Indice)next_position;	
	char			character;
	
	if (Cherche_Fils(h_arbre_lex, father, &son, &character))
		return son;

	else
		return 0;
		
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    internalSearch
 * Signature: (JLjava/lang/String;)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_internalSearch__JLjava_lang_String_2
  (JNIEnv* env, jclass clazz, jlong native_data, jstring element)
{
	Arbre_lexico*						h_arbre_lex		= (Arbre_lexico*)native_data;
	Retour_recherche_valeur_lexico*		h_retour_rech	= (Retour_recherche_valeur_lexico*)malloc(sizeof(Retour_recherche_valeur_lexico));
	const char*							GRAPHY			= JNI_GET_STRING(env, element);

	*h_retour_rech = GetFirst_Valeur_Lexico(h_arbre_lex, GRAPHY);

	JNI_RELEASE_STRING(env, element, GRAPHY);

	return (jlong)h_retour_rech;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    internalSearch
 * Signature: (J[J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeArbreLex_internalSearch__J_3J
  (JNIEnv* env, jclass clazz, jlong native_data, jlongArray element)
{
	Arbre_lexico*						h_arbre_lex		= (Arbre_lexico*)native_data;
	Retour_recherche_valeur_lexico*		h_retour_rech	= (Retour_recherche_valeur_lexico*)malloc(sizeof(Retour_recherche_valeur_lexico));
	unsigned long*						sequence		= myJniJlongArrayToArrayOfLong(env, element);
	
	*h_retour_rech = GetFirst_Valeur_Lexico_Ulong(h_arbre_lex, sequence);

	free(sequence);
	
	return (jlong)h_retour_rech;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    accessString
 * Signature: (JJ)Ljava/lang/String;
 ******************************************************************************/
JNIEXPORT jstring JNICALL Java_SlpToolKit_NativeArbreLex_accessString
  (JNIEnv* env, jclass clazz, jlong native_data, jlong inversion_code)
{
	Arbre_lexico*	h_arbre_lex = (Arbre_lexico*)native_data;	
	XCString		string;
	jstring			output;

	StringInitNull(&string);

	Acces_Code_Inversion_BdD(h_arbre_lex, (Indice)inversion_code, &string);

	output = (*env)->NewStringUTF(env, string.content);

	StringDel(&string);

	return output;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeArbreLex
 * Method:    accessSequence
 * Signature: (JJ)[J
 ******************************************************************************/
JNIEXPORT jlongArray JNICALL Java_SlpToolKit_NativeArbreLex_accessSequence
  (JNIEnv* env, jclass clazz, jlong native_data, jlong inversion_code)
{
	Arbre_lexico*	h_arbre_lex = (Arbre_lexico*)native_data;
	Dyntab_longint	dyntab;
	jlongArray		output;
	int				index;
	jlong			buffer;


	Init_Dyntab_longint(&dyntab);

	Acces_Code_Inversion_BdD_Ulong(h_arbre_lex, (Indice)inversion_code, &dyntab);

	output = (*env)->NewLongArray(env, dyntab.nb_elements - 1);

	for(index = 0; index < (*env)->GetArrayLength(env, output); index++)
	{
		buffer = dyntab.tab[index];
		(*env)->SetLongArrayRegion(env, output, index, 1, &buffer);
	}

	return output;
}

