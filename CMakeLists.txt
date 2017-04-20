cmake_minimum_required(VERSION 2.8)
project(WormMeister Java)

find_package(Java REQUIRED)
include(UseJava)

file(GLOB SOURCES "src/*.java")
add_jar(WormMeister ${SOURCES} ENTRY_POINT SnakeJFrame)
