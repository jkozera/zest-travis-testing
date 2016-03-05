{
  "targets": [
    {
      "target_name": "nodelucene",
      "sources": [ "nodelucene/LuceneIndex.cc" ],
      "libraries": [ "-llucene++", "-llucene++-contrib", "-L/usr/local/lib", "-Wl,-rpath,\\$$ORIGIN/resources" ],
      "xcode_settings": {
        "OTHER_CFLAGS": [
          "-std=c++11", "-stdlib=libc++", "-mmacosx-version-min=10.7", "-fexceptions"
        ],
      },
      "cflags!": [ "-fno-exceptions" ],
      "cflags_cc!": [ "-fno-exceptions" ],
      "include_dirs": [
        "/usr/local/include/lucene++",
        "/usr/local/include",
        "/home/travis/installprefix/include/lucene++"
      ],
    }
  ]
}
