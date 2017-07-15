#include "SlpToolKit_NativeCyk_TableCYK.h"
#include "cyk.h"
#include <malloc.h>

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_TableCYK
 * Method:    caseContientNT
 * Signature: (JJJII)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeCyk_00024TableCYK_caseContientNT
  (JNIEnv* env, jclass clazz, jlong native_table_cyk, jlong native_grammaire, jlong nt, jint i, jint j)
{
	// NON IMPLEMENTÉ	
	return JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_TableCYK
 * Method:    caseContientP
 * Signature: (JJJII)Z
 ******************************************************************************/
JNIEXPORT jboolean JNICALL Java_SlpToolKit_NativeCyk_00024TableCYK_caseContientP
  (JNIEnv* env, jclass clazz, jlong native_table_cyk, jlong native_grammaire, jlong nt, jint i, jint j)
{
	// NON IMPLEMENTÉ
	return JNI_FALSE;
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_TableCYK
 * Method:    destructor
 * Signature: (J)V
 ******************************************************************************/
JNIEXPORT void JNICALL Java_SlpToolKit_NativeCyk_00024TableCYK_destructor
  (JNIEnv* env, jclass clazz, jlong native_table_cyk)
{
	Table_CYK* h_table_cyk = (Table_CYK*)native_table_cyk;

	free(h_table_cyk);
}

/*******************************************************************************
 * Class:     SlpToolKit_NativeCyk_TableCYK
 * Method:    getSize
 * Signature: (J)I
 ******************************************************************************/
JNIEXPORT jint JNICALL Java_SlpToolKit_NativeCyk_00024TableCYK_getSize
  (JNIEnv* env, jclass clazz, jlong native_table_cyk)
{
	Table_CYK* h_table_cyk = (Table_CYK*)native_table_cyk;

	return (jint)h_table_cyk->taille;
}
