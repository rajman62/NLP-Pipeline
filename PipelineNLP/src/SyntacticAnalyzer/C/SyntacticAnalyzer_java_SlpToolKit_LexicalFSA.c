#include "SlpToolKit_LexicalFSA.h"
#include "automatelex.h"
#include "automategen.h"
#include "my_JNI.h"
#include <malloc.h>

const char* FSA_AUTOMATE_LEX_MEMBER				= "automate_lex";
const char* FSA_LEX_OF_LONG_MEMBER				= "lex_of_long";


#define GET_LEXICAL_FSA(env,obj)	((AutomateLex*)JNI_GET_LONG(env, obj, FSA_AUTOMATE_LEX_MEMBER))

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    constructor
 * Signature: (Z)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_constructor
  (JNIEnv* env , jobject obj, jboolean lex_of_long)
{
	// Allocation dynamique de la mémoire accueillant la structure AutomateLex
	AutomateLex* h_lex_fsa = (AutomateLex*)malloc(sizeof(AutomateLex));

	// Assigne le membre de classe Java
	JNI_SET_LONG(env, obj, h_lex_fsa, FSA_AUTOMATE_LEX_MEMBER);

	// Initialise la structure de donnée AutomateLex
	if (lex_of_long)
		InitAutomateLexUlong(h_lex_fsa);
	else
		InitAutomateLexChar(h_lex_fsa);
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    finalize
 * Signature: ()V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_finalize
  (JNIEnv* env, jobject obj) 
{
	AutomateLex* h_lex_fsa = GET_LEXICAL_FSA(env, obj);

	LibereAutomateLex(h_lex_fsa);
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    insert
 * Signature: (Ljava/lang/String;)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_LexicalFSA_insert__Ljava_lang_String_2
  (JNIEnv* env, jobject obj, jstring entry)
{
	// NON IMPLEMENTÉ
	return 0;
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    insert
 * Signature: ([J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_LexicalFSA_insert___3J
  (JNIEnv* env, jobject obj, jlongArray entry)
{
	// NON IMPLEMENTÉ
	return 0;
}


/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    search
 * Signature: (Ljava/lang/String;)LSlpToolKit/SearchResult;
 ******************************************************************************/
JNIEXPORT jobject JNICALL Java_SlpToolKit_LexicalFSA_search__Ljava_lang_String_2
  (JNIEnv* env, jobject obj, jstring graphy)
{
	// NON IMPLEMENTÉ
	return NULL;
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    search
 * Signature: ([J)LSlpToolKit/SearchResult;
 ******************************************************************************/
JNIEXPORT jobject JNICALL Java_SlpToolKit_LexicalFSA_search___3J
  (JNIEnv* env, jobject obj, jlongArray graphy)
{
	// NON IMPLEMENTÉ
	return NULL;
}


/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    getSize
 * Signature: ()J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_LexicalFSA_getSize
  (JNIEnv *env, jobject obj)
{
	// NON IMPLEMENTÉ
	return 0;
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    saveToFile
 * Signature: (Ljava/lang/String;)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_saveToFile
  (JNIEnv* env, jobject obj, jstring filename) 
{
	AutomateLex* h_lex_fsa = GET_LEXICAL_FSA(env, obj);

	const char* string = JNI_GET_STRING(env, filename);

	if (WriteAutomateLex(h_lex_fsa, string))
		myJniThrowCantWriteFileException(env, string);
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    loadFromFile
 * Signature: (Ljava/lang/String;)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_loadFromFile
  (JNIEnv* env, jobject obj, jstring filename) 
{
	AutomateLex* h_lex_fsa = GET_LEXICAL_FSA(env, obj);

	const char* string = JNI_GET_STRING(env, filename);

	if (ReadAutomateLex(h_lex_fsa, string))
		myJniThrowCantReadFileException(env, string);

}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    importContents
 * Signature: (Ljava/lang/String;)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_importContents
  (JNIEnv* env, jobject obj, jstring filename) 
{
	AutomateLex* h_lex_fsa = GET_LEXICAL_FSA(env, obj);

	const char* string = JNI_GET_STRING(env, filename);

	if (ImporteAutomateLex(h_lex_fsa, string))
		myJniThrowCantReadFileException(env, string);
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    exportContents
 * Signature: (Ljava/lang/String;)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_exportContents
  (JNIEnv* env, jobject obj, jstring filename) 
{
	AutomateLex* h_lex_fsa = GET_LEXICAL_FSA(env, obj);

	const char* string = JNI_GET_STRING(env, filename);

	if (ExporteAutomateLex(h_lex_fsa, string))
		myJniThrowCantWriteFileException(env, string);
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    listContents
 * Signature: ()V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_listContents
  (JNIEnv* env, jobject obj) 
{
	AutomateLex* h_lex_fsa = GET_LEXICAL_FSA(env, obj);

	ListAutomateLex(h_lex_fsa, printf);
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    goToRoot
 * Signature: ()V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_goToRoot
  (JNIEnv* env, jobject obj) 
{
	// NON IMPLEMENTÉ	
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    goToChar
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_LexicalFSA_goToChar
  (JNIEnv* env, jobject obj) 
{
	// NON IMPLEMENTÉ
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    getNextChar
 * Signature: ()J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_LexicalFSA_getNextChar
  (JNIEnv* env, jobject obj) 
{
	// NON IMPLEMENTÉ
	return 0;
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    endOfWord
 * Signature: ()Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_LexicalFSA_endOfWord
  (JNIEnv* env, jobject obj) 
{
	// NON IMPLEMENTÉ
	return 0;
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    accessString
 * Signature: (J)[Ljava/lang/String;
 ******************************************************************************/
JNIEXPORT jobjectArray JNICALL Java_SlpToolKit_LexicalFSA_accessString
  (JNIEnv* env, jobject obj, jlong key) 
{
	// NON IMPLEMENTÉ
	return NULL;
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    access
 * Signature: (J)[[J
 ******************************************************************************/
JNIEXPORT jobjectArray JNICALL Java_SlpToolKit_LexicalFSA_access
  (JNIEnv* env, jobject obj, jlong key) 
{
	// NON IMPLEMENTÉ
	return NULL;
}

/*******************************************************************************
 * Class:     SlpToolKit_LexicalFSA
 * Method:    getAlphabetCardinality
 * Signature: ()I
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_SlpToolKit_LexicalFSA_getAlphabetCardinality
  (JNIEnv* env, jobject obj)
{
	if (JNI_GET_BOOLEAN(env, obj, FSA_LEX_OF_LONG_MEMBER))
		return (jint)32;

	else
		return (jint)8;
}

