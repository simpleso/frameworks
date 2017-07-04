cmake_minimum_required(VERSION 3.4.1)

find_library( log-lib
              log
            )

add_library( fix
             SHARED
             src/main/jni/DexManager.cpp
           )

target_link_libraries( fix
                       ${log-lib}
                     )

add_library( bspatch
             SHARED
             src/main/cpp/bspatch.c
             src/main/cpp/bzip2/blocksort.c
             src/main/cpp/bzip2/huffman.c
             src/main/cpp/bzip2/crctable.c
             src/main/cpp/bzip2/randtable.c
             src/main/cpp/bzip2/compress.c
             src/main/cpp/bzip2/decompress.c
             src/main/cpp/bzip2/bzlib.c
             src/main/cpp/bspatchnative.c
             )

target_link_libraries( bspatch
                       ${log-lib}
                     )
#set(CMAKE_C_FLAGS "-g -Wall")

add_library( jpeg
             SHARED
             src/main/cpp/jpeg/jaricom.c
             src/main/cpp/jpeg/jcapimin.c
             src/main/cpp/jpeg/jcapistd.c
             src/main/cpp/jpeg/jcarith.c
             src/main/cpp/jpeg/jccoefct.c
             src/main/cpp/jpeg/jccolor.c
             src/main/cpp/jpeg/jcdctmgr.c
             src/main/cpp/jpeg/jchuff.c
             src/main/cpp/jpeg/jcinit.c
             src/main/cpp/jpeg/jcmainct.c
             src/main/cpp/jpeg/jcmarker.c
             src/main/cpp/jpeg/jcmaster.c
             src/main/cpp/jpeg/jcomapi.c
             src/main/cpp/jpeg/jcparam.c
             src/main/cpp/jpeg/jcprepct.c
             src/main/cpp/jpeg/jcsample.c
             src/main/cpp/jpeg/jctrans.c
             src/main/cpp/jpeg/jdapimin.c
             src/main/cpp/jpeg/jdapistd.c
             src/main/cpp/jpeg/jdarith.c
             src/main/cpp/jpeg/jdatadst.c
             src/main/cpp/jpeg/jdatasrc.c
             src/main/cpp/jpeg/jdcoefct.c
             src/main/cpp/jpeg/jdcolor.c
             src/main/cpp/jpeg/jddctmgr.c
             src/main/cpp/jpeg/jdhuff.c
             src/main/cpp/jpeg/jdinput.c
             src/main/cpp/jpeg/jdmainct.c
             src/main/cpp/jpeg/jdmarker.c
             src/main/cpp/jpeg/jdmaster.c
             src/main/cpp/jpeg/jdmerge.c
             src/main/cpp/jpeg/jdpostct.c
             src/main/cpp/jpeg/jdsample.c
             src/main/cpp/jpeg/jdtrans.c
             src/main/cpp/jpeg/jerror.c
             src/main/cpp/jpeg/jfdctflt.c
             src/main/cpp/jpeg/jfdctfst.c
             src/main/cpp/jpeg/jfdctint.c
             src/main/cpp/jpeg/jidctflt.c
             src/main/cpp/jpeg/jidctfst.c
             src/main/cpp/jpeg/jidctint.c
             src/main/cpp/jpeg/jquant1.c
             src/main/cpp/jpeg/jquant2.c
             src/main/cpp/jpeg/jutils.c
             src/main/cpp/jpeg/jmemmgr.c
             src/main/cpp/jpeg/jmemnobs.c
             src/main/cpp/jpeg/cjpeg.c
             src/main/cpp/jpeg/rdppm.c
             src/main/cpp/jpeg/rdgif.c
             src/main/cpp/jpeg/rdtarga.c
             src/main/cpp/jpeg/rdrle.c
             src/main/cpp/jpeg/rdbmp.c
             src/main/cpp/jpeg/rdswitch.c
             src/main/cpp/jpeg/cdjpeg.c
             src/main/cpp/jpeg/djpeg.c
             src/main/cpp/jpeg/wrppm.c
             src/main/cpp/jpeg/wrgif.c
             src/main/cpp/jpeg/wrtarga.c
             src/main/cpp/jpeg/wrrle.c
             src/main/cpp/jpeg/wrbmp.c
             src/main/cpp/jpeg/rdcolmap.c
             src/main/cpp/imageUtils.c
             )

find_library(graphics
            jnigraphics
            )
target_link_libraries( jpeg
                       ${log-lib}
                       ${graphics}
                     )


