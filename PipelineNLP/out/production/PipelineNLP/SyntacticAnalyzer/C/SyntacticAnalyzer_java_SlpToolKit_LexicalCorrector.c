#include "SlpToolKit_LexicalCorrector.h"
#include <jni.h>
#include "corrlex.h"
#include "arbrelex.h"
#include "my_JNI.h"
#include <string.h>
#include <malloc.h>

const char	SEP_CHAR = 13;

const char* LEX_CORRECTOR_ARBRE_LEX_MEMBER		= "arbre_lex";
const char* LEX_CORRECTOR_MAX_DISTANCE_MEMBER	= "max_distance";
const char* LEX_CORRECTOR_ACCENT_WEIGHT_MEMBER	= "accentuation_weight";
const char* LEX_CORRECTOR_SPACE_WEIGHT_MEMBER	= "space_weight";
const char* LEX_CORRECTOR_MODE_MEMBER			= "correction_mode";
const char* LEX_CORRECTOR_MAJ_MIN_WEIGHT_MEMBER = "maj_min_weight";
const char* LEX_CORRECTOR_RETURN_ALL_MEMBER		= "return_all_solutions";
const char* LEX_CORRECTOR_SOL_TO_RETURN_MEMBER  = "solutions_to_return";


const char*	LEX_CORR_CLASS_NAME					= "SlpToolKit/LexicalCorrection";
const char*	LEX_CORR_PART_CLASS_NAME			= "SlpToolKit/LexicalCorrectionPart";

/*******************************************************************************
 * Class:     SlpToolKit_LexicalCorrector
 * Method:    correctWord
 * Signature: (Ljava/lang/String;)[LSlpToolKit/LexicalCorrection;
 ******************************************************************************/
