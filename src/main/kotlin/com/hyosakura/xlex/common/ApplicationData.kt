package com.hyosakura.xlex.common

import com.hyosakura.regexparser.automata.Converter
import com.hyosakura.regexparser.code.CPPGenerator

object ApplicationData {
    val converter = Converter.DefaultConverter()
    val generator = CPPGenerator()
}