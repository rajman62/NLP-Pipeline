#include "SlpToolKit_NativeLexique.h"
#include "lexique.h"
#include <malloc.h>
#include "my_JNI.h"

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    lookForNext
 * Signature: (JJ)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_lookForNext
  (JNIEnv* env, jclass clazz, jlong lexicon, jlong retour)
{
	Suivant_Lexique((Lexique*)lexicon, (Retour_recherche_lex*)retour);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    getSize
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_getSize
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	return SizeLex((Lexique*)lexicon);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    exportToASCII
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_exportToASCII
  (JNIEnv* env, jclass clazz, jlong lexicon, jstring filename)
{
	const char* STRING = JNI_GET_STRING(env, filename);	
	
	int result = Exporte_Lexique((Lexique*)lexicon, STRING);
	
	JNI_RELEASE_STRING(env, filename, STRING);

	if (result == 0)
		return JNI_FALSE;

	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    importFromASCII
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_importFromASCII
  (JNIEnv* env, jclass clazz, jlong lexicon, jstring filename)
{
	const char* STRING = JNI_GET_STRING(env, filename);
	
	int result = Importe_Lexique((Lexique*)lexicon, STRING);
	
	JNI_RELEASE_STRING(env, filename, STRING);

	if (result == 0)
		return JNI_FALSE;
	
	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    loadFromFile
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_loadFromFile
  (JNIEnv* env, jclass clazz, jlong lexicon, jstring filename)
{
	const char* STRING = JNI_GET_STRING(env, filename);
	XCString	xc_filename;	
	int			result;

	StringInitNull(&xc_filename);
	StringCpy(&xc_filename, STRING);
	StringCat(&xc_filename, lexique_FILE_EXT);
	
	result = Read_Lexique((Lexique*)lexicon, xc_filename.content);
	
	// Lib�ration des ressources
	JNI_RELEASE_STRING(env, filename, STRING);
	StringDel(&xc_filename);

	// Retour du r�sultat
	if (result == 0)
		return JNI_FALSE;
	
	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    saveFromFile
 * Signature: (JLjava/lang/String;)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_saveFromFile
  (JNIEnv* env, jclass clazz, jlong lexicon, jstring filename)
{
	const char* STRING = JNI_GET_STRING(env, filename);
	
	int result = Write_Lexique((Lexique*)lexicon, STRING);
	
	JNI_RELEASE_STRING(env, filename, STRING);

	if (result == 0)
		return JNI_FALSE;
	
	else
		return JNI_TRUE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    normalize
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_normalize
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	Log_Proba((Lexique*)lexicon);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    constructor
 * Signature: ()J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_constructor
  (JNIEnv* env, jclass clazz)
{
	Lexique* h_lexicon = (Lexique*)malloc(sizeof(Lexique));

	h_lexicon->arbre_lexico = (Arbre_lexico *) 0;
	Init_Lexique(h_lexicon);

	return (jlong)h_lexicon;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_destructor
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	Lexique* h_lexicon = (Lexique*)lexicon;
	
	// Lib�re la m�moire occup�e par le lexique natif
	Libere_Lexique(h_lexicon);
	free(h_lexicon);
}


/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    insert
 * Signature: (JLjava/lang/String;[JZDZJZJZJ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_insert
  (JNIEnv* env, jclass clazz, jlong lexicon, jstring p_graphy, jlongArray p_sequence, jboolean p_proba_def, jdouble p_proba, jboolean p_freq_def, jlong p_freq, jboolean p_lemme_def, jlong p_lemme, jboolean p_pos_def, jlong p_pos)
{
	Lexique*				h_lexicon		= (Lexique*)lexicon;
	const jlong				ENTRY_INDEX		= SizeLex((Lexique*)lexicon);
	Feuille_arbre_lexico	lex_size		= (Feuille_arbre_lexico)ENTRY_INDEX;
	Type_freq*				h_frequency		= NULL;
	Type_proba*				h_probability	= NULL;
	Type_morpho*			h_morpho		= NULL;
	Type_lemme*				h_lemme			= NULL;
	void*					h_c_graphy		= NULL;
	Type_freq				frequency;
	Type_proba				probability;
	Type_lemme				lemme;
	Booleen					insertion_problem;
	Type_morpho				morpho;

	// Extraction de la graphie - obligatoire
	if (p_sequence)
		h_c_graphy = (void*)myJniJlongArrayToArrayOfLong(env, p_sequence);
	
	else if (p_graphy)
		h_c_graphy = (void*)myJniJStringToStringOfChar(env, p_graphy);
	

	// Extraction de la fr�quence - optionnelle
	if (p_freq_def)
	{
		frequency = (Type_freq)p_freq;
		h_frequency = &frequency;

	}

	// Extraction de la probabilit� - optionnelle
	if (p_proba_def)
	{
		probability = (Type_proba)p_proba;
		h_probability = &probability;
	}

	// Extraction du lemme - optionnelle
	if (p_lemme_def)
	{
		lemme = (Type_lemme)p_lemme;
		h_lemme = &lemme;
	}
	
	// Extraction de la cat�gorie morpho-syntaxique - optionnelle	
	if (p_pos_def)
	{
		morpho		= (Type_morpho)p_pos;
		h_morpho	= &morpho;
	}

	// Insertion de la nouvelle entr�e dans le lexique
	if (p_graphy)
		insertion_problem = Insere_Lexique(h_lexicon, h_c_graphy, h_lemme, h_morpho, h_frequency, h_probability, &lex_size, FAUX);

	else
		insertion_problem = Insere_Lexique_Ulong(h_lexicon, h_c_graphy, h_lemme, h_morpho, h_frequency, h_probability, &lex_size, FAUX);

	// Lever une exception en cas de probl�me lors de l'insertion
	if (insertion_problem)
		return -1;

	// Lib�ration des allocations dynamiques de m�moire
	free(h_c_graphy);
		
	return ENTRY_INDEX;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    getArbreLexico
 * Signature: (J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_getArbreLexico
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	Lexique* h_lexicon = (Lexique*)lexicon;

	return (jlong)h_lexicon->arbre_lexico;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    probabilityDefined
 * Signature: (J)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_probabilityDefined
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	Lexique* h_lexicon = (Lexique*)lexicon;

	if (h_lexicon->proba)
		return JNI_TRUE;

	else
		return JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    frequencyDefined
 * Signature: (J)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_frequencyDefined
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	Lexique* h_lexicon = (Lexique*)lexicon;
	
	if (h_lexicon->freq)
		return JNI_TRUE;
	
	else
		return JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    lemmeDefined
 * Signature: (J)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_lemmeDefined
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	Lexique* h_lexicon = (Lexique*)lexicon;
	
	if (h_lexicon->lemme)
		return JNI_TRUE;
	
	else
		return JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    morphoDefined
 * Signature: (J)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_morphoDefined
  (JNIEnv* env, jclass clazz, jlong lexicon)
{
	Lexique* h_lexicon = (Lexique*)lexicon;
	
	if (h_lexicon->morpho)
		return JNI_TRUE;
	
	else
		return JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    internalGetFromIndex
 * Signature: (JJ)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_internalGetFromIndex
  (JNIEnv* env, jclass clazz, jlong lexicon, jlong index)
{
	Lexique*				h_lexicon		= (Lexique*)lexicon;
	Retour_recherche_lex*	h_retour_rech	= (Retour_recherche_lex*)malloc(sizeof(Retour_recherche_lex));
	Retour_accede_lex		retour_accede	= Accede_Lexique(h_lexicon, (Index)index, FAUX);

	h_retour_rech->acces = retour_accede;

	return (jlong)h_retour_rech;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    internalLookFor
 * Signature: (JLjava/lang/String;)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_internalLookFor__JLjava_lang_String_2
  (JNIEnv* env, jclass clazz, jlong lexicon, jstring graphy)
{
	Lexique*				h_lexicon		= (Lexique*)lexicon;
	Retour_recherche_lex*	h_ret_rech_lex	= (Retour_recherche_lex*)malloc(sizeof(Retour_recherche_lex));
	const char*				GRAPHY			= JNI_GET_STRING(env, graphy);
	
	// Ex�cuter la recherche lexique native
	*h_ret_rech_lex = Recherche_Lexique(h_lexicon, GRAPHY);

	JNI_RELEASE_STRING(env, graphy, GRAPHY);
	
	return (jlong)h_ret_rech_lex;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    internalLookFor
 * Signature: (J[J)J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_internalLookFor__J_3J
  (JNIEnv* env, jclass clazz, jlong lexicon, jlongArray p_sequence)
{
	Lexique*				h_lexicon		= (Lexique*)lexicon;
	Retour_recherche_lex*	h_ret_rech_lex	= (Retour_recherche_lex*)malloc(sizeof(Retour_recherche_lex));
	long*					sequence		= myJniJlongArrayToArrayOfLong(env, p_sequence);
	
	// Ex�cuter la recherche lexique native
	*h_ret_rech_lex = Recherche_Lexique_Ulong(h_lexicon, sequence);

	free(sequence);
		
	return (jlong)h_ret_rech_lex;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    extractGraphy
 * Signature: (JJ)Ljava/lang/String;
 ******************************************************************************/
JNIEXPORT jstring JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_extractGraphy
  (JNIEnv* env, jclass clazz, jlong lexicon, jlong index)
{
	Lexique*	h_lexicon = (Lexique*)lexicon;
	XCString	graphy;
	jstring		output;

	StringInitNull(&graphy);
	
	Get_Graphie(h_lexicon, index, &graphy);

	output = (*env)->NewStringUTF(env, graphy.content);

	StringDel(&graphy);

	return output;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    extractSequence
 * Signature: (JJ)[J
 ******************************************************************************/
JNIEXPORT jlongArray JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_extractSequence
  (JNIEnv* env, jclass clazz, jlong lexicon, jlong index)
{
	Lexique*		h_lexicon = (Lexique*)lexicon;
	Dyntab_longint	sequence;
	jlongArray		output;
	long			i;

	Init_Dyntab_longint(&sequence);

	Acces_Code_Inversion_BdD_Ulong(h_lexicon->arbre_lexico, h_lexicon->graphie[index], &sequence);

	if (sequence.nb_elements > 0)
		output = (*env)->NewLongArray(env, sequence.nb_elements - 1);
	else
		output = (*env)->NewLongArray(env, 0);

	for(i = 0; i < (*env)->GetArrayLength(env, output); i++)
	{
		jlong element = sequence.tab[i];
		(*env)->SetLongArrayRegion(env, output, i, 1, &element);		
	}
	
	Libere_Dyntab(&sequence);
	
	return output;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeLexique
 * Method:    internalContains
 * Signature: (JLjava/lang/String;[JZDZJZJZJ)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SyntacticAnalyzer_java_SlpToolKit_NativeLexique_internalContains
  (JNIEnv* env, jclass clazz, jlong lexicon, jstring p_graphy, jlongArray p_sequence, jboolean p_proba_def, jdouble p_proba, jboolean p_freq_def, jlong p_freq, jboolean p_lemme_def, jlong p_lemme, jboolean p_pos_def, jlong p_pos)
  
{
	Lexique*				h_lexicon		= (Lexique*)lexicon;
	const long				LEX_SIZE		= SizeLex((Lexique*)lexicon);
	Type_freq*				h_frequency		= NULL;
	Type_proba*				h_probability	= NULL;
	Type_morpho*			h_morpho		= NULL;
	Type_lemme*				h_lemme			= NULL;
	void*					h_c_graphy		= NULL;
	Type_freq				frequency;
	Type_proba				probability;
	Type_lemme				lemme;
	Type_morpho				morpho;
	Booleen					result;
	
	// Extraction de la graphie - obligatoire
	if (p_sequence)
		h_c_graphy = (void*)myJniJlongArrayToArrayOfLong(env, p_sequence);
	
	else if (p_graphy)
		h_c_graphy = (void*)myJniJStringToStringOfChar(env, p_graphy);
	
	
	// Extraction de la fr�quence - optionnelle
	if (p_freq_def)
	{
		frequency = (Type_freq)p_freq;
		h_frequency = &frequency;
	}
	
	// Extraction de la probabilit� - optionnelle
	if (p_proba_def)
	{
		probability = (Type_proba)p_proba;
		h_probability = &probability;
	}
	
	// Extraction du lemme - optionnelle
	if (p_lemme_def)
	{
		lemme = (Type_lemme)p_lemme;
		h_lemme = &lemme;
	}
	
	// Extraction de la cat�gorie morpho-syntaxique - optionnelle	
	if (p_pos_def)
	{
		morpho		= (Type_morpho)p_pos;
		h_morpho	= &morpho;
	}

	
	if (p_graphy)
		result = Dans_Lexique(h_lexicon, h_c_graphy, h_lemme, h_morpho, h_frequency, h_probability);
	
	else
		result = Dans_Lexique_Ulong(h_lexicon, h_c_graphy, h_lemme, h_morpho, h_frequency, h_probability);
	
	free(h_c_graphy);
	
	if (result)
		return JNI_TRUE;
	else
		return JNI_FALSE;
}