JNIEXPORT jobjectArray JNICALL Java_SlpToolKit_LexicalCorrector_correctWord
  (JNIEnv* env, jobject obj, jstring input) 
{
	Arbre_lexico*		h_arbre_lex = (Arbre_lexico*)JNI_GET_LONG(env, obj, LEX_CORRECTOR_ARBRE_LEX_MEMBER);
	Ensemble_solutions	solution_set;
	int					solution_index;
	jobjectArray		result;
	XCString			buffer;

	// Pour l'instant, cette fonctionnalité n'est valable que pour les instances de Tries
	if (h_arbre_lex == NULL)
		return NULL;
	
	/***************************************************************************
	* Exécution de la correction lexicale avec le module corrlex			   *
	***************************************************************************/
	{
		Type_cout			max_distance	= JNI_GET_DOUBLE(env, obj,		LEX_CORRECTOR_MAX_DISTANCE_MEMBER);
		Type_cout			accent_weight	= JNI_GET_DOUBLE(env, obj,		LEX_CORRECTOR_ACCENT_WEIGHT_MEMBER);
		Type_cout			space_weight	= JNI_GET_DOUBLE(env, obj,		LEX_CORRECTOR_SPACE_WEIGHT_MEMBER);
		Type_cout			maj_min_weight	= JNI_GET_DOUBLE(env, obj,		LEX_CORRECTOR_MAJ_MIN_WEIGHT_MEMBER);
		int					number_of_sol   = JNI_GET_INT(env, obj,			LEX_CORRECTOR_SOL_TO_RETURN_MEMBER);
		const int			CORRECTION_MODE = JNI_GET_INT(env, obj,			LEX_CORRECTOR_MODE_MEMBER);
		const jboolean		ALL_SOLUTION	= JNI_GET_BOOLEAN(env, obj,		LEX_CORRECTOR_RETURN_ALL_MEMBER);
		const char*			GRAPHY			= JNI_GET_STRING(env, input);
		Mode_Correction		mode;

		// Déterminer le mode de correction
		switch (CORRECTION_MODE)
		{
			case 1:		mode = CORRECTION_PONDEREE; break;
			case 2:		mode = CORRECTION_SEPARE; break;
			default:	mode = CORRECTION_BASE; break;
		}

		if (ALL_SOLUTION)
			number_of_sol = -1;
		
		solution_set.nombre = 0;
		solution_set.nombre_alloue = 0;
		solution_set.ensemble = (Solution *) 0;
		
		Correction_Lexico(	GRAPHY, 
							h_arbre_lex, 
							max_distance, 
							&solution_set, 
							number_of_sol, 
							mode, 
							accent_weight, maj_min_weight, space_weight);

		JNI_RELEASE_STRING(env, input, GRAPHY);
	}

	StringInitNull(&buffer);

	// Instancie le tableau de Solution retourné par la méthode
	result = (*env)->NewObjectArray(env, solution_set.nombre, JNI_GET_CLASS(env, LEX_CORR_CLASS_NAME) ,NULL);

	/***************************************************************************
	* Pour chaque solution trouvée, générer une instance de Solution ...       *
	***************************************************************************/
	for (solution_index = 0; solution_index < solution_set.nombre; solution_index++)
	{
		const char		SEP_STRING[] = {SEP_CHAR, 0};
		unsigned int	string_position;
		unsigned int	start_index = 0;
		unsigned int	finish_index;
		Dyntab_pointeur words_dyntab;

		
		Init_Dyntab_pointeur(&words_dyntab);
		
		// Extrait la suite de graphies trouvée pour la solution courante
		StringEmpty(&buffer);
		Solution_Vers_String(solution_set.ensemble[solution_index].constituants, h_arbre_lex, &buffer, SEP_STRING);
		

		// Séparer chaque partie de graphie et les déposer dans le tableau dynamique words_dyntab
		for (string_position = 0; string_position <= strlen(buffer.content); string_position++)
		{
			const char CHARACTER = buffer.content[string_position];

			if ((CHARACTER == SEP_CHAR) || (CHARACTER == 0))
			{
				char*	string;
				int		string_length;
				int		copy_index;

				finish_index = string_position-1;

				string_length = finish_index - start_index + 1;
				string = (char*)malloc(sizeof(char)*(string_length + 1));

				for(copy_index = 0; copy_index < string_length; copy_index++)
					string[copy_index] = buffer.content[copy_index+start_index];

				string[string_length] = 0;

				Augmente_Dyntab_pointeur(string, &words_dyntab); 

				start_index = string_position + 1;
			}
		}
		
		/***********************************************************************
		* Créer l'instance de LexicalCorrection pour la solution courante      *
		***********************************************************************/
		{
			Liste_indices*	pos_codes			= solution_set.ensemble[solution_index].constituants;
			jclass			LEX_CORR_PART_CLASS	= JNI_GET_CLASS(env, LEX_CORR_PART_CLASS_NAME);
			jclass			LEX_CORR_CLASS		= JNI_GET_CLASS(env, LEX_CORR_CLASS_NAME);
			const char*		CONSTRUCTOR_SIG		= "(ILjava/lang/String;)V";
			jmethodID		constructor;
			jdouble			cost;
			int				part_index;

			// Construire le tableau de LexicalCorrectionPart pour la solution courante
			jobjectArray	corr_part_array		= (*env)->NewObjectArray(env, words_dyntab.nb_elements, LEX_CORR_PART_CLASS, NULL);
			
			// Extraire le constructeur de LexicalCorrectionPart
			constructor = (*env)->GetMethodID(env, LEX_CORR_PART_CLASS, "<init>", CONSTRUCTOR_SIG);

			// Instancier chaque élément du tableau de LexicalCorrectionPart
			part_index = 0;
			do {
				jint		string_position	= (pos_codes->pos_chaine < 0)? 0:pos_codes->pos_chaine;
				jstring		graphy			= (*env)->NewStringUTF(env, words_dyntab.tab[part_index]);
				jobject		instance		= (*env)->NewObject(env, LEX_CORR_PART_CLASS, constructor, string_position, graphy);

				(*env)->SetObjectArrayElement(env, corr_part_array, part_index, instance);

				pos_codes = pos_codes->suivant;
				part_index++;
			} while (pos_codes);


			// Extraire le constructeur de LexicalCorrection
			constructor = (*env)->GetMethodID(env, LEX_CORR_CLASS, "<init>", "(D[LSlpToolKit/LexicalCorrectionPart;)V");
			
			cost = solution_set.ensemble[solution_index].cout;

			// Instancie une nouvel object LexicalCorrection et l'assigner à l'index correspondant du tableau retourné
			(*env)->SetObjectArrayElement(	env, 
											result, 
											solution_index, 
											(*env)->NewObject(env, LEX_CORR_CLASS, constructor, cost, corr_part_array));
		}

		// Libération de la mémoire
		Libere_Dyntab(&words_dyntab);
	}
	
	// Libération des allocations mémoires
	Libere_Ensemble_Solutions(&solution_set);
	StringDel(&buffer);
	
	// Retour du résultat
	return result;
}