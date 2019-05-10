
cmake_minimum_required(VERSION 3.4.0)

add_library( # Specifies the name of the library.
        libnative_lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        main/native/libnative_lib.cpp)