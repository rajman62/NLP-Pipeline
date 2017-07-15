#include "SlpToolKit_NativeCyk_Liste_0005fL1.h"
#include <cyk.h>
#include <malloc.h>

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_Liste_0005fL1
 * Method:    constructor
 * Signature: ()J
 ******************************************************************************/
JNIEXPORT jlong JNICALL Java_SlpToolKit_NativeCyk_00024Liste_1L1_constructor
  (JNIEnv* env, jclass clazz)
{
	Liste_L1** h_h_liste_l1 = (Liste_L1**)malloc(sizeof(Liste_L1*));
	
	*h_h_liste_l1 = (Liste_L1*)malloc(sizeof(Liste_L1));

	return (jlong)h_h_liste_l1;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_Liste_0005fL1
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeCyk_00024Liste_1L1_destructor
  (JNIEnv* env, jclass clazz, jlong native_liste_l1)
{
	Liste_L1** h_h_liste_l1 = (Liste_L1**)native_liste_l1;

	free(*h_h_liste_l1);
	free(h_h_liste_l1);
}