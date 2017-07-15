#include "SlpToolKit_Lexematizor.h"
#include "my_JNI.h"
#include <jni.h>
#include <ctype.h>
#include "default_charset.h"
#include "dyntab.h"
#include "XCString.h"
#include "slplibversion.h"
#include "maptable.h"
#include "lexique.h"
#include "lexemme.h"

const char* LEXEMATIZOR_CLASS_NAME = "SlpToolKit/Lexematizor";

/*******************************************************************************
 * Class:     SlpToolKit_Lexematizor
 * Method:    internalLexematize
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 ******************************************************************************/
JNIEXPORT jobjectArray JNICALL Java_SlpToolKit_Lexematizor_internalLexematize
  (JNIEnv* env, jobject obj, jstring input)
{
	Arbre_lexico*		h_arbre_lex			= (Arbre_lexico*)JNI_GET_LONG(env, obj, "arbre_lex");
	const char*			TEMP_BUFFER			= JNI_GET_STRING(env, input);
	const jclass		LEXEMATIZOR_CLASS	= JNI_GET_CLASS(env, LEXEMATIZOR_CLASS_NAME);
	jfieldID			head_fieldID		= (*env)->GetFieldID(env, LEXEMATIZOR_CLASS, "head", "Ljava/lang/String;");
	jobject				head_object			= (*env)->GetObjectField(env, obj, head_fieldID);
	const char*			HEAD				= JNI_GET_STRING(env, head_object);
	unsigned int		index;
	jobjectArray		result;
	Dyntab_pointeur		words_dyntab;
	XCString			sentence;

	// Pour l'instant, cette méthode n'est applicable que dans le cas d'un arbre lexical
	if (h_arbre_lex == NULL)
		return NULL;
	
	
	Init_Dyntab_pointeur(&words_dyntab);
	
	// Extraire la phrase en paramètre dans la XCString sentence
	StringInitNull(&sentence);
	StringCatFront(&sentence, TEMP_BUFFER);
		
	/***************************************************************************
	* Lexematisation														   *
	***************************************************************************/
	Lexematise(&sentence, h_arbre_lex, &words_dyntab, isspace, Default_Homogene, Default_Collable, HEAD);

	// Libération des allocations dynamiques
	StringDel(&sentence);
	JNI_RELEASE_STRING(env, head_object, HEAD);
	JNI_RELEASE_STRING(env, input, TEMP_BUFFER);
	

	// Création d'un tableau de String où y déposer le résultat de la lexemmatisation
	result = (*env)->NewObjectArray(env, words_dyntab.nb_elements, JNI_GET_STRING_CLASS(env), NULL);

	for(index = 0; index < words_dyntab.nb_elements; index++)
	{
		jobject new_string = (*env)->NewStringUTF(env, words_dyntab.tab[index]);
		(*env)->SetObjectArrayElement(env, result, index, new_string);
	}

	// Retour du résultat
	return result;
}
