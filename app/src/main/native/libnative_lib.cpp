//
// Created by ASUSNB on 10.05.2019.
//

#include <jni.h>
#include <string>

std::string getData(int x) {
    std::string app_id = "Null";

    if (x == 1) app_id = "b63def9d6ca05b2808b93be36b2ee119"; //openweathermap api key
    // if (x == 2) app_id = "";
    return app_id;
}


extern "C"
JNIEXPORT jstring

JNICALL
Java_com_cansuaktas_weatherapp_application_App_getApiKey(
        JNIEnv *env,
        jobject /* this */,
        jint x) {
    std::string app_id = "Null";
    app_id = getData(x);
    return env->NewStringUTF(app_id.c_str());
}